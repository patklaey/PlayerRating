package football.scd.playerrating;

import java.util.HashMap;
import java.util.List;

import football.scd.playerrating.ListView.PlayerRatingPlayerListView;
import football.scd.playerrating.ListView.RatingsOfGameListViewAdapter;
import football.scd.playerrating.contents.GamesContent;
import football.scd.playerrating.contents.PlayersContent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
@SuppressWarnings("unchecked")
public class GameActivity extends Activity
{
	public static final String EXTRA_PLAYED_LIST = "football.scd.playerrating.GameActivity.Played_List";
	public static final String EXTRA_GAME_ID = "football.scd.playerrating.GameActivity.Game_ID";
	public static final String EXTRA_GAME = "football.scd.playerrating.GameActivity.Game";
	public static final String EXTRA_GAME_TIME = "football.scd.playerrating.GameActivity.Game_Time";
	private static final int SELF_GOAL_SCORED_REQUEST_CODE = 1;
	private static final int EDIT_HOME_GOAL_REQUEST_CODE = 2;
	private static final int EDIT_AWAY_GOAL_REQUEST_CODE = 3;
	public static int current_game_id;
	
	
	private Game game;
	private boolean new_game;
	private Chronometer chrono;
	private long chrono_pause_base;
	private boolean first_half = true;
	private boolean editing_game = false;
	public static HashMap<Integer, Player> played;
	
	// The goal list adapters
	private ArrayAdapter<Goal> home_goal_adapter;
	private ArrayAdapter<Goal> away_goal_adapter;
	
	// Wake lock
	PowerManager power_manager = null;
	WakeLock wake_lock = null;
	private static final String WAKE_LOCK_NAME = "MyWakeLock";
	
	@Override
	public void onBackPressed() 
	{
		if ( this.game.isFinished() || this.wake_lock == null )
			super.onBackPressed();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		
		// Request portrait orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// Get the intent
		Intent intent = getIntent();
		
		if ( intent.getStringExtra(MainActivity.EXTRA_TYPE) != null && 
			 intent.getStringExtra(MainActivity.EXTRA_TYPE).equals(MainActivity.EXTRA_TYPE_NEW) )
		{
			setContentView(R.layout.new_game_activity);
			this.new_game = true;
		} else
		{
			// Set the layout
			setContentView(R.layout.activity_game);

			// Set the fields
			GameActivity.current_game_id = intent.getIntExtra(MainActivity.EXTRA_GAME_ID, 0);
			this.game = GamesContent.getGameById(GameActivity.current_game_id);

			// Set the text fields accordingly
			if ( this.game.isHomeGame() )
			{
				((TextView)findViewById(R.id.away_team_name)).setText(this.game.getOpponent());
				((TextView)findViewById(R.id.home_team_name)).setText(this.game.getSelf_name());
				((TextView)findViewById(R.id.home_team_score)).setText("" + this.game.getSelf_score());
				((TextView)findViewById(R.id.away_team_score)).setText("" + this.game.getOpponent_score());
								
				Log.d("Goals scored:", "" + this.game.getGoalsScored());
				
				// The home goal adapter
				this.home_goal_adapter = new ArrayAdapter<Goal>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.game.getGoalsScored() );
				((ListView)findViewById(R.id.home_goal_list_view)).setAdapter(this.home_goal_adapter);
				
				// The away goal adapter
				this.away_goal_adapter = new ArrayAdapter<Goal>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.game.getGoalsConceded() );
				((ListView)findViewById(R.id.away_goal_list_view)).setAdapter(this.away_goal_adapter);
				
			} else
			{
				((TextView)findViewById(R.id.away_team_name)).setText(this.game.getSelf_name());
				((TextView)findViewById(R.id.home_team_name)).setText(this.game.getOpponent());
				((TextView)findViewById(R.id.home_team_score)).setText("" + this.game.getOpponent_score());
				((TextView)findViewById(R.id.away_team_score)).setText("" + this.game.getSelf_score());
				
				// The home goal adapter
				this.home_goal_adapter = new ArrayAdapter<Goal>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.game.getGoalsConceded() );
				((ListView)findViewById(R.id.home_goal_list_view)).setAdapter(this.home_goal_adapter);
				
				// The away goal adapter
				this.away_goal_adapter = new ArrayAdapter<Goal>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.game.getGoalsScored() );
				((ListView)findViewById(R.id.away_goal_list_view)).setAdapter(this.away_goal_adapter);
			}
			
			((ListView)findViewById(R.id.away_goal_list_view)).setOnItemClickListener(edit_away_goal_list_listener);
			((ListView)findViewById(R.id.home_goal_list_view)).setOnItemClickListener(edit_home_goal_list_listener);
			
			// If the game is finished, we don't want to show start game and 
			// substitution button
			if ( this.game.isFinished() )
			{
				((Button)findViewById(R.id.start_end_game_button)).setVisibility(View.INVISIBLE);
				((Button)findViewById(R.id.substitution_button)).setVisibility(View.INVISIBLE);
				((TextView)findViewById(R.id.half_time_text)).setText("Finished");
				findViewById(R.id.game_minutes_played).setVisibility(View.INVISIBLE);
				((Button)findViewById(R.id.show_ratings_button)).setVisibility(View.VISIBLE);
			}
			
			// Assign chronometer
			this.chrono = (Chronometer)findViewById(R.id.game_minutes_played);
			
			// Create an empty currently playing list
			played = new HashMap<Integer, Player>();
		}
				
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() 
	{
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		
		if ( this.new_game )
		{
			menu.findItem(R.id.action_edit).setVisible(false);
			return true;
		}
		
		if ( !this.game.isFinished() )
			menu.findItem(R.id.action_edit).setVisible(false);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				// NavUtils.navigateUpFromSameTask(this);
				return true;
				
			case R.id.action_edit:
				
				// Edit the current game
				findViewById(R.id.save_game_button).setVisibility(View.VISIBLE);
				findViewById(R.id.increase_home_team_score).setVisibility(View.VISIBLE);
				findViewById(R.id.increase_away_team_score).setVisibility(View.VISIBLE);
				findViewById(R.id.decrease_home_team_score).setVisibility(View.VISIBLE);
				findViewById(R.id.decrease_away_team_score).setVisibility(View.VISIBLE);
				findViewById(R.id.start_end_game_button).setVisibility(View.INVISIBLE);
				findViewById(R.id.substitution_button).setVisibility(View.INVISIBLE);
				findViewById(R.id.cancel_edit_game_button).setVisibility(View.VISIBLE);
				findViewById(R.id.delete_game_button).setVisibility(View.VISIBLE);
				findViewById(R.id.show_ratings_button).setVisibility(View.INVISIBLE);
				
				this.editing_game = true;
				
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	
	// Finish game
	public void finishGame()
	{
		// Set the game finished
		this.game.setFinished(true);
				
		// Rate all players
		Intent intent = new Intent(this,RatePlayers.class);
		intent.putExtra(GameActivity.EXTRA_PLAYED_LIST, GameActivity.played);
		intent.putExtra(GameActivity.EXTRA_GAME_ID, this.game.getID());
		this.startActivity(intent);
		
		// Update the current game
		this.updateGame(null);
		
		// Release the wake lock
		this.wake_lock.release();
	}
	
	// Start the game
	public void startGame(View view)
	{
		// If the buttons text is finish game, then call the finishGame method
		if ( ((Button)view).getText().equals(this.getString(R.string.finish_game)) )
		{
			this.finishGame();
			return;
		}
		
		// If the buttons text is finish game, then call the finishGame method
		if ( ((Button)view).getText().equals(this.getString( R.string.start_game) ) )
		{
			this.power_manager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
			this.wake_lock = this.power_manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, GameActivity.WAKE_LOCK_NAME);
			this.wake_lock.acquire();
		}
		
		// If the button text is "Continue", then set second half
		if ( ((Button)view).getText().equals(this.getString(R.string.continue_string) ) )
		{
			this.setFirstHalf(false);
		}
			
		// Set up the chronometer
		this.chrono.setBase( SystemClock.elapsedRealtime() );
		this.chrono.setOnChronometerTickListener( new GameChronometer(this) );
		
		// Disable the start/end game button
		this.findViewById(R.id.start_end_game_button).setEnabled(false);
		
		// Make the increase score buttons visible
		findViewById(R.id.increase_away_team_score).setVisibility(View.VISIBLE);
		findViewById(R.id.increase_home_team_score).setVisibility(View.VISIBLE);
		
		// Start the chronometer
		this.chrono.start();
	}
	
	// Perform one or multiple substitutions
	public void substitution(View view)
	{
		// Enable the start game button
		if ( ((Button)findViewById(R.id.substitution_button)).getText().equals("Start 11") )
		{
			((Button)findViewById(R.id.start_end_game_button)).setEnabled(true);
			((Button)findViewById(R.id.substitution_button)).setText(R.string.substitution);
		}
		
		Intent intent = new Intent(this,Substitution.class);
		this.startActivity(intent);
	}
	
	// Delete the game 
	public void deleteGame(View view)
	{
		new AlertDialog.Builder(this)
	    .setTitle("Delete Game")
	    .setMessage("Are you sure you want to delete this game with all its goals, minutes and ratings?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int which)
	        {
	    		// Delete the game locally
	    		GamesContent.removeGame( GameActivity.current_game_id );
	    		
	    		// Delete the game from the backend
	    		MainActivity.getBackend().removeGame( GameActivity.current_game_id );
	    		
	    		finish();
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int which)
	        { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
	}
	
	// Cancle creating new game
	public void cancel(View view)
	{
		// Simply finish the current view
		finish();
	}
	
	// Cancel edit game
	public void cancelEdit(View view)
	{
		findViewById(R.id.save_game_button).setVisibility(View.INVISIBLE);
		findViewById(R.id.increase_home_team_score).setVisibility(View.INVISIBLE);
		findViewById(R.id.increase_away_team_score).setVisibility(View.INVISIBLE);
		findViewById(R.id.decrease_home_team_score).setVisibility(View.INVISIBLE);
		findViewById(R.id.decrease_away_team_score).setVisibility(View.INVISIBLE);
		findViewById(R.id.cancel_edit_game_button).setVisibility(View.INVISIBLE);
		findViewById(R.id.delete_game_button).setVisibility(View.INVISIBLE);
		
		if ( !this.game.isFinished() )
		{
			findViewById(R.id.start_end_game_button).setVisibility(View.VISIBLE);
			findViewById(R.id.substitution_button).setVisibility(View.VISIBLE);
		} else
		{
			findViewById(R.id.show_ratings_button).setVisibility(View.VISIBLE);
		}
		
		this.editing_game = false;
	}
	
	// Save the new game
	public void saveGame(View view)
	{
		int game_ID = MainActivity.next_free_game_id++;
		String my_team_name = MainActivity.getSettings().getTeamName();
		String opponent = ((EditText)findViewById(R.id.opponent_name)).getEditableText().toString();
		boolean is_home = ((CheckBox)findViewById(R.id.is_home_game)).isChecked();
		
		// Create a new game
		Game game = new Game( game_ID, my_team_name, opponent, is_home );
		
		// Save the game locally
		GamesContent.addGame(game);

		// Save the game to the backend
		MainActivity.getBackend().createGame(game);
		
		this.editing_game = false;
		finish();
	}

	public void increaseHomeScore(View view)
	{			
		// If it is a home game, select the scorer
		if ( this.game.isHomeGame() )
		{
			Intent scorer = new Intent(this,SelectPlayer.class);
			this.startActivityForResult(scorer, GameActivity.SELF_GOAL_SCORED_REQUEST_CODE);
		} else
		{
    		// Get the current minute
    		int minute = Integer.parseInt((this.chrono.getText().toString().split(":"))[0]);
    				
    		// Check if it is the second half
    		if ( ! this.first_half )
    			minute += MainActivity.getSettings().getHalfTimeDuration();
    		
    		// Add one to the minutes
    		minute++;
    		
			// Otherwise just add the minute and a dummy player
			int id = MainActivity.next_free_goal_id++;
			this.game.getGoalsConceded().add(new Goal(id, minute, MainActivity.GOAL_AGAINS_PLAYER.getID() , this.game.getID() ) );
			((ArrayAdapter<Goal>)((ListView)findViewById(R.id.away_goal_list_view)).getAdapter()).notifyDataSetChanged();
			this.game.setOpponent_score( this.game.getOpponent_score() + 1 );
			
			// Get the text of the home score field
			TextView home_score_text = (TextView)findViewById(R.id.home_team_score);
			int home_score = Integer.parseInt((String)home_score_text.getText());
			
			// Set the text of the home score field to home_score + 1
			home_score_text.setText( "" + (home_score + 1));
		}
	}
	
	public void increaseAwayScore(View view)
	{		
		// If it is not a home game, select the scorer
		if ( ! this.game.isHomeGame() )
		{
			Intent scorer = new Intent(this,SelectPlayer.class);
			this.startActivityForResult(scorer, GameActivity.SELF_GOAL_SCORED_REQUEST_CODE);
		} else
		{
			// Get the current minute
			int minute = Integer.parseInt((this.chrono.getText().toString().split(":"))[0]);
					
			// Check if it is the second half
			if ( ! this.first_half )
				minute += MainActivity.getSettings().getHalfTimeDuration();
			
			// Add one to the minutes
			minute++;
			
			// Otherwise just add the minute and a dummy player
			int id = MainActivity.next_free_goal_id++;
			this.game.getGoalsConceded().add(new Goal(id, minute, MainActivity.GOAL_AGAINS_PLAYER.getID(), this.game.getID() ));
			((ArrayAdapter<Goal>)((ListView)findViewById(R.id.away_goal_list_view)).getAdapter()).notifyDataSetChanged();
			this.game.setOpponent_score( this.game.getOpponent_score() + 1 );
			
			// Get the text of the home score field
			TextView away_score_text = (TextView)findViewById(R.id.away_team_score);
			int away_score = Integer.parseInt((String)away_score_text.getText());
			
			// Set the text of the home score field to home_score + 1
			away_score_text.setText( "" + (away_score + 1));
		}
	}
	
	public void decreaseHomeScore(View view)
	{
		// Get the text of the home score field
		TextView home_score_text = (TextView)findViewById(R.id.home_team_score);
		int home_score = Integer.parseInt((String)home_score_text.getText());
		
		// Set the text of the home score field to home_score - 1
		home_score_text.setText( "" + (home_score - 1));
		
		// Set the games home score to - 1
		if ( this.game.isHomeGame() )
		{
			this.game.setSelf_score( this.game.getSelf_score() - 1 );
		} else
		{
			this.game.setOpponent_score( this.game.getOpponent_score() - 1 );
		}
	}
	
	public void decreaseAwayScore(View view)
	{
		// Get the text of the home score field
		TextView away_score_text = (TextView)findViewById(R.id.away_team_score);
		int away_score = Integer.parseInt((String)away_score_text.getText());
		
		// Set the text of the home score field to home_score - 1
		away_score_text.setText( "" + (away_score - 1));
		
		// Set the games away score to - 1
		if ( ! this.game.isHomeGame() )
		{
			this.game.setSelf_score( this.game.getSelf_score() - 1 );
		} else
		{
			this.game.setOpponent_score( this.game.getOpponent_score() - 1 );
		}
	}
	
	public void updateGame(View view)
	{	
		// Update the game locally
		GamesContent.updateGame(this.game);
		
		// Update the game in the database
		MainActivity.getBackend().updateGame(this.game);
		
		// Update all players to not lose the minutes and ratings value for this game
		PlayersContent.updateAllPlayers();
		
		finish();
	}
	
	public void showRatings(View view)
	{
		// Create a new intent
		Intent intent = new Intent(this, PlayerRatingPlayerListView.class);
		intent.putExtra(MainActivity.EXTRA_STATS_TYPE, new RatingsOfGameListViewAdapter( this.game.getID() ) );
		this.startActivity(intent);
	}
	
	/**
	 * @return the chrono_pause_base
	 */
	public long getChronoPauseBase()
	{
		return chrono_pause_base;
	}

	/**
	 * @param chrono_pause_base the chrono_pause_base to set
	 */
	public void setChronoPauseBase(long chrono_pause_base)
	{
		this.chrono_pause_base = chrono_pause_base;
	}

	/**
	 * @return the first_half
	 */
	public boolean isFirstHalf() 
	{
		return first_half;
	}

	/**
	 * @param first_half the first_half to set
	 */
	public void setFirstHalf(boolean first_half)
	{
		this.first_half = first_half;
	}

	private class GameChronometer implements OnChronometerTickListener
	{
		private GameActivity activity;
		private int current_minute;
		
		public GameChronometer(GameActivity activity)
		{
			this.activity = activity;
			this.current_minute = 0;
		}
		
		@Override
		public void onChronometerTick(Chronometer chronometer)
		{		
			// Get the current minute
			String array[] = chronometer.getText().toString().split(":");
			int minute = Integer.parseInt(array[0]);
			
			// If no minute passed, return
			if ( minute == this.current_minute )
				return;
				
			// If the time is over
			if ( minute == MainActivity.getSettings().getHalfTimeDuration() )
			{
				// Stop the chronometer
				chronometer.stop();
				
				// Set the according button text
				if ( this.activity.isFirstHalf() )
				{
					((Button)this.activity.findViewById(R.id.start_end_game_button)).setText(R.string.continue_string);
					((TextView)this.activity.findViewById(R.id.half_time_text)).setText(R.string.second_half);
				} else
				{
					((Button)this.activity.findViewById(R.id.start_end_game_button)).setText(R.string.finish_game);
				}
				
				this.activity.findViewById(R.id.start_end_game_button).setEnabled(true);
			}
			
			// Otherwise calculate the minute offset
			int offset = minute - this.current_minute;
			this.current_minute = minute;

			// Add the offset to all players which are currently playing
			for (Player player : PlayersContent.getAllPlayers())
				if ( player.isPlaying() )
					player.setCurrentGameMinutes( player.getCurrentGameMinutes() + offset );
		}
		
	}
	
    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data)
    {
    	super.onActivityResult(request_code, result_code, data);
    	
        // If it was a SELF_GOAL_SCORED activity and it returned ok, add the
    	// goal to the goals scored list
        if ( request_code == GameActivity.SELF_GOAL_SCORED_REQUEST_CODE && result_code == RESULT_OK )
        {
        	Player returned = (Player) data.getSerializableExtra(SelectPlayer.EXTRA_PLAYER);
        	
        	// Get the player from the map to not edit a copy of the player
        	Player player = PlayersContent.getPlayerById(returned.getID());
    		player.setCurrentGameGoals( player.getCurrentGameGoals() + 1 );
    		
    		// Get the current minute
    		int minute = Integer.parseInt((this.chrono.getText().toString().split(":"))[0]);
    				
    		// Check if it is the second half
    		if ( ! this.first_half )
    			minute += MainActivity.getSettings().getHalfTimeDuration();
    		
    		// Add one to the minutes
    		minute++;
    		
    		// Create a new goal object to return
    		Goal goal = new Goal(MainActivity.next_free_goal_id++, minute, player.getID(), this.game.getID() );
    		
    		// Add the goal to the player
    		player.addGoal(goal);
        	
        	this.game.addGoal(goal);
			((ArrayAdapter<Goal>)((ListView)findViewById(R.id.home_goal_list_view)).getAdapter()).notifyDataSetChanged();
			this.game.setSelf_score( this.game.getSelf_score() + 1 );
			
			// And update the text field
			if ( this.game.isHomeGame() )
			{
				// Get the text of the home score field
				TextView home_score_text = (TextView)findViewById(R.id.home_team_score);
				int home_score = Integer.parseInt((String)home_score_text.getText());
				
				// Set the text of the home score field to home_score + 1
				home_score_text.setText( "" + (home_score + 1));
			} else
			{
				// Get the text of the home score field
				TextView away_score_text = (TextView)findViewById(R.id.away_team_score);
				int away_score = Integer.parseInt((String)away_score_text.getText());
				
				// Set the text of the home score field to home_score + 1
				away_score_text.setText( "" + (away_score + 1));
			}
        }
        
        if ( request_code == GameActivity.EDIT_AWAY_GOAL_REQUEST_CODE && result_code == Activity.RESULT_OK )
        {
        	Goal edited_goal = (Goal) data.getSerializableExtra(EditPlayerProperty.EXTRA_PROPERTY);
        	
        	List<Goal> goals_in_list = this.game.getGoalsScored();
        	if ( this.game.isHomeGame() )
        		goals_in_list = this.game.getGoalsConceded();
        	
        	for (Goal current_goal : goals_in_list ) {
				if ( current_goal.getID() == edited_goal.getID() ) {
					current_goal.setMinute( edited_goal.getMinute() );
					current_goal.setPlayerId( edited_goal.getPlayerId() );
				}
			}
        	
        	Player player_who_scored = PlayersContent.getPlayerById(edited_goal.getPlayerId());
        	PlayersContent.updatePlayer(player_who_scored);
        	MainActivity.getBackend().updatePlayer(player_who_scored);
        	
        	this.away_goal_adapter.notifyDataSetChanged();
        }
        
        if ( request_code == GameActivity.EDIT_HOME_GOAL_REQUEST_CODE && result_code == Activity.RESULT_OK )
        {
        	Goal edited_goal = (Goal) data.getSerializableExtra(EditPlayerProperty.EXTRA_PROPERTY);
        	
        	List<Goal> goals_in_list = this.game.getGoalsConceded();
        	if ( this.game.isHomeGame() )
        		goals_in_list = this.game.getGoalsScored();
        	
        	for (Goal current_goal : goals_in_list ) {
				if ( current_goal.getID() == edited_goal.getID() ) {
					current_goal.setMinute( edited_goal.getMinute() );
					current_goal.setPlayerId( edited_goal.getPlayerId() );
				}
			}
        	
        	Player player_who_scored = PlayersContent.getPlayerById(edited_goal.getPlayerId());
        	PlayersContent.updatePlayer(player_who_scored);
        	MainActivity.getBackend().updatePlayer(player_who_scored);
        	
        	this.home_goal_adapter.notifyDataSetChanged();
        }
    }
    
    OnItemClickListener edit_away_goal_list_listener = new OnItemClickListener() {
        
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
    		if ( ! GameActivity.this.editing_game )
    			return;
    		
    		Goal goal_to_edit;
    		if ( GameActivity.this.game.isHomeGame() )
    			goal_to_edit = GameActivity.this.game.getGoalsConceded().get(position);
    		else
    			goal_to_edit = GameActivity.this.game.getGoalsScored().get(position);
    		
    		// Start the player activity
    		Intent intent = new Intent(GameActivity.this, EditGoal.class);
    		intent.putExtra(EditPlayerProperty.EXTRA_PROPERTY, goal_to_edit);
    		GameActivity.this.startActivityForResult(intent, GameActivity.EDIT_AWAY_GOAL_REQUEST_CODE);			
		}
	};
	
    OnItemClickListener edit_home_goal_list_listener = new OnItemClickListener() {
        
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
    		if ( ! GameActivity.this.editing_game )
    			return;
    		
    		Goal goal_to_edit;
    		if ( GameActivity.this.game.isHomeGame() )
    			goal_to_edit = GameActivity.this.game.getGoalsScored().get(position);
    		else
    			goal_to_edit = GameActivity.this.game.getGoalsConceded().get(position);
    		
    		// Start the player activity
    		Intent intent = new Intent(GameActivity.this, EditGoal.class);
    		intent.putExtra(EditPlayerProperty.EXTRA_PROPERTY, goal_to_edit);
    		GameActivity.this.startActivityForResult(intent, GameActivity.EDIT_HOME_GOAL_REQUEST_CODE);			
		}
	};
}

package football.scd.playerrating;

import java.util.HashMap;
import football.scd.playerrating.contents.GamesContent;
import football.scd.playerrating.contents.PlayersContent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
	private static final int SELF_GOAL_SCORED = 1;
	
	private Game game;
	private boolean new_game;
	private Chronometer chrono;
	private long chrono_pause_base;
	private boolean first_half = true;
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
			this.game = (Game) intent.getSerializableExtra(MainActivity.EXTRA_GAME);
			
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
			
			// If the game is finished, we don't want to show start game and 
			// substitution button
			if ( this.game.isFinished() )
			{
				((Button)findViewById(R.id.start_end_game_button)).setVisibility(View.INVISIBLE);
				((Button)findViewById(R.id.substitution_button)).setVisibility(View.INVISIBLE);
				((TextView)findViewById(R.id.half_time_text)).setText("Finished");
				findViewById(R.id.game_minutes_played).setVisibility(View.INVISIBLE);
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
		if ( ((Button)view).getText().equals("Finish Game") )
		{
			this.finishGame();
			return;
		}
		
		// If the buttons text is finish game, then call the finishGame method
		if ( ((Button)view).getText().equals("Start Game") )
		{
			this.power_manager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
			this.wake_lock = this.power_manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, GameActivity.WAKE_LOCK_NAME);
			this.wake_lock.acquire();
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
			((Button)findViewById(R.id.substitution_button)).setText(R.string.Substitution);
		}
		
		Intent intent = new Intent(this,Substitution.class);
		this.startActivity(intent);
	}
	
	// Delete the game 
	public void deleteGame(View view)
	{
		// Delete the game locally
		GamesContent.removeGame(this.game.getID());
		
		// Delete the game from the backend
		MainActivity.getBackend().removeGame(this.game.getID());
		
		finish();
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
		}
	}
	
	// Save the new game
	public void saveGame(View view)
	{
		int game_ID = MainActivity.next_free_game_id++;
		String my_team_name = MainActivity.MY_TEAM_NAME;
		String opponent = ((EditText)findViewById(R.id.opponent_name)).getEditableText().toString();
		boolean is_home = ((CheckBox)findViewById(R.id.is_home_game)).isChecked();
		
		// Create a new game
		Game game = new Game( game_ID, my_team_name, opponent, is_home );
		
		// Save the game locally
		GamesContent.addGame(game);

		// Save the game to the backend
		MainActivity.getBackend().createGame(game);
		
		finish();
	}

	public void increaseHomeScore(View view)
	{	
		// Get the current minute
		int minute = Integer.parseInt((this.chrono.getText().toString().split(":"))[0]);
				
		// Check if it is the second half
		if ( ! this.first_half )
			minute += MainActivity.HALF_TIME_DURATION;
		
		// Add one to the minutes
		minute++;
				
		// If it is a home game, select the scorer
		if ( this.game.isHomeGame() )
		{
			Intent scorer = new Intent(this,GoalScorer.class);
			scorer.putExtra(GameActivity.EXTRA_GAME_TIME, minute);
			scorer.putExtra(GameActivity.EXTRA_GAME_ID, this.game.getID() );
			this.startActivityForResult(scorer, GameActivity.SELF_GOAL_SCORED);
		} else
		{
			// Otherwise just add the minute and a dummy player
			int id = MainActivity.next_free_goal_id++;
			this.game.getGoalsConceded().add(new Goal(id, minute, MainActivity.GOAL_AGAINS_PLAYER , this.game.getID() ) );
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
		// Get the current minute
		int minute = Integer.parseInt((this.chrono.getText().toString().split(":"))[0]);
				
		// Check if it is the second half
		if ( ! this.first_half )
			minute += MainActivity.HALF_TIME_DURATION;
		
		// Add one to the minutes
		minute++;
		
		// If it is not a home game, select the scorer
		if ( ! this.game.isHomeGame() )
		{
			Intent scorer = new Intent(this,GoalScorer.class);
			scorer.putExtra(GameActivity.EXTRA_GAME_TIME, minute );
			scorer.putExtra(GameActivity.EXTRA_GAME_ID, this.game.getID() );
			this.startActivityForResult(scorer, GameActivity.SELF_GOAL_SCORED);
		} else
		{
			// Otherwise just add the minute and a dummy player
			int id = MainActivity.next_free_goal_id++;
			this.game.getGoalsConceded().add(new Goal(id, minute, MainActivity.GOAL_AGAINS_PLAYER, this.game.getID() ));
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
		
		finish();
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
			if ( minute == MainActivity.HALF_TIME_DURATION )
			{
				// Stop the chronometer
				chronometer.stop();
				
				// Set the accoring button text
				if ( this.activity.isFirstHalf() )
				{
					((Button)this.activity.findViewById(R.id.start_end_game_button)).setText(R.string.Continue);
					this.activity.setFirstHalf(false);
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
			for (Player player : PlayersContent.PLAYERS)
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
        if ( request_code == GameActivity.SELF_GOAL_SCORED && result_code == RESULT_OK )
        {
        	Goal goal = (Goal) data.getSerializableExtra(GoalScorer.EXTRA_GOAL);
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
    }
}

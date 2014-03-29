package football.scd.playerrating;

import java.util.HashMap;

import football.scd.playerrating.contents.GamesContent;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

@SuppressLint("UseSparseArrays")
public class GameActivity extends Activity {
	
	private String opponent_name;
	private String self_name;
	private int self_goals;
	private int opponent_goals;
	private boolean is_home_game;
	private int game_ID;
	private boolean new_game;
	
	public static HashMap<Integer, Player> currenty_playing;
	public static HashMap<Integer, Player> played;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		
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
			this.game_ID = intent.getIntExtra(MainActivity.EXTRA_ID, -1);
			this.is_home_game = intent.getBooleanExtra(MainActivity.EXTRA_IS_HOME_GAME, true);
			this.opponent_goals = intent.getIntExtra(MainActivity.EXTRA_OPPONENT_SCORE, 0);
			this.self_goals = intent.getIntExtra(MainActivity.EXTRA_SELF_SCORE, 0);
			this.self_name = intent.getStringExtra(MainActivity.EXTRA_SELF_NAME);
			this.opponent_name = intent.getStringExtra(MainActivity.EXTRA_OPPONENT);
			
			// Set the text fields accordingly
			if ( this.is_home_game )
			{
				((TextView)findViewById(R.id.away_team_name)).setText(this.opponent_name);
				((TextView)findViewById(R.id.home_team_name)).setText(this.self_name);
				((TextView)findViewById(R.id.home_team_score)).setText("" + this.self_goals);
				((TextView)findViewById(R.id.away_team_score)).setText("" + this.opponent_goals);
			} else
			{
				((TextView)findViewById(R.id.away_team_name)).setText(this.self_name);
				((TextView)findViewById(R.id.home_team_name)).setText(this.opponent_name);
				((TextView)findViewById(R.id.home_team_score)).setText("" + this.opponent_goals);
				((TextView)findViewById(R.id.away_team_score)).setText("" + this.self_goals);
			}
			
			// Create an empty currently playing list
			currenty_playing = new HashMap<Integer, Player>();
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
				NavUtils.navigateUpFromSameTask(this);
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
	
	// Perform one or multiple substitutions
	public void substitution(View view)
	{
		Intent intent = new Intent(this,Substitution.class);
		this.startActivity(intent);
	}
	
	// Delete the game 
	public void deleteGame(View view)
	{
		// Delete the game locally
		GamesContent.removeGame(this.game_ID);
		
		// Notify the game adapter that the data has changed
		GamesFragment.updateList();
		
		// Delete the game from the backend
		MainActivity.getBackend().removeGame(this.game_ID);
		
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
		findViewById(R.id.start_end_game_button).setVisibility(View.VISIBLE);
		findViewById(R.id.substitution_button).setVisibility(View.VISIBLE);
		findViewById(R.id.cancel_edit_game_button).setVisibility(View.INVISIBLE);
		findViewById(R.id.delete_game_button).setVisibility(View.INVISIBLE);
	}
	
	// Save the new game
	public void saveGame(View view)
	{
		this.game_ID = MainActivity.next_free_game_id++;
		String my_team_name = MainActivity.MY_TEAM_NAME;
		String opponent = ((EditText)findViewById(R.id.opponent_name)).getEditableText().toString();
		boolean is_home = ((CheckBox)findViewById(R.id.is_home_game)).isChecked();
		
		// Create a new game
		Game game = new Game( this.game_ID, my_team_name, opponent, is_home );
		
		// Save the game locally
		GamesContent.addGame(game);
		
		// Notify the game adapter that the data has changed
		GamesFragment.updateList();
		
		// Save the game to the backend
		MainActivity.getBackend().createGame(game);
		
		finish();
	}

	public void increaseHomeScore(View view)
	{
		// Get the text of the home score field
		TextView home_score_text = (TextView)findViewById(R.id.home_team_score);
		int home_score = Integer.parseInt((String)home_score_text.getText());
		
		// Set the text of the home score field to home_score + 1
		home_score_text.setText( "" + (home_score + 1));
	}
	
	public void increaseAwayScore(View view)
	{
		// Get the text of the home score field
		TextView away_score_text = (TextView)findViewById(R.id.away_team_score);
		int away_score = Integer.parseInt((String)away_score_text.getText());
		
		// Set the text of the home score field to home_score + 1
		away_score_text.setText( "" + (away_score + 1));
	}
	
	public void decreaseHomeScore(View view)
	{
		// Get the text of the home score field
		TextView home_score_text = (TextView)findViewById(R.id.home_team_score);
		int home_score = Integer.parseInt((String)home_score_text.getText());
		
		// Set the text of the home score field to home_score - 1
		home_score_text.setText( "" + (home_score - 1));
	}
	
	public void decreaseAwayScore(View view)
	{
		// Get the text of the home score field
		TextView away_score_text = (TextView)findViewById(R.id.away_team_score);
		int away_score = Integer.parseInt((String)away_score_text.getText());
		
		// Set the text of the home score field to home_score - 1
		away_score_text.setText( "" + (away_score - 1));
	}
	
	public void updateGame(View view)
	{
		// Get the text of the home score field
		TextView home_score_text = (TextView)findViewById(R.id.home_team_score);
		int home_score = Integer.parseInt((String)home_score_text.getText());
		
		// Get the text of the home score field
		TextView away_score_text = (TextView)findViewById(R.id.away_team_score);
		int away_score = Integer.parseInt((String)away_score_text.getText());
		
		// Create a new game with the ID of the old game
		Game game = new Game(this.game_ID, this.self_name, this.opponent_name, this.is_home_game);
		
		// Set home and away goals
		if ( this.is_home_game )
		{
			game.setSelf_goals(home_score);
			game.setOpponent_goals(away_score);
		} else
		{
			game.setSelf_goals(away_score);
			game.setOpponent_goals(home_score);
		}
		
		// Update the game locally
		GamesContent.updateGame(game);
		
		// Update the game in the database
		MainActivity.getBackend().updateGame(game);
		
		finish();
	}
}

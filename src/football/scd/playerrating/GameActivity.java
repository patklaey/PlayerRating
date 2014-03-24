package football.scd.playerrating;

import football.scd.playerrating.contents.GamesContent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class GameActivity extends Activity {
	
	private String opponent_name;
	private String self_name;
	private int self_goals;
	private int opponent_goals;
	private boolean is_home_game;
	private int game_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		
		// Get the intent
		Intent intent = getIntent();
		
		if ( intent.getStringExtra(MainActivity.EXTRA_TYPE).equals(MainActivity.EXTRA_TYPE_NEW) )
		{
			setContentView(R.layout.new_game_activity);
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
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
		}
		return super.onOptionsItemSelected(item);
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
		
		// Save the game to the backend
		MainActivity.getBackend().createGame(game);
		
		finish();
	}

}

package football.scd.playerrating;

import football.scd.playerrating.contents.PlayersContent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.NavUtils;
import android.text.Editable;

public class PlayerActivity extends Activity {

	// Define fields used for player
	private String player_name;
	private String player_givenname;
	private int player_id;
	private int player_goals;
	private int player_minutes;
	private float player_rating;
	private boolean new_player = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		// Get the intent
		Intent intent = getIntent();
		
		// Get the intent type extra
		String extra_type = intent.getStringExtra(MainActivity.EXTRA_TYPE);
		
		// If the extra is TYPE_NEW, then we want to create a new player
		if ( extra_type.equals(MainActivity.EXTRA_TYPE_NEW))
		{
			// Enable the name fields and the save button
			((EditText)findViewById(R.id.player_edit_name)).setEnabled(true);
			((EditText)findViewById(R.id.player_edit_givenname)).setEnabled(true);
			((Button)findViewById(R.id.save_player_button)).setEnabled(true);
			
			// Disable the edit button
			((Button)findViewById(R.id.edit_player_button)).setEnabled(false);
			
			// Remember that it is a new player
			this.new_player = true;
			
		} else if ( extra_type.equals(MainActivity.EXTRA_TYPE_SHOW) )
		{
			// Set the fields
			this.player_name = intent.getStringExtra(MainActivity.EXTRA_NAME);
			this.player_givenname = intent.getStringExtra(MainActivity.EXTRA_GIVENNAME);
			this.player_id = intent.getIntExtra(MainActivity.EXTRA_ID, -1);
			this.player_minutes = intent.getIntExtra(MainActivity.EXTRA_MINUTES, 0);
			this.player_rating = intent.getIntExtra(MainActivity.EXTRA_RATING, 0);
			this.player_goals = intent.getIntExtra(MainActivity.EXTRA_GOALS, 0);
			
			// Set the corresponding text fields
			((EditText)findViewById(R.id.player_edit_name)).setText(this.player_name);
			((EditText)findViewById(R.id.player_edit_givenname)).setText(this.player_givenname);
			((EditText)findViewById(R.id.player_goals_value)).setText("" + this.player_goals);
			((EditText)findViewById(R.id.player_minutes_value)).setText("" + this.player_minutes);
			((EditText)findViewById(R.id.player_rating_values)).setText("" + this.player_rating);

			
			// It is not a new player
			this.new_player = false;
		}
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	// Edit the players values
	public void editPlayer(View view)
	{
		// Make all EditText fields enabled
		((EditText)findViewById(R.id.player_edit_name)).setEnabled(true);
		((EditText)findViewById(R.id.player_edit_givenname)).setEnabled(true);
		((EditText)findViewById(R.id.player_goals_value)).setEnabled(true);
		((EditText)findViewById(R.id.player_minutes_value)).setEnabled(true);
		((EditText)findViewById(R.id.player_rating_values)).setEnabled(true);

		// Enable save and disable edit button
		((Button)findViewById(R.id.save_player_button)).setEnabled(true);
		((Button)findViewById(R.id.edit_player_button)).setEnabled(false);

	}
	
	// Save the players values
	public void savePlayer(View view)
	{
		this.player_givenname = ((EditText)findViewById(R.id.player_edit_givenname)).getText().toString();
		this.player_name = ((EditText)findViewById(R.id.player_edit_name)).getText().toString();
		this.player_goals = Integer.parseInt(((EditText)findViewById(R.id.player_goals_value)).getText().toString());
		this.player_minutes = Integer.parseInt(((EditText)findViewById(R.id.player_minutes_value)).getText().toString());
		this.player_rating = Float.parseFloat(((EditText)findViewById(R.id.player_rating_values)).getText().toString());
		
		Player player;
		
		if ( this.new_player )
		{
			this.player_id = MainActivity.next_free_player_id++;
			player = new Player(this.player_id);
		} else
		{
			player = PlayersContent.PLAYER_MAP.get(this.player_id);
			PlayersContent.PLAYER_MAP.remove(this.player_id);
		}
		
		// TODO: Check player values for emptiness
		
		// Update the players values
		player.setGivenname(this.player_givenname);
		player.setName(this.player_name);
		player.setGoals(this.player_goals);
		player.setMinutes(this.player_minutes);
		player.setRating(this.player_rating);
		
		if ( this.new_player )
		{
			// Save it locally
			PlayersContent.addPlayer(player);
			
			// And save it to the database
			MainActivity.getBackend().createPlayer(player);
		} else
		{
			// Save it locally
			PlayersContent.updatePlayer(player);
			
			// And save it to the database
			MainActivity.getBackend().updatePlayer(player);
		}
		
		finish();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

}

package football.scd.playerrating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import football.scd.playerrating.contents.PlayersContent;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class RatePlayers extends Activity
{
	private HashMap<Integer, Player> players;
	private int game_id;
	private int player_to_rate = 0;
	private List<Player> player_list;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate_players);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Create an empty player list
		this.player_list = new ArrayList<Player>();
		
		// Get the extra
		Intent intent = this.getIntent();
		this.players = (HashMap<Integer, Player>) intent.getSerializableExtra(GameActivity.EXTRA_PLAYED_LIST);
		this.game_id = intent.getIntExtra(GameActivity.EXTRA_GAME_ID, -1);
		
		// If the game id is -1 we have a problem, stop here...
		if ( this.game_id == -1 )
			finish();
			// TODO: throw error
				
		// Go through all players and add them to the list
		for (Entry<Integer, Player> entry : this.players.entrySet() )
			this.player_list.add( entry.getValue() );
		
		// And rate the first player
		this.displayPlayer();
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
		getMenuInflater().inflate(R.menu.rate_players, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void displayPlayer()
	{
		// Check if all players are already rated
		if ( this.player_to_rate >= this.player_list.size() )
		{
			finish();
			return;
		}
		// Get the player from the list at position position
		Player player = this.player_list.get( player_to_rate );
		
		// Set the name field
		((TextView)findViewById(R.id.players_name_field)).setText(player.toString());
		
		// Set the minutes played field
		((TextView)findViewById(R.id.rate_played_minutes_field)).setText( "" + player.getCurrentGameMinutes() );
		
		// Set the goals scored field
		((TextView)findViewById(R.id.rate_goals_scored)).setText( "" + player.getCurrentGameGoals() );
	}
	
	public void ratePlayer(View view)
	{
		// Get the buttons tag
		int rating = Integer.parseInt( (String) view.getTag() );
		Log.d("Rating", "Player " + this.player_list.get(this.player_to_rate) + " rated as " + rating );
		
		// For simplicity create a local player object
		Player player = this.player_list.get(this.player_to_rate);
		
		// Add the players current minutes to the list of minutes
		player.getMinutes().add( new Minute(player.getID(), this.game_id, player.getCurrentGameMinutes()) );
		
		// Add the players rating  to the list of ratings
		player.getRatings().add( new Rating(player.getID(), this.game_id, rating) );
		
		// Update the player locally
		PlayersContent.updatePlayer(player);
		
		// Update the player in the backend
		MainActivity.getBackend().updatePlayer(player);
		
		// Reset the current game values
		player.setCurrentGameGoals(0);
		player.setCurrentGameMinutes(0);
		
		// Display the next player
		this.player_to_rate++;
		this.displayPlayer();
	}

}

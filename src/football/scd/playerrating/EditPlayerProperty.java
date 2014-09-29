package football.scd.playerrating;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import football.scd.playerrating.contents.GamesContent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class EditPlayerProperty extends Activity {

	private Player player;
	private ArrayAdapter<String> adapter;
	
	// The goals, minutes, and ratings list are all separate lists
	private List<String> goals;
	private List<String> minutes;
	private List<String> ratings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_player_property);
		
		this.player = (Player) this.getIntent().getSerializableExtra(MainActivity.EXTRA_PLAYER);
		
		// Setup the list view adapter according to the property which is to 
		// edit
		System.out.println("player is  " + this.player);
		switch ( this.getIntent().getIntExtra(PlayerActivity.EXTRA_EDITABLE_PROPERTY, 0) ) 
		{
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_GOALS :
				
				// Set the minutes list
				this.goals = new ArrayList<String>();
				
				// Fill the goals list
				for (Goal goal : this.player.getGoals())
				{
					Game game = GamesContent.GAME_MAP.get(goal.getGameId());
					this.goals.add(game.getOpponent() + ": " + goal.toString() );
				}
				
				// Set the players goals as array adapter content
				this.adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.goals );
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_MINUTES :
				
				// Set the minutes list
				this.minutes = new ArrayList<String>();
				
				// Fill the minutes list
				for (Map.Entry<Integer,Integer> entry: this.player.getMinutes().entrySet() )
				{
					Game game = GamesContent.GAME_MAP.get(entry.getKey());
					this.minutes.add(game.getOpponent() + ": " + entry.getValue() );
				}
				
				
				// Set the players minutes as array adapter content
				this.adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.minutes );
	
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_RATINGS :
				
				// Set the ratings list
				this.ratings = new ArrayList<String>();
				
				// Fill the ratings list
				for (Map.Entry<Integer,Integer> entry: this.player.getRatings().entrySet() )
				{
					Game game = GamesContent.GAME_MAP.get(entry.getKey());
					this.ratings.add(game.getOpponent() + ": " + entry.getValue() );
				}
				
				// Set the players ratings as array adapter content
				this.adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.ratings );
				
				break;
				
			default:
				break;
		}

		((ListView)findViewById(R.id.propertyList)).setAdapter(this.adapter);
		
		// Show the Up button in the action bar.
		setupActionBar();
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
		getMenuInflater().inflate(R.menu.edit_player_property, menu);
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

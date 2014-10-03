package football.scd.playerrating;

import java.util.ArrayList;
import java.util.List;
import football.scd.playerrating.contents.GamesContent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EditPlayerProperty extends Activity {

	private Player player;
	private ArrayAdapter<Minute> minute_adapter;
	private ArrayAdapter<Rating> rating_adapter;
	private ArrayAdapter<String> goal_adapter;
	
	// The goals, minutes, and ratings list are all separate lists
	private List<String> goals_string;
	private List<Goal> goals;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_player_property);
		
		this.player = (Player) this.getIntent().getSerializableExtra(MainActivity.EXTRA_PLAYER);
		
		// Setup the list view adapter according to the property which is to 
		// edit
		switch ( this.getIntent().getIntExtra(PlayerActivity.EXTRA_EDITABLE_PROPERTY, 0) ) 
		{
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_GOALS :
				
				// Set the minutes list
				this.goals_string = new ArrayList<String>();
				
				// Fill the goals list
				for (Goal goal : this.player.getGoals())
				{
					this.goals.add( goal );
					this.goals_string.add(GamesContent.GAME_MAP.get(goal.getGameId()).getOpponent() + ": " + goal.toString() );
				}
				
				// Set the players goals as array adapter content
				this.goal_adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.goals_string );
				
				// Set the listvies adapter
				((ListView)findViewById(R.id.propertyList)).setAdapter(this.goal_adapter);
				
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_MINUTES :

				// Set the players minutes as array adapter content
				this.minute_adapter = new ArrayAdapter<Minute>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.player.getMinutes() );
				
				// Set the listvies adapter
				((ListView)findViewById(R.id.propertyList)).setAdapter(this.minute_adapter);
	
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_RATINGS :
				
				// Set the players ratings as array adapter content
				this.rating_adapter = new ArrayAdapter<Rating>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.player.getRatings() );
				
				// Set the listvies adapter
				((ListView)findViewById(R.id.propertyList)).setAdapter(this.rating_adapter);
				
				break;
				
			default:
				break;
		}
		
		// And create a new onitemclick listener for the list view
		((ListView)findViewById(R.id.propertyList)).setOnItemClickListener(new OnItemClickListener()
		{
			// Override the onItemClick function
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				
			}
		});
		
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
			//NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

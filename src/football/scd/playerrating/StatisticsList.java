package football.scd.playerrating;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.support.v4.app.NavUtils;

public class StatisticsList extends ListActivity 
{

	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	
	{
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_statistics_list);
		
		// Get the intent
		Intent intent = this.getIntent();
		
		// Get the type of statistics to display from the intent
		int type = intent.getIntExtra(MainActivity.EXTRA_STATS_TYPE, MainActivity.EXTRA_STATS_SCORER);
		
		// Create an emtpy list of strings
		List<String> list = new ArrayList<String>();
		
		// Check which type of stats it is
		int i = 0;
		int current_val = 0;
		switch (type)
		{
			// List the scorers 
			case MainActivity.EXTRA_STATS_SCORER:
				
				// Go through the players, sorted by goals scored
				for (Player player : PlayerStatistics.getTopScorerList() )
				{
					// All players scored the same amount of goals have the
					// same rank, so only increase the rank counter if the 
					// players goals are less than what we had before
					if ( current_val != player.getTotalGoals() )
						i++;
					
					// Remember the current amount of goals for the next
					// iteration
					current_val = player.getTotalGoals();
					
					// The string for goals
					String goals = (current_val == 1 ? "Goals" : "Goal" );
					
					// Add the composed sting to the list which will be
					// set as adapter later
					list.add(i + ".      " + current_val + " " + goals + "       " +  player.toString()  );
					
				}
					
				break;
				
			// List the minutes played 
			case MainActivity.EXTRA_STATS_MINUTES:
				
				// Go through the players, sorted by goals scored
				for (Player player : PlayerStatistics.getMostPlayedList() )
				{
					// All players played the same amount of minutes have the
					// same rank, so only increase the rank counter if the 
					// players minutes are less than what we had before
					if ( current_val != player.getTotalMinutes() )
						i++;
					
					// Remember the current amount of minutes played for the
					// next iteration
					current_val = player.getTotalMinutes();
					
					// The string for minutes
					String minutes = (current_val == 1 ? "Minutes" : "Minute" );
					
					// Add the composed sting to the list which will be
					// set as adapter later
					list.add(i + ".      " + current_val + " " + minutes + "       " +  player.toString()  );
					
				}
					
				break;
				
			// List the average rating
			case MainActivity.EXTRA_STATS_MVP:
				
				// We need a float to remember the current value
				float average_rating = 0;
				
				// Go through the players, sorted by average rating
				for (Player player : PlayerStatistics.getMvpList() )
				{
					// All players played the same average rating have the
					// same rank, so only increase the rank counter if the 
					// average rating are less than what we had before
					if ( average_rating != player.getAverageRating() )
						i++;
					
					// Remember the current average rating for the
					// next iteration
					average_rating = player.getAverageRating();

					// Add the composed sting to the list which will be
					// set as adapter later
					list.add(i + ".      " + average_rating + "       " +  player.toString()  );	
				}
					
				break;
				
			default:
				break;
		}
		
		// Create the adapter and set it
		this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
												android.R.id.text1, list);
		this.setListAdapter(this.adapter);
		
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
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics_list, menu);
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

}

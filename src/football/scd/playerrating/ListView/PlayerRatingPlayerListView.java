package football.scd.playerrating.ListView;

import java.util.ArrayList;
import java.util.List;

import football.scd.playerrating.*;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlayerRatingPlayerListView extends ListActivity 
{
	
	private ArrayAdapter<String> adapter;
	private PlayerRatingPlayerListViewAdapter contentAdapter;
	
	// Create an emtpy list of strings
	private List<String> stringList = new ArrayList<String>();
	private List<Player> playerList = new ArrayList<Player>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	
	{
		super.onCreate(savedInstanceState);
		
		// Get the intent
		Intent intent = this.getIntent();
		
		// Get the type of statistics to display from the intent
		this.contentAdapter= (PlayerRatingPlayerListViewAdapter) intent.getSerializableExtra(MainActivity.EXTRA_STATS_TYPE);
		
		// Build the list from the passed class
		List<Pair<Player,String>> content = this.contentAdapter.buildListViewAdapterContent();
		
		// Go through the list and extract the player and the string from each map
		for( Pair<Player,String> tmp : content )
		{
			this.stringList.add( tmp.second );
			this.playerList.add( tmp.first );
		}
		
		// Create the adapter and set it
		this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
												android.R.id.text1, stringList);
		this.setListAdapter(this.adapter);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);

		// Start the player activity
		Intent intent = new Intent(this, PlayerActivity.class);
		intent.putExtra(MainActivity.EXTRA_TYPE, MainActivity.EXTRA_TYPE_SHOW);
		intent.putExtra(MainActivity.EXTRA_PLAYER, this.playerList.get(position));
		this.startActivity(intent);
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
			//NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

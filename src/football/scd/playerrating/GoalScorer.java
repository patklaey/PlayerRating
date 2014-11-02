package football.scd.playerrating;

import football.scd.playerrating.contents.PlayersContent;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GoalScorer extends ListActivity 
{

	public static final String EXTRA_PLAYER = "football.scd.playerrating.GoalScorer.player";
	
	private ArrayAdapter<Player> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		this.adapter = new ArrayAdapter<Player>(this,
							android.R.layout.simple_list_item_1, android.R.id.text1,
							PlayersContent.PLAYERS);
		
		this.setListAdapter(this.adapter);
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
		getMenuInflater().inflate(R.menu.goal_scorer, menu);
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
			// NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);

		// Add the current goal to the selected player
		Player player = PlayersContent.PLAYERS.get(position);
		
		// Set the result as ok and pass the game back
		Intent intent = new Intent();
		intent.putExtra(GoalScorer.EXTRA_PLAYER, player );
		setResult(RESULT_OK, intent);
		finish();
	}

}

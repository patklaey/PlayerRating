package football.scd.playerrating;

import football.scd.playerrating.contents.GamesContent;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectGame extends ListActivity 
{

	public static final String EXTRA_GAME = "football.scd.playerrating.GoalScorer.game";
	
	private ArrayAdapter<Game> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		this.adapter = new ArrayAdapter<Game>(this,
							android.R.layout.simple_list_item_1, android.R.id.text1,
							GamesContent.getAllGames());
		
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
		Game game = GamesContent.getAllGames().get(position);
		
		// Set the result as ok and pass the game back
		Intent intent = new Intent();
		intent.putExtra(SelectGame.EXTRA_GAME, game );
		setResult(RESULT_OK, intent);
		finish();
	}

}

package football.scd.playerrating;

import java.util.ArrayList;
import java.util.List;
import football.scd.playerrating.contents.GamesContent;
import football.scd.playerrating.contents.PlayersContent;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class PlayerActivity extends Activity 
{	
	// Define the player
	private Player player;
	private boolean new_player = false;
	
	// The goals, minutes, and ratings list are all separate lists
	private List<String> goals;
	
	// The adapters for the corresponding listviews
	private ArrayAdapter<String> goals_adapter;
	private ArrayAdapter<Minute> minutes_adapter;
	private ArrayAdapter<Rating> ratings_adapter;
	
	// Extra strings to pass to the editProperty activity
	public static final String EXTRA_EDITABLE_PROPERTY = "football.scd.playerrating.playeractivity.property";
	public static final int EXTRA_EDITABLE_PROPERTY_GOALS = 0;
	public static final int EXTRA_EDITABLE_PROPERTY_MINUTES = 1;
	public static final int EXTRA_EDITABLE_PROPERTY_RATINGS = 2;
	public static final int EXTRA_EDIT_FINISHED = 0;
	
	// The current players ID
	public static int current_player_id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		// Request portrait orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// Get the intent
		Intent intent = getIntent();
		
		// Get the intent type extra
		String extra_type = intent.getStringExtra(MainActivity.EXTRA_TYPE);
		
		// If the extra is TYPE_NEW, then we want to create a new player
		if ( extra_type.equals(MainActivity.EXTRA_TYPE_NEW))
		{
			// Create a new player
			this.player = new Player( MainActivity.next_free_player_id );
			
			// Increase the next free player ID
			MainActivity.next_free_player_id++;
			
			// Enable the name fields and the save button
			((EditText)findViewById(R.id.player_edit_name)).setEnabled(true);
			((EditText)findViewById(R.id.player_edit_givenname)).setEnabled(true);
			((Button)findViewById(R.id.save_player_button)).setEnabled(true);
			
			// Hide the delete button
			((Button)findViewById(R.id.delete_player_button)).setVisibility(View.INVISIBLE);
			
			// Show the save player button
			((Button)findViewById(R.id.save_player_button)).setVisibility(View.VISIBLE);
			
			// Remember that it is a new player
			this.new_player = true;
			
		} else if ( extra_type.equals(MainActivity.EXTRA_TYPE_SHOW) )
		{
			// Set the fields
			this.player = (Player) intent.getSerializableExtra(MainActivity.EXTRA_PLAYER);
			PlayerActivity.current_player_id = this.player.getID();
			this.goals = new ArrayList<String>();
	
			setupUI();
			
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

		// Enable save and 
		((Button)findViewById(R.id.save_player_button)).setEnabled(true);
		
		// Show save cancel and delete button
		((Button)findViewById(R.id.delete_player_button)).setVisibility(View.VISIBLE);
		((Button)findViewById(R.id.cancel_edit_player)).setVisibility(View.VISIBLE);
		((Button)findViewById(R.id.save_player_button)).setVisibility(View.VISIBLE);
		
		// Show edit buttons for minutes, ratings and goals
		((Button)findViewById(R.id.edit_goals)).setVisibility(View.VISIBLE);
		((Button)findViewById(R.id.edit_minutes)).setVisibility(View.VISIBLE);
		((Button)findViewById(R.id.edit_ratings)).setVisibility(View.VISIBLE);
		
		// Request focus for the players name
		((EditText)findViewById(R.id.player_edit_name)).requestFocus();
		
	}
	
	@SuppressWarnings("unchecked")
	private void setupUI()
	{
		// Set the goals list
		this.goals.clear();
		
		// Fill the goals list
		for (Goal goal : this.player.getGoals())
		{
			Game game = GamesContent.getGameById(goal.getGameId());
			this.goals.add(game.getOpponent() + ": " + goal.toString() );
		}
		
		// Set the goal list views adapter
		this.goals_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
													   android.R.id.text1, this.goals );
		((ListView)findViewById(R.id.players_goals_list)).setAdapter(this.goals_adapter);
		((ArrayAdapter<String>)((ListView)findViewById(R.id.players_goals_list)).getAdapter()).notifyDataSetChanged();
		
		// Set the minutes list views adapter
		this.minutes_adapter = new ArrayAdapter<Minute>(this, android.R.layout.simple_list_item_1, 
													   android.R.id.text1, this.player.getMinutes() );
		((ListView)findViewById(R.id.players_minutes_list)).setAdapter(this.minutes_adapter);
		((ArrayAdapter<Minute>)((ListView)findViewById(R.id.players_minutes_list)).getAdapter()).notifyDataSetChanged();


		// Set the ratings list views adapter
		this.ratings_adapter = new ArrayAdapter<Rating>(this, android.R.layout.simple_list_item_1, 
													     android.R.id.text1, this.player.getRatings() );
		((ListView)findViewById(R.id.players_ratings_list)).setAdapter(this.ratings_adapter);
		((ArrayAdapter<Rating>)((ListView)findViewById(R.id.players_ratings_list)).getAdapter()).notifyDataSetChanged();
		
		// Set the corresponding text fields
		((EditText)findViewById(R.id.player_edit_name)).setText(this.player.getName() );
		((EditText)findViewById(R.id.player_edit_givenname)).setText(this.player.getGivenname());
		((TextView)findViewById(R.id.player_total_goals)).setText("" + this.player.getTotalGoals() );
		
		// Set the corresponding text fields
		((TextView)findViewById(R.id.player_total_minutes)).setText("" + this.player.getTotalMinutes());
		((TextView)findViewById(R.id.player_average_rating)).setText("" + this.player.getAverageRating());
	}
	
	// Cancel the editing
	public void cancelEdit(View view)
	{
		// Make all EditText fields enabled
		((EditText)findViewById(R.id.player_edit_name)).setEnabled(false);
		((EditText)findViewById(R.id.player_edit_givenname)).setEnabled(false);
		
		// Show save cancel and delete button
		((Button)findViewById(R.id.delete_player_button)).setVisibility(View.INVISIBLE);
		((Button)findViewById(R.id.cancel_edit_player)).setVisibility(View.INVISIBLE);
		((Button)findViewById(R.id.save_player_button)).setVisibility(View.INVISIBLE);
		
		// Show edit buttons for minutes, ratings and goals
		((Button)findViewById(R.id.edit_goals)).setVisibility(View.INVISIBLE);
		((Button)findViewById(R.id.edit_minutes)).setVisibility(View.INVISIBLE);
		((Button)findViewById(R.id.edit_ratings)).setVisibility(View.INVISIBLE);

	}
	
	// Edit the players goals
	public void editGoals(View view)
	{
		Intent intent = new Intent(this, EditPlayerProperty.class);
		intent.putExtra(MainActivity.EXTRA_PLAYER, this.player);
		intent.putExtra(PlayerActivity.EXTRA_EDITABLE_PROPERTY,PlayerActivity.EXTRA_EDITABLE_PROPERTY_GOALS);
		this.startActivityForResult(intent, EXTRA_EDIT_FINISHED);
	}
	
	// Edit the players goals
	public void editMinutes(View view)
	{
		Intent intent = new Intent(this, EditPlayerProperty.class);
		intent.putExtra(MainActivity.EXTRA_PLAYER, this.player);
		intent.putExtra(PlayerActivity.EXTRA_EDITABLE_PROPERTY,PlayerActivity.EXTRA_EDITABLE_PROPERTY_MINUTES);
		this.startActivityForResult(intent, EXTRA_EDIT_FINISHED);
	}
	
	// Edit the players goals
	public void editRatings(View view)
	{
		Intent intent = new Intent(this, EditPlayerProperty.class);
		intent.putExtra(MainActivity.EXTRA_PLAYER, this.player);
		intent.putExtra(PlayerActivity.EXTRA_EDITABLE_PROPERTY,PlayerActivity.EXTRA_EDITABLE_PROPERTY_RATINGS);
		this.startActivityForResult(intent, EXTRA_EDIT_FINISHED);
	}
	
	// Save the players values
	public void savePlayer(View view)
	{
		this.player.setGivenname(((EditText)findViewById(R.id.player_edit_givenname)).getText().toString());
		this.player.setName(((EditText)findViewById(R.id.player_edit_name)).getText().toString());
		
		if ( this.new_player )
		{
			// Save it locally
			PlayersContent.addPlayer(this.player);
			
			// And save it to the database
			MainActivity.getBackend().createPlayer(this.player);
		} else
		{
			// Save it locally
			PlayersContent.updatePlayer(this.player);
			
			// And save it to the database
			MainActivity.getBackend().updatePlayer(this.player);
		}
		
		PlayersFragment.updateList();
		finish();
	}
	
	public void deletePlayer(View view)
	{
		new AlertDialog.Builder(this)
	    .setTitle("Delete Player")
	    .setMessage("Are you sure you want to delete this player with all its goals, minutes and ratings?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int which)
	        {
	    		// Remove the player locally
	    		PlayersContent.removePlayer( PlayerActivity.current_player_id );
	    		
	    		// Remove the player from the database
	    		MainActivity.getBackend().removePlayer( PlayerActivity.current_player_id );
	    		
	    		finish();
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int which)
	        { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
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
		getMenuInflater().inflate(R.menu.player, menu);
		
		if ( this.new_player )
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
				
				// Lets edit the player
				this.editPlayer(null);
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
				
		}
		
	}
	
	@Override
    protected void onActivityResult(int request_code, int result_code, Intent data)
    {
    	super.onActivityResult(request_code, result_code, data);
    	
        // 
        if ( request_code == PlayerActivity.EXTRA_EDIT_FINISHED && result_code == RESULT_OK )
        {
        	int player_id = (int) data.getIntExtra(EditPlayerProperty.EXTRA_PROPERTY_PLAYER_ID, 0);
        	this.player = PlayersContent.getPlayerById(player_id);
        	this.setupUI();
        }
    }
}

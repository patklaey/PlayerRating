package football.scd.playerrating;

import java.util.ArrayList;
import java.util.List;
import football.scd.playerrating.contents.GamesContent;
import football.scd.playerrating.contents.PlayersContent;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EditPlayerProperty extends ListActivity {

	private Player player;
	private ArrayAdapter<Minute> minute_adapter;
	private ArrayAdapter<Rating> rating_adapter;
	private ArrayAdapter<String> goal_adapter;
	
	// The goals, minutes, and ratings list are all separate lists
	private List<String> goals_string;
	private List<Goal> goals;

	// The property
	private static int property;
	private int property_position;
	private Object old_property;

	// The extra to pass to the edit activity
	public static final String EXTRA_PROPERTY = "football.scd.playerrating.editplayerproperty.extra_property";
	private static final int EDIT_PROPERTY_RESULT = 1;
	private static final int ADD_PROPERTY_RESULT = 2; 
	public static final String EXTRA_PROPERTY_PLAYER_ID = "football.scd.playerrating.editplayerproperty.extra_property_player_id";
	public static final int RESULT_DELETED = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_player_property);
		
		// Set the static property as it needs to be accessed from the onClick
		// listener
		EditPlayerProperty.property = this.getIntent().getIntExtra(PlayerActivity.EXTRA_EDITABLE_PROPERTY, 0);
		this.goals = new ArrayList<Goal>();
		
		// Get the player which is passed
		int player_id = this.getIntent().getIntExtra(MainActivity.EXTRA_PLAYER_ID, 0);
		this.player = PlayersContent.getPlayerById(player_id);
		
		// Set the adapter content
		this.setAdapterContent();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@SuppressWarnings("unchecked")
	public void setAdapterContent()
	{
		// Setup the list view adapter according to the property which is to 
		// edit
		switch ( EditPlayerProperty.property ) 
		{
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_GOALS :
						
				// Set the minutes list
				this.goals_string = new ArrayList<String>();
						
				// Fill the goals list
				for (Goal goal : this.player.getGoals())
				{
					this.goals.add( goal );
					this.goals_string.add(GamesContent.getGameById(goal.getGameId()).getOpponent() + ": " + goal.toString() );
				}
						
				// Set the players goals as array adapter content
				this.goal_adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.goals_string );
				
				// Set the listvies adapter
				((ListView)findViewById(android.R.id.list)).setAdapter(this.goal_adapter);
				((ArrayAdapter<String>)((ListView)findViewById(android.R.id.list)).getAdapter()).notifyDataSetChanged();
				
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_MINUTES :

				// Set the players minutes as array adapter content
				this.minute_adapter = new ArrayAdapter<Minute>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.player.getMinutes() );
				
				// Set the listvies adapter
				((ListView)findViewById(android.R.id.list)).setAdapter(this.minute_adapter);
	
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_RATINGS :
				
				// Set the players ratings as array adapter content
				this.rating_adapter = new ArrayAdapter<Rating>(this,
						android.R.layout.simple_list_item_1,android.R.id.text1,
						this.player.getRatings() );
				
				// Set the listvies adapter
				((ListView)findViewById(android.R.id.list)).setAdapter(this.rating_adapter);
				
				break;
				
			default:
				break;
		}
	}
	
	public void finishEdit(View view) 
	{
		// Set the result as OK and pass the player back
		Intent intent = new Intent();
		intent.putExtra( EditPlayerProperty.EXTRA_PROPERTY_PLAYER_ID, this.player.getID() );
		setResult(RESULT_OK, intent);
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
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		Intent intent;
		
		switch ( EditPlayerProperty.property ) 
		{
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_GOALS :
				
				// Start the EditGoal activity
				intent = new Intent(this, EditGoal.class);
				intent.putExtra(EditPlayerProperty.EXTRA_PROPERTY, this.goals.get(position) );
				this.old_property = this.goals.get(position);
				this.property_position = position;
				this.startActivityForResult(intent, EditPlayerProperty.EDIT_PROPERTY_RESULT);
				
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_MINUTES :

				// Start the EditRating activity
				intent = new Intent(this, EditMinute.class);
				intent.putExtra(EditPlayerProperty.EXTRA_PROPERTY, this.player.getMinutes().get(position) );
				this.property_position = position;
				this.startActivityForResult(intent, EditPlayerProperty.EDIT_PROPERTY_RESULT);
				
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_RATINGS :
				
				// Start the EditRating activity
				intent = new Intent(this, EditRating.class);
				intent.putExtra(EditPlayerProperty.EXTRA_PROPERTY, this.player.getRatings().get(position) );
				this.property_position = position;
				this.startActivityForResult(intent, EditPlayerProperty.EDIT_PROPERTY_RESULT);
				
				break;
				
			default:
				break;
		}
	}
	
	@Override
    protected void onActivityResult(int request_code, int result_code, Intent data)
    {
    	super.onActivityResult(request_code, result_code, data);
    	
        // If the result is from edit property
        if ( request_code == EditPlayerProperty.EDIT_PROPERTY_RESULT )
        {
        	switch ( EditPlayerProperty.property ) 
    		{
    			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_GOALS :
    				
    				if ( result_code == RESULT_OK)
    				{
	    				// Replace the goal in the players goal list
	    				Goal goal = (Goal) data.getSerializableExtra(EditPlayerProperty.EXTRA_PROPERTY);
	    				
	    				// Check if it is the same player
	    				if ( ((Goal)this.old_property).getPlayerId() == goal.getPlayerId() )
	    				{
		    				List<Goal> goal_list = this.player.getGoals();
		    				goal_list.set(this.property_position, goal);
		    				this.player.setGoals(goal_list);
		    				
	    				} else
	    				{
	    					// Remove the goal from the old player
	    					List<Goal> goal_list = this.player.getGoals();
	    					goal_list.remove(this.property_position);
	    					this.player.setGoals(goal_list);
		    				
		    				// Add the goal to the new player
		    				Player new_scorer = PlayersContent.getPlayerById( goal.getPlayerId() );
		    				new_scorer.addGoal(goal);
		    				PlayersContent.updatePlayer( new_scorer );
	    				}
    				} else if ( result_code == RESULT_DELETED )
    				{
    					// Remove the goal from the old player
    					List<Goal> goal_list = this.player.getGoals();
    					goal_list.remove(this.property_position);
    					this.player.setGoals(goal_list);
    				}
    				
    				break;
    				
    			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_MINUTES :

    				List<Minute> minute_list = this.player.getMinutes();
    				if ( result_code == RESULT_OK )
    				{
	    				// Get the minute returned from EditMinute activity and set it
	    				Minute minute = (Minute) data.getSerializableExtra(EditPlayerProperty.EXTRA_PROPERTY);
	    				minute_list.set(this.property_position, minute);
    				} else if ( result_code == RESULT_DELETED )
    				{
    					minute_list.remove(this.property_position);
    				}
    				
    				this.player.setMinutes(minute_list);

    				break;
    				
    			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_RATINGS :
    				
    				// Start the EditRating activity
    				List<Rating> rating_list = this.player.getRatings();
    				if ( result_code == RESULT_OK )
    				{
        				Rating rating = (Rating) data.getSerializableExtra(EditPlayerProperty.EXTRA_PROPERTY);
        				rating_list.set(this.property_position, rating);
    				} else if (result_code == RESULT_DELETED )
    				{
    					rating_list.remove(this.property_position);
    				}
    				
    				this.player.setRatings(rating_list);
    				
    				break;
    				
    			default:
    				break;
    		}
        }
        
        // If the result is from add property
        if ( request_code == EditPlayerProperty.ADD_PROPERTY_RESULT )
        {
        	// Did the user save?
        	if ( result_code == RESULT_OK )
        	{
        		// Check which property it is
        		switch ( EditPlayerProperty.property )
        		{
					case PlayerActivity.EXTRA_EDITABLE_PROPERTY_GOALS:
						
						// Get the goal and add the correct player
						Goal goal = (Goal) data.getSerializableExtra(AddProperty.EXTRA_NEW_PROPERTY);
						goal.setPlayerId(this.player.getID());
						this.player.addGoal(goal);
						
	    				break;
	    				
					case PlayerActivity.EXTRA_EDITABLE_PROPERTY_MINUTES:
						
						// Get the minutes object and add the correct player
						Minute minute = (Minute) data.getSerializableExtra(AddProperty.EXTRA_NEW_PROPERTY);
						minute.setPlayerId(this.player.getID());
						this.player.addMinute(minute);
						
						break;
						
					case PlayerActivity.EXTRA_EDITABLE_PROPERTY_RATINGS:
						
						// Get the rating object and add the correct player
						Rating rating = (Rating) data.getSerializableExtra(AddProperty.EXTRA_NEW_PROPERTY);
						rating.setPlayerId(this.player.getID());
						this.player.addRating(rating);
						
						break;
	
					default:
						break;
				}
        	}
        }
        
        // Save the player
		PlayersContent.updatePlayer( this.player );
        
        // Update the property list
		this.setAdapterContent();
    }
	
	public void addProperty(View view)
	{
		// Start the add property activity
		Intent intent = new Intent(this, AddProperty.class);
		this.startActivityForResult(intent, EditPlayerProperty.ADD_PROPERTY_RESULT);
	}
	
	public static int getProperty() 
	{
		return EditPlayerProperty.property;
	}
}

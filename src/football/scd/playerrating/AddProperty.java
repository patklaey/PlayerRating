package football.scd.playerrating;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddProperty extends Activity 
{
	public static final int GAME_SELECTED = 1;
	public static final String EXTRA_NEW_PROPERTY = "football.scd.playerrating.AddProperty.newProperty";
	
	private TextView game_name;
	private TextView property_name;
	private Game game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_property);
		
		this.property_name = (TextView) findViewById(R.id.add_property_property);
		this.game_name = (TextView) findViewById(R.id.add_goal_game_name);
		this.game = null;
		
		// Check which property is currently beeing edited and set the label
		// accordingly
		switch (EditPlayerProperty.getProperty() )
		{
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_GOALS:
				
				this.property_name.setText(R.string.Minute);
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_MINUTES:
				
				this.property_name.setText(R.string.Minutes);
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_RATINGS:
				
				this.property_name.setText(R.string.Rating);
				break;
				
			default:
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_goal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void selectGame(View view)
	{
		Intent intent = new Intent(this, SelectGame.class);
		this.startActivityForResult(intent, AddProperty.GAME_SELECTED);
	}
	
	public void save(View view)
	{
		// Validate the input
		if ( ! this.validatePropertyInput() )
			return;
		
		// Create the intent
		Intent intent = new Intent();
		
		// Check which property it is and act accordingly
		int minutes;
		switch ( EditPlayerProperty.getProperty() )
		{
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_GOALS:
				
				// Create a new goal and add it to the intent
				minutes = Integer.valueOf( ((TextView) findViewById(R.id.add_property_property_input )).getText().toString() );
				Goal goal = new Goal(MainActivity.next_free_goal_id, minutes, new Player(0), this.game.getID() );
				intent.putExtra(AddProperty.EXTRA_NEW_PROPERTY, goal);
				
				break;
				
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_MINUTES:
				
				// Create a new minute object and add it to the intent
				minutes = Integer.valueOf( ((TextView) findViewById(R.id.add_property_property_input )).getText().toString() );
				Minute minute = new Minute(0, this.game.getID(), minutes);
				intent.putExtra(AddProperty.EXTRA_NEW_PROPERTY, minute);
				
				break;
	
			case PlayerActivity.EXTRA_EDITABLE_PROPERTY_RATINGS:
				
				// Create a new rating and add it to the intent
				int rating_value = Integer.valueOf( ((TextView) findViewById(R.id.add_property_property_input )).getText().toString() );
				Rating rating = new Rating(0, this.game.getID(), rating_value);
				intent.putExtra(AddProperty.EXTRA_NEW_PROPERTY, rating);
				
				break;
				
			default:
				break;
		}
		
		setResult(RESULT_OK,intent);
		finish();
	}
	
	public void cancel(View view)
	{
		Intent intent = new Intent();
		setResult(RESULT_CANCELED, intent);
		finish();
	}
	
	private boolean validatePropertyInput()
	{
		// First check if a game is set
		if ( this.game == null )
		{
			Context context = getApplicationContext();
			String message = "Please select a game!";
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return false;
		}
		
		// Validate the input
		if ( EditPlayerProperty.getProperty() == PlayerActivity.EXTRA_EDITABLE_PROPERTY_GOALS || 
			 EditPlayerProperty.getProperty() == PlayerActivity.EXTRA_EDITABLE_PROPERTY_MINUTES )
		{
			// Minutes must be between 0 and HALF_TIME_DURATION x 2
			int minutes = Integer.valueOf( ((TextView) findViewById(R.id.add_property_property_input )).getText().toString() );
			System.out.println("Minute " + minutes );
			if ( minutes < 0 || minutes > ( MainActivity.getSettings().getHalfTimeDuration() * 2 ) )
			{
				Context context = getApplicationContext();
				String message = "The entered minutes value is not valid!\nMinutes must be between 0 and ";
				message += MainActivity.getSettings().getHalfTimeDuration() * 2;
				Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return false;
			}
		} else
		{
			// Rating must be between 1 and 4
			int rating = Integer.valueOf( (String)((TextView) findViewById(R.id.add_property_property_input )).getText().toString() );
			if ( rating < 1 || rating > 4 )
			{
				Context context = getApplicationContext();
				String message = "The entered rating value is not valid!\nRatings must be between 1 and 4";
				Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return false;
			}
		}
		
		return true;
	}
	
	@Override
    protected void onActivityResult(int request_code, int result_code, Intent data)
    {
    	super.onActivityResult(request_code, result_code, data);
    	
    	if ( request_code == AddProperty.GAME_SELECTED && result_code == RESULT_OK )
    	{
    		Game game = (Game) data.getSerializableExtra(SelectGame.EXTRA_GAME);
    		this.game_name.setText(game.getOpponent());
    		this.game = game;
    	}
    }
	
}

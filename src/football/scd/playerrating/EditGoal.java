package football.scd.playerrating;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class EditGoal extends Activity 
{

	private static final int CHANGE_SCORER = 1;
	private Goal goal;
	private EditText goal_minute_view;
	private Player goal_scorer;
	private TextView goal_scorer_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_goal);
		
		// Get the goal extra
		this.goal = (Goal) this.getIntent().getSerializableExtra(EditPlayerProperty.EXTRA_PROPERTY);
		this.goal_scorer = goal.getPlayer();
		
		// Assing the views
		this.goal_minute_view = (EditText) this.findViewById(R.id.edit_goal_minute);
		this.goal_scorer_view = (TextView) this.findViewById(R.id.player_textview);
		
		
		// Set the values of label and minute edit text
		this.goal_minute_view.setText( "" + this.goal.getMinute() );
		this.goal_scorer_view.setText( this.goal_scorer.getGivenname() + " " + this.goal_scorer.getName() );	
		
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	public void save(View view)
	{
		int minute_value = Integer.valueOf( this.goal_minute_view.getText().toString() );
		
		// Check if the value is correct
		if ( minute_value < 0 || minute_value > MainActivity.getSettings().getHalfTimeDuration() * 2 )
		{
			Context context = getApplicationContext();
			String message = "The entered minutes value is not valid!\nMinutes must be between 0 and ";
			message += MainActivity.getSettings().getHalfTimeDuration() * 2;
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		}
		
		// Set the goals minute
		this.goal.setMinute( minute_value );
		this.goal.setPlayer( this.goal_scorer );
		
		// Set the result as OK and pass the game back
		Intent intent = new Intent();
		intent.putExtra( EditPlayerProperty.EXTRA_PROPERTY, this.goal );
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void changePlayer(View view)
	{
		// Get the player
		Intent scorer = new Intent(this,SelectPlayer.class);
		this.startActivityForResult(scorer, EditGoal.CHANGE_SCORER);
	}
	
	public void delete(View view)
	{
		new AlertDialog.Builder(this)
	    .setTitle("Delete Goal")
	    .setMessage("Are you sure you want to delete this goal?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	    		setResult(EditPlayerProperty.RESULT_DELETED);
	    		finish();
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_goal, menu);
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
	
	@Override
    protected void onActivityResult(int request_code, int result_code, Intent data)
    {
    	super.onActivityResult(request_code, result_code, data);
    	
        // If it was a SELF_GOAL_SCORED activity and it returned ok, add the
    	// goal to the goals scored list
        if ( request_code == EditGoal.CHANGE_SCORER && result_code == RESULT_OK )
        {
        	Player returned = (Player) data.getSerializableExtra(SelectPlayer.EXTRA_PLAYER);
        	this.goal_scorer = returned;
        	this.goal_scorer_view.setText( this.goal_scorer.getGivenname() + " " + this.goal_scorer.getName() );	
        }
    }

}

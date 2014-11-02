package football.scd.playerrating;

import football.scd.playerrating.contents.PlayersContent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class EditGoal extends Activity 
{

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
		// Set the goals minute
		this.goal.setMinute( Integer.valueOf( this.goal_minute_view.getText().toString() ) );
		
		// Set the result as ok and pass the game back
		Intent intent = new Intent();
		intent.putExtra( EditPlayerProperty.EXTRA_PROPERTY, this.goal );
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

}

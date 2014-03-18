package football.scd.playerrating;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class PlayerActivity extends Activity {

	// Define fields used for player
	private String player_name;
	private String player_givenname;
	private int player_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		// Get the intent
		Intent intent = getIntent();
		
		// Get the intent type extra
		String extra_type = intent.getStringExtra(MainActivity.EXTRA_TYPE);
		
		// If the extra is TYPE_NEW, then we want to create a new player
		if ( extra_type.equals(MainActivity.EXTRA_TYPE_NEW))
		{
			// Enable the name fields and the save button
			((EditText)findViewById(R.id.player_edit_name)).setEnabled(true);
			((EditText)findViewById(R.id.player_edit_givenname)).setEnabled(true);
			((Button)findViewById(R.id.save_player_button)).setEnabled(true);
			
			// Disable the edit button
			((Button)findViewById(R.id.edit_player_button)).setEnabled(false);
		} else if ( extra_type.equals(MainActivity.EXTRA_TYPE_SHOW) )
		{
			// Set the fields
			this.player_name = intent.getStringExtra(MainActivity.EXTRA_NAME);
			this.player_givenname = intent.getStringExtra(MainActivity.EXTRA_GIVENNAME);
			this.player_id = intent.getIntExtra(MainActivity.EXTRA_ID, -1);
			
			// Set the corresponding text fields
			((EditText)findViewById(R.id.player_edit_name)).setText(this.player_name);
			((EditText)findViewById(R.id.player_edit_givenname)).setText(this.player_givenname);
		}
		
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
		getMenuInflater().inflate(R.menu.player, menu);
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

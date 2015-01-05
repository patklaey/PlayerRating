package football.scd.playerrating;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EditRating extends Activity {
	
	private Rating rating;
	private TextView rating_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_rating);
		
		this.rating = (Rating) this.getIntent().getSerializableExtra(EditPlayerProperty.EXTRA_PROPERTY);
		this.rating_view = (TextView) findViewById(R.id.edit_rating_rating);
		this.rating_view.setText( "" + this.rating.getRating() );
	}
	
	public void save(View view) {
		int value = Integer.valueOf( this.rating_view.getText().toString() );
		
		// Check if the value is correct
		if ( value < 1 || value > 4 ) {
			Context context = getApplicationContext();
			String message = "The entered rating value is not valid!\nRatings must be between 1 and 4";
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		}
		
		// Add the correct rating
		this.rating.setRating(value);
		Intent intent = new Intent();
		intent.putExtra(EditPlayerProperty.EXTRA_PROPERTY, this.rating);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void delete(View view) {
		new AlertDialog.Builder(this)
	    .setTitle("Delete Rating")
	    .setMessage("Are you sure you want to delete this rating?")
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_rating, menu);
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
}

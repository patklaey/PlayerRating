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

public class EditMinute extends Activity
{
	private Minute minute;
	private TextView minuteView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_minute);
		
		this.minute = (Minute) getIntent().getSerializableExtra(EditPlayerProperty.EXTRA_PROPERTY);
		this.minuteView = (TextView) findViewById(R.id.edit_minute_minute);
		this.minuteView.setText("" + this.minute.getMinutes());
		
	}
	
	public void save(View view) {
		int value = Integer.valueOf( this.minuteView.getText().toString() );
		
		// Check if the value is correct
		if ( value < 0 || value > MainActivity.getSettings().getHalfTimeDuration() * 2 )
		{
			Context context = getApplicationContext();
			String message = "The entered minutes value is not valid!\nMinutes must be between 0 and ";
			message += MainActivity.getSettings().getHalfTimeDuration() * 2;
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		}
		
		// Add the correct rating
		this.minute.setMinutes(value);
		Intent intent = new Intent();
		intent.putExtra(EditPlayerProperty.EXTRA_PROPERTY, this.minute);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void delete(View view) {
		new AlertDialog.Builder(this)
	    .setTitle("Delete Minutes")
	    .setMessage("Are you sure you want to delete this minutes entry?")
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
		getMenuInflater().inflate(R.menu.edit_minute, menu);
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

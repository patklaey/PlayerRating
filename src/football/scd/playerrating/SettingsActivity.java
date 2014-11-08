package football.scd.playerrating;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends Activity 
{
	private TextView half_time_text;
	private TextView team_name_text;

	public static final String EXTRA_SETTINGS = "football.scd.playerrating.SettingsActivity.extra_settings";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		this.half_time_text = (TextView) findViewById(R.id.settings_half_time_duration);
		this.team_name_text = (TextView) findViewById(R.id.settings_team_name);
		this.half_time_text.setText( "" + MainActivity.getSettings().getHalfTimeDuration() );
		this.team_name_text.setText( MainActivity.getSettings().getTeamName() );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void save(View view)
	{
		Settings settings = new Settings();
		int half_time = Integer.valueOf( this.half_time_text.getText().toString() );
		settings.setHalfTimeDuration(half_time);
		settings.setTeamName( this.team_name_text.getText().toString() );
		
		Intent intent = new Intent();
		intent.putExtra(SettingsActivity.EXTRA_SETTINGS, settings);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void cancel(View view)
	{
		setResult(RESULT_CANCELED);
		finish();
	}
}

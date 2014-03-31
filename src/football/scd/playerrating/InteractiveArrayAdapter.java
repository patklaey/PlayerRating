package football.scd.playerrating;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class InteractiveArrayAdapter extends ArrayAdapter<Player>
{
	
	private final List<Player> list;
	private final Activity context;

	public InteractiveArrayAdapter(Activity context, List<Player> list)
	{
		// Set the layout and the context and the list
		super(context, R.layout.activity_substitution, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder
	{
		// The view holder contains simply a text view (for the players name) 
		// and a check box (whether the player is on the field or not)
		protected TextView text;
		protected CheckBox checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = null;
		
		// If the passed view is empty
		if (convertView == null) 
		{
			// Create a new view according to the layout
			LayoutInflater inflator = context.getLayoutInflater();
		    view = inflator.inflate(R.layout.activity_substitution, null);
		    final ViewHolder viewHolder = new ViewHolder();
		    viewHolder.text = (TextView) view.findViewById(R.id.player_name_text_field);
		    viewHolder.checkbox = (CheckBox) view.findViewById(R.id.on_field_checkbox);
		    
		    // Set a oncheckedchangeListener for the checkbox (this performs
		    // the substitution)
		    viewHolder.checkbox
	        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() 
	        {	 
	        	@Override
		        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		        {
	        		// Set the players playing attribute to the checkboxes checked
	        		// value
	        		Player player = (Player) viewHolder.checkbox.getTag();
			        player.setPlaying(buttonView.isChecked());
			        
			        // If the player goes on the field, remmber that this player
			        // played in this match
			        if ( buttonView.isChecked() )
			        	GameActivity.played.put(player.getID(), player);
		        }
	        });
		    
		    // Connect player and list position, so when clicking the checkbox
		    // the correct player is updated
		    view.setTag(viewHolder);
		    viewHolder.checkbox.setTag(list.get(position));
		     
		} else
		{
			// If the view is not empty, just connect player and list position
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		 
		// Display all list rows correctly
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(list.get(position).toString());
		holder.checkbox.setChecked(list.get(position).isPlaying());
		return view;
	}
}

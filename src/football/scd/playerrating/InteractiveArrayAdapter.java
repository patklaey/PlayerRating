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
	   super(context, R.layout.activity_substitution, list);
	   this.context = context;
	   this.list = list;
	 }

	 static class ViewHolder
	 {
		 protected TextView text;
		 protected CheckBox checkbox;
	 }

	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) 
	 {
		 View view = null;
		 if (convertView == null) 
		 {
		     LayoutInflater inflator = context.getLayoutInflater();
		     view = inflator.inflate(R.layout.activity_substitution, null);
		     final ViewHolder viewHolder = new ViewHolder();
		     viewHolder.text = (TextView) view.findViewById(R.id.player_name_text_field);
		     viewHolder.checkbox = (CheckBox) view.findViewById(R.id.on_field_checkbox);
		     viewHolder.checkbox
	         .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() 
	         {
	        	 
	        	 @Override
		         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		         {
	        		 Player player = (Player) viewHolder.checkbox.getTag();
			         player.setPlaying(buttonView.isChecked());
		         }
	         });
		      
		     view.setTag(viewHolder);
		     viewHolder.checkbox.setTag(list.get(position));
		     
		 } else
		 {
			 view = convertView;
			 ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		 }
		 
		 ViewHolder holder = (ViewHolder) view.getTag();
		 holder.text.setText(list.get(position).toString());
		 holder.checkbox.setChecked(list.get(position).isPlaying());
		 return view;
	 }
}

/**
 * 
 */
package football.scd.playerrating.ListView;

import java.util.List;

import android.util.Pair;

import football.scd.playerrating.Player;
import football.scd.playerrating.PlayerStatistics;

/**
 * @author TGDKLPA4
 *
 */
public class MostPlayedListViewAdapter extends PlayerRatingPlayerListViewAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6292073029058297395L;

	/* (non-Javadoc)
	 * @see football.scd.playerrating.ListView.PlayerRatingListViewAdapter#buildListViewAdapterContent()
	 */
	@Override
	public List<Pair<Player, String>> buildListViewAdapterContent() {

		// Go through the players, sorted by goals scored
		for (Player player : PlayerStatistics.getMostPlayedList() )
		{
			// All players scored the same amount of goals have the
			// same rank, so only increase the rank counter if the 
			// players goals are less than what we had before
			if ( this.current_val != player.getTotalMinutes() )
				this.position++;
			
			// Remember the current amount of goals for the next
			// iteration
			this.current_val = player.getTotalMinutes();
			
			// The string for goals
			String minutes = (current_val == 1 ? "Minute" : "Minutes" );
			
			// Add the composed sting to the list which will be
			// set as adapter later
			String adapterContent = this.position + ".      " + this.current_val + " " + minutes + "       " +  player.toString();
			Pair<Player, String> listContent = new Pair<Player, String>(player, adapterContent);
			this.listViewContentList.add( listContent );
		}
		
		return this.listViewContentList;
	}

}

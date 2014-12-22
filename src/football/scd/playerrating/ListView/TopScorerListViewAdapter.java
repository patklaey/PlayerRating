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
public class TopScorerListViewAdapter extends PlayerRatingPlayerListViewAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7033234789315596145L;

	/* (non-Javadoc)
	 * @see football.scd.playerrating.ListView.PlayerRatingListViewAdapter#buildListViewAdapterContent()
	 */
	@Override
	public List<Pair<Integer, String>> buildListViewAdapterContent() {
		
		// Go through the players, sorted by goals scored
		for (Player player : PlayerStatistics.getTopScorerList() )
		{
			// All players scored the same amount of goals have the
			// same rank, so only increase the rank counter if the 
			// players goals are less than what we had before
			if ( this.current_val != player.getTotalGoals() )
				this.position++;
			
			// Remember the current amount of goals for the next
			// iteration
			this.current_val = player.getTotalGoals();
			
			// The string for goals
			String goals = (this.current_val == 1 ? "Goal" : "Goals" );
			
			// Add the composed sting to the list which will be
			// set as adapter later
			String adapterContent = this.position + ".      " + this.current_val + " " + goals + "       " +  player.toString();
			Pair<Integer, String> listContent = new Pair<Integer, String>(player.getID(), adapterContent);
			this.listViewContentList.add( listContent );
		}
		
		return this.listViewContentList;
	}

}

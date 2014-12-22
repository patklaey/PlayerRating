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
public class MinutesPerGoalListViewAdapter extends PlayerRatingPlayerListViewAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1951163009020024626L;

	/* (non-Javadoc)
	 * @see football.scd.playerrating.ListView.PlayerRatingListViewAdapter#buildListViewAdapterContent()
	 */
	@Override
	public List<Pair<Integer, String>> buildListViewAdapterContent() {

		// We need a double to remember the current value
		double minutes_per_goal = 0;
		
		// Go through the players, sorted by goals scored
		for (Player player : PlayerStatistics.getMinutesPerGoalList() )
		{
			// All players played the same minutes per goal have
			// the same rank, so only increase the rank counter if
			// the minutes per goal is more than what we had before
			if ( minutes_per_goal != player.getMinutesPerGoal() )
				this.position++;
			
			// Remember the current minutes per goal for the
			// next iteration
			minutes_per_goal = player.getMinutesPerGoal();

			// Add the composed sting to the list which will be
			// set as adapter later
			String adapterContent = this.position + ".      " + String.format("%.2f", minutes_per_goal) + "       " +  player.toString();
			Pair<Integer, String> listContent = new Pair<Integer, String>(player.getID(), adapterContent);
			this.listViewContentList.add( listContent );
		}
		
		return this.listViewContentList;
	}

}

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
public class MinutesPerGameListViewAdapter extends PlayerRatingPlayerListViewAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5003792654260793620L;

	/* (non-Javadoc)
	 * @see football.scd.playerrating.ListView.PlayerRatingListViewAdapter#buildListViewAdapterContent()
	 */
	@Override
	public List<Pair<Integer, String>> buildListViewAdapterContent() {
		
		// We need a double to remember the current value
		double minutes_per_game = 0;
		
		// Go through the players, sorted by goals scored
		for (Player player : PlayerStatistics.getMinutesPerGameList() )
		{
			// All players played the same minutes per game have
			// the same rank, so only increase the rank counter if
			// the minutes per game is more than what we had before
			if ( minutes_per_game != player.getMinutesPerGame() )
				this.position++;
			
			// Remember the current minutes per game for the
			// next iteration
			minutes_per_game = player.getMinutesPerGame();

			// Add the composed sting to the list which will be
			// set as adapter later
			String adapterContent = this.position + ".      " + String.format("%.2f", minutes_per_game) + "       " +  player.toString();
			Pair<Integer, String> listContent = new Pair<Integer, String>(player.getID(), adapterContent);
			this.listViewContentList.add( listContent );
		}
		
		return this.listViewContentList;
	}

}

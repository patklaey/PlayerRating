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
public class MVPListViewAdapter extends PlayerRatingPlayerListViewAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 895801373539010652L;

	/* (non-Javadoc)
	 * @see football.scd.playerrating.ListView.PlayerRatingListViewAdapter#buildListViewAdapterContent()
	 */
	@Override
	public List<Pair<Integer, String>> buildListViewAdapterContent() {
		
		// We need a double to remember the current value
		double average_rating = 0;
		
		// Go through the players, sorted by goals scored
		for (Player player : PlayerStatistics.getMvpList() )
		{
			// All players played the same average rating have the
			// same rank, so only increase the rank counter if the 
			// average rating are less than what we had before
			if ( average_rating != player.getAverageRating() )
				this.position++;
			
			// Remember the current average rating for the
			// next iteration
			average_rating = player.getAverageRating();

			// Add the composed sting to the list which will be
			// set as adapter later
			String adapterContent = this.position + ".      " + String.format("%.2f", average_rating) + "       " +  player.toString();
			Pair<Integer, String> listContent = new Pair<Integer, String>(player.getID(), adapterContent);
			this.listViewContentList.add( listContent );
		}
		
		return this.listViewContentList;
	}

}

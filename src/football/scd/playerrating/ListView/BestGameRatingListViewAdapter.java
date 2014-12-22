/**
 * 
 */
package football.scd.playerrating.ListView;

import java.util.List;

import android.util.Pair;

import football.scd.playerrating.Game;
import football.scd.playerrating.GameStatistics;

/**
 * @author TGDKLPA4
 *
 */
public class BestGameRatingListViewAdapter extends PlayerRatingGameListViewAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2981771996887068988L;

	/* (non-Javadoc)
	 * @see football.scd.playerrating.ListView.PlayerRatingListViewAdapter#buildListViewAdapterContent()
	 */
	@Override
	public List<Pair<Integer, String>> buildListViewAdapterContent() {
		
		// We need a double to remember the current value
		double average_match_rating = 0;

		// Go through the players, sorted by minutes per goal
		for (Game game : GameStatistics.getAverageMatchRatingList() )
		{
			// All players played the same minutes per goal have
			// the same rank, so only increase the rank counter if
			// the minutes per goal is more than what we had before
			if ( average_match_rating != game.getAverageRating() )
				this.position++;
				
			// Remember the current minutes per goal for the
			// next iteration
			average_match_rating = game.getAverageRating();

			// Add the composed sting to the list which will be
			// set as adapter later
			String adapterContent = this.position + ".      " + String.format("%.2f", average_match_rating) + "       " +  game.getOpponent() + " - " + game.getSelf_name();
			if ( game.isHomeGame() ) {
				adapterContent = this.position + ".      " + String.format("%.2f", average_match_rating) + "       " +  game.getSelf_name() + " - " + game.getOpponent();
			}
			Pair<Integer, String> listContent = new Pair<Integer, String>(game.getID(), adapterContent);
			this.listViewContentList.add( listContent );
		}
		
		return this.listViewContentList;
	}

}

/**
 * 
 */
package football.scd.playerrating.ListView;

import java.util.List;

import android.util.Pair;

import football.scd.playerrating.Player;
import football.scd.playerrating.Rating;
import football.scd.playerrating.contents.PlayersContent;

/**
 * @author TGDKLPA4
 *
 */
public class RatingsOfGameListViewAdapter extends PlayerRatingPlayerListViewAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4184426033504861659L;
	private int gameId;
	
	public RatingsOfGameListViewAdapter( int gameId )
	{
		this.gameId = gameId;
	}

	/* (non-Javadoc)
	 * @see football.scd.playerrating.ListView.PlayerRatingListViewAdapter#buildListViewAdapterContent()
	 */
	@Override
	public List<Pair<Player, String>> buildListViewAdapterContent() {
		
		// Go through all players
		for (Player player : PlayersContent.PLAYERS )
		{
			boolean playedInGame = false;
			int rating = 0;
			
			// Check if the player played in the game
			for( Rating played : player.getRatings() )
			{
				if ( played.getGameId() == this.gameId )
				{
					playedInGame = true;
					rating = played.getRating();
					continue;
				}
			}
			
			// If the player did not play in this game, skip it
			if ( ! playedInGame )
				continue;
			
			// If the player played in this game, add 
			String adapterContent = "" + rating + "  " + player.toString();
			Pair<Player, String> listContent = new Pair<Player, String>(player, adapterContent);
			this.listViewContentList.add( listContent );
		}
		
		return this.listViewContentList;
	}

}

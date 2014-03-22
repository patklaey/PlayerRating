package football.scd.playerrating.contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import football.scd.playerrating.Player;

@SuppressLint("UseSparseArrays") 
public class PlayersContent 
{
	
	/**
	 * An array of all players.
	 */
	public static List<Player> PLAYERS = new ArrayList<Player>();

	/**
	 * A map of all players, by ID.
	 */
	public static Map<Integer, Player> PLAYER_MAP = new HashMap<Integer, Player>();
	
	// Add all players from the database
	static {
		addPlayer(new Player(1, "Baeriswyl", "Livio"));
		addPlayer(new Player(2, "Berniki", "Fitim"));
		addPlayer(new Player(3, "Kolly", "Claudio"));
	}
	
	public static void addPlayer(Player player) {
		PLAYERS.add(player);
		PLAYER_MAP.put(player.getID(), player);
	}
	
	public static void updatePlayer(Player player)
	{
		// 
		//PLAYER_MAP.remove(player.getID());
		PLAYER_MAP.put(player.getID(), player);
		
		// Go through all players in the list and replace the player
		for( int i = 0; i < PLAYERS.size(); i++ )
		{
			// If the current player has the same id as the one passed
			// we need to replace it
			if ( PLAYERS.get(i).getID() == player.getID() )
			{
				PLAYERS.remove(i);
				PLAYERS.add(i, player);
				return;
			}
		}
	}

}

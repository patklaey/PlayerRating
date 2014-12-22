package football.scd.playerrating.contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import football.scd.playerrating.MainActivity;
import football.scd.playerrating.Player;
import football.scd.playerrating.PlayersFragment;

@SuppressLint("UseSparseArrays") 
public class PlayersContent 
{
	
	/**
	 * An array of all players.
	 */
	private static List<Player> PLAYERS = new ArrayList<Player>();

	/**
	 * A map of all players, by ID.
	 */
	private static Map<Integer, Player> PLAYER_MAP = new HashMap<Integer, Player>();
	
	public static void addPlayer(Player player)
	{
		// Add the player to the local maps
		PLAYERS.add(player);
		PLAYER_MAP.put(player.getID(), player);
		//PlayersFragment.updateList();
	}
	
	public static void updatePlayer(Player player)
	{
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
				PlayersFragment.updateList();
				return;
			}
		}
	}
	
	public static void addPlayers(List<Player> players)
	{
		// If players is null return 
		if ( players == null )
			return;
		
		// Add all players from the list
		for (Player player : players) 
			addPlayer(player);
	}
	
	public static void removePlayer(Player player)
	{
		// Delete the player
		PlayersContent.removePlayer( player.getID() );
	}

	public static void removePlayer(int player_ID)
	{
		// Remove the player from the PLAYER_MAP
		PLAYER_MAP.remove( player_ID );
		
		// Go through all players in the list and delete the player
		for( int i = 0; i < PLAYERS.size(); i++ )
		{
			// If the current player has the same id as the one passed
			// we need to delete it
			if ( PLAYERS.get(i).getID() == player_ID )
			{
				PLAYERS.remove(i);
				PlayersFragment.updateList();
				return;
			}
		}
	}
	
	public static void updateAllPlayers() 
	{
		Player[] all_players = PlayersContent.PLAYERS.toArray(new Player[PlayersContent.PLAYERS.size()]);
		for (Player player : all_players) 
		{
			PlayersContent.updatePlayer(player);
			MainActivity.getBackend().updatePlayer(player);
		}
	}
	
	public static Player getPlayerById(int player_id) {
		if ( player_id == -1 )
			return MainActivity.GOAL_AGAINS_PLAYER;
		
		return PlayersContent.PLAYER_MAP.get(player_id);
	}
	
	public static List<Player> getAllPlayers() {
		return PlayersContent.PLAYERS;
	}
}

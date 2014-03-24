package football.scd.playerrating.contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import football.scd.playerrating.Game;
import football.scd.playerrating.MainActivity;

@SuppressLint("UseSparseArrays") 
public class GamesContent 
{
	
	/**
	 * An array of all players.
	 */
	public static List<Game> GAMES = new ArrayList<Game>();

	/**
	 * A map of all players, by ID.
	 */
	public static Map<Integer, Game> GAME_MAP = new HashMap<Integer, Game>();
	
	public static void addGame(Game game) 
	{
		GAMES.add(game);
		GAME_MAP.put(game.getID(), game);
		
		// Check if the MainActivity.next_free_player_id needs to be increased
		if ( game.getID() >= MainActivity.next_free_game_id )
			MainActivity.next_free_game_id = game.getID() + 1;
	}

	public static void addGames(List<Game> games)
	{
		// If there are no games, return
		if (games == null)
			return;
		
		// Otherwise add all the games
		for (Game game : games)
			addGame(game);
	}
}

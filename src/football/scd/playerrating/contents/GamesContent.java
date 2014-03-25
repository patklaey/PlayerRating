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
	
	public static void updateGame(Game game)
	{
		// Replace the game in the game map
		GAME_MAP.put(game.getID(), game);
		
		// Go through all games in the list and replace the game
		for( int i = 0; i < GAMES.size(); i++ )
		{
			// If the current game has the same id as the one passed
			// we need to replace it
			if ( GAMES.get(i).getID() == game.getID() )
			{
				GAMES.remove(i);
				GAMES.add(i, game);
				return;
			}
		}
	}
	
	public static void removeGame(Game game)
	{
		// Delete the game
		GamesContent.removeGame( game.getID() );
	}
	
	public static void removeGame(int game_ID)
	{
		// Remove the game from the GAME_MAP
		GAME_MAP.remove( game_ID );
		
		// Go through all games in the list and delete the game
		for( int i = 0; i < GAMES.size(); i++ )
		{
			// If the current game has the same id as the one passed
			// we need to delete it
			if ( GAMES.get(i).getID() == game_ID )
			{
				GAMES.remove(i);
				return;
			}
		} 
	}
}

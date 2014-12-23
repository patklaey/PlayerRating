package football.scd.playerrating.contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import football.scd.playerrating.Game;
import football.scd.playerrating.GamesFragment;
import football.scd.playerrating.backend.Backend;

@SuppressLint("UseSparseArrays") 
public class GamesContent 
{
	/**
	 * The backend we're connected with
	 */
	private static Backend backend;
	
	/**
	 * An array of all players.
	 */
	private static List<Game> GAMES = new ArrayList<Game>();

	/**
	 * A map of all players, by ID.
	 */
	private static Map<Integer, Game> GAME_MAP = new HashMap<Integer, Game>();
	
	public static void setBackend(Backend new_backend) {
		GamesContent.backend = new_backend;
	}
	
	public static void initializeGamesFromBackend() {
		List<Game> backend_games = GamesContent.backend.getAllGames();
		
		if ( backend_games == null )
			return;
		
		for (Game backend_game : backend_games ) {
			GAMES.add(backend_game);
			GAME_MAP.put(backend_game.getID(), backend_game);
		}
	}
	
	public static int getMaxGameId() {
		return GamesContent.backend.getMaxGameID();
	}
	
	public static int getMaxGoalId() {
		return GamesContent.backend.getMaxGoalID();
	}
	
	public static void addGame(Game game) 
	{
		GAMES.add(game);
		GAME_MAP.put(game.getID(), game);
		GamesContent.backend.createGame(game);
		//GamesFragment.updateList();
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
				GamesContent.backend.updateGame(game);
				GamesFragment.updateList();
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
				GamesContent.backend.removeGame(game_ID);
				GamesFragment.updateList();
				return;
			}
		} 
	}
	
	public static Game getGameById(int game_id) {
		return GamesContent.GAME_MAP.get(game_id);
	}
	
	public static List<Game> getAllGames() {
		return GamesContent.GAMES;
	}
}

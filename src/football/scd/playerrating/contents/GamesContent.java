package football.scd.playerrating.contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import football.scd.playerrating.Game;

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
	
	// Add all players from the database
	static {
		addGame(new Game(1,"Sense Mitte Cb", true));
		addGame(new Game(2, "Gurmels", false));
		addGame(new Game(3, "Vully Sport Ca", false));
	}
	
	private static void addGame(Game game) {
		GAMES.add(game);
		GAME_MAP.put(game.getID(), game);
	}

}

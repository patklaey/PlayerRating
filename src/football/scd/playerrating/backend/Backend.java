package football.scd.playerrating.backend;

import java.util.List;

import football.scd.playerrating.Game;
import football.scd.playerrating.Player;

public interface Backend
{	
	// Connect to backend
	public Object connect(String server, int port, String user, String password, String db_name);

	// Disconnect from backend
	public void disconnect();
	
	// Get a player by ID
	public Player getPlayerByID(int ID);
	
	// Get all players
	public List<Player> getAllPlayers();
	
	// Get a match by ID
	public Game getGameByID(int ID);
	
	// Get all matches
	public List<Game> getAllGames();
	
	// Create a new player
	public boolean createPlayer(Player player);
	
	// Update a player
	public boolean updatePlayer(Player player);
	
	// Remove a player by ID
	public boolean removePlayer(Player player);
	
	// Remove a player by ID
	public boolean removePlayer(int ID);
	
	// Create a new game
	public boolean createGame(Game game);
	
	// Update a game
	public boolean updateGame(Game game);
	
	// Remove a game by game
	public boolean removeGame(Game game);
	
	// Remove a game by ID
	public boolean removeGame(int ID);

	public void debug();
	
}

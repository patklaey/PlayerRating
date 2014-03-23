package football.scd.playerrating.backend;

import java.util.ArrayList;
import java.util.List;

import football.scd.playerrating.Game;
import football.scd.playerrating.Player;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteBackend extends SQLiteOpenHelper implements Backend
{

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "scd_player_rating";
    
    // Table names
    private static final String PLAYERS_TABLE = "players";
    private static final String GAMES_TABLE = "games";
    private static final String GOALS_TABLE = "goals";
    private static final String PLAYED_TABLE = "played";
    
    // Key names
    private static final String KEY_ID = "ID";
    private static final String KEY_NAME = "Name";
    private static final String KEY_GIVENNAME = "Givenname";
    private static final String KEY_GOALS = "Goals";
    private static final String KEY_RATING = "Rating";
    private static final String KEY_MINUTES = "Minutes";
    private static final String KEY_PLAYER_ID = "Player_ID";
    private static final String KEY_GAME_ID = "Game_ID";
    private static final String KEY_TIME = "Time";
    private static final String KEY_OPPONENT = "Opponent";
    private static final String KEY_IS_HOME = "Is_Home";
    private static final String KEY_SELF_NAME = "Self_Name";
    private static final String KEY_SELF_GOALS = "Self_Goals";
    private static final String KEY_OPPONENT_GOALS = "Opponent_Goals";
    
    // Indexes
    private static final int INDEX_PLAYER_ID = 0;
    private static final int INDEX_PLAYER_NAME = 1;
    private static final int INDEX_PLAYER_GIVENNAME = 2;
    private static final int INDEX_PLAYER_GOALS = 3;
    private static final int INDEX_PLAYER_MINUTES = 4;
    private static final int INDEX_PLAYER_RATING = 5;

    

    // Create table strings
    private static final String CREATE_PLAYERS_TABLE = "CREATE TABLE " + PLAYERS_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_GIVENNAME + " TEXT," + KEY_GOALS + " INTEGER," + KEY_MINUTES + " INTEGER,"
                + KEY_RATING + " REAL " + ")";
    
    private static final String CREATE_GAMES_TABLE = "CREATE TABLE " + GAMES_TABLE + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_OPPONENT + " TEXT,"
            + KEY_IS_HOME + " INTEGER," + KEY_SELF_NAME + " TEXT," + KEY_SELF_GOALS + " INTEGER," 
            + KEY_OPPONENT_GOALS + " INTEGER " + ")"; 
    
    
	
	public SQLiteBackend(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// Create players table
		db.execSQL(CREATE_PLAYERS_TABLE);
		db.execSQL(CREATE_GAMES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object connect(String server, int port, String user,
			String password, String db_name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Player getPlayerByID(int ID) 
	{
		// Get a readable db access
		SQLiteDatabase db = this.getReadableDatabase();
		
		// Create the query string
		String query = "SELECT * from " + PLAYERS_TABLE + " WHERE ID=" + ID + ";";
		
		// Execute the query
		Cursor cursor = db.rawQuery(query, null);
		
		// Create a NULL player
		Player player = null;
		
		// Check the result
		if ( cursor.getColumnCount() == 1 )
		{
			// Go the the first (and only result)
			cursor.moveToFirst();
			player = playerFromCursor(cursor);
		}
		
		// Close the database connection
		db.close();
		
		// Return the player
		return player;
	}

	@Override
	public List<Player> getAllPlayers() 
	{
		// Get a readable db access
		SQLiteDatabase db = this.getReadableDatabase();
		
		// Create the query string
		String query = "SELECT * from " + PLAYERS_TABLE + ";";
		
		// Execute the query
		Cursor cursor = db.rawQuery(query, null);
		
		// Create the players list
		List<Player> players = new ArrayList<Player>();
		
		// Go through all results and add the player to the list
		if ( ! (cursor.moveToFirst()) )
			return null;
		
		do
		{
			// Add the player from the current row to the list
            players.add(playerFromCursor(cursor));
            
        } while (cursor.moveToNext());
		
		// Close the database connection
		db.close();
		
		return players;
	}

	@Override
	public Game getGameByID(int ID) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Game> getAllGames()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createPlayer(Player player)
	{
		// Get writable access to the database
		SQLiteDatabase db = this.getWritableDatabase();
		
		// Create the attributes
		ContentValues attributes = new ContentValues();
		
		// Add all players attributes
		attributes.put(KEY_ID, player.getID());
		attributes.put(KEY_NAME, player.getName());
		attributes.put(KEY_GIVENNAME, player.getGivenname());
		attributes.put(KEY_GOALS, player.getGoals());
		attributes.put(KEY_MINUTES, player.getMinutes());
		attributes.put(KEY_RATING, player.getRating());

		// Insert the player into the database
		long success = db.insert(PLAYERS_TABLE, null, attributes);
		
		// Close the database connection
		db.close();
		
		if (success == -1 )
			return false;
		
		return true;
	}

	@Override
	public boolean updatePlayer(Player player)
	{
		// Get writable access to the database
	    SQLiteDatabase db = this.getWritableDatabase();
	    
		// Create the attributes
	    ContentValues attributes = new ContentValues();
	    
		// Add all players attributes
		attributes.put(KEY_NAME, player.getName());
		attributes.put(KEY_GIVENNAME, player.getGivenname());
		attributes.put(KEY_GOALS, player.getGoals());
		attributes.put(KEY_MINUTES, player.getMinutes());
		attributes.put(KEY_RATING, player.getRating());
	 
	    // updating row
	    long success = db.update(PLAYERS_TABLE, attributes, KEY_ID + " = ?",
	            new String[] { String.valueOf(player.getID()) });
	    
	    if ( success == -1 )
	    	return false;
	    
	    return true;
	}

	@Override
	public boolean createGame(Game game)
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean updateGame(Game game)
	{
		// TODO Auto-generated method stub
		return true;
	}
	
	public Player playerFromCursor(Cursor cursor)
	{
		Player player = new Player(cursor.getInt(INDEX_PLAYER_ID));
		player.setName(cursor.getString(INDEX_PLAYER_NAME));
		player.setGivenname(cursor.getString(INDEX_PLAYER_GIVENNAME));
		player.setGoals(cursor.getInt(INDEX_PLAYER_GOALS));
		player.setMinutes(cursor.getInt(INDEX_PLAYER_MINUTES));
		player.setRating(cursor.getFloat(INDEX_PLAYER_RATING));
		return player;
	}
	
	
}

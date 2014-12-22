package football.scd.playerrating.backend;

import java.util.ArrayList;
import java.util.List;

import football.scd.playerrating.Game;
import football.scd.playerrating.Goal;
import football.scd.playerrating.MainActivity;
import football.scd.playerrating.Minute;
import football.scd.playerrating.Player;
import football.scd.playerrating.Rating;
import football.scd.playerrating.contents.PlayersContent;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("UseSparseArrays")
public class SQLiteBackend extends SQLiteOpenHelper implements Backend
{

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
 
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
//    private static final String KEY_GOALS = "Goals";
    private static final String KEY_RATING = "Rating";
    private static final String KEY_OPPONENT = "Opponent";
    private static final String KEY_IS_HOME = "Is_Home";
    private static final String KEY_SELF_NAME = "Self_Name";
    private static final String KEY_SELF_GOALS = "Self_Goals";
    private static final String KEY_OPPONENT_GOALS = "Opponent_Goals";
    private static final String KEY_FINISHED = "Finished";
    private static final String KEY_PLAYER_ID = "Player_ID";
    private static final String KEY_GAME_ID = "Game_ID";
    private static final String KEY_TIME = "Time";
    
    // Indexes
    private static final int INDEX_PLAYER_ID = 0;
    private static final int INDEX_PLAYER_NAME = 1;
    private static final int INDEX_PLAYER_GIVENNAME = 2;
    private static final int INDEX_GAME_ID = 0;
    private static final int INDEX_GAME_OPPONENT = 1;
    private static final int INDEX_GAME_IS_HOME = 2;
    private static final int INDEX_GAME_SELF_NAME = 3;
    private static final int INDEX_GAME_SELF_GOALS = 4;
    private static final int INDEX_GAME_OPPONENT_GOALS = 5;
    private static final int INDEX_GAME_FINISHED = 6;
//    private static final int INDEX_PLAYED_ID = 0;
//    private static final int INDEX_PLAYED_PLAYER_ID = 1;
    private static final int INDEX_PLAYED_GAME_ID = 2;
    private static final int INDEX_PLAYED_TIME = 3;
    private static final int INDEX_PLAYED_RATING = 4;
    private static final int INDEX_GOAL_ID = 0;
    private static final int INDEX_GOAL_PLAYER_ID = 1;
    private static final int INDEX_GOAL_GAME_ID = 2;
    private static final int INDEX_GOAL_MINUTE = 3;

    
    // Create table strings
    private static final String CREATE_PLAYERS_TABLE = "CREATE TABLE " + PLAYERS_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_GIVENNAME + " TEXT " + ")";
    
    private static final String CREATE_GAMES_TABLE = "CREATE TABLE " + GAMES_TABLE + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_OPPONENT + " TEXT,"
            + KEY_IS_HOME + " INTEGER," + KEY_SELF_NAME + " TEXT," + KEY_SELF_GOALS + " INTEGER," 
            + KEY_OPPONENT_GOALS + " INTEGER," + KEY_FINISHED + " INTEGER " + ")"; 
    
    private static final String CREATE_PLAYED_TABLE = "CREATE TABLE " + PLAYED_TABLE + "("
    		+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PLAYER_ID + " INTEGER," + KEY_GAME_ID + " INTEGER," 
    		+ KEY_TIME + " INTEGER," + KEY_RATING + " INTEGER " + ")";
    
    private static final String CREATE_GOALS_TABLE = "CREATE TABLE " + GOALS_TABLE + "("
    		+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PLAYER_ID + " INTEGER," + KEY_GAME_ID + " INTEGER,"
    		+ KEY_TIME + " INTEGER " + ")";
    
    // Drop table strings
    private static final String DROP_PLAYERS_TABLE = "DROP TABLE IF EXISTS " + PLAYERS_TABLE;
    private static final String DROP_GAMES_TABLE = "DROP TABLE IF EXISTS " + GAMES_TABLE;
    private static final String DROP_PLAYED_TABLE = "DROP TABLE IF EXISTS " + PLAYED_TABLE;
    private static final String DROP_GOALS_TABLE = "DROP TABLE IF EXISTS " + GOALS_TABLE;
	
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
		db.execSQL(CREATE_PLAYED_TABLE);
		db.execSQL(CREATE_GOALS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop the tables
		db.execSQL(DROP_PLAYERS_TABLE);
		db.execSQL(DROP_GAMES_TABLE);
		db.execSQL(DROP_PLAYED_TABLE);
		db.execSQL(DROP_GOALS_TABLE);
		
		// Recreate the tables
		this.onCreate(db);
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
		String query = "SELECT * from " + PLAYERS_TABLE + " WHERE " + KEY_ID + "=" + ID + ";";
		
		// Execute the query
		Cursor cursor = db.rawQuery(query, null);
		
		// Create a NULL player
		Player player = null;
		
		// Check the result
		if ( cursor.moveToFirst())
		{
			// Go the the first (and only result)
			player = playerFromCursor(cursor);
		}
		
		// Get the players minutes and ratings
		query = "SELECT * FROM " + PLAYED_TABLE + " WHERE " + KEY_PLAYER_ID + "=" + ID + ";";
		
		// Execute the query
		cursor = db.rawQuery(query, null);
		
		// Create the hashmaps
		List<Rating> ratings = new ArrayList<Rating>();
		List<Minute> minutes = new ArrayList<Minute>();
		
		// If the player already played in games, get the data
		if ( cursor.moveToFirst() )
		{
			do
			{
				// Get the different values
				int game_id = cursor.getInt(INDEX_PLAYED_GAME_ID);
				int time = cursor.getInt(INDEX_PLAYED_TIME);
				int rating = cursor.getInt(INDEX_PLAYED_RATING);
				
				// Add the corresponding minute object to the list
				minutes.add( new Minute(ID, game_id, time) );
				
				// Add the corresponding rating object to the list
				ratings.add( new Rating(ID, game_id, rating) );
				
			} while ( cursor.moveToNext() );
		}
		
		// Add the hash maps to the player object
		player.setRatings(ratings);
		player.setMinutes(minutes);
		
		// Get the players goals
		query = "SELECT * FROM " + GOALS_TABLE + " WHERE " + KEY_PLAYER_ID + "=" + ID + ";";
		
		// Execute the query
		cursor = db.rawQuery(query, null);
		
		// Create a empty list
		List<Goal> goals = new ArrayList<Goal>();
		
		// If the player already played in games, get the data
		if ( cursor.moveToFirst() )
		{
			do
			{
				// Get the goals minute and game_id
				int minute = cursor.getInt(INDEX_GOAL_MINUTE);
				int game_id = cursor.getInt(INDEX_GOAL_GAME_ID);
				int id = cursor.getInt(INDEX_GOAL_ID);
				
				// Add the goal to the list
				goals.add(new Goal(id, minute, player.getID() , game_id ));
			} while ( cursor.moveToNext() );
		}
		
		// Add the goals list to the player
		player.setGoals(goals);
		
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
		String query = "SELECT " + KEY_ID + " from " + PLAYERS_TABLE + ";";
		
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
            players.add( this.getPlayerByID(cursor.getInt(INDEX_PLAYER_ID)) );
            
        } while (cursor.moveToNext());
		
		// Close the database connection
		db.close();
		
		return players;
	}

	@Override
	public Game getGameByID(int ID) 
	{
		// Get a readable db access
		SQLiteDatabase db = this.getReadableDatabase();
		
		// Create the query string
		String query = "SELECT * from " + GAMES_TABLE + " WHERE ID=" + ID + ";";
		
		// Execute the query
		Cursor cursor = db.rawQuery(query, null);
		
		// Create a NULL game
		Game game = null;
		
		// Check the result
		if ( cursor.moveToFirst() )
		{
			// Go the the first (and only result)
			game = gameFromCursor(cursor);
		}
		
		// Get the goals belonging to this game
		query = "SELECT * FROM " + GOALS_TABLE + " WHERE " + KEY_GAME_ID + "=" + ID + ";";
		
		// Execute the query
		cursor = db.rawQuery(query, null);
		
		// Create the lists
		List<Goal> goals_scored = new ArrayList<Goal>();
		List<Goal> goals_conceded = new ArrayList<Goal>();
		
		// If the player already played in games, get the data
		if ( cursor.moveToFirst() )
		{
			do
			{
				// Get the goals minute, player id and game id
				int min = cursor.getInt(INDEX_GOAL_MINUTE);
				int player_id = cursor.getInt(INDEX_GOAL_PLAYER_ID);
				int id = cursor.getInt(INDEX_GOAL_ID);
				
				// If it is a goal scored (i.e. player id is not -1) add it 
				// to the goals scored list, otherwise add it to the goals
				// conceded list
				if ( player_id != MainActivity.GOAL_AGAINS_PLAYER.getID() )
				{
					goals_scored.add(new Goal(id, min, PlayersContent.getPlayerById(player_id).getID(), game.getID() ));
				} else
				{
					goals_conceded.add( new Goal(id, min, MainActivity.GOAL_AGAINS_PLAYER.getID(), game.getID() ) );
				}
				
			} while ( cursor.moveToNext() );
			
			// Add the games scored and conceded goals
			game.setGoalsConceded(goals_conceded);
			game.setGoalsScored(goals_scored);
		}
				
		// Close the database connection
		db.close();
		
		// Return the player
		return game;
	}

	@Override
	public List<Game> getAllGames()
	{
		// Get a readable db access
		SQLiteDatabase db = this.getReadableDatabase();
		
		// Create the query string
		String query = "SELECT " + KEY_ID + " from " + GAMES_TABLE + ";";
		
		// Execute the query
		Cursor cursor = db.rawQuery(query, null);
		
		// Create the games list
		List<Game> games = new ArrayList<Game>();
		
		// Go through all results and add the player to the list
		if ( ! (cursor.moveToFirst()) )
			return null;
		
		do
		{
			// Add the player from the current row to the list
            games.add( this.getGameByID( (cursor.getInt(INDEX_GAME_ID) ) ) );
            
        } while (cursor.moveToNext());
		
		// Close the database connection
		db.close();
		
		return games;
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

		// Insert the player into the database
		long success = db.insert(PLAYERS_TABLE, null, attributes);
		
		// Close the db connection and return if the player could not be added
		if ( success == -1 )
		{
			db.close();
			return false;
		}
		
		// If the player does not have and minutes and ratings, close the db
		// connection and return
		if ( player.getMinutes() == null || player.getMinutes().size() == 0 )
		{
			db.close();
			return true;
		}
		
		// Insert the players minutes and ratings
		for ( Minute minute : player.getMinutes() )
		{
			ContentValues played = new ContentValues();
			played.put(KEY_PLAYER_ID, player.getID() );
			played.put(KEY_GAME_ID, minute.getGameId() );
			played.put(KEY_TIME, minute.getMinutes() );
			
			// Search for the corresponding rating object and add the rating 
			// to the value set
			for ( Rating rating : player.getRatings() )
			{
				if ( rating.getGameId() == minute.getGameId() )
					played.put(KEY_RATING, rating.getRating() );
			}
			
			// Add the entry to the db
			db.insert(PLAYED_TABLE, null, played);
	
		}
		
		// Insert the players goals 
		for (Goal goal : player.getGoals() )
		{
			// Check if this goal already exists (since the goals will be 
			// written by the game too)
			String query = "SELECT * FROM " + GOALS_TABLE + " WHERE " + KEY_GAME_ID
					     + "=" + goal.getGameId() + " AND " + KEY_PLAYER_ID + "=" 
					     + player.getID() + " AND " + KEY_TIME + "=" + goal.getMinute();
			
			// Execute the query
			Cursor cursor = db.rawQuery(query, null);
			
			if ( ! cursor.moveToFirst() )
			{
				ContentValues pl_goal = new ContentValues();
				pl_goal.put(KEY_PLAYER_ID, player.getID());
				pl_goal.put(KEY_GAME_ID, goal.getGameId());
				pl_goal.put(KEY_TIME, goal.getMinute() );
				
				// Add the entry to the db
				db.insert(GOALS_TABLE, null, pl_goal);
			}
		}	
		
		// Close the database connection
		db.close();
		
		if (success == -1 )
			return false;
		
		return true;
	}

	@Override
	public boolean updatePlayer(Player player)
	{
		// Remove and recreate the player
		this.removePlayer(player);
		this.createPlayer(player);
	    
	    return true;
	}

	@Override
	public boolean createGame(Game game)
	{
		// Get writable access to the database
		SQLiteDatabase db = this.getWritableDatabase();
		
		// Create the attributes
		ContentValues attributes = new ContentValues();
		
		// Add all players attributes
		attributes.put(KEY_ID, game.getID());
		attributes.put(KEY_OPPONENT, game.getOpponent());
		attributes.put(KEY_SELF_NAME, game.getSelf_name());
		attributes.put(KEY_SELF_GOALS, game.getSelf_score());
		attributes.put(KEY_OPPONENT_GOALS, game.getOpponent_score());
		attributes.put(KEY_FINISHED, game.isFinished());
		attributes.put(KEY_IS_HOME, game.isHomeGame());

		// Insert the player into the database
		long success = db.insert(GAMES_TABLE, null, attributes);
		
		// If the game could not be created, stop here
		if (success == -1 )
		{
			// Close the database connection
			db.close();
			return false;
		}
		
		// Otherwise add all goals to the goals table
		// If there are no goals, we can stop here
		if ( game.getGoalsConceded().size() == 0 && game.getGoalsScored().size() == 0 )
		{
			// Close the database connection
			db.close();
			return true;
		}
		
		// If there are goals, add them to the table
		for (Goal self_goal : game.getGoalsScored() ) 
		{
			ContentValues goal = new ContentValues();
			goal.put(KEY_ID, self_goal.getID() );
			goal.put(KEY_PLAYER_ID, self_goal.getPlayerId());
			goal.put(KEY_GAME_ID, self_goal.getGameId() );
			goal.put(KEY_TIME, self_goal.getMinute() );
			
			// Add the entry to the db
			db.insert(GOALS_TABLE, null, goal);
		}
		
		// Also add the goals conceded
		for (Goal goal_against : game.getGoalsConceded() ) 
		{
			ContentValues goal = new ContentValues();
			goal.put(KEY_ID, goal_against.getID() );
			goal.put(KEY_PLAYER_ID, goal_against.getPlayerId() );
			goal.put(KEY_GAME_ID, goal_against.getGameId() );
			goal.put(KEY_TIME, goal_against.getMinute() );
			
			// Add the entry to the db
			db.insert(GOALS_TABLE, null, goal);
		}
				
		return true;
	}

	@Override
	public boolean updateGame(Game game)
	{
		// Simply remove and readd the game
		this.removeGame(game);
		this.createGame(game);
	    
	    return true;
	}
	
	private Player playerFromCursor(Cursor cursor)
	{
		Player player = new Player(cursor.getInt(INDEX_PLAYER_ID));
		player.setName(cursor.getString(INDEX_PLAYER_NAME));
		player.setGivenname(cursor.getString(INDEX_PLAYER_GIVENNAME));
		return player;
	}
	
	private Game gameFromCursor(Cursor cursor)
	{
		Game game = new Game(cursor.getInt(INDEX_GAME_ID), null, null, false);
		game.setOpponent(cursor.getString(INDEX_GAME_OPPONENT));
		game.setOpponent_score(cursor.getInt(INDEX_GAME_OPPONENT_GOALS));
		game.setSelf_score(cursor.getInt(INDEX_GAME_SELF_GOALS));
		game.setSelf_name(cursor.getString(INDEX_GAME_SELF_NAME));
		
		if ( cursor.getInt(INDEX_GAME_IS_HOME) == 1 )
			game.setIsHomeGame(true);
		
		if ( cursor.getInt(INDEX_GAME_FINISHED) == 1 )
			game.setFinished(true);
		
		return game;
	}

	@Override
	public boolean removePlayer(Player player) 
	{
		return this.removePlayer( player.getID() );
	}

	@Override
	public boolean removeGame(Game game) 
	{
		return this.removeGame( game.getID() );
	}

	@Override
	public boolean removePlayer(int ID) 
	{
		// Get writable database access
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    // Delete the given player
	    long success = db.delete(PLAYERS_TABLE, KEY_ID + " = ?",
	            new String[] { String.valueOf( ID ) });
	    
	    // Delete all the players ratings and minutes
	    success = db.delete(PLAYED_TABLE, KEY_PLAYER_ID + " = ?", 
	    		new String[] { String.valueOf( ID ) } );

	    // Delete all the players goals
	    success = db.delete(GOALS_TABLE, KEY_PLAYER_ID + " = ?", 
	    		new String[] { String.valueOf( ID ) } );
	    
	    db.close();
	    
	    if ( success >= 1 )
	    	return true;
	    
		return false;
	}

	@Override
	public boolean removeGame(int ID)
	{
		// Get writable database access
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    // Delete the given player
	    long success = db.delete(GAMES_TABLE, KEY_ID + " = ?",
	            new String[] { String.valueOf( ID ) });
	    
	    // Delete all the goals belonging to this game
	    success = db.delete(GOALS_TABLE, KEY_GAME_ID + " = ?", 
	    		new String[] { String.valueOf( ID ) } );
	    
	    // Delete all the ratings and minutes belonging to this game
	    success = db.delete(PLAYED_TABLE, KEY_GAME_ID + " = ?",  
	    		new String[] { String.valueOf(ID)} );
	    
	    db.close();
	    
	    if ( success == 1 )
	    	return true;
	    
		return false;
	}
	
	public void debug()
	{
		// Get writable database access
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    // Create the query string
	 	String query = "SELECT * from " + PLAYERS_TABLE + ";";
	 		
	 	// Execute the query
	 	Cursor cursor = db.rawQuery(query, null);
	 	
	 	cursor.moveToFirst();
	 	
		do
		{
			// Add the player from the current row to the list
			Log.d("database", cursor.getString( INDEX_PLAYER_NAME ) + " " + cursor.getInt(INDEX_PLAYER_ID));
            
        } while (cursor.moveToNext());
		
		query = "SELECT * from " + GOALS_TABLE + ";";
 		
	 	// Execute the query
	 	cursor = db.rawQuery(query, null);
	 	
	 	cursor.moveToFirst();
	 	
		do
		{
			// Add the player from the current row to the list
			Log.d("database", cursor.getInt( 0 ) + " " + cursor.getInt(INDEX_GOAL_PLAYER_ID));
            
        } while (cursor.moveToNext());
		
	    //ContentValues args = new ContentValues();
	    //args.put(KEY_PLAYER_ID, "17");
	    //db.update(GOALS_TABLE, args, KEY_ID + "=12" , null);
	}

	@Override
	public int getMaxGameID()
	{
		// Get a readable db access
		SQLiteDatabase db = this.getReadableDatabase();
		
		// The query string
		String query = "SELECT MAX(" + KEY_ID + ") FROM " + GAMES_TABLE;
		
		// Execute the query
		Cursor cursor = db.rawQuery(query, null);
		
		if ( ! cursor.moveToFirst() )
			return 0;
		
		return cursor.getInt(0);
	}

	@Override
	public int getMaxPlayerID() 
	{
		// Get a readable db access
		SQLiteDatabase db = this.getReadableDatabase();
		
		// The query string
		String query = "SELECT MAX(" + KEY_ID + ") FROM " + PLAYERS_TABLE;
		
		// Execute the query
		Cursor cursor = db.rawQuery(query, null);
		
		if ( ! cursor.moveToFirst() )
			return 0;
		
		return cursor.getInt(0);
	}

	@Override
	public int getMaxGoalID()
	{
		// Get a readable db access
		SQLiteDatabase db = this.getReadableDatabase();
		
		// The query string
		String query = "SELECT MAX(" + KEY_ID + ") FROM " + GOALS_TABLE;
		
		// Execute the query
		Cursor cursor = db.rawQuery(query, null);
		
		if ( ! cursor.moveToFirst() )
			return 0;
		
		return cursor.getInt(0);
	}
	
}

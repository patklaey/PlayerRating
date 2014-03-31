package football.scd.playerrating;

import android.annotation.SuppressLint;
import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class Player
{
	
	private int ID;
	private String name;
	private String givenname;
	private int goals;
	private int current_game_minutes;
	
	// Hashmap for the players ratings
	private HashMap<Integer, Integer> ratings;
	
	// Hashmap for the players minutes
	private HashMap<Integer, Integer> minutes;
	

	private boolean playing;
	
	/**
	 * @param ID
	 * @param name
	 * @param givenname
	 */
	public Player(int _ID, String _name, String _givenname)
	{
		super();
		this.ID = _ID;
		this.name = _name;
		this.givenname = _givenname;
		this.goals = 0;
		this.minutes = new HashMap<Integer, Integer>();
		this.ratings = new HashMap<Integer, Integer>();
		this.playing = false;
		this.current_game_minutes = 0;
	}
	
	/**
	 * @param ID
	 */
	public Player(int _ID) 
	{
		super();
		this.ID = _ID;
		this.givenname = "John";
		this.name = "Doe";
		this.minutes = new HashMap<Integer, Integer>();
		this.goals = 0;
		this.ratings = new HashMap<Integer, Integer>();
		this.playing = false;
		this.current_game_minutes = 0;
	}

	/**
	 * @return the current_game_minutes
	 */
	public int getCurrentGameMinutes()
	{
		return current_game_minutes;
	}

	/**
	 * @param current_game_minutes the current_game_minutes to set
	 */
	public void setCurrentGameMinutes(int current_game_minutes)
	{
		this.current_game_minutes = current_game_minutes;
	}

	/**
	 * @return the ratings
	 */
	public HashMap<Integer, Integer> getRatings() 
	{
		return ratings;
	}

	/**
	 * @param ratings the ratings to set
	 */
	public void setRatings(HashMap<Integer, Integer> ratings) 
	{
		this.ratings = ratings;
	}

	/**
	 * @return the minutes
	 */
	public HashMap<Integer, Integer> getMinutes() 
	{
		return minutes;
	}

	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(HashMap<Integer, Integer> minutes) 
	{
		this.minutes = minutes;
	}

	/**
	 * @return the playing
	 */
	public boolean isPlaying()
	{
		return playing;
	}

	/**
	 * @param playing the playing to set
	 */
	public void setPlaying(boolean playing) 
	{
		this.playing = playing;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		return this.name + " " + this.givenname;
	}

	// Getters and Setters
	/**
	 * @return the goals
	 */
	public int getGoals() 
	{
		return goals;
	}

	/**
	 * @param goals the goals to set
	 */
	public void setGoals(int goals)
	{
		this.goals = goals;
	}
	
	public int getID() 
	{
		return ID;
	}

	public void setID(int iD) 
	{
		ID = iD;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getGivenname() 
	{
		return givenname;
	}

	public void setGivenname(String givenname) 
	{
		this.givenname = givenname;
	}

}

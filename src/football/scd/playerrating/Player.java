package football.scd.playerrating;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class Player implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8163010295132771740L;
	
	private int ID;
	private String name;
	private String givenname;
	private List<Goal> goals;
	private int current_game_minutes;
	private int current_game_goals;
	
	// Hashmap<GameID,Rating> for the players ratings
	private HashMap<Integer, Integer> ratings;
	
	// Hashmap<GameID,MinutesPlayed> for the players minutes
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
		this.goals = new ArrayList<Goal>();
		this.minutes = new HashMap<Integer, Integer>();
		this.ratings = new HashMap<Integer, Integer>();
		this.playing = false;
		this.current_game_minutes = 0;
		this.current_game_goals = 0;
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
		this.goals = new ArrayList<Goal>();
		this.ratings = new HashMap<Integer, Integer>();
		this.playing = false;
		this.current_game_minutes = 0;
		this.current_game_goals = 0;
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
	public List<Goal> getGoals() 
	{
		return goals;
	}

	/**
	 * @param goals the goals to set
	 */
	public void setGoals(List<Goal> goals)
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

	/**
	 * @return the current_game_goals
	 */
	public int getCurrentGameGoals() 
	{
		return current_game_goals;
	}

	/**
	 * @param current_game_goals the current_game_goals to set
	 */
	public void setCurrentGameGoals(int current_game_goals)
	{
		this.current_game_goals = current_game_goals;
	}
	
	public int getTotalGoals()
	{
		return this.getGoals().size();
	}
	
	public int getTotalMinutes()
	{
		// If there are no entries in the minutes hashtable return zero
		if ( this.getMinutes() == null || this.getMinutes().size() == 0 )
			return 0;
		
		// Temp var for total minutes
		int total_minutes = 0;
		
		// Sum up the minutes played
		for (Map.Entry<Integer, Integer> entry : this.getMinutes().entrySet())
			total_minutes += entry.getValue();
		
		// And return them
		return total_minutes;
	}

	public double getAverageRating()
	{
		// If there are no entries in the minutes hashtable return zero
		if ( this.getRatings() == null || this.getRatings().size() == 0 )
			return 0;
		
		// Temp var for average rating
		double average_rating = 0;
		
		// Sum up the ratings
		for (Map.Entry<Integer, Integer> entry : this.getRatings().entrySet())
			average_rating += entry.getValue();
		
		// And divide it by the number of ratings
		average_rating = average_rating / this.getRatings().size();
		
		// And return them
		return average_rating;
	}
	
	public void addGoal(Goal goal)
	{
		this.getGoals().add(goal);
	}
	
	public double getMinutesPerGoal()
	{
		// If the player did not score any goals, return infinity
		if ( this.getTotalGoals() == 0)
			return Double.POSITIVE_INFINITY;
		
		// Otherwise return the minutes per goal
		return this.getTotalMinutes() / this.getTotalGoals();
	}
}

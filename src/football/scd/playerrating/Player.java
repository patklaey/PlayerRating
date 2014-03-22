package football.scd.playerrating;

public class Player
{
	
	private int ID;
	private String name;
	private String givenname;
	private int goals;
	private int minutes;
	private float rating;
	
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
		this.minutes = 0;
		this.rating = 0;
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
		this.minutes = 0;
		this.goals = 0;
		this.rating = 0;
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

	/**
	 * @return the minutes
	 */
	public int getMinutes()
	{
		return minutes;
	}

	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(int minutes)
	{
		this.minutes = minutes;
	}

	/**
	 * @return the rating
	 */
	public float getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(float rating)
	{
		this.rating = rating;
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

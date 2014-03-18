package football.scd.playerrating;

public class Player {
	
	private int ID;
	private String name;
	private String givenname;
	
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
	}
	
	/**
	 * @param ID
	 */
	public Player(int _ID) 
	{
		super();
		this.ID = _ID;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Player [ID=" + ID + ", name=" + name + ", givenname="
				+ givenname + "]";
	}

	public String fullName()
	{
		return this.name + " " + this.givenname;
	}

	// Getters and Setters
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

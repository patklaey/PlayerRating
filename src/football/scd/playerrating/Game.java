/**
 * 
 */
package football.scd.playerrating;

/**
 * @author uni
 *
 */
public class Game 
{
	private int ID;
	private String opponent;
	private boolean is_home_game;
	
	/**
	 * @param ID
	 * @param opponent
	 * @param is_home_game
	 */
	public Game(int _ID, String _opponent, boolean _is_home_game)
	{
		super();
		this.ID = _ID;
		this.opponent = _opponent;
		this.is_home_game = _is_home_game;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Game [ID=" + ID + ", opponent=" + opponent + ", is_home_game="
				+ is_home_game + "]";
	}

	/**
	 * @return the iD
	 */
	public int getID() 
	{
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) 
	{
		ID = iD;
	}

	/**
	 * @return the opponent
	 */
	public String getOpponent() 
	{
		return opponent;
	}

	/**
	 * @param opponent the opponent to set
	 */
	public void setOpponent(String opponent) 
	{
		this.opponent = opponent;
	}

	/**
	 * @return the is_home_game
	 */
	public boolean isHomeGame() 
	{
		return is_home_game;
	}

	/**
	 * @param is_home_game the is_home_game to set
	 */
	public void setIsHomeGame(boolean is_home_game) 
	{
		this.is_home_game = is_home_game;
	}
	
	
}

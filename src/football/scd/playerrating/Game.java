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
	private String self_name;
	private boolean is_home_game;
	private int self_goals;
	private int opponent_goals;

	
	
	/**
	 * @param ID
	 * @param opponent
	 * @param self_name
	 * @param is_home_game
	 */
	public Game(int _ID, String _self_name, String _opponent, boolean _is_home_game) {
		super();
		this.ID = _ID;
		this.opponent = _opponent;
		this.self_name = _self_name;
		this.is_home_game = _is_home_game;
		this.self_goals = 0;
		this.opponent_goals = 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		if ( this.isHomeGame() )
			return this.getSelf_name() + " " + this.getSelf_goals() + " : " + 
				    this.getOpponent_goals() + " " + this.getOpponent();
		
		return this.getOpponent() + " " + this.getOpponent_goals() + " : " + 
	            this.getSelf_goals() + " " + this.getSelf_name();
	}

	// Getters and setters
	/**
	 * @return the self_name
	 */
	public String getSelf_name() {
		return self_name;
	}

	/**
	 * @param self_name the self_name to set
	 */
	public void setSelf_name(String self_name) {
		this.self_name = self_name;
	}

	/**
	 * @return the self_goals
	 */
	public int getSelf_goals() {
		return self_goals;
	}

	/**
	 * @param self_goals the self_goals to set
	 */
	public void setSelf_goals(int self_goals) {
		this.self_goals = self_goals;
	}

	/**
	 * @return the opponent_goals
	 */
	public int getOpponent_goals() {
		return opponent_goals;
	}

	/**
	 * @param opponent_goals the opponent_goals to set
	 */
	public void setOpponent_goals(int opponent_goals) {
		this.opponent_goals = opponent_goals;
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

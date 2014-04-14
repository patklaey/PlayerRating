/**
 * 
 */
package football.scd.playerrating;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author uni
 *
 */
@SuppressLint("UseSparseArrays")
public class Game implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7187483443182132491L;
	private int ID;
	private String opponent;
	private String self_name;
	private boolean is_home_game;
	private boolean finished;
	private int self_score;
	private int opponent_score;
	private List<Goal> goals_scored;
	private List<Goal> goals_conceded;

	
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
		this.finished = false;
		this.self_score = 0;
		this.opponent_score = 0;
		this.goals_conceded = new ArrayList<Goal>();
		this.goals_scored = new ArrayList<Goal>();
	}
	
	/**
	 * @return the finished
	 */
	public boolean isFinished() 
	{
		return finished;
	}

	/**
	 * @param finished the finished to set
	 */
	public void setFinished(boolean finished) 
	{
		this.finished = finished;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		if ( this.isHomeGame() )
			return this.getSelf_name() + " " + this.getSelf_score() + " : " + 
				    this.getOpponent_score() + " " + this.getOpponent();
		
		return this.getOpponent() + " " + this.getOpponent_score() + " : " + 
	            this.getSelf_score() + " " + this.getSelf_name();
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

	/**
	 * @return the self_score
	 */
	public int getSelf_score() {
		return self_score;
	}

	/**
	 * @param self_score the self_score to set
	 */
	public void setSelf_score(int self_score) {
		this.self_score = self_score;
	}

	/**
	 * @return the opponent_score
	 */
	public int getOpponent_score()
	{
		return opponent_score;
	}

	/**
	 * @param opponent_score the opponent_score to set
	 */
	public void setOpponent_score(int opponent_score)
	{
		this.opponent_score = opponent_score;
	}
	
	/**
	 * @return the goals_scored
	 */
	public List<Goal> getGoalsScored()
	{
		return goals_scored;
	}

	/**
	 * @param goals_scored the goals_scored to set
	 */
	public void setGoalsScored(List<Goal> goals_scored)
	{
		this.goals_scored = goals_scored;
	}

	/**
	 * @return the goals_conceded
	 */
	public List<Goal> getGoalsConceded()
	{
		return goals_conceded;
	}

	/**
	 * @param goals_conceded the goals_conceded to set
	 */
	public void setGoalsConceded(List<Goal> goals_conceded) 
	{
		this.goals_conceded = goals_conceded;
	}
	
	public void addGoal(Goal goal)
	{
    	this.getGoalsScored().add(goal);

	}
	
}

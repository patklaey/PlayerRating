package football.scd.playerrating;

import java.io.Serializable;

import football.scd.playerrating.contents.PlayersContent;

public class Goal implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6563522635304334751L;
	private int minute;
	private int player_id;
	private int game_id;
	private int id;
	
	public Goal( int id, int min, int pl, int game )
	{
		this.id = id;
		this.minute = min;
		this.player_id = pl;
		this.game_id = game;
	}
	
	@Override
	public String toString()
	{
		return this.minute + "' " + PlayersContent.getPlayerById(this.player_id).toString();
	}

	/**
	 * @return the game_id
	 */
	public int getGameId()
	{
		return game_id;
	}

	/**
	 * @param game_id the game_id to set
	 */
	public void setGameId(int game_id)
	{
		this.game_id = game_id;
	}

	/**
	 * @return the minute
	 */
	public int getMinute()
	{
		return minute;
	}

	/**
	 * @param minute the minute to set
	 */
	public void setMinute(int minute)
	{
		this.minute = minute;
	}

	/**
	 * @return the player_id
	 */
	public int getPlayerId() 
	{
		return player_id;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayerId(int player_id)
	{
		this.player_id = player_id;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}
}

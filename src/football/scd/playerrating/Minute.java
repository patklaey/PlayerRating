package football.scd.playerrating;

import java.io.Serializable;

import football.scd.playerrating.contents.GamesContent;

public class Minute implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2296895576640112902L;
	private int player_id;
	private int game_id;
	private int minutes;
	
	/**
	 * @param player_id
	 * @param game_id
	 * @param minutes
	 */
	public Minute(int player_id, int game_id, int minutes)
	{
		this.player_id = player_id;
		this.game_id = game_id;
		this.minutes = minutes;
	}

	/**
	 * @return the player_id
	 */
	public int getPlayerId()
	{
		return player_id;
	}

	/**
	 * @param player_id the player_id to set
	 */
	public void setPlayerId(int player_id)
	{
		this.player_id = player_id;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		return GamesContent.GAME_MAP.get( this.getGameId() ).getOpponent() + ": " + this.getMinutes();
	}


}

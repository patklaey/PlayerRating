package football.scd.playerrating;

import java.io.Serializable;

public class Goal implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6563522635304334751L;
	private int minute;
	private Player player;
	private int game_id;
	private int id;
	
	public Goal( int id, int min, Player pl, int game )
	{
		this.id = id;
		this.minute = min;
		this.player = pl;
		this.game_id = game;
	}
	
	@Override
	public String toString()
	{
		return this.minute + "' " + this.player.toString();
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
	 * @return the player
	 */
	public Player getPlayer() 
	{
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public int getGameID() {
		return game_id;
	}

	public void setGameID(int game_id) {
		this.game_id = game_id;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}
}

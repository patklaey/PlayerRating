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
	
	public Goal( int min, Player pl )
	{
		this.minute = min;
		this.player = pl;
	}
	
	@Override
	public String toString()
	{
		return this.minute + "' " + this.player.toString();
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
}

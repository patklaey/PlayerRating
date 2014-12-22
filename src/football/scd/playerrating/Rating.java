package football.scd.playerrating;

import java.io.Serializable;

import football.scd.playerrating.contents.GamesContent;

public class Rating implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7478662735861256001L;
	private int player_id;
	private int game_id;
	private int rating;
	
	/**
	 * @param player_id
	 * @param game_id
	 * @param rating
	 */
	public Rating(int player_id, int game_id, int rating)
	{
		this.player_id = player_id;
		this.game_id = game_id;
		this.rating = rating;
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
	 * @return the rating
	 */
	public int getRating()
	{
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating)
	{
		this.rating = rating;
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		return GamesContent.getGameById( this.getGameId() ).getOpponent() + ": " + this.getRating();
	}

}

package football.scd.playerrating;

import java.io.Serializable;

public class Settings implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6514422186121277610L;
	private int half_time_duration;
	private String team_name;
	
	public Settings()
	{
		super();
	}

	public int getHalfTimeDuration()
{
		return half_time_duration;
	}

	public void setHalfTimeDuration(int half_time_duration) 
	{
		this.half_time_duration = half_time_duration;
	}

	public String getTeamName()
	{
		return team_name;
	}

	public void setTeamName(String team_name)
	{
		this.team_name = team_name;
	}
	
	
	
	
}

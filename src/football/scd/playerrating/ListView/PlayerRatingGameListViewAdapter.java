package football.scd.playerrating.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

import football.scd.playerrating.Game;

public abstract class PlayerRatingGameListViewAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5098407910508679552L;
	protected int current_val = 0;
	protected int position = 0;
	protected List<Pair<Game,String>> listViewContentList = new ArrayList<Pair<Game,String>>();
	
	public abstract List<Pair<Game, String>> buildListViewAdapterContent();

}
package football.scd.playerrating.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

public abstract class PlayerRatingGameListViewAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5098407910508679552L;
	protected int current_val = 0;
	protected int position = 0;
	protected List<Pair<Integer,String>> listViewContentList = new ArrayList<Pair<Integer,String>>();
	
	public abstract List<Pair<Integer, String>> buildListViewAdapterContent();

}

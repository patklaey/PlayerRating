package football.scd.playerrating;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import football.scd.playerrating.contents.GamesContent;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link GameStatistics.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link GameStatistics#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class GameStatistics extends Fragment 
{
	// Base game stats
	private int wins;
	private int draws;
	private int defeats;
	private int goals_scored;
	private int goals_conceded;
	
	// Lists
	private static List<Game> average_rating_list;

	private OnGameStatsFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment GameStatistics.
	 */
	// TODO: Rename and change types and number of parameters
	public static GameStatistics newInstance(String param1, String param2)
	{
		GameStatistics fragment = new GameStatistics();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public GameStatistics() 
	{
		// Required empty public constructor
		this.goals_conceded = 0;
		this.goals_scored = 0;
		this.wins = 0;
		this.defeats = 0;
		this.draws = 0;
		
		// Get all games in the average ratings list and sort it according to the ratings
		GameStatistics.average_rating_list = new LinkedList<Game>( GamesContent.GAMES);
		Collections.sort( GameStatistics.average_rating_list, new GameRatingComparator() );
		
		// Go through each game and collect base stats
		for (Game game : GamesContent.GAMES )
		{
			// Add the goals conceded and scored
			this.goals_conceded += game.getGoalsConceded().size();
			this.goals_scored += game.getGoalsScored().size();
			
			// Check if it is a win draw or defeat
			if ( game.getGoalsConceded().size() > game.getGoalsScored().size() )
				this.defeats++;
			
			if ( game.getGoalsConceded().size() == game.getGoalsScored().size() )
				this.draws++;
			
			if ( game.getGoalsConceded().size() < game.getGoalsScored().size() )
				this.wins++;
		}
	}
	
	@Override
	public void onViewCreated (View view, Bundle savedInstanceState)
	{	
		// Set the labels
		((TextView)getView().findViewById(R.id.win_draw_defeat_values)).setText( this.wins + "-" + this.draws + "-" + this.defeats);
		((TextView)getView().findViewById(R.id.goal_difference_values)).setText( this.goals_scored + ":" + this.goals_conceded);
		
		((TextView)getView().findViewById(R.id.best_rating_opponent_name)).setText( GameStatistics.average_rating_list.get(0).getOpponent());
		((TextView)getView().findViewById(R.id.best_rating_rating_value)).setText( String.format("%.2f", GameStatistics.average_rating_list.get(0).getAverageRating() ) );
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_game_statistics, container,
				false);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri)
	{
		if (mListener != null) {
			mListener.onGameStatsFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try {
			mListener = (OnGameStatsFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() 
	{
		super.onDetach();
		mListener = null;
	}
	
	public static List<Game> getAverageMatchRatingList()
	{
		return GameStatistics.average_rating_list;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnGameStatsFragmentInteractionListener
	{
		public void onGameStatsFragmentInteraction(Uri uri);
		
		public void listBestRatings(View view);
	}
	
	public static class GameRatingComparator implements Comparator<Game>
	{

		@Override
		public int compare(Game lhs, Game rhs)
		{
			if ( lhs.getAverageRating() > rhs.getAverageRating() )
				return -1;
			
			if ( lhs.getAverageRating() < rhs.getAverageRating() )
				return 1;
			
			return 0;
		}
		
	}

}

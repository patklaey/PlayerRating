package football.scd.playerrating;

import football.scd.playerrating.contents.PlayersContent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
 * {@link PlayerStatistics.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link PlayerStatistics#newInstance} factory method to
 * create an instance of this fragment.
 * 
 */
public class PlayerStatistics extends Fragment
{
	
	// The different lists
	private static List<Player> top_scorer_list;
	private static List<Player> mvp_list;
	private static List<Player> most_played_list;
	private static List<Player> minutes_per_goal_list;
	private static List<Player> minutes_per_game_list;
	
	private OnStatsFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment Statistics.
	 */
	
	// TODO: Rename and change types and number of parameters
	public static PlayerStatistics newInstance(String param1, String param2)
	{
		PlayerStatistics fragment = new PlayerStatistics();
		return fragment;
	}

	public PlayerStatistics()
	{
		// Required empty public constructor
	}

	@Override
	public void onViewCreated (View view, Bundle savedInstanceState)
	{
		// Set the top scorer labels
		if ( PlayerStatistics.top_scorer_list.size() > 0 )
		{
			((TextView) this.getView().findViewById(R.id.top_scorer_name)).setText(PlayerStatistics.top_scorer_list.get(0).toString());
			((TextView) this.getView().findViewById(R.id.top_scorer_value)).setText(PlayerStatistics.top_scorer_list.get(0).getTotalGoals() + " Goals");
		}
		
		if ( PlayerStatistics.most_played_list.size() > 0 )
		{
			// Set the MVP labels
			((TextView) this.getView().findViewById(R.id.mvp_name)).setText(PlayerStatistics.mvp_list.get(0).toString());
			((TextView) this.getView().findViewById(R.id.mvp_value)).setText(PlayerStatistics.mvp_list.get(0).getAverageRating() +  "");
			
			// Set the most played labels
			((TextView) this.getView().findViewById(R.id.most_played_name)).setText(PlayerStatistics.most_played_list.get(0).toString());
			((TextView) this.getView().findViewById(R.id.most_played_value)).setText(PlayerStatistics.most_played_list.get(0).getTotalMinutes() + " min");
			
			// Set the minutes per goal labels
			((TextView) this.getView().findViewById(R.id.minutes_per_goal_name)).setText(PlayerStatistics.minutes_per_goal_list.get(0).toString());
			((TextView) this.getView().findViewById(R.id.minutes_per_goal_value)).setText(PlayerStatistics.minutes_per_goal_list.get(0).getMinutesPerGoal() + "");
			
			// Set the minutes per goal labels
			((TextView) this.getView().findViewById(R.id.minutes_per_game_name)).setText(PlayerStatistics.minutes_per_game_list.get(0).toString());
			((TextView) this.getView().findViewById(R.id.minutes_per_game_value)).setText(PlayerStatistics.minutes_per_game_list.get(0).getMinutesPerGame() + "");
		}
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Create the lists and copy the content
		PlayerStatistics.top_scorer_list = new ArrayList<Player>(PlayersContent.PLAYERS);
		PlayerStatistics.mvp_list = new ArrayList<Player>(PlayersContent.PLAYERS);
		PlayerStatistics.most_played_list = new ArrayList<Player>(PlayersContent.PLAYERS);
		PlayerStatistics.minutes_per_goal_list = new ArrayList<Player>(PlayersContent.PLAYERS);
		PlayerStatistics.minutes_per_game_list = new ArrayList<Player>(PlayersContent.PLAYERS);

		// Sort the given lists
		Collections.sort(PlayerStatistics.top_scorer_list, new PlayerGoalComparator());
		Collections.sort(PlayerStatistics.mvp_list, new PlayerRatingComparator());
		Collections.sort(PlayerStatistics.most_played_list, new PlayerMinutesComparator());
		Collections.sort(PlayerStatistics.minutes_per_goal_list, new PlayerMinutesPerGoalComparator());
		Collections.sort(PlayerStatistics.minutes_per_game_list, new PlayerMinutesPerGameComparator());

	}

	/**
	 * @return the top_scorer_list
	 */
	public static List<Player> getTopScorerList() 
	{
		return top_scorer_list;
	}

	/**
	 * @param top_scorer_list the top_scorer_list to set
	 */
	public static void setTopScorerList(List<Player> top_scorer_list) 
	{
		PlayerStatistics.top_scorer_list = top_scorer_list;
	}

	/**
	 * @return the mvp_list
	 */
	public static List<Player> getMvpList()
	{
		return mvp_list;
	}

	/**
	 * @param mvp_list the mvp_list to set
	 */
	public static void setMvpList(List<Player> mvp_list)
	{
		PlayerStatistics.mvp_list = mvp_list;
	}

	/**
	 * @return the most_played_list
	 */
	public static List<Player> getMostPlayedList() 
	{
		return most_played_list;
	}

	/**
	 * @param most_played_list the most_played_list to set
	 */
	public static void setMostPlayedList(List<Player> most_played_list)
	{
		PlayerStatistics.most_played_list = most_played_list;
	}

	/**
	 * @return the minutes_per_goal_list
	 */
	public static List<Player> getMinutesPerGoalList() 
	{
		return minutes_per_goal_list;
	}

	/**
	 * @param minutes_per_goal_list the minutes_per_goal_list to set
	 */
	public static void setMinutesPerGoalList(List<Player> minutes_per_goal_list)
	{
		PlayerStatistics.minutes_per_goal_list = minutes_per_goal_list;
	}
	
	
	/**
	 * @return the minutes_per_game_list
	 */
	public static List<Player> getMinutesPerGameList() {
		return minutes_per_game_list;
	}

	/**
	 * @param minutes_per_game_list the minutes_per_game_list to set
	 */
	public static void setMinutesPerGameList(List<Player> minutes_per_game_list) {
		PlayerStatistics.minutes_per_game_list = minutes_per_game_list;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_statistics, container, false);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri)
	{
		if (mListener != null) {
			mListener.onStatsFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		try {
			mListener = (OnStatsFragmentInteractionListener) activity;
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

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnStatsFragmentInteractionListener
	{
		// TODO: Update argument type and name
		public void onStatsFragmentInteraction(Uri uri);
		
		public void listTopScorer(View view);
		
		public void listMostPlayed(View view);
		
		public void listMVP(View view);
		
		public void listMinutesPerGoal(View view);
		
		public void listMinutesPerGame(View view);

	}
	
	// The sort classes
	public static class PlayerGoalComparator implements Comparator<Player>
	{

		@Override
		public int compare(Player lhs, Player rhs)
		{
			if ( lhs.getTotalGoals() > rhs.getTotalGoals() )
				return -1;
			
			if ( lhs.getTotalGoals() < rhs.getTotalGoals() )
				return 1;
			
			return 0;
		}
		
	}
	
	public static class PlayerMinutesComparator implements Comparator<Player>
	{

		@Override
		public int compare(Player lhs, Player rhs)
		{
			if ( lhs.getTotalMinutes() > rhs.getTotalMinutes() )
				return -1;
			
			if ( lhs.getTotalMinutes() < rhs.getTotalMinutes() )
				return 1;
			
			return 0;
		}
		
	}
	
	public static class PlayerRatingComparator implements Comparator<Player>
	{

		@Override
		public int compare(Player lhs, Player rhs)
		{
			if ( lhs.getAverageRating() > rhs.getAverageRating() )
				return -1;
			
			if ( lhs.getAverageRating() < rhs.getAverageRating() )
				return 1;
			
			return 0;
		}
		
	}
	
	public static class PlayerMinutesPerGoalComparator implements Comparator<Player>
	{

		@Override
		public int compare(Player lhs, Player rhs)
		{
			if ( lhs.getMinutesPerGoal() > rhs.getMinutesPerGoal() )
				return 1;
			
			if ( lhs.getMinutesPerGoal() < rhs.getMinutesPerGoal() )
				return -1;
			
			return 0;
		}
		
	}
	
	public static class PlayerMinutesPerGameComparator implements Comparator<Player>
	{

		@Override
		public int compare(Player lhs, Player rhs)
		{
			if ( lhs.getMinutesPerGame() < rhs.getMinutesPerGame() )
				return 1;
			
			if ( lhs.getMinutesPerGame() > rhs.getMinutesPerGame() )
				return -1;
			
			return 0;
		}
		
	}

}

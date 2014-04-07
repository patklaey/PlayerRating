package football.scd.playerrating;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import football.scd.playerrating.contents.PlayersContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link Statistics.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link Statistics#newInstance} factory method to
 * create an instance of this fragment.
 * 
 */
public class Statistics extends Fragment
{
	
	// The different lists
	private List<Player> top_scorer_list;
	private List<Player> mvp_list;
	private List<Player> most_played_list;

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
	public static Statistics newInstance(String param1, String param2)
	{
		Statistics fragment = new Statistics();
		return fragment;
	}

	public Statistics()
	{
		// Required empty public constructor
	}

	@Override
	public void onViewCreated (View view, Bundle savedInstanceState)
	{
		((TextView) this.getView().findViewById(R.id.top_scorer_name)).setText(this.top_scorer_list.get(0).toString());
		((TextView) this.getView().findViewById(R.id.mvp_name)).setText(this.mvp_list.get(0).toString());
		((TextView) this.getView().findViewById(R.id.most_played_name)).setText(this.most_played_list.get(0).toString());
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Create the lists and copy the content
		this.top_scorer_list = new ArrayList<Player>(PlayersContent.PLAYERS);
		this.mvp_list = new ArrayList<Player>(PlayersContent.PLAYERS);
		this.most_played_list = new ArrayList<Player>(PlayersContent.PLAYERS);

		// Sort the given lists
		Collections.sort(this.top_scorer_list, new PlayerGoalComparator());
		Collections.sort(this.mvp_list, new PlayerRatingComparator());
		Collections.sort(this.most_played_list, new PlayerMinutesComparator());

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

}

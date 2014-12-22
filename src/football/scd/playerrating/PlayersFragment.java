package football.scd.playerrating;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import football.scd.playerrating.contents.PlayersContent;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class PlayersFragment extends ListFragment 
{

	private OnPlayerFragmentInteractionListener mListener;
	private static ArrayAdapter<Player> adapter;

	// TODO: Rename and change types of parameters
	public static PlayersFragment newInstance(String param1, String param2)
	{
		PlayersFragment fragment = new PlayersFragment();
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PlayersFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		// Display all players
		adapter = new ArrayAdapter<Player>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				PlayersContent.getAllPlayers());
		
		setListAdapter( adapter );

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnPlayerFragmentInteractionListener) activity;
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

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);

		if (null != mListener) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			mListener
					.onPlayerSelected(PlayersContent.getAllPlayers().get(position).getID());
		}
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
	public interface OnPlayerFragmentInteractionListener 
	{
		// TODO: Update argument type and name
		public void onPlayerSelected(int id);
	}
	
	public static void updateList()
	{
		adapter.notifyDataSetChanged();
	}

}

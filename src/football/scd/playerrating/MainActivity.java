package football.scd.playerrating;

import java.util.Locale;

import football.scd.playerrating.GamesFragment.OnGameFragmentInteractionListener;
import football.scd.playerrating.PlayersFragment.OnPlayerFragmentInteractionListener;
import football.scd.playerrating.Statistics.OnStatsFragmentInteractionListener;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, OnGameFragmentInteractionListener, OnPlayerFragmentInteractionListener, OnStatsFragmentInteractionListener{

	private static final int PLAYER_TAB = 0;
	private static final int GAME_TAB = 1;
	private static final int STATS_TAB = 2;
	
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    // Remember the current section as it is important for the menu
    private int currentTabSection = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	
    			// Set the currentTabSection
            	currentTabSection = position;
            	invalidateOptionsMenu();
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player, menu);
        if ( currentTabSection == STATS_TAB )
        {
        	menu.findItem(R.id.action_add).setVisible(false);
        } else
        {
        	menu.findItem(R.id.action_add).setVisible(true);
        }
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                Log.d("Menu","Creating new player " + currentTabSection);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
        	
        	Bundle args = new Bundle();

        	switch (position) {
			case PLAYER_TAB:				
	            // Return a PlayersFragment
	            Fragment players = new PlayersFragment();
	            players.setArguments(args);
	            return players;
			case GAME_TAB:
	            // Return a GamesFragment
	            Fragment games = new GamesFragment();
	            games.setArguments(args);
	            return games;
			case STATS_TAB:
				Fragment stats = new Statistics();
				stats.setArguments(args);
	            return stats;
			default:
	            // Return a PlayersFragment
	            Fragment fragment = new PlayersFragment();
	            fragment.setArguments(args);
	            return fragment;
        	}
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
	
	@Override
	public void onGameSelected(int id) {
		// TODO Auto-generated method stub
		Log.d("Callback", "Selected game with ID " + id);
	}

	@Override
	public void onPlayerSelected(int id) {
		// TODO Auto-generated method stub
		Log.d("Callback", "Selected player with ID " + id);
	}

	@Override
	public void onStatsFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		Log.d("Callback", "Selected setting with uri " + uri);
	}

}

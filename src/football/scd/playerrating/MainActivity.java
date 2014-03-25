package football.scd.playerrating;

import java.util.Locale;

import football.scd.playerrating.GamesFragment.OnGameFragmentInteractionListener;
import football.scd.playerrating.PlayersFragment.OnPlayerFragmentInteractionListener;
import football.scd.playerrating.Statistics.OnStatsFragmentInteractionListener;
import football.scd.playerrating.backend.Backend;
import football.scd.playerrating.backend.SQLiteBackend;
import football.scd.playerrating.contents.GamesContent;
import football.scd.playerrating.contents.PlayersContent;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
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

	public static final String EXTRA_TYPE = "football.scd.playerrating.Type";
	public static final String EXTRA_TYPE_SHOW = "football.scd.playerrating.Type_Show";
	public static final String EXTRA_TYPE_NEW = "football.scd.playerrating.Type_New";
	public static final String EXTRA_NAME = "football.scd.playerrating.Name";
	public static final String EXTRA_GIVENNAME = "football.scd.playerrating.Givenname";
	public static final String EXTRA_ID = "football.scd.playerrating.ID";
	public static final String EXTRA_MINUTES = "football.scd.playerrating.Minutes";
	public static final String EXTRA_GOALS = "football.scd.playerrating.Goals";
	public static final String EXTRA_RATING = "football.scd.playerrating.Rating";
	public static final String EXTRA_OPPONENT = "football.scd.playerrating.Opponent";
	public static final String EXTRA_SELF_SCORE = "football.scd.playerrating.Self_Score";
	public static final String EXTRA_OPPONENT_SCORE = "football.scd.playerrating.Opponent_Score";
	public static final String EXTRA_SELF_NAME = "football.scd.playerrating.Self_Name";
	public static final String EXTRA_IS_HOME_GAME = "football.scd.playerrating.Is_Home_Game";
	
	private static final int PLAYER_TAB = 0;
	private static final int GAME_TAB = 1;
	private static final int STATS_TAB = 2;
	
	public static int next_free_player_id = 0;
	public static int next_free_game_id = 0;

	private static Backend backend;
	
	public static final String MY_TEAM_NAME = "SC Düdingen Cb";
	
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

        // Create the backend
        MainActivity.backend = new SQLiteBackend(this);

        // Get all players from the backend if not already done
        if ( PlayersContent.PLAYERS.size() == 0 )
        	PlayersContent.addPlayers(MainActivity.backend.getAllPlayers());
        
        // Get all games from the backend if not already done
        if ( GamesContent.GAMES.size() == 0 )
        	GamesContent.addGames(MainActivity.backend.getAllGames());

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
            public void onPageSelected(int position)
            {
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
        getMenuInflater().inflate(R.menu.main, menu);
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
            	Intent intent;
            	switch (currentTabSection) {
					case PLAYER_TAB:
						intent = new Intent(this,PlayerActivity.class);
				    	intent.putExtra(MainActivity.EXTRA_TYPE, MainActivity.EXTRA_TYPE_NEW);
				    	startActivity(intent);
						break;
	
					case GAME_TAB:
						intent = new Intent(this,GameActivity.class);
				    	intent.putExtra(MainActivity.EXTRA_TYPE, MainActivity.EXTRA_TYPE_NEW);
				    	startActivity(intent);
						break;
						
					default:
						intent = new Intent(this,PlayerActivity.class);
				    	intent.putExtra(MainActivity.EXTRA_TYPE, MainActivity.EXTRA_TYPE_NEW);
				    	startActivity(intent);
						break;
				}
            	
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
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
        	
        	Bundle args = new Bundle();

        	switch (position) 
        	{
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
    	Intent intent = new Intent(this,GameActivity.class);
    	intent.putExtra(MainActivity.EXTRA_TYPE, MainActivity.EXTRA_TYPE_SHOW);
    	intent.putExtra(MainActivity.EXTRA_ID, GamesContent.GAME_MAP.get(id).getID());
    	intent.putExtra(MainActivity.EXTRA_OPPONENT, GamesContent.GAME_MAP.get(id).getOpponent());
    	intent.putExtra(MainActivity.EXTRA_SELF_SCORE, GamesContent.GAME_MAP.get(id).getSelf_goals());
    	intent.putExtra(MainActivity.EXTRA_OPPONENT_SCORE, GamesContent.GAME_MAP.get(id).getOpponent_goals());
    	intent.putExtra(MainActivity.EXTRA_SELF_NAME, GamesContent.GAME_MAP.get(id).getSelf_name());
    	intent.putExtra(MainActivity.EXTRA_IS_HOME_GAME, GamesContent.GAME_MAP.get(id).isHomeGame());
    	startActivity(intent);	}

	@Override
	public void onPlayerSelected(int id) {
    	Intent intent = new Intent(this,PlayerActivity.class);
    	intent.putExtra(MainActivity.EXTRA_TYPE, MainActivity.EXTRA_TYPE_SHOW);
    	intent.putExtra(MainActivity.EXTRA_NAME, PlayersContent.PLAYER_MAP.get(id).getName());
    	intent.putExtra(MainActivity.EXTRA_GIVENNAME, PlayersContent.PLAYER_MAP.get(id).getGivenname());
    	intent.putExtra(MainActivity.EXTRA_ID, PlayersContent.PLAYER_MAP.get(id).getID());
    	intent.putExtra(MainActivity.EXTRA_MINUTES, PlayersContent.PLAYER_MAP.get(id).getMinutes());
    	intent.putExtra(MainActivity.EXTRA_GOALS, PlayersContent.PLAYER_MAP.get(id).getGoals());
    	intent.putExtra(MainActivity.EXTRA_RATING, PlayersContent.PLAYER_MAP.get(id).getRating());
    	startActivity(intent);
	}

	@Override
	public void onStatsFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		Log.d("Callback", "Selected setting with uri " + uri);
	}

	public static Backend getBackend()
	{
		return MainActivity.backend;
	}
	
}

package football.scd.playerrating;

import java.util.Locale;

import football.scd.playerrating.GameStatistics.OnGameStatsFragmentInteractionListener;
import football.scd.playerrating.GamesFragment.OnGameFragmentInteractionListener;
import football.scd.playerrating.PlayersFragment.OnPlayerFragmentInteractionListener;
import football.scd.playerrating.PlayerStatistics.OnStatsFragmentInteractionListener;
import football.scd.playerrating.backend.Backend;
import football.scd.playerrating.backend.SQLiteBackend;
import football.scd.playerrating.contents.GamesContent;
import football.scd.playerrating.contents.PlayersContent;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, OnGameFragmentInteractionListener, OnPlayerFragmentInteractionListener, OnStatsFragmentInteractionListener, OnGameStatsFragmentInteractionListener {
	
	// All extras
	public static final String EXTRA_TYPE = "football.scd.playerrating.Type";
	public static final String EXTRA_TYPE_SHOW = "football.scd.playerrating.Type_Show";
	public static final String EXTRA_TYPE_NEW = "football.scd.playerrating.Type_New";
	public static final String EXTRA_PLAYER = "football.scd.playerrating.Player";
	public static final String EXTRA_GAME = "football.scd.playerrating.Game";
	public static final String EXTRA_SELF_SCORE = "football.scd.playerrating.Self_Score";
	public static final String EXTRA_OPPONENT_SCORE = "football.scd.playerrating.Opponent_Score";
	public static final String EXTRA_SELF_NAME = "football.scd.playerrating.Self_Name";
	public static final String EXTRA_IS_HOME_GAME = "football.scd.playerrating.Is_Home_Game";
	public static final String EXTRA_GAME_FINISHED = "football.scd.playerrating.Game_Finished";	
	public static final int EXTRA_STATS_SCORER = 0;
	public static final int EXTRA_STATS_MVP = 1;
	public static final int EXTRA_STATS_MINUTES = 2;
	public static final int EXTRA_STATS_MINUTES_PER_GOALS = 3;
	public static final int EXTRA_STATS_MATCH_RATINGS = 4;
	public static final int EXTRA_STATS_MINUTES_PER_GAME = 5;
	public static final String EXTRA_STATS_TYPE = "football.scd.playerrating.Stats_Type";
	
	// Setting keys
	private static final String SETTINGS_TEAM_NAME = "football.scd.playerrating.settings.team_name";
	private static final String SETTINGS_HALF_TIME = "football.scd.playerrating.settings.half_time";

	// The sections
	private static final int NUMBER_OF_TABS = 4;
	private static final int PLAYER_TAB = 1;
	private static final int GAME_TAB = 0;
	private static final int PLAYER_STATS_TAB = 2;
	private static final int GAME_STATS_TAB = 3;
	
	public static int next_free_player_id = 0;
	public static int next_free_game_id = 0;
	public static int next_free_goal_id = 0;

	private static Backend backend;
	
	// The evil player ;-)
	public static final Player GOAL_AGAINS_PLAYER = new Player(-1, "Goal", "Against");
	private static final int SETTINGS = 1;
	
	// The settings
	private static Settings settings;
	
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
        
        this.loadSettings();

        // Create the backend
        MainActivity.backend = new SQLiteBackend(this);

        // Get all players from the backend if not already done
        if ( PlayersContent.PLAYERS.size() == 0 )
        	PlayersContent.addPlayers(MainActivity.backend.getAllPlayers());
        
        // Get all games from the backend if not already done
        if ( GamesContent.GAMES.size() == 0 )
        	GamesContent.addGames(MainActivity.backend.getAllGames());

        // Get the next free uids
        MainActivity.next_free_game_id = MainActivity.backend.getMaxGameID() + 1;
        MainActivity.next_free_player_id = MainActivity.backend.getMaxPlayerID() + 1;
        MainActivity.next_free_goal_id = MainActivity.backend.getMaxGoalID() + 1;
        
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
        if ( currentTabSection == PLAYER_STATS_TAB || currentTabSection == GAME_STATS_TAB )
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
    	Intent intent;
        switch (item.getItemId()) {
            case R.id.action_add:
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
                
            case R.id.action_settings:
            	
            	// Simply start the SettingsActivity
            	intent = new Intent(this,SettingsActivity.class);
            	this.startActivityForResult(intent, MainActivity.SETTINGS);
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
				case PLAYER_STATS_TAB:
					// Return a PlayerStatistics Fragment
					Fragment stats = new PlayerStatistics();
					stats.setArguments(args);
		            return stats;
				case GAME_STATS_TAB:
					// Return a GameStatistics Fragment
					Fragment game_stats = new GameStatistics();
					game_stats.setArguments(args);
		            return game_stats;
				default:
		            // Return a PlayersFragment
		            Fragment fragment = new PlayersFragment();
		            fragment.setArguments(args);
		            return fragment;
		            
        	}
        }

        @Override
        public int getCount() {
            // Show NUMBER_OF_TABS total pages.
            return NUMBER_OF_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case PLAYER_TAB:
                    return getString(R.string.title_players_section).toUpperCase(l);
                case GAME_TAB:
                    return getString(R.string.title_games_section).toUpperCase(l);
                case PLAYER_STATS_TAB:
                    return getString(R.string.title_players_stats_section).toUpperCase(l);
                case GAME_STATS_TAB:
                	return getString(R.string.title_game_stats_section).toUpperCase(l);
            }
            return null;
        }
    }
    
    private void loadSettings()
    {
    	SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
    	String default_team_name = getResources().getString( R.string.my_team_name );
    	String team_name = sharedPref.getString(MainActivity.SETTINGS_TEAM_NAME, default_team_name);
    	int half_time = sharedPref.getInt(MainActivity.SETTINGS_HALF_TIME, 45);
    	
    	// Create a new settings objcet
    	Settings settings = new Settings();
    	settings.setHalfTimeDuration(half_time);
    	settings.setTeamName(team_name);
    	MainActivity.settings = settings;
    }
    
    public void saveSettings(Settings settings)
    {
    	// Assign the settings variable
    	MainActivity.settings = settings;
    	
    	// Write the application settings
    	SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	editor.putInt(MainActivity.SETTINGS_HALF_TIME, settings.getHalfTimeDuration() );
    	editor.putString(MainActivity.SETTINGS_TEAM_NAME, settings.getTeamName() );
    	editor.commit();
    }
    
    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data)
    {
    	super.onActivityResult(request_code, result_code, data);
    	
        // If the result is from edit property
        if ( request_code == MainActivity.SETTINGS && result_code == RESULT_OK )
        {
        	Settings settings = (Settings) data.getSerializableExtra(SettingsActivity.EXTRA_SETTINGS);
        	this.saveSettings(settings);
        }
    }
	
	@Override
	public void onGameSelected(int id)
	{
    	Intent intent = new Intent(this,GameActivity.class);
    	intent.putExtra(MainActivity.EXTRA_TYPE, MainActivity.EXTRA_TYPE_SHOW);
    	intent.putExtra(MainActivity.EXTRA_GAME, GamesContent.GAME_MAP.get(id));
    	startActivity(intent);	
    }

	@Override
	public void onPlayerSelected(int id) 
	{
    	Intent intent = new Intent(this,PlayerActivity.class);
    	intent.putExtra(MainActivity.EXTRA_TYPE, MainActivity.EXTRA_TYPE_SHOW);
    	intent.putExtra(MainActivity.EXTRA_PLAYER, PlayersContent.PLAYER_MAP.get(id) );
    	startActivity(intent);
	}

	@Override
	public void onStatsFragmentInteraction(Uri uri)
	{
		Log.d("Callback", "Selected setting with uri " + uri);
	}

	public static Backend getBackend()
	{
		return MainActivity.backend;
	}

	public static Settings getSettings()
	{
		return MainActivity.settings;
	}
	
	@Override
	public void listTopScorer(View view) 
	{
		// Create a new intent
		Intent intent = new Intent(this, StatisticsList.class);
		intent.putExtra( MainActivity.EXTRA_STATS_TYPE, MainActivity.EXTRA_STATS_SCORER );
		this.startActivity(intent);
	}
	
	@Override
	public void listMostPlayed(View view) 
	{
		// Create a new intent
		Intent intent = new Intent(this, StatisticsList.class);
		intent.putExtra( MainActivity.EXTRA_STATS_TYPE, MainActivity.EXTRA_STATS_MINUTES );
		this.startActivity(intent);
	}
	
	@Override
	public void listMVP(View view) 
	{
		// Create a new intent
		Intent intent = new Intent(this, StatisticsList.class);
		intent.putExtra( MainActivity.EXTRA_STATS_TYPE, MainActivity.EXTRA_STATS_MVP );
		this.startActivity(intent);
	}

	@Override
	public void onGameStatsFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listMinutesPerGoal(View view) 
	{
		// Create a new intent
		Intent intent = new Intent(this, StatisticsList.class);
		intent.putExtra( MainActivity.EXTRA_STATS_TYPE, MainActivity.EXTRA_STATS_MINUTES_PER_GOALS );
		this.startActivity(intent);
	}
	
	@Override
	public void listMinutesPerGame(View view) 
	{
		// Create a new intent
		Intent intent = new Intent(this, StatisticsList.class);
		intent.putExtra( MainActivity.EXTRA_STATS_TYPE, MainActivity.EXTRA_STATS_MINUTES_PER_GAME );
		this.startActivity(intent);
	}

	@Override
	public void listBestRatings(View view)
	{
		// Create a new intent
		Intent intent = new Intent(this, StatisticsList.class);
		intent.putExtra( MainActivity.EXTRA_STATS_TYPE, MainActivity.EXTRA_STATS_MATCH_RATINGS );
		this.startActivity(intent);		
	}
}

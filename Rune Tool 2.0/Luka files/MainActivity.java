package rs.inventivess.kbstatistika;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import rs.inventivess.kbstatistika.dataobjects.Bank;
import rs.inventivess.kbstatistika.dataobjects.ReportType;
import rs.inventivess.kbstatistika.dataobjects.Request;
import rs.inventivess.kbstatistika.fragmentpages.ReportList;
import rs.inventivess.kbstatistika.fragmentpages.RequestPreviewList;
import rs.inventivess.kbstatistika.inputpages.StatInputBankDate;
import rs.inventivess.kbstatistika.inputpages.StatInputJmbg;
import rs.inventivess.kbstatistika.inputpages.StatInputYearMonth;
import rs.inventivess.kbstatistika.jsonparser.JsonParser;
import rs.inventivess.kbstatistika.listadapters.ReportListAdapter;
import rs.inventivess.kbstatistika.listadapters.RequestListAdapter;
import rs.inventivess.kbstatistika.sqlite.BanksDatabaseHandler;
import rs.inventivess.kbstatistika.sqlite.DatabaseHandler;
import rs.inventivess.kbstatistika.sqlite.ReportsDatabaseHandler;
import rs.inventivess.kbstatistika.sqlite.RequestDatabaseHandler;

public class MainActivity extends FragmentActivity {

    /*replacing fragments
    fragmentManager
        .beginTransaction()
        .replace(android.R.id.content, statisticsFragment, "Statistics")
        .addToBackStack(null)
        .commit();
     */

    private String[] menuItemList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuItemList = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuItemList));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        LoadBanksAsync bankLoader = new LoadBanksAsync();
        LoadReportsAsync reportLoader = new LoadReportsAsync();
        LoadRequestsAsync requestLoader = new LoadRequestsAsync();

        bankLoader.execute(null);
        reportLoader.execute(null);
        requestLoader.execute(null);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;

        switch(position) {
            case 0:
                ReportsDatabaseHandler rdh = new ReportsDatabaseHandler(context);
                if (rdh.getReportsCount() > 0) {
                    fragment = new ReportList();
                }
                else
                {
                    Toast.makeText(context, "Nema izvestaja za kreiranje zahteva", Toast.LENGTH_LONG);
                    fragment = null;
                }
                break;
            case 1:
                fragment = new RequestPreviewList();
                break;
            default:
                fragment = null;
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            mDrawerList.setItemChecked(position, true);
            setTitle(menuItemList[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(menuItemList[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    public class LoadBanksAsync extends AsyncTask<String, Void, List<Bank>> {

        @Override
        protected List<Bank> doInBackground(String... strings) {
            JsonParser parser = new JsonParser();
            return parser.getBanksFromServer();
        }

        @Override
        protected void onPostExecute(List<Bank> bankList) {
            BanksDatabaseHandler bdh = new BanksDatabaseHandler(context);
            bdh.addBanks(bankList);
        }
    }

    public class LoadReportsAsync extends AsyncTask<String, Void, List<ReportType>> {

        @Override
        protected List<ReportType> doInBackground(String... strings) {
            JsonParser parser = new JsonParser();
            return parser.getReportsFromServer();
        }

        @Override
        protected void onPostExecute(List<ReportType> reportsList) {
            ReportsDatabaseHandler rdh = new ReportsDatabaseHandler(context);
            rdh.addReports(reportsList);
        }
    }

    public class LoadRequestsAsync extends AsyncTask<String, Void, List<Request>> {

        @Override
        protected List<Request> doInBackground(String... strings) {
            JsonParser parser = new JsonParser();
            return parser.getRequestsFromServer();
        }

        @Override
        protected void onPostExecute(List<Request> requestsList) {
            RequestDatabaseHandler rdh = new RequestDatabaseHandler(context);
            rdh.addRequests(requestsList);
        }
    }
}

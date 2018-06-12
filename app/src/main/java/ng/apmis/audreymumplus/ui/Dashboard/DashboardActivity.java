package ng.apmis.audreymumplus.ui.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Appointments.AppointmentFragment;
import ng.apmis.audreymumplus.ui.Chat.ChatFragment;
import ng.apmis.audreymumplus.ui.Faq.FaqFragment;
import ng.apmis.audreymumplus.ui.Home.HomeFragment;
import ng.apmis.audreymumplus.ui.Journal.MyJournalFragment;
import ng.apmis.audreymumplus.ui.PregnancyDetails.PregnancyDetailsActivity;
import ng.apmis.audreymumplus.utils.BottomNavigationViewHelper;

public class DashboardActivity extends AppCompatActivity implements HomeFragment.OnfragmentInteractionListener {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
/*    @BindView(R.id.title_toolbar)
    TextView title_toolbar;*/

    @BindView(R.id.global_toolbar)
    Toolbar globalToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        ButterKnife.bind(this);

        setSupportActionBar(globalToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            selectFragment(item);
            return true;
        });

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        mFragmentManager = getSupportFragmentManager();

       // placeFragment(new HomeFragment(), true, mFragmentManager);

        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, new HomeFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*
    public void setToolBarTitle (String title) {
        title_toolbar.setText(title);
     */
/*   if (welcomeScreen) {
            backButton.setVisibility(View.GONE);
        } else {
            backButton.setVisibility(View.VISIBLE);
        }
   *//*
 }
*/
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.nav_items, menu);
        return true;
    }*/

    private void selectFragment(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.view_menu:
                placeFragment(new HomeFragment(), true, mFragmentManager);
                break;
            case R.id.journal_menu:
                placeFragment(new MyJournalFragment(), true, mFragmentManager);
                break;
            case R.id.chat_menu:
                placeFragment(new ChatFragment(), true, mFragmentManager);
                break;
            case R.id.pill_reminder:
                placeFragment(new AppointmentFragment(), true, mFragmentManager);
                break;
            case R.id.profile_menu:
                placeFragment(new FaqFragment(),true, mFragmentManager);
                break;
        }

    }

    private void placeFragment (Fragment fragment, boolean popBackStack, FragmentManager fm) {
        if (fragment instanceof HomeFragment) {
            fm.popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return;
        }
        if (popBackStack) {
            fm.popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack("current")
                    .commit();
        } else {
            fm.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("current")
                    .commit();
        }
    }


    @Override
    public void onGridItemClick(String selectedText) {
        switch (selectedText) {
            case "My Pregnancy":
                startActivity(new Intent(this, PregnancyDetailsActivity.class));
                break;
            case "My Appointments":
                placeFragment(new AppointmentFragment(), true, mFragmentManager);
                break;
            case "Chatrooms":
                placeFragment(new ChatFragment(), true, mFragmentManager);
                break;
            case "FAQs":
                placeFragment(new FaqFragment(), true, mFragmentManager);
                break;

        }
    }
}

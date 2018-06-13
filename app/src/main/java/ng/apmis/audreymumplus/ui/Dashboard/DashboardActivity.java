package ng.apmis.audreymumplus.ui.Dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
import ng.apmis.audreymumplus.utils.Utils;

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

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(item -> selectNavigationItem(item));

        CircularImageView profileCircularImageView = headerLayout.findViewById(R.id.user_image);

        View logoutView = navigationView.findViewById(R.id.logout_view);

        logoutView.setOnClickListener((View) -> Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show());

        try {
            JSONObject job =  new JSONObject(new Utils().loadJSONFromAsset(this));
            Log.v("Job weekly", job.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        Glide.with(DashboardActivity.this)
                .load(R.drawable.profile_image)
                .into(profileCircularImageView);

    }

    boolean selectNavigationItem (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile:
                Toast.makeText(DashboardActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.about_us:
                Toast.makeText(DashboardActivity.this, "About-us selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help:
                Toast.makeText(DashboardActivity.this, "Help selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Toast.makeText(DashboardActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
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

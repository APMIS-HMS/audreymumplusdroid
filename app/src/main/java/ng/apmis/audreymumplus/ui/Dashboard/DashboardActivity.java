package ng.apmis.audreymumplus.ui.Dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.LoginActivity;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.ui.Appointments.AppointmentFragment;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ChatForumFragment;
import ng.apmis.audreymumplus.ui.Faq.FaqFragment;
import ng.apmis.audreymumplus.ui.PregnancyDetails.PregnancyFragment;
import ng.apmis.audreymumplus.ui.getaudrey.GetAudreyFragment;
import ng.apmis.audreymumplus.ui.Home.HomeFragment;
import ng.apmis.audreymumplus.ui.Journal.MyJournalFragment;
import ng.apmis.audreymumplus.ui.profile.ProfileFragment;
import ng.apmis.audreymumplus.utils.BottomNavigationViewHelper;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;

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
    boolean goBackOrShowNavigationView = false;

    SharedPreferencesManager sharedPreferencesManager;
    public MutableLiveData<Person> person = new MutableLiveData<>();
    public RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        ButterKnife.bind(this);
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

        setActionBarButton(false, getString(R.string.app_name));
        queue = Volley.newRequestQueue(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        TextView userName = headerLayout.findViewById(R.id.user_name);
        //new GetVersionCode().execute();

        AudreyMumplus.getInstance().diskIO().execute(() -> InjectorUtils.provideRepository(this).getPerson().observe(this, person -> this.person.postValue(person == null ? new Person("", "", "", "", "", "", "", "", new byte[0]) : person))
        );

        getPersonLive().observe(this, theUser -> userName.setText(theUser.getFirstName() + " " + theUser.getLastName()));

        navigationView.setNavigationItemSelectedListener(this::selectNavigationItem);

        CircularImageView profileCircularImageView = headerLayout.findViewById(R.id.user_image);

        Button logoutView = navigationView.findViewById(R.id.logout);

        logoutView.setOnClickListener((View) -> {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            sharedPreferencesManager.storeUserToken("");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        });

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
                .load(R.drawable.ic_profile_place_holder)
                .into(profileCircularImageView);

    }

    public LiveData<Person> getPersonLive () {
        return person;
    }


    public void setActionBarButton(boolean shouldShowBackButton, String title) {
        setSupportActionBar(globalToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (shouldShowBackButton) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            actionBar.setTitle(title);
            goBackOrShowNavigationView = shouldShowBackButton;
        } else {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            actionBar.setTitle(title);
            goBackOrShowNavigationView = shouldShowBackButton;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    boolean selectNavigationItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                placeFragment(new HomeFragment(), true, mFragmentManager);
                drawerLayout.closeDrawers();
                return true;
            case R.id.my_profile:
                placeFragment(new ProfileFragment(), true, mFragmentManager);
                drawerLayout.closeDrawers();
                return true;
            case R.id.about_us:
                Toast.makeText(DashboardActivity.this, "About-us selected", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                return true;
            case R.id.help:
                Toast.makeText(DashboardActivity.this, "Help selected", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                return true;
            case R.id.get_audrey:
                placeFragment(new GetAudreyFragment(), false, mFragmentManager);
                drawerLayout.closeDrawers();
                return true;
            case R.id.settings:
                prefFrag(new SettingFragment());
                drawerLayout.closeDrawers();
                return true;
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (goBackOrShowNavigationView) {
                    onBackPressed();
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void bottomNavVisibility(boolean show) {
        if (show) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            return;
        }
        bottomNavigationView.setVisibility(View.GONE);
    }


    private void selectFragment(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.view_menu:
                placeFragment(new HomeFragment(), true, mFragmentManager);
                break;
            case R.id.journal_menu:
                placeFragment(new MyJournalFragment(), true, mFragmentManager);
                break;
            case R.id.chat_menu:
                placeFragment(new ChatForumFragment(), true, mFragmentManager);
                break;
            case R.id.pill_reminder:
                placeFragment(new AppointmentFragment(), true, mFragmentManager);
                break;
        }

    }

    private void placeFragment(Fragment fragment, boolean popBackStack, FragmentManager fm) {

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
                    .addToBackStack("top-current")
                    .commit();
        }
    }

    private void prefFrag(PreferenceFragment fragment) {
        getFragmentManager().popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getFragmentManager().beginTransaction()
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.fragment_container, fragment)
                .addToBackStack(null).commit();
    }


    @Override
    public void onGridItemClick(String selectedText) {
        switch (selectedText) {
            case "My Pregnancy":
                placeFragment(new PregnancyFragment(), true, mFragmentManager);
                break;
            case "My Appointments":
                placeFragment(new AppointmentFragment(), true, mFragmentManager);
                break;
            case "Chatrooms":
                placeFragment(new ChatForumFragment(), true, mFragmentManager);
                break;
            case "FAQs":
                placeFragment(new FaqFragment(), true, mFragmentManager);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

   /* private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID *//*MainActivity.this.getPackageName() + "&hl=it"*//*)
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (!BuildConfig.VERSION_NAME.equalsIgnoreCase(onlineVersion)) {
                    Toast.makeText(getApplicationContext(), BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show();
                    showUpdateDialog();
                }
            }
        }
    }*

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new OperatingProcedureFragment())
                        .commit();
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new OperatingProcedureFragment())
                        .commit();
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                dialog.dismiss();
            }
        })
                .setCancelable(false)
                .show();
    }

    */

}

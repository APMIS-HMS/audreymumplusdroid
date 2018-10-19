package ng.apmis.audreymumplus.ui.Dashboard;

import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.LoginActivity;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.data.network.ChatSocketService;
import ng.apmis.audreymumplus.ui.AboutFragment;
import ng.apmis.audreymumplus.ui.Appointments.AppointmentFragment;
import ng.apmis.audreymumplus.ui.Chat.ChatContextFragment;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ChatForumFragment;
import ng.apmis.audreymumplus.ui.HelpFragment;
import ng.apmis.audreymumplus.ui.Home.HomeFragment;
import ng.apmis.audreymumplus.ui.getaudrey.GetAudreyFragment;
import ng.apmis.audreymumplus.ui.kickcounter.KickCounterFragment;
import ng.apmis.audreymumplus.ui.pills.PillReminderFragment;
import ng.apmis.audreymumplus.ui.pregnancymodule.PregnancyFragment;
import ng.apmis.audreymumplus.ui.pregnancymodule.journal.JournalAddFragment;
import ng.apmis.audreymumplus.ui.pregnancymodule.journal.JournalFragment;
import ng.apmis.audreymumplus.ui.profile.ProfileFragment;
import ng.apmis.audreymumplus.ui.settings.SettingsFragment;
import ng.apmis.audreymumplus.utils.AlarmMangerSingleton;
import ng.apmis.audreymumplus.utils.BottomNavigationViewHelper;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.NotificationUtils;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;
import ng.apmis.audreymumplus.utils.Utils;
import ng.apmis.audreymumplus.utils.Week;

import static ng.apmis.audreymumplus.utils.SharedPreferencesManager.PREF_NAME;

public class DashboardActivity extends AppCompatActivity implements HomeFragment.OnfragmentInteractionListener {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.global_toolbar)
    Toolbar globalToolbar;

    @BindView(R.id.toolbar_title)
    TextView globalToolbarTitle;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.launch_kick_counter)
    FloatingActionButton launchKickCounter;

    FragmentManager mFragmentManager;
    boolean goBackOrShowNavigationView = false;

    SharedPreferencesManager sharedPreferencesManager;
    public MutableLiveData<Person> person = new MutableLiveData<>();
    public RequestQueue queue;

    CircularImageView profileCircularImageView;

    public static Person globalPerson;
    public static String globalOpenChatForum = null;
    private int mShortAnimationDuration;

    public static int UNKWOWN_PROGRESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtils.createNotificationChannel(this);
        }
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        mFragmentManager = getSupportFragmentManager();

        setActionBarButton(false, getString(R.string.app_name));
        queue = Volley.newRequestQueue(this);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        TextView userName = headerLayout.findViewById(R.id.user_name);
        profileCircularImageView = headerLayout.findViewById(R.id.user_image);
        //new GetVersionCode().execute();

        AudreyMumplus.getInstance().diskIO().execute(() -> {
            InjectorUtils.provideRepository(this)
                    .getPerson().observe(this, person -> {

                if (person != null) {

                    if (person.getDay() == UNKWOWN_PROGRESS || sharedPreferencesManager.justLoggedIn()) {

                        for (String forumName: person.getForums()) {
                            sharedPreferencesManager.addForumNameAndLastCreatedAtAsStringInPrefs(forumName, Utils.localDateToDbString(Calendar.getInstance().getTime()), 0);
                        }

                        InjectorUtils.provideRepository(this).getDayWeek(person);
                        AlarmMangerSingleton.setDailyWeekDayProgress(this);

                    }
                    userName.setText(getString(R.string.user_name, person.getFirstName(), person.getLastName()));

                    Glide.with(DashboardActivity.this)
                            .load(person.getProfileImage() != null ? person.getProfileImage() : R.drawable.ic_profile_place_holder)
                            .into(profileCircularImageView);
                    InjectorUtils.provideJournalNetworkDataSource(this).getForums(0);

                    List<String> forums = person.getForums();
                    for (String forum : forums) {
                        InjectorUtils.provideJournalNetworkDataSource(this)
                                .getChats(forum);
                    }

                    startService(new Intent(this, ChatSocketService.class).putStringArrayListExtra("forums", new ArrayList<>(forums)));

                    sharedPreferencesManager.setJustLoggedIn(false);
                }

                DashboardActivity.this.person.postValue(person == null ? new Person("", "", "", "", Week.Week0.week, "", "", "", "", "", "", UNKWOWN_PROGRESS) : person);

            });
        });

        launchKickCounter.setOnClickListener((view) -> {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new KickCounterFragment())
                    .addToBackStack("Kick-Counter")
                    .commit();
        });

        getPersonLive().observe(this, theUser -> {
            if (theUser != null && !TextUtils.isEmpty(theUser.get_id())) {
                globalPerson = theUser;
            }
        });

        navigationView.setNavigationItemSelectedListener(this::selectNavigationItem);


        Button logoutView = navigationView.findViewById(R.id.logout);

        logoutView.setOnClickListener((View) -> {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();

            /*Reset all shared preferences*/
            //getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit().clear().apply();

            sharedPreferencesManager.storeUserToken("");
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Logging out");
            progressDialog.setCancelable(false);
            progressDialog.show();

            InjectorUtils.provideRepository(this).clearAllTables();
            InjectorUtils.provideRepository(this).getAllWeeklyProgressData().observe(this, data -> {
                //Log out when all has been deleted
                if (data == null || data.size() == 0) {
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(this, LoginActivity.class));
                }
            });
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            selectFragment(item);
            return true;
        });

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        if (getIntent().getExtras() != null) {

            Log.v("forumName notification", getIntent().getExtras().getString("forumName"));

            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment(), "HOME")
                    .setReorderingAllowed(true)
                    .commit();

            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new ChatForumFragment(), "CHAT")
                    .addToBackStack("current")
                    .setReorderingAllowed(true)
                    .commit();

            Fragment chatFragement = new ChatContextFragment();
            Bundle chatBundle = new Bundle();
            chatBundle.putString("forumName", getIntent().getExtras().getString("forumName"));
            chatFragement.setArguments(chatBundle);

            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, chatFragement)
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit();

        } else {

            placeFragment(new HomeFragment(), true, mFragmentManager, "HOME");
        }

    }


    public LiveData<Person> getPersonLive() {
        return person;
    }


    public void setActionBarButton(boolean shouldShowBackButton, String title) {
        setSupportActionBar(globalToolbar);
        getSupportActionBar().setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (shouldShowBackButton) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            globalToolbarTitle.setText(title);
            goBackOrShowNavigationView = shouldShowBackButton;
        } else {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            globalToolbarTitle.setText(title);
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
                placeFragment(new HomeFragment(), true, mFragmentManager, "HOME");
                drawerLayout.closeDrawers();
                return true;
            case R.id.my_profile:
                placeFragment(new ProfileFragment(), true, mFragmentManager, "PROFILE");
                drawerLayout.closeDrawers();
                return true;
            case R.id.about_us:
                placeFragment(new AboutFragment(), true, mFragmentManager, "ABOUT");
                drawerLayout.closeDrawers();
                return true;
            case R.id.help:
                placeFragment(new HelpFragment(), true, mFragmentManager, "HELP");
                drawerLayout.closeDrawers();
                return true;
            case R.id.get_audrey:
                getPersonLive().observe(this, person -> {
                    Log.v("perons", String.valueOf(person));
                    if (person != null && TextUtils.isEmpty(person.getExpectedDateOfDelivery())) {
                        placeFragment(new GetAudreyFragment(), false, mFragmentManager, "AUDREY");
                    } else {
                        Toast.makeText(this, "You have a current Audrey session", Toast.LENGTH_SHORT).show();
                    }
                });
                drawerLayout.closeDrawers();
                return true;
            case R.id.settings:
                placeFragment(new SettingsFragment(), true, mFragmentManager, "SETTINGS");
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

    public void fabVisibility(boolean show) {
        if (show) {
            fab.setAlpha(0f);
            fab.setVisibility(View.VISIBLE);

            fab.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);

            fab.setOnClickListener((view ->
                /*AudreyMumplus.getInstance().diskIO().execute(() ->
                    InjectorUtils.provideRepository(this).getDaysJournal(String.valueOf(globalPerson.getDay())).observe(this, journalModel -> {
                        if (journalModel != null) {
                            Toast.makeText(this, "You have inputed a journal today", Toast.LENGTH_SHORT).show();
                        }*/
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new JournalAddFragment())
                            .addToBackStack(null)
                            .commit()
               /*     })
                )*/
            ));
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    public void kickCounterFabVisibility(boolean show) {
        launchKickCounter.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    private void selectFragment(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.view_menu:
                placeFragment(new HomeFragment(), true, mFragmentManager, "HOME");
                break;
            case R.id.journal_menu:
                placeFragment(new JournalFragment(), true, mFragmentManager, "JOURNAL");
                break;
            case R.id.chat_menu:
                placeFragment(new ChatForumFragment(), true, mFragmentManager, "CHAT");
                break;
            case R.id.pill_reminder:
                placeFragment(new PillReminderFragment(), true, mFragmentManager, "PILL");
                break;
        }

    }

    private void placeFragment(Fragment fragment, boolean popBackStack, FragmentManager fm, String tag) {

        if (fragment instanceof HomeFragment) {
            fm.popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.fragment_container, fragment)
                    .commit();
            return;
        }
        if (popBackStack) {
            fm.popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack("current")
                    .commit();
        } else {
            fm.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    @Override
    public void onGridItemClick(String selectedText) {
        switch (selectedText) {
            case "My Pregnancy":
                placeFragment(new PregnancyFragment(), true, mFragmentManager, "PREGNANCY");
                break;
            case "My Appointments":
                placeFragment(new AppointmentFragment(), true, mFragmentManager, "APPOINTMENT");
                break;
            case "Chatrooms":
                placeFragment(new ChatForumFragment(), true, mFragmentManager, "CHAT");
                break;
            case "Pills":
                placeFragment(new PillReminderFragment(), true, mFragmentManager, "PILL");
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
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

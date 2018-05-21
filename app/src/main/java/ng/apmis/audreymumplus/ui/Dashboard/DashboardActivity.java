package ng.apmis.audreymumplus.ui.Dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.Home.HomeFragment;
import ng.apmis.audreymumplus.ui.Dashboard.Journal.MyJournalFragment;

public class DashboardActivity extends AppCompatActivity {
   /* @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;*/
/*    @BindView(R.id.title_toolbar)
    TextView title_toolbar;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        BottomNavigationView navigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });



        //bottomNavigationView.setOnNavigationItemSelectedListener(item -> false);




/*
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            selectFragment(item);
            return true;
        });
*/

        // TODO: Create BottomNavViewHelper and implement here to stop bottom nav from moving


        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new DashboardFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();






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
                placeFragment(new HomeFragment());
                break;
            case R.id.buy_menu:
                placeFragment(new MyJournalFragment());
                break;
            /*case R.id.chat_menu:
                placeFragment(new HomeFragment());
                break;
            case R.id.read_menu:
                placeFragment(new HomeFragment());
                break;
            case R.id.find_menu:
                placeFragment(new HomeFragment());
                break;*/
        }



    }

    private void placeFragment (Fragment fragment) {
        getSupportFragmentManager().popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("current")
                .commit();
    }




}

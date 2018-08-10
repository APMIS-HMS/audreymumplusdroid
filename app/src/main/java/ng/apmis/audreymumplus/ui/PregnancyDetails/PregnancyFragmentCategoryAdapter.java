package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Journal.MyJournalFragment;
import ng.apmis.audreymumplus.ui.PregnancyDetails.pregnancyimagegallery.MyGalleryFragment;
import ng.apmis.audreymumplus.ui.PregnancyDetails.pregnancyweeklyprogress.PregnancyWeeklyProgressFragment;

public class PregnancyFragmentCategoryAdapter extends FragmentPagerAdapter {

    private Context context;
    public PregnancyFragmentCategoryAdapter(Context mcontext, FragmentManager fm) {
        super(fm);
        context = mcontext;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new PregnancyWeeklyProgressFragment();
        }else if(position == 1){
            return new MyGalleryFragment();
        }else {return new MyJournalFragment();}
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return context.getString(R.string.weekly_progress);
        }else if(position == 1){
            return context.getString(R.string.Mygallery);
        }else {return context.getString(R.string.pregJournal);}
    }
}

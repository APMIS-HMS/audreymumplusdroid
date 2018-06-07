package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Journal.MyJournalFragment;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context context;
    public CategoryAdapter( Context mcontext, FragmentManager fm) {
        super(fm);
        context = mcontext;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new MyJournalFragment();
        }else if(position == 1){
            return new MyGalleryFragment();
        }else {return new PregnancyJournalFragment();}
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return context.getString(R.string.Mypregnancy);
        }else if(position == 1){
            return context.getString(R.string.Mygallery);
        }else {return context.getString(R.string.pregJournal);}
    }
}

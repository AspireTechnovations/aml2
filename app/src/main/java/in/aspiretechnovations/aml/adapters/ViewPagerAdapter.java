package in.aspiretechnovations.aml.adapters;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by shyam on 04/09/17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    //private String[] mTitles = {"Category 1","Category 2","Category 3"};
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    FragmentManager fragmentManager = null;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;


    }


    // overriding getPageTitle()
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    public void replaceFragmetns(Fragment oldFragment) {


      fragmentManager.beginTransaction().detach(oldFragment).attach(oldFragment).commit();

    }


    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);


    }

    @Override
    public int getCount() {
        return mFragmentList.size();

    }

    public void addFragment(Fragment fragment, String title) {

        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);


    }

    public void addFragment(Fragment fragment) {

        mFragmentList.add(fragment);



    }




}

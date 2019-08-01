package in.aspiretechnovations.aml.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.adapters.ViewPagerAdapter;
import in.aspiretechnovations.aml.fragments.SelectBikeFragment;
import in.aspiretechnovations.aml.fragments.SelectCarFragment;
import in.aspiretechnovations.aml.utils.ConstantUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;

public class ServiceFilterActivity extends AppCompatActivity {

    public ViewPager viewPager;
    private TabLayout tabLayout;

    private ViewPagerAdapter viewPagerAdapter;


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_filter);
        context = this;
        initialise();

        EditText et_search;
        et_search  = findViewById(R.id.et_search);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, SearchActivity.class));
            }
        });



    }

    private void initialise()
    {
        viewPager = findViewById(R.id.viewpager_home);
        tabLayout =  findViewById(R.id.tabs_home);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        );
        setUpViewPager(viewPager);
    }

    public void setUpViewPager(ViewPager viewPager){

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(SelectCarFragment.newInstance(ConstantUtils.TYPE_CARS),"Cars");
        viewPagerAdapter.addFragment(SelectBikeFragment.newInstance(ConstantUtils.TYPE_BIKES),"Bikes");
        viewPager.setAdapter(viewPagerAdapter);
    }
}

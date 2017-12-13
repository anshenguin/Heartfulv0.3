package com.kinitoapps.ngolink;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.kinitoapps.ngolink.R.layout.firstpage);

        // Find the view pager that will allow the user to swipe between fragments
        final ViewPager viewPager = (ViewPager) findViewById(com.kinitoapps.ngolink.R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        final SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this,getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        viewPager.getAdapter().notifyDataSetChanged();

        final TabLayout tabLayout = (TabLayout) findViewById(com.kinitoapps.ngolink.R.id.tabs);


        tabLayout.setupWithViewPager(viewPager);

    }



}

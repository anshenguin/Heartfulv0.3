package com.example.hp.heartful;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private View prelollipop;
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthStateListener;
//    private boolean isLoggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        prelollipop = findViewById(R.id.prelollipop);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            prelollipop.setVisibility(View.GONE);
        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.firstpage);

        // Find the view pager that will allow the user to swipe between fragments
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        final SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this,getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        viewPager.getAdapter().notifyDataSetChanged();

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        mAuth = FirebaseAuth.getInstance();
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                if (mAuth.getCurrentUser() == null) {
//                    isLoggedIn = false;
//                    Log.v("Running", "Sign up page AUTH STATE");
//
//
//                } else {
//
//                    isLoggedIn = true;
//                    Log.v("Running", "Sign in page AUTH STATE");
//
//                }
//
//                Log.v("Running","main");
//            }
//
//
//        };
//        mAuth.addAuthStateListener(mAuthStateListener);
        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()

        tabLayout.setupWithViewPager(viewPager);

    }



}

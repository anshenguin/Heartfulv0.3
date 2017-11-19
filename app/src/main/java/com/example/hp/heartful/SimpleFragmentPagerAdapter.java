package com.example.hp.heartful;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by HP INDIA on 08-Apr-17.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private boolean isLoggedIn;
    private Context mContext;
    public SimpleFragmentPagerAdapter(Context context , FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.tab_one);
        } else if (position == 1) {
            return mContext.getString(R.string.tab_two);
        } else  {
            return mContext.getString(R.string.tab_three);
        }
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            Log.v("Running", "tab one");
            return new FragmentOne();
        } else if (position == 1) {
            Log.v("Running", "tab two");
            return new FragmentTwo();
        } else {
            mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() == null) {
                Log.v("Running", "Sign up page");
                isLoggedIn = false;
            } else {
                Log.v("Running", "Sign in page");
                isLoggedIn = true;
            }

            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (mAuth.getCurrentUser() == null) {
                        isLoggedIn = false;
                        Log.v("Running", "Sign up page AUTH STATE");
                    } else {

                        isLoggedIn = true;
                        Log.v("Running", "Sign in page AUTH STATE");

                    }
                    Log.v("main","");
                }


            };

            Log.v("Running", "tab three");
            mAuth.addAuthStateListener(mAuthStateListener);

            if (!isLoggedIn) {
                return new FragmentThree();
            } else {
                return new FragmentThreeProfile();
            }
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


}

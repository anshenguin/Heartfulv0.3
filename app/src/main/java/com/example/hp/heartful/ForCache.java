package com.example.hp.heartful;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by HP on 04-07-2017.
 */

public class ForCache extends android.app.Application  {
    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

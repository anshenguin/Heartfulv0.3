package com.example.hp.heartful;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by HP INDIA on 12-Apr-17.
 */

public class login_dialogbox extends android.support.v4.app.DialogFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login_popup, container, false);
            ImageButton dismiss = (ImageButton) rootView.findViewById(R.id.cross);
            dismiss.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            getDialog().setTitle("Simple Dialog");
            return rootView;

    }


}

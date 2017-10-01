package com.example.hp.heartful;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by HP INDIA on 08-Apr-17.
 */

public  class FragmentOne extends Fragment  {
    public FragmentOne() {}
    private DatabaseReference mDatabase;
//    private ArrayList<OrgInfo>orgInfoArrayList;
//    private OrgInfoAdapter adapter;
    private RecyclerView recyclerView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_one, container, false);
        super.onCreate(savedInstanceState);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("NgoList");
        mDatabase.keepSynced(true);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.ngo_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        //mNewsLists.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(linearLayoutManager);
        FirebaseRecyclerAdapter<OrgInfo,OrgInfoViewHolder>firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<OrgInfo, OrgInfoViewHolder>(
                OrgInfo.class,
                R.layout.home_list_item,
                OrgInfoViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(OrgInfoViewHolder viewHolder, OrgInfo model, int position) {
                final String post_key=getRef(position).getKey();
                viewHolder.setmOrginfo(model.getmOrginfo());
                viewHolder.setmOrgname(model.getmOrgname());
                viewHolder.setmCategory(model.getmCategory());
                viewHolder.setmImage(getActivity().getApplicationContext(),model.getmImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        Intent intent=new Intent(getActivity(),orgInsideActivity.class);
                        intent.putExtra("news_id",post_key);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Transition transition_two = TransitionInflater.from(getActivity()).inflateTransition(R.transition.transition_two);
                            getActivity().getWindow().setSharedElementEnterTransition(transition_two);
                            ImageView transitionView = (ImageView) v1.findViewById(R.id.org_logo);
                            Bundle b = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(getActivity(), transitionView, "orgimg").toBundle();
                            startActivity(intent, b);
                        }
                        else
                            startActivity(intent);
                    }
                });
            }

        };
//        View tv = rootView.findViewById(R.id.search_item);
//        tv.setFocusable(false);                                         //EDIT TEXT WALE KO HIGHLIGHT NA KRANE KE LIYE CODE
//        tv.setFocusableInTouchMode(true);
//        tv.requestFocus();
//        orgInfoArrayList= new ArrayList<>();
////        mDatabase= FirebaseDatabase.getInstance().getReference().child("Info");
////
//        orgInfoArrayList.add(new OrgInfo("AASRA", "do you wish you could help yourself well now you can lol", R.drawable.heart));
//        orgInfoArrayList.add(new OrgInfo("Sada Suhaagan Raho", "test info", R.drawable.heart));
//        orgInfoArrayList.add(new OrgInfo("NGO with a really big name", "but an average info length", R.drawable.heart));
//        orgInfoArrayList.add(new OrgInfo("WHO", "are you?", R.drawable.heart));
//        orgInfoArrayList.add(new OrgInfo("aaaaaaaaaaabbbbbbbbbbbccccccccccccc", "aaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbccccccccccccaaaaaaaaaaaacccccccccc", R.drawable.heart));
//        orgInfoArrayList.add(new OrgInfo("chhota naam", "but info itni bdi ki chhote devices mein info view ke baahar chle jaae and last mein teen chhote dots dikhein haha", R.drawable.heart));
//        orgInfoArrayList.add(new OrgInfo("Jaago Graahak Jaago", "Apne Adhikaar ko Jaano, Apne Hakk ko Pehchano. Jaago, Graahak Jaago!", R.drawable.heart));
//        orgInfoArrayList.add(new OrgInfo("Satyamev Jayate", "Saccha hai pyaar tera!", R.drawable.heart));
        Spinner spinner = (Spinner) rootView.findViewById(R.id.category_spinner);
////      adapter= new OrgInfoAdapter(getActivity(),orgInfoArrayList);
//// Create an ArrayAdapter using the string array and a default spinner layout
//
        ArrayAdapter<CharSequence> spinadapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(spinadapter);
//
//       final   EditText searchItem= (EditText)rootView. findViewById(R.id.search_item);
//        final ListView listView = (ListView) rootView.findViewById(R.id.ngo_list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
//        listView.setAdapter(adapter);


        // Capture Text in EditText
//        searchItem.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                // TODO Auto-generated method stub
//                String text = searchItem.getText().toString().toLowerCase(Locale.getDefault());
//                adapter.filter(text);
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//                                      int arg3) {
//                // TODO Auto-generated method stub
//            }
//        });





       recyclerView.setAdapter(firebaseRecyclerAdapter);
        return rootView;
    }

    public static class OrgInfoViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public OrgInfoViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }


        public void setmOrginfo(String s) {
            TextView post_title=(TextView)mView.findViewById(R.id.org_about);
            post_title.setText(s);
        }

        public void setmOrgname(String s) {
            TextView post_title=(TextView)mView.findViewById(R.id.org_name);
            post_title.setText(s);
        }

        public void setmImage(Context applicationContext, String image) {
            final ImageView post_image=(ImageView)mView.findViewById(R.id.org_logo);
            Glide
                    .with(applicationContext)
                    .load(image)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(post_image);

        }

        public void setmCategory(String s) {
            TextView post_title=(TextView)mView.findViewById(R.id.category);
            post_title.setText(s);
        }
    }
}







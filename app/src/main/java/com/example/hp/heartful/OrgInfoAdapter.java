package com.example.hp.heartful;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by HP INDIA on 10-Apr-17.
 */

public class OrgInfoAdapter extends RecyclerView.Adapter<OrgInfoAdapter.myViewHolder> {
    private ArrayList<OrgInfo> orgList;
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("NgoList");
    Context mContext;
    LayoutInflater inflater;
//    private List<OrgInfo> searchLists = null;
//    private ArrayList<OrgInfo> Organisations;
    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView orgName, orgAbout,orgCategory;
    public ImageView mIconView;

      View mView;
     String key = mDatabase.push().getKey();

    public myViewHolder(View itemView) {

            super(itemView);
        mView = itemView;
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,key,Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(mContext,orgInsideActivity.class);
//                intent.putExtra("news_id",key);
//                mContext.startActivity(intent);
            }
        });
            orgCategory=(TextView)mView.findViewById(R.id.category);
            orgName=(TextView)mView.findViewById(R.id.org_name);
            orgAbout=(TextView)mView.findViewById(R.id.org_about);
            mIconView=(ImageView)mView.findViewById(R.id.org_logo);
        }
    }
    public OrgInfoAdapter(Context context, ArrayList<OrgInfo> worldpopulationlist) {
        this.orgList=worldpopulationlist;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
//        this.Organisations = new ArrayList<>();


    }
    @Override
    public OrgInfoAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_list_item, parent, false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrgInfoAdapter.myViewHolder holder, int position) {
        OrgInfo info = orgList.get(position);
        holder.orgName.setText(info.getmOrgname());
        holder.orgAbout.setText(info.getmOrginfo());
        holder.orgCategory.setText(info.getmCategory());
        Glide.with(holder.mIconView.getContext()).load(info.getmImage()).into(holder.mIconView);
    }

    @Override
    public int getItemCount() {
        return orgList.size();
    }
    // Filter Class
    public void filter(ArrayList<OrgInfo>orgInfos) {
        orgList= new ArrayList<>();
        orgList.addAll(orgInfos);
        notifyDataSetChanged();
    }
//    Context mContext;
//    LayoutInflater inflater;
//    private List<OrgInfo> searchLists = null;
//    private ArrayList<OrgInfo> Organisations;
//
//    public OrgInfoAdapter(Context context,ArrayList<OrgInfo> worldpopulationlist) {
//        super(context, 0, worldpopulationlist);
//        mContext = context;
//        this.searchLists = worldpopulationlist;
//        inflater = LayoutInflater.from(mContext);
//        this.Organisations = new ArrayList<OrgInfo>();
//        this.Organisations.addAll(worldpopulationlist);
//    }
//
//
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        // Check if an existing view is being reused, otherwise inflate the view
//        View listItemView = convertView;
//        if (listItemView == null) {
//            listItemView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.home_list_item, parent, false);
//
//        }
//        OrgInfo currentOrg = (OrgInfo) getItem(position);
//
//        // Find the TextView in the list_item.xml layout with the ID miwok_text_view.
//        TextView orgName = (TextView) listItemView.findViewById(R.id.org_name);
//        // Get the Miwok translation from the currentWord object and set this text on
//        // the Miwok TextView.
//        orgName.setText(currentOrg.getOrgname());
//        // Find the TextView in the list_item.xml layout with the ID default_text_view.
//        TextView orgInfo = (TextView) listItemView.findViewById(R.id.org_about);
//        // Get the default translation from the currentWord object and set this text on
//        // the default TextView.
//        orgInfo.setText(currentOrg.getOrginfo());
//        // Find the ImageView in the list_item.xml layout with the ID image.
//        ImageView imageView = (ImageView) listItemView.findViewById(R.id.org_logo);
//        // Check if an image is provided for this word or not
//        if (currentOrg.hasImage()) {
//            // If an image is available, display the provided image based on the resource ID
//            imageView.setImageResource(currentOrg.getImageResourceId());
//            // Make sure the view is visible
//            imageView.setVisibility(View.VISIBLE);
//        } else {
//            // Otherwise hide the ImageView (set visibility to GONE)
//            imageView.setVisibility(View.GONE);
//        }
//        // Return the whole list item layout (containing 2 TextViews) so that it can be shown in
//        // the ListView.
//        return listItemView;
//    }
//
//    // Filter Class
//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        searchLists.clear();
//        if (charText.length() == 0) {
//            searchLists.addAll(Organisations);
//        }
//        else
//        {
//            for (OrgInfo wp : Organisations)
//            {
//                if (wp.getOrginfo().toLowerCase(Locale.getDefault()).contains(charText))
//                {
//                    searchLists.add(wp);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
}
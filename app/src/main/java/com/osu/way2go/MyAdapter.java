package com.osu.way2go;

/**
 * Created by jhansi_lak on 10/28/2015.
 */


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static  final String TAG = "MyAdapter";
    Context mContext;
    MapsActivity mapsActivity;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    //private int mIcons[];

    private String name;
    private int profile;
    private String email;

    int selectedItem;




    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        ImageView imageView;
        ListView hiddenList;
        ImageView profile;
        TextView Name;
        TextView email;


        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                hiddenList = (ListView) itemView.findViewById(R.id.expandableList);
                Holderid = 1;
            } else {


                Name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                profile = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = 0;
            }
        }


    }


    MyAdapter(String Titles[], String Name, String Email, int Profile, Context context) {
        mContext = context;
        mapsActivity = (MapsActivity) context;
        mNavTitles = Titles;
       // mIcons = Icons;
        name = Name;
        email = Email;
        profile = Profile;
    }




    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_item_row, parent, false);

            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            ViewHolder vhHeader = new ViewHolder(v, viewType);
            return vhHeader;
        }
        return null;

    }


    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
        if (holder.Holderid == 1) {
            holder.textView.setText(mNavTitles[position - 1]);
            //holder.imageView.setImageResource(mIcons[position - 1]);

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((TextView)v).getText().toString().equals("Invite Friends")){
                        selectedItem = position;
                        Toast.makeText(mContext, "inviting friends", Toast.LENGTH_SHORT).show();

                        final Dialog addFriendsDialog = new Dialog((mContext));
                        addFriendsDialog.setContentView(R.layout.add_friends_layout);
                        addFriendsDialog.setTitle("Select Friends");
                        ListView addFriendsList = (ListView) addFriendsDialog.findViewById(R.id.addFriends);
                        List<String> allUsers = null;
                        try {
                            allUsers = ParseUtility.getallUsers();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        final FriendsListDialogAdapter addFriendsAdapter = new FriendsListDialogAdapter(allUsers, mContext, 0);
                        addFriendsList.setAdapter(addFriendsAdapter);

                        addFriendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        });
                        Button inviteAll = (Button) addFriendsDialog.findViewById(R.id.inviteAll);
                        Button invite = (Button) addFriendsDialog.findViewById(R.id.invite);

                        final List<String> finalAllUsers = allUsers;
                        inviteAll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //invite everyone
                                ParseUtility.putInvites(finalAllUsers);
                                addFriendsDialog.dismiss();
                            }
                        });

                        invite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //invite selected ones
                                ParseUtility.putInvites(addFriendsAdapter.getSelectedFriendsList());
                                for(String s: addFriendsAdapter.getSelectedFriendsList()){
                                    Log.i(TAG, "selected friend : " + s);

                                }
                                addFriendsDialog.dismiss();
                            }
                        });

                        addFriendsDialog.show();
                    }else if(((TextView)v).getText().toString().equals("Invites")){

                        selectedItem = position;
                        List<String> invites = null;
                        try {
                            invites = ParseUtility.getInvites();
                            for(String s : invites){
                                Log.i(TAG, "in invites " + s);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "Clicked on invites. showing listview");
                        if(invites != null && !invites.isEmpty()){
                            InvitesAdapter adapter = new InvitesAdapter(mContext, mapsActivity,invites, true);
                            holder.hiddenList.setAdapter(adapter);
                        }


                    }else if(((TextView)v).getText().toString().equals("Connected")){
                        selectedItem = position;
                        List<String> connected = null;
                        try {
                            connected = ParseUtility.getConnectedList();
                            for(String s : connected){
                                Log.i(TAG, "in invites " + s);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "Clicked on invites. showing listview");
                        if(connected != null && !connected.isEmpty()){
                            InvitesAdapter adapter = new InvitesAdapter(mContext, mapsActivity,connected, false);
                            holder.hiddenList.setAdapter(adapter);
                        }
                    }else if(((TextView)v).getText().toString().equals("Blocked")){
                        selectedItem = position;
                        List<String> blocked = null;
                        try {
                            blocked = ParseUtility.getBlockedList();
                            //for(String s : blocked){
                            //Log.i(TAG, "in invites " + s);
                            //}
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "Clicked on invites. showing listview");
                        if(blocked != null && !blocked.isEmpty()){
                            InvitesAdapter adapter = new InvitesAdapter(mContext, mapsActivity,blocked, false);
                            holder.hiddenList.setAdapter(adapter);
                        }
                    }else if(((TextView)v).getText().toString().equals("Block Users")){
                        //Toast.makeText(mContext, "inviting friends", Toast.LENGTH_SHORT).show();
                        final Dialog addFriendsDialog = new Dialog((mContext));
                        addFriendsDialog.setContentView(R.layout.add_friends_layout);
                        addFriendsDialog.setTitle("Select Users to Block");
                        ListView addFriendsList = (ListView) addFriendsDialog.findViewById(R.id.addFriends);
                        List<String> allUsers = null;
                        try {
                            allUsers = ParseUtility.getallUsers();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        final FriendsListDialogAdapter addFriendsAdapter = new FriendsListDialogAdapter(allUsers, mContext, 0);
                        addFriendsList.setAdapter(addFriendsAdapter);

                        Button blockAll = (Button) addFriendsDialog.findViewById(R.id.inviteAll);
                        blockAll.setVisibility(View.INVISIBLE);
                        Button block = (Button) addFriendsDialog.findViewById(R.id.invite);
                        block.setText("Block");
                        final List<String> finalAllUsers = allUsers;
                        //bbb
                        block.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //block selected ones
                                ParseUtility.putBlocked(addFriendsAdapter.getSelectedFriendsList());
                                for(String s: addFriendsAdapter.getSelectedFriendsList()){
                                    Log.i(TAG, "selected friend : " + s);
                                }
                                addFriendsDialog.dismiss();
                            }
                        });
                        addFriendsDialog.show();
                    }else if(((TextView)v).getText().toString().equals("Logout")){
                        ParseUtility.logout();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                        mapsActivity.finish();
                    }
                    if(selectedItem == position){
                        setListViewHeightBasedOnItems(holder.hiddenList);
                        if(holder.hiddenList.getVisibility() == View.INVISIBLE || holder.hiddenList.getVisibility() == View.GONE){
                            holder.hiddenList.setVisibility(View.VISIBLE);
                        }else if(holder.hiddenList.getVisibility() == View.VISIBLE){
                            holder.hiddenList.setVisibility(View.GONE);
                        }

                    }

                }
            });
        } else {

            holder.profile.setImageResource(profile);
            holder.Name.setText(name);
            holder.email.setText(email);
        }


    }

    public boolean setListViewHeightBasedOnItems(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter != null){
            int numberOfItems = listAdapter.getCount();
            int totalItemsHeight = 0;
            for(int i = 0; i < numberOfItems; i++){
                View item = listAdapter.getView(i, null, listView);
                item.measure(0,0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            int totalDividersHeight = listView.getDividerHeight()*(numberOfItems - 1);
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        }else{
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return mNavTitles.length + 1;
    }



    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}

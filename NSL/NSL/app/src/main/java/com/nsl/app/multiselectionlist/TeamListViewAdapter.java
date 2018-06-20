package com.nsl.app.multiselectionlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nsl.app.R;
import com.nsl.app.pojo.MappedRetailerWithDistributorPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sys on 6/15/2017.
 */

public class TeamListViewAdapter extends ArrayAdapter<MappedRetailerWithDistributorPojo>
{
    View row;
    List<MappedRetailerWithDistributorPojo> mappedRetailerWithDistributorPojos;
    int resLayout;
    Context context;

    public TeamListViewAdapter(Context context, int textViewResourceId, List<MappedRetailerWithDistributorPojo> mappedRetailerWithDistributorPojos) {
        super(context, textViewResourceId, mappedRetailerWithDistributorPojos);
        this.mappedRetailerWithDistributorPojos = mappedRetailerWithDistributorPojos;
        resLayout = textViewResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        row = convertView;
        if(row == null)
        {   // inflate our custom layout. resLayout == R.layout.row_team_layout.xml
            LayoutInflater ll = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = ll.inflate(resLayout, parent, false);
        }

        MappedRetailerWithDistributorPojo item = mappedRetailerWithDistributorPojos.get(position); // Produce a row for each Team.

        if(item != null)
        {   // Find our widgets and populate them with the Team data.
            TextView myTeamDescription = (TextView) row.findViewById(R.id.listview_TeamDescription);
            TextView myTeamWins = (TextView) row.findViewById(R.id.listview_TeamWins);
            if(myTeamDescription != null)
                myTeamDescription.setText(item.retailerName);
           // if(myTeamWins != null)
              //  myTeamWins.setText("Wins: " + String.valueOf(item.getTeamWins()));
        }
        return row;
    }
}
package com.example.bycycle.routesList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bycycle.R;

/*
    Adapter for the list activity
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private RouteView[] itemsData;

    public MyAdapter(RouteView[] itemsData) {
        this.itemsData = itemsData;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rowlayout, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.txtView1.setText(itemsData[position].getName());
        viewHolder.txtView2.setText(itemsData[position].getZone());
        viewHolder.txtView3.setText(itemsData[position].getLengthtime());
        viewHolder.txtView4.setText(itemsData[position].getReward()+" coins");
        viewHolder.txtView5.setText(itemsData[position].getDate());

    }

    // inner class to hold a reference to each item of RecyclerView 
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtView1;
        public TextView txtView2;
        public TextView txtView3;
        public TextView txtView4;
        public TextView txtView5;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtView1 = (TextView) itemLayoutView.findViewById(R.id.textRowl);
            txtView2 = (TextView) itemLayoutView.findViewById(R.id.textRow2);
            txtView3 = (TextView) itemLayoutView.findViewById(R.id.textRow3);
            txtView4 = (TextView) itemLayoutView.findViewById(R.id.textRow4);
            txtView5 = (TextView) itemLayoutView.findViewById(R.id.textRow5);

        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.length;
    }
}
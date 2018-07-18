package com.cumaprokerajasih.o2sApp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cumaprokerajasih.o2sApp.R;
import com.cumaprokerajasih.o2sApp.activities.ActivityAbout;
import com.cumaprokerajasih.o2sApp.activities.ActivityCart;
import com.cumaprokerajasih.o2sApp.activities.ActivityCheckout;
import com.cumaprokerajasih.o2sApp.activities.ActivityInformation;
import com.cumaprokerajasih.o2sApp.activities.ActivityMenuCategory;
import com.cumaprokerajasih.o2sApp.activities.ActivityProfile;
import com.cumaprokerajasih.o2sApp.adapters.AdapaterGridView;
import com.cumaprokerajasih.o2sApp.adapters.AdapterGridViewItem;

import java.util.ArrayList;

public class FragmentHome extends Fragment implements OnItemClickListener {

    GridView gridview;
    AdapaterGridView gridviewAdapter;
    ArrayList<AdapterGridViewItem> data = new ArrayList<AdapterGridViewItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        gridview = (GridView) v.findViewById(R.id.gridView1);
        gridview.setOnItemClickListener(this);

        data.add(new AdapterGridViewItem(getResources().getString(R.string.menu_product), ContextCompat.getDrawable(getActivity(), R.drawable.ic_product)));
        data.add(new AdapterGridViewItem(getResources().getString(R.string.menu_cart), ContextCompat.getDrawable(getActivity(), R.drawable.ic_cart)));

        setDataAdapter();

        return v;
    }

    // Set the Data Adapter
    private void setDataAdapter() {
        gridviewAdapter = new AdapaterGridView(getActivity(), R.layout.lsv_item_category, data);
        gridview.setAdapter(gridviewAdapter);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id) {
        if (position == 0) {
            startActivity(new Intent(getActivity(), ActivityMenuCategory.class));
        } else if (position == 1) {
            startActivity(new Intent(getActivity(), ActivityCart.class));
        }
    }

}
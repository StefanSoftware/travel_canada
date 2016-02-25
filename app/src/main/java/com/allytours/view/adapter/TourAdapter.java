package com.allytours.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.allytours.R;
import com.allytours.model.TourModel;
import com.allytours.view.PurchaseActivity;
import com.allytours.widget.HorizontalListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2/9/2016.
 */
public class TourAdapter extends BaseAdapter {

    ArrayList<TourModel> arrTours;
    Context mContext;
    LayoutInflater mlayoutInflater;
    public TourAdapter (Context context, ArrayList<TourModel> arrTours) {
        this.arrTours = arrTours;
        this.mContext = context;
        mlayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return arrTours.size();
    }

    @Override
    public Object getItem(int position) {
        return arrTours.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mlayoutInflater.inflate(R.layout.item_detail_tour, null);
        }
        HorizontalListView horizontalListView = (HorizontalListView)view.findViewById(R.id.horizontalListView);
        ImageAdapter imageAdapter = new ImageAdapter(mContext, arrTours.get(position).getArrImage());
        horizontalListView.setAdapter(imageAdapter);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int imageNum, long id) {
                Intent intent = new Intent(mContext, PurchaseActivity.class);
                intent.putExtra("tour", arrTours.get(position));
                mContext.startActivity(intent);
            }
        });
        return view;
    }
    private class ImageAdapter extends BaseAdapter {

        ArrayList<Integer> arrImages;
        Context mContext;
        LayoutInflater mlayoutInflater;
        public ImageAdapter (Context context, ArrayList<Integer> arrTours) {
            this.arrImages = arrTours;
            this.mContext = context;
            mlayoutInflater = LayoutInflater.from(mContext);
        }
        @Override
        public int getCount() {
            return arrImages.size();
        }

        @Override
        public Object getItem(int position) {
            return arrImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view1 = convertView;
            if (view1 == null) {
                view1 = mlayoutInflater.inflate(R.layout.item_detail_tour_image, null);
            }
            ImageView imageView = (ImageView)view1.findViewById(R.id.iv_image);
            imageView.setImageDrawable(mContext.getResources().getDrawable(arrImages.get(position)));
            return view1;
        }
    }
}

package com.baskrut.beavertv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by BasMalenkaya on 07.05.2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    static public ArrayList<String> aarImageAdapter;

    public ImageAdapter(Context c, ArrayList<String> arr) {
        mContext = c; aarImageAdapter = arr;
    }

    public int getCount() {
        // Log.d("myLog", "отрабатывает в ImageAdapter.getCount()"+" "+Integer.toString(aarImageAdapter.size()));
        return aarImageAdapter.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // public String getLink (int position){
    //     String link ;//="";
    //     link = mThumbIds.get(position);
    //     return String link;
    // }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));//было GridView.LayoutParams(85, 85)
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);


        new DownloadImageTask(imageView).execute(aarImageAdapter.get(position));
        return imageView;
    }

    // references to our images
    //private Integer[] mThumbIds = {
     /*       R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };*/
}
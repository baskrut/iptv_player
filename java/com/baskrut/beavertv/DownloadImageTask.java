package com.baskrut.beavertv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        // Log.d("myLog", "start doInBackground");
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            //Log.d("myLog", " mIcon11 = BitmapFactory.decodeStream(in);");
        } catch (Exception e) {
            // Log.e("Ошибка ", e.getMessage());
            e.printStackTrace();
        }
        // Log.d("myLog", "return mIcon11");
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        try{
            //  Log.d("myLog", "начали выполнять onPostExecute------------------------------------------------------------");

            bmImage.setImageBitmap(result);
            //   Log.d("myLog", "закончили выполнять onPostExecute");
        }
        catch (java.lang.Throwable e){
            e.printStackTrace();
        }
    }

}
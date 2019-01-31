package com.baskrut.beavertv;

import android.util.Log;

import java.util.ArrayList;

public class PlayListClassParser {

    PlayListClassParser(ArrayList<String> arrAllChanel){
        this.arrAllChanel = arrAllChanel;
    }

    private ArrayList<String> arrAllChanel;
    // static final String TAG = "myLog";

    protected String getLogo(int i){
//if(i<arrAllChanel.size()) {
//Log.d(TAG, "обозначили лого");
        String logo;
        String playList = arrAllChanel.get(i);

        if (playList.contains ("logo=\"")){
            logo = playList.substring(playList.lastIndexOf("logo=\"") + 6, playList.indexOf("\","));
            //  Log.d(, logo);
            if (logo.endsWith(".png") || logo.endsWith(".gif") || logo.endsWith(".jpeg") ||
                    logo.endsWith (".jpg") || logo.endsWith (".svg") || logo.endsWith (".swf")) {
                return logo;
            }
        }else {
            logo = "https://upload.wikimedia.org/wikipedia/commons/7/71/Pixelart-tv-iso-2.png";
        }
        //    Log.d(, logo);}
        return logo;
    }

    //protected int getKey(int i){
    //   String playList = arrAllChanel.get(i);
    //    if(playList.contains("&key")){
    //       number = Integer.parseInt(playList.substring(playList.indexOf("channel="),playList.indexOf("&key")));
//Log.d(TAG, "нашли номер из личного кабинета");
    //    }
    //   if(playList.contains("=pc")){
    //        number = Integer.parseInt(playList.substring(playList.indexOf("channel="),playList.indexOf("&device")));
//Log.d(TAG, "нашли номер из локал");
    //   }
    //   return number;
    //}

    protected String getName(int i){

        String name = "";
        StringBuilder sb = new StringBuilder ();
        String playList = arrAllChanel.get(i);

        if(playList.contains ("\n")){
            name = playList.substring(playList.indexOf(',')+1,playList.indexOf('\n'));//было '\n' вместо "http" "http", playList.indexOf(','))
            sb.append (name);
        }else if(playList.contains ("\r")){
            name = playList.substring(playList.indexOf(',')+1,playList.indexOf('\r'));//было '\n' вместо "http" "http", playList.indexOf(','))
            sb.append (name);

        }else if (name.isEmpty ()){
            name = Integer.toString (i);
            sb.append (name);
        }

        /*if (i==1){
            Log.d ("myLog", "String name = "+ name);
            Log.d ("myLog", "StringBuilder sb = " + sb.toString ());
            Log.d ("myLog", "String playList = arrAllChanel.get(i) = " + playList);
        }*/
//Log.d(TAG, "нашли название");
        return sb.toString ();
    }

    protected String getLink(int i){
        String link;
        String playList = arrAllChanel.get(i);
        link = playList.substring(playList.indexOf('\n' , playList.indexOf(',')) + 1,playList.length());//было http вместо '\n'
        Log.d ("myLog", " link = " + link);
        return link;
    }

    /*protected String getType(String playList){ //при создании списка типов из метода  private void createArrAllChannel() приходит строка
        //String playList = arrAllChanel.get(i);
      *//*  int i=arrAllChanel.indexOf(playList);*//*
     //   Log.d("myLog", playList);

        String type;
        if(playList.contains ("group-title=")) {

            type = playList.substring (playList.indexOf ("group-title=") + 12, playList.indexOf ("tvg-"));
//        Log.d(TAG, "нашли тип");
            return type;
        }else {
            type = "noType";
        }
        Log.d ("myLog", type);
        return type;
    }*/

}
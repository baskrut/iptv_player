package com.baskrut.beavertv;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener{

    ImageButton ibGrid;
    ImageButton ibList;
    ImageButton ibFolderOpenToolbar;
    ImageButton ibFolderOpenActivity;
    ImageButton settings;

    TextView textView;

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 14341;
    private static final int READ_REQUEST_CODE = 1905;
    private static final String TAG = "myLog";

    private static final String LOOK = "look";
    private static final String LOOK_ID = "look_id";

    private SharedPreferences shPrLook;

    final String FILENAME = "file";

//    GridView.LayoutParams layParGrid = new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT,
//            GridView.LayoutParams.WRAP_CONTENT);

//    ViewGroup.LayoutParams layParWrap_content = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//        ViewGroup.LayoutParams.WRAP_CONTENT);

    static ArrayList<String> arrAllChannel = new ArrayList<>();
    static ArrayList<String> arrChanelType = new ArrayList<>();
    static ArrayList<String> arrChanelName = new ArrayList<>();

    PlayListClassParser playListClassParser = new PlayListClassParser(arrAllChannel);

    static int iArrLength = 0; //масивы
    static int iType = 0;

    int indLook = 0; //sharedPreferences


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ibFolderOpenToolbar = findViewById(R.id.ibFolderOpenToolbar);
        ibFolderOpenToolbar.setOnClickListener(this);

        ibFolderOpenActivity = findViewById(R.id.ibFolderOpenActivity);
        ibFolderOpenActivity.setOnClickListener(this);

        ibGrid = findViewById(R.id.ibGrid);
        ibGrid.setOnClickListener(this);

        ibList = findViewById(R.id.ibList);
        ibList.setOnClickListener(this);

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(this);

        textView = findViewById(R.id.textView);
        textView.setOnClickListener(this);

        shPrLook = getSharedPreferences(LOOK, MODE_PRIVATE);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // if ()
        createArrAllChannel();

        if (shPrLook.contains(LOOK_ID)){
            indLook = shPrLook.getInt(LOOK_ID, 10);
        }

        if (indLook == 1){
            inflateList();
            Log.d(TAG, "inflateGrid in onCreate");
        }else{
            inflateGrid();
            Log.d(TAG, "inflateList in onCreate");
        }

    }

    public void onRequestPermission(){//todo проверить этот метод для рантайм пермиссион
        Log.d(TAG, "выполняется онРек..пер..");

        if (Build.VERSION.SDK_INT > 21){

            Log.d(TAG, "build.VERSION.SDK_INT > 21");

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "если разрешения нет");

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Log.d(TAG, "если разрешение НЕ дали с первого раза кидаем тост");
                    Toast.makeText(this, getString(R.string.needPermission), Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                }else{
                    Log.d(TAG, "вызываем ActivityCompat.requestPermissions(this,\n" +
                            "                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},\n" +
                            "                            MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);");

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                }
            }else{
                intentCatchFileFromStorage();
            }

        }else{

            intentCatchFileFromStorage();
            // Permission has already been granted
        }
    }

    @Override//todo проверить этот метод для рантайм пермиссион
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults){
        Log.d(TAG, "выполняется онРек..пер..ресулт");
        switch (requestCode){
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "если разрешение дали вызываем интент");
                    intentCatchFileFromStorage();

                }else{
                    Log.d(TAG, "если разрешение НЕ дали кидаем тост");
                    Toast.makeText(this, getString(R.string.needPermission), Toast.LENGTH_LONG);

                }

            }
        }
    }

    void intentCatchFileFromStorage(){
        try{
            Intent intentGetFileFromStorage = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intentGetFileFromStorage.addCategory(Intent.CATEGORY_OPENABLE);
            intentGetFileFromStorage.setType("*/m3u");
            startActivityForResult(intentGetFileFromStorage, READ_REQUEST_CODE);
        }catch (ActivityNotFoundException e){
            Log.d(TAG, "ActivityNotFoundException e trows intentCatchFileFromStorage()");
            e.printStackTrace();
        }catch (SecurityException e){
            onRequestPermission();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        if (requestCode == READ_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK){
            Uri uri;
            if (resultData != null){
                uri = resultData.getData();

                Log.d(TAG, " public void onActivityResult(int requestCode, int resultCode, Intent resultData) -> readTextFromUri(uri);");

                readTextFromUri(uri);
            }
        }
    }

    private void readTextFromUri(Uri uri){//todo переделать без использования uri
        try{
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null){
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                try{
                    while ((line = reader.readLine()) != null){

                        stringBuilder.append(line);
                        stringBuilder.append('\n');
                    }
                }catch (IOException e){
                    textView.setText(getString(R.string.wrongFormat));
                }
                inputStream.close();
                writeFileFromString(stringBuilder.toString());
            }
        }catch (IOException e){
            Log.d(TAG, "readTextFromUri(Uri uri) trows IOException");
            e.printStackTrace();
        }
    }

    private void writeFileFromString(String s){
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));

            bw.write(s);

            bw.close();
            Log.d(TAG, "Файл записан");

        }catch (IOException e){
            e.printStackTrace();
        }
        createArrAllChannel();
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.ibFolderOpenToolbar || v.getId() == R.id.textView
                || v.getId() == R.id.ibFolderOpenActivity){

            Log.d(TAG, "нажали кнопку вызвали онРеквестПермисион");


            onRequestPermission();

            Log.d(TAG, "вернулись в кнопку после онРеквестПермисион");
        }

        if (v.getId() == R.id.settings){
            Log.d(TAG, "нажали кнопку сетингс");

        }

        if (v.getId() == R.id.ibGrid){
            Log.d(TAG, "нажали кнопку грид");
            SharedPreferences.Editor editorGrid = shPrLook.edit();
            editorGrid.putInt(LOOK_ID, 0);
            editorGrid.apply();
            if (shPrLook.contains(LOOK_ID)){
                indLook = shPrLook.getInt(LOOK_ID, 1);
            }
            inflateGrid();
        }

        if (v.getId() == R.id.ibList){
            Log.d(TAG, "нажали кнопку лист");
            SharedPreferences.Editor editorList = shPrLook.edit();
            editorList.putInt(LOOK_ID, 1);
            editorList.apply();
            if (shPrLook.contains(LOOK_ID)){
                indLook = shPrLook.getInt(LOOK_ID, 100);
            }
            inflateList();
        }
    }

    private void createArrAllChannel(){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));

            StringBuilder sb = new StringBuilder();
            String line;

            byte i = 0;
            iArrLength = 0;
            iType = 0;
//            String keepType = "";
            arrChanelType.clear();
            arrAllChannel.clear();

            while ((line = reader.readLine()) != null){

                if (!line.isEmpty()){

                    if (i > 0){
                        sb.append(line);
                        sb.append('\n');
                    }
                    if (i == 2){
                        String s = sb.toString();
                        arrAllChannel.add(iArrLength, s);
                        iArrLength++;
                        Log.d(TAG, "iArrLength++" + iArrLength);

                        i = 0;
                        sb.delete(0, sb.length());
                    }
                    i++;
                }
            }
            arrChanelType.add(iType, getString(R.string.allChanel));
            iType++; //нужно чтобы отображался последний пункт подменю
            Log.d(TAG, "arrChanelType.add (iType, getString (R.string.allChanel)); iType =" + iType);

            reader.close();

            if (shPrLook.contains(LOOK_ID)){
                indLook = shPrLook.getInt(LOOK_ID, 10);
            }

            if (indLook == 1){
                inflateGrid();
                Log.d(TAG, "inflateGrid in onCreate");
            }else{
                inflateList();
                Log.d(TAG, "inflateList in onCreate");
            }

        }catch (FileNotFoundException e){
            Log.d(TAG, "словили ексепшин что нет файла");

        }catch (IOException e){
            Log.d(TAG, "readLine() trows IOException e");
        }
   }


    void inflateList(){

        textView.setVisibility(View.GONE);
        ibFolderOpenActivity.setVisibility(View.GONE);
        ibList.setVisibility(View.GONE);
        ibGrid.setVisibility(View.VISIBLE);

        LinearLayout llForGrid;
        llForGrid = (LinearLayout) findViewById(R.id.llForGrid);
        llForGrid.removeAllViews();

        LinearLayout llForList;
        llForList = (LinearLayout) findViewById(R.id.llForList);
        llForList.removeAllViews();
        ListView lv = new ListView(this);
        lv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        llForList.addView(lv);

        arrChanelName.clear();
//Log.d (TAG, "iArrLength in inflateList = " + iArrLength + "iType = " + iType + "typeId = " + typeId);

        for (int i = 0; i < iArrLength; i++){
            arrChanelName.add(playListClassParser.getName(i));
        }

        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrChanelName);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);


    }

    void inflateGrid(){//todo добавить названия каналов к картинкам через subItem

        textView.setVisibility(View.GONE);
        ibFolderOpenActivity.setVisibility(View.GONE);
        ibGrid.setVisibility(View.GONE);
        ibList.setVisibility(View.VISIBLE);

        LinearLayout llForList;
        llForList = (LinearLayout) findViewById(R.id.llForList);
        llForList.removeAllViews();

        LinearLayout llForGrid;
        llForGrid = (LinearLayout) findViewById(R.id.llForGrid);
        llForGrid.removeAllViews();

        GridView gv = new GridView(this);
        gv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        gv.setColumnWidth(200);//90
        gv.setMinimumHeight(200);
        gv.setVerticalSpacing(10);
        gv.setPadding(10, 10, 10, 10);
        gv.setHorizontalSpacing(10);
        gv.setNumColumns(GridView.AUTO_FIT);
        gv.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gv.setGravity(GridView.TEXT_ALIGNMENT_CENTER);
        llForGrid.addView(gv);

        // ArrayList<String> arrChanelName = new ArrayList<>();

        ArrayList<String> arrChanLogo = new ArrayList<>();

        for (int i = 0; i < iArrLength; i++){
            arrChanLogo.add(playListClassParser.getLogo(i));
            //    Log.d (TAG, " arrChanLogo.add " + Integer.toString (i) + " " + Integer.toString (arrChanLogo.size ()));
        }

        ImageAdapter adapter = new ImageAdapter(this, arrChanLogo);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        Intent playIntent = new Intent(Intent.ACTION_VIEW);

        if (ibList.getSystemUiVisibility() == 0){//todo дописал эту строчку вместо проверки через шаред преференс

            Log.d(TAG, "if (ibList.getSystemUiVisibility () == 0)");
            if (playListClassParser.getLogo(position).equals(ImageAdapter.aarImageAdapter.get(position))){

                playIntent.setDataAndType(Uri.parse(playListClassParser.getLink((position/*i*/))), "video/*");
                if (playIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(playIntent);
                    Log.d(TAG, "вызвали интент+ + + + + + + + + + + + + + + +" + playListClassParser.getName((/*i*/position)) + " " + Integer.toString(/*i*/position));
                }
            }

        }else if (playListClassParser.getName(position).equals(arrChanelName.get(position))){
            Log.d(TAG, "if (ibList.getSystemUiVisibility () != 0)");
            Log.d(TAG, "вызываем интент+ + + + + + + + + + + + + + + +" + playListClassParser.getName((/*i*/position)) + " " + Integer.toString(/*i*/position));
            playIntent.setDataAndType(Uri.parse(playListClassParser.getLink((/*i*/position))), "video/*");

            if (playIntent.resolveActivity(getPackageManager()) != null){
                startActivity(playIntent);
                Log.d(TAG, "вызвали интент+ + + + + + + + + + + + + + + +" + playListClassParser.getName((/*i*/position)) + " " + Integer.toString(/*i*/position));
            }
        }
    }
}
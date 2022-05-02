package com.example.downloadbg;



import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DownloadBackGround extends IntentService {

    //use DownloadManager in the IntentService, to make the downloading
    // 1.run in the background(avoid blocking the UI)
    // 2.still run even after the app is closed

    public DownloadBackGround() {
        super("DownloadSongService");

    }


    private static final String DOWNLOAD_PATH = "com.example.downloadbg_DownloadSongService_Download_path";
    private static final String DESTINATION_PATH = "com.example.downloadbg_DownloadSongService_Destination_path";


    public static Intent getDownloadService(final @NonNull Context callingClassContext, final @NonNull String downloadURL) {
        return new Intent(callingClassContext, DownloadBackGround.class)
                .putExtra(DOWNLOAD_PATH, downloadURL);
        //start a new Intent
        //Intent is for make communications between service(DownloadBackGround) and activity(MainActivity)
        //new Intent(start class(calling class),destination class)
        //putExtra(variable's name, variable) to send the information of this variable between service and activity


    }

    //this method gets invoked when the intent is passed from the activity.
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // this part is useless, ignore it
        //test to get and put some extra information in the intent
        String downloadPath = intent.getStringExtra(DOWNLOAD_PATH);

        long downloadId = startDownload(downloadPath);
        //Log.e("downloadid2", String.valueOf(downloadId));
        //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putExtra("downloadId",String.valueOf(downloadId)));
        intent.putExtra("downloadId",downloadId);
    }

    public long startDownload(String downloadPath) {

        Uri uri = Uri.parse(downloadPath);
        DownloadManager.Request request = new DownloadManager.Request(uri); //download request
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // show visible downloading notification on top
        request.setTitle("Downloading a file"); // notification's title
        request.setVisibleInDownloadsUi(true);

        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "large.bin" ); //put the file in the download dir of the project
        //int downloadStatus = getDownloadStatus();
        long downloadId;
        //type 1
        //downloadId = ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);

        //type2
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request); //put the download task in a queue.
        //when download is finished, DownloadManger will send a broadcast "ACTION_DOWNLOAD_COMPLETE"


        return downloadId;
    }
    /*
    private int getDownloadStatus(){
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Cursor cursor = downloadManager.query(query);

        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);

            return status;
        }



        return DownloadManager.ERROR_UNKNOWN;

    }
    */



}
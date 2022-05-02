package com.example.downloadbg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // activity: the interaction UI
    //10MB .PDF
    private static final String DOWNLOAD_URL = "https://jsoncompare.org/LearningContainer/SampleFiles/PDF/sample-pdf-download-10-mb.pdf";

    //100MB .bin
    //private static final String DOWNLOAD_URL = "https://speed.hetzner.de/100MB.bin";

    //KB .PNG
    //private static final String DOWNLOAD_URL = "https://botw-pd.s3.amazonaws.com/styles/logo-thumbnail/s3/042011/evo.png?itok=1xnLYWzY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //---------broadcast receiver--------------------
        //filter:only received the signal from downloadmanager(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        //register broadcast receiver
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //when recieve DownloadManager.ACTION_DOWNLOAD_COMPLETE
                //make the success notification
                Toast toast = Toast.makeText(MainActivity.this,
                        "Download Complete", Toast.LENGTH_LONG);
                toast.show();

                /*
                //id get from the download background service
                long downloadId = intent.getLongExtra("downloadId",-1);
                //id get from the system service DownloadManager (after DownloadManger's job is finished, it sends out a signal)
                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.e("downloadId", downloadId+"-");
                Log.e("referenceId", String.valueOf(referenceId));

                //compare,if match, then the file we want to download in the service is downloaded successfully
                if(referenceId == downloadId) {
                    //make the success notification
                    Toast toast = Toast.makeText(MainActivity.this,
                            "Download Complete", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    //make the fail notification
                    Toast toast = Toast.makeText(MainActivity.this,
                            "Download fail", Toast.LENGTH_LONG);
                    toast.show();
                }

                 */
            }
        },filter);
        //DownloadSongService ds = new DownloadSongService(this);
    }



    public void startDownloading(View view){
        //button's onclick funciotn
        //when button is clicked, start the service method "getDownloadService" defined in "DownloadBackGround.java"
        startService(DownloadBackGround.getDownloadService(this, DOWNLOAD_URL));
    }





}

/*
                int status = intent.getIntExtra("DownloadStatus",-1);


                if(status == DownloadManager.STATUS_SUCCESSFUL){
                    Toast.makeText(MainActivity.this, "Download success!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Download FAIL！", Toast.LENGTH_LONG).show();
                }
                */
//String downloadId = intent.getStringExtra("downloadId");
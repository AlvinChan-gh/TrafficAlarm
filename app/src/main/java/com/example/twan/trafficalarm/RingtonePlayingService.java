package com.example.twan.trafficalarm;

import android.app.Service;
import java.security.Provider;
import java.util.List;
import java.util.Map;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by TWAN on 2017-10-21.
 */

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    int start_id;

    @Nullable
    @Override
    public IBinder onBind(Intent intent){

        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        boolean state=intent.getExtras().getBoolean("extra");

        Intent intent_main_activity= new Intent (this.getApplicationContext(), MainActivity.class);
        //set up an intent that goes to the main activity
        if (state==true){
            start_id = 1;
        } else {
            start_id =0;
        }

        //if else statements
        //no music playing user turns alarm on
        //start music
        if (startId==1) {
            Log.e("There is no music, ", "and you want on");
            media_song = MediaPlayer.create(this, R.raw.alarm);
            media_song.start();
            this.start_id=0;
        }
        //music playing user turns alarm off
        //stop music
        else {
            media_song.stop();
            media_song.reset();
            this.start_id=0;
            Log.e("There is music, ", "and you want off");
        }


      return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        //tell the user it stopped
        Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
    }

}

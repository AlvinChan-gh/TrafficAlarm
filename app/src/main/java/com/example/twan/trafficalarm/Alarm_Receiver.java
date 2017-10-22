package com.example.twan.trafficalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by TWAN on 2017-10-21.
 */

public class Alarm_Receiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context,Intent intent) {
        Log.e("We are in the receiver!","XD");

        // fetch extra string from the intent
        boolean get_your_boolean = intent.getExtras().getBoolean("extra");

        Log.e("What is the key? ", String.valueOf(get_your_boolean));

        //create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //pass the extra string from Main Activity to the Ringtone Playing Service
        service_intent.putExtra("extra", get_your_boolean);

        //start the ringtone service
        context.startService(service_intent);
    }
}

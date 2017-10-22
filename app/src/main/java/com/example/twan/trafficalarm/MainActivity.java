package com.example.twan.trafficalarm;

import android.app.AlarmManager;
import android.util.Log;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;   
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import java.util.Random;



public class MainActivity extends AppCompatActivity {
    //to make our alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pending_intent;
    TextView update_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        //initialize our alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize time picker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        //create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();

        //initialize switch
        Switch switch1 = (Switch) findViewById(R.id.switch1);

        //initialize text update box
        update_text = (TextView) findViewById(R.id.update_text);




        //create an intent to the Alarm Receiver class
        final Intent my_intent=new Intent(this.context,Alarm_Receiver.class);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    int hour= alarm_timepicker.getHour();
                    int minute= alarm_timepicker.getMinute();

                    int arrivalTime=hrToSec(hour)+minToSec(minute);
                    int currentTime= (int)(calendar.getTimeInMillis()/1000);
                    DirectionAPI da= new DirectionAPI("Toronto","Newmarket",arrivalTime,currentTime);
                    int alarmTime=0;
                    alarmTime = arrivalTime - da.callAPI() - 600;

                    //create an object of DirectionAPI
                    da.contCheck();
                    boolean pm=false;

                    String hourSt = String.valueOf(hour);
                    String minuteSt;
                    if (pm)
                        minuteSt= String.valueOf(minute)+" PM";
                    else
                        minuteSt= String.valueOf(minute)+" AM";
                    if (minute<10)
                        minuteSt="0"+minuteSt;
                    Toast.makeText(getApplicationContext(),"Alarm set to " +hourSt+":" + minuteSt,Toast.LENGTH_LONG).show();
                    my_intent.putExtra("extra",true);
                    //create a pending intent that delays the intent until the specified time
                    pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    //set the alarm manager
                    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pending_intent);
                    set_alarm_text("Alarm set at: " + convTime(alarmTime));

                } else {
                    Toast.makeText(getApplicationContext(),"Alarm OFF",Toast.LENGTH_LONG).show();
                    alarm_manager.cancel(pending_intent);
                    //put extra boolean into my intent
                    //tells the clock that you pressed the "alarm off" buttom
                    my_intent.putExtra("extra", false);
                    sendBroadcast(my_intent);
                    set_alarm_text("Alarm OFF");
                }


//                //create a pending intent that delays the intent until the specified time
//                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                //set the alarm manager
//                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pending_intent);

            }
        });


    }
    private void set_alarm_text(String output){
        update_text.setText(output);
    }

    public String convTime(int sec){
        int min = sec/60;
            min = min%60;
        int hr = (sec-min*60)/3600;
        if (min<10)
            return hr+":0"+min;
        return hr+":"+min;
    }

    public int hrToSec(int hr){
        return hr*3600;
    }

    public int minToSec(int min){
        return min*60;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

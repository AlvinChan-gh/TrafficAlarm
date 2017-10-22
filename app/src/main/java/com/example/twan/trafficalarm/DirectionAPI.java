package com.example.twan.trafficalarm;

import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Alvin on 2017-10-22.
 */

public class DirectionAPI {

    private String origin;
    private String destination;
    private int arrivalTime;
    private int currentTime;

    public DirectionAPI(String og,String dn,int at,int ct){
        origin=og;
        destination=dn;
        arrivalTime=at;
        currentTime=ct;
    }

    public void contCheck(){
        final int INTERVAL=1000;
        DirectionAPI da = new DirectionAPI(origin,destination,arrivalTime,currentTime);
        boolean flag = true;
        while(flag){
            try {
                Thread.sleep(INTERVAL); //it will call the api every minute to update
                int dur = da.callAPI();
                if (currentTime + dur > arrivalTime - INTERVAL)
                    flag = false;
            }catch (Exception e){

            }
        }
    }



    public int callAPI(){
    int duration = 0;
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&arrival_time=" + arrivalTime + "&key=AIzaSyDDTKfYGAIsqXmkV0VTUE2EBrD4LoJWPP4";
        try {
            URLConnection connection = new URL(url).openConnection();
            InputStream response = connection.getInputStream();
            String responseBody = "";
            try (Scanner scanner = new Scanner(response)) {
                responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(response, "UTF-8"));
            JSONArray routes = jsonObject.getJSONArray("routes");
            JSONObject route = routes.getJSONObject(0);
            JSONArray legs = route.getJSONArray("routes");
            JSONObject leg = legs.getJSONObject(0);
            duration = leg.getJSONObject("duration").getInt("value");
        }catch(Exception e){
            }

        return duration;
    }

    public void contCheck(int t){ //debugg

    }
}

package com.example.fatfinger;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;

public class Data {
    private static final int participant_id = new Random().nextInt();
    private static ArrayList<DataRow> dataRowArrayList;

    Data() {
        if(dataRowArrayList == null)
            dataRowArrayList = new ArrayList<DataRow>();
    }

    public void addRow(int lens_No, int trial_No, boolean target_Clicked, double click_X, double click_Y, double target_X, double target_Y, long time_Taken) {
        dataRowArrayList.add(new DataRow(participant_id, lens_No, trial_No, target_Clicked, click_X, click_Y, target_X, target_Y, time_Taken));
    }

    public void sendDataToServer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Gson gson = new Gson();
                    URL url = new URL("http://hillcoat.ddnsfree.com:5544");
                    HttpURLConnection conn;
                    OutputStreamWriter os;

                    for(DataRow dataRow : dataRowArrayList) {
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        os = new OutputStreamWriter(conn.getOutputStream());
                        os.write(gson.toJson(dataRow));
                        os.flush();
                        os.close();
                        Log.println(Log.INFO, "RESPONSE: " , conn.getResponseMessage());
                        conn.disconnect();
                    }
                    Log.println(Log.INFO, "Data Transfer Successful", "Yay");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

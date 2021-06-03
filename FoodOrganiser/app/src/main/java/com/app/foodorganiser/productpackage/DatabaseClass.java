package com.app.foodorganiser.productpackage;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DatabaseClass {

    private final String DATABASE;
    private final String USER;
    private final String PASS;

    private HttpURLConnection connection;

    public DatabaseClass(String database, String user, String pass) {
        DATABASE = database;
        USER = user;
        PASS = pass;
    }

    //TODO: placeholder
    public DatabaseClass() {
        DATABASE = "http://192.168.0.165/projectAndroid/web/index.php"; //put in your local IP
        USER = "root";
        PASS = "";
    }

    public void openConnection() {
        try {
            URL url = new URL(DATABASE);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
        } catch (IOException e) {
            if (connection != null) {
                connection.disconnect();
            }
            e.printStackTrace();
            Log.wtf("Connect", "Connection Error");
        }
    }

    public boolean sendQuery(String query) {
        try {
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream)));
            bufferedWriter.write(query);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
    }

    public String receiveReply() {
        String line;
        StringBuilder list = new StringBuilder("");
        try {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
            while ((line = bufferedReader.readLine()) != null) {
                list.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            return list.toString();
        } catch (IOException e) {
            Log.wtf("Connect", e);
            e.printStackTrace();
            return null;
        }
        finally {
            connection.disconnect();
        }
    }
}
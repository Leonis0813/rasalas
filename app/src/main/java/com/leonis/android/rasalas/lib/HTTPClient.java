package com.leonis.android.rasalas.lib;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by leonis on 2019/09/22.
 */

public class HTTPClient extends AsyncTaskLoader<HashMap<String, Object>> {
    private static final String BASE_PATH = "/regulus/api";
    private static final String PORT = "80";

    private HttpURLConnection con;
    private HashMap<String, Object> response;
    private String baseUrl;
    private String credential;

    public HTTPClient(Context context) {
        super(context);
        InputStream inputStream = context.getClassLoader().getResourceAsStream("web-api.properties");

        try {
            Properties webApiProp = new Properties();
            webApiProp.load(inputStream);
            baseUrl = "http://" + webApiProp.getProperty("host") + ":" + PORT + BASE_PATH;

            String application_id = webApiProp.getProperty("application_id");
            String application_key = webApiProp.getProperty("application_key");
            byte[] credential = Base64.encode((application_id + ":" + application_key).getBytes(), Base64.DEFAULT);
            this.credential = new String(credential, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPredictions(String page, String pair) {
        try {
            StringBuilder query_string = new StringBuilder("?means=auto&page=");
            query_string.append(page);
            query_string.append("&pair=");
            query_string.append(pair);
            con = (HttpURLConnection) new URL(baseUrl + "/predictions" + query_string).openConnection();
            con.setRequestMethod("GET");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Object> sendRequest() {
        try {
            con.setRequestProperty("Authorization", "Basic " + credential);
            con.connect();

            StringBuilder sb = new StringBuilder();
            String st;
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } catch (FileNotFoundException e) {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }
            while((st = br.readLine()) != null){
                sb.append(st);
            }
            response = new HashMap<>();
            response.put("statusCode", con.getResponseCode());
            response.put("body", sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public HashMap<String, Object> loadInBackground() {
        return sendRequest();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

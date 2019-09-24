package com.leonis.android.rasalas.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leonis on 2019/09/22.
 */

public class Prediction {
    final private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private Date datetime;
    private String pair;
    private String result;

    public Prediction(JSONObject prediction) {
        try {
            this.datetime = formatter.parse(prediction.getString("created_at"));
            this.pair = prediction.getString("pair");
            this.result = prediction.getString("result");
        } catch (JSONException e) {
        } catch (ParseException e) {
        }
    }

    public Date getDatetime() {
        return this.datetime;
    }

    public String getPair() {
        return this.pair;
    }

    public String getResult() {
        return this.result;
    }
}

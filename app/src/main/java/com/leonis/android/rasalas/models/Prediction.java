package com.leonis.android.rasalas.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by leonis on 2019/09/22.
 */

public class Prediction {
    private Date datetime;
    private String pair;
    private String result;

    public Prediction(JSONObject prediction) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.JAPAN);
            this.datetime = formatter.parse(prediction.getString("created_at"));
            this.pair = prediction.getString("pair");
            this.result = prediction.getString("result");
        } catch (JSONException ignored) {
        } catch (ParseException ignored) {
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

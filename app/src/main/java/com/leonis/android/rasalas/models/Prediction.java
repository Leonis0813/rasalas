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
    final private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSSZ");
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
}

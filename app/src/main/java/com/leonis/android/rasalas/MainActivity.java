package com.leonis.android.rasalas;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.leonis.android.rasalas.lib.HTTPClient;
import com.leonis.android.rasalas.models.Prediction;
import com.leonis.android.rasalas.views.PredictionView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final int LOADER_ID = 0;

    private Context activity;
    private PredictionView predictionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        predictionView = findViewById(R.id.prediction);
        getPredictions(PredictionView.DEFAULT_PAGE, PredictionView.DEFAULT_PAIR);
    }

    public void getPredictions(String page, final String pair) {
        Bundle args = new Bundle();
        args.putString("page", page);
        args.putString("pair", pair);

        getLoaderManager().initLoader(LOADER_ID, args, new LoaderManager.LoaderCallbacks<HashMap<String, Object>>() {
            @Override
            public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
                HTTPClient httpClient = new HTTPClient(activity);
                httpClient.getPredictions(args.getString("page"), args.getString("pair"));
                return httpClient;
            }

            @Override
            public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
                int code = Integer.parseInt(data.get("statusCode").toString());
                if(code == 200) {
                    try {
                        JSONObject body = new JSONObject(data.get("body").toString());
                        JSONArray jsonArray = body.getJSONArray("predictions");
                        final ArrayList<Prediction> predictions = new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++) {
                            predictions.add(new Prediction(jsonArray.getJSONObject(i)));
                        }
                        if(!pair.equals(predictionView.getCurrentPair())) {
                            predictionView.clearListView();
                            predictionView.setCurrentPair(pair);
                        }
                        predictionView.addPredictions(predictions);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    predictionView.showMessage("予測情報の取得に失敗しました");
                }
                getLoaderManager().destroyLoader(LOADER_ID);
            }

            public void onLoaderReset(Loader<HashMap<String, Object>> loader) {}
        });
    }
}

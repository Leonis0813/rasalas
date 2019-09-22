package com.leonis.android.rasalas;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.leonis.android.rasalas.lib.HTTPClient;
import com.leonis.android.rasalas.models.Prediction;
import com.leonis.android.rasalas.views.PredictionView;

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public void getPredictions(HashMap<String, String> query) {
        Bundle args = new Bundle();
        args.putSerializable("query", query);
        getLoaderManager().initLoader(LOADER_ID, args, new LoaderManager.LoaderCallbacks<HashMap<String, Object>>() {
            @Override
            public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
                HTTPClient httpClient = new HTTPClient(activity);
                httpClient.getPredictions(((HashMap<String, String>) args.getSerializable("query")));
                return httpClient;
            }

            @Override
            public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
                int code = Integer.parseInt(data.get("statusCode").toString());
                if(code == 200) {
                    try {
                        JSONObject body = new JSONObject(data.get("body").toString());
                        ArrayList<Prediction> predictions = new ArrayList<>();
                        for(int i=0;i<body.getJSONArray("predictions").length();i++) {
                            predictions.add(new Prediction(body.getJSONArray("prediction").getJSONObject(i)));
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

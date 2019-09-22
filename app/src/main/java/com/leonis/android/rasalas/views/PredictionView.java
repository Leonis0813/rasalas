package com.leonis.android.rasalas.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.leonis.android.rasalas.MainActivity;
import com.leonis.android.rasalas.R;
import com.leonis.android.rasalas.models.Prediction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leonis on 2019/09/22.
 */

public class PredictionView extends RelativeLayout implements OnClickListener {
    private PredictionListAdapter predictionListAdapter;
    private ListView predictionListView;
    private ArrayList<Prediction> predictions;
    private final Button nextPage;
    private HashMap<String, String> query;
    private int currentPage;

    private final Context context;

    public PredictionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        View layout = View.inflate(context, R.layout.prediction_view, this);

        nextPage = layout.findViewById(R.id.index_next_page);
        nextPage.setOnClickListener(this);
        nextPage.setVisibility(INVISIBLE);

        predictions = new ArrayList<>();
        predictionListAdapter = new PredictionListAdapter(context, predictions);
        predictionListView = layout.findViewById(R.id.index_prediction_list);

        currentPage = 1;
    }

    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void addPredictions(ArrayList<Prediction> predictions) {
        for(Prediction payment : predictions) {
            this.predictions.add(payment);
        }
        predictionListView.setAdapter(predictionListAdapter);
        fixListViewHeight(predictionListView);
        nextPage.setVisibility(predictions.isEmpty() ? INVISIBLE : VISIBLE);
    }

    private void fixListViewHeight(ListView listView) {
        int totalHeight = 0;
        ListAdapter adapter = listView.getAdapter();
        int itemCount = adapter.getCount();

        for (int i = 0;i < itemCount;i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() - 50;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        query.put("page", String.valueOf(++currentPage));
        ((MainActivity) context).getPredictions(query);
    }
}

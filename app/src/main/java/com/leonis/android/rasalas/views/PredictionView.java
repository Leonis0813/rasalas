package com.leonis.android.rasalas.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.leonis.android.rasalas.MainActivity;
import com.leonis.android.rasalas.R;
import com.leonis.android.rasalas.models.Prediction;

import java.util.ArrayList;

/**
 * Created by leonis on 2019/09/22.
 */

public class PredictionView extends RelativeLayout implements OnClickListener, OnItemSelectedListener {
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_PAIR = "USDJPY";

    private final PredictionListAdapter predictionListAdapter;
    private final ListView predictionListView;
    private final ArrayList<Prediction> predictions;
    private final Spinner pairs;
    private final Button nextPage;
    private int currentPage;
    private String currentPair;

    private final Context context;

    public PredictionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        View layout = View.inflate(context, R.layout.prediction_view, this);

        nextPage = layout.findViewById(R.id.next_page);
        nextPage.setOnClickListener(this);
        nextPage.setVisibility(INVISIBLE);

        pairs = layout.findViewById(R.id.prediction_select_pair);
        pairs.setOnItemSelectedListener(this);

        predictions = new ArrayList<>();
        predictionListAdapter = new PredictionListAdapter(context, predictions);
        predictionListView = layout.findViewById(R.id.prediction_list);

        currentPage = 1;
        currentPair = DEFAULT_PAIR;
    }

    public String getCurrentPair() {
        return currentPair;
    }

    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void addPredictions(ArrayList<Prediction> predictions) {
        this.predictions.addAll(predictions);
        predictionListView.setAdapter(predictionListAdapter);
        fixListViewHeight(predictionListView);
        nextPage.setVisibility(predictions.isEmpty() ? INVISIBLE : VISIBLE);
    }

    public void clearListView() {
        predictions.clear();
    }

    private void fixListViewHeight(ListView listView) {
        int totalHeight = 0;
        ListAdapter adapter = listView.getAdapter();
        int itemCount = adapter.getCount();

        for (int i = 0;i < itemCount;i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + 3;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) context).getPredictions(Integer.toString(++currentPage), currentPair);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        String selectedPair = spinner.getSelectedItem().toString();
        if(!currentPair.equals(selectedPair)) {
            ((MainActivity) context).getPredictions(DEFAULT_PAGE, selectedPair);
            currentPair = selectedPair;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}

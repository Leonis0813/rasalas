package com.leonis.android.rasalas.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leonis.android.rasalas.R;
import com.leonis.android.rasalas.models.Prediction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by leonis on 2019/09/22.
 */

class PredictionListAdapter extends BaseAdapter {
    private final Context context;
    private final List<Prediction> predictions;

    private class ViewHolder {
        TextView dateView;
        TextView pairView;
        ImageView resultView;
    }

    public PredictionListAdapter(Context context, List<Prediction> predictions) {
        this.context = context;
        this.predictions = predictions;
    }

    @Override
    public int getCount() {
        return predictions.size();
    }

    @Override
    public Object getItem(int position) {
        return predictions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        Prediction prediction = predictions.get(position);

        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater != null ? inflater.inflate(R.layout.prediction_list_view, parent, false) : null;

            TextView dateView = view.findViewById(R.id.prediction_date);
            TextView pairView = view.findViewById(R.id.prediction_pair);
            ImageView resultView = view.findViewById(R.id.prediction_result);

            holder = new ViewHolder();
            holder.dateView = dateView;
            holder.pairView = pairView;
            holder.resultView = resultView;
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN);
        holder.dateView.setText(formatter.format(prediction.getDatetime()));
        holder.pairView.setText(prediction.getPair());
        switch (prediction.getResult()) {
            case "up":
                holder.resultView.setImageResource(R.mipmap.up);
                break;
            case "down":
                holder.resultView.setImageResource(R.mipmap.down);
                break;
            default :
                holder.resultView.setImageResource(R.mipmap.range);
        }

        return view;
    }
}

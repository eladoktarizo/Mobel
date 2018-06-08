package com.example.eladoktarizo.mobel.adapter;

/**
 * Created by Elad Oktarizo on 12/03/2018.
 */
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eladoktarizo.mobel.app.AppController;
import com.example.eladoktarizo.mobel.data.DataForMonitoring;
import com.example.eladoktarizo.mobel.R;

import java.util.List;

public class AdapterMonitoring extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataForMonitoring> items;

    public AdapterMonitoring(Activity activity, List<DataForMonitoring> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_laporanharian, null);

        TextView id_laporan = (TextView) convertView.findViewById(R.id.id_laporan);
        TextView judul_laporan = (TextView) convertView.findViewById(R.id.judul_laporan);
        TextView isi_laporan = (TextView) convertView.findViewById(R.id.isi_laporan);

        DataForMonitoring data = items.get(position);

        id_laporan.setText(data.getId_laporan());
        judul_laporan.setText(data.getJudul_laporan());
        isi_laporan.setText(data.getIsi_laporan());
        return convertView;
    }
}

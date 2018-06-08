package com.example.eladoktarizo.mobel.adapter;

/**
 * Created by Elad Oktarizo on 23/04/2018.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.eladoktarizo.mobel.R;
import com.example.eladoktarizo.mobel.Server;
import com.example.eladoktarizo.mobel.data.DataForGuru;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterGuru extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataForGuru> items;

    public AdapterGuru(Activity activity, List<DataForGuru> items) {
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
            convertView = inflater.inflate(R.layout.list_rowguru, null);


        TextView id_guru      = (TextView) convertView.findViewById(R.id.id_guru);
        TextView namalengkap = (TextView) convertView.findViewById(R.id.namalengkap);
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);
        ImageView iv_guru     = convertView.findViewById(R.id.iv_guru);

        DataForGuru data = items.get(position);

        id_guru.setText(data.getId_guru());
        namalengkap.setText(data.getNamalengkap());
        alamat.setText(data.getAlamat());
        Picasso.get()
                .load(Server.URL + data.getIv_guru())
                .placeholder(R.drawable.ic_action_icon)
                .error(R.drawable.ic_action_icon)
                .into(iv_guru);


        return convertView;

    }
}

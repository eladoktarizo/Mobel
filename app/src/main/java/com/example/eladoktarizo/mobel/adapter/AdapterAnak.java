package com.example.eladoktarizo.mobel.adapter;

/**
 * Created by Elad Oktarizo on 01/03/2018.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
import com.example.eladoktarizo.mobel.R;
import com.example.eladoktarizo.mobel.Server;
import com.example.eladoktarizo.mobel.data.DataForAnak;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAnak extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataForAnak> items;

    public AdapterAnak(Activity activity, List<DataForAnak> items) {
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
            convertView = inflater.inflate(R.layout.list_row_anak, null);


        TextView id_anak      = convertView.findViewById(R.id.id_anak);
        TextView noinduk_anak = convertView.findViewById(R.id.noinduk_anak);
        TextView nama_lengkap = convertView.findViewById(R.id.nama_lengkap);
//        TextView umur         = convertView.findViewById(R.id.umur);
        ImageView iv_anak     = convertView.findViewById(R.id.iv_anak);

        DataForAnak data = items.get(position);

        id_anak.setText(data.getId_anak());
        nama_lengkap.setText(data.getNama_lengkap());
        noinduk_anak.setText(data.getNoinduk_anak());
//        umur.setText(data.getUmur());
        Picasso.get()
                .load(Server.URL + data.getIv_anak())
                .placeholder(R.drawable.ic_action_icon)
                .error(R.drawable.ic_action_icon)
                .into(iv_anak);


        return convertView;
    }
}

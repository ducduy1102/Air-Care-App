package com.example.aircareapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aircareapp.Model.ItemsDetail;
import com.example.aircareapp.R;

import java.util.List;

public class DetailAssetAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<ItemsDetail> listItem;

    public DetailAssetAdapter(Context context, int layout, List<ItemsDetail> listItem) {
        this.context = context;
        this.layout = layout;
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout,null);
        ItemsDetail itemDetail = listItem.get(position);

        TextView name = convertView.findViewById(R.id.name);
        TextView dataAsset = convertView.findViewById(R.id.dataAsset);
        ImageView icon = convertView.findViewById(R.id.icon);

        name.setText(itemDetail.getName());
        dataAsset.setText(itemDetail.getDataAsset());
        icon.setImageResource(itemDetail.getIcon());
        return convertView;
    }
}

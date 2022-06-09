package com.baumannsw.lewind;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baumannsw.lewind.stations.WindStation;

import java.util.ArrayList;

public class EditListAdapter extends BaseAdapter {
    Context context;
    ArrayList<WindStation> stations;
    LayoutInflater inflater;
    private EditListListener listener;

    public interface EditListListener {
        public void deleteElement(long id);
        public void editElement(long id);
    }

    public EditListAdapter(Context context, ArrayList<WindStation> data, EditListListener listener) {
        this.context= context;
        this.stations = data;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Object getItem(int i) {
        return stations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return stations.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.edit_list_item, null);
        TextView tvName = view.findViewById(R.id.tvNameEditList);
        TextView tvId = view.findViewById(R.id.tvEditListId);
        ImageButton btnEdit = view.findViewById(R.id.imgEdit);
        btnEdit.setImageDrawable(view.getResources().getDrawable(android.R.drawable.ic_menu_edit, context.getTheme()));
        ImageButton btnDelete = view.findViewById(R.id.imgDelete);
        btnDelete.setImageDrawable(view.getResources().getDrawable(android.R.drawable.ic_menu_delete, context.getTheme()));
        tvName.setText(stations.get(i).getDisplayName());
        tvId.setText(stations.get(i).getName());
        btnEdit.setOnClickListener(v -> listener.editElement(stations.get(i).getId()));
        btnDelete.setOnClickListener(v -> listener.deleteElement(stations.get(i).getId()));
        return view;
    }
}

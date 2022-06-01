package com.abdallahehab.backendapitester.ui;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abdallahehab.backendapitester.R;
import com.abdallahehab.backendapitester.data.HeaderField;

import java.util.ArrayList;

public class HeaderFieldsListAdapter extends ArrayAdapter<HeaderField> {

    public HeaderFieldsListAdapter(@NonNull Context context, ArrayList<HeaderField> headerFields) {
        super(context, 0, headerFields);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.header_feild_item, parent, false);

        HeaderField currentHeaderField = getItem(position);

        TextView headerKeyTextView = currentItemView.findViewById(R.id.header_key);
        headerKeyTextView.setText(currentHeaderField.getKey());

        TextView headerValueTextView = currentItemView.findViewById(R.id.header_value);
        headerValueTextView.setText(currentHeaderField.getValue());

        ImageButton editBtn = currentItemView.findViewById(R.id.edit_header_btn);
        editBtn.setOnClickListener(view -> {
            ((MainActivity)getContext()).editHeaderItem(currentHeaderField);
            notifyDataSetChanged();
        });

        ImageButton deleteBtn = currentItemView.findViewById(R.id.delete_header_btn);
        deleteBtn.setOnClickListener(view -> {
            ((MainActivity)getContext()).removeHeaderItem(currentHeaderField);
        });

        return currentItemView;
    }
//    private void deleteItemFromList(HeaderField currentItem){
//        for (HeaderField header:dataList){
//            if (header.key.equals(currentItem.key)){
//                dataList.remove()
//            }
//        }
//    }
}




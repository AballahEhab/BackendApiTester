package com.abdallahehab.backendapitester.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        headerKeyTextView.setText(currentHeaderField.key);

        TextView headerValueTextView = currentItemView.findViewById(R.id.header_value);
        headerValueTextView.setText(currentHeaderField.value);


        return currentItemView;
    }
}




package com.miroslavmirkovic.bookclub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.dto.CommentDTO;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<CommentDTO> {

    public CommentAdapter(@NonNull Context context, ArrayList<CommentDTO> comments) {
        super(context, 0, comments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CommentDTO commentDTO = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
        }

        TextView comment = (TextView) convertView.findViewById(R.id.comm);
        comment.setText(commentDTO.getContent());

        return convertView;
    }
}

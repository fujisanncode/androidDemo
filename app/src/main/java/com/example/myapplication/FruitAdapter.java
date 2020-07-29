package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class FruitAdapter extends ArrayAdapter<Fruit> {
    private int resourceId;

    public FruitAdapter(@NonNull Context context, int resource, @NonNull List<Fruit> objects) {
        super(context, resource, objects);
        // item布局文件的id
        this.resourceId = resource;
    }

    // 数据实体，暂存的view，view实体(自定义的list_item)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ImageView img;
        TextView text;
        // convertView中缓存了每一个个view(list_item), view中缓存了img和text
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            img = view.findViewById(R.id.list_img);
            text = view.findViewById(R.id.list_text);
            ViewHolder viewHolder = new ViewHolder(img, text);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            img = ((ViewHolder)view.getTag()).getImageView();
            text = ((ViewHolder)view.getTag()).getTextView();
        }
        // 找到数据对象, 数据塞到页面中
        Fruit item = getItem(position);
        img.setImageResource(item.getSourceId());
        text.setText(item.getName());
        return view;
    }
}

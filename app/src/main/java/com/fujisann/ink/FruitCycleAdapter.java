package com.fujisann.ink;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FruitCycleAdapter extends RecyclerView.Adapter<FruitCycleAdapter.VHolder> {

    public static class VHolder extends RecyclerView.ViewHolder {

        private ImageView img;

        private TextView text;

        public ImageView getImg() {
            return img;
        }

        public TextView getText() {
            return text;
        }

        // 传入view_item
        public VHolder(@NonNull View itemView) {
            super(itemView);
            this.img = itemView.findViewById(R.id.list_img);
            this.text = itemView.findViewById(R.id.list_text);
        }
    }

    private List<Fruit> fruitList;

    public FruitCycleAdapter(List<Fruit> fruits) {
        this.fruitList = fruits;
    }

    // 缓存view
    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        VHolder holder = new VHolder(inflate);
        return holder;
    }

    // 数据设置到view中
    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.getImg().setImageResource(fruitList.get(position).getSourceId());
        holder.getText().setText(fruitList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }
}

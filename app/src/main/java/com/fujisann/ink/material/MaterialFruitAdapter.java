package com.fujisann.ink.material;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fujisann.ink.R;

import java.util.List;

public class MaterialFruitAdapter extends RecyclerView.Adapter<MaterialFruitAdapter.VHolder> {
  class VHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private ImageView imageView;
    private TextView textView;

    // 缓存item布局文件
    public VHolder(@NonNull View itemView) {
      super(itemView);
      this.cardView = (CardView) itemView;
      this.imageView = itemView.findViewById(R.id.view_card_img);
      this.textView = itemView.findViewById(R.id.view_card_text);
    }

    public CardView getCardView() {
      return cardView;
    }

    public ImageView getImageView() {
      return imageView;
    }

    public TextView getTextView() {
      return textView;
    }
  }

  static class MaterialFruit {
    // 水果图片名称
    private String fruitName;
    // 水果图片资源id
    private int fruitId;

    public MaterialFruit(String fruitName, int fruitId) {
      this.fruitName = fruitName;
      this.fruitId = fruitId;
    }

    public String getFruitName() {
      return fruitName;
    }

    public int getFruitId() {
      return fruitId;
    }
  }

  /** 数据列表 */
  private List<MaterialFruit> materialFruitList;

  public MaterialFruitAdapter(List<MaterialFruit> materialFruitList) {
    this.materialFruitList = materialFruitList;
  }

  /**
   * 视图初始化
   *
   * @param parent 父视图
   * @param viewType 视图类型
   * @return
   */
  @NonNull
  @Override
  public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (pContext == null) {
      pContext = parent.getContext();
    }
    // false 不附加到root上，list的item项不能附加到root上
    View view = LayoutInflater.from(pContext).inflate(R.layout.material_fruit_item, parent, false);
    final VHolder vHolder = new VHolder(view);
    // 添加监听事件
    vHolder
        .getCardView()
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                int position = vHolder.getAdapterPosition();
                MaterialFruit materialFruit = materialFruitList.get(position);
                MaterialFruitActivity.start(
                    pContext, materialFruit.getFruitName(), materialFruit.getFruitId());
              }
            });
    return vHolder;
  }

  /** 上下文 */
  private Context pContext;

  /**
   * 视图数据绑定
   *
   * @param holder 视图缓存对象
   * @param position 视图位置
   */
  @Override
  public void onBindViewHolder(@NonNull VHolder holder, int position) {
    MaterialFruit materialFruit = materialFruitList.get(position);
    holder.getTextView().setText(materialFruit.getFruitName());
    // 加载图片
    Glide.with(pContext).load(materialFruit.getFruitId()).into(holder.getImageView());
  }

  @Override
  public int getItemCount() {
    return materialFruitList.size();
  }
}

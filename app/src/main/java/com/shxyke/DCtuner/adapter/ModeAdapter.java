package com.shxyke.DCtuner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.shxyke.DCtuner.R;
import com.shxyke.DCtuner.helper.Utilties;

import java.util.List;

public class ModeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;
    private Context context;
    private OnItemClick onItemClick;
    private int previous = -1;

    public ModeAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick{
        void onClick(int pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_mode_item,parent,false);
        ModeViewHolder holder = new ModeViewHolder(view);
        holder.cardView.setOnClickListener(v -> {
            onItemClick.onClick(holder.getAdapterPosition());
            previous = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ModeViewHolder) {
            ((ModeViewHolder) holder).tvMode.setText(list.get(position));
            if (!Utilties.isColorModKernel()) {
                ((ModeViewHolder) holder).cardView.setEnabled(false);
            }
            if (position == previous) {
                clickViewChange(holder, true);
            } else {
                clickViewChange(holder, false);
            }
            if (position == 2 || position == 3) {
                ((ModeViewHolder) holder).cardView.setStrokeWidth(4);
                ((ModeViewHolder) holder).cardView.setStrokeColor(Color.RED);
                ((ModeViewHolder) holder).cardView.setCardBackgroundColor(Color.RED & 0x40FFFFFF);
                ((ModeViewHolder) holder).cardView.setEnabled(false);
                ((ModeViewHolder) holder).tvMode.append("(当前不可选择)");
            }
        }
    }

    private void clickViewChange(RecyclerView.ViewHolder holder, boolean bool) {
        int color = bool ? context.getColor(R.color.colorAccent) : context.getColor(R.color.colorGray);
        int colorCard = bool ? context.getColor(R.color.colorAccent) & 0x40FFFFFF: context.getColor(R.color.colorGray);
        ((ModeViewHolder) holder).cardView.setStrokeWidth(4);
        ((ModeViewHolder) holder).cardView.setStrokeColor(color);
        ((ModeViewHolder) holder).cardView.setCardBackgroundColor(colorCard);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ModeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMode;
        private MaterialCardView cardView;

        ModeViewHolder(View itemView) {
            super(itemView);
            tvMode = itemView.findViewById(R.id.tv_rv_mode);
            cardView = itemView.findViewById(R.id.card_item);
        }

    }

}

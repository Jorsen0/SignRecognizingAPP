package com.github.scarecrow.signscognizing.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.scarecrow.signscognizing.R;

import java.util.LinkedList;
import java.util.List;

public class PopupItemRecyclerViewAdapter extends RecyclerView.Adapter <PopupItemRecyclerViewAdapter.StringListItemViewHolder>{

    private static final String TAG = "PopupItemRecyclerViewAd";
    private List<String> items;
    private OnItemClickListener mListener = null;

    public PopupItemRecyclerViewAdapter(){
        items = new LinkedList<>();

    }


    public void setItemList(List<String> itemList){
        this.items = itemList;
        notifyDataSetChanged();
    }

    public List<String> getItems() {
        return items;
    }

    public void clearItems() {
        items.clear();
    }

    public void setItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public StringListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popup_item, parent, false);
        return new StringListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StringListItemViewHolder holder, int position) {
        final String nowContent = items.get(position);
        Log.d(TAG, "onBindViewHolder: content is " + nowContent);
        System.out.println("onBindViewHolder: content is " + nowContent);
        holder.content.setText(nowContent);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onItemticClick(view, nowContent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class StringListItemViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout item_body;
        public TextView content;

        public StringListItemViewHolder(View item_view){
            super(item_view);
            item_body = (LinearLayout) item_view;
            content = item_view.findViewById(R.id.popup_item_content);
        }
    }

    public interface OnItemClickListener {
        void onItemticClick(View view, String content);
        //void onItemLongClick(View view);
    }
}

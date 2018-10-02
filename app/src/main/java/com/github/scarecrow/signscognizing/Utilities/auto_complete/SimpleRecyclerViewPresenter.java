package com.github.scarecrow.signscognizing.Utilities.auto_complete;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.scarecrow.signscognizing.adapters.PopupItemRecyclerViewAdapter;
import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.util.List;



public class SimpleRecyclerViewPresenter extends RecyclerViewPresenter<String> {

    private static final String TAG = "RecyclerViewPresenter";
    private PopupItemRecyclerViewAdapter instance;

    private List<String> complete_res;

    public SimpleRecyclerViewPresenter(Context context, List<String> res) {
        super(context);
        complete_res = res;

    }

    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        instance = new PopupItemRecyclerViewAdapter();
        instance.setItemClickListener(new PopupItemRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemticClick(View view, String content) {
                dispatchClick(content);
            }
        });
        Log.d(TAG, "instantiateAdapter: get instance");
        return instance;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        if (query != null) {
            String queryContent = query.toString();
            Log.d(TAG, "onQuery: " + queryContent);
        }

        instance.setItemList(complete_res);
    }
}

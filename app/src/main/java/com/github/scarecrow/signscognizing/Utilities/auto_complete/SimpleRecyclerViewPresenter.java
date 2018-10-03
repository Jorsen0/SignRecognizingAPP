package com.github.scarecrow.signscognizing.Utilities.auto_complete;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.scarecrow.signscognizing.adapters.PopupItemRecyclerViewAdapter;
import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.util.Arrays;
import java.util.List;



public class SimpleRecyclerViewPresenter extends RecyclerViewPresenter<String> {

    private static final String TAG = "RecyclerViewPresenter";
    private PopupItemRecyclerViewAdapter instance;

    private List<String> complete_res;

    public SimpleRecyclerViewPresenter(Context context) {
        super(context);
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
        Log.d(TAG, "instantiateAdapter: get instance " + instance.getItems());
        System.out.println("instantiateAdapter: get instance " + instance.getItems());
        return instance;
    }

    public void setComleteRes(List<String> res) {
        complete_res = res;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        if (query != null) {
            String queryContent = query.toString();
            Log.d(TAG, "onQuery: " + queryContent);
        }

        System.out.println("onQuery: executed");
        if (complete_res != null) {
//            instance.setItemList(complete_res);
            List<String> test = Arrays.asList("demo", "demo2", "demo3");
            instance.setItemList(test);
        }
    }
}

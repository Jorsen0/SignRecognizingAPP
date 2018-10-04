package com.github.scarecrow.signscognizing.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.scarecrow.signscognizing.R;
import com.github.scarecrow.signscognizing.activities.MainActivity;

/**
 * Created by Scarecrow on 2018/2/5.
 *
 */

public class StartControlPanelFragment extends Fragment {

    // 保存一个parent activity ，在需要操作parent上的组件时可以通过activity处理
    // activity 上的view组件也可以通过其进行访问


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_start_control, container,
                                    false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        View fragment_view = getView();
        final MainActivity parent_activity = (MainActivity) getActivity();

        assert fragment_view != null;
        assert parent_activity != null;

        LinearLayout bt = fragment_view.findViewById(R.id.button_start_conversation);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent_activity.switchFragment(MainActivity.FRAGMENT_ARMBANDS_SELECT);
                parent_activity.switchFragment(MainActivity.FRAGMENT_SPLIT_BOARD);
            }
        });

        bt = fragment_view.findViewById(R.id.setting_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_activity.switchFragment(MainActivity.FRAGMENT_SETTING);
            }
        });



    }
}

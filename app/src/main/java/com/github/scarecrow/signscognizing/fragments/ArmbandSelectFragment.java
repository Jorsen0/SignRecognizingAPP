package com.github.scarecrow.signscognizing.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.scarecrow.signscognizing.R;
import com.github.scarecrow.signscognizing.Utilities.Armband;
import com.github.scarecrow.signscognizing.Utilities.ArmbandManager;
import com.github.scarecrow.signscognizing.Utilities.MessageManager;
import com.github.scarecrow.signscognizing.Utilities.SocketConnectionManager;
import com.github.scarecrow.signscognizing.activities.MainActivity;
import com.github.scarecrow.signscognizing.adapters.ArmbandListRecyclerViewAdapter;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Scarecrow on 2018/2/5.
 *
 */

public class ArmbandSelectFragment extends Fragment {
    final ArmbandListRecyclerViewAdapter adapter = new ArmbandListRecyclerViewAdapter();
    private View fragment_view ;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.init_state();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_armband_select, container,
                false);

        fragment_view = view;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        final RecyclerView armbands_rv = fragment_view.findViewById(R.id.armbands_list_rv);
        armbands_rv.setLayoutManager(linearLayoutManager);


        ArmbandManager.getArmbandsManger()
                .setOnUpdateCompleteCallback(new ArmbandManager.OnUpdateComplete() {
                    @Override
                    public void noticeUpdateComplete(List<Armband> armbandList) {
                        adapter.setArmbandList(armbandList);
                    }
                });
        ArmbandManager.getArmbandsManger().updateArmbandsList();

        boolean pair_mode = ArmbandManager.getArmbandsManger().getArmbandPairMode();
//         单手模式时 点击列表中item即可开始匹配
        if (!pair_mode) {
            adapter.setOnListItemClickListenner(new ListItemClickListener() {
                @Override
                public void onListItemClick(Armband item) {
                    ArmbandManager.getArmbandsManger()
                            .setCurrentConnectedArmband(item);
                    startArmbandPair();
                }
            });
        } else {
//        双手模式时设置确定所选手环按钮的监听器
            Button bt = fragment_view.findViewById(R.id.button_armband_select_confirm);
            bt.setVisibility(View.VISIBLE);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Armband[] selected_armband = adapter.getSelectedArmband();
                    if (selected_armband == null) {
                        Toast.makeText(getContext(), "请选择两个手环", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    ArmbandManager.getArmbandsManger()
                            .setCurrentConnectedArmband(selected_armband[0], selected_armband[1]);
                    startArmbandPair();
                }
            });
        }
        armbands_rv.setAdapter(adapter);


        final MainActivity parent_activity = (MainActivity) getActivity();
        LinearLayout bt = fragment_view.findViewById(R.id.button_armband_select_refresh);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArmbandManager.getArmbandsManger().updateArmbandsList();
            }
        });

        bt = fragment_view.findViewById(R.id.button_armband_select_return);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent_activity.switchFragment(MainActivity.FRAGMENT_START_CONTROL);
                parent_activity.switchFragment(MainActivity.FRAGMENT_INFO_DISPLAY);
            }
        });
    }

    private void startArmbandPair() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("连接中");
        progressDialog.show();

        SocketConnectionManager.getInstance()
                .startConnection(new SocketConnectionManager.TaskCompleteCallback() {
                    @Override
                    public void onConnectSucceeded() {
                        progressDialog.cancel();
                        Toast.makeText(getContext(), " 连接成功", Toast.LENGTH_SHORT)
                                .show();

                        ((MainActivity) getActivity())
                                .switchFragment(MainActivity.FRAGMENT_INPUT_CONTROL);
                        ((MainActivity) getActivity())
                                .switchFragment(MainActivity.FRAGMENT_CONVERSATION_DISPLAY);
                    }

                    @Override
                    public void onConnectFailed() {
                        progressDialog.cancel();
                        Toast.makeText(getContext(), " 连接失败", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onReceivedMessage(String message) {
                        Log.d(TAG, "onReceiveMessage: receive message from server: "
                                + message);
                        MessageManager.getInstance()
                                .processSignMessageFeedback(message);
                    }

                    @Override
                    public void onDisconnect() {
                        Toast.makeText(MainActivity.APP_CONTEXT, "与服务器断开连接", Toast.LENGTH_SHORT)
                                .show();
                        MessageManager.getInstance()
                                .stopSignRecognize();
                    }
                });
    }

    public interface ListItemClickListener {
        void onListItemClick(Armband item);
    }

}

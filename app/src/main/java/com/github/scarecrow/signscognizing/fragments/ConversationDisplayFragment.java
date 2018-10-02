package com.github.scarecrow.signscognizing.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.scarecrow.signscognizing.R;
import com.github.scarecrow.signscognizing.Utilities.MessageManager;
import com.github.scarecrow.signscognizing.adapters.ConversationMessagesRVAdapter;

/**
 * Created by Scarecrow on 2018/2/5.
 *
 */

public class ConversationDisplayFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MessageManager.getInstance()
                .cleanMessageList();
        return inflater.inflate(R.layout.fragment_coversation_display, container,
                false);
    }


    private ConversationMessagesRVAdapter conversation_rv_adapter;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        final RecyclerView recyclerView = view.findViewById(R.id.conversation_display_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        conversation_rv_adapter = new ConversationMessagesRVAdapter(getContext());
        recyclerView.setAdapter(conversation_rv_adapter);

        MessageManager.getInstance().addNewNoticeTarget(new MessageManager.NoticeMessageChanged() {
            @Override
            public void onNewMessageAdd() {
                conversation_rv_adapter.updateMessageList();
                conversation_rv_adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(conversation_rv_adapter.getItemCount() - 1);
            }

            @Override
            public void onMessageContentChange() {
                conversation_rv_adapter.updateMessageList();
                conversation_rv_adapter.notifyDataSetChanged();
            }

            @Override
            public void onSignCaptureEnd() {
                conversation_rv_adapter.notifyDataSetChanged();
            }

            @Override
            public void onSignCaptureStart() {
                conversation_rv_adapter.notifyDataSetChanged();
            }

        });
        //发送消息按钮
        ImageView bt = view.findViewById(R.id.button_text_send);
        final EditText editText = view.findViewById(R.id.conversation_text_input);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                MessageManager.getInstance().buildTextMessage(text);
                editText.setText("");
            }
        });

//        MessageManager.getInstance().sampleDisplayCreate();



    }

    //    在fragment不可见的时候 清除handler里面的东西 避免fragment内存泄漏
    @Override
    public void onStop() {
        conversation_rv_adapter.onDestroy();
        super.onStop();
    }

}

package com.github.scarecrow.signscognizing.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.scarecrow.signscognizing.R;
import com.github.scarecrow.signscognizing.Utilities.MessageManager;
import com.github.scarecrow.signscognizing.Utilities.auto_complete.SentenceAutoCompleter;
import com.github.scarecrow.signscognizing.fragments.ArmbandSelectFragment;
import com.github.scarecrow.signscognizing.fragments.ConversationDisplayFragment;
import com.github.scarecrow.signscognizing.fragments.InfoDisplayFragment;
import com.github.scarecrow.signscognizing.fragments.InputControlPanelFragment;
import com.github.scarecrow.signscognizing.fragments.SettingFragment;
import com.github.scarecrow.signscognizing.fragments.ShowSplitBoardFragment;
import com.github.scarecrow.signscognizing.fragments.StartControlPanelFragment;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

public class MainActivity extends AppCompatActivity {
    // 各个fragment的代号 在切换fragment的时候可以由外部调用。
    public static final int FRAGMENT_START_CONTROL = 289,
                            FRAGMENT_INPUT_CONTROL = 25,
                            FRAGMENT_ARMBANDS_SELECT = 46,
                            FRAGMENT_CONVERSATION_DISPLAY = -289,
                            FRAGMENT_INFO_DISPLAY = -29,
                            FRAGMENT_SETTING = 30,
                            FRAGMENT_SPLIT_BOARD = -33;

    public static Context APP_CONTEXT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APP_CONTEXT = getApplicationContext();
        switchFragment(FRAGMENT_START_CONTROL);
        switchFragment(FRAGMENT_INFO_DISPLAY);

        SpeechUtility.createUtility(getApplicationContext(), "appid=5a883f0c");
        SpeechRecognizer speechRecognizer
                = SpeechRecognizer.createRecognizer(getApplicationContext(), new InitListener() {
            @Override
            public void onInit(int code) {
                Log.d("", "SpeechRecognizer init() code = " + code);
            }
        });
        Log.d("", "onCreate: " + speechRecognizer);
        SentenceAutoCompleter.getInstance();
        MessageManager.getInstance().initTTS(getApplicationContext());

    }

    @Override
    public void onDestroy() {
        MessageManager.getInstance().releaseTTS();
        super.onDestroy();
    }


    /**
     * 主activity的fragment切换工作都托管到这个方法里
     * 通过传入一个fragment code 完成fragment的切换
     * 每个fragment都是在指定的位置 所以只需要指定目标fragment就ok
     * @param fragment_code 目标fragment的code
     */
    public void switchFragment(int fragment_code) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;
        int target_container;
        boolean isContainNull ;

        // 先通过code判断目标fragment应该在的容器位置
        // 大于零的是在control位置 小于零是在info位置

        if(fragment_code > 0){
            target_container = R.id.control_panel_fragment_container;
        }else
            target_container = R.id.info_display_fragment_container;
        fragment = fm.findFragmentById(target_container);
        //判断这个位置的fragment是否为空 从而决定是add还是切换
        isContainNull = fragment == null;

        // assign 目标fragment
        switch (fragment_code){
            case FRAGMENT_ARMBANDS_SELECT:
                fragment = new ArmbandSelectFragment();
                break;
            case FRAGMENT_CONVERSATION_DISPLAY:
                fragment = new ConversationDisplayFragment();
                break;
            case FRAGMENT_INFO_DISPLAY:
                fragment = new InfoDisplayFragment();
                break;
            case FRAGMENT_INPUT_CONTROL:
                fragment = new InputControlPanelFragment();
                break;
            case FRAGMENT_START_CONTROL:
                fragment = new StartControlPanelFragment();
                break;
            case FRAGMENT_SETTING:
                fragment = new SettingFragment();
                break;
            case FRAGMENT_SPLIT_BOARD:
                fragment = new ShowSplitBoardFragment();
                break ;
            default:
                fragment = new InfoDisplayFragment();
                break;

        }

        if (isContainNull) {
            fm.beginTransaction()
                    .add(target_container, fragment)
                    .commit();
        }else {
            fm.beginTransaction()
                    .replace(target_container, fragment)
                    .commit();
        }

    }
}

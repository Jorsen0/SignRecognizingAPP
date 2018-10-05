package com.github.scarecrow.signscognizing.Utilities.auto_complete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * 手语句子补全
 * 单例模式 使用 getInstance获取对象 然后调用相关接口进行补全
 */
public class SentenceAutoCompleter {


    private static SentenceAutoCompleter instance = new SentenceAutoCompleter();
    private SubsequenceSearchTree tree;

    private SentenceAutoCompleter() {
        //load the dict to the tree
        tree = new SubsequenceSearchTree();
        String SENTENCE_TABLE = "[{\"value\": \"where is the toilet?\", \"keys\": [\"Ask\", \"Toilet\", \"How\", \"Go\"]}, {\"value\": \"Where is the terminal building?\", \"keys\": [\"Ask\", \"Terminal building\", \"Where\"]}, {\"value\": \"Where is the ticket counter?\", \"keys\": [\"Ask\", \"Ticket Counter\", \"Where\"]}, {\"value\": \"Where is the ticket entrance?\", \"keys\": [\"Ask\", \"Ticket Entrance\", \"Where\"]}, {\"value\": \"Where is the security check?\", \"keys\": [\"Ask\", \"Where\", \"Security Check\"]}, {\"value\": \"How can I get the security check?\", \"keys\": [\"Ask\", \"How\", \"Security Check\"]}, {\"value\": \"Where is the boarding gate?\", \"keys\": [\"Ask\", \"Abrading Entrance\", \"Where\"]}, {\"value\": \"What should I do if the ticket cannot be found?\", \"keys\": [\"Ask\", \"Ticket\", \"Find\", \"Not\", \"How\", \"Do\"]}, {\"value\": \"What should I do if I miss my flight?\", \"keys\": [\"Ask\", \"Missed\", \"Mine\", \"Flight\", \"How\", \"Do\"]}, {\"value\": \"What should I do if the flight is delayed?\", \"keys\": [\"Ask\", \"Flight\", \"Delay\", \"How\", \"Do\"]}, {\"value\": \"How long is the flight delayed?\", \"keys\": [\"Ask\", \"Flight\", \"Delay\", \"How long\"]}, {\"value\": \"Where can I check my baggage?\", \"keys\": [\"Ask\", \"Where\", \"Consignment\", \"baggage\"]}, {\"value\": \"How can I get my baggage checked?\", \"keys\": [\"Ask\", \"baggage\", \"Consignment\", \"How\", \"Do\"]}, {\"value\": \"What should I do if my checked baggage is overweight?\", \"keys\": [\"Ask\", \"How\", \"Do\", \"Consignment\", \"Baggage\", \"Over\", \"Weight\", \"\"]}, {\"value\": \"What items in baggage can't been checked?\", \"keys\": [\"Ask\", \"What\", \"Can\", \"Not\", \"Consignment\"]}, {\"value\": \"What material do I need for check-in?\", \"keys\": [\"Ask\", \"Get\", \"Ticket\", \"What\"]}, {\"value\": \"What documents do I need for boarding?\", \"keys\": [\"Ask\", \"Aboard\", \"What\", \"ID card\"]}, {\"value\": \"What should I do if my ID card cannot be found?\", \"keys\": [\"Ask\", \"Id Card\", \"Find\", \"How\", \"Do\"]}, {\"value\": \"How can I change my flight?\", \"keys\": [\"Ask\", \"Flight\", \"How\", \"Change sign\"]}, {\"value\": \"How can I refund my ticket?\", \"keys\": [\"Ask\", \"How\", \"Back\", \"Ticket\"]}, {\"value\": \"Why did you confiscate my lighter?\", \"keys\": [\"Ask\", \"Why\", \"Confiscate\", \"Mine\", \"Lighter\"]}, {\"value\": \"Why did you confiscate my perfume?\", \"keys\": [\"Ask\", \"Why\", \"Confiscate\", \"Mine\", \"Perfume\"]}, {\"value\": \"Why did you confiscate my water drink?\", \"keys\": [\"Ask\", \"Why\", \"Confiscate\", \"Mine\", \"Drinks\"]}, {\"value\": \"Why is the portable charger can not been checked?\", \"keys\": [\"Ask\", \"Why\", \"Confiscate\", \"Portable\", \"Can Not\", \"Consignment\"]}, {\"value\": \"Why can't cosmetics be taken on board?\", \"keys\": [\"Ask\", \"cosmetics\", \"Why\", \"Can\", \"Take\", \"Plane\"]}, {\"value\": \"What is the limit for checked baggage weight?\", \"keys\": [\"Ask\", \"Consignment\", \"baggage\", \"Weight\", \"Limit\", \"What\"]}, {\"value\": \"Why is the boarding gate changed?\", \"keys\": [\"Ask\", \"Abrading Entrance\", \"Why\", \"Change\"]}, {\"value\": \"How can I choose my sate on the plane?\", \"keys\": [\"Ask\", \"Plane\", \"How\", \"Choose\", \"Seat\"]}, {\"value\": \"Where should I go after I passed the boarding gate?\", \"keys\": [\"Ask\", \"Pass\", \"Abrading Entrance\", \"After\", \"How\", \"Go\"]}]";
        JSONArray jsonArray;
        try {
//            Log.d(TAG, "SentenceAutoCompleter: " + SENTENCE_TABLE);
            jsonArray = new JSONArray(SENTENCE_TABLE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject e = jsonArray.getJSONObject(i);
                String v = e.getString("value");
                JSONArray keys = e.getJSONArray("keys");
                List<String> key_list = new LinkedList<>();
                for (int j = 0; j < keys.length(); j++) {
                    key_list.add(keys.getString(j));
                }
                tree.addSeqence(key_list, v);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static SentenceAutoCompleter getInstance() {
        return instance;
    }

    /**
     * 执行query
     * 传入key sequence(当前识别出来的手语词)，经过匹配后返回满足条件的补全后的句子
     *
     * @param keys  待补全的序列
     * @param exact 是否保证完全匹配识别句子
     * @return 匹配补全后的句子，如果不设置为exact为true则会返回按照匹配程度自大到小排列的补全结果
     */
    public List<String> executeValueQuery(List<String> keys, boolean exact) {
        return tree.querySequenceValue(keys, exact);
    }

    @Override
    public String toString() {
        return "Subsequence Tree:\n" + tree.toString();
    }
}

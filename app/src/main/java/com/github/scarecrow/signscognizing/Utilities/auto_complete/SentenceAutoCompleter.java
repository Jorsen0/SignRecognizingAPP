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
        String SENTENCE_TABLE = "[{\"value\": \"where is the toilet?\", \"keys\": [\"Ask\", \"Toilet\", \"How\", \"Go\"]}, {\"value\": \"Where is the terminal building?\", \"keys\": [\"Ask\", \"Terminal building\", \"Where\"]}, {\"value\": \"Where is the ticket counter?\", \"keys\": [\"Ask\", \"Ticket Counter\", \"Where\"]}, {\"value\": \"Where is the ticket entrance?\", \"keys\": [\"Ask\", \"Ticket Entrance\", \"Where\"]}, {\"value\": \"Where is the security check?\", \"keys\": [\"Ask\", \"Where\", \"Security Check\"]}, {\"value\": \"How can I get the security check?\", \"keys\": [\"Ask\", \"How\", \"Security Check\"]}, {\"value\": \"Where is the boarding gate?\", \"keys\": [\"Ask\", \"Abrading Entrance\", \"Where\"]}, {\"value\": \"What should I do if the ticket cannot be found?\", \"keys\": [\"Ask\", \"Ticket\", \"Find\", \"Not\", \"How\", \"Do\"]}, {\"value\": \"What should I do if I miss my flight?\", \"keys\": [\"Ask\", \"Missed\", \"Mine\", \"Flight\", \"How\", \"Do\"]}, {\"value\": \"What should I do if the flight is delayed?\", \"keys\": [\"Ask\", \"Flight\", \"Delay\", \"How\", \"Do\"]}, {\"value\": \"How long is the flight delayed?\", \"keys\": [\"Ask\", \"Flight\", \"Delay\", \"How long\"]}, {\"value\": \"Where can I get my baggage check-in?\", \"keys\": [\"Ask\", \"Where\", \"Consignment\", \"baggage\"]}, {\"value\": \"How can I get my baggage checked?\", \"keys\": [\"Ask\", \"How\", \"Do\", \"baggage\", \"Consignment\"]}, {\"value\": \"What should I do if my checked baggage is overweight?\", \"keys\": [\"Ask\", \"How\", \"Do\", \"Consignment\", \"Baggage\", \"Over\", \"Weight\"]}, {\"value\": \"What items in baggage can't been checked?\", \"keys\": [\"Ask\", \"What\", \"Can\", \"Not\", \"Consignment\"]}, {\"value\": \"What material do I need for check-in?\", \"keys\": [\"Ask\", \"Get\", \"Ticket\", \"What\"]}, {\"value\": \"What documents do I need for boarding?\", \"keys\": [\"Ask\", \"Aboard\", \"What\", \"ID card\"]}, {\"value\": \"What should I do if my ID card cannot be found?\", \"keys\": [\"Ask\", \"Id Card\", \"Find\", \"How\", \"Do\"]}, {\"value\": \"How can I change my flight?\", \"keys\": [\"Ask\", \"Flight\", \"How\", \"Change sign\"]}, {\"value\": \"How can I refund my ticket?\", \"keys\": [\"Ask\", \"How\", \"Back\", \"Ticket\"]}, {\"value\": \"Why did you confiscate my lighter?\", \"keys\": [\"Ask\", \"Why\", \"Confiscate\", \"Mine\", \"Lighter\"]}, {\"value\": \"Why did you confiscate my perfume?\", \"keys\": [\"Ask\", \"Why\", \"Confiscate\", \"Mine\", \"Perfume\"]}, {\"value\": \"Why did you confiscate my water drink?\", \"keys\": [\"Ask\", \"Why\", \"Confiscate\", \"Mine\", \"Drinks\"]}, {\"value\": \"Why is the portable charger can not been checked?\", \"keys\": [\"Ask\", \"Why\", \"Confiscate\", \"Portable\", \"Can Not\", \"Consignment\"]}, {\"value\": \"Why can't cosmetics be taken on board?\", \"keys\": [\"Ask\", \"cosmetics\", \"Why\", \"Can\", \"Take\", \"Plane\"]}, {\"value\": \"What is the limit for checked baggage weight?\", \"keys\": [\"Ask\", \"Consignment\", \"baggage\", \"Weight\", \"Limit\", \"What\"]}, {\"value\": \"Why is the boarding gate changed?\", \"keys\": [\"Ask\", \"Abrading Entrance\", \"Why\", \"Change\"]}, {\"value\": \"How can I choose my sate on the plane?\", \"keys\": [\"Ask\", \"Plane\", \"How\", \"Choose\", \"Seat\"]}, {\"value\": \"Where should I go after I passed the boarding gate?\", \"keys\": [\"Ask\", \"Pass\", \"Abrading Entrance\", \"After\", \"How\", \"Go\"]}, {\"value\": \"I want to reserve a tomorrow\\u2019s flight from Shenyang to Beijing\", \"keys\": [\"Ticket\", \"Tomorrow\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a today\\u2019s flight from Shenyang to Beijing\", \"keys\": [\"Ticket\", \"Today\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a tomorrow\\u2019s flight from Shenyang to Beijing\", \"keys\": [\"flight\", \"Tomorrow\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a today\\u2019s flight from Shenyang to Beijing\", \"keys\": [\"flight\", \"Today\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a today\\u2019s flight from Shenyang to Beijing flight in the A.M.\", \"keys\": [\"Ticket\", \"Today\", \"A.M\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a today\\u2019s flight from Shenyang to Beijing flight in the afternoon.\", \"keys\": [\"Ticket\", \"Today\", \"afternoon\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a today\\u2019s flight from Shenyang to Beijing flight in the morning.\", \"keys\": [\"Ticket\", \"Today\", \"morning\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a today\\u2019s flight from Shenyang to Beijing flight at night.\", \"keys\": [\"Ticket\", \"Today\", \"night\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a today\\u2019s flight from Shenyang to Beijing flight in the A.M.\", \"keys\": [\"Flight\", \"Today\", \"A.M\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a today\\u2019s flight from Shenyang to Beijing flight in the afternoon.\", \"keys\": [\"Flight\", \"Today\", \"afternoon\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a today\\u2019s flight from Shenyang to Beijing flight in the morning.\", \"keys\": [\"Flight\", \"Today\", \"morning\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"I want to reserve a today\\u2019s flight from Shenyang to Beijing flight at night.\", \"keys\": [\"Flight\", \"Today\", \"night\", \"Go\", \"Shenyang\", \"Beijing\"]}, {\"value\": \"What should I do if I want to buy a ticket back to Shenyang?\", \"keys\": [\"Ask\", \"Ticket\", \"Back\", \"Shenyang\"]}, {\"value\": \"What should I do if I want to buy a ticket back to Liaoning?\", \"keys\": [\"Ask\", \"Ticket\", \"Back\", \"Liaoning\"]}, {\"value\": \"What should I do if I want to buy a ticket back to Beijing?\", \"keys\": [\"Ask\", \"Ticket\", \"Back\", \"Beijing\"]}, {\"value\": \"What should I do if I want to buy a ticket go to Shenyang?\", \"keys\": [\"Ask\", \"Ticket\", \"Go\", \"Shenyang\"]}, {\"value\": \"What should I do if I want to buy a ticket go to Liaoning?\", \"keys\": [\"Ask\", \"Ticket\", \"Go\", \"Liaoning\"]}, {\"value\": \"What should I do if I want to buy a ticket go to Beijing?\", \"keys\": [\"Ask\", \"Ticket\", \"Go\", \"Beijing\"]}, {\"value\": \"What should I do if I want to get my luggage check-in?\", \"keys\": [\"Ask\", \"baggage\", \"consignment\"]}, {\"value\": \"Where can I charge my phone?\", \"keys\": [\"Ask\", \"Where\", \"charging\"]}, {\"value\": \"What should I do if I can't find my friend?\", \"keys\": [\"Ask\", \"find\", \"less\", \"friend\"]}, {\"value\": \"Sorry to bother.\", \"keys\": [\"Sorry\"]}, {\"value\": \"Sorry, I am deaf people and I need your help.\", \"keys\": [\"Sorry\", \"deaf people\", \"help\"]}, {\"value\": \"It doesn't matter, you can ask the staff.\", \"keys\": [\"Doesn't matter\", \"Ask\"]}, {\"value\": \"It doesn't matter if you can communicate with me with \\\"Say Your Voice\\\"\", \"keys\": [\"Doesn't matter\", \"communication\", \"I\"]}, {\"value\": \"What should I do if I am late for my flight?\", \"keys\": [\"Ask\", \"late\", \"flight\"]}, {\"value\": \"What should I do if I miss the departure time?\", \"keys\": [\"Ask\", \"missed\", \"flight\"]}, {\"value\": \"My home is in Shenyang, Liaoning.\", \"keys\": [\"Home\", \"Shenyang\", \"Liaoning\"]}, {\"value\": \"When is the departure time of flight has been changed to?\", \"keys\": [\"Ask\", \"flight\", \"time\"]}]";
        JSONArray jsonArray;
        try {
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

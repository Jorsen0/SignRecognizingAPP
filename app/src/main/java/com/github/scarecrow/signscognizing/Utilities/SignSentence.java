package com.github.scarecrow.signscognizing.Utilities;


import java.util.LinkedList;
import java.util.List;


public class SignSentence {

    private List<String> word_seq;
    private String sentence;

    public SignSentence(){
        word_seq = new LinkedList<>();
    }

    public void appendWord(String w){
        word_seq.add(w);
    }

    public void clearWords(){
        word_seq.clear();
    }


    public List<String> getWordSeq() {
        return word_seq;
    }

    public void setSentence(String s) {
        sentence = s;
    }

    public String getSentenceStr(){
        if (sentence == null) {
            StringBuilder ret = new StringBuilder();
            ret.append("");
            for (String word : word_seq) {
                ret.append(word).append(" ");
            }
            return ret.toString();
        } else {
            return sentence;
        }


    }
}

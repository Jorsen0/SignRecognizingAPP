package com.github.scarecrow.signscognizing;

import com.github.scarecrow.signscognizing.Utilities.auto_complete.SentenceAutoCompleter;
import com.github.scarecrow.signscognizing.Utilities.auto_complete.SubsequenceSearchTree;
import com.github.scarecrow.signscognizing.Utilities.auto_complete.TreeNode;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void searchTreeTest(){
        SubsequenceSearchTree tree = new SubsequenceSearchTree();
        String[] test1 = new String[] {"aaa", "bbb", "ccc", "ddd"};
        String[] test2 = new String[] {"bbb", "ccc", "ddd"};
        String[] test3 = new String[] {"aaa", "bbb", "ccc",};
        String[] test4 = new String[] {"aaa", "bbb", "ddd", "eee"};
        tree.addSeqence(Arrays.asList(test1), "aaaaa");
        tree.addSeqence(Arrays.asList(test2), "bbbbb");
        tree.addSeqence(Arrays.asList(test3), "ccccc");
        tree.addSeqence(Arrays.asList(test4), "ddddd");
        System.out.println(tree.toString());

        List<TreeNode> nodes = tree.findMultipleNodes("ccc");
//        System.out.println( String.valueOf(tree.getAllAccessibleValue(nodes)));

        List<String> seq = Arrays.asList("aaa", "kkk", "ddd");
        System.out.println( String.valueOf(tree.querySequenceValue(seq, true)));
        System.out.println( String.valueOf(tree.querySequence(seq, false)));
    }


    @Test
    public void testAutoCompleter() {
        List<String> query1 = Arrays.asList("Ask");
        List<String> query2 = Arrays.asList("Ask", "Flight");
        List<String> query3 = Arrays.asList("Ask", "where");
        List<String> query4 = Arrays.asList("baggage");
        System.out.println(SentenceAutoCompleter.getInstance().executeValueQuery(query1, true));
        System.out.println(SentenceAutoCompleter.getInstance().executeValueQuery(query2, false));
        System.out.println(SentenceAutoCompleter.getInstance().executeValueQuery(query3, true));
        System.out.println(SentenceAutoCompleter.getInstance().executeValueQuery(query4, true));
    }

}
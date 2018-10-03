package com.github.scarecrow.signscognizing.Utilities.auto_complete;

import android.text.Editable;
import android.util.Log;

import com.github.scarecrow.signscognizing.Utilities.SignMessage;
import com.github.scarecrow.signscognizing.adapters.ConversationMessagesRVAdapter;
import com.otaliastudios.autocomplete.AutocompleteCallback;

/**
 * Created by boyzhang on 2018/10/1.
 */

public class SimpleAutocompleteCallback implements AutocompleteCallback<String> {

    private static final String TAG = "AutocompleteCallback";

    // for calling method when notice the adapter notify data set changed
    private ConversationMessagesRVAdapter adapter;

    private SignMessage message;

    public SimpleAutocompleteCallback(ConversationMessagesRVAdapter adapter) {
        this.adapter = adapter;
    }

    //when you can get the message set it in.
    public void setMessageObj(SignMessage m) {
        message = m;
    }

    /**
     * Called when an item inside your list is clicked.
     * This works if your presenter has dispatched a click event.
     * At this point you can edit the text, e.g. {@code editable.append(item.toString())}.
     *
     * @param editable editable text that you can work on
     * @param item item that was clicked
     * @return true if the action is valid and the popup can be dismissed
     */
    public boolean onPopupItemClicked(Editable editable, String item) {
        String content = editable.toString();

        if (message != null) {
            Log.d(TAG, "onPopupItemClicked: set the complete result in");
            System.out.println("onPopupItemClicked: set the complete result in");
            message.setCompleteResult(item);
            adapter.notifyDataSetChanged();
        }

        Log.e(TAG, ": change " + content + " to " + item);
        System.out.println("onPopupItemClicked: change " + content + " to " + item);
        return true;
    }

    /**
     * Called when popup visibility state changes.
     *
     * @param shown true if the popup was just shown, false if it was just hidden
     */
    public void onPopupVisibilityChanged(boolean shown) {

    }
}

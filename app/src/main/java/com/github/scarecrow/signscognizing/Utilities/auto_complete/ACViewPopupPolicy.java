package com.github.scarecrow.signscognizing.Utilities.auto_complete;

import android.text.Spannable;
import android.util.Log;

import com.otaliastudios.autocomplete.AutocompletePolicy;


/**
 * This class controls when to show or hide the popup window, and, in the first case,
 * what text should be passed to the popup.
 *
 */
public class ACViewPopupPolicy implements AutocompletePolicy {

    //popup_show_status标识是否显示popup
    private boolean popup_show_status = false;

    private String content = "";

    private static final String TAG = "ACViewPopupPolicy";


    /**
     * Called to understand whether the popup should be shown. Some naive examples:
     * - Show when there's text: {@code return text.length() > 0}
     * - Show when last char is @: {@code return text.getCharAt(text.length()-1) == '@'}
     *
     * @param text current text, along with its Spans
     * @param cursorPos the position of the cursor
     * @return true if popup should be shown
     */
    public boolean shouldShowPopup(Spannable text, int cursorPos) {
        content = text.toString();
        popup_show_status = content.length() > 1;
        Log.d(TAG, "shouldShowPopup: content  " + content + ", cursorPos " + cursorPos);
        return popup_show_status;

    }

    /**
     * Called to understand whether a currently shown popup should be closed, maybe
     * because text is invalid. A reasonable implementation is
     * {@code return !shouldShowPopup(text, cursorPos)}.
     *
     * However this is defined so you can add or clear spans.
     *
     * @param text current text, along with its Spans
     * @param cursorPos the position of the cursor
     * @return true if popup should be hidden
     */
    public boolean shouldDismissPopup(Spannable text, int cursorPos) {
        Log.d(TAG, "shouldDismissPopup: content: " + text + ", cursorPos" + cursorPos);
        return !popup_show_status;
    }

    /**
     * Called to understand which query should be passed to AutocompletePresenter
     * for a showing popup. If this is called, {@link #shouldShowPopup(Spannable, int)} just returned
     * true, or {@link #shouldDismissPopup(Spannable, int)} just returned false.
     *
     * This is useful to understand which part of the text should be passed to presenters.
     * For example, user might have typed '@john' to select a username, but you just want to
     * search for 'john'.
     *
     * For more complex cases, you can add inclusive Spans in {@link #shouldShowPopup(Spannable, int)},
     * and get the span position here.
     *
     * @param text current text, along with its Spans
     * @return the query for presenter
     */
    public CharSequence getQuery(Spannable text) {
        content = text.toString();
        Log.d(TAG, "getQuery: the text is " + content);
        return content;
    }

    /**
     * Called when popup is dismissed. This can be used, for instance, to clear custom Spans
     * from the text.
     *
     * @param text text at the moment of dismissing
     */
    public void onDismiss(Spannable text) {
        Log.d(TAG, "onDismiss: popup view dismiss");

    }

}

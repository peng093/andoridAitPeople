package com.example.atpeople.myapplication.atPeople;

import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import com.example.atpeople.myapplication.atPeople.model.FormatRange;
import com.example.atpeople.myapplication.customview.AitEditText;

import java.util.List;

/**
 * Create by peng on 2020/11/26
 */
public class MentionInputConnection extends InputConnectionWrapper {
    private final AitEditText mEditText;
    private final List<FormatRange> mRangeManager;

    public MentionInputConnection(InputConnection target, boolean mutable, AitEditText editText) {
        super(target, mutable);
        this.mEditText = editText;
        this.mRangeManager = editText.mRangeManager;
    }

    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            if (null != mRangeManager) {
                int selectionStart = mEditText.getSelectionStart();
                int selectionEnd = mEditText.getSelectionEnd();
                Range closestRange = mEditText.getRangeOfClosestMentionString(selectionStart, selectionEnd);
                if (closestRange == null) {
                    mEditText.setSelected(false);
                    return super.sendKeyEvent(event);
                }
                //if mention string has been selected or the cursor is at the beginning of mention string, just use default action(delete)
                if (mEditText.isSelected() || selectionStart == closestRange.getFrom()) {
                    mEditText.setSelected(false);
                    return super.sendKeyEvent(event);
                } else {
                    // select the mention string
                    mEditText.setSelected(true);
                    // 如果光标所在位置是文字块，直接删除文字块
                    mEditText.getText().delete(closestRange.getFrom(),closestRange.getTo());
                }
                return true;
            }
        }
        return super.sendKeyEvent(event);
    }

    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        if (beforeLength == 1 && afterLength == 0) {
            return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(
                    new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
        }
        return super.deleteSurroundingText(beforeLength, afterLength);
    }
}

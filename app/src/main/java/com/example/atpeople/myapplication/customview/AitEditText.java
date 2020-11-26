package com.example.atpeople.myapplication.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.example.atpeople.myapplication.atPeople.MentionInputConnection;
import com.example.atpeople.myapplication.atPeople.Range;
import com.example.atpeople.myapplication.atPeople.model.FormatRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by peng on 2020/11/25
 */
public class AitEditText extends android.support.v7.widget.AppCompatEditText {
    public List<FormatRange> mRangeManager;

    public AitEditText(Context context) {
        super(context);
        init();
    }

    public AitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AitEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        mRangeManager =new ArrayList<>();
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        Range nearbyRange = getRangeOfNearbyMentionString(selStart, selEnd);
        if (null != nearbyRange) {
            // 起始位置一样说明，没有选中任何字符
            if (selStart == selEnd) {
                // 如果光标是在文字块中 则保证光标没有插入在文字块即可
                int sss=nearbyRange.getAnchorPosition(selStart);
                setSelection(sss);
            } else {
                if (selEnd < nearbyRange.getTo()) {
                    setSelection(selStart, nearbyRange.getTo());
                }
                if (selStart > nearbyRange.getFrom()) {
                    setSelection(nearbyRange.getFrom(), selEnd);
                }
            }
        }
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MentionInputConnection(super.onCreateInputConnection(outAttrs), true, this);
    }

    public Range getRangeOfNearbyMentionString(int selStart, int selEnd) {
        if (mRangeManager == null) {
            return null;
        }
        for (Range range : mRangeManager) {
            if (range.isWrappedBy(selStart, selEnd)) {
                return range;
            }
        }
        return null;
    }

    public Range getRangeOfClosestMentionString(int selStart, int selEnd) {
        if (mRangeManager == null) {
            return null;
        }
        for (Range range : mRangeManager) {
            if (range.contains(selStart, selEnd)) {
                return range;
            }
        }
        return null;
    }
}

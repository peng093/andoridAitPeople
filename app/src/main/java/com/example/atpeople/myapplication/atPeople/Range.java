package com.example.atpeople.myapplication.atPeople;

import android.support.annotation.NonNull;

public class Range implements Comparable{
    int id;
    String name;
    int from;
    int to;

    public Range(int id, String name, int from, int to) {
        this.id = id;
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public boolean isWrapped(int start, int end) {
        return from >= start && to <= end;
    }

    private boolean isWrappedBy(int start, int end) {
        return (start > from && start < to) || (end > from && end < to);
    }

    private boolean contains(int start, int end) {
        return from <= start && to >= end;
    }

    private boolean isEqual(int start, int end) {
        return (from == start && to == end) || (from == end && to == start);
    }

    private int getAnchorPosition(int value) {
        if ((value - from) - (to - value) >= 0) {
            return to;
        } else {
            return from;
        }
    }

    public void setOffset(int offset) {
        from += offset;
        to += offset;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return from - ((Range)o).from;
    }
}

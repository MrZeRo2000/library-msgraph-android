package com.romanpulov.library.msgraph;

public abstract class MSAbstractAction<T> {
    protected final int mAction;
    protected final OnMSActionListener<T> mMSActionListener;

    public MSAbstractAction(int mAction, OnMSActionListener<T> msActionListener) {
        this.mAction = mAction;
        this.mMSActionListener = msActionListener;
    }

    public abstract void execute();
}

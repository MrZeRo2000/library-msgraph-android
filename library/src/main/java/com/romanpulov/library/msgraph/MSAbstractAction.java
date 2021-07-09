package com.romanpulov.library.msgraph;

public abstract class MSAbstractAction<T> {
    protected final int mAction;

    protected OnMSActionListener<T> mMSActionListener;

    public OnMSActionListener<T> getMSActionListener() {
        return mMSActionListener;
    }

    public void setMSActionListener(OnMSActionListener<T> mMSActionListener) {
        this.mMSActionListener = mMSActionListener;
    }

    public MSAbstractAction(int mAction, OnMSActionListener<T> msActionListener) {
        this.mAction = mAction;
        this.mMSActionListener = msActionListener;
    }

    public abstract void execute();
}

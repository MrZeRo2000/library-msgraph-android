package com.romanpulov.library.msgraph;

import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class MSActionExecutor {
    private static final String TAG = MSActionExecutor.class.getSimpleName();

    public static <T> void execute(MSAbstractAction<T> action) {
        action.execute();
    }

    public static <T> T executeSync(MSAbstractAction<T> action) throws MSActionException {
        final CountDownLatch mLocker = new CountDownLatch(1);
        final AtomicReference<T> mData = new AtomicReference<>();
        final AtomicReference<MSActionException> mException = new AtomicReference<>();

        action.setMSActionListener(new OnMSActionListener<T>() {
            @Override
            public void onActionSuccess(int action, T data) {
                Log.d(TAG, "Action success");
                mData.set(data);
                mLocker.countDown();
            }

            @Override
            public void onActionFailure(int action, String errorMessage) {
                Log.d(TAG, "Action failure");
                mException.set(new MSActionException(errorMessage));
                mLocker.countDown();
            }
        });

        action.execute();

        try {
            mLocker.await();

            if (mException.get() != null) {
                throw mException.get();
            }

            return mData.get();

        } catch (InterruptedException e) {
            throw new MSActionException("Process interrupted");
        }
    }
}

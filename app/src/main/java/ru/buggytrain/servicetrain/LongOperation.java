package ru.buggytrain.servicetrain;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by Buggy on 03.09.2017.
 */

public class LongOperation {

    private static ExecutorService mExecutorService = Executors.newCachedThreadPool();

    public interface LongOperationCallback {
        boolean onProgress(long timeLeft);
        void onFinished(boolean isInterrupted);
    }

    static private void runLongOperation(final long durationMs, final LongOperationCallback callback) {
        if (durationMs <= 0) {
            return;
        }

        long timeLeft = durationMs;
        boolean isInterrupted = false;
        while (timeLeft > 0) {
            if (callback != null) {
                if (!callback.onProgress(timeLeft)) {
                    isInterrupted = true;
                    break;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            timeLeft -= 1000;
        }

        if (callback != null) {
            callback.onFinished(isInterrupted);
        }
    }

    public static void runSync(final long durationMs, final LongOperationCallback callback) {
        run(false, durationMs, callback);
    }

    public static void runAsync(final long durationMs, final LongOperationCallback callback) {
        run(true, durationMs, callback);
    }

    private static void run(boolean isAsync, final long durationMs, final LongOperationCallback callback) {
        FutureTask<Void> ft = new FutureTask<>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                runLongOperation(durationMs, callback);
                return null;
            }
        });

        mExecutorService.submit(ft);
        if (!isAsync) {
            try {
                ft.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

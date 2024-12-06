package ru.buggytrain.servicetrain.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import ru.buggytrain.servicetrain.LongOperation;
import ru.buggytrain.servicetrain.Utils;


public class MyStartedService extends Service {

    public static String PUBLISH_PROGRESS_ACTION = "MyStartedService.PUBLISH_PROGRESS_ACTION";

    public enum SupportedCommands {
        ACTION1, ACTION2
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Utils.LogE(MyStartedService.class.getSimpleName() + " onCreate");
    }

    @Override
    public void onDestroy() {
        Utils.LogE(MyStartedService.class.getSimpleName() + " onDestroy");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        Utils.LogE(MyStartedService.class.getSimpleName() + " onStartCommand");
        Utils.printStartCommandInfo(intent, flags, startId);

        if (intent != null) {
            String action = intent.getAction();
            final String commandName = action + "-" + startId;

            LongOperation.LongOperationCallback callback = new LongOperation.LongOperationCallback() {
                @Override
                public boolean onProgress(long timeLeft) {
                    Utils.LogE(commandName +  ", timeLeft: " + timeLeft);
                    publishProgress(commandName +  ", timeLeft: " + timeLeft);
                    return true;
                }

                @Override
                public void onFinished(boolean isInterrupted) {
                    publishProgress(commandName + " running is finished");
                    stopSelf(startId);
                }
            };

            if (SupportedCommands.ACTION1.toString().equals(action)) {
                LongOperation.runSync(3000, callback); // sync
            } else if (SupportedCommands.ACTION2.toString().equals(action)) {
                LongOperation.runAsync(5000, callback); // async
            } else {
                Utils.LogE("Unknown action: " + action);
                publishProgress("Unknown action: " + action);
                stopSelf(startId);
            }
        } else {
            publishProgress("intent is null");
        }

        return START_NOT_STICKY ;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Utils.LogE(MyStartedService.class.getSimpleName() + " onTaskRemoved");
    }

    private void publishProgress(String message) {
        Intent intent = new Intent(PUBLISH_PROGRESS_ACTION);
        intent.putExtra(Utils.MESSAGE_INTENT_EXTRA_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public static Intent getStartIntent(Context context, String action) {
        Intent intent = new Intent(context, MyStartedService.class);
        intent.setAction(action);
        return intent;
    }
}

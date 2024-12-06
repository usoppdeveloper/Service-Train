package ru.buggytrain.servicetrain.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import ru.buggytrain.servicetrain.LongOperation;
import ru.buggytrain.servicetrain.Utils;

/**
 * Created by Buggy on 03.09.2017.
 */

public class MyIntentService extends IntentService {

    public static String PUBLISH_PROGRESS_ACTION = "MyIntentService.PUBLISH_PROGRESS_ACTION";

    public enum SupportedCommands {
        ACTION1, ACTION2
    }

    public MyIntentService() {
       super(MyStartedService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.LogE(MyIntentService.class.getSimpleName() + " onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.LogE(MyIntentService.class.getSimpleName() + " onDestroy");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Utils.LogE(MyIntentService.class.getSimpleName() + " onStartCommand");
        Utils.printStartCommandInfo(intent, flags, startId);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Utils.LogE(MyIntentService.class.getSimpleName() + " onHandleIntent: " + intent);

        if (intent != null) {
            String action = intent.getAction();
            final String commandName = action;

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
                }
            };

            if (MyStartedService.SupportedCommands.ACTION1.toString().equals(action)) {
                LongOperation.runSync(5000, callback); // sync
            } else if (MyStartedService.SupportedCommands.ACTION2.toString().equals(action)) {
                LongOperation.runSync(5000, callback); // sync
            } else {
                Utils.LogE("Unknown action: " + action);
                publishProgress("Unknown action: " + action);
            }
        } else {
            publishProgress("intent is null");
        }
    }

    public static Intent getStartIntent(Context context, String action) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(action);
        return intent;
    }

    private void publishProgress(String message) {
        Intent intent = new Intent(PUBLISH_PROGRESS_ACTION);
        intent.putExtra(Utils.MESSAGE_INTENT_EXTRA_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}

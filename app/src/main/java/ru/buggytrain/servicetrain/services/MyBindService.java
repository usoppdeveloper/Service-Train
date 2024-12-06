package ru.buggytrain.servicetrain.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import ru.buggytrain.servicetrain.LongOperation;
import ru.buggytrain.servicetrain.Utils;

/**
 * Created by Buggy on 03.09.2017.
 */

public class MyBindService extends Service {

    public static String PUBLISH_PROGRESS_ACTION = "MyBindService.PUBLISH_PROGRESS_ACTION";
    private MyBinder mMyBinder = new MyBinder();

    public class MyBinder extends Binder {
        public void runLongOperation1() {
            LongOperation.runSync(5000, new LongOperation.LongOperationCallback() {
                String commandName = "bind-runLongOperation1";

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
            }); // sync
        }

        public void runLongOperation2() {
            LongOperation.runAsync(5000, new LongOperation.LongOperationCallback() {
                String commandName = "bind-runLongOperation2";

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
            }); // async
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMyBinder;
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MyBindService.class);
    }

    private void publishProgress(String message) {
        Intent intent = new Intent(PUBLISH_PROGRESS_ACTION);
        intent.putExtra(Utils.MESSAGE_INTENT_EXTRA_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}

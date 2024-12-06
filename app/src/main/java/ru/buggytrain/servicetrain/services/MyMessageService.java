package ru.buggytrain.servicetrain.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import ru.buggytrain.servicetrain.LongOperation;
import ru.buggytrain.servicetrain.Utils;

/**
 * Created by Buggy on 09.09.2017.
 */

public class MyMessageService extends Service {

    public static final String PUBLISH_PROGRESS_ACTION = "MyBindService.PUBLISH_PROGRESS_ACTION";

    // handler commands
    public static final int MSG_SAY_HELLO = 1;
    public static final int MSG_ACTION1 = 2;
    public static final int MSG_ACTION2 = 3;

    // Handler of incoming messages from clients.
    private class IncomingMessagesHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;

                case MSG_ACTION1:
                    LongOperation.runSync(5000, new LongOperation.LongOperationCallback() {
                        String commandName = "bind-runLongOperation1";

                        @Override
                        public boolean onProgress(long timeLeft) {
                            publishProgress(commandName +  ", timeLeft: " + timeLeft);
                            return true;
                        }

                        @Override
                        public void onFinished(boolean isInterrupted) {
                            publishProgress(commandName + " running is finished");
                        }
                    }); // sync
                    break;

                case MSG_ACTION2:
                    LongOperation.runAsync(5000, new LongOperation.LongOperationCallback() {
                        String commandName = "bind-runLongOperation1";

                        @Override
                        public boolean onProgress(long timeLeft) {
                            publishProgress(commandName +  ", timeLeft: " + timeLeft);
                            return true;
                        }

                        @Override
                        public void onFinished(boolean isInterrupted) {
                            publishProgress(commandName + " running is finished");
                        }
                    }); // async
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new IncomingMessagesHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Utils.LogE("MyMessageService onBind");
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MyMessageService.class);
    }

    private void publishProgress(String message) {
        Utils.LogE(message);
        Intent intent = new Intent(PUBLISH_PROGRESS_ACTION);
        intent.putExtra(Utils.MESSAGE_INTENT_EXTRA_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}

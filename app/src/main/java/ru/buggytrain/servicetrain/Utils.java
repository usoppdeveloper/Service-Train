package ru.buggytrain.servicetrain;

import android.content.Intent;
import android.util.Log;

import static android.app.Service.START_FLAG_REDELIVERY;
import static android.app.Service.START_FLAG_RETRY;

/**
 * Created by Buggy on 03.09.2017.
 */

public class Utils {
    public static String MESSAGE_INTENT_EXTRA_MESSAGE = "message";

    private static String TAG = "---" + Utils.class.getSimpleName();

    public static void LogE(String message) {
        Log.e(TAG, message);
    }

    public static void printStartCommandInfo(Intent intent, int flags, int startId) {
        Utils.LogE("* intent: " + intent);
        Utils.LogE("* startId: " + startId);

        String flagsStr = flags + ": ";
        if (flags == 0) flagsStr += 0;
        if ((flags & START_FLAG_RETRY) != 0) flagsStr += "START_FLAG_RETRY";
        else if ((flags & START_FLAG_REDELIVERY ) != 0) flagsStr += "START_FLAG_REDELIVERY ";
        Utils.LogE("* flags: " + flagsStr);
    }
}

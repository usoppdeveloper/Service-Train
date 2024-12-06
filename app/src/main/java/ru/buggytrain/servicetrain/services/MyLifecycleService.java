package ru.buggytrain.servicetrain.services;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import ru.buggytrain.servicetrain.LongOperation;
import ru.buggytrain.servicetrain.Utils;

public class MyLifecycleService extends LifecycleService {
    private MyLifecycleService.MyBinder mMyBinder = new MyLifecycleService.MyBinder();
    private MutableLiveData<String> progressLiveData = new MutableLiveData<>();

    public class MyBinder extends Binder {
        public Lifecycle getServiceLifecycle() {
            return getLifecycle();
        }

        public LiveData<String> getProgressLiveData() {
            return progressLiveData;
        }

        public void runLongOperation1() {
            LongOperation.runSync(5000, new LongOperation.LongOperationCallback() {
                String commandName = "lifecycle-bind-runLongOperation1";

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
                String commandName = "lifecycle-bind-runLongOperation2";

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
        super.onBind(intent);
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMyBinder;
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MyLifecycleService.class);
    }

    private void publishProgress(String message) {
        progressLiveData.postValue(message);
    }
}

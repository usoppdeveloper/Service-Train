package ru.buggytrain.servicetrain.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.widget.Toast;

import ru.buggytrain.servicetrain.LongOperation;
import ru.buggytrain.servicetrain.Utils;

public class MyJobService extends JobService {

    public final static int JOB_SERVICE_OPERATION_1 = 1;
    public final static int JOB_SERVICE_OPERATION_2 = 2;
    public final static int JOB_SERVICE_OPERATION_3 = 3;

    final Handler handler = new Handler();

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Utils.LogE("onStartJob " + jobParameters.getJobId());

        switch (jobParameters.getJobId()) {
            case JOB_SERVICE_OPERATION_1:
                publishProgress("JOB_SERVICE_OPERATION_1 is started");
                return false;

            case JOB_SERVICE_OPERATION_2:
                publishProgress("JOB_SERVICE_OPERATION_2 is started");
                return false;

            case JOB_SERVICE_OPERATION_3:
                publishProgress("JOB_SERVICE_OPERATION_3 is started");
                LongOperation.runAsync(3000, new LongOperation.LongOperationCallback() {
                    @Override
                    public boolean onProgress(long timeLeft) {
                        Utils.LogE(jobParameters.getJobId() +  ", timeLeft: " + timeLeft);
                        return true;
                    }

                    @Override
                    public void onFinished(boolean isInterrupted) {
                        Utils.LogE("Long operation for " + jobParameters.getJobId() + " is finished");
                        publishProgress("JOB_SERVICE_OPERATION_3 is finished");
                        MyJobService.this.jobFinished(jobParameters, false);
                    }
                });
                return false; // run on working thread
        }

        // true - means that operation is continue running on working thread,
        // in this case you need to call jobFinished() method after task is done.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Utils.LogE("onStopJob " + jobParameters.getJobId());
        return false; // do not reschedule
    }

    private void publishProgress(final String message) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyJobService.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

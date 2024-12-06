package ru.buggytrain.servicetrain.fragments;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import ru.buggytrain.servicetrain.R;
import ru.buggytrain.servicetrain.Utils;
import ru.buggytrain.servicetrain.services.MyJobService;

public class MyJobServiceFragment extends Fragment implements View.OnClickListener {


    public MyJobServiceFragment() {
    }

    public static MyJobServiceFragment newInstance() {
        return new MyJobServiceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_service_layout, container, false);

        view.findViewById(R.id.js_run_long_op1).setOnClickListener(this);
        view.findViewById(R.id.js_run_long_op2).setOnClickListener(this);
        view.findViewById(R.id.js_run_long_op3).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        JobScheduler scheduler = (JobScheduler) getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName serviceName = new ComponentName(getContext(), MyJobService.class);

        switch (view.getId()) {
            case R.id.js_run_long_op1:
                JobInfo jobInfo1 = new JobInfo.Builder(MyJobService.JOB_SERVICE_OPERATION_1, serviceName)
                        .setOverrideDeadline(0) // run immediate
                        .build();

                scheduler.schedule(jobInfo1);
                break;

            case R.id.js_run_long_op2:
                JobInfo jobInfo2 = new JobInfo.Builder(MyJobService.JOB_SERVICE_OPERATION_2, serviceName)
                        .setMinimumLatency(0) // latency is 0, but other constrains are required
                        .build();

                scheduler.schedule(jobInfo2);
                break;

            case R.id.js_run_long_op3:
                JobInfo jobInfo3 = new JobInfo.Builder(MyJobService.JOB_SERVICE_OPERATION_3, serviceName)
                        .setOverrideDeadline(0) // run immediate
                        .build();

                scheduler.schedule(jobInfo3);
                break;
        }
    }
}

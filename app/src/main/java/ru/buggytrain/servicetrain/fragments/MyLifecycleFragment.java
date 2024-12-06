package ru.buggytrain.servicetrain.fragments;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.buggytrain.servicetrain.IMainActivity;
import ru.buggytrain.servicetrain.R;
import ru.buggytrain.servicetrain.Utils;
import ru.buggytrain.servicetrain.services.MyLifecycleService;

import static android.arch.lifecycle.Lifecycle.Event.ON_ANY;
import static android.content.Context.BIND_AUTO_CREATE;

/**
 * My fragment that observes LifecycleService state
 * and service data changing using LiveData.
 */
public class MyLifecycleFragment extends Fragment implements View.OnClickListener, LifecycleObserver {

    private TextView mServiceStateInfo = null;
    private MyLifecycleService.MyBinder mMyBindServiceBinder = null;
    private ServiceConnection mMyServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Utils.LogE("onServiceConnected " + className);
            mMyBindServiceBinder = (MyLifecycleService.MyBinder) service;
            mMyBindServiceBinder.getServiceLifecycle().addObserver(MyLifecycleFragment.this);
            mMyBindServiceBinder.getProgressLiveData().observe(MyLifecycleFragment.this, progressObserver);
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Utils.LogE("onServiceDisconnected " + className);
            mMyBindServiceBinder = null;
        }
    };

    // live data observer
    Observer<String> progressObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
           updateInfoTextView(s);
        }
    };

    public MyLifecycleFragment() {
    }

    public static MyLifecycleFragment newInstance() {
        return new MyLifecycleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lifecycle_service_layout, container, false);

        mServiceStateInfo = view.findViewById(R.id.service_state_info);
        view.findViewById(R.id.bs_bind).setOnClickListener(this);
        view.findViewById(R.id.bs_unbind).setOnClickListener(this);
        view.findViewById(R.id.bs_run_long_op1).setOnClickListener(this);
        view.findViewById(R.id.bs_run_long_op2).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bs_bind:
                getActivity().bindService(MyLifecycleService.getStartIntent(getContext()), mMyServiceConnection, BIND_AUTO_CREATE);
                break;

            case R.id.bs_unbind:
                if (mMyBindServiceBinder != null) {
                    getActivity().unbindService(mMyServiceConnection);
                }
                break;

            case R.id.bs_run_long_op1:
                if (mMyBindServiceBinder != null) {
                    mMyBindServiceBinder.runLongOperation1();
                }
                break;

            case R.id.bs_run_long_op2:
                if (mMyBindServiceBinder != null) {
                    mMyBindServiceBinder.runLongOperation2();
                }
                break;

            default:
                Utils.LogE("unknown view has been clicked");
        }
    }

    @OnLifecycleEvent(ON_ANY)
    void onAny(LifecycleOwner source, Lifecycle.Event event) {
        Utils.LogE("ON_ANY, source: " + source + ", event: " + event);
        mServiceStateInfo.setText("Service Stae: " + source.getLifecycle().getCurrentState());
    }

    private void updateInfoTextView(String text) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof IMainActivity) {
            ((IMainActivity) activity).updateInfoTextView(text);
        }
    }

}

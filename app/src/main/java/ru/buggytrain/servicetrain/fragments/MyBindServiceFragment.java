package ru.buggytrain.servicetrain.fragments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.buggytrain.servicetrain.R;
import ru.buggytrain.servicetrain.Utils;
import ru.buggytrain.servicetrain.services.MyBindService;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by Buggy on 09.09.2017.
 */

public class MyBindServiceFragment extends Fragment implements View.OnClickListener {

    private MyBindService.MyBinder mMyBindServiceBinder = null;
    private ServiceConnection mMyServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Utils.LogE("onServiceConnected " + className);
            mMyBindServiceBinder = (MyBindService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Utils.LogE("onServiceDisconnected " + className);
            mMyBindServiceBinder = null;
        }
    };

    public MyBindServiceFragment() {
    }

    public static MyBindServiceFragment newInstance() {
        return new MyBindServiceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_service_layout, container, false);

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
                getActivity().bindService(MyBindService.getStartIntent(getContext()), mMyServiceConnection, BIND_AUTO_CREATE);
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
}

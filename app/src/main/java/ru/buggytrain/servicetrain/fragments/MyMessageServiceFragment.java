package ru.buggytrain.servicetrain.fragments;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.buggytrain.servicetrain.R;
import ru.buggytrain.servicetrain.Utils;
import ru.buggytrain.servicetrain.services.MyBindService;
import ru.buggytrain.servicetrain.services.MyMessageService;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by Buggy on 09.09.2017.
 */

public class MyMessageServiceFragment extends Fragment implements View.OnClickListener {

    Messenger mServiceMessenger = null;

    private ServiceConnection mMyServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceMessenger = new Messenger(service);
        }

        public void onServiceDisconnected(ComponentName className) {
            mServiceMessenger = null;
        }
    };

    public MyMessageServiceFragment() {
    }

    public static MyMessageServiceFragment newInstance() {
        return new MyMessageServiceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_service_layout, container, false);

        view.findViewById(R.id.ms_bind).setOnClickListener(this);
        view.findViewById(R.id.ms_unbind).setOnClickListener(this);
        view.findViewById(R.id.ms_say_hello).setOnClickListener(this);
        view.findViewById(R.id.ms_run_long_op1).setOnClickListener(this);
        view.findViewById(R.id.ms_run_long_op2).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ms_bind:
                getActivity().bindService(MyMessageService.getStartIntent(getContext()), mMyServiceConnection, BIND_AUTO_CREATE);
                break;

            case R.id.ms_unbind:
                if (mServiceMessenger != null) {
                    getActivity().unbindService(mMyServiceConnection);
                }
                break;

            case R.id.ms_say_hello:
                if (mServiceMessenger != null) {
                    Message msg = Message.obtain(null, MyMessageService.MSG_SAY_HELLO, 0, 0);
                    try {
                        mServiceMessenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.ms_run_long_op1:
                if (mServiceMessenger != null) {
                    Message msg = Message.obtain(null, MyMessageService.MSG_ACTION1, 0, 0);
                    try {
                        mServiceMessenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.ms_run_long_op2:
                if (mServiceMessenger != null) {
                    Message msg = Message.obtain(null, MyMessageService.MSG_ACTION2, 0, 0);
                    try {
                        mServiceMessenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                Utils.LogE("unknown view has been clicked");
        }
    }
}

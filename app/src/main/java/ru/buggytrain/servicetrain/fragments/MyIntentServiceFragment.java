package ru.buggytrain.servicetrain.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.buggytrain.servicetrain.R;
import ru.buggytrain.servicetrain.Utils;
import ru.buggytrain.servicetrain.services.MyIntentService;

/**
 * Created by Buggy on 09.09.2017.
 */

public class MyIntentServiceFragment extends Fragment implements View.OnClickListener {

    public MyIntentServiceFragment() {
    }

    public static MyIntentServiceFragment newInstance() {
        return new MyIntentServiceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intent_service_layout, container, false);

        // started service
        view.findViewById(R.id.is_run_long_op1).setOnClickListener(this);
        view.findViewById(R.id.is_run_long_op2).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // intent service
            case R.id.is_run_long_op1:
                getActivity().startService(MyIntentService.getStartIntent(getContext(), MyIntentService.SupportedCommands.ACTION1.toString()));
                break;

            case R.id.is_run_long_op2:
                getActivity().startService(MyIntentService.getStartIntent(getContext(), MyIntentService.SupportedCommands.ACTION2.toString()));
                break;

            default:
                Utils.LogE("unknown view has been clicked");
        }
    }
}

package ru.buggytrain.servicetrain.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.buggytrain.servicetrain.R;
import ru.buggytrain.servicetrain.Utils;
import ru.buggytrain.servicetrain.services.MyStartedService;

/**
 * Created by Buggy on 09.09.2017.
 */

public class MyStartedServiceFragment extends Fragment implements View.OnClickListener {

    public MyStartedServiceFragment() {
    }

    public static MyStartedServiceFragment newInstance() {
        return new MyStartedServiceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_started_service_layout, container, false);

        // started service
        view.findViewById(R.id.ss_run_long_op1).setOnClickListener(this);
        view.findViewById(R.id.ss_run_long_op2).setOnClickListener(this);
        view.findViewById(R.id.ss_run_long_op3).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // started service
            case R.id.ss_run_long_op1:
                getActivity().startService(MyStartedService.getStartIntent(getContext(), MyStartedService.SupportedCommands.ACTION1.toString()));
                break;

            case R.id.ss_run_long_op2:
                getActivity().startService(MyStartedService.getStartIntent(getContext(), MyStartedService.SupportedCommands.ACTION2.toString()));
                break;

            case R.id.ss_run_long_op3:
                getActivity().startService(MyStartedService.getStartIntent(getContext(), "Booom!"));
                break;


            default:
                Utils.LogE("unknown view has been clicked");
        }
    }
}

package ru.buggytrain.servicetrain;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import ru.buggytrain.servicetrain.fragments.MyBindServiceFragment;
import ru.buggytrain.servicetrain.fragments.MyIntentServiceFragment;
import ru.buggytrain.servicetrain.fragments.MyJobServiceFragment;
import ru.buggytrain.servicetrain.fragments.MyLifecycleFragment;
import ru.buggytrain.servicetrain.fragments.MyMessageServiceFragment;
import ru.buggytrain.servicetrain.fragments.MyStartedServiceFragment;
import ru.buggytrain.servicetrain.services.MyBindService;
import ru.buggytrain.servicetrain.services.MyIntentService;
import ru.buggytrain.servicetrain.services.MyMessageService;
import ru.buggytrain.servicetrain.services.MyStartedService;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private TextView mInfoTextView = null;

    // or you can uses PendingIntents instead of BroadcastReceivers
    // http://startandroid.ru/ru/uroki/vse-uroki-spiskom/160-urok-95-service-obratnaja-svjaz-s-pomoschju-pendingintent.html
    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Utils.LogE("onReceive " + intent);
            String action = intent.getAction();
            if (action == null) return;

            if (action.equals(MyStartedService.PUBLISH_PROGRESS_ACTION)) {
                String message = intent.getStringExtra(Utils.MESSAGE_INTENT_EXTRA_MESSAGE);
                Utils.LogE("message: " + message);
                updateInfoTextView(message);
            } else if (action.equals(MyIntentService.PUBLISH_PROGRESS_ACTION)){
                String message = intent.getStringExtra(Utils.MESSAGE_INTENT_EXTRA_MESSAGE);
                Utils.LogE("message: " + message);
                updateInfoTextView(message);
            } else if (action.equals(MyBindService.PUBLISH_PROGRESS_ACTION)) {
                String message = intent.getStringExtra(Utils.MESSAGE_INTENT_EXTRA_MESSAGE);
                Utils.LogE("message: " + message);
                updateInfoTextView(message);
            }
        }
    };

    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();

        MyFragmentPagerAdapter(FragmentManager fm, Fragment... args) {
            super(fm);
            for (int i=0; i<args.length; i++) {
                fragments.add(args[i]);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.LogE(MainActivity.class.getSimpleName() + " onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Started Service"));
        tabLayout.addTab(tabLayout.newTab().setText("Intent Service"));
        tabLayout.addTab(tabLayout.newTab().setText("Bind Service"));
        tabLayout.addTab(tabLayout.newTab().setText("Message Service"));
        tabLayout.addTab(tabLayout.newTab().setText("Lifecycle Service"));
        tabLayout.addTab(tabLayout.newTab().setText("Job Service"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // setup view pager
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                MyStartedServiceFragment.newInstance(),
                MyIntentServiceFragment.newInstance(),
                MyBindServiceFragment.newInstance(),
                MyMessageServiceFragment.newInstance(),
                MyLifecycleFragment.newInstance(),
                MyJobServiceFragment.newInstance());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // other inits
        mInfoTextView = (TextView) findViewById(R.id.info_text_view);

        // register receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyStartedService.PUBLISH_PROGRESS_ACTION);
        intentFilter.addAction(MyIntentService.PUBLISH_PROGRESS_ACTION);
        intentFilter.addAction(MyBindService.PUBLISH_PROGRESS_ACTION);
        intentFilter.addAction(MyMessageService.PUBLISH_PROGRESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        Utils.LogE(MainActivity.class.getSimpleName() + " onDestroy");
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.LogE("* " + MainActivity.class.getSimpleName() + " onActivityResult: ");
        Utils.LogE(String.format("* %s requestCode: %d, resultCode: %d, intent: %s", MainActivity.class.getSimpleName(), requestCode, resultCode, data));
    }

    @Override
    public void updateInfoTextView(String message) {
        mInfoTextView.setText(message);
    }
}

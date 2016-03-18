package edu.ifpb.pdm.applistservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import edu.ifpb.pdm.applistservice.MainActivity;
import edu.ifpb.pdm.applistservice.service.ServiceLoadList;

public class MyReceiver extends BroadcastReceiver {
    private Notice notice;

    public MyReceiver(Notice note) {
        notice = note;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        notice.process(intent.getStringArrayListExtra("list"));
    }
}

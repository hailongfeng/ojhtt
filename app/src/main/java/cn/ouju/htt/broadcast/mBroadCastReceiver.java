package cn.ouju.htt.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class mBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        callback.refresh();
    }

    public interface CallBack {
        void refresh();
    }

    private CallBack callback;

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }
}

package cn.ouju.htt.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import cn.ouju.htt.R;

/**
 * Created by Administrator on 2017/9/23.
 */

public class AlertDialogUtils {
    public static void showMsg(Context context, String message) {
        new AlertDialog.Builder(context).setMessage(message).setCancelable(false).setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    public static void showMsg(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.confirm), listener).create();
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return (keyCode == KeyEvent.KEYCODE_SEARCH);
            }
        });
        builder.show();
    }
}

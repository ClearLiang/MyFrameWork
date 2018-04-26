package com.example.a99794.framework.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a99794.mytest.R;

/**
 * Created by 99794 on 2018/4/13.
 */

public class DialogUtils {
    private static DialogUtils dialogUtils;
    private static AlertDialog.Builder normalDialog;

    private DialogUtils() {
    }

    public static synchronized DialogUtils getDialogUtils() {
        if(dialogUtils == null){
            dialogUtils = new DialogUtils();
        }
        return dialogUtils;
    }
    public String showEditDialog(final Activity activity, String title, DialogInterface.OnClickListener negativeButton, DialogInterface.OnClickListener positiveButton){
        final String[] result = {""};
        normalDialog = new AlertDialog.Builder(activity);
        normalDialog.setTitle(title);
        final EditText editText = new EditText(activity);
        normalDialog.setView(editText);
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(activity,"执行操作",Toast.LENGTH_SHORT).show();
                result[0] = editText.getText().toString();
            }
        });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                result[0] = "cancel";
                Toast.makeText(activity,"取消操作",Toast.LENGTH_SHORT).show();
            }
        });
        normalDialog.show();
        return result[0];
    }

    public void showInputDialog(final Activity activity) {
        final EditText editText = new EditText(activity);
        normalDialog = new AlertDialog.Builder(activity);
        normalDialog.setTitle("我是一个输入Dialog").setView(editText);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText.getText().toString();
                    }
                }).show();
    }

    public void showNormalDialog(Activity activity,int icon ,String title,String message, DialogInterface.OnClickListener negativeButton, DialogInterface.OnClickListener positiveButton){
        normalDialog = new AlertDialog.Builder(activity);
        normalDialog.setIcon(icon);
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        final EditText editText = new EditText(activity);
        normalDialog.setView(editText);
        normalDialog.setPositiveButton("确定",positiveButton);
        normalDialog.setNegativeButton("取消",negativeButton);
        normalDialog.show();
    }

    /**
     * 显示loading对话框
     */
    public AlertDialog.Builder loadingDlg(final Activity activity, String msg) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.utils_dialog_loading, null);
        normalDialog = new AlertDialog.Builder(activity);
        normalDialog.setView(view);
        normalDialog.setCancelable(false);
        normalDialog.show();
        TextView dlg_tv_title = (TextView) view.findViewById(R.id.tv_loading_msg);
        dlg_tv_title.setText(msg);
        /*dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                OkHttpUtils.getInstance().cancelTag("send_extract_check_email");
            }
        });*/
        return normalDialog;
    }
}

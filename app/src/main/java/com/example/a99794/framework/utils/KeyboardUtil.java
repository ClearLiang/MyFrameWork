package com.example.a99794.framework.utils;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.example.a99794.framework.R;

/**
 *@作者 ClearLiang
 *@日期 2018/4/16
 *@描述 在布局文件中加入 <android.inputmethodservice.KeyboardView>
 *     并设置android:id="@+id/keyboard_view"   （通用一个id）
 *     在onCreate()中调用showKeyBoard()
 *     或者 keyboardUtil = new KeyboardUtil(activity_pay.this, mEdit);
 *     keyboardUtil.showKeyboard();
 **/

public class KeyboardUtil {

    private KeyboardView keyboardView;
    private Keyboard k2;// 数字键盘
    private EditText ed;
    private boolean isPre = true;

    public KeyboardUtil(Activity act, EditText edit) {
        this.ed = edit;
        k2 = new Keyboard(act, R.xml.keyboard_numbers_my);
        keyboardView = act.findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k2);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(isPre);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    public void setPreviewEnabled(boolean isPreview){
        keyboardView.setPreviewEnabled(isPreview);
    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            //LogUtils.i(primaryCode);
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            //LogUtils.i("start = "+start);
            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                //LogUtils.i("点击回退键之前的值--"+editable.toString());
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

}

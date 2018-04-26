package com.example.a99794.framework.presenter;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.a99794.framework.presenter.viewinterface.FirstFragmentViewInterface;
import com.example.a99794.framework.view.base.BasePresenter;

/**
 * Created by 99794 on 2018/4/13.
 */

public class FirstFragmentPresenter extends BasePresenter<FirstFragmentViewInterface> {
    FirstFragmentViewInterface firstFragmentViewInterface;

    public FirstFragmentPresenter(FirstFragmentViewInterface firstFragmentViewInterface) {
        this.firstFragmentViewInterface = firstFragmentViewInterface;
    }

    public boolean login(String username, String password, Context context){
        boolean result = false;
        String[] names = {"李文志", "李世民", "lwl", "lili"};

        //判断非空
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(context, "用户名不能为空", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
            result = false;
        }
        //如果用户名和密码正确才做相应的动作
        //遍历每一个用户名做判断

        for (int i = 0; i < names.length; i++) {
            if (username.equals(names[i]) && password.equals("123456")) {
                Toast.makeText(context, "恭喜你登陆成功", Toast.LENGTH_LONG).show();
                result = true;
            }
        }
        if (!result) {
            Toast.makeText(context, "用户名或密码不正确！", Toast.LENGTH_LONG).show();
        }
        return result;
    }
}

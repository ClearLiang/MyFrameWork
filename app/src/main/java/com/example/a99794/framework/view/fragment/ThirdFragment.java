package com.example.a99794.framework.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a99794.framework.event.FirstEvent;
import com.example.a99794.framework.presenter.FirstFragmentPresenter;
import com.example.a99794.framework.presenter.viewinterface.FirstFragmentViewInterface;
import com.example.a99794.framework.utils.SPUtil;
import com.example.a99794.framework.view.base.BaseFragment;
import com.example.a99794.framework.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @作者 ClearLiang
 * @日期 2018/4/13
 * @描述 第三个 fragment
 **/

public class ThirdFragment extends BaseFragment<FirstFragmentViewInterface, FirstFragmentPresenter> implements FirstFragmentViewInterface, CompoundButton.OnCheckedChangeListener {
    private EditText etFragment3Username;
    private EditText etFragment3Pwd;
    private CheckBox checkboxFragment3Remember;
    private CheckBox checkboxFragment3Login;
    private Button btnFragment3Login;
    //下面是数据的相关存储
    SPUtil helper;

    @Override
    protected FirstFragmentPresenter createPresenter() {
        return new FirstFragmentPresenter(this);
    }

    public static ThirdFragment newInstance() {

        Bundle args = new Bundle();

        ThirdFragment fragment = new ThirdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        initView(view);
        helper = new SPUtil(getContext(), "login");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(helper.getBoolean("isAutoLogin")){
            String lastTime = helper.getString("time");
            LogUtils.i("上一次时间："+lastTime);
            String currentTime = TimeUtils.getNowString();
            LogUtils.i("这一次时间："+currentTime);
            if(TimeUtils.getTimeSpan(lastTime,currentTime, TimeConstants.SEC)<20){
                ToastUtils.showShort("再次登陆");
            }else {
                LogUtils.i("超时，请重新登陆");
                //ToastUtil.showShort("超时，请重新登陆");
            }
        }
    }

    @Override
    protected void initEvent() {
        btnFragment3Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取用户输入的数据
                String username = etFragment3Username.getText().toString();
                String password = etFragment3Pwd.getText().toString();
                if(mPresenter.login(username,password,getContext())){


                    //如果点击记住密码，那么要保存密码
                    if (checkboxFragment3Remember.isChecked()) {
                        //保存用户名
                        helper.putValues(new SPUtil.ContentValue("username", username));
                        //保存密码
                        helper.putValues(new SPUtil.ContentValue("password", password));
                        //保存当前时间
                    } else {
                        //清除所有的信息
                        //helper.clear();
                        //清除密码信息
                        helper.putValues(new SPUtil.ContentValue("username", username));
                        helper.remove("password");
                    }

                    //如果点击了自动登陆，要记住选择的状态
                    if (checkboxFragment3Login.isChecked()) {
                        //保存自动登陆的状态
                        helper.putValues(new SPUtil.ContentValue("isAutoLogin", true));
                        helper.putValues(new SPUtil.ContentValue("time",TimeUtils.getNowString()));
                    } else {
                        helper.putValues(new SPUtil.ContentValue("isAutoLogin", false));
                    }
                }
            }
        });
    }

    private void initView(View view) {
        etFragment3Username = view.findViewById(R.id.et_fragment_3_username);
        etFragment3Pwd = view.findViewById(R.id.et_fragment_3_pwd);
        checkboxFragment3Remember = view.findViewById(R.id.checkbox_fragment_3_remember);
        checkboxFragment3Login = view.findViewById(R.id.checkbox_fragment_3_login);
        btnFragment3Login = view.findViewById(R.id.btn_fragment_3_login);

        //给两个选框设置监听事件
        checkboxFragment3Remember.setOnCheckedChangeListener(this);
        checkboxFragment3Login.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        //当对记住密码选框操作时
        if (compoundButton == checkboxFragment3Remember) {
            if(!checkboxFragment3Remember.isChecked()){
                if(checkboxFragment3Login.isChecked()){
                    checkboxFragment3Login.setChecked(false);
                }
            }
        }

        //当对自动登陆选框操作时
        if (compoundButton == checkboxFragment3Login) {
            if(checkboxFragment3Login.isChecked()){
                if(!checkboxFragment3Remember.isChecked()){
                    checkboxFragment3Remember.setChecked(true);
                }
            }
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FirstEvent event){
        LogUtils.i("用户名" + event.getName() + "密码" + event.getPwd());
    }
}

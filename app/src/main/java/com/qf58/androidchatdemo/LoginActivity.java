package com.qf58.androidchatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by linSir
 * date at 2017/12/17.
 * describe:
 */

public class LoginActivity extends AppCompatActivity {

    private EditText phone;
    private EditText pwd;
    private Button login;
    private Button register;
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone = (EditText) findViewById(R.id.phone);
        pwd = (EditText) findViewById(R.id.pwd);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        try{
            phone.setText(SPUtils.getInstance().getString("username"));
            pwd.setText(SPUtils.getInstance().getString("pwd"));
        }catch (Exception e){

        }



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray array = CacheUtils.getInstance().getJSONArray("user");
                if (array == null) {
                    ToastUtils.showShort("用户名或密码错误");
                    return;
                }
                List<User> userList = gson.fromJson(array.toString(),
                        new TypeToken<List<User>>() {
                        }.getType());

                LogUtils.e("---lin---> userList  " + userList.size());

                boolean isLoginSuccess = false;
                for (int i = 0; i < userList.size(); i++) {
                    User user = userList.get(i);

                    LogUtils.e("---lin---> " + user.getUsername());
                    LogUtils.e("---lin---> " + user.getPwd());


                    if (user.getUsername().equals(phone.getText().toString())
                            && user.getPwd().equals(pwd.getText().toString())) {
                        isLoginSuccess = true;

                    }
                }
                if (isLoginSuccess) {
                    SPUtils.getInstance().put("username", phone.getText().toString());
                    SPUtils.getInstance().put("pwd", pwd.getText().toString());

                    Intent intent = new Intent(LoginActivity.this, FriendList.class);
                    intent.putExtra("phone", phone.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showShort("用户名或密码错误");
                }

            }
        });


    }
}

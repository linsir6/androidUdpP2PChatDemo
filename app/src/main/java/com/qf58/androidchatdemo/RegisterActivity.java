package com.qf58.androidchatdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir
 * date at 2017/12/17.
 * describe:
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText phone;
    private EditText pwd;
    private Button register;
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phone = (EditText) findViewById(R.id.phone);
        pwd = (EditText) findViewById(R.id.pwd);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().toString().length() != 11) {
                    ToastUtils.showShort("请输入11位手机号");
                    return;
                }

                List<User> userList;
                JSONArray array = CacheUtils.getInstance().getJSONArray("user");
                if (array != null) {
                    userList = gson.fromJson(array.toString(),
                            new TypeToken<List<User>>() {
                            }.getType());
                } else {
                    userList = new ArrayList<User>();
                }
                for (User user : userList) {
                    if (user.getUsername().equals(phone.getText().toString())) {
                        ToastUtils.showShort("该用户已经存在");
                        return;
                    }
                }
                User user = new User(phone.getText().toString(), pwd.getText().toString());
                userList.add(user);
                CacheUtils.getInstance().put("user", gson.toJson(userList));
                ToastUtils.showShort("注册成功");
                finish();
            }
        });


    }
}
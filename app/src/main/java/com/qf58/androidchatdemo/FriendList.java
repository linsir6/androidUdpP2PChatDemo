package com.qf58.androidchatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by linSir
 * date at 2017/12/20.
 * describe:
 */

public class FriendList extends AppCompatActivity {

    private LinearLayout list;
    private Button web_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        list = (LinearLayout) findViewById(R.id.list);
        web_view = (Button) findViewById(R.id.web_view);


        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendList.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        web_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendList.this,WebViewActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}

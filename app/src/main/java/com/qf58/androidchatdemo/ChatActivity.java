package com.qf58.androidchatdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {

    private InternetDynamicBroadCastReceiver mReceiver;

    private ChatAdapter chatAdapter;
    /**
     * 声明ListView
     */
    private ListView lv_chat_dialog;
    private String text = "";
    DatagramPacket dp = null;
    //将要发送的数据、要发送到什么地址设置好并打成一个 DatagramPacket 包
    /**
     * 集合
     */
    private List<PersonChat> personChats = new ArrayList<PersonChat>();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    /**
                     * ListView条目控制在最后一行
                     */
                    lv_chat_dialog.setSelection(personChats.size());
                    final byte[] buffer = text.getBytes();
                    LogUtils.e("---lin--->  run  2  text" + text);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            DatagramSocket ds = null;
                            try {
                                //new 一个DatagramSocket对象（即打开一个UDP端口准备从此处发出数据包）
                                LogUtils.e("---lin--->  run  2");
                                //    dp = new DatagramPacket(buffer, buffer.length, new InetSocketAddress("192.168.20.211", 9999));
                                dp = new DatagramPacket(buffer, buffer.length, new InetSocketAddress("192.168.20.14", 9999));

                                ds = new DatagramSocket(9998);
                                ds.send(dp);
                                ds.close();
                            } catch (SocketException e) {
                                LogUtils.e("---lin--->  run  3");
                                e.printStackTrace();
                            } catch (IOException e) {
                                LogUtils.e("---lin--->  run  4");

                                e.printStackTrace();
                            }
                        }
                    }).start();

                    break;

                default:
                    chatAdapter.notifyDataSetChanged();
                    lv_chat_dialog.setSelection(personChats.size());

                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        boolean a = NetworkUtils.isWifiAvailable();
        if (a) {
            ToastUtils.showShort("当前网络可用");
        } else {
            ToastUtils.showShort("当前网络不可用");
        }
        ToastUtils.showLong("当前ip地址为 " + NetworkUtils.getIPAddress(true));


        lv_chat_dialog = (ListView) findViewById(R.id.lv_chat_dialog);
        Button btn_chat_message_send = (Button) findViewById(R.id.btn_chat_message_send);
        final EditText et_chat_message = (EditText) findViewById(R.id.et_chat_message);
        /**
         *setAdapter
         */
        personChats = DataSupport.findAll(PersonChat.class);
        if (personChats == null || personChats.size() == 0) {
            personChats = new ArrayList<>();
        }

        chatAdapter = new ChatAdapter(this, personChats);
        lv_chat_dialog.setAdapter(chatAdapter);


        LogUtils.e("---lin--->  长度为" + personChats.size());
        for (int i = 0; i < personChats.size(); i++) {
            LogUtils.e("---lin---> " + personChats.get(i).getChatMessage() + personChats.get(i).getName() + personChats.get(i).isMeSend());
        }


        lv_chat_dialog.setSelection(personChats.size());

        /**
         * 发送按钮的点击事件
         */
        btn_chat_message_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(et_chat_message.getText().toString())) {
                    Toast.makeText(ChatActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                PersonChat personChat = new PersonChat();
                //代表自己发送
                personChat.setMeSend(true);
                //得到发送内容
                personChat.setChatMessage(et_chat_message.getText().toString());
                personChat.save();
                text = et_chat_message.getText().toString();
                LogUtils.e("---lin--->  正在发送 " + text);
                //加入集合
                personChats.add(personChat);
                //清空输入框
                et_chat_message.setText("");
                //刷新ListView
                chatAdapter.notifyDataSetChanged();
                handler.sendEmptyMessage(1);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("---lin--->  run  ");
                //先将 "Hello!" 封装成一个字符串对象，在调用其 getBytes() 方法将其转换成字节数组
                //转换成字节数组是为了方便传输


                //以 "字节数组" 的形式开辟一块内存用于缓存接收到的UDP数据包
                byte[] buffer = new byte[1024];

                //虽然开辟的缓冲内存大小为1024字节，但也可以设置一个小于该值的缓存空间接收数据包
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                DatagramSocket ds = null;
                try {
                    //监听在UDP 9999 端口
                    ds = new DatagramSocket(9999);
                } catch (SocketException e) {
                    e.printStackTrace();
                }

                while (true) {
                    try {
                        //receive() 方法是一个阻塞性方法！
                        ds.receive(dp);

                        handler.sendEmptyMessage(2);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //从下一行代码中可以学习到一种用字节数组构造字符串对象的方法

                    PersonChat personChat = new PersonChat();
                    personChat.setChatMessage(new String(buffer, 0, dp.getLength()));
                    personChat.setMeSend(false);
                    personChat.save();
                    personChats.add(personChat);
                    handler.sendEmptyMessage(2);
                    System.out.println(new String(buffer, 0, dp.getLength()));
                    LogUtils.e("---lin---> result " + new String(buffer, 0, dp.getLength()));
                }

            }
        }).start();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new InternetDynamicBroadCastReceiver();
        this.registerReceiver(new InternetDynamicBroadCastReceiver(), filter);


    }

    public class InternetDynamicBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(ChatActivity.this,"网络发生了变化",Toast.LENGTH_SHORT).show();
        }
    }



}

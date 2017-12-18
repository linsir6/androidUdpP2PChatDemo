package com.qf58.androidchatdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by linSir
 * date at 2017/12/17.
 * describe:
 */

public class TestActivity extends AppCompatActivity {

    // TODO: 2017/12/18 1.sqlite数据库 6.LitePal  2.广播，网络变化4g，wifi 3.强制下线  4.多线程  5.解析json

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                LogUtils.e("---lin--->  run  ");
//                //先将 "Hello!" 封装成一个字符串对象，在调用其 getBytes() 方法将其转换成字节数组
//                //转换成字节数组是为了方便传输
//                byte[] buffer = (new String("11111!")).getBytes();
//                DatagramPacket dp = null;
//                //将要发送的数据、要发送到什么地址设置好并打成一个 DatagramPacket 包
//                dp = new DatagramPacket(buffer, buffer.length, new InetSocketAddress("192.168.199.226", 9999));
//
//                DatagramSocket ds = null;
//                try {
//                    //new 一个DatagramSocket对象（即打开一个UDP端口准备从此处发出数据包）
//                    LogUtils.e("---lin--->  run  2");
//
//                    ds = new DatagramSocket(9998);
//                    ds.send(dp);
//                    ds.close();
//                } catch (SocketException e) {
//                    LogUtils.e("---lin--->  run  3");
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    LogUtils.e("---lin--->  run  4");
//
//                    e.printStackTrace();
//                }


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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //从下一行代码中可以学习到一种用字节数组构造字符串对象的方法
                    System.out.println(new String(buffer, 0, dp.getLength()));
                    LogUtils.e("---lin---> result " + new String(buffer, 0, dp.getLength()));
                }

            }
        }).start();

    }
}

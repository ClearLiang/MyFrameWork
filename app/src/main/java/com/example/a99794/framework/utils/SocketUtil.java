package com.example.a99794.framework.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @作者 ClearLiang
 * @日期 2018/4/28
 * @描述 Socket工具类
 **/
public class SocketUtil {
    public static SocketUtil sSocketUtil;

    private SocketUtil() {
    }

    public static synchronized SocketUtil getSocketUtil() {
        if(sSocketUtil == null){
            sSocketUtil = new SocketUtil();
        }
        return sSocketUtil;
    }

    private Socket mSocket;

    public void connectedSocket(String ip,int port){
        try {
            // 创建一个Socket对象，并指定服务端的IP及端口号
            System.out.println("连接到主机：" + ip + " ，端口号：" + port);
            mSocket = new Socket(ip,port);
            System.out.println("远程主机地址：" + mSocket.getRemoteSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readMessageToClient(String pathName){
        try {
            // 创建一个InputStream用户读取要发送的文件。
            InputStream inputStream = new FileInputStream(pathName);
            // 获取Socket的OutputStream对象用于发送数据。
            OutputStream outputStream = mSocket.getOutputStream();
            // 创建一个byte类型的buffer字节数组，用于存放读取的本地文件
            byte buffer[] = new byte[4 * 1024];
            int temp = 0;
            // 循环读取文件
            while ((temp = inputStream.read(buffer)) != -1) {
                // 把数据写入到OuputStream对象中
                outputStream.write(buffer, 0, temp);
            }
            // 发送读取的数据到服务端
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessageToClient(String msg){
        try {
            OutputStream outToServer = mSocket.getOutputStream();
            outToServer.flush();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("客户端内容：" + msg);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void getMessageFromClient(){
        try {
            InputStream inFromServer = mSocket.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            System.out.println("服务器响应： " + in.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket(){
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

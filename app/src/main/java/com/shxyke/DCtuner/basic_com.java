package com.shxyke.DCtuner;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class basic_com {

    private static final String TAG = "RootCmd";
    private static boolean mHaveRoot = false;

    public static boolean haveRoot() {
        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
            if (ret != -1) {
                Log.i(TAG, "have root!");
                mHaveRoot = true;
            } else {
                Log.i(TAG, "not root!");
            }
        } else {
            Log.i(TAG, "mHaveRoot = true, have root!");
        }
        return mHaveRoot;
    }

    private static String execRootCmd(String cmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            InputStreamReader dat=new InputStreamReader(dis);
            BufferedReader bin=new BufferedReader(dat);
            while ((line = bin.readLine()) != null) {
                Log.d("result", line);
                result += line;
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    // 执行命令但不关注结果输出
    private static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean get_status(){
        return Integer.parseInt(execRootCmd("cat /proc/fliker_free/fliker_free")) == 1;
    }

    public static void set_status(boolean enabled){
        if(enabled){
            execRootCmdSilent("echo 1 > /proc/fliker_free/fliker_free");
        }else{
            execRootCmdSilent("echo 0 > /proc/fliker_free/fliker_free");
        }
    }

    public static int get_elvss_num(){
        return Integer.valueOf(execRootCmd("cat /proc/fliker_free/min_brightness"));
    }

    public static void set_elvss_num(int num){
        execRootCmdSilent("echo " + num + " > /proc/fliker_free/min_brightness");
    }

    public static void update_status(Context context){
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = context.getSharedPreferences("share", context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.DcModeStatus),get_status());
        editor.putInt(context.getString(R.string.ElvssMin),get_elvss_num());
        editor.apply();
    }
}

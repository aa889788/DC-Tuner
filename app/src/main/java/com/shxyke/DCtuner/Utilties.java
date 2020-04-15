package com.shxyke.DCtuner;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.regex.*;

public class Utilties {

    private static final String TAG = "RootCmd";
    private static boolean mHaveRoot = false;

    public static boolean haveRoot() {
        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
            Log.i(TAG, String.valueOf(ret));
            if (ret == 0) {
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

    public static boolean isDCKernel(){
        String ret = execRootCmd("ls /proc/flicker_free/flicker_free");
        Pattern pattern = Pattern.compile("^/proc/flicker_free/flicker_free$");
        Matcher matcher = pattern.matcher(ret);
        if(matcher.find()){
            return true;
        }
        return false;
    }

    public static boolean isColorModKernel(){
        String ret = execRootCmd("ls /sys/class/graphics/fb0/adaption_mode_");
        Pattern pattern = Pattern.compile("^/sys/class/graphics/fb0/adaption_mode_$");
        Matcher matcher = pattern.matcher(ret);
        if(matcher.find()){
            return true;
        }
        return false;
    }

    private static String execRootCmd(String cmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            //Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            InputStreamReader dat=new InputStreamReader(dis);
            BufferedReader bin=new BufferedReader(dat);
            while ((line = bin.readLine()) != null) {
                //Log.d("result", line);
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

            //Log.i(TAG, cmd);
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

    public static boolean get_dc_status(){
        return Integer.parseInt(execRootCmd("cat /proc/flicker_free/flicker_free")) == 1;
    }

    public static void set_dc_status(boolean enabled){
        if(enabled){
            execRootCmdSilent("echo 1 > /proc/flicker_free/flicker_free");
        }else{
            execRootCmdSilent("echo 0 > /proc/flicker_free/flicker_free");
        }
    }

    public static boolean get_srgb_status(){
        String ret = execRootCmd("cat /sys/class/graphics/fb0/srgb");
        Pattern pattern = Pattern.compile("^mode = ([0-1]).*|^([0-1])$");
        Matcher matcher = pattern.matcher(ret);
        if(matcher.find()){
            if(matcher.group(1) != null) {
                return Integer.parseInt(matcher.group(1)) == 1;
            }
            else{
                return Integer.parseInt(matcher.group(2)) == 1;
            }
        }
        return false;
    }

    public static void set_srgb_status(boolean enabled){
        if(enabled){
            execRootCmdSilent("echo 1 > /sys/class/graphics/fb0/srgb");
        }else{
            execRootCmdSilent("echo 0 > /sys/class/graphics/fb0/srgb");
        }
    }

    public static boolean get_dci_p3_status(){
        String ret = execRootCmd("cat /sys/class/graphics/fb0/dci_p3");
        Pattern pattern = Pattern.compile("^mode = ([0-1]).*|^([0-1])$");
        Matcher matcher = pattern.matcher(ret);
        if(matcher.find()){
            if(matcher.group(1) != null) {
                return Integer.parseInt(matcher.group(1)) == 1;
            }
            else{
                return Integer.parseInt(matcher.group(2)) == 1;
            }
        }
        return false;
    }

    public static void set_dci_p3_status(boolean enabled){
        if(enabled){
            execRootCmdSilent("echo 1 > /sys/class/graphics/fb0/dci_p3");
        }else{
            execRootCmdSilent("echo 0 > /sys/class/graphics/fb0/dci_p3");
        }
    }

    public static boolean get_adaption_status(){
        String ret = execRootCmd("cat /sys/class/graphics/fb0/adaption_mode_");
        Pattern pattern = Pattern.compile("^mode = ([0-1]).*|^([0-1])$");
        Matcher matcher = pattern.matcher(ret);
        if(matcher.find()){
            if(matcher.group(1) != null) {
                return Integer.parseInt(matcher.group(1)) == 1;
            }
            else{
                return Integer.parseInt(matcher.group(2)) == 1;
            }
        }
        return false;
    }

    public static void set_adaption_status(boolean enabled){
//        if(get_srgb_status()){
//            set_srgb_status(false);
//        }
//        if(get_dci_p3_status()){
//            set_dci_p3_status(false);
//        }
//        if(get_opmode_status()){
//            set_opmode_status(false);
//        }
        if(enabled){
            execRootCmdSilent("echo 1 > /sys/class/graphics/fb0/adaption_mode_");
        }else{
            execRootCmdSilent("echo 0 > /sys/class/graphics/fb0/adaption_mode_");
        }
    }

    public static boolean get_opmode_status(){
        String ret = execRootCmd("cat /sys/class/graphics/fb0/oneplus_mode");
        Pattern pattern = Pattern.compile("^mode = ([0-1]).*|^([0-1])$");
        Matcher matcher = pattern.matcher(ret);
        if(matcher.find()){
            if(matcher.group(1) != null) {
                return Integer.parseInt(matcher.group(1)) == 1;
            }
            else{
                return Integer.parseInt(matcher.group(2)) == 1;
            }
        }
        return false;
    }

    public static void set_opmode_status(boolean enabled){
//        if(get_srgb_status()){
//            set_srgb_status(false);
//        }
//        if(get_adaption_status()){
//            set_srgb_status(false);
//        }
//        if(get_dci_p3_status()){
//            set_dci_p3_status(false);
//        }
        if(enabled){
            execRootCmdSilent("echo 1 > /sys/class/graphics/fb0/oneplus_mode");
        }else{
            execRootCmdSilent("echo 0 > /sys/class/graphics/fb0/oneplus_mode");
        }
    }


    public static int get_elvss_num(){
        return Integer.valueOf(execRootCmd("cat /proc/flicker_free/min_brightness"));
    }

    public static void set_elvss_num(int num){
        execRootCmdSilent("echo " + num + " > /proc/flicker_free/min_brightness");
    }

    public static void update_status(Context context){
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = context.getSharedPreferences("share", context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.dc_status),get_dc_status());
        if(isColorModKernel()) {
            editor.putBoolean(context.getString(R.string.dci_p3_status), get_dci_p3_status());
            editor.putBoolean(context.getString(R.string.srgb_status), get_srgb_status());
            //editor.putBoolean(context.getString(R.string.adaption_status), get_adaption_status());
            //editor.putBoolean(context.getString(R.string.oneplus_status), get_opmode_status());
        }
        editor.putInt(context.getString(R.string.elvss_min),get_elvss_num());
        editor.apply();
    }
}

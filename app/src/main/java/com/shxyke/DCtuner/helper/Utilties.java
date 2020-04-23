package com.shxyke.DCtuner.helper;

import android.content.Context;

import com.shxyke.DCtuner.Contents;
import com.shxyke.DCtuner.utils.SPUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.regex.*;

public class Utilties {

    private static boolean mHaveRoot = false;

    public static boolean haveRoot() {
        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo test");
            mHaveRoot = ret == 0;
        }
        return mHaveRoot;
    }

    public static boolean isDCKernel(){
        String ret = execRootCmd("ls /proc/fliker_free/fliker_free");
        Pattern pattern = Pattern.compile("^/proc/fliker_free/fliker_free$");
        Matcher matcher = pattern.matcher(ret);
        return matcher.find();
    }

    public static boolean isColorModKernel(){
        String ret = execRootCmd("ls /sys/class/graphics/fb0/adaption_mode_");
        Pattern pattern = Pattern.compile("^/sys/class/graphics/fb0/adaption_mode_$");
        Matcher matcher = pattern.matcher(ret);
        return matcher.find();
    }

    private static String execRootCmd(String cmd) {
        StringBuilder result = new StringBuilder();
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line;
            InputStreamReader dat=new InputStreamReader(dis);
            BufferedReader bin=new BufferedReader(dat);
            while ((line = bin.readLine()) != null) {
                result.append(line);
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
        return result.toString();
    }

    // 执行命令但不关注结果输出
    private static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());

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
        return Integer.parseInt(execRootCmd("cat /proc/fliker_free/fliker_free")) == 1;
    }

    public static void set_dc_status(boolean enabled){
        if(enabled){
            execRootCmdSilent("echo 1 > /proc/fliker_free/fliker_free");
        }else{
            execRootCmdSilent("echo 0 > /proc/fliker_free/fliker_free");
        }
    }

    public static boolean get_srgb_status(){
        String ret = execRootCmd("cat /sys/class/graphics/fb0/srgb");
        Pattern pattern = Pattern.compile("^mode = ([0-1]).*|^([0-1])$");
        Matcher matcher = pattern.matcher(ret);
        if(matcher.find()){
            if(matcher.group(1) != null) {
                return Integer.parseInt(Objects.requireNonNull(matcher.group(1))) == 1;
            } else {
                return Integer.parseInt(Objects.requireNonNull(matcher.group(2))) == 1;
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
                return Integer.parseInt(Objects.requireNonNull(matcher.group(1))) == 1;
            } else {
                return Integer.parseInt(Objects.requireNonNull(matcher.group(2))) == 1;
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
                return Integer.parseInt(Objects.requireNonNull(matcher.group(1))) == 1;
            }
            else{
                return Integer.parseInt(Objects.requireNonNull(matcher.group(2))) == 1;
            }
        }
        return false;
    }

    public static void set_adaption_status(boolean enabled){
        /*if(get_srgb_status()){
            set_srgb_status(false);
        }
        if(get_dci_p3_status()){
            set_dci_p3_status(false);
        }
        if(get_opmode_status()){
            set_opmode_status(false);
        } */
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
                return Integer.parseInt(Objects.requireNonNull(matcher.group(1))) == 1;
            }
            else{
                return Integer.parseInt(Objects.requireNonNull(matcher.group(2))) == 1;
            }
        }
        return false;
    }

    public static void set_opmode_status(boolean enabled){
        /*if(get_srgb_status()){
            set_srgb_status(false);
        }
        if(get_adaption_status()){
            set_srgb_status(false);
        }
        if(get_dci_p3_status()){
            set_dci_p3_status(false);
        }*/
        if(enabled){
            execRootCmdSilent("echo 1 > /sys/class/graphics/fb0/oneplus_mode");
        }else{
            execRootCmdSilent("echo 0 > /sys/class/graphics/fb0/oneplus_mode");
        }
    }


    public static int get_elvss_num(){
        return Integer.parseInt(execRootCmd("cat /proc/fliker_free/min_brightness"));
    }

    public static void set_elvss_num(int num){
        execRootCmdSilent("echo " + num + " > /proc/fliker_free/min_brightness");
    }

    public static void update_status(){
        SPUtils.putData(Contents.SP_DC_DIMMING_STATUS_KEY,get_dc_status());
        if(isColorModKernel()) {
            SPUtils.putData(Contents.SP_DCIP3_MODE_STATUS_KEY, get_dci_p3_status());
            SPUtils.putData(Contents.SP_SRGB_MODE_STATUS_KEY, get_srgb_status());
        }
        SPUtils.putData(Contents.SP_ELVSS_MIN_KEY,get_elvss_num());
    }

}

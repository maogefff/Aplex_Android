package com.aplex.canopenboot.utils;

import android.util.Log;
import com.stericson.RootTools.exceptions.RootDeniedException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.stericson.RootTools.RootTools;

/**
 * Created by aplex on 2018/3/17.
 */
public class CanUtils {

    private static String TAG = "CanUtils";

    private static String cmdList[] = {
            "ifconfig can0 down",
            "ip link set can0 type can bitrate 1000000",
            "ifconfig can0 up",
            "cd /system/bin/canopen",
            "./canopend can0 -i 4 -s od4_storage -a od4_storage_auto -c /tmp/CO_command_socket &"};


    public static void setCmdList(String can0bitrate, String nodeid){
        cmdList[1] = "ip link set can0 type can bitrate " +can0bitrate+ "  triple-sampling on";
        cmdList[4] = "./canopend can0 -i " +nodeid+ " -s od4_storage -a od4_storage_auto -c /tmp/CO_command_socket &";
        Log.d(TAG, "cmdList[1]="+cmdList[1]);
    }

    public static void startCanopen(){
        for (String cmd : cmdList) {
            Log.e(TAG, cmd);
            CanCommand command = new CanCommand(0,cmd);
            try {
                RootTools.getShell(true).add(command);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (RootDeniedException e) {
                e.printStackTrace();
            }
        }
    }

    static class CanCommand extends com.stericson.RootTools.execution.Command{

        public CanCommand(int id, String... command) {
            super(id, command);
        }

        @Override
        public void commandOutput(int i, String s) {
            //命令执行的过程中会执行此方法，line参数可用于调试
        }

        @Override
        public void commandTerminated(int i, String s) {
            //命令被取消后的执行此方法
        }

        @Override
        public void commandCompleted(int i, int i1) {
            //命令执行完成后会调用此方法
        }
    }
}

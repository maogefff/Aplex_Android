package com.aplex.canopenboot;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;

import com.aplex.canopenboot.utils.SPUtils;

public class RemoteService extends Service {
    boolean isOpen;
    Integer nodeID;
    Integer baudRate;

    class RemoteServiceInterface extends IRemoteService.Stub {

        @Override
        public int getDeviceBaudRate() throws RemoteException {
            return localGetDeviceBaudRate();
        }

        @Override
        public int getDeviceNodeID() throws RemoteException {
            return localGetDeviceNodeID();
        }

        @Override
        public boolean getCANopenStatus() throws RemoteException {
            return localGetCANopenStatus();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new RemoteServiceInterface();
    }

    private int localGetDeviceBaudRate(){
        baudRate = (Integer) SPUtils.getValue("baudRate", Integer.valueOf(0));
        return baudRate;
    }

    private int localGetDeviceNodeID(){
        nodeID = (Integer) SPUtils.getValue("nodeID", Integer.valueOf(0));
        return nodeID;
    }

    private boolean localGetCANopenStatus(){
        SharedPreferences sp = this.getSharedPreferences("mydata",0);
        isOpen = sp.getBoolean("isOpen", false);
        return isOpen;
    }
}

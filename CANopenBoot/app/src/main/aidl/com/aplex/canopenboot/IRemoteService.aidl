// IRemoteService.aidl
package com.aplex.canopenboot;

// Declare any non-default types here with import statements

interface IRemoteService {
    int getDeviceBaudRate();
    int getDeviceNodeID();
    boolean getCANopenStatus();
}

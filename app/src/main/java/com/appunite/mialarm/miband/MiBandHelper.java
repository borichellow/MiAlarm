package com.appunite.mialarm.miband;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.model.VibrationMode;

public class MiBandHelper {

    public static void wakeUpNeo(final MiBand miband) {
        if (miband.getDevice() == null) {
            MiBand.startScan(getScanCallback(miband));
        } else {
            miband.startVibration(VibrationMode.VIBRATION_10_TIMES_WITH_LED);
        }
    }

    private static ScanCallback getScanCallback(final MiBand miband) {
        final String TAG = "CYKA";

        return new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {

                //TODO hack because scanning doesn't stop
                if (miband.getDevice() != null)
                    return;

                final BluetoothDevice device = result.getDevice();
                int deviceClass = device.getBluetoothClass().getDeviceClass();
                Log.d(TAG,
                        "We found nearby Bluetooth devices : name :" + device.getName() +
                                ", uuid: " + device.getUuids() +
                                ", add: " + device.getAddress() +
                                ", type: " + device.getType() +
                                ", bondState: " + device.getBondState() +
                                ", rssi: " + result.getRssi() +
                                ", class: " + deviceClass);

                // TODO change it to MAC address checking after implementing device-sync
                if (device.getName() != null && device.getName().startsWith("MI") &&
                        (deviceClass == BluetoothClass.Device.Major.UNCATEGORIZED) ||
                        deviceClass == BluetoothClass.Device.Major.WEARABLE ||
                        deviceClass == BluetoothClass.Device.Major.HEALTH) {
                    MiBand.stopScan(this);
                    miband.connect(device, new ActionCallback() {

                        @Override
                        public void onSuccess(Object data) {
                            Log.d(TAG, "connect success");
                            miband.startVibration(VibrationMode.VIBRATION_WITHOUT_LED);
                        }

                        @Override
                        public void onFail(int errorCode, String msg) {
                            Log.d(TAG, "connect fail, code:" + errorCode + ",mgs:" + msg);
                        }
                    });
                }
            }

        };
    }
}

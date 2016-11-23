package com.appunite.mialarm.service;

import android.app.IntentService;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.model.VibrationMode;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

@Singleton
public class AlarmService extends IntentService {

    private static final String TAG = "CYKA";
    public static final String TIME_EXTRA = "time";
    private Executor es;
    private int time;
    private MiBand miband;

    public AlarmService() {
        super("AlarmService");
    }

    public static Intent newIntent(Context context, int time) {
        return new Intent(context, AlarmService.class).putExtra(TIME_EXTRA, time);
    }

//    public void onCreate() {
//        Log.d(TAG, "MyService onCreate");
//        es = Executors.newFixedThreadPool(1);
//        super.onCreate();
//    }
//
    /*
    * when service was runned by .startService()
    */
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MyService onStartCommand");
//        time = intent.getIntExtra(TIME_EXTRA, 1);
//        time = 17;
//        MyRun mr = new MyRun(time, startId);
//        es.execute(mr);
        return super.onStartCommand(intent, flags, startId);
    }
//
//    public void onDestroy() {
//        Log.d(TAG, "MyService onDestroy");
//        super.onDestroy();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "MyService onHandleIntent");
        time = intent.getIntExtra(TIME_EXTRA, 1);
        es = Executors.newFixedThreadPool(1);

        miband = new MiBand(AlarmService.this);

        final ScanCallback scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
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

                if (device.getName().startsWith("MI") &&
                        (deviceClass == BluetoothClass.Device.Major.UNCATEGORIZED) ||
                        deviceClass == BluetoothClass.Device.Major.WEARABLE ||
                        deviceClass == BluetoothClass.Device.Major.HEALTH) {
                    if (miband.getDevice() == null)
                        miband.connect(device, new ActionCallback() {

                            @Override
                            public void onSuccess(Object data) {
                                Log.d(TAG, "connect success");
//                                miband.startVibration(VibrationMode.VIBRATION_WITHOUT_LED);
                            }

                            @Override
                            public void onFail(int errorCode, String msg) {
                                Log.d(TAG, "connect fail, code:" + errorCode + ",mgs:" + msg);
                            }
                        });
                }
            }

        };
        MiBand.startScan(scanCallback);



        es.execute(new MyRun(time, 1, miband));
    }

    class MyRun implements Runnable {
        final int time;
        final int startId;
        final MiBand miBand;

        public MyRun(int time, int startId, MiBand miBand) {
            this.time = time;
            this.startId = startId;
            Log.d(TAG, "MyRun# " + startId + " create");
            this.miBand = miBand;
        }

        public void run() {
            Log.d(TAG, "MyRun# " + startId + " start, time = " + time);
            try {
                Log.d(TAG, "First sleep" );
                miBand.startVibration(VibrationMode.VIBRATION_WITHOUT_LED);
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Log.d(TAG, "MyRun# " + startId);
            } catch (NullPointerException e) {
                Log.d(TAG, "MyRun# " + startId + " error, null pointer");
            }

            try {
                Log.d(TAG, "Second sleep" );
                miBand.startVibration(VibrationMode.VIBRATION_WITHOUT_LED);
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop();
        }

        void stop() {
            Log.d(TAG, "MyRun# " + startId + " end, stopSelf(" + startId + ")");
            stopSelf(startId);
        }

    }
}

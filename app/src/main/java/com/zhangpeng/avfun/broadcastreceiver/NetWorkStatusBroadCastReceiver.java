package com.zhangpeng.avfun.broadcastreceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.widget.Toast;
/**
 * 监听网络状态变化
 */
public class NetWorkStatusBroadCastReceiver extends BroadcastReceiver {
    private NetWorkSuccessCallBack[] callBack;

    public NetWorkStatusBroadCastReceiver(NetWorkSuccessCallBack[] callBack) {
        this.callBack = callBack;
    }

    private static final String NETWORK_MESSAGE="当前网络不可用,请检查网络设置";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            //连接管理器
            ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
            //获取网络信息
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //获取网络信息
            if (networkInfo!=null){
                //网络连接上并且可用
                if (NetworkInfo.State.CONNECTED==networkInfo.getState()&&networkInfo.isAvailable()){
                    int type = networkInfo.getType();
                    switch (type){
                        //移动网络
                        case ConnectivityManager.TYPE_MOBILE:
                            for (NetWorkSuccessCallBack callBack:callBack){
                                callBack.callBack();
                            }
                            Toast.makeText(context,"正在使用移动网络,请注意流量!",Toast.LENGTH_SHORT).show();
                            break;
                        //wifi网络
                        case ConnectivityManager.TYPE_WIFI:
                            for (NetWorkSuccessCallBack callBack:callBack){
                                callBack.callBack();
                            }
                            break;
                        //网线连接
                        case ConnectivityManager.TYPE_ETHERNET:
                            //callBack.callBack();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        //检测WiFi状态
        if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_NEW_STATE, -1);
            switch (wifiState){
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
            }
        }
        //检测WiFi是否连接有效
        if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (parcelableExtra!=null){
               NetworkInfo networkInfo= (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
               boolean isConnected= state==NetworkInfo.State.CONNECTED;
               if (isConnected){
                   //WiFi网络连接可用
               }
            }
        }
    }
    public interface  NetWorkSuccessCallBack{
        void callBack();
    }
}
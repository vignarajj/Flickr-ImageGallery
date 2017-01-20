package com.flickr.imgallery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    boolean WIFI = false;


    public static int getConnectivityState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean getConnectivityStatus(Context context) {
        int conn = NetworkUtil.getConnectivityState(context);
        String status = null;
        boolean state = false;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
            state = true;
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
            state = true;
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
            state = false;
        }
        return state;
    }

    public static String getLocalIpAddress(Context context) {
        if (getConnectivityState(context) == TYPE_WIFI) {
            Log.d("state", "wifi");
            return GetDeviceipWiFiData(context);
        } else if (getConnectivityState(context) == TYPE_MOBILE) {
            Log.d("state", "mobile");
            return GetDeviceipMobileData();
        }
        return "192.168.1.2";
    }

    public static String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.d("Current IP", ex.toString());
        }
        return "192.168.1.2";
    }

    public static String GetDeviceipWiFiData(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

}
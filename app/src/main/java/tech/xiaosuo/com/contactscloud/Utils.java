package tech.xiaosuo.com.contactscloud;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import tech.xiaosuo.com.contactscloud.bmob.UserInfo;

public class Utils {

    private static final String TAG = "Utils";
    //after request sms code, we will wait 60s,to request it again
    public static final int REFRESH_SEND_SMS_CODE_TIMER = 1;
    public static final int ONE_MINUTE = 60;
    public static final int PHONE_MUNBER_LEN = 11;
    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isMobileDataConnected(Context context){
        boolean result = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            result = true;
        }
        return result;
    }

    public static boolean isWifiConnected(Context context){
        boolean result = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            result = true;
        }
        return result;
    }


    /**
     * check the phone number whether is correct.
     * @param phoneNumber
     * @return
     */
    public static boolean isValidPhoneNumber(String phoneNumber){
        if(phoneNumber == null || phoneNumber.length() != 11){
            return false;
        }
        return true;
    }

    /**
     * get current login user's info.
     * @return
     */
    public static UserInfo getCurrUserInfo(){
        UserInfo mBmobUser  = UserInfo.getCurrentUser(UserInfo.class);
        return  mBmobUser;
    }
    /**
     * get current login user's phone number.
     * @return
     */
    public static String getCurrUserPhoneNumber(){
        UserInfo mBmobUser  = UserInfo.getCurrentUser(UserInfo.class);
        String currPhoneNumber = mBmobUser.getMobilePhoneNumber();
        return  currPhoneNumber;
    }

    /**
     * modify the phonenumber ex:18612345678  --> 186****5678
     * @param phoneNumber
     * @return
     */
    public static  String modifyPhoneMiddleNumberUsingStar(String phoneNumber){

        String encryptPhoneNumber = null;
        int middleCount = 4;
        if(!Utils.isValidPhoneNumber(phoneNumber)){
            Log.d(TAG," modify phone middl number fail,params is null");
            return null ;
        }

        int len = phoneNumber.length();
        String begin3bit = phoneNumber.substring(0,middleCount - 1);
        String midle4bit = "****";
        String end4bit = phoneNumber.substring(2*middleCount - 1,len);
        Log.d(TAG," modify phone middl number begin3bit " + begin3bit + " end4bit " + end4bit);
        encryptPhoneNumber = begin3bit + midle4bit + end4bit;
        return encryptPhoneNumber;
    }
}

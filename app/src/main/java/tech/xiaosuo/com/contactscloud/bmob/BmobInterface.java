package tech.xiaosuo.com.contactscloud.bmob;


import android.util.Log;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import tech.xiaosuo.com.contactscloud.BaseActivity;
import tech.xiaosuo.com.contactscloud.R;
import tech.xiaosuo.com.contactscloud.Utils;


public  class BmobInterface {

    private static final String TAG = "BmobInterface";
    public static final int USER_NAME_EXIST = 202;
    public static final int PHONE_NUMBER_EXIST = 209;
    public static final int SMS_CODE_ERROR = 207;
    public static final int SMS_CODE_SEND_ERROR = 10010;
    public static final int SMS_CODE_SEND_FAIL_SERVER_NO_MSG_COUNT_ERROR = 10011;
    public static final int NOT_FOUNT = 10007;


/*    public static void setCallBackPresenter(CallBackPresenter callBackPresenter) {
        mCallBackPresenter = callBackPresenter;
    }*/

    /**
     * send sms code request to Bmob server
     * @param phone
     */
    public static void sendSmsCodeRequest(final BaseActivity context, final String phone){
        /**
         * TODO template 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；如果没有对应的自定义短信模板，则使用默认短信模板，默认模板名称为空字符串""。
         *
         * TODO 应用名称以及自定义短信内容，请使用不会被和谐的文字，防止发送短信验证码失败。
         *
         */

        if(phone == null){
            Log.d(TAG," sendSmsCodeRequest fail,params is null,return ");
            return ;
        }
        BmobSMS.requestSMSCode(phone, "相册云短信模板", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    Log.d(TAG,"send sms code request success " + smsId + "\n");
                    String phoneNumberWithStar = Utils.modifyPhoneMiddleNumberUsingStar(phone);
                    Toast.makeText(context,context.getResources().getString(R.string.sms_code_has_send_to) + phoneNumberWithStar, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG,"send sms code request fail " + e.getErrorCode() + "-" + e.getMessage());
                    context.showErrorCode(e.getErrorCode());
                  //  Toast.makeText(context,context.getResources().getString(R.string.sms_code_request_fail) + e.getErrorCode() + "-" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * send sms code request to bmob server
     *
     * @param smsCode
     * @param newPassword
     */
    public static void resetPassword(final BaseActivity context , String smsCode, String newPassword){

        if(smsCode == null || newPassword == null){
            Log.d(TAG,"reset password fail,params is null,return ");
            return ;
        }
        context.showProgress(true,-1);
        BmobUser.resetPasswordBySMSCode(smsCode, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                context.showProgress(false,-1);
                if (e == null) {
                   // mTvInfo.append("重置成功");
                    Log.d(TAG,"reset password success ");
                  //  Toast.makeText(context,context.getResources().getString(R.string.reset_pwd_success),Toast.LENGTH_SHORT).show();
/*                    if(mCallBackPresenter != null){
                        mCallBackPresenter.resetPasswordDialog(true);
                    }*/
                    context.resetPasswordDialog(true,0);
                } else {
                    Log.d(TAG,"reset password fail " + e.getErrorCode() + "-" + e.getMessage() );
                  //  Toast.makeText(context,context.getResources().getString(R.string.reset_pwd_fail) + e.getErrorCode() + "-" + e.getMessage() ,Toast.LENGTH_SHORT).show();
                    context.showErrorCode(e.getErrorCode());
/*                    if(mCallBackPresenter != null){
                        mCallBackPresenter.resetPasswordDialog(false);
                    }*/
                }
            }
        });
    }

    /**
     * replace phone number for user.
     * @param phone
     * @param code
     */
    public static void replacePhoneNumber(final BaseActivity context , final String phone, String code){

         if(!Utils.isValidPhoneNumber(phone) || code == null){
             Log.d(TAG," the phone number or code is invalid ");
/*             if(mCallBackPresenter != null){
                 mCallBackPresenter.replacePhoneNumberDialog(false);
             }*/
            return;
         }
        BmobSMS.verifySmsCode(phone, code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                 //   mTvInfo.append("验证码验证成功，您可以在此时进行绑定操作！\n");
                    Log.d(TAG,"the sms code is correct ,now bind/unbind the phone number ");
                    UserInfo user = Utils.getCurrUserInfo();
                    user.setUsername(null);

                    user.setMobilePhoneNumber(phone);
                    //绑定
                    user.setMobilePhoneNumberVerified(true);
                    //解绑
                    //user.setMobilePhoneNumberVerified(false);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.d(TAG,"bind/unbind phone number success ");
/*                                if(mCallBackPresenter != null){
                                    mCallBackPresenter.replacePhoneNumberDialog(true);
                                }*/
                                context.replacePhoneNumberDialog(true,0);
                              //  mTvInfo.append("绑定/解绑手机号码成功");
                            } else {
                              //  Toast.makeText(context,context.getResources().getString(R.string.replace_phone_number_fail) + e.getErrorCode() + "-" + e.getMessage(),Toast.LENGTH_SHORT).show();
/*                                if(mCallBackPresenter != null){
                                    mCallBackPresenter.replacePhoneNumber(false,true);
                                }*/
                                //context.replacePhoneNumberDialog(false,e.getErrorCode());
                                context.showErrorCode(e.getErrorCode());
                                Log.d(TAG,"bind/unbind phone number fail "  + e.getErrorCode() + "-" + e.getMessage());
                               // mTvInfo.append("绑定/解绑手机号码失败：" + e.getErrorCode() + "-" + e.getMessage());
                            }
                        }
                    });
                } else {
                    context.showErrorCode(e.getErrorCode());
                  //  Toast.makeText(context,context.getResources().getString(R.string.sms_code_verify_fail) + e.getErrorCode() + "-" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.d(TAG," the sms code verify fail "  + e.getErrorCode() + "-" + e.getMessage());
                   // mTvInfo.append("验证码验证失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                }
            }
        });

    }
/*    public interface CallBackPresenter{
         public void resetPasswordDialog(boolean result);
         public void replacePhoneNumberDialog(boolean result);
      //   public void showErrorDialog();
    }*/




    /*private static void fetchUserInfoFromBomb(){
        *//**
         * 同步控制台数据到缓存中
         * @param view
         *//*
            BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
                @Override
                public void done(BmobUser user, BmobException e) {
                    if (e == null) {
                        final MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
                        Snackbar.make(view, "更新用户本地缓存信息成功："+myUser.getUsername()+"-"+myUser.getAge(), Snackbar.LENGTH_LONG).show();
                    } else {
                        Log.e("error",e.getMessage());
                        Snackbar.make(view, "更新用户本地缓存信息失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });

    }*/
}

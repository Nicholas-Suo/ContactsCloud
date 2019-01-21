package tech.xiaosuo.com.contactscloud;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import tech.xiaosuo.com.contactscloud.bmob.BmobInterface;
import tech.xiaosuo.com.contactscloud.bmob.UserInfo;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    private ProgressDialog progressDialog;
    private Button requestSmsCodeButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base);
/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    //show the app error string.
    protected  void showErrorCode(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title).setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //show the bmob server error code.
    public  void showErrorCode(int err_code){
        showError(this,err_code);
    }
    /**
     * show the error code from the bmob web server.
     * @param err_code
     */
    protected  void showError(Activity activity, int err_code){
        String messageStr = Integer.toString(err_code);

        if(activity == null){
            return;
        }

        switch (err_code){

            case BmobInterface.SMS_CODE_ERROR:
                messageStr = activity.getString(R.string.sms_code_err);
                break;
            case BmobInterface.SMS_CODE_SEND_ERROR:
                messageStr = activity.getString(R.string.sms_code_request_fail);
                break;
            case BmobInterface.SMS_CODE_SEND_FAIL_SERVER_NO_MSG_COUNT_ERROR:
                messageStr = activity.getString(R.string.pls_contact_customer_service);
                break;
            case BmobInterface.USER_NAME_EXIST:
                messageStr = activity.getString(R.string.nickname_exist);
                break;
            case BmobInterface.NOT_FOUNT:
                messageStr = activity.getString(R.string.phonenumber_no_registered);
                break;

            default:
                messageStr = Integer.toString(err_code);
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_title).setMessage(messageStr).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * show the replacePhoneNumberDialog result.
     * @param result
     * @param err_code  :from Bmob web server
     */
    public void replacePhoneNumberDialog(boolean result, int err_code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(result){
            builder.setTitle(R.string.replace_phonenumber).setMessage(R.string.replace_phone_number_success).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
           // UserInfo userInfo = UserInfo.getCurrentUser(UserInfo.class);
          //  Log.d(TAG," the new number is: " +userInfo.getMobilePhoneNumber());
        }else{
            showError(this,err_code);
        }

    }

    /**
     * show the result dialog for reset password
     * @param result
     * @param err_code from Bmob web server
     */
    public void resetPasswordDialog(boolean result,int err_code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(result == true){
            builder.setTitle(R.string.reset_password).setMessage(R.string.reset_pwd_success).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            showError(this,err_code);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG," onSupportNavigateUp ");
        finish();
        return super.onSupportNavigateUp();
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show,int stringId) {

        if(!show && progressDialog != null){
            progressDialog.cancel();
            progressDialog = null;
            return;
        }

        if(show){
            if(stringId == -1){
                stringId = R.string.wating;
            }
            if(progressDialog == null){
                progressDialog = new ProgressDialog(this);
            }
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(stringId));
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.cancel();
            progressDialog = null;
        }
        requestSmsCodeButton = null;
        basseHandler.removeMessages(Utils.REFRESH_SEND_SMS_CODE_TIMER);
    }

    /**
     * begin send msg to refresh the Button : "Request sms code button" per 1s.
     * @param second
     */
    protected void requestSmsCodeAgainTimer(Button view,int second){
        Log.d(TAG," the left time is " + second + " second" + " view: " +view);
        if(view == null){
            Log.d(TAG," the param: view is null,return");
             return;
        }
        requestSmsCodeButton = view;
        updateRequestSmsCodeButtonText(view,second);
        if(second < 0 || second > Utils.ONE_MINUTE){
            requestSmsCodeButton = null;
            return;
        }
        Message msg = new Message();
        msg.what = Utils.REFRESH_SEND_SMS_CODE_TIMER;
        msg.arg1 = second;
        basseHandler.sendMessageDelayed(msg,1000);
    }

    Handler basseHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case Utils.REFRESH_SEND_SMS_CODE_TIMER:
                    int second = msg.arg1;
                    if(second > 0){
                        second--;
                        requestSmsCodeAgainTimer(requestSmsCodeButton,second);
                    }
                    break;
                default:
                    break;
            }
        }
    };




    /**
     * update the reqest sms code button's text
     * @param second
     */
    @SuppressLint("StringFormatInvalid")
    private void updateRequestSmsCodeButtonText(Button view , int second){
        if(second <= 0){
            view.setText(R.string.request_sms_code);
            view.setEnabled(true);
            requestSmsCodeButton = null;
            return;
        }
        view.setEnabled(false);
        String timerStr = getString(R.string.request_sms_code_agian,String.valueOf(second));
        view.setText(timerStr);
    }



}

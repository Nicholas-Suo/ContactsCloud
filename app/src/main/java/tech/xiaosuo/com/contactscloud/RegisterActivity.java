package tech.xiaosuo.com.contactscloud;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import tech.xiaosuo.com.contactscloud.bmob.BmobInterface;
import tech.xiaosuo.com.contactscloud.bmob.UserInfo;


//import tech.xiaosuo.com.bmob.http.HttpClientUtils;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    // UI references.
    private AutoCompleteTextView mNickName;
    private EditText mTelNumber;
    private EditText mPasswordView;
    private EditText mRepeatPasswordView;
    private ProgressDialog progressDialog;
    private Context mContext;
    private Button mRquestSmsCodeButton;
    private EditText mSmsCodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mContext = getApplicationContext();
        // Set up the login form.
        mNickName = (AutoCompleteTextView) findViewById(R.id.nick_name);

        mTelNumber = (EditText) findViewById(R.id.telnumber);

        mPasswordView = (EditText) findViewById(R.id.password);
        mRepeatPasswordView = (EditText) findViewById(R.id.repeat_password);
        mRepeatPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Log.d(TAG," wangsm register action option");
                if (id == R.id.login || id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_DONE){
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        mRquestSmsCodeButton = (Button) findViewById(R.id.request_register_sms_code_button);
        mRquestSmsCodeButton.setOnClickListener(this);
        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(this);

        mSmsCodeView = (EditText) findViewById(R.id.register_sms_code_view);

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if(!Utils.isNetworkConnected(this)){
            Toast.makeText(this,R.string.pls_connect_network, Toast.LENGTH_SHORT).show();
            return;
        }
        // Reset errors.

        mTelNumber.setError(null);
        mPasswordView.setError(null);
        mRepeatPasswordView.setError(null);
        mSmsCodeView.setError(null);

        // Store values at the time of the login attempt.
        final String nickname = mNickName.getText().toString();
        final String telnumber = mTelNumber.getText().toString();
        final String password = mPasswordView.getText().toString();
        String rePassword = mRepeatPasswordView.getText().toString();
        String code = mSmsCodeView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }else if (!TextUtils.isEmpty(rePassword) && !isPasswordValid(rePassword)) {
            mRepeatPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mRepeatPasswordView;
            cancel = true;
        }else if (!rePassword.equals(password)) {
            mRepeatPasswordView.setError(getString(R.string.password_not_equal));
            focusView = mRepeatPasswordView;
            cancel = true;
        }

        // Check for a valid tel address.
        if (TextUtils.isEmpty(telnumber) || (telnumber.length() != 11)) {
            mTelNumber.setError(getString(R.string.error_invalid_phonenumber));
            focusView = mTelNumber;
            cancel = true;
        }
        // Check for a valid tel address.
        if (TextUtils.isEmpty(nickname)) {
            mNickName.setError(getString(R.string.error_field_required));
            focusView = mNickName;
            cancel = true;
        }

        // Check sms code whether invalid.
        if (TextUtils.isEmpty(code)) {
            mSmsCodeView.setError(getString(R.string.error_field_required));
            focusView = mSmsCodeView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
           // BmobSMS.verifySmsCodeObservable(telnumber,code);
            BmobSMS.verifySmsCode(telnumber, code, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //   mTvInfo.append("验证码验证成功，您可以在此时进行绑定操作！\n");
                        Log.d(TAG,"the sms code is correct ,now bind/unbind the phone number ");
                        UserInfo user = new UserInfo();
                        user.setUsername(nickname);
                        user.setMobilePhoneNumber(telnumber);
                        user.setMobilePhoneNumberVerified(true);
                        user.setPassword(password);
                        user.signUp(new SaveListener<UserInfo>() {
                            @Override
                            public void done(UserInfo s, BmobException e) {
                                showProgress(false);
                                if (e==null) {
                                    Log.d(TAG," register success " + s.toString());
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle(R.string.dialog_title).setMessage(R.string.register_sucess).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    }).show();
                                }else{

                                    Log.d(TAG," register fail " + e.toString());
                                    int errorCode = e.getErrorCode();
                                    switch (errorCode){
                                        case BmobInterface.USER_NAME_EXIST:
                                            mNickName.setError(getString(R.string.nickname_exist));
                                            mNickName.requestFocus();
                                            break;

                                        case BmobInterface.PHONE_NUMBER_EXIST:
                                            mTelNumber.setError(getString(R.string.telnumber_exist));
                                            mTelNumber.requestFocus();
                                            break;
                                        default:
                                            showErrorCode(errorCode);
                                           // Toast.makeText(mContext,e.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
                           /* UserInfo user = Utils.getCurrUserInfo();
                            user.setUsername(null);
                            user.setMobilePhoneNumber(phone);
                            //绑定
                            user.setMobilePhoneNumberVerified(true);
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Log.d(TAG,"bind/unbind phone number success ");
                                        context.replacePhoneNumberDialog(true,0);
                                        //  mTvInfo.append("绑定/解绑手机号码成功");
                                    } else {
                                        showErrorCode(e.getErrorCode());
                                        Log.d(TAG,"bind/unbind phone number fail "  + e.getErrorCode() + "-" + e.getMessage());
                                    }
                                }
                            });*/
                    } else {
                        showProgress(false);
                        showErrorCode(e.getErrorCode());
                        //  Toast.makeText(context,context.getResources().getString(R.string.sms_code_verify_fail) + e.getErrorCode() + "-" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d(TAG," the sms code verify fail "  + e.getErrorCode() + "-" + e.getMessage());
                        // mTvInfo.append("验证码验证失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                    }
                }
            });
        }
    }



    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
        }
        if(show){
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.register_progress_message));
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.show();
        }else{
            progressDialog.cancel();
            progressDialog = null;
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.register_button:
               // finish();
                attemptRegister();
                break;
            case R.id.request_register_sms_code_button:
                if(mTelNumber==null || mTelNumber.getText() == null || mTelNumber.length() == 0){
                    Log.d(TAG," the phone number is null,return");
                    Toast.makeText(mContext,R.string.error_invalid_phonenumber,Toast.LENGTH_SHORT).show();
                    return;
                }
                String phoneNumber = mTelNumber.getText().toString();
                boolean isValidPhoneNumber = Utils.isValidPhoneNumber(phoneNumber);
                if(isValidPhoneNumber){
                    requestSmsCodeAgainTimer(Utils.ONE_MINUTE);
                   // BmobInterface.sendSmsCodeRequest(this,phoneNumber);
                }else{
                    Toast.makeText(mContext,R.string.error_invalid_phonenumber,Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    /**
     * begin send msg to refresh the Button : "Request sms code button" per 1s.
     * @param second
     */
    private void requestSmsCodeAgainTimer(int second){
        Log.d(TAG," the left time is " + second + " second");
        updateRequestSmsCodeButtonText(second);
        if(second < 0 || second > Utils.ONE_MINUTE){
            return;
        }
        Message msg = new Message();
        msg.what = Utils.REFRESH_SEND_SMS_CODE_TIMER;
        msg.arg1 = second;
        registerHandler.sendMessageDelayed(msg,1000);
    }

    Handler registerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case Utils.REFRESH_SEND_SMS_CODE_TIMER:
                    int second = msg.arg1;
                    if(second > 0){
                        second--;
                        requestSmsCodeAgainTimer(second);
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
    private void updateRequestSmsCodeButtonText(int second){
        if(second <= 0){
            mRquestSmsCodeButton.setText(R.string.request_sms_code);
            mRquestSmsCodeButton.setEnabled(true);
            return;
        }
        mRquestSmsCodeButton.setEnabled(false);
        String timerStr = getString(R.string.request_sms_code_agian,String.valueOf(second));
        mRquestSmsCodeButton.setText(timerStr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerHandler.removeMessages(Utils.REFRESH_SEND_SMS_CODE_TIMER);
    }
}


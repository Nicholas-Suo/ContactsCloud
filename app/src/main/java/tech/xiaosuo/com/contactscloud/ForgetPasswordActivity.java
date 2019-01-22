package tech.xiaosuo.com.contactscloud;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import tech.xiaosuo.com.contactscloud.bmob.BmobInterface;
import tech.xiaosuo.com.contactscloud.bmob.UserInfo;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener { //implements LoaderCallbacks<Cursor>

    private static final String TAG = "ForgetPasswordActivity";

    // UI references.
    private EditText phoneNumberView;
    private EditText smsCodeVew;
    private EditText passwordView;
    private Button requesSmsCodeView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mContext = getApplicationContext();
        ActionBar actionBar = getSupportActionBar();
       // actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        phoneNumberView = (EditText) findViewById(R.id.phone_number);
        passwordView = (EditText)findViewById(R.id.new_password_fgt_pwd);
        smsCodeVew = (EditText) findViewById(R.id.forget_pwd_sms_code_view);
        requesSmsCodeView = (Button) findViewById(R.id.request_forget_pwd_sms_code_button);
        requesSmsCodeView.setOnClickListener(this);

        Button mUserSignInButton = (Button) findViewById(R.id.forget_pwd_verify_button);
        mUserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptVerify();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual.
     */
    private void attemptVerify() {

        // Reset errors.
        phoneNumberView.setError(null);
        smsCodeVew.setError(null);
        passwordView.setError(null);
        // Store values at the time of the login attempt.
        String phoneNumber = phoneNumberView.getText().toString();
        String smsCode = smsCodeVew.getText().toString();
        String newPwd = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(smsCode) || smsCode.length() < 4) {
            smsCodeVew.setError(getString(R.string.verify_code_invalid));
            focusView = smsCodeVew;
            cancel = true;
        }

        //check password
        if(TextUtils.isEmpty(newPwd) || newPwd.length() < 6){
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }


        // Check for a valid phone number
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberView.setError(getString(R.string.error_field_required));
            focusView = phoneNumberView;
            cancel = true;
        } else if (!Utils.isValidPhoneNumber(phoneNumber)) {
            phoneNumberView.setError(getString(R.string.error_invalid_phonenumber));
            focusView = phoneNumberView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            BmobInterface.resetPassword(this,smsCode,newPwd);
        }
    }


    @Override
    public void onClick(View v) {
         int id = v.getId();
         if(id == R.id.request_forget_pwd_sms_code_button){

             // Check for a valid phone number
             boolean cancel = false;
             String phoneNumber = phoneNumberView.getText().toString();
             if (TextUtils.isEmpty(phoneNumber)) {
                 phoneNumberView.setError(getString(R.string.error_field_required));
                 cancel = true;
             } else if (!Utils.isValidPhoneNumber(phoneNumber)) {
                 phoneNumberView.setError(getString(R.string.error_invalid_phonenumber));
                 cancel = true;
             }
             if (cancel) {
                 phoneNumberView.requestFocus();
                 return;
             }
             smsCodeVew.requestFocus();
             requestSmsCodeAgainTimer(requesSmsCodeView,Utils.ONE_MINUTE);
             BmobInterface.sendSmsCodeRequest(ForgetPasswordActivity.this,phoneNumber);
            // isRegisteredPhone(phoneNumber);
         }
    }

    /**
     * check the phone number whether is registered,
     * if not,do not request sms code.
     * if yes,request sms code.
     * forgetpassword only using for registered user's phone number.
     * @return
     */
    private void isRegisteredPhone(final String phoneNumber){
        final boolean[] result ={false};
        if(!Utils.isValidPhoneNumber(phoneNumber)){
            Log.d(TAG," the phone number is invalid,return false");
            return;
        }
        smsCodeVew.requestFocus();
        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("mobilePhoneNumber",phoneNumber);
        bmobQuery.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if(e == null){
                    if(list != null && list.size() > 0){
                        Log.d(TAG,"the phonnumber: "+ phoneNumber +" is registerd.then request the sms code");
                        BmobInterface.sendSmsCodeRequest(ForgetPasswordActivity.this,phoneNumber);
                    }else{
                        Log.d(TAG,"the phonnumber: "+ phoneNumber +" is not registerd.show error");
                        showErrorCode(getString(R.string.phonenumber_no_registered));
                    }
                }else{
                    Log.d(TAG," isRegisteredPhone check exception" +  e.getErrorCode() + " " + e.getMessage());
                    showErrorCode(e.getErrorCode());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


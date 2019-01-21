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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import tech.xiaosuo.com.contactscloud.bmob.UserInfo;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity  { //implements LoaderCallbacks<Cursor>

    private static final String TAG = "LoginActivity";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };


    // UI references.
    private EditText mUserNameOrPhoneNumberView;
    private EditText mPasswordView;
    private TextView registerView;
    private TextView forgetPasswodView;
    private Context mContext;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        ActionBar actionBar = getSupportActionBar();
       // actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
       //register textView
        registerView = (TextView)findViewById(R.id.register_textview);
        String registerStr = getString(R.string.register);
        int registerLen = registerStr.length();
        SpannableString registerSpanStr = new SpannableString(registerStr);
        registerSpanStr.setSpan(new UnderlineSpan(),0,registerLen,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        registerSpanStr.setSpan(new ForegroundColorSpan(Color.RED),0,registerLen,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        registerSpanStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
               Log.d(TAG," open the register activity");
               Intent intent = new Intent(mContext,RegisterActivity.class);
               startActivity(intent);
               finish();
            }
        },0,registerLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerView.setMovementMethod(LinkMovementMethod.getInstance());
        registerView.setText(registerSpanStr);

        //forget password view
        forgetPasswodView = (TextView)findViewById(R.id.forget_password_textview);
        String forgetPwdStr = getString(R.string.forget_password);
        int fPwdLen = forgetPwdStr.length();
        SpannableString  forgetPwdSpanStr = new SpannableString(forgetPwdStr);
        forgetPwdSpanStr.setSpan(new UnderlineSpan(),0,fPwdLen,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        forgetPwdSpanStr.setSpan(new ForegroundColorSpan(Color.RED),0,fPwdLen,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        forgetPwdSpanStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                  Log.d(TAG, " open the forget pwd activity");
            }
        },0,fPwdLen,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgetPasswodView.setMovementMethod(LinkMovementMethod.getInstance());
        forgetPasswodView.setText(forgetPwdSpanStr);
        // Set up the login form.
        mUserNameOrPhoneNumberView = (EditText) findViewById(R.id.username_or_phone_number);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mUserSignInButton = (Button) findViewById(R.id.user_sign_in_button);
        mUserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

/*    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }*/

/*    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
*//*            Snackbar.make(mPhoneNumberView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });*//*
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }*/

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUserNameOrPhoneNumberView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phoneNumber = mUserNameOrPhoneNumberView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phoneNumber)) {
            mUserNameOrPhoneNumberView.setError(getString(R.string.error_field_required));
            focusView = mUserNameOrPhoneNumberView;
            cancel = true;
        } else if (!isPhoneValid(phoneNumber)) {
            mUserNameOrPhoneNumberView.setError(getString(R.string.error_invalid_phonenumber));
            focusView = mUserNameOrPhoneNumberView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            showProgress(true);
            LoginByPassword(phoneNumber,password);

        }
    }

    private boolean isPhoneValid(String phoneNumber) {
        //TODO: Replace this with your own logic
        if(phoneNumber == null || phoneNumber.length() != 11){
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }




    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG," onSupportNavigateUp ");
        finish();
        return super.onSupportNavigateUp();
    }


    /**
     * login using username + password
     * @param username
     * @param password
     */
    private void LoginByPassword( String username, String password){

        if(username == null || password == null){
            Log.d(TAG," username or password is null.return");
           return;
        }

        BmobUser.loginByAccount(username, password, new LogInListener<UserInfo>() {

            @Override
            public void done(UserInfo user, BmobException e) {
                showProgress(false);
                if(user!=null){
                    Log.d(TAG,"login success");
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    Log.d(TAG,"login fail " + e.toString());
                    showErrorCode(e.getErrorCode());
/*                    new AlertDialog.Builder(mActivity).setTitle(R.string.dialog_title).setMessage(e.toString()).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();*/
                }
            }
        });
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
        }
        if(show){
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.is_logining));
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.show();
        }else{
            progressDialog.cancel();
            progressDialog = null;
        }
    }
}


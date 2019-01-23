package tech.xiaosuo.com.contactscloud;

import android.annotation.SuppressLint;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import tech.xiaosuo.com.contactscloud.bmob.BmobInterface;


public class ReplacePhoneNumberActivity extends BaseActivity implements View.OnClickListener {// ,BmobInterface.CallBackPresenter

    private static final String TAG="ReplacePhoneNumber";
    TextView currNumberView;
    String currPhoneNumber;
    EditText newNumberView;
    Button reqSmsCodeButton;
    EditText smsCodeView;
    Button submitButton;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_phone_number);
        ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mContext = getApplicationContext();
     //   BmobInterface.setCallBackPresenter(this);
        initView();
    }

    /**
     *
     */
    private void initView(){
        //curr login user'phone
        currNumberView = (TextView)findViewById(R.id.replace_pnum_curr_num_view);
        currPhoneNumber = Utils.getCurrUserPhoneNumber();
        String starPhoneNumber = Utils.modifyPhoneMiddleNumberUsingStar(currPhoneNumber);
        currNumberView.setText(starPhoneNumber);


        newNumberView = (EditText)findViewById(R.id.replace_pnum_new_phonenumber_view);
        newNumberView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                     if(s == null || s.length() != Utils.PHONE_MUNBER_LEN){
                         submitButton.setEnabled(false);
                         reqSmsCodeButton.setEnabled(false);
                     }else{
                         submitButton.setEnabled(true);
                         reqSmsCodeButton.setEnabled(true);
                     }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reqSmsCodeButton = (Button)findViewById(R.id.replace_pnum_request_smscode_button);
        reqSmsCodeButton.setOnClickListener(this);

        smsCodeView = (EditText)findViewById(R.id.replace_pnum_sms_code_view);


        submitButton = (Button)findViewById(R.id.replace_pnum_submit_button);
        submitButton.setOnClickListener(this);
        submitButton.setEnabled(false);
        reqSmsCodeButton.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        String newPhoneNumber = newNumberView.getText().toString();
        Log.d(TAG," newPhoneNumber: " + newPhoneNumber);
        switch (id){
            case R.id.replace_pnum_request_smscode_button:
                if(Utils.isValidPhoneNumber(currPhoneNumber)){
                    smsCodeView.requestFocus();
                    BmobInterface.sendSmsCodeRequest(this,newPhoneNumber);
                    requestSmsCodeAgainTimer(reqSmsCodeButton,Utils.ONE_MINUTE);
                }
                break;
            case R.id.replace_pnum_submit_button:
                Log.d(TAG,"bind/unbind phone number clic ");
                if(!Utils.isValidPhoneNumber(newPhoneNumber)){
                    Log.d(TAG," the new phone number is not valide");
                    newNumberView.setError(getString(R.string.error_invalid_phonenumber));
                     return;
                }
                String smsCode = smsCodeView.getText().toString();
                if(smsCodeView != null && !TextUtils.isEmpty(smsCode)){
                    Log.d(TAG,"bind/unbind phone number start ");
                    BmobInterface.replacePhoneNumber(this,smsCode,newPhoneNumber);//currPhoneNumber,
                }
                break;
                default:
                    break;
        }
    }

}

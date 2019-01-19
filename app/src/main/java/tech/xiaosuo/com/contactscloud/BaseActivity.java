package tech.xiaosuo.com.contactscloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tech.xiaosuo.com.contactscloud.bmob.BmobInterface;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
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

    public  void showErrorCode(int err_code){
        showError(this,err_code);
    }
    /**
     * show the error code from the bmob web server.
     * @param err_code
     */
    public static void showError(Activity activity, int err_code){
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

}

package in.aspiretechnovations.aml.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.response.OtpResponse;
import in.aspiretechnovations.aml.response.UserResponse;
import in.aspiretechnovations.aml.retrofit.ApiClient;
import in.aspiretechnovations.aml.retrofit.ApiInterface;
import in.aspiretechnovations.aml.utils.AppPref;
import in.aspiretechnovations.aml.utils.CommonUtils;
import in.aspiretechnovations.aml.utils.ConnectionDetector;
import in.aspiretechnovations.aml.utils.ConstantUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {

    private Context context;
    private Button btn_verify;
    private String otp;
    private String mobile_no;
    private TextView tv_mobile_no,tv_otp;
    private EditText et_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        context = this;
        btn_verify = findViewById(R.id.btn_verify);
        tv_mobile_no = findViewById(R.id.tv_mobile_no);
        et_otp = findViewById(R.id.et_otp);
        tv_otp = findViewById(R.id.tv_otp);


        otp = getIntent().getExtras().getString("otp");
        mobile_no = getIntent().getExtras().getString("mobile_no");

        tv_otp.setText(otp);
        tv_mobile_no.setText("+91 -"+mobile_no);





        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(context,LocationSelectionActivity.class));

                if (et_otp.getText().toString().contentEquals(""))
                {
                    et_otp.setError(getString(R.string.et_error));
                }else {

                    if (et_otp.getText().toString().trim().contentEquals(otp))
                    {

                       if (ConnectionDetector.isInternetConnected(context))
                           networkRegister(mobile_no);
                       else
                           CommonUtils.showToast(context,getString(R.string.no_internet));

                    }else
                    {
                        CommonUtils.showToast(context,"Invalid OTP");
                    }

                }
            }
        });
    }


    private void networkRegister(final String mobile_no ) {


//        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(context,"Loading");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Map<String,String> map = new HashMap<>();
        map.put(ConstantUtils.KEY_MOBILE,  mobile_no);




        Call<UserResponse> call = apiService.register_by_mobile(map);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {


                //Todo: add response null checker

                //  CommonUtils.cancelProgressDialog(progressDialog);
                if (response.code() == 200) {

                    if (response != null) {

                        String code = response.body().getCode();
                        String message = response.body().getMessage();

                        if (code.contentEquals(ConstantUtils.SUCESS_CODE)){
/*

                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.cancel();
                            }
*/

                            // networkUpdateFcm(context,ConstantUtils.USER_TYPE_KITCHEN);

                            AppPref.setLoggedIn(context, true);
                            AppPref.saveInfo(context,response.body().getUserModel());

                            startActivity(new Intent(context, HomeActivity.class));


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finishAffinity();
                            }else
                                finish();


                        }else{
                            CommonUtils.showToast(context,message);


                        }
                    }else {

                        CommonUtils.showToast(context,getString(R.string.er_server));
                    }

                }else{
                    CommonUtils.showToast(context,getString(R.string.er_server));

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

                //     CommonUtils.cancelProgressDialog(progressDialog);
                t.printStackTrace();

            }
        });
    }

}

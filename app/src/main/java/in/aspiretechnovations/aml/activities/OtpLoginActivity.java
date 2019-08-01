package in.aspiretechnovations.aml.activities;

import androidx.appcompat.app.AppCompatActivity;
import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.response.OtpResponse;
import in.aspiretechnovations.aml.retrofit.ApiClient;
import in.aspiretechnovations.aml.retrofit.ApiInterface;
import in.aspiretechnovations.aml.utils.AppPref;
import in.aspiretechnovations.aml.utils.CommonUtils;
import in.aspiretechnovations.aml.utils.ConnectionDetector;
import in.aspiretechnovations.aml.utils.ConstantUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class OtpLoginActivity extends AppCompatActivity {

    private Button btn_login;
    private Context context;
    private EditText et_mobile_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        btn_login = findViewById(R.id.btn_login);
        et_mobile_no = findViewById(R.id.et_mobile_no);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // startActivity(new Intent(context,VerificationActivity.class));

                String mobile_no = et_mobile_no.getText().toString().trim();

                if (mobile_no.contentEquals(""))
                {
                    et_mobile_no.setError(getString(R.string.et_error));
                }else
                {

                    if(ConnectionDetector.isInternetConnected(context))
                        networkOtp(mobile_no);
                    else
                        CommonUtils.showToast(context,getString(R.string.no_internet));

                }


            }
        });



    }


    private void networkOtp(final String mobile_no ) {


    final ProgressDialog progressDialog = CommonUtils.showProgressDialog(context,"Loading");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Map<String,String> map = new HashMap<>();
        map.put(ConstantUtils.KEY_MOBILE,  mobile_no);




        Call<OtpResponse> call = apiService.sendOtp(map);

        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {


                //Todo: add response null checker

               CommonUtils.cancelProgressDialog(progressDialog);
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

                            startActivity(new Intent(context, VerificationActivity.class).putExtra("otp",response.body().getOtpModel().getOtp()).putExtra("mobile_no",mobile_no));





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
            public void onFailure(Call<OtpResponse> call, Throwable t) {

            CommonUtils.cancelProgressDialog(progressDialog);
                t.printStackTrace();

            }
        });
    }
}

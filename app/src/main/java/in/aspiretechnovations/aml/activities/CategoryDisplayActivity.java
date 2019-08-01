package in.aspiretechnovations.aml.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.adapters.CategoryAdapter;
import in.aspiretechnovations.aml.response.CategoryResponse;
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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class CategoryDisplayActivity extends AppCompatActivity {

    private int type  = 0;
    private ProgressDialog progressDialog;
    private Context context;

    private RecyclerView rv_category;
    private ImageView im_cart;
    private TextView tv_change, tv_vehicle_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_display);
        context = this;

        type = getIntent().getExtras().getInt("type");

        rv_category = findViewById(R.id.rv_category);
        rv_category.setLayoutManager(new GridLayoutManager(context,3));
        tv_change  = findViewById(R.id.tv_change);
        tv_vehicle_name = findViewById(R.id.tv_vehicle_name);


        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context,ServiceFilterActivity.class));
                finish();
            }
        });

        tv_vehicle_name.setText(AppPref.getSelectedMakerName(context)+" "+AppPref.getKeyModelName(context)+" Components");
        if (ConnectionDetector.isInternetConnected(context))
            networkCategories();
        else
            CommonUtils.showToast(context,getString(R.string.no_internet));
        im_cart = findViewById(R.id.im_cart);
        im_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context,CheckOutPageActivity.class));
            }
        });
        EditText et_search;
        et_search  = findViewById(R.id.et_search);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, SearchActivity.class));
            }
        });

    }




    private void networkCategories( ) {


          progressDialog = CommonUtils.showProgressDialog(context,"Loading");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Map<String,String> map = new HashMap<>();
        map.put(ConstantUtils.VEHICLE_TYPE,  String.valueOf(type));


        Call<CategoryResponse> call = apiService.get_categories(map);

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {


                //Todo: add response null checker

               CommonUtils.cancelProgressDialog(progressDialog);
                if (response.code() == 200) {

                    if (response != null) {

                        String code = response.body().getCode();
                        String message = response.body().getMessage();

                        if (code.contentEquals(ConstantUtils.SUCESS_CODE)){

                            if (response.body().getCategoryModels() != null)
                            {
                                if (response.body().getCategoryModels().size() > 0)
                                {

                                    rv_category.setAdapter(new CategoryAdapter(response.body().getCategoryModels(), context));
                                }

                            }

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
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

                   CommonUtils.cancelProgressDialog(progressDialog);
                t.printStackTrace();

            }
        });
    }

}

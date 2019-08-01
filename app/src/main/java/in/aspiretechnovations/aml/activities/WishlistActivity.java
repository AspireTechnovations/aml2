package in.aspiretechnovations.aml.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.adapters.ProductAdapter;
import in.aspiretechnovations.aml.model.ProductModel;
import in.aspiretechnovations.aml.response.ProductResponse;
import in.aspiretechnovations.aml.retrofit.ApiClient;
import in.aspiretechnovations.aml.retrofit.ApiInterface;
import in.aspiretechnovations.aml.utils.AppPref;
import in.aspiretechnovations.aml.utils.CommonUtils;
import in.aspiretechnovations.aml.utils.ConnectionDetector;
import in.aspiretechnovations.aml.utils.ConstantUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView rv_product;
    private ArrayList<ProductModel> productModels;
    private ProductAdapter productListAdapter;

    private FrameLayout frameNoData;
    private TextView tvError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        context = this;
        rv_product = findViewById(R.id.rv_product);
        rv_product.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        frameNoData = findViewById(R.id.frame_no_data);
        tvError = findViewById(R.id.tv_error_message);


        callnetwork();


    }

    private void callnetwork() {
        if (ConnectionDetector.isInternetConnected(context))
            networkWishlistProducts();
        else
            CommonUtils.showToast(context,getString(R.string.no_internet));
    }


    private void networkWishlistProducts() {

        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(context,"Loading wishlist");


        Map<String, String> map = new HashMap<>();
        map.put("user_id", AppPref.getUserId(context));


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ProductResponse> call = apiInterface.get_wishlist(map);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                CommonUtils.cancelProgressDialog(progressDialog);

                if (response.code() == 200){
                    if (response != null){

                        if (response.body().getCode().contentEquals(ConstantUtils.CODE_SUCESS)){


                            productModels = response.body().getProductModels();

                            if (productModels != null){

                                if (productModels.size() >0 ){
                                    setUpLayout(1,"");

                                    productListAdapter  = new ProductAdapter(context,productModels);

                                    rv_product.setAdapter(productListAdapter);


                                    // rv_products.setAdapter(productListAdapter);

                                }else {

                                    setUpLayout(0,response.body().getMessage());

                                }
                            }else {

                                setUpLayout(0,response.body().getMessage());

                            }



                        }else {

                            CommonUtils.showToast(context,response.body().getMessage());

                            setUpLayout(0,response.body().getMessage());
                        }



                    }else {

                        CommonUtils.showToast(context,getString(R.string.er_server));

                        setUpLayout(0,getString(R.string.er_server));
                    }

                }else {

                    CommonUtils.showToast(context,context.getString(R.string.er_server));
                }



            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                CommonUtils.cancelProgressDialog(progressDialog);
                CommonUtils.showToast(context,t.getMessage());
                setUpLayout(0,t.getLocalizedMessage());
            }
        });

    }

    private void setUpLayout(int i,String message) {
        if (i >0){
            frameNoData.setVisibility(View.GONE);
            rv_product.setVisibility(View.VISIBLE);

        }else {

            frameNoData.setVisibility(View.VISIBLE);
            rv_product.setVisibility(View.GONE);
            tvError.setText(message);

        }
    }

}

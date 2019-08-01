package in.aspiretechnovations.aml.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.adapters.ProductAdapter;
import in.aspiretechnovations.aml.model.ProductModel;
import in.aspiretechnovations.aml.response.ProductResponse;
import in.aspiretechnovations.aml.retrofit.ApiClient;
import in.aspiretechnovations.aml.retrofit.ApiInterface;
import in.aspiretechnovations.aml.utils.CommonUtils;
import in.aspiretechnovations.aml.utils.ConnectionDetector;
import in.aspiretechnovations.aml.utils.ConstantUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity{


    private RecyclerView rvSearch;
    private Context context;

    private EditText et_search;
    private ArrayList<ProductModel> productModels = new ArrayList<>();

  //  private CallbackCartItems callbackCartItems;

    private ProductAdapter productListAdapter;

    private ImageView im_back;

    private FrameLayout frameNoData;
    private TextView tvError;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        et_search = findViewById(R.id.et_search);
    //    callbackCartItems = this;
        im_back = findViewById(R.id.im_back);
        frameNoData = findViewById(R.id.frame_no_data);
        tvError = findViewById(R.id.tv_error_message);



        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        rvSearch   = findViewById(R.id.rv_search_products);
        rvSearch.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count > 0){

                    if (ConnectionDetector.isInternetConnected(context))
                        networkSearchProducts(s.toString());
                    else
                        CommonUtils.showToast(context,getString(R.string.no_internet));

                }else {



                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void networkSearchProducts(String keyword) {

        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(context,"Searching for '"+keyword+"'");


        Map<String, String> map = new HashMap<>();
        map.put("keyword",keyword);


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ProductResponse> call = apiInterface.search_products(map);

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

                                rvSearch.setAdapter(productListAdapter);


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
            rvSearch.setVisibility(View.VISIBLE);

        }else {

            frameNoData.setVisibility(View.VISIBLE);
            rvSearch.setVisibility(View.GONE);
            tvError.setText(message);

        }
    }



}

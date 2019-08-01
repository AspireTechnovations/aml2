package in.aspiretechnovations.aml.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.activities.CheckOutPageActivity;
import in.aspiretechnovations.aml.activities.SearchActivity;
import in.aspiretechnovations.aml.activities.ServiceFilterActivity;
import in.aspiretechnovations.aml.adapters.ProductAdapter;
import in.aspiretechnovations.aml.adapters.ProductGridAdapter;
import in.aspiretechnovations.aml.adapters.ViewPagerAdapter;
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

public class HomeFragment extends Fragment implements View.OnClickListener{
    private Activity context;
    private View view;
    private ViewPagerAdapter viewPagerAdapter;

    private ViewPager viewPager;
    private CardView cv_products, cv_services, cv_accessories;

    private ImageView im_cart;
    private RecyclerView recyclerView;
    private ArrayList<ProductModel> productModels;
    private ProductGridAdapter productGridAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        viewPager = view.findViewById(R.id.view_pager);
        cv_products = view.findViewById(R.id.cv_products);
        cv_services = view.findViewById(R.id.cv_services);
        cv_accessories = view.findViewById(R.id.cv_accessories);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));


        im_cart = view.findViewById(R.id.im_cart);
        im_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, CheckOutPageActivity.class));
            }
        });
        cv_services.setOnClickListener(this);
        cv_accessories.setOnClickListener(this);
        cv_products.setOnClickListener(this);
         EditText et_search;
        et_search  = view.findViewById(R.id.et_search);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, SearchActivity.class));
            }
        });

        setUpViewPager(viewPager);

        if (ConnectionDetector.isInternetConnected(context))
            networkProducts();
        else
            CommonUtils.showToast(context,getString(R.string.no_internet));
        return view;
    }

    private void setUpViewPager(ViewPager viewPager){
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SliderFragment());
        viewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.cv_services:

                startActivity(new Intent(context, ServiceFilterActivity.class));

                break;
            case R.id.cv_accessories:

                startActivity(new Intent(context,ServiceFilterActivity.class));

                break;
            case R.id.cv_products:


                startActivity(new Intent(context,ServiceFilterActivity.class));

                break;


        }
    }


    private void networkProducts() {

        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(context,"Loading");


        Map<String, String> map = new HashMap<>();
        map.put("user_id", AppPref.getUserId(context));


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ProductResponse> call = apiInterface.get_recent_products(map);

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



                                    productGridAdapter  = new ProductGridAdapter(context,productModels);

                                    recyclerView.setAdapter(productGridAdapter);


                                    // rv_products.setAdapter(productListAdapter);

                                }else {

                               //     setUpLayout(0,response.body().getMessage());

                                }
                            }else {

                               // setUpLayout(0,response.body().getMessage());

                            }



                        }else {

                            CommonUtils.showToast(context,response.body().getMessage());

                           // setUpLayout(0,response.body().getMessage());
                        }



                    }else {

                        CommonUtils.showToast(context,getString(R.string.er_server));

                     //   setUpLayout(0,getString(R.string.er_server));
                    }

                }else {

                    CommonUtils.showToast(context,context.getString(R.string.er_server));
                }



            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                CommonUtils.cancelProgressDialog(progressDialog);
                CommonUtils.showToast(context,t.getMessage());
             //   setUpLayout(0,t.getLocalizedMessage());
            }
        });

    }

}

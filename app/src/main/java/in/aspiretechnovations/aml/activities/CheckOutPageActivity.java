package in.aspiretechnovations.aml.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.adapters.CartAdapter;
import in.aspiretechnovations.aml.callbacks.CallBackRefresh;
import in.aspiretechnovations.aml.database.GeneralTables;
import in.aspiretechnovations.aml.model.CartItemModel;
import in.aspiretechnovations.aml.model.CartModel;
import in.aspiretechnovations.aml.model.ProductModel;
import in.aspiretechnovations.aml.response.BaseResponse;
import in.aspiretechnovations.aml.retrofit.ApiClient;
import in.aspiretechnovations.aml.retrofit.ApiInterface;
import in.aspiretechnovations.aml.utils.AppPref;
import in.aspiretechnovations.aml.utils.CommonUtils;
import in.aspiretechnovations.aml.utils.ConnectionDetector;
import in.aspiretechnovations.aml.utils.ConstantUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOutPageActivity extends AppCompatActivity implements CallBackRefresh {

    private CartModel cartModel;
    private GeneralTables orderTable;
    private RecyclerView rv_cart_items;

    private Activity context;
    private ArrayList<ProductModel> productModels;

    private CartAdapter cartAdapter;
    Double total_price_double;
    private TextView tv_total_price, tv_items_count, tv_sub_total;
    private LinearLayout ll_no_data;


    private Button btn_checkout;

    private Button btn_address;


    private CallBackRefresh callBackRefresh;




    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_page);

        orderTable = new GeneralTables(this);
        btn_checkout = findViewById(R.id.btn_checkout);
        context = this;
        btn_address = findViewById(R.id.btn_address);
        callBackRefresh = this;
        ll_no_data = findViewById(R.id.ll_no_data);
        tv_items_count = findViewById(R.id.tv_items_count);
        cartModel = orderTable.cartData();
        rv_cart_items = findViewById(R.id.rv_cart_items);
        rv_cart_items.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        productModels =new ArrayList<>();
        productModels = cartModel.getProductModels();

        if (productModels != null){

            if (productModels.size() > 0 ){

                total_price_double = orderTable.getTotalCartItemPrice();
                ll_no_data.setVisibility(View.GONE);
                rv_cart_items.setVisibility(View.VISIBLE);


                cartAdapter = new CartAdapter(context,productModels,callBackRefresh);
                rv_cart_items.setAdapter(cartAdapter);

            }else {

                ll_no_data.setVisibility(View.VISIBLE);
                rv_cart_items.setVisibility(View.GONE);
            }
        }else {

            ll_no_data.setVisibility(View.VISIBLE);
            rv_cart_items.setVisibility(View.GONE);
        }





        tv_total_price = findViewById(R.id.tv_total_price);
        if (rv_cart_items.getAdapter() != null)
        tv_items_count.setText(rv_cart_items.getAdapter().getItemCount()+" Nos");
        //tv_sub_total = findViewById(R.id.tv_sub_total);
        tv_total_price.setText(getString(R.string.rs)+"."+cartModel.getTotal());

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (rv_cart_items.getAdapter() != null)
                if (rv_cart_items.getAdapter().getItemCount() > 0)
                {

                    placeOrder();

                }else {

                    CommonUtils.showToast(context,"No items in cart");
                }
                else
                    CommonUtils.showToast(context,"No items in cart");

            }
        });


        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context,AddAddressActivity.class));
            }
        });


    }


    private void placeOrder(){


        if (ConnectionDetector.isInternetConnected(context)) {


            ArrayList<CartItemModel> cartItemModels = new ArrayList<>();

            ArrayList<ProductModel> productModels = new ArrayList<>();

            productModels = new GeneralTables(context).getCartItems();

            if (productModels != null) {

                if (productModels.size() > 0) {

                    for (ProductModel productModel : productModels) {

                        CartItemModel cartItemModel = new CartItemModel();
                        cartItemModel.setProduct_id(productModel.getProduct_id());
                        cartItemModel.setQuantity(String.valueOf(productModel.getCart_qty()));
                        cartItemModel.setPrice(productModel.getTotal_price());
                        cartItemModel.setVendor_id(productModel.getVendor_id());

                        cartItemModels.add(cartItemModel);

                    }
                }
            }

            Type type = new TypeToken<List<CartItemModel>>() {
            }.getType();
            String json = new Gson().toJson(cartItemModels, type);
            //networkAddToKitchen(json);

            networkPlaceOrder(String.valueOf(total_price_double), json, "0");


        } else {

            CommonUtils.showToast(context, getString(R.string.no_internet));
        }

    }

    @Override
    public void refresh() {
        tv_total_price = findViewById(R.id.tv_total_price);

        if (rv_cart_items.getAdapter() != null)
        tv_items_count.setText(rv_cart_items.getAdapter().getItemCount()+" Nos");

        //tv_sub_total = findViewById(R.id.tv_sub_total);
        tv_total_price.setText(getString(R.string.rs)+"."+orderTable.cartData().getTotal());

    }

    private void networkPlaceOrder(String total_price,String item_data,String address_id){

        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(context,"Sending your order request, Please wait...");

        Map<String, String> map = new HashMap<>();

        map.put("user_id", AppPref.getUserId(context));
        map.put("address_id", address_id);
        map.put("total_price", total_price);
        map.put("item_data", item_data);
        map.put("total_items", String.valueOf(rv_cart_items.getAdapter().getItemCount()));





        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<BaseResponse> call = apiInterface.create_order(map);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                CommonUtils.cancelProgressDialog(progressDialog);

                if (response.code() == 200){
                    if (response != null){

                        if (response.body().getCode().contentEquals(ConstantUtils.CODE_SUCESS)){


                            orderTable.clearCart();
                            CommonUtils.showToast(context,response.body().getMessage());
                            startActivity(new Intent(context, HomeActivity.class));
                            finishAffinity();



                        }else {

                            CommonUtils.showToast(context,response.body().getMessage());
                        }



                    }else {

                        CommonUtils.showToast(context,getString(R.string.er_server));
                    }

                }else {

                    CommonUtils.showToast(context,context.getString(R.string.er_server));
                }


            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                CommonUtils.cancelProgressDialog(progressDialog);
                CommonUtils.showToast(context,t.getMessage());
            }
        });





    }


}

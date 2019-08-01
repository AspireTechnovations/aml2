package in.aspiretechnovations.aml.adapters;

import android.app.Activity;

import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.callbacks.CallBackRefresh;
import in.aspiretechnovations.aml.database.GeneralTables;
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

/**
 * Created by shyam on 07/02/18.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder>{


    private Activity context;
    private GeneralTables orderTable;
    private ArrayList<ProductModel> arrayList;
    private AlertDialog alertDialog;
    private CallBackRefresh callBackRefresh;

    public CartAdapter(Activity context, ArrayList<ProductModel> arrayList, CallBackRefresh callBackRefresh) {
        this.context = context;
        this.arrayList = arrayList;
        this.callBackRefresh = callBackRefresh;
        orderTable = new GeneralTables(context);
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_items,parent,false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartHolder holder, final int position)
    {

        final ProductModel productModel = arrayList.get(position);



        holder.tv_item_name.setText(String.valueOf(productModel.getTitle()));
        holder.tv_item_price.setText(context.getString(R.string.rs)+"."+productModel.getTotal_price());

        holder.et_qty.setText(String.valueOf(productModel.getCart_qty()));

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderTable != null)
                    orderTable.deleteItemFromCart(productModel.getProduct_id(),productModel.getVendor_id());
                    arrayList.remove(position);

                    if (arrayList.size() == 0){
                        context.finish();
                    }else
                    notifyDataSetChanged();



            }
        });



        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.et_qty.getText().toString().contentEquals("0")){

                    orderTable.deleteItemFromCart(productModel.getProduct_id(),productModel.getVendor_id());

                    arrayList.remove(position);


                    if (arrayList.size() == 0){
                        context.finish();
                    }else
                        notifyDataSetChanged();


                }else {

                    remove_from_cart(productModel,holder,position);
                }
            }
        });

        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_to_cart(productModel,holder,position);




            }
        });
        /*if (productModel.getProduct_image() != null)

            Glide.with(context)
                    .load(productModel.getProduct_image())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(holder.im_product));
*/

    }

    private void add_to_cart(ProductModel productModel, CartHolder holder, int position){

        int current_qty = orderTable.getCartItemQty(productModel.getProduct_id(),productModel.getVendor_id());

        int qty;


            qty = current_qty+1;

            if (qty < 6) {

                orderTable.addToCart(productModel, qty);

                holder.et_qty.setText(String.valueOf(qty));

                String price = String.valueOf(Double.parseDouble(productModel.getDiscounted_price())*qty);
                holder.tv_item_price.setText(context.getString(R.string.rs)+"."+price);

                if (callBackRefresh != null)
                    callBackRefresh.refresh();
            }else
            {

                alertQuoteInfo(qty,productModel,position);

            }
        /*notifyItemChanged(position);
        notifyDataSetChanged();*/


    }


    private void alertQuoteInfo(final int qty, final ProductModel productModel, final int position){


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_quote, null);
        final EditText et_name = view.findViewById(R.id.et_product_name);
        final EditText et_qty = view.findViewById(R.id.et_qty);
        final EditText et_description = view.findViewById(R.id.et_description);
        et_name.setText(productModel.getTitle());
        et_qty.setText(String.valueOf(qty));
        final Button btn_submit =(Button) view.findViewById(R.id.btn_submit);

        et_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d("On changed",s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.d("After changed",s.toString());

                if(!s.toString().contentEquals("")){

                    if (Double.parseDouble(s.toString()) < 6)
                    {
                        et_qty.setError("Minimum qty should be 6");

                    }
                }

            }
        });




        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = et_name.getText().toString().trim();
                String qty = et_qty.getText().toString().trim();
                String description = et_description.getText().toString().trim();


                if (name.contentEquals("")){

                    et_name.setError(context.getString(R.string.et_error));
                }else{

                    if (qty.contentEquals("")){

                        et_qty.setError(context.getString(R.string.et_error));
                    }else{


                        if (description.contentEquals("")){

                            et_description.setError(context.getString(R.string.et_error));
                        }else{


                            if (Integer.valueOf(qty) < 6){

                                CommonUtils.showToast(context,"Minimum qty should be 6");

                            }else {
                                if (ConnectionDetector.isInternetConnected(context))
                                    networkGetQuote(qty,description,productModel,position);
                                else
                                    CommonUtils.showToast(context,context.getString(R.string.no_internet));
                                alertDialog.dismiss();

                            }





                        }
                    }


                }

            }
        });



        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);


    }


 private void remove_from_cart(ProductModel productModel,CartHolder holder, int position){

        int current_qty = orderTable.getCartItemQty(productModel.getProduct_id(), productModel.getVendor_id());
    int qty;

            qty = current_qty-1;

        if (qty >0) {
            orderTable.addToCart(productModel, qty);


            holder.et_qty.setText(String.valueOf(qty));

            String price = String.valueOf(Double.parseDouble(productModel.getDiscounted_price())*qty);
            holder.tv_item_price.setText(context.getString(R.string.rs)+"."+price);


        }else {

            if (orderTable != null)
                orderTable.deleteItemFromCart(productModel.getProduct_id(), productModel.getVendor_id());
            arrayList.remove(position);

            if (arrayList.size() == 0){
                context.finish();
            }else
                notifyDataSetChanged();

        }
         if (callBackRefresh != null)
             callBackRefresh.refresh();


//        notifyItemChanged(position);
//        notifyDataSetChanged();


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {

        TextView tv_item_name,  tv_item_price,tv_delete;
        EditText et_qty;


        Button btn_minus, btn_plus;


        public CartHolder(View itemView) {
            super(itemView);

            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_price = itemView.findViewById(R.id.tv_product_price);
            et_qty = itemView.findViewById(R.id.et_qty);
            tv_delete = itemView.findViewById(R.id.tv_delete);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            btn_plus = itemView.findViewById(R.id.btn_plus);





        }
    }


    private void networkGetQuote(final String qty , String description, ProductModel productModel, final int position) {


        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(context,"Loading");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Map<String,String> map = new HashMap<>();
        map.put("vendor_id",  productModel.getVendor_id());
        map.put("user_id",  AppPref.getUserId(context));
        map.put("description",  description);
        map.put("qty",  qty);
        map.put("product_id",  productModel.getProduct_id());





        Call<BaseResponse> call = apiService.insert_quote(map);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {


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


                            arrayList.get(position).setCart_qty(1);
                            notifyDataSetChanged();

                            CommonUtils.showToast(context,message);
                            if (callBackRefresh != null)
                            callBackRefresh.refresh();





                        }else{
                            CommonUtils.showToast(context,message);


                        }
                    }else {

                        CommonUtils.showToast(context,context.getString(R.string.er_server));
                    }

                }else{
                    CommonUtils.showToast(context,context.getString(R.string.er_server));

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

                CommonUtils.cancelProgressDialog(progressDialog);
                t.printStackTrace();

            }
        });
    }

}

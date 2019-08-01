package in.aspiretechnovations.aml.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.activities.ProductDetailActivity;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private Context context;
    private ArrayList<ProductModel> arrayList;

    public ProductAdapter(Context context, ArrayList<ProductModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_products,parent,false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, final int position) {

        final ProductModel productModel = arrayList.get(position);

        if (productModel != null)
        {

            String title = productModel.getTitle();
            String actual_price = productModel.getActual_price();
            String discounted_price = productModel.getDiscounted_price();
            String maker = productModel.getMaker();
            String image = productModel.getImage();

            if (title != null)
                holder.tv_title.setText(title);


            if (maker != null)
                holder.tv_maker.setText("By "+maker);


            if (actual_price != null)
                holder.tv_actual.setText(actual_price);


            if (discounted_price != null)
                holder.tv_discounted.setText(discounted_price);

            if (image !=null)
            {
                if (!image.contentEquals("")){

                    if(image.contains(".gif"))
                        Glide.with(context).asGif()
                                .load(image).into(holder.im_product);
                    else
                        Glide.with(context)
                                .load(image).into(holder.im_product);


                }
            }


            if (productModel.getIs_wishlist().contentEquals("1"))
                holder.im_heart.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart_red));
            else
                holder.im_heart.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart));



            holder.im_heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ConnectionDetector.isInternetConnected(context))
                        networkWishList(productModel,position);
                    else
                        CommonUtils.showToast(context,context.getString(R.string.no_internet));


                }
            });

            holder.ll_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, ProductDetailActivity.class).putExtra("model",productModel));
                }
            });
        }

    }

    private void networkWishList(ProductModel productModel, final int position){

        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(context,"Loading...");

        Map<String, String> map = new HashMap<>();

        map.put("user_id", AppPref.getUserId(context));
        map.put("product_id", productModel.getProduct_id());


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<BaseResponse> call = apiInterface.insert_wishlist(map);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                CommonUtils.cancelProgressDialog(progressDialog);

                if (response.code() == 200){

                    if (response != null){

                        if (response.body().getCode().contentEquals(ConstantUtils.CODE_SUCESS)){

                            if (arrayList.get(position).getIs_wishlist().contentEquals("1"))
                                arrayList.get(position).setIs_wishlist("0");
                            else
                                arrayList.get(position).setIs_wishlist("1");

                            notifyDataSetChanged();


                        }else {

                            CommonUtils.showToast(context,response.body().getMessage());
                        }



                    }else {

                        CommonUtils.showToast(context,context.getString(R.string.er_server));
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



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        ImageView im_product, im_heart;
        TextView tv_discounted, tv_actual, tv_maker, tv_title;

        LinearLayout ll_parent;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            tv_discounted = itemView.findViewById(R.id.tv_discounted);
            tv_actual = itemView.findViewById(R.id.tv_actual);
            tv_maker = itemView.findViewById(R.id.tv_maker);
            tv_title = itemView.findViewById(R.id.tv_title);
            im_product = itemView.findViewById(R.id.im_product);
            ll_parent = itemView.findViewById(R.id.ll_parent);
            im_heart = itemView.findViewById(R.id.im_heart);

        }
    }

}

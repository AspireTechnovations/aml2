package in.aspiretechnovations.aml.retrofit;


import java.util.Map;

import in.aspiretechnovations.aml.response.BaseResponse;
import in.aspiretechnovations.aml.response.CategoryResponse;
import in.aspiretechnovations.aml.response.DataResponse;
import in.aspiretechnovations.aml.response.OtpResponse;
import in.aspiretechnovations.aml.response.ProductResponse;
import in.aspiretechnovations.aml.response.UserResponse;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("send_otp")
    Call<OtpResponse> sendOtp(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("register_user_by_number")
    Call<UserResponse> register_by_mobile(@FieldMap Map<String, String> map);


    @POST("get_data")
    Call<DataResponse> getData();


    @FormUrlEncoded
    @POST("get_categories")
    Call<CategoryResponse> get_categories(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("get_products")
    Call<ProductResponse> get_products(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("insert_quote")
    Call<BaseResponse> insert_quote(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("create_order")
    Call<BaseResponse> create_order(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("search_products")
    Call<ProductResponse> search_products(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("add_address")
    Call<BaseResponse> addAddres(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("insert_wishlist")
    Call<BaseResponse> insert_wishlist(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("get_wishlist")
    Call<ProductResponse> get_wishlist(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("get_recent_products")
    Call<ProductResponse> get_recent_products(@FieldMap Map<String, String> map);



}

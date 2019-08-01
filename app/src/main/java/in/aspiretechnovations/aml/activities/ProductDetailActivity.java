package in.aspiretechnovations.aml.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.adapters.ViewPagerAdapter;
import in.aspiretechnovations.aml.database.GeneralTables;
import in.aspiretechnovations.aml.fragments.DescriptionFragment;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    public ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView im_cart;

    private ViewPagerAdapter viewPagerAdapter;

    private ImageView im_product;
    private Activity context;
    private ProductModel productModel;

    private TextView tv_title, tv_maker,tv_discounted,tv_actual,tv_qty,tv_price;

    private EditText et_qty;
    private AlertDialog alertDialog;

    private Button btn_minus,btn_plus,btn_qoute ;
    private int qty = 0;
    private GeneralTables orderTable;


    private EditText et_location;

    private static final int PLACE_PICKER_REQUEST = 1001;
    private LatLng latLng;
    private static final String TAG = "location_tag";
    private Double latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        context = this;
        productModel = (ProductModel) getIntent().getSerializableExtra("model");
        orderTable = new GeneralTables(context);
        et_location = findViewById(R.id.et_location);
        Places.initialize(getApplicationContext(), getString(R.string.api_key));

        im_cart = findViewById(R.id.im_cart);
        im_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context,CheckOutPageActivity.class));
            }
        });
        try {

            initialise();
        }catch (NullPointerException ne){

            ne.printStackTrace();
        }
        EditText et_search;
        et_search  = findViewById(R.id.et_search);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, SearchActivity.class));
            }
        });

        et_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Set the fields to specify which types of place data to return.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG,  Place.Field.NAME);
// Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(ProductDetailActivity.this);
                startActivityForResult(intent, PLACE_PICKER_REQUEST);

               /* PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }*/
            }
        });



    }

    private void initialise()
    {
        btn_qoute = findViewById(R.id.btn_qoute);
        im_product = findViewById(R.id.im_product);
        tv_title = findViewById(R.id.tv_title);
        tv_maker = findViewById(R.id.tv_maker);
        tv_discounted = findViewById(R.id.tv_discounted);
        tv_actual = findViewById(R.id.tv_actual);
        tv_qty = findViewById(R.id.tv_qty);

        tv_price = findViewById(R.id.tv_price);
        qty = orderTable.getCartItemQty(String.valueOf(productModel.getProduct_id()),productModel.getVendor_id());
        et_qty = findViewById(R.id.et_qty);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);

        PlacesClient placesClient = Places.createClient(this);

        if (productModel != null)
        {

            btn_qoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertQuoteInfo(6);
                }
            });

            tv_title.setText(""+productModel.getTitle());
            tv_maker.setText("By "+productModel.getMaker());
            tv_actual.setText(context.getString(R.string.rs)+" "+productModel.getActual_price());
            CommonUtils.strikeThroughText(tv_actual);

            tv_discounted.setText(context.getString(R.string.rs)+"."+productModel.getDiscounted_price());

            tv_qty.setText("Qty: 1-5 "+ context.getString(R.string.rs)+"."+productModel.getDiscounted_price());

            if (qty >  0){


                Double price = Double.valueOf(productModel.getDiscounted_price())*qty;

                tv_price.setText("Product Price: "+String.valueOf(price));
              et_qty.setText(String.valueOf(qty));


            }else {
                tv_price.setText("Product Price: "+productModel.getDiscounted_price());

            }


            btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    remove_from_cart(productModel);
                }

            });

            btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_to_cart(productModel);

                }
            });


            String image = productModel.getImage();

            if (image !=null)
            {
                if (!image.contentEquals("")){

                    if(image.contains(".gif"))
                        Glide.with(context).asGif()
                                .load(image).into(im_product);
                    else
                        Glide.with(context)
                                .load(image).into(im_product);


                }
            }


        }


        viewPager = findViewById(R.id.viewpager_home);
        tabLayout =  findViewById(R.id.tabs_home);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        );
        setUpViewPager(viewPager);
    }

    public void setUpViewPager(ViewPager viewPager){

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new DescriptionFragment(),"Description");
        viewPagerAdapter.addFragment(new DescriptionFragment(),"Specifications");
        viewPagerAdapter.addFragment(new DescriptionFragment(),"More Info");

        viewPager.setAdapter(viewPagerAdapter);


    }

    private void add_to_cart(ProductModel productModel){

        int current_qty = orderTable.getCartItemQty(productModel.getProduct_id(),productModel.getVendor_id());
        int qty = 0;


            qty = current_qty+1;

            if (qty < 6) {


                orderTable.addToCart(productModel, qty);

                et_qty.setText(String.valueOf(qty));
                Double current_price = current_qty * Double.parseDouble(productModel.getDiscounted_price());
                tv_price.setText("Product Price: " + String.valueOf(current_price));
            }else {

                alertQuoteInfo(qty);
            }

        //  callbackCartItems.update_cart_items();

    }

    private void remove_from_cart(ProductModel productModel){

        int current_qty = orderTable.getCartItemQty(productModel.getProduct_id(),productModel.getVendor_id());

        if (current_qty >0) {
            current_qty = current_qty -1;
            orderTable.addToCart(productModel,current_qty);
            et_qty.setText(String.valueOf(current_qty));
        /*    if (callbackCartItems !=null)
                callbackCartItems.update_cart_items();*/

        }else {

            if (orderTable != null)
                orderTable.deleteItemFromCart(productModel.getProduct_id(),productModel.getVendor_id());
            et_qty.setText(String.valueOf("0"));

        }

        Double current_price = current_qty*Double.parseDouble(productModel.getDiscounted_price());
        tv_price.setText("Product Price: "+String.valueOf(current_price));




    }




    private void alertQuoteInfo(final int qty){


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

                    }else
                    {


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

                    et_name.setError(getString(R.string.et_error));
                }else{

                    if (qty.contentEquals("")){

                        et_qty.setError(getString(R.string.et_error));
                    }else{


                        if (description.contentEquals("")){

                            et_description.setError(getString(R.string.et_error));
                        }else{


                            if (Integer.valueOf(qty) < 6){

                                CommonUtils.showToast(context,"Minimum qty should be 6");

                            }else {
                                if (ConnectionDetector.isInternetConnected(context))
                                    networkGetQuote(qty,description);
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




    private void networkGetQuote(String qty , String description) {


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

                            CommonUtils.showToast(context,message);





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
            public void onFailure(Call<BaseResponse> call, Throwable t) {

                CommonUtils.cancelProgressDialog(progressDialog);
                t.printStackTrace();

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                // is_location_set = true;
                latLng = place.getLatLng();

                if (latLng != null) {
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                }
                et_location.setText(String.format("%s", place.getName()));

                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

     /*   if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Selected Place: %s", place.getName());
                tv_location_selected.setText(String.format("Selected Place: %s", place.getName()));

                latLng = PlacePicker.getLatLngBounds(data);
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                Toast.makeText(this, "Latitude: "+latitude+", Longitude: "+longitude, Toast.LENGTH_LONG).show();

                //et_area.setText(place.getName());
                is_location_set = true;
            }
        }*/
    }


}

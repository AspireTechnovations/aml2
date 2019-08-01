package in.aspiretechnovations.aml.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aspiretechnovations.aml.R;
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

public class AddAddressActivity extends AppCompatActivity {

    private Context context;

    private EditText et_title, et_full_address, et_location, et_landmark, et_pincode;

    private Button btn_submit,btn_location;

    private static final int PLACE_PICKER_REQUEST = 1001;
    private static final String TAG = "location_tag";

    private Double latitude, longitude;
    private LatLng latLng;
    private Boolean is_location_set = false;
    private TextView tv_location_selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        context = this;
        tv_location_selected = findViewById(R.id.tv_location_selected);
        Places.initialize(getApplicationContext(), getString(R.string.api_key));

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
        et_title = findViewById(R.id.et_title);
        et_full_address = findViewById(R.id.et_full_address);
        et_location = findViewById(R.id.et_location);
        et_landmark = findViewById(R.id.et_landmark);
        et_pincode = findViewById(R.id.et_pincode)
        ;

        btn_submit = findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (is_location_set) {
                    String title = et_title.getText().toString();
                    String address = et_full_address.getText().toString();
                    String location = tv_location_selected.getText().toString();
                    String landmark = et_landmark.getText().toString();
                    String pincode = et_pincode.getText().toString();

                    if (title.contentEquals("")) {

                        et_title.setError(getString(R.string.et_error));

                    } else {

                        if (address.contentEquals("")) {

                            et_full_address.setError(getString(R.string.et_error));

                        } else {




                                if (landmark.contentEquals("")) {

                                    et_landmark.setError(getString(R.string.et_error));

                                } else {

                                    if (pincode.contentEquals("")) {

                                        et_pincode.setError(getString(R.string.et_error));

                                    } else {

                                        if (ConnectionDetector.isInternetConnected(context))
                                            networkAddAddress(title, address, location, landmark, pincode, "1");
                                        else
                                            CommonUtils.showToast(context, getString(R.string.no_internet));


                                    }



                            }
                        }


                    }

                }else {
                    CommonUtils.showToast(context, "Please select your location");


                }
            }
        });



        btn_location = findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Set the fields to specify which types of place data to return.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG,  Place.Field.NAME);
// Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(AddAddressActivity.this);
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




    private void networkAddAddress(String title,String address,String location,String landmark,String pincode,String city_id){

        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(context,"Loading...");

        Map<String, String> map = new HashMap<>();

        map.put("user_id", AppPref.getUserId(context));
        map.put("title", title);
        map.put("address", address);
        map.put("location", location);
        map.put("landmark", landmark);
        map.put("pincode", pincode);
        map.put("latitude",String.valueOf(latitude));
        map.put("longitude",String.valueOf(longitude));
        map.put("city_id", city_id);



        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<BaseResponse> call = apiInterface.addAddres(map);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                CommonUtils.cancelProgressDialog(progressDialog);

                if (response.code() == 200){

                if (response != null){

                    if (response.body().getCode().contentEquals(ConstantUtils.CODE_SUCESS)){


                        finish();


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                is_location_set = true;
                latLng = place.getLatLng();

                if (latLng != null) {
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                }
                tv_location_selected.setText(String.format("Selected Place: %s", place.getName()));

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

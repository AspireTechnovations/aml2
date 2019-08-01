package in.aspiretechnovations.aml.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.adapters.CategoryAdapter;
import in.aspiretechnovations.aml.adapters.ProductAdapter;
import in.aspiretechnovations.aml.response.CategoryResponse;
import in.aspiretechnovations.aml.response.ProductResponse;
import in.aspiretechnovations.aml.retrofit.ApiClient;
import in.aspiretechnovations.aml.retrofit.ApiInterface;
import in.aspiretechnovations.aml.utils.AppPref;
import in.aspiretechnovations.aml.utils.CommonUtils;
import in.aspiretechnovations.aml.utils.ConnectionDetector;
import in.aspiretechnovations.aml.utils.ConstantUtils;
import in.aspiretechnovations.aml.utils.GPSTracker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductListActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_CHECK_SETTINGS = 0001;
    private ProgressDialog progressDialog;
    private Context context;

    private Geocoder geocoder;
    private List<Address> addresses;

    private String category_id = "0";
    private RecyclerView rv_products;
    private EditText et_location;
    private ImageView im_cart;
    private static final int PLACE_PICKER_REQUEST = 1001;
    private LatLng latLng;
    private static final String TAG = "location_tag";
    private Double latitude, longitude;
    String provider;
    protected boolean gps_enabled, network_enabled;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        context = this;
        category_id = getIntent().getExtras().getString(ConstantUtils.KEY_CATEGORY_ID);
        et_location = findViewById(R.id.et_location);
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        geocoder = new Geocoder(this, Locale.getDefault());
      /*  GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            String addressLine = gpsTracker.getAddressLine(this);
            String city = gpsTracker.getLocality(this);


            et_location.setText(addressLine+" , "+city);
        }else {

            gpsTracker.showSettingsAlert();
        }*/

        PlacesClient placesClient = Places.createClient(this);
        rv_products = findViewById(R.id.rv_product);
        rv_products.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        im_cart = findViewById(R.id.im_cart);
        im_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, CheckOutPageActivity.class));
            }
        });
        if (ConnectionDetector.isInternetConnected(context))
            networkProducts(category_id);
        else
            CommonUtils.showToast(context, getString(R.string.no_internet));

        EditText et_search;
        et_search = findViewById(R.id.et_search);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, SearchActivity.class));
            }
        });


        if (!CommonUtils.is_gps_enabled(context)) {

            displayLocationSettingsRequest(context);
        }


        et_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Set the fields to specify which types of place data to return.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME);
// Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(ProductListActivity.this);
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

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            et_location.setText(address+" , "+city);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(ProductListActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void networkProducts(String category_id) {


        progressDialog = CommonUtils.showProgressDialog(context,"Loading");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Map<String,String> map = new HashMap<>();
        map.put(ConstantUtils.FUEL_TYPE, String.valueOf(AppPref.getKeyFuelType(context)));
        map.put(ConstantUtils.MAKER_ID,  AppPref.getSelectedMakerId(context));
        map.put(ConstantUtils.MODEL_ID,  AppPref.getKeyModelId(context));
        map.put(ConstantUtils.KEY_CATEGORY_ID,  category_id);
        map.put(ConstantUtils.VARIANT_ID,  AppPref.getKeyVariantId(context));
        map.put(ConstantUtils.USER_ID,  AppPref.getUserId(context));


        Call<ProductResponse> call = apiService.get_products(map);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {


                //Todo: add response null checker

                CommonUtils.cancelProgressDialog(progressDialog);
                if (response.code() == 200) {

                    if (response != null) {

                        String code = response.body().getCode();
                        String message = response.body().getMessage();

                        if (code.contentEquals(ConstantUtils.SUCESS_CODE)){

                            if (response.body().getProductModels() != null)
                            {
                                if (response.body().getProductModels().size() > 0)
                                {

                                    rv_products.setAdapter(new ProductAdapter(context,response.body().getProductModels()));
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
            public void onFailure(Call<ProductResponse> call, Throwable t) {

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


    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            et_location.setText(address+" , "+city);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

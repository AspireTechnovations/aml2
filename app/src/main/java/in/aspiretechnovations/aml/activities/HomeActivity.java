package in.aspiretechnovations.aml.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;
import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.adapters.ViewPagerAdapter;
import in.aspiretechnovations.aml.database.GeneralTables;
import in.aspiretechnovations.aml.fragments.HomeFragment;
import in.aspiretechnovations.aml.fragments.MyAccountFragment;
import in.aspiretechnovations.aml.fragments.SliderFragment;
import in.aspiretechnovations.aml.response.DataResponse;
import in.aspiretechnovations.aml.response.OtpResponse;
import in.aspiretechnovations.aml.retrofit.ApiClient;
import in.aspiretechnovations.aml.retrofit.ApiInterface;
import in.aspiretechnovations.aml.utils.CommonUtils;
import in.aspiretechnovations.aml.utils.ConnectionDetector;
import in.aspiretechnovations.aml.utils.ConstantUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity  {

    private static final int REQUEST_CHECK_SETTINGS = 0001;
    private static final String TAG = "tag";
    private ViewPagerAdapter viewPagerAdapter;

    private ViewPager viewPager;
    private CardView cv_others, cv_services, cv_accessories;
    private Context context;
    private AHBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);


        if (!CommonUtils.is_gps_enabled(context))
        {

            displayLocationSettingsRequest(context);
        }

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_home, R.color.title);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_settings, R.color.bar_background);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_notifications, R.color.jet);
        AHBottomNavigationItem item4= new AHBottomNavigationItem(R.string.tab_4, R.drawable.ic_location, R.color.already_member);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tab_5, R.drawable.ic_person, R.color.aluminum);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);


// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Display color under navigation bar (API 21+)
// Don't forget these lines in your style-v21
// <item name="android:windowTranslucentNavigation">true</item>
// <item name="android:fitsSystemWindows">true</item>
        bottomNavigation.setTranslucentNavigationEnabled(true);

// Manage titles

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);


// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(1);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));


      getSupportFragmentManager().beginTransaction().add(R.id.frame,new HomeFragment()).commit();

      bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
          @Override
          public boolean onTabSelected(int position, boolean wasSelected) {

              switch (position)
              {

                  case 0:

                      getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragment()).commit();



                      break;


                  case 1:

                      break;


                  case 2:

                      break;

                  case 3:

                      break;

                  case 4:
                      getSupportFragmentManager().beginTransaction().replace(R.id.frame,new MyAccountFragment()).commit();

                      break;
              }

              return true;
          }
      });


      if (ConnectionDetector.isInternetConnected(context))
          networkData();
      else
          CommonUtils.showToast(context,getString(R.string.no_internet));
    }



    private void networkData() {


        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(context,"Loading");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

         Call<DataResponse> call = apiService.getData();

        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {


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

                            DataResponse.DataModel dataModel = response.body().getDataModel();

                            GeneralTables generalTables = new GeneralTables(context);

                            if (dataModel != null)
                            {

                                if (dataModel.getMakeModels() != null)
                                {
                                    generalTables.insertMake(dataModel.getMakeModels());
                                }

                                if (dataModel.getModelArrayList() != null)
                                {
                                    generalTables.insertModel(dataModel.getModelArrayList());
                                }


                                if (dataModel.getVariantModels() != null)
                                {
                                    generalTables.insertVariant(dataModel.getVariantModels());
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
            public void onFailure(Call<DataResponse> call, Throwable t) {

                CommonUtils.cancelProgressDialog(progressDialog);
                t.printStackTrace();

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
                            status.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);
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



}

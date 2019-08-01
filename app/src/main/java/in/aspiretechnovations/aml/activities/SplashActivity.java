package in.aspiretechnovations.aml.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.utils.AppPref;
import in.aspiretechnovations.aml.utils.ConnectionDetector;

public class SplashActivity extends AppCompatActivity {


    private static final long SPLASH_TIME_OUT = 2000;
    private Context context;


    private static final int PERMISSIONS_REQUEST = 1234;

    public String[] getRequiredPermissions() {
        String[] permissions = null;
        try {
            permissions = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (permissions == null) {
            return new String[0];
        } else {
            return permissions.clone();
        }
    }

    /**
     * Convert the array of required permissions to a {@link Set} to remove
     * redundant elements. Then remove already granted permissions, and return
     * an array of ungranted permissions.
     */
    @TargetApi(23)
    private String[] requiredPermissionsStillNeeded() {

        Set<String> permissions = new HashSet<String>();
        for (String permission : getRequiredPermissions()) {
            permissions.add(permission);
        }
        for (Iterator<String> i = permissions.iterator(); i.hasNext();) {
            String permission = i.next();
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(SplashActivity.class.getSimpleName(),
                        "Permission: " + permission + " already granted.");
                i.remove();
            } else {
                Log.d(SplashActivity.class.getSimpleName(),
                        "Permission: " + permission + " not yet granted.");
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            checkPermissions();
        }
    }


    private void checkPermissions() {
        String[] ungrantedPermissions = requiredPermissionsStillNeeded();
        if (ungrantedPermissions.length == 0) {
            init();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(ungrantedPermissions, PERMISSIONS_REQUEST);
            }
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
      //  FirebaseApp.initializeApp(context);



        if(ConnectionDetector.isInternetConnected(context)) {




            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions();
            } else {
                init();
            }

        }else {


            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getString(R.string.alert_title_default));
            builder.setMessage(getString(R.string.no_internet));
            builder.setPositiveButton(getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        }

    }

    private void init() {



        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(SPLASH_TIME_OUT);


                    startActivity();



                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }



    private void startActivity(){


        if (AppPref.isLoggedIn(context))
        {
            startActivity(new Intent(context, HomeActivity.class));
        }else
        startActivity(new Intent(context, OtpLoginActivity.class));
        finish();


    }

}

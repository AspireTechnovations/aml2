package in.aspiretechnovations.aml.utils;

import android.content.Context;
import android.content.SharedPreferences;

import in.aspiretechnovations.aml.model.UserModel;

import static in.aspiretechnovations.aml.utils.CommonUtils.KEY_MOBILE_NO;

public class AppPref {

    private static final String IS_REGISTERED = "is_registered";
    private static final String KEY_MAKER_ID = "maker_id";
    private static final String KEY_MAKER_NAME = "maker_name";
    private static final String KEY_VARIANT_ID = "variant_id";
    private static final String KEY_VARIANT_NAME = "variant_name";
    private static final String KEY_MODEL_ID = "model_id";
    private static final String KEY_MODEL_NAME = "model_name";
    private static final String KEY_FUEL_TYPE = "fuel_type";
    private static final String KEY_YEAR = "year";
    private static SharedPreferences sharedPreferences;

    private static final String KEY_PREF_NAME = "pref";

    private static final String KEY_FCM_TOKEN = "fcm_token";
    private static final String KEY_LOGIN = "isLoggedIn";
    private static final String KEY_USER_NAME = "name";
    private static final String EMAIL_ID = "email_id";
    private static final String KEY_MOBILE_NO = "mobile_no";


    private static final String USER_ID = "user_id";
    private static final String CREATED_AT = "created_at";
    private static SharedPreferences.Editor editSharedPref(Context context){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        return editor;
    }

    public static void setKeyFcmToken(Context context, String fcm_token){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_FCM_TOKEN, fcm_token);
        editor.commit();

    }

    public static String getKeyFcmToken(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FCM_TOKEN, null);

    }



    public static void saveInfo(Context context, UserModel userModel) {

        SharedPreferences.Editor editor = editSharedPref(context);

        if (userModel.getUser_id() != null)
            if (!userModel.getUser_id().contentEquals(""))
                editor.putString(USER_ID,userModel.getUser_id());

        if (userModel.getName() != null)
            if (!userModel.getName().contentEquals(""))
                editor.putString(KEY_USER_NAME,userModel.getName());

        if (userModel.getEmail_id() != null)
            if (!userModel.getEmail_id().contentEquals(""))
                editor.putString(EMAIL_ID,userModel.getEmail_id());


        if (userModel.getMobile_no() != null)
            if (!userModel.getMobile_no().contentEquals(""))
                editor.putString(KEY_MOBILE_NO,userModel.getMobile_no());

        if (userModel.getIs_registered() != null)
            if (!userModel.getIs_registered().contentEquals(""))
                editor.putString(IS_REGISTERED,userModel.getIs_registered());


        if (userModel.getCreated_at() != null)
            if (!userModel.getCreated_at().contentEquals(""))
                editor.putString(CREATED_AT,userModel.getCreated_at());


        editor.commit();




    }


    public static void setLogout(Context context){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putBoolean(KEY_LOGIN, false);
        editor.commit();

    }

    public static void setLoggedIn(Context context, boolean isLoggedIn){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putBoolean(KEY_LOGIN, isLoggedIn);


        editor.commit();

    }

    public static boolean isLoggedIn(Context context){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(KEY_LOGIN, false);
    }

    public static String getUsername(Context context){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public static String getKeyMobileNo(Context context){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_MOBILE_NO, "");
    }

    public static String getEmailId(Context context){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(EMAIL_ID, "");
    }

    public static String getUserId(Context context){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(USER_ID, "");
    }



    public static String getIsRegistered(Context context){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(IS_REGISTERED, "0");
    }



    public static String getSelectedMakerId(Context context){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_MAKER_ID, "0");

    }

    public static String getSelectedMakerName(Context context){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_MAKER_NAME, "0");

    }

    public static void setSelectedMakerId(Context context, String maker_id){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_MAKER_ID, maker_id);
        editor.commit();

    }


    public static void setSelectedMakerName(Context context, String name){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_MAKER_NAME, name);
        editor.commit();

    }

    public static void setSelectedModelId(Context context, String model_id){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_MODEL_ID, model_id);
        editor.commit();

    }

    public static void setSelectedModelName(Context context, String name){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_MODEL_NAME, name);
        editor.commit();

    }



    public static void setSelectVariantId(Context context, String variant_id){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_VARIANT_ID, variant_id);
        editor.commit();

    }


    public static void setSelectVariantName(Context context, String name){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_VARIANT_NAME, name);
        editor.commit();

    }


    public static void setSelectFuelType(Context context, int fuel_type){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putInt(KEY_FUEL_TYPE, fuel_type);
        editor.commit();

    }


    public static void setSelectYear(Context context, String year){
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_YEAR, year);
        editor.commit();

    }

    public static String getKeyModelId(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_MODEL_ID, "0");

    }



    public static String getKeyVariantId(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_VARIANT_ID, "0");

    }

    public static String getKeyModelName(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_MODEL_NAME, "0");

    }




    public static String getKeyVariantName(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_VARIANT_NAME, "0");

    }

    public static int getKeyFuelType(Context context) {

        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(KEY_FUEL_TYPE, 3);

    }
}

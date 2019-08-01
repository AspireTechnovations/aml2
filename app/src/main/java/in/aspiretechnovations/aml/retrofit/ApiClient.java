package in.aspiretechnovations.aml.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shyam on 01/09/17.
 */

public class ApiClient {



    public static final String BASE_URL = "http://projectserver.xyz/aml/api/index.php/api/";


    private static Retrofit retrofit = null;



    public static Retrofit getClient() {

        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS).build();


        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }


}

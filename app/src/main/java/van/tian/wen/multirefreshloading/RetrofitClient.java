package van.tian.wen.multirefreshloading;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RadAsm on 17/3/6.
 */
public class RetrofitClient {

    public static final String API_BASE_URL = "http://112.74.32.29:8081";

    public static Retrofit get() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit;
    }

    public static <T> T service(Class<T> clazz) {
        return get().create(clazz);
    }

}

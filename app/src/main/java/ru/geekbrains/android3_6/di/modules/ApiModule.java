package ru.geekbrains.android3_6.di.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.android3_6.mvp.model.api.IDataSource;

import javax.inject.Named;

@Module
public class ApiModule {

    @Named("baseUrlProd")
    @Provides
    public String baseUrlProduction(){
        return "https://api.github.com/";
    }

    @Named("baseUrlDev")
    @Provides
    public String baseUrlDev(){
        return "https://api.github.com/"; //Different from baseUrlProduction
    }


    @Provides
    public OkHttpClient okHttpClient(HttpLoggingInterceptor loggingInterceptor){
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    public IDataSource iDataSource(Gson gson, @Named("baseUrlProd") String baseUrl){
       return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(IDataSource.class);
    }

    @Provides
    public Gson gson(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    @Provides
    public HttpLoggingInterceptor loggingInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}

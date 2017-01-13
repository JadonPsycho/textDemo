package com.psycho;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by mr.psycho on 2017/1/11.
 */

public interface ApiService {

    @GET("Home/GetTypeAll")
    Call<InfoResponse> info();

    @Multipart
    @POST("User/UpdateForImage")
    Call<UploadEntity> Upload(@PartMap Map<String,RequestBody> requestBody);
}

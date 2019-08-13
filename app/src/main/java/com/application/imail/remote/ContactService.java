package com.application.imail.remote;


import com.application.imail.model.listcontact;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ContactService {
    @FormUrlEncoded
    @POST("getcontacts/")
    Call<List<listcontact>> getcontacts(@Field("UserID") int UserID);

    @FormUrlEncoded
    @POST("getcontact/")
    Call<List<listcontact>> getcontact(@Field("UserID") int UserID);

    @FormUrlEncoded
    @POST("addcontact/")
    Call<listcontact> addcontact(@Field("UserID") int UserID, @Field("Email") String Email, @Field("Name") String Name, @Field("Phone") String Phone
            , @Field("Birth_Date") String Birth_Date, @Field("Gender") String Gender);

    @FormUrlEncoded
    @POST("editcontact/")
    Call<listcontact> editcontact(@Field("UserID") int UserID, @Field("Email") String Email, @Field("Name") String Name, @Field("Phone") String Phone);

    @FormUrlEncoded
    @POST("deletecontact/")
    Call<listcontact> deletecontact(@Field("UserID") int UserID, @Field("Email") String Email);

}

package com.application.imail.remote;


import com.application.imail.model.User;
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

public interface UserService {
    @FormUrlEncoded
    @POST("user/")
    Call<User> login(@Field("Email") String email, @Field("Password") String password);

    @FormUrlEncoded
    @POST("register/")
    Call<User> register(@Field("Email") String email, @Field("Password") String password, @Field("Name") String name);

    @FormUrlEncoded
    @POST("password/")
    Call<User> changepassword(@Field("Email") String email, @Field("OldPassword") String oldpassword, @Field("NewPassword") String newpassword);

    @FormUrlEncoded
    @POST("registeralternatif/")
    Call<User> addakunalternatif(@Field("Email") String email, @Field("Password") String password, @Field("Alternate_Email") String alternate_email);

    @FormUrlEncoded
    @POST("getcontact/")
    Call<List<listcontact>> getcontact(@Field("UserID") int UserID);

    @FormUrlEncoded
    @POST("addcontact/")
    Call<listcontact> addcontact(@Field("UserID") int UserID, @Field("Email") String Email, @Field("Name") String Name, @Field("Phone") String Phone);

    @FormUrlEncoded
    @POST("deletecontact/")
    Call<listcontact> deletecontact(@Field("UserID") int UserID, @Field("Email") String Email);

    @FormUrlEncoded
    @POST("ceksaldo/")
    Call<User> ceksaldo(@Field("pin") String pin, @Field("norekening") String norekening);

    @FormUrlEncoded
    @PUT("gantipin/")
    Call<User> gantipin(@Field("norekening") String norekening, @Field("pinlama") String pin, @Field("pinbaru") String pinbaru);

//    @FormUrlEncoded
//    @POST("mutasi/")
//    Call<Transaksi> mutasi(@Field("norekening") String norekening, @Field("pin") String pin, @Field("bulan") String bulan);

    @GET("logout/")
    Call<List<User>> getUsers();

    @POST("add/")
    Call<ResponseBody> addUser(@Body User name);

    @PUT("update/{id}")
    Call<ResponseBody> updateUser(@Path("id") int id, @Body User name);

    @DELETE("delete/{id}")
    Call<ResponseBody> deleteUser(@Path("id") int id);
}

package com.application.imail.remote;


import com.application.imail.model.Domain;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DomainService {
    @GET("getdomain/")
    Call<List<Domain>> getdomain();

}

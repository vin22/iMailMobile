package com.application.imail.remote;

import com.application.imail.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MessageService {
    @FormUrlEncoded
    @POST("getdraft/")
    Call<List<Message>> getdraft(@Field("UserID") int userid);

    @FormUrlEncoded
    @POST("adddraft/")
    Call<Message> adddraft(@Field("UserID") int userid, @Field("Sender") String sender, @Field("Receiver") String receiver, @Field("Reply") String reply,
                           @Field("Forward") String forward, @Field("Subject") String subject, @Field("Body") String body,
                           @Field("Cc") String cc,@Field("Bcc") String bcc,@Field("Attachment") String attachment);

    @FormUrlEncoded
    @POST("movedraft/")
    Call<Message> movedraft(@Field("MessageID") int MessageID);

    @FormUrlEncoded
    @POST("deletedraft/")
    Call<Message> deletedraft(@Field("MessageID") int messageid);

    @FormUrlEncoded
    @POST("send/")
    Call<Message> send(@Field("UserID") int UserID, @Field("messagefrom") String sender, @Field("messagefromname") String sendername,@Field("messageto") String receiver,
                            @Field("messagesubject") String subject, @Field("messagebody") String body,
                           @Field("messagecc") String cc,@Field("messagebcc") String bcc);

    @FormUrlEncoded
    @POST("reply/")
    Call<Message> reply(@Field("UserID") int UserID, @Field("messagefrom") String sender, @Field("messagefromname") String sendername,@Field("messageto") String receiver,
                       @Field("messagesubject") String subject, @Field("messagebody") String body,
                       @Field("messagecc") String cc,@Field("messagebcc") String bcc, @Field("Reply") String Reply);

//    @FormUrlEncoded
//    @POST("send/")
//    Call<Message> send(@Field("UserID") int UserID, @Field("Sender") String sender, @Field("Sendername") String sendername,@Field("Receiver") String receiver,
//                       @Field("Subject") String subject, @Field("Body") String body,
//                       @Field("Cc") String cc,@Field("Bcc") String bcc);


    @FormUrlEncoded
    @POST("read/")
    Call<List<Message>> read(@Field("UserID") int userid, @Field("Email") String email, @Field("Password") String password);

    @FormUrlEncoded
    @POST("readspam/")
    Call<List<Message>> readspam(@Field("UserID") int userid, @Field("Email") String email, @Field("Password") String password);

    @FormUrlEncoded
    @POST("readinbox/")
    Call<Message> readinbox(@Field("MessageID") int MessageID);

    @FormUrlEncoded
    @POST("moveinbox/")
    Call<Message> moveinbox(@Field("MessageID") int MessageID);

    @FormUrlEncoded
    @POST("movetoinbox/")
    Call<Message> movetoinbox(@Field("MessageID") int MessageID);

    @FormUrlEncoded
    @POST("movetodraft/")
    Call<Message> movetodraft(@Field("MessageID") int MessageID);

    @FormUrlEncoded
    @POST("deleteinbox/")
    Call<Message> deleteinbox(@Field("MessageID") int MessageID);

    @FormUrlEncoded
    @POST("deletespam/")
    Call<Message> deletespam(@Field("MessageID") int MessageID);

    @FormUrlEncoded
    @POST("getsent/")
    Call<List<Message>> getsent(@Field("UserID") int userid);

    @FormUrlEncoded
    @POST("movesent/")
    Call<Message> movesent(@Field("MessageID") int MessageID);

    @FormUrlEncoded
    @POST("deletesent/")
    Call<Message> deletesent(@Field("MessageID") int MessageID);

    @FormUrlEncoded
    @POST("gettrash/")
    Call<List<Message>> gettrash(@Field("UserID") int userid);

    @FormUrlEncoded
    @POST("deletetrash/")
    Call<Message> deletetrash(@Field("MessageID") int messageid);

    @FormUrlEncoded
    @POST("getstarred/")
    Call<List<Message>> getstarred(@Field("UserID") int userid);

    @FormUrlEncoded
    @POST("starred/")
    Call<Message> starred(@Field("MessageID") int messageid);

    @FormUrlEncoded
    @POST("markasspam/")
    Call<Message> markasspam(@Field("MessageID") int messageid, @Field("UserID") int userid);

    @FormUrlEncoded
    @POST("markasnonspam/")
    Call<Message> markasnonspam(@Field("MessageID") int messageid, @Field("UserID") int userid);
}

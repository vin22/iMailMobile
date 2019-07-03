package com.application.imail.remote;

public class APIUtils {

    private APIUtils() {

    }

    public static final String API_URL = "http://192.168.43.234/WebApi/api/";
    //192.168.100.5
    //192.168.43.38
    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }
    public static ContactService getContactService(){
        return RetrofitClient.getClient(API_URL).create(ContactService.class);
    }
    public static DomainService getDomainService(){
        return RetrofitClient.getClient(API_URL).create(DomainService.class);
    }
    public static MessageService getMessageService(){
        return RetrofitClient.getClient(API_URL).create(MessageService.class);
    }
}


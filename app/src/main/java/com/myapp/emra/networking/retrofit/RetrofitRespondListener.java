package com.myapp.emra.networking.retrofit;

/**
 * This interface contains common methods which are invoked by retrofit GET/POST json request function calls.
 */

import retrofit2.Response;

public interface RetrofitRespondListener {

    void onRetrofitSuccess(Response<?> response, String requestName);

    void onRetrofitFailure(String responseError, String requestName);
}

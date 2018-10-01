package br.com.joaoapps.faciplac.carona.service;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by joaov on 19/11/2017.
 */

public class ServicePost {

    public static String post(String url, RequestBody requestBody) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Content-Type", "application/json")
                .header("Authorization", "key=AIzaSyDiWd8jsQrGaTWfDvmLypvtWQDbKa1LjXg ")
                .build();

        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return response.body().string();
    }
}

package com.r4z0r.pridesphere.geoapify;

import com.google.gson.Gson;
import com.r4z0r.pridesphere.geoapify.models.reverseGeocoding.ReverseGeocoding;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class Api {
    private String apiKey;
    @Autowired
    public Api(Environment env) {
        apiKey = env.getProperty("geoapify.data.config.apiKey");
    }
    public ReverseGeocoding reverseGeocoding(Double lat, Double lon) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.geoapify.com/v1/geocode/reverse?lat=" + lat + "&lon=" + lon + "&apiKey=" + apiKey).method("GET", null).build();
        Response response = client.newCall(request).execute();
        return new Gson().fromJson(response.body().string(), ReverseGeocoding.class);
    }
}

package com.r4z0r.pridesphere.geoapify.models.endereco;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Query {
    private Double lat;
    private Double lon;
    @SerializedName("plus_code")
    private String plusCode;
}

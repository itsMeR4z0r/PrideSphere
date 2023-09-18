package com.r4z0r.pridesphere.geoapify.models.reverseGeocoding;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Properties {
    private Datasource datasource;
    private String country;
    @SerializedName("country_code")
    private String countryCode;
    private String region;
    private String state;
    private String county;
    private String city;
    private String municipality;
    private String postcode;
    private String suburb;
    private String street;
    private String housenumber;
    private Double lon;
    private Double lat;
    @SerializedName("state_code")
    private String stateCode;
    private Double distance;
    @SerializedName("result_type")
    private String resultType;
    private String district;
    private String formatted;
    @SerializedName("address_line1")
    private String addressLine1;
    @SerializedName("address_line2")
    private String addressLine2;
    private String category;
    private Timezone timezone;
    @SerializedName("plus_code")
    private String plusCode;
    @SerializedName("plus_code_short")
    private String plusCodeShort;
    private Rank rank;
    @SerializedName("place_id")
    private String placeId;
}

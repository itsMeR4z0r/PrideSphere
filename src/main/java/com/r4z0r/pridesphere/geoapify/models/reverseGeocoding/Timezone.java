package com.r4z0r.pridesphere.geoapify.models.reverseGeocoding;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Timezone {
    private String name;
    @SerializedName("offset_STD")
    private String offsetSTD;
    @SerializedName("offset_STD_seconds")
    private Integer offsetSTDSeconds;
    @SerializedName("offset_DST")
    private String offsetDST;
    @SerializedName("offset_DST_seconds")
    private Integer offsetDSTSeconds;
}

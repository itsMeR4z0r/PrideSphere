package com.r4z0r.pridesphere.geoapify.models.reverseGeocoding;

import lombok.Data;

import java.util.List;

@Data
public class ReverseGeocoding {
    private String type;
    private List<Feature> features;
    private Query query;
}


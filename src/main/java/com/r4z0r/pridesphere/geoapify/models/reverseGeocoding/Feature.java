package com.r4z0r.pridesphere.geoapify.models.reverseGeocoding;

import lombok.Data;

import java.util.List;

@Data
public class Feature {
    private String type;
    private Properties properties;
    private Geometry geometry;
    private List<Double> bbox;
}

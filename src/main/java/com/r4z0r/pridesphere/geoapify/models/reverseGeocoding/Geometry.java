package com.r4z0r.pridesphere.geoapify.models.reverseGeocoding;

import lombok.Data;

import java.util.List;

@Data
public class Geometry {
    private String type;
    private List<Double> coordinates;
}

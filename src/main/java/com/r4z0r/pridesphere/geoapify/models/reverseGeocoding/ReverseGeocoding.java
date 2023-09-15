package com.r4z0r.pridesphere.geoapify.models.endereco;

import lombok.Data;

import java.util.List;

@Data
public class Endereco {
    private String type;
    private List<Feature> features;
    private Query query;
}


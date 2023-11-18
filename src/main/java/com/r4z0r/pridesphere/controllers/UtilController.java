package com.r4z0r.pridesphere.controllers;

import com.r4z0r.pridesphere.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/util")
public class UtilController {
    @Value("${urlUI}")
    private String urlUI;

    @Autowired
    private ConfigurableEnvironment env;

    public void updateProperty(String key, String value) {
        MutablePropertySources propertySources = env.getPropertySources();
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        propertySources.addFirst(new MapPropertySource("runtime-updates", map));
    }

    @PostMapping("/updateUIUrl")
    public String updateUIUrl(@RequestBody String data) {
        JSONObject returnJson = new JSONObject();
        String returnString = "";
        Util util = new Util(env.getProperty("crypto.key.encryption"));
        try {
            returnJson.put("key", env.getProperty("crypto.key.validation"));
            JSONObject json = new JSONObject(data);
            var decrypted = util.decrypt(json.getString("data"));
            json = new JSONObject(decrypted);
            if (!json.has("key") || !json.has("url")) {
                returnJson.put("status", "error");
                returnJson.put("message", "Dados inválidos");
            } else {
                if (!json.getString("key").equals(env.getProperty("crypto.key.validation"))) {
                    returnJson.put("status", "error");
                    returnJson.put("message", "Chave de validação inválida");
                } else {
                    System.setProperty("urlUI", json.getString("url"));
                    updateProperty("urlUI", json.getString("url"));
                    returnJson.put("status", "success");
                    returnJson.put("message", "URL atualizada com sucesso");
                }
            }
            returnJson.put("key", env.getProperty("crypto.key.validation"));
            returnString = util.encrypt(returnJson.toString());
        } catch (GeneralSecurityException | JSONException e) {
            throw new RuntimeException(e);
        }
        return returnString;
    }

}

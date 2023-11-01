package com.r4z0r.pridesphere.controllers;

import com.google.zxing.WriterException;
import com.r4z0r.pridesphere.Util;
import com.r4z0r.pridesphere.entity.AdminSession;
import com.r4z0r.pridesphere.entity.Usuario;
import com.r4z0r.pridesphere.repositories.AdminRepository;
import com.r4z0r.pridesphere.repositories.AdminSessionRepository;
import com.r4z0r.pridesphere.repositories.UsuarioRepository;
import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.GeneralSecurityException;
import java.util.UUID;

import static com.r4z0r.pridesphere.Util.gerarQrCode;


@RestController()
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private Environment env;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminSessionRepository adminSessionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/validateSession")
    public String validateSession(@RequestBody String data){
        JSONObject returnJson = new JSONObject();
        String returnString = "";
        Util util = new Util(env.getProperty("crypto.key.encryption"));
        try {
            returnJson.put("key", env.getProperty("crypto.key.validation"));
            JSONObject json = new JSONObject(data);
            var decrypted = util.decrypt(json.getString("data"));
            json = new JSONObject(decrypted);
            if (!json.has("key") || !json.has("sid") || !json.has("userAgent")) {
                returnJson.put("key", env.getProperty("crypto.key.validation"));
                returnJson.put("error", "Dados inválidos");
                returnString = util.encrypt(returnJson.toString());
                return returnString;
            }else{
                if (json.getString("key").equals(env.getProperty("crypto.key.validation"))) {
                    var session = adminSessionRepository.findById(UUID.fromString(json.getString("sid")));
                    if(session.isPresent()){
                        var sessionAdmin = session.get();
                        if(sessionAdmin.getUserAgent().equals(json.getString("userAgent")) && sessionAdmin.isLogado()){
                            returnJson.put("Success","requisicao verificada");
                            returnJson.put("sid",sessionAdmin.getId());
                            returnJson.put("user",new JSONObject(sessionAdmin.getAdmin().toString()));
                        }else{
                            returnJson.put("error", "Requisicao invalida");
                        }
                    }else{
                        returnJson.put("error", "Requisicao nao encontrada");
                    }
                }else {
                    returnJson.put("error", "Chave inválida");
                }
            }
            returnJson.put("key", env.getProperty("crypto.key.validation"));
            returnString = util.encrypt(returnJson.toString());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return returnString;
    }

    @PostMapping("/check")
    public String checkQrCodeLogin(@RequestBody String data){
        JSONObject returnJson = new JSONObject();
        String returnString = "";
        Util util = new Util(env.getProperty("crypto.key.encryption"));
        try {
            returnJson.put("key", env.getProperty("crypto.key.validation"));
            JSONObject json = new JSONObject(data);
            var decrypted = util.decrypt(json.getString("data"));
            json = new JSONObject(decrypted);
            if (!json.has("key") || !json.has("sid")) {
                returnJson.put("key", env.getProperty("crypto.key.validation"));
                returnJson.put("error", "Dados inválidos");
                returnString = util.encrypt(returnJson.toString());
                return returnString;
            }else{
                if (json.getString("key").equals(env.getProperty("crypto.key.validation"))) {
                    var session = adminSessionRepository.findById(UUID.fromString(json.getString("sid")));
                    if(session.isPresent()){
                        if(session.get().isLogado() && !session.get().isUsado()){
                            var sessionAdmin = session.get();
                            sessionAdmin.setUsado(true);
                            adminSessionRepository.save(sessionAdmin);
                            returnJson.put("Success","requisicao verificada");
                            returnJson.put("sid",sessionAdmin.getId());
                        }else{
                            if(session.get().isUsado()){
                                returnJson.put("error", "requisicao ja usada");
                            }else {
                                returnJson.put("error", "requisicao nao verificada");
                            }
                        }
                    }else{
                        returnJson.put("error", "Requisicao nao encontrada");
                    }
                }else {
                    returnJson.put("error", "Chave inválida");
                }
            }
            returnJson.put("key", env.getProperty("crypto.key.validation"));
            returnString = util.encrypt(returnJson.toString());
        }catch (GeneralSecurityException | JSONException e) {
            throw new RuntimeException(e);
        }
        return returnString;
    }

    @PostMapping("/validate")
    public String loginValidate(@RequestBody String data) {
        JSONObject returnJson = new JSONObject();
        String returnString = "";
        Util util = new Util(env.getProperty("crypto.key.encryption"));
        try {
            returnJson.put("key", env.getProperty("crypto.key.validation"));
            JSONObject json = new JSONObject(data);
            var decrypted = util.decrypt(json.getString("data"));
            json = new JSONObject(decrypted);
            if (!json.has("key") || !json.has("qrCodeMessage") || !json.has("requestId")) {
                returnJson.put("key", env.getProperty("crypto.key.validation"));
                returnJson.put("error", "Dados inválidos");
                returnString = util.encrypt(returnJson.toString());
                return returnString;
            }
            if (json.getString("key").equals(env.getProperty("crypto.key.validation"))) {
                JSONObject qrcode = new JSONObject(util.decrypt(json.getString("qrCodeMessage")));
                var session = adminSessionRepository.findById(UUID.fromString(qrcode.getString("sid")));
                var usuario = usuarioRepository.findByidPlataforma(json.getString("requestId"));
                if (session.isPresent()) {
                    if (session.get().isUsado() || session.get().isLogado()) {
                        returnJson.put("error", "Sessão não validada");
                    } else {
                        if (usuario.isPresent()) {
                            var sessionAdm = session.get();
                            sessionAdm.setLogado(true);

                            if (sessionAdm.getAdmin().getUsuario() == null) {
                                sessionAdm.getAdmin().setUsuario(usuario.get());
                                sessionAdm = adminSessionRepository.save(sessionAdm);
                            }

                            if (sessionAdm.getAdmin().getUsuario().getIdPlataforma().equals(usuario.get().getIdPlataforma())) {
                                adminSessionRepository.save(sessionAdm);
                                returnJson.put("sid", session.get().getId());
                                returnJson.put("success", true);
                            } else {
                                returnJson.put("error", "Usuário já vinculado");
                            }
                        } else {
                            returnJson.put("error", "Usuário não encontrado");
                        }
                    }
                } else {
                    returnJson.put("error", "Sessão não encontrada");
                }
            } else {
                returnJson.put("error", "Chave inválida");
            }
            returnJson.put("key", env.getProperty("crypto.key.validation"));
            returnString = util.encrypt(returnJson.toString());
        } catch (GeneralSecurityException | JSONException e) {
            throw new RuntimeException(e);
        }
        return returnString;
    }

    @PostMapping
    public String login(@RequestBody String data) {
        JSONObject returnJson = new JSONObject();
        String returnString = "";
        Util util = new Util(env.getProperty("crypto.key.encryption"));
        try {
            JSONObject json = new JSONObject(data);
            var decrypted = util.decrypt(json.getString("data"));
            json = new JSONObject(decrypted);

            if (!json.has("key") || !json.has("email") || !json.has("ip") || !json.has("userAgent")) {
                returnJson.put("key", env.getProperty("crypto.key.validation"));
                returnJson.put("error", "Dados inválidos");
                returnString = util.encrypt(returnJson.toString());
                return returnString;
            }

            if (json.getString("key").equals(env.getProperty("crypto.key.validation"))) {
                var admin = adminRepository.findByEmail(json.getString("email"));
                if (admin.isPresent()) {

                    AdminSession adminSession = new AdminSession();
//                    if (!json.getString("ip").equals("::1")) {
//                        try {
//                            IPinfo ipInfo = new IPinfo.Builder().setToken(env.getProperty("ipinfo.token")).build();
//                            IPResponse response = ipInfo.lookupIP(json.getString("ip"));
//                            adminSession.setDevice(response.getHostname());
//                            adminSession.setOs(response.getOrg());
//                        } catch (RateLimitedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    adminSession.setRequestId(json.getString("requestId"));
                    adminSession.setAdmin(admin.get());
                    adminSession.setIp(json.getString("ip"));
                    adminSession.setUserAgent(json.getString("userAgent"));
                    var session = adminSessionRepository.save(adminSession);
                    returnJson.put("sid", session.getId());
                    JSONObject tmp = new JSONObject();
                    tmp.put("sid", session.getId());
                    tmp.put("key", env.getProperty("crypto.key.validation"));
                    returnJson.put("qrcode", gerarQrCode(util.encrypt(tmp.toString())));
                } else {
                    returnJson.put("error", "Usuário não encontrado");
                }

            } else {
                returnJson.put("error", "Chave inválida");
            }

            returnJson.put("key", env.getProperty("crypto.key.validation"));
            returnString = util.encrypt(returnJson.toString());
        } catch (GeneralSecurityException | JSONException | WriterException e) {
            throw new RuntimeException(e);
        }
        return returnString;
    }
}

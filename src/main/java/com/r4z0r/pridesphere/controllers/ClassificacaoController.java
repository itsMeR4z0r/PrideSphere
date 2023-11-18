package com.r4z0r.pridesphere.controllers;

import com.r4z0r.pridesphere.Util;
import com.r4z0r.pridesphere.entity.Classificacao;
import com.r4z0r.pridesphere.enums.TipoClassificacao;
import com.r4z0r.pridesphere.services.ClassificacaoService;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;

@RestController()
@RequestMapping("/classificacao")
public class ClassificacaoController {
    private final Environment env;
    private final ClassificacaoService classificacaoService;
    private final Util util;

    public ClassificacaoController(Environment env, ClassificacaoService classificacaoService) {
        this.env = env;
        this.classificacaoService = classificacaoService;
        this.util = new Util(env.getProperty("crypto.key.encryption"));
    }

    /**
     * @param data - JSON com os dados da classificação
     *             <p> Exemplo de JSON:
     *             <p> {
     *             <p> "key": "chave de validação",
     *             <p> "descricao": "descricao da classificacao",
     *             <p> "tipo": "tipo da classificacao",
     *             <p> "id": "id da classificacao"
     *             <p> }
     *             <p> Onde o tipo pode ser: "CATEGORIA" ou "SUBCATEGORIA"
     * @return - JSON com a mensagem de sucesso ou erro
     */
    @PostMapping("/editar")
    public String editar(@RequestBody String data) {
        try {
            JSONObject returnJson = new JSONObject();
            JSONArray lista = new JSONArray();

            String returnString = "";
            var decrypted = decryptData(data, util);
            JSONObject json = new JSONObject(decrypted);
            if (validateData(json, returnJson)) {
                returnString = encryptData(returnJson.toString());
                return returnString;
            }
            String idString = json.getString("id");
            if (isValidUUID(idString)) {
                returnJson.put("error", "error");
                returnJson.put("message", "Invalid id");
                returnString = encryptData(returnJson.toString());
                return returnString;
            }
            UUID id = fromString(idString);
            Classificacao classificacao = classificacaoService.findById(id).orElseThrow(() -> {
                return new RuntimeException("Classificacao not found");
            });
            classificacao.setDescricao(json.getString("descricao"));
            classificacaoService.save(classificacao);
            returnJson.put("success", "success");
            returnJson.put("message", "Classificação editada com sucesso");
            returnString = encryptData(returnJson.toString());
            return returnString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param data - JSON com os dados da classificação
     *             <p> Exemplo de JSON:
     *             <p> {
     *             <p> "key": "chave de validação",
     *             <p> "descricao": "descricao da classificacao",
     *             <p> "tipo": "tipo da classificacao"
     *             <p> }
     *             <p> Onde o tipo pode ser: "CATEGORIA" ou "SUBCATEGORIA"
     * @return - JSON com a mensagem de sucesso ou erro
     */
    @PostMapping("/obter")
    public String obter(@RequestBody String data) {
        try {
            JSONObject returnJson = new JSONObject();
            String returnString = "";
            var decrypted = decryptData(data, util);
            JSONObject json = new JSONObject(decrypted);
            if (validateData(json, returnJson)) {
                returnString = encryptData(returnJson.toString());
                return returnString;
            }
            String idString = json.getString("id");
            if (isValidUUID(idString)) {
                returnJson.put("error", "error");
                returnJson.put("message", "Invalid id");
                returnString = encryptData(returnJson.toString());
                return returnString;
            }
            UUID id = fromString(idString);
            Classificacao classificacao = classificacaoService.findById(id).orElseThrow(() -> new RuntimeException("Classificacao not found"));
            JSONObject tmp = new JSONObject();
            tmp.put("id", classificacao.getId());
            tmp.put("nome", classificacao.getDescricao());
            tmp.put("tipo", classificacao.getTipoClassificacao());
            returnJson.put("item", tmp);
            returnJson.put("success", "success");
            returnJson.put("message", "Classificação obtida com sucesso");
            returnString = encryptData(returnJson.toString());
            return returnString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param data - JSON com os dados da classificação
     *             <p> Exemplo de JSON:
     *             <p> {
     *             <p> "key": "chave de validação",
     *             <p> "id": "id da classificacao"
     *             <p> }
     * @return - JSON com a mensagem de sucesso ou erro
     */
    @PostMapping("/deletar")
    public String deletar(@RequestBody String data) {
        try {
            JSONObject returnJson = new JSONObject();
            JSONArray lista = new JSONArray();
            String returnString = "";
            var decrypted = decryptData(data, util);
            JSONObject json = new JSONObject(decrypted);
            if (validateData(json, returnJson)) {
                returnString = encryptData(returnJson.toString());
                return returnString;
            }
            String idString = json.getString("id");
            if (isValidUUID(idString)) {
                returnJson.put("error", "error");
                returnJson.put("message", "Invalid id");
                returnString = encryptData(returnJson.toString());
                return returnString;
            }
            UUID id = fromString(idString);
            classificacaoService.deleteById(id);
            returnJson.put("success", "success");
            returnJson.put("message", "Classificação deletada com sucesso");
            returnString = encryptData(returnJson.toString());
            return returnString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param data - JSON com os dados da classificação
     *             <p> Exemplo de JSON:
     *             <p> {
     *             <p> "key": "chave de validação",
     *             <p> "descricao": "descricao da classificacao",
     *             <p> "tipo": "tipo da classificacao"
     *             <p> }
     *             <p> Onde o tipo pode ser: "CATEGORIA" ou "SUBCATEGORIA"
     * @return - JSON com a mensagem de sucesso ou erro
     */

    @PostMapping("/cadastrar")
    public String cadastrar(@RequestBody String data) {
        try {
            JSONObject returnJson = new JSONObject();
            JSONArray lista = new JSONArray();
            String returnString = "";
            var decrypted = decryptData(data, util);
            Classificacao classificacao = new Classificacao();
            JSONObject json = new JSONObject(decrypted);
            if (validateData(json, returnJson)) {
                returnString = encryptData(returnJson.toString());
                return returnString;
            }
            classificacao.setDescricao(json.getString("descricao"));
            String tipo = json.getString("tipo");
            if (isValidTipo(tipo)) {
                classificacao.setTipoClassificacao(TipoClassificacao.valueOf(tipo));
                classificacaoService.save(classificacao);
                returnJson.put("success", "success");
                returnJson.put("message", "Classificação cadastrada com sucesso");
                returnString = encryptData(returnJson.toString());
                return returnString;
            } else {
                returnJson.put("error", "error");
                returnJson.put("message", "Tipo de classificação inválido");
                returnString = encryptData(returnJson.toString());
                return returnString;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param tipo - Tipo da classificação
     *             <p> Onde o tipo pode ser: "CATEGORIA" ou "SUBCATEGORIA"
     * @return - JSON com a mensagem de sucesso ou erro
     * <p> Exemplo de JSON:
     * <p> {
     * <p> "lista": [
     * <p> {
     * <p> "id": "id da classificacao",
     * <p> "nome": "nome da classificacao",
     * <p> "tipo": "tipo da classificacao"
     * <p> }
     * <p> ],
     * <p> "success": "success",
     * <p> "key": "chave de validação"
     * <p> }
     */
    @GetMapping("/lista")
    public String listagem(@RequestParam TipoClassificacao tipo) {
        try {
            JSONObject returnJson = new JSONObject();
            JSONArray lista = new JSONArray();
            String returnString = "";
            List<Classificacao> classificacoes = classificacaoService.findByTipoClassificacao(tipo);
            for (Classificacao classificacao : classificacoes) {
                JSONObject tmp = new JSONObject();
                tmp.put("id", classificacao.getId());
                tmp.put("nome", classificacao.getDescricao());
                tmp.put("tipo", classificacao.getTipoClassificacao());
                lista.put(tmp);
            }
            returnJson.put("lista", lista);
            returnJson.put("success", "success");
            returnJson.put("key", env.getProperty("crypto.key.validation"));
            returnString = encryptData(returnJson.toString());
            return returnString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param data - JSON com os dados da classificação
     *             <p> Exemplo de JSON:
     *             <p> {
     *             <p> "key": "chave de validação",
     *             <p> "descricao": "descricao da classificacao",
     *             <p> "tipo": "tipo da classificacao"
     *             <p> }
     * @param util - Classe utilitária para criptografia
     * @return - JSON com a mensagem de sucesso ou erro
     * @throws GeneralSecurityException - Exceção de segurança
     */
    private String decryptData(String data, Util util) throws GeneralSecurityException {
        String decrypted = null;
        try {
            JSONObject jsonData = new JSONObject(data);
            if (jsonData.has("data")) {
                decrypted = util.decrypt(jsonData.getString("data"));
            } else {
                throw new GeneralSecurityException("Invalid data format");
            }
        } catch (JSONException e) {
            throw new GeneralSecurityException(e);
        }
        return decrypted;
    }

    /**
     * @param json       - JSON com os dados da classificação
     *                   <p> Exemplo de JSON:
     *                   <p> {
     *                   <p> "key": "chave de validação",
     *                   <p> "descricao": "descricao da classificacao",
     *                   <p> "tipo": "tipo da classificacao"
     *                   <p> }
     *                   <p> Onde o tipo pode ser: "CATEGORIA" ou "SUBCATEGORIA"
     *                   <p> Onde o id é opcional
     *                   <p> Onde a chave de validação é a mesma chave de validação do arquivo application.properties
     *                   <p> Onde a chave de criptografia é a mesma chave de criptografia do arquivo application.properties
     * @param returnJson - JSON de retorno
     *                   <p> Exemplo de JSON:
     *                   <p> {
     *                   <p> "error": "error",
     *                   <p> "message": "mensagem de erro"
     *                   <p> }
     *                   <p> Onde o error é opcional
     *                   <p> Onde a mensagem é opcional
     * @return - true se os dados forem inválidos, false se os dados forem válidos
     * @throws JSONException - Exceção de JSON
     */
    private boolean validateData(JSONObject json, JSONObject returnJson) throws JSONException {
        if (!json.has("key") || !json.has("descricao") || !json.has("tipo") || !json.has("id")) {
            returnJson.put("error", "error");
            returnJson.put("message", "Dados inválidos");
            return true;
        } else {
            if (!json.getString("key").equals(env.getProperty("crypto.key.validation"))) {
                returnJson.put("error", "error");
                returnJson.put("message", "Chave de validação inválida");
                return true;
            }
        }
        return false;
    }

    /**
     * @param data - JSON com os dados da classificação
     * @return - JSON com a mensagem de sucesso ou erro
     * @throws GeneralSecurityException - Exceção de segurança
     */
    private String encryptData(String data) throws GeneralSecurityException {
        return util.encrypt(data);
    }

    /**
     * @param idString - String com o id da classificação
     * @return - true se o id for inválido, false se o id for válido
     */
    private boolean isValidUUID(String idString) {
        try {
            fromString(idString);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * @param tipo - String com o tipo da classificação
     * @return - true se o tipo for inválido, false se o tipo for válido
     */
    private boolean isValidTipo(String tipo) {
        try {
            TipoClassificacao.valueOf(tipo);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

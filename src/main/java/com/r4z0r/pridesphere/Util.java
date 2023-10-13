package com.r4z0r.pridesphere;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Random;

public class Util {
    private String key;

    public Util(String encryptedKey) {
        this.key = encryptedKey;
    }

    public String encrypt(String value) throws GeneralSecurityException {

        byte[] raw = key.getBytes(StandardCharsets.UTF_8);

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(byte[] encrypted) throws GeneralSecurityException {

        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(encrypted);

        return new String(original, StandardCharsets.UTF_8);
    }

    public String gerarCodigo(int tamanho) {
        if (tamanho <= 0) {
            throw new IllegalArgumentException("O tamanho do código deve ser maior que zero.");
        }

        Random rand = new Random();
        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < tamanho; i++) {
            int digito = rand.nextInt(10); // Gera um dígito aleatório de 0 a 9
            codigo.append(digito);
        }

        return codigo.toString();
    }
}

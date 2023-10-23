package com.r4z0r.pridesphere;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Random;

public class Util {
    private final String key;

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

    public String decrypt(String encrypted) throws GeneralSecurityException {

        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

        return new String(original, StandardCharsets.UTF_8);
    }

    public static String gerarCodigo(int tamanho) {
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

    public static String gerarQrCode(String data) throws WriterException {
        String ret = null;
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
        var byteArray = toByteArray(MatrixToImageWriter.toBufferedImage(bitMatrix), "png");
        ret = Base64.getEncoder().encodeToString(byteArray);
        ret = ret.replace(System.lineSeparator(), "");
        return ret;
    }

    private static byte[] toByteArray(BufferedImage img, String imageFileType) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, imageFileType, baos);
            return baos.toByteArray();
        } catch (Throwable e) {
            throw new RuntimeException();
        }
    }
}

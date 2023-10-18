package com.r4z0r.pridesphere;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
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

    private static BufferedImage toBufferedImage(Mat mat) {
        byte[] data = new byte[mat.cols() * mat.rows() * (int) mat.elemSize()];
        mat.get(0, 0, data);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_BYTE_GRAY);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return image;
    }

    public static String readQrCode(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        var stringresult = "";
        // Load the OpenCV library
        nu.pattern.OpenCV.loadShared();
        // Load the OpenCV Haar Cascade classifier for QR codes
        CascadeClassifier classifier = new CascadeClassifier("haarcascade_qrcode.xml");


        BufferedImage screenCapture = ImageIO.read(inputStream);

        // Convert the screen capture to a Mat object
        Mat image = new Mat();
        image.put(0, 0, screenCapture.getRGB(0, 0, screenCapture.getWidth(), screenCapture.getHeight(), null, 0, screenCapture.getWidth()));

        // Convert the Mat object to a grayscale image
        Mat grayscaleImage = new Mat();
        Imgproc.cvtColor(image, grayscaleImage, Imgproc.COLOR_BGR2GRAY);

        // Detect QR codes in the image
        MatOfRect qrCodes = new MatOfRect();
        classifier.detectMultiScale(grayscaleImage, qrCodes);

        // Loop through each detected QR code
        for (Rect qrCode : qrCodes.toArray()) {
            // Crop the image to the QR code region
            Mat qrCodeImage = grayscaleImage.submat(qrCode);


            // Convert the Mat object to a BufferedImage
            BufferedImage bufferedImage = toBufferedImage(qrCodeImage);

            // Create a LuminanceSource from the BufferedImage
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);

            // Create a BinaryBitmap from the LuminanceSource
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // Create a MultiFormatReader
            MultiFormatReader reader = new MultiFormatReader();

            // Configure the reader
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            reader.setHints(hints);

            // Decode the QR code
            try {
                Result result = reader.decode(bitmap);
                stringresult += result.getText();
            } catch (NotFoundException e) {
                return "No QR code found.";
            }
        }
        return stringresult;
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

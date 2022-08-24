package com.group4.qrcodepayment.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

@Component
public class QRCodeGenerator {
    private static final String LOGO= "https://i.imgur.com/qtdPESn.png";
    public static byte[] getQRcodeImage(String content, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix matrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(matrix, "PNG", byteArrayOutputStream );
//       return Base64.getEncoder().encode( byteArrayOutputStream.toByteArray());
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

        MatrixToImageConfig matrixToImageConfig  =
                new MatrixToImageConfig(MatrixToImageConfig.BLACK,MatrixToImageConfig.WHITE);

        // Load QR image
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);

        // Load logo image
        BufferedImage overly = getOverly();


        // Calculate the delta height and width between QR code and logo
        int deltaHeight = qrImage.getHeight() - overly.getHeight();
        int deltaWidth = qrImage.getWidth() - overly.getWidth();

        // Initialize combined image
        BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) combined.getGraphics();

        // Write QR code to new image at position 0/0
        g.drawImage(qrImage, 0, 0, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Write logo into combine image at position (deltaWidth / 2) and
        // (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
        // the same space for the logo to be centered
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);
        ImageIO.write(combined, "png", byteArrayOutputStream);
        return Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
    }
    private static BufferedImage getOverly() throws IOException {
        URL url = new URL(QRCodeGenerator.LOGO);
        return ImageIO.read(url);
    }
}


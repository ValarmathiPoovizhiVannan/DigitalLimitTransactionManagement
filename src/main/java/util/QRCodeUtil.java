package util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import jakarta.servlet.ServletOutputStream;

import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

public class QRCodeUtil {            
	public static void generateQRCode(String qrData, ServletOutputStream outputStream) 
    	throws WriterException, IOException {

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrData,
                    BarcodeFormat.QR_CODE,
                    250,
                    250
            );

            BufferedImage image = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < 250; x++) {
                for (int y = 0; y < 250; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            ImageIO.write(image, "PNG", outputStream);
        }
		
	}


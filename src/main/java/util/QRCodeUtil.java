package util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import jakarta.servlet.ServletOutputStream;

import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
 
public class QRCodeUtil {   
	private QRCodeUtil() {}
	private static final int MAX_LOGIN_ATTEMPTS = 250;

	public static void generateQRCode(String qrData, ServletOutputStream outputStream) 
    	throws WriterException, IOException {

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrData,
                    BarcodeFormat.QR_CODE,
                    MAX_LOGIN_ATTEMPTS,
                    MAX_LOGIN_ATTEMPTS
            );

            BufferedImage image = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < MAX_LOGIN_ATTEMPTS; x++) {
                for (int y = 0; y < MAX_LOGIN_ATTEMPTS; y++) {
                	if (bitMatrix.get(x, y)) {
                	    image.setRGB(x, y, 0xFF000000);
                	    return;
                	}
                	image.setRGB(x, y, 0xFFFFFFFF);
                }
            }

            ImageIO.write(image, "PNG", outputStream);
        }
		
	}


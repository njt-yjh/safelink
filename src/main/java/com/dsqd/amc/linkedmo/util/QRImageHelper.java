package com.dsqd.amc.linkedmo.util;

import static spark.Spark.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
public class QRImageHelper {

	    public void generateQRCodeImage(String text, ByteArrayOutputStream stream, int width, int height) throws WriterException, IOException {
	        QRCodeWriter qrCodeWriter = new QRCodeWriter();
	        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
	        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
	    }

}

package com.salmandhariwala.helloworld;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.openalpr.api.DefaultApi;
import com.openalpr.api.models.InlineResponse200;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class AlprWrapper {

    private static final String country = "in";
    private static final String secretKey = "sk_7a15ccad60df4cd781ed7971";
    private static final Integer recognizeVehicle = 0;
    private static final String state = "";
    private static final Integer returnImage = 0;
    private static final Integer topn = 1;
    private static final String prewarp = "";

    public static String recognize(File file) {

        String result = null;

        try {

            String imageDataString = getFileToByte(file.getAbsolutePath());

            Log.i("AplrWrapper", imageDataString);
            result = recognize(imageDataString);

        } catch (Exception e) {
            Log.e("AplrWrapper", "", e);
        }

        return result;

    }

    // put the image file path into this method
    public static String getFileToByte(String filePath) {

        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 10, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;

    }

    public static String recognize(String imageString) {

        String result = null;

        try {
            DefaultApi apiInstance = new DefaultApi();

            InlineResponse200 _result = apiInstance.recognizeBytes(imageString, secretKey, country, recognizeVehicle,
                    state, returnImage, topn, prewarp);

            System.out.println(_result);

            String plate = _result.getResults().get(0).getPlate();

            result = plate;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}


package com.vamsi.open_cv_assignment.miscellaneous;

import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;

public class DataConverter {

    public static byte[] convertImage2ByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0,stream);

        return stream.toByteArray();
    }
}


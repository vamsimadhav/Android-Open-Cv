package com.vamsi.open_cv_assignment.gallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.vamsi.open_cv_assignment.R;
import com.vamsi.open_cv_assignment.miscellaneous.Constants;
import com.vamsi.open_cv_assignment.miscellaneous.DataConverter;
import com.vamsi.open_cv_assignment.room_database.model.UserImage;
import com.vamsi.open_cv_assignment.room_database.viewmodel.UserImageViewModel;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GalleryActivity extends AppCompatActivity {

    private ImageView galleryIMGVIEW;
    private Button openGallery,processImage;
    private Uri selectedPhotoUri;
    UserImageViewModel userImageViewModel;
    UserImage userImage;
    Bitmap finalGalleryImageBitmap = null;
    Bitmap bmp;
    String galleryImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        userImageViewModel = new ViewModelProvider(this).get(UserImageViewModel.class);
        userImage = new UserImage();

        galleryIMGVIEW = findViewById(R.id.galleryImage);
        openGallery = findViewById(R.id.openGalleryButton);
        processImage = findViewById(R.id.processImageButton);

        galleryImageName = getIntent().getStringExtra("gallery_image_name");

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, Constants.REQUEST_PHOTO_GALLERY);
            }
        });

        processImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalGalleryImageBitmap == null){
                    Toast.makeText(GalleryActivity.this,"Please load the image from Gallery",Toast.LENGTH_SHORT).show();
                }
                else{
                    galleryIMGVIEW.setImageBitmap(finalGalleryImageBitmap);
                    //Add to database
                    userImage.setNormalImage(DataConverter.convertImage2ByteArray(bmp));
                    userImage.setProcessedImage(DataConverter.convertImage2ByteArray(finalGalleryImageBitmap));
                    userImage.setName(galleryImageName);
                    userImageViewModel.insertUserImage(userImage);
                    Toast.makeText(GalleryActivity.this,"Insertion Operation in DB through Gallery", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode ==Constants.REQUEST_PHOTO_GALLERY){
                assert data != null;
                try {
                    setPhotoUri(data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setPhotoUri(Uri photoUri) throws IOException {
        if (photoUri != null) {
            selectedPhotoUri = photoUri;
        }

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        bmp = MediaStore.Images.Media.getBitmap(
                this.getContentResolver(),
                selectedPhotoUri);

        Mat obj = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmp,obj);
        Mat galleryImage = new Mat();

        Imgproc.Canny(obj,galleryImage,100,200);
        finalGalleryImageBitmap = Bitmap.createBitmap(galleryImage.cols(),galleryImage.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(galleryImage,finalGalleryImageBitmap);

        galleryIMGVIEW.setImageBitmap(bmp);

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/OpenCV_Gallery");
        //Checking for exisitng folder
        boolean success = true;
        if(!folder.exists()){
            success = folder.mkdirs();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateAndTime = simpleDateFormat.format(new Date());
        String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/OpenCV_Gallery/"+currentDateAndTime+".jpeg";

        //Write file to device
        Imgcodecs.imwrite(fileName,galleryImage);
    }
}
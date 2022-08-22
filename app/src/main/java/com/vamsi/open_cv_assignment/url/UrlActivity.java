package com.vamsi.open_cv_assignment.url;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.vamsi.open_cv_assignment.R;
import com.vamsi.open_cv_assignment.miscellaneous.DataConverter;
import com.vamsi.open_cv_assignment.room_database.model.UserImage;
import com.vamsi.open_cv_assignment.room_database.viewmodel.UserImageViewModel;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UrlActivity extends AppCompatActivity {

    private MaterialButton addImage, processImage;
    private TextInputLayout textUrl;
    private ImageView imageView;
    String URL;
    Bitmap bitmap;
    Bitmap finalProcessedImage;
    UserImageViewModel userImageViewModel;
    UserImage userImage;
    String urlImageName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);

        addImage = findViewById(R.id.addImageButton);
        processImage = findViewById(R.id.processImageButton);
        textUrl = findViewById(R.id.textUrl);
        imageView = findViewById(R.id.imageView);

        urlImageName = getIntent().getStringExtra("url_image_name");

        userImageViewModel = new ViewModelProvider(this).get(UserImageViewModel.class);
        userImage = new UserImage();

        EditText editTextURL = textUrl.getEditText();

        assert editTextURL != null;

        processImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalProcessedImage ==  null){
                    Toast.makeText(UrlActivity.this,"Please load the image",Toast.LENGTH_SHORT).show();
                }
                else{
                    imageView.setImageBitmap(finalProcessedImage);

                    //Add Data to Database
                    userImage.setNormalImage(DataConverter.convertImage2ByteArray(bitmap));
                    userImage.setProcessedImage(DataConverter.convertImage2ByteArray(finalProcessedImage));
                    userImage.setName(urlImageName);
                    userImageViewModel.insertUserImage(userImage);
                    Toast.makeText(UrlActivity.this,"Insertion Operation in DB through URL", Toast.LENGTH_SHORT).show();

                }
            }
        });


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                URL = editTextURL.getText().toString();

                Glide.with(imageView)
                        .asBitmap()
                        .load(URL)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                imageView.setImageBitmap(resource);
                                bitmap = resource;
                                Mat obj = new Mat(bitmap.getWidth(),bitmap.getHeight(), CvType.CV_8UC4);
                                Utils.bitmapToMat(bitmap,obj);
                                Mat urlImage = new Mat();
                                Imgproc.Canny(obj,urlImage,100,200);
                                //Mat to bit map conversion
                                finalProcessedImage = Bitmap.createBitmap(urlImage.cols(),urlImage.rows(),Bitmap.Config.ARGB_8888);
                                Utils.matToBitmap(urlImage,finalProcessedImage);

                                File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/OpenCV_URL");
                                boolean success = true;
                                if(!folder.exists()){
                                    success = folder.mkdirs();
                                }

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                                String currentDateAndTime = simpleDateFormat.format(new Date());

                                String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/OpenCV_URL/"+currentDateAndTime+".jpeg";

                                       Imgcodecs.imwrite(fileName,urlImage);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }
        });
    }
}

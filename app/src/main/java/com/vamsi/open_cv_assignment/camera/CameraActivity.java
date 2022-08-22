package com.vamsi.open_cv_assignment.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.vamsi.open_cv_assignment.R;
import com.vamsi.open_cv_assignment.miscellaneous.DataConverter;
import com.vamsi.open_cv_assignment.room_database.model.UserImage;
import com.vamsi.open_cv_assignment.room_database.viewmodel.UserImageViewModel;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "MainActivity";

    private Mat mRgba;
    private Mat mGray;
    private Mat edgeDetected;
    private ImageView cameraClick, cameraFlip, galleryClick;
    private int cameraID = 0;
    private int click_image = 0;
    private CameraBridgeViewBase cameraBridgeViewBase;
    private UserImageViewModel userImageViewModel;
    private UserImage userImage;
    Bitmap normalImageBitmap;
    String cameraImageName;
    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface
                        .SUCCESS: {
                    Log.i(TAG, "OpenCv Is loaded");
                    cameraBridgeViewBase.enableView();
                }
                default: {
                    super.onManagerConnected(status);

                }
                break;
            }
        }
    };

    public CameraActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        userImage = new UserImage();
        userImageViewModel = new ViewModelProvider(this).get(UserImageViewModel.class);

        cameraImageName = getIntent().getStringExtra("camera_image_name");

        int MY_PERMISSIONS_REQUEST_CAMERA = 0;
        // if camera permission is not given it will ask for it on device
        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }

        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }

        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }

        setContentView(R.layout.activity_camera);

        cameraClick = findViewById(R.id.cameraClick);
        cameraFlip = findViewById(R.id.cameraFlip);
        galleryClick = findViewById(R.id.galleryClick);

        cameraBridgeViewBase = (CameraBridgeViewBase) findViewById(R.id.cameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        cameraFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapCamera();
            }
        });

        cameraClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click_image ==0){
                    click_image = 1;
                }
                else{
                    click_image = 0;
                }
            }
        });

        galleryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CameraActivity.this, CameraGalleryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }

    private void swapCamera() {
        cameraID = cameraID^1;
        cameraBridgeViewBase.disableView();
        cameraBridgeViewBase.setCameraIndex(cameraID);
        cameraBridgeViewBase.enableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            //if load success
            Log.d(TAG, "Opencv initialization is done");
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            //if not loaded
            Log.d(TAG, "Opencv is not loaded. try again");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, baseLoaderCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }

    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
    }

    public void onCameraViewStopped() {
        mRgba.release();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        normalImageBitmap = Bitmap.createBitmap(mRgba.cols(),mRgba.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mRgba,normalImageBitmap);
        if(cameraID == 1){
            Core.flip(mRgba,mRgba,-1);
            Core.flip(mGray,mGray,-1);
        }

        edgeDetected = new Mat();
        Imgproc.Canny(mRgba,edgeDetected,100,200);

        click_image = clickImageRGBA(click_image,edgeDetected);
        return edgeDetected;

    }

    private int clickImageRGBA(int click_image, Mat mRgba) {

        if(click_image ==1){
            Mat savedImage = new Mat();
            Core.flip(mRgba.t(),savedImage,1);
            //Mat to Bitmap Conversion
            Bitmap finalProcessedImage = Bitmap.createBitmap(savedImage.cols(),savedImage.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(savedImage,finalProcessedImage);

//            Imgproc.cvtColor(savedImage,savedImage,Imgproc.COLOR_RGBA2BGRA);
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/OpenCV");
            boolean success = true;
            if(!folder.exists()){
                success = folder.mkdirs();
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String currentDateAndTime = simpleDateFormat.format(new Date());

            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/OpenCV/"+currentDateAndTime+".jpeg";

            //Add data to database
            userImage.setNormalImage(DataConverter.convertImage2ByteArray(normalImageBitmap));
            userImage.setProcessedImage(DataConverter.convertImage2ByteArray(finalProcessedImage));
            userImage.setName(cameraImageName);
            userImageViewModel.insertUserImage(userImage);
           // Toast.makeText(CameraActivity.this,"Insertion Operation in DB through Camera", Toast.LENGTH_SHORT).show();
            Imgcodecs.imwrite(fileName,savedImage);
            click_image = 0;
        }

        return click_image;
    }
}
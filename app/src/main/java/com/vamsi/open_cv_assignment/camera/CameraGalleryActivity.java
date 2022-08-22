package com.vamsi.open_cv_assignment.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Environment;

import com.vamsi.open_cv_assignment.R;

import java.io.File;
import java.util.ArrayList;

public class CameraGalleryActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<String> filePath = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_gallery);

        File folder = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath()+"/OpenCV");

        createFileArray(folder);

        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(CameraGalleryActivity.this,filePath);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void createFileArray(File folder) {
        File fileList[] = folder.listFiles();

        if(fileList != null){
            for(int i = 0; i< fileList.length;i++){
                filePath.add(fileList[i].getAbsolutePath());
            }
        }
    }
}
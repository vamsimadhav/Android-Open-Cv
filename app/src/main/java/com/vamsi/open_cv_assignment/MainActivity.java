package com.vamsi.open_cv_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.vamsi.open_cv_assignment.camera.CameraActivity;
import com.vamsi.open_cv_assignment.gallery.GalleryActivity;
import com.vamsi.open_cv_assignment.room_database.model.UserImage;
import com.vamsi.open_cv_assignment.room_database.viewmodel.UserImageViewModel;
import com.vamsi.open_cv_assignment.url.UrlActivity;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    static {
        if(OpenCVLoader.initDebug()){
            Log.d("MainActivity: ","Opencv is loaded");
        }
        else {
            Log.d("MainActivity: ","Opencv failed to load");
        }
    }

    private FloatingActionButton floatingActionButton;
    TextInputLayout textImageName;

    UserImageViewModel userImageViewModel;
    UserImage userImage;
    String userImageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        camera_button=findViewById(R.id.camera_btn);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        textImageName = findViewById(R.id.textImageName);
        assert textImageName != null;
        EditText editTextName = textImageName.getEditText();


        userImageViewModel = new ViewModelProvider(this).get(UserImageViewModel.class);
        userImage = new UserImage();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImageName = editTextName.getText().toString();
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                bottomSheetDialog.setCancelable(true);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

               if(userImageName.isEmpty()){
                   Toast.makeText(MainActivity.this,"Please write the Image Name",Toast.LENGTH_LONG).show();
               }
               else{

                   MaterialButton btnGallery = bottomSheetDialog.findViewById(R.id.gallery);
                   MaterialButton btnCamera = bottomSheetDialog.findViewById(R.id.camera);
                   MaterialButton btnURL = bottomSheetDialog.findViewById(R.id.url);

                   assert btnGallery != null;
                   assert btnCamera != null;
                   assert btnURL != null;

                   btnGallery.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           Intent galleryIntent = new Intent(MainActivity.this, GalleryActivity.class)
                                   .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           galleryIntent.putExtra("gallery_image_name",userImageName);

                           startActivity(galleryIntent);

                           bottomSheetDialog.dismiss();
                       }
                   });

                   btnCamera.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class)
                                   .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           cameraIntent.putExtra("camera_image_name",userImageName);

                           startActivity(cameraIntent);

                           bottomSheetDialog.dismiss();
                       }
                   });

                   btnURL.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           Intent urlIntent = new Intent(MainActivity.this, UrlActivity.class)
                                   .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           urlIntent.putExtra("url_image_name",userImageName);

                           startActivity(urlIntent);

                           bottomSheetDialog.dismiss();
                       }
                   });
                   bottomSheetDialog.show();
               }
            }
        });
    }
}
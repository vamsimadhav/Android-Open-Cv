package com.vamsi.open_cv_assignment.room_database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.vamsi.open_cv_assignment.room_database.Repository.UserImageRepository;
import com.vamsi.open_cv_assignment.room_database.model.UserImage;

public class UserImageViewModel extends AndroidViewModel {

    private UserImageRepository userImageRepository;

    public UserImageViewModel(@NonNull Application application) {
        super(application);
        userImageRepository = new UserImageRepository(application);
    }

    public void insertUserImage(UserImage userImage){
        userImageRepository.insertUserImage(userImage);
    }

    public void deleteUserImage(UserImage userImage){
        userImageRepository.deleteUserImage(userImage);
    }
}

package com.vamsi.open_cv_assignment.room_database.Repository;

import android.app.Application;

import com.vamsi.open_cv_assignment.room_database.DAO.UserImageDAO;
import com.vamsi.open_cv_assignment.room_database.UserImageRoomDatabase;
import com.vamsi.open_cv_assignment.room_database.model.UserImage;

public class UserImageRepository {
    private UserImageDAO userImageDAO;

    public UserImageRepository(Application application){
        UserImageRoomDatabase userImageRoomDatabase = UserImageRoomDatabase.getDatabase(application);
        userImageDAO = userImageRoomDatabase.userImageDAO();
    }

    public void insertUserImage(UserImage userImage){

        UserImageRoomDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userImageDAO.insertImage(userImage);
            }
        });
    }

    public void deleteUserImage(UserImage userImage){

        UserImageRoomDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userImageDAO.deleteUserImage(userImage);
            }
        });
    }
}

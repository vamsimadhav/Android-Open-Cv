package com.vamsi.open_cv_assignment.room_database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.vamsi.open_cv_assignment.room_database.model.UserImage;

@Dao
public interface UserImageDAO {

    @Insert
    void insertImage(UserImage userImage);

    @Query("Delete From user_image")
    void deleteAllUserImages();

    @Delete
    void deleteUserImage(UserImage userImage);

//    @Query("Select * From user_image Order by ID ASC")
//    void
}

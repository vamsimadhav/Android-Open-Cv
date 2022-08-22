package com.vamsi.open_cv_assignment.room_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vamsi.open_cv_assignment.room_database.DAO.UserImageDAO;
import com.vamsi.open_cv_assignment.room_database.model.UserImage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UserImage.class},version = 1,exportSchema = false)
public abstract class UserImageRoomDatabase extends RoomDatabase {

    public abstract UserImageDAO userImageDAO();

    private static volatile UserImageRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static UserImageRoomDatabase getDatabase(Context context){

        if(INSTANCE == null){
            synchronized (UserImageRoomDatabase.class){
                INSTANCE = Room.databaseBuilder(context,UserImageRoomDatabase.class,"opencv_images").build();
            }
        }
        return INSTANCE;
    }
}

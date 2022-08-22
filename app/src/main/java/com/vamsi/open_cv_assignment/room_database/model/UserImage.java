package com.vamsi.open_cv_assignment.room_database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_image")
public class UserImage {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "image_name")
    @NonNull
    private String name;

    @ColumnInfo(name = "normal_image",typeAffinity = ColumnInfo.BLOB)
    @NonNull
    private byte [] normalImage;

    @ColumnInfo(name = "processed_image",typeAffinity = ColumnInfo.BLOB)
    @NonNull
    private byte [] processedImage;


    public int getId() {
        return id;
    }

    public byte[] getNormalImage() {
        return normalImage;
    }

    public byte[] getProcessedImage() {
        return processedImage;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setNormalImage(byte[] normalImage) {
        this.normalImage = normalImage;
    }

    public void setProcessedImage(byte[] processedImage) {
        this.processedImage = processedImage;
    }

    public void setId(int id) {
        this.id = id;
    }
}

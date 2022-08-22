package com.vamsi.open_cv_assignment.camera;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.vamsi.open_cv_assignment.R;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    Context ctx;
    ArrayList<String> images = new ArrayList<>();
    LayoutInflater inflater;

    public ViewPagerAdapter(Context ctx, ArrayList<String> images) {
        this.ctx = ctx;
        this.images = images;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void destroyItem( ViewGroup container, int position,  Object object) {
        container.removeView((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View itemView = inflater.inflate(R.layout.item,container,false);
        ImageView imageView = itemView.findViewById(R.id.image_gallery_page);

        //Getting Uri
        Uri imageUri = Uri.parse("file://"+images.get(position));

        imageView.setImageURI(imageUri);

        Objects.requireNonNull(container).addView(itemView);

        return itemView;

    }
}

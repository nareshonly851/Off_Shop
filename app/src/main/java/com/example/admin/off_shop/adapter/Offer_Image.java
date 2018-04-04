package com.example.admin.off_shop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.admin.off_shop.R;
import com.example.admin.off_shop.network.APIUrl;
import com.example.admin.off_shop.network.model.offers.Img;

import java.util.List;

/**
 * Created by admin on 04-04-2018.
 */

public class Offer_Image extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<Img> imgList;



    public Offer_Image(Context context,List<Img> imgList)
    {
        this.context=context;
        this.imgList=imgList;
    }




    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.offer_slide_layout,container,false);

        final ImageView slid_img = view.findViewById(R.id.slide_image);

        ConstraintLayout constraintLayout = view.findViewById(R.id.slide_bg);



       Img img  = imgList.get(position);

        Glide.with(context)
                .load(APIUrl.BASE_URL+img.getImagePath())
                .asBitmap()
                .placeholder(R.drawable.shop_icon)
                .error(R.drawable.shop_icon)
                .into(new BitmapImageViewTarget(slid_img) {
                    @Override
                    protected void setResource(Bitmap resource) {

                        slid_img.setImageBitmap(resource);
                    }
                });

               container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
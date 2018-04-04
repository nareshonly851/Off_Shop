package com.example.admin.off_shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.off_shop.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;



    public SliderAdapter(Context context)
    {
        this.context=context;
    }

    public int[] slideimage={R.drawable.smartphone_icon,R.drawable.offer_icon,R.drawable.bag_icon};
    public String[] slidetitel={"SMART SHOPPING","MORE DISCOUNT","EASY TO PAY"};
    public String[] color={"#c51162","#aa00ff","#6200ea"};
    public String[] detaile ={"Smart Shopping helps you manage your shopping list. Very simple application with barcode grocery and best offer from many products. Below is the feature of Smart Shopping",
    " India's largest online marketplace, strives to deliver an unparalleled shopping experience by offering heavy discounts & special deals; shoppers must take advantage of",
    "Making it as easy as possible for your customers to pay is essential for increasing conversions and sales."};



    @Override
    public int getCount() {
        return slideimage.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slid_img = view.findViewById(R.id.slide_image);
        TextView slide_titel  = view.findViewById(R.id.slide_txt);
        TextView slide_desc =  view.findViewById(R.id.desc_txt);
        ConstraintLayout constraintLayout = view.findViewById(R.id.slide_bg);

//       constraintLayout.setBackgroundColor(Color.parseColor(color[position]));

        slid_img.setImageResource(slideimage[position]);
        slide_titel.setText(slidetitel[position]);
        slide_desc.setText(detaile[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}

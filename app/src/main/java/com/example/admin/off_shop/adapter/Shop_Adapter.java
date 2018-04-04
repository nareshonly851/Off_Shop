package com.example.admin.off_shop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.admin.off_shop.R;
import com.example.admin.off_shop.network.APIUrl;
import com.example.admin.off_shop.network.model.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 24-03-2018.
 */

public class Shop_Adapter extends RecyclerView.Adapter<Shop_Adapter.MyViewHolder>
        implements Filterable {
private Context context;
private List<Shop> shopList;
private List<Shop> shopListFiltered;
private Offer_Image sliderAdapter;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView name, phone;
    public ImageView imageView1;
    public ViewPager offer_photo;
    public LinearLayout dotlayout;
   // public ImageView thumbnail;

    public MyViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.shop);
        imageView1 = view.findViewById(R.id.imageView1);
        offer_photo = view.findViewById(R.id.offer_photo);
        dotlayout = view.findViewById(R.id.dot_layout);




    }
}


    public Shop_Adapter(Context context, List<Shop> shopList) {
        this.context = context;

        this.shopList = shopList;
        this.shopListFiltered = shopList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Shop shop = shopListFiltered.get(position);
        holder.name.setText(shop.getShopName());

         sliderAdapter = new Offer_Image(context,shop.getOffer().getImg());
         holder.offer_photo.setAdapter(sliderAdapter);

        Log.d("logo",shop.getLogo());

//        String pureBase64Encoded = shop.getLogo().substring(shop.getLogo().indexOf(",")  + 1);
//
//      byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
//
//        Glide.with(context).load(decodedBytes).crossFade().fitCenter().into(holder.imageView1);





        Glide.with(context)
                .load(APIUrl.BASE_URL+shop.getLogo())
                .asBitmap()
                .placeholder(R.drawable.shop_icon)
                .error(R.drawable.shop_icon)
                .into(new BitmapImageViewTarget(holder.imageView1) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                Bitmap.createScaledBitmap(resource, 80, 80, false));
                        drawable.setCircular(true);
                        holder.imageView1.setImageDrawable(drawable);
                    }
                });



    }






    @Override
    public int getItemCount() {
        return shopListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    shopListFiltered = shopList;
                } else {
                    List<Shop> filteredList = new ArrayList<>();
                    for (Shop row : shopList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getShopName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    shopListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = shopListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                shopListFiltered = (ArrayList<Shop>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }




}
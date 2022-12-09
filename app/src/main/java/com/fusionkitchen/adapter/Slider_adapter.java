package com.fusionkitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.model.The_Slide_Items_Model_Class;

import java.util.List;


public class Slider_adapter extends PagerAdapter {

    private Context Mcontext;
   private List<String> mSliderItems;


    public Slider_adapter(Context Mcontext, List<String> theSlideItemsModelClassList) {
        this.Mcontext = Mcontext;
        this.mSliderItems = theSlideItemsModelClassList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sliderLayout = inflater.inflate(R.layout.layout_txt,null);

        ImageView featured_image = sliderLayout.findViewById(R.id.my_featured_image);


        Glide.with(Mcontext)
                .load(mSliderItems.get(position))
                .into(featured_image);

        container.addView(sliderLayout);
        return sliderLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}

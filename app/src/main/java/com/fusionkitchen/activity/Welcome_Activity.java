package com.fusionkitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fusionkitchen.R;
import com.fusionkitchen.adapter.The_Slide_items_Pager_Adapter;
import com.fusionkitchen.model.The_Slide_Items_Model_Class;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by Lincoln on 02/01/21.
 * Welcome page slider page
 */
public class Welcome_Activity extends AppCompatActivity {

    /*---------------------------XML ID Call----------------------------------------------------*/
    //private ViewPager viewPager;
  //  private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private AppCompatButton btnNext;
    private PrefManager prefManager;




    private List<The_Slide_Items_Model_Class> listItems;
    private ViewPager page;
    private TabLayout tabLayout;

    /*---------------------------every 5 seconds next design Call----------------------------------------------------*/
    Handler myHandler = new Handler();
    Runnable runnable;
    int delay = 3000; // 5000 milliseconds == 5 second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*---------------------------Checking for first time launch - before calling setContentView()----------------------------------------------------*/
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        /*--------------------------- Making notification bar transparent----------------------------------------------------*/
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);



        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        overridePendingTransition(R.anim.enter, R.anim.exit);

        /*---------------------------XML ID Call----------------------------------------------------*/
        page = (ViewPager) findViewById(R.id.my_pager);
        btnNext = findViewById(R.id.btn_next);
        tabLayout = findViewById(R.id.my_tablayout);

        /*---------------------------button bg image set----------------------------------------------------*/
        //  btnNext.setBackground(getResources().getDrawable(R.drawable.background_button_new));


        /*---------------------------layouts of all welcome sliders---------------------------------------------------*/
      //  layouts = new int[]{
             //   R.layout.welcome_slide1,
              //  R.layout.welcome_slide2,
              ///  R.layout.welcome_slide3};



        /*---------------------------making notification bar transparent---------------------------------------------------*/
      //  changeStatusBarColor();
       // myViewPagerAdapter = new MyViewPagerAdapter();
      //  viewPager.setAdapter(myViewPagerAdapter);
      //  viewPager.addOnPageChangeListener(viewPagerPageChangeListener);



        String textview1 = "Find the food you crave from more \n than 2000+ Restaurants and \n Takeaways";
        String textview2 = "Select from more than 200+ \n varieties of cuisines and taste \n with fun";
        String textview3 ="We make food ordering and \n delivery fast, and safe";
        listItems = new ArrayList<>() ;
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.online_food_application,"Best Food Spots Near you",textview1));
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.online_fast_food_ordering,"Order what you love",textview2));
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.food_delivery_drone,"Food you love, delivered to you",textview3));

        The_Slide_items_Pager_Adapter itemsPager_adapter = new The_Slide_items_Pager_Adapter(this, listItems);
        page.setAdapter(itemsPager_adapter);


        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new The_slide_timer(),2000,3000);
        tabLayout.setupWithViewPager(page,true);


        /*---------------------------every 5 seconds next design Call----------------------------------------------------*/
        //start handler as activity become visible
      /*  myHandler.postDelayed(runnable = new Runnable() {
            public void run() {
                //Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                }
                myHandler.postDelayed(runnable, delay);
            }
        }, delay);*/
        /*---------------------------Set OnClick Listener----------------------------------------------------*/
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
    }

/*
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }*/

    /*---------------------------already welcome sliders showed---------------------------------------------------*/
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(Welcome_Activity.this, Postcode_Activity.class));
        finish();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    /*---------------------------viewpager change listener---------------------------------------------------*/
/*
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == layouts.length - 1) {
                btnNext.setText(getString(R.string.start));

            } else {
                btnNext.setText(getString(R.string.skip));

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
*/


    /*---------------------------Making notification bar transparent---------------------------------------------------*/
   /* private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }*/


    /*---------------------------View pager adapter---------------------------------------------------*/
/*    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }*/



    public class The_slide_timer extends TimerTask {
        @Override
        public void run() {

            Welcome_Activity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (page.getCurrentItem()< listItems.size()-1) {
                        page.setCurrentItem(page.getCurrentItem()+1);
                    }
                    else
                        page.setCurrentItem(0);
                }
            });
        }
    }

}

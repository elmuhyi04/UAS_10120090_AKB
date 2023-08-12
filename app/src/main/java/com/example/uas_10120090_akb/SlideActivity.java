package com.example.uas_10120090_akb;
//NIM   : 10102090
//NAMA  : Muhammad Rizky Muhyi
//Kelas : IF-3


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class SlideActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    SlideViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        viewPager=findViewById(R.id.viewpager);
        adapter=new SlideViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

    }
}

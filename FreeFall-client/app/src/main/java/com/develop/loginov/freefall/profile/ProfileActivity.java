package com.develop.loginov.freefall.profile;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.develop.loginov.freefall.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final ViewPager viewPager = findViewById(R.id.activity_profile__viewpager);
        final TabLayout tabLayout = findViewById(R.id.activity_profile__tablayout);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new SignInFragment(), "Вход");
        adapter.addPage(new SignUpFragment(), "Регистрация");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }
}

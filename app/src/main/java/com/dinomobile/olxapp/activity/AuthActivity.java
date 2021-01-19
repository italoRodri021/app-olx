package com.dinomobile.olxapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dinomobile.olxapp.R;
import com.dinomobile.olxapp.fragment.LoginFragment;
import com.dinomobile.olxapp.fragment.RecoverPasswordFragment;
import com.dinomobile.olxapp.fragment.RegisterFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SmartTabLayout smartTabLayout = findViewById(R.id.smartTabAuth);
        ViewPager viewPager = findViewById(R.id.viewPagerAuth);

        FragmentPagerAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Entrar", LoginFragment.class)
                .add("Cadastro", RegisterFragment.class)
                .add("Recuperar Senha", RecoverPasswordFragment.class)
                .create()
        );

        smartTabLayout.setViewPager(viewPager);
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent i = new Intent(getApplicationContext(), SplashActivity.class);
        startActivity(i);

    }
}
package com.example.edatarestoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.edatarestoran.Cerceve.HomeFragment;
import com.example.edatarestoran.Cerceve.MasalarFragment;
import com.example.edatarestoran.Cerceve.ProfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnaSayfaActivity extends AppCompatActivity {

    FirebaseAuth yetki;

    BottomNavigationView bottomNavigationView;

    Fragment seciliCerceve = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent = getIntent().getExtras();

        if (intent != null) {

            String gonderen = intent.getString("id");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();

            editor.putString("profileId", gonderen);

            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici, new ProfilFragment()).commit();

        } else {

            getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici, new HomeFragment()).commit();

        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.nav_home:
                            //Ana çerçeveyi çağırsın
                            seciliCerceve = new HomeFragment();

                            break;

                        case R.id.nav_ekle:
                            //Çerçeve Boş olsun YemekEkleActivity'e gitsin
                            seciliCerceve = null;
                            startActivity(new Intent(AnaSayfaActivity.this, YemekEkleActivity.class));

                            break;

                        case R.id.nav_profil:

                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();

                            //Profil çerçevesini çağırsın
                            seciliCerceve = new ProfilFragment();

                            break;

                        case R.id.nav_masalar:



                            //Masalar çerçevesini çağırsın
                            seciliCerceve = new MasalarFragment();

                    }

                    if (seciliCerceve != null) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici, seciliCerceve).commit();

                    }

                    return true;
                }
            };

}
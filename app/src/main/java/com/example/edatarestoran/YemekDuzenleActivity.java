package com.example.edatarestoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edatarestoran.Model.Yemek;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import javax.net.ssl.SSLEngineResult;

public class YemekDuzenleActivity extends AppCompatActivity {

    private EditText edt_yemekAdi, edt_yemekAciklama, edt_kategori, edt_fiyat;
    private Button btn_kaydet, btn_sil;

    private String str_yemekAdi, str_yemekAciklama, str_kategori, str_fiyat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yemek_duzenle);

        tanimlamalar();

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        String yemekHakkinda = intent.getStringExtra("yemekHakkinda");
        String kategori = intent.getStringExtra("kategori");
        String yemekAdi = intent.getStringExtra("yemekAdi");
        String yemekFiyati = intent.getStringExtra("yemekFiyati");
        String yemekResmiUrl = intent.getStringExtra("yemekResmiUrl");
        String yemekSahibi = intent.getStringExtra("yemekSahibi");

        Yemek yemek = new Yemek(id, yemekHakkinda, kategori, yemekAdi, yemekFiyati, yemekResmiUrl, yemekSahibi);

        System.out.println("Yemek id'si: " + id);

        btn_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_yemekAdi = edt_yemekAdi.getText().toString();
                str_yemekAciklama = edt_yemekAciklama.getText().toString();
                str_kategori = edt_kategori.getText().toString();
                str_fiyat = edt_fiyat.getText().toString();

                DatabaseReference yemekYolu = FirebaseDatabase.getInstance().getReference().child("Yemekler").child(id);

                if (!str_yemekAdi.isEmpty()) {
                    yemek.setYemekAdi(str_yemekAdi);
                    yemekYolu.child("yemekAdi").setValue(yemek.getYemekAdi());
                }

                if (!TextUtils.isEmpty(str_yemekAciklama)) {
                    yemek.setYemekHakkinda(str_yemekAciklama);
                    yemekYolu.child("yemekHakkinda").setValue(yemek.getYemekHakkinda());
                }

                if (!TextUtils.isEmpty(str_kategori)) {
                    yemek.setKategori(str_kategori);
                    yemekYolu.child("kategori").setValue(yemek.getKategori());
                }

                if (!TextUtils.isEmpty(str_fiyat)) {
                    yemek.setYemekFiyati(str_fiyat);
                    yemekYolu.child("yemekFiyati").setValue(yemek.getYemekFiyati());
                }

                Toast.makeText(getApplicationContext(), "Değişiklikler Başarıyla Uygulandı", Toast.LENGTH_SHORT).show();

            }
        });

        btn_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference yemekYolu = FirebaseDatabase.getInstance().getReference().child("Yemekler").child(id);

                yemekYolu.removeValue();

                startActivity(new Intent(YemekDuzenleActivity.this, AnaSayfaActivity.class));

                Toast.makeText(getApplicationContext(), "Yemek Başarıyla Silindi", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void tanimlamalar(){

        edt_yemekAdi = findViewById(R.id.edt_yemekAdiDegistir_YemekDuzenleActivity);
        edt_yemekAciklama = findViewById(R.id.edt_yemekAciklamasiDegistir_YemekDuzenleActivity);
        edt_kategori = findViewById(R.id.edt_yemekKategoriDegistir_YemekDuzenleActivity);
        edt_fiyat = findViewById(R.id.edt_yemekFiyatiDegistir_YemekDuzenleActivity);

        btn_kaydet = findViewById(R.id.btn_kaydet_YemekDuzenleActivity);
        btn_sil = findViewById(R.id.btn_yemegiSil_YemekDuzenleActivity);

    }

}
package com.example.edatarestoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KaydolActivity extends AppCompatActivity {

    EditText edt_restaurantKodu, edt_kullaniciAdi, edt_ad, edt_email, edt_sifre;
    Button btn_kaydol;
    TextView txt_girisSayfasinaGit;
    RadioButton rbtn_musteri, rbtn_restaurant;

    private String str_kullaniciTipi = "Restaurant", key = "";
    private String str_restaurantKodu;

    FirebaseAuth yetki;
    DatabaseReference yol;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydol);

        tanimlamalar();

        txt_girisSayfasinaGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KaydolActivity.this, GirisActivity.class));
            }
        });

        btn_kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Progress dialog
                pd = new ProgressDialog(KaydolActivity.this);
                pd.setMessage("Lütfen Bekleyin...");
                pd.show();


                String str_kullaniciAdi = edt_kullaniciAdi.getText().toString();
                String str_ad = edt_ad.getText().toString();
                String str_email = edt_email.getText().toString();
                String str_sifre = edt_sifre.getText().toString();
                str_restaurantKodu = edt_restaurantKodu.getText().toString();

                if (rbtn_restaurant.isChecked()) {

                    System.out.println(rbtn_restaurant.isChecked());

                    if (TextUtils.isEmpty(str_restaurantKodu) || TextUtils.isEmpty(str_kullaniciAdi) || TextUtils.isEmpty(str_ad) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_sifre)) {

                        Toast.makeText(getApplicationContext(), "Lütfen Boşlukları Doldurunuz", Toast.LENGTH_SHORT).show();

                        pd.dismiss();

                    } else if (edt_sifre.length() < 6 && edt_restaurantKodu.length() < 6) {

                        Toast.makeText(getApplicationContext(), "Şifreniz ve Kodunuz Minimum 6 Karakter Olmalı", Toast.LENGTH_SHORT).show();

                        pd.dismiss();

                    } else {

                        //Yeni kullanıcı kaydetme metodunu çağır

                        kaydetRestoran(str_restaurantKodu, str_kullaniciTipi, str_kullaniciAdi, str_ad, str_email, str_sifre);

                    }

                }

                else if (rbtn_musteri.isChecked()) {

                    if (TextUtils.isEmpty(str_kullaniciAdi) || TextUtils.isEmpty(str_ad) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_sifre)) {

                        Toast.makeText(getApplicationContext(), "Lütfen Boşlukları Doldurunuz", Toast.LENGTH_SHORT).show();

                        pd.dismiss();

                    } else if (edt_sifre.length() < 6) {

                        Toast.makeText(getApplicationContext(), "Şifreniz Minimum 6 Karakter Olmalı", Toast.LENGTH_SHORT).show();

                        pd.dismiss();

                    } else {

                        //Yeni kullanıcı kaydetme metodunu çağır

                        kaydetMusteri(str_kullaniciTipi, str_kullaniciAdi, str_ad, str_email, str_sifre);

                    }

                }

            }
        });

    }

    private void tanimlamalar() {

        edt_restaurantKodu = findViewById(R.id.edt_restaurantKodu_kaydolActivity);
        edt_kullaniciAdi = findViewById(R.id.edt_kullaniciAdi_kaydolActivity);
        edt_ad = findViewById(R.id.edt_ad_kaydolActivity);
        edt_email = findViewById(R.id.edt_email_kaydolActivity);
        edt_sifre = findViewById(R.id.edt_sifre_kaydolActivity);

        btn_kaydol = findViewById(R.id.btn_kaydolActivity);

        txt_girisSayfasinaGit = findViewById(R.id.txt_girisSayfasinaGit_kaydolActivity);

        yetki = FirebaseAuth.getInstance();

        rbtn_musteri = findViewById(R.id.rbtn_kayitSekliMusteri_kaydolActivity);
        rbtn_restaurant = findViewById(R.id.rbtn_kayitSekliRestaurant_kaydolActivity);

    }

    private void kaydetMusteri(String kullaniciTipi, String kullaniciAdi, String ad, String email, String sifre) {

        yetki.createUserWithEmailAndPassword(email, sifre)
                .addOnCompleteListener(KaydolActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser firebaseKullanici = yetki.getCurrentUser();

                            String kullaniciId = firebaseKullanici.getUid();

                            yol = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(kullaniciId);

                            HashMap<String, Object> hashMap = new HashMap<>();

                            hashMap.put("id", kullaniciId);
                            hashMap.put("kullaniciTipi", kullaniciTipi);
                            hashMap.put("kullaniciAdi", kullaniciAdi.toLowerCase());
                            hashMap.put("ad", ad);
                            hashMap.put("resimUrl", "https://firebasestorage.googleapis.com/v0/b/e-data-restoran.appspot.com/o/placeholder.png?alt=media&token=973dcc5f-e141-4055-a73a-65627706ef1c");

                            yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        pd.dismiss();

                                        Intent intent = new Intent(KaydolActivity.this, GirisActivity.class);

                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(intent);

                                    }

                                }
                            });

                        } else {

                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Kayıt Başarısız", Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }

    private void kaydetRestoran(String restoranKodu, String kullaniciTipi, String kullaniciAdi, String ad, String email, String sifre) {

        yetki.createUserWithEmailAndPassword(email, sifre)
                .addOnCompleteListener(KaydolActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser firebaseKullanici = yetki.getCurrentUser();

                            String kullaniciId = firebaseKullanici.getUid();

                            yol = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(kullaniciId);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                            HashMap<String, Object> hashMap = new HashMap<>();

                            hashMap.put("id", kullaniciId);
                            hashMap.put("kullaniciTipi", kullaniciTipi);
                            hashMap.put("kullaniciAdi", kullaniciAdi.toLowerCase());
                            hashMap.put("ad", ad);
                            hashMap.put("resimUrl", "https://firebasestorage.googleapis.com/v0/b/e-data-restoran.appspot.com/o/placeholder.png?alt=media&token=973dcc5f-e141-4055-a73a-65627706ef1c");

                            Query query = reference.child("Restoranlar").orderByChild("restoranKodu").equalTo(restoranKodu);

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                        key = child.getKey();

                                        hashMap.put("restoranKodu", restoranKodu);
                                        hashMap.put("restoranId", key);

                                        yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    pd.dismiss();

                                                    Intent intent = new Intent(KaydolActivity.this, GirisActivity.class);

                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                    startActivity(intent);

                                                }

                                            }
                                        });

                                    }
                                    if (key.isEmpty()) {

                                        pd.dismiss();

                                        Toast.makeText(getApplicationContext(), "Böyle Bir Restoran Bulunmamaktadır.", Toast.LENGTH_SHORT).show();

                                        yetki.getCurrentUser().delete();

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {


                                }
                            });



                        } else {

                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Bu email ile zaten bir kullanıcı bulunmakta", Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }


    public void radioButtonCheck(View v) {

        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {
            case R.id.rbtn_kayitSekliMusteri_kaydolActivity:
                if (checked)

                    str_kullaniciTipi = rbtn_musteri.getText().toString();

                    edt_restaurantKodu.setVisibility(View.GONE);

                    break;

            case R.id.rbtn_kayitSekliRestaurant_kaydolActivity:
                if (checked)

                    str_restaurantKodu = edt_restaurantKodu.getText().toString();

                    str_kullaniciTipi = rbtn_restaurant.getText().toString();

                    edt_restaurantKodu.setVisibility(View.VISIBLE);

                    break;

        }

    }

}
package com.example.edatarestoran;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


import java.sql.SQLOutput;
import java.util.HashMap;

public class YemekEkleActivity extends AppCompatActivity {

    Uri resimUri;
    String benimUrim = "";
    String yemekSahibi;

    StorageTask yuklemeGorevi;
    StorageReference resimYukleYolu;

    ImageView img_kapat, img_eklendi;
    TextView txt_ekle;
    EditText edt_yemekHakkinda, edt_yemekAdi, edt_yemekKategori, edt_yemekFiyat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yemek_ekle);

        img_kapat = findViewById(R.id.close_yemekEkleme);
        img_eklendi = findViewById(R.id.img_eklenenYemek_YemekEkleActivity);

        txt_ekle = findViewById(R.id.txt_yemekEkle);

        edt_yemekHakkinda = findViewById(R.id.edt_yemekHakkinda_YemekEkleActivity);
        edt_yemekAdi = findViewById(R.id.edt_yemekAdi_YemekEkleActivity);
        edt_yemekKategori = findViewById(R.id.edt_yemekKategori_YemekEkleActivity);
        edt_yemekFiyat = findViewById(R.id.edt_yemekFiyati_YemekEkleActivity);

        resimYukleYolu = FirebaseStorage.getInstance().getReference("yemekler");

        img_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(YemekEkleActivity.this, AnaSayfaActivity.class));
                finish();
            }
        });

        txt_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimYukle();
            }
        });

        img_eklendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(YemekEkleActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

    }

    private String dosyaUzantisiAl (Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            resimUri = result.getUri();

            img_eklendi.setImageURI(resimUri);

        }

        else {

            Toast.makeText(getApplicationContext(), "Resim Seçilemedi", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(YemekEkleActivity.this, AnaSayfaActivity.class));

            finish();

        }*/
        if (resultCode == YemekEkleActivity.RESULT_OK) {

            resimUri = data.getData();

            img_eklendi.setImageURI(resimUri);


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getApplicationContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    private void resimYukle() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Gönderiliyor...");
        progressDialog.show();

        //Resim Yükleme Kodları
        if (resimUri != null) {

            StorageReference dosyaYolu = resimYukleYolu.child(System.currentTimeMillis()
                    + "." + dosyaUzantisiAl(resimUri));

            yuklemeGorevi = dosyaYolu.putFile(resimUri);

            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {

                        throw task.getException();

                    }

                    return dosyaYolu.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {

                        Uri indirmeUrisi = task.getResult();

                        benimUrim = indirmeUrisi.toString();

                        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Yemekler");

                        String id = veriYolu.push().getKey();

                        //HashMap<String, Object> hashMap = new HashMap<>();

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        String kullaniciId = firebaseUser.getUid();

                        DatabaseReference yol = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(kullaniciId).child("restoranId");

                        yol.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                HashMap<String, Object> hashMap = new HashMap<>();

                                hashMap.put("yemekSahibi", snapshot.getValue());
                                hashMap.put("id", id);
                                hashMap.put("kategori", edt_yemekKategori.getText().toString());
                                hashMap.put("yemekAdi", edt_yemekAdi.getText().toString());
                                hashMap.put("yemekFiyati", edt_yemekFiyat.getText().toString());
                                hashMap.put("yemekResmiUrl", benimUrim);
                                hashMap.put("yemekHakkinda", edt_yemekHakkinda.getText().toString());

                                veriYolu.child(id).setValue(hashMap);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        progressDialog.dismiss();

                        startActivity(new Intent(YemekEkleActivity.this, AnaSayfaActivity.class));
                        finish();

                    }
                    else {

                        Toast.makeText(getApplicationContext(), "Yemek Ekleme Başarısız", Toast.LENGTH_SHORT).show();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {

            Toast.makeText(getApplicationContext(), "Seçilen Resim Yok", Toast.LENGTH_SHORT).show();

        }

    }

}
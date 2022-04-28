package com.example.edatarestoran;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class RestoranKayitActivity extends AppCompatActivity {

    private EditText txt_restoranKodu, txt_restoranAdi;
    private Button btn_restoranKayit;

    private ProgressDialog pd;

    FirebaseAuth yetki;

    DatabaseReference yol;

    StorageTask yuklemeGorevi;
    StorageReference resimYukleYolu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoran_kayit);

        txt_restoranKodu = findViewById(R.id.edt_restaurantKodu_restoranKayitActivity);
        txt_restoranAdi = findViewById(R.id.edt_restaurantAdi_restoranKayitActivity);

        btn_restoranKayit = findViewById(R.id.btn_restoranKayitActivity);

        resimYukleYolu = FirebaseStorage.getInstance().getReference("gonderiler");

        btn_restoranKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd = new ProgressDialog(RestoranKayitActivity.this);
                pd.setMessage("Lütfen Bekleyin...");
                pd.show();

                String str_restoranKodu = txt_restoranKodu.getText().toString();
                String str_restoranAdi = txt_restoranAdi.getText().toString();

                if (TextUtils.isEmpty(str_restoranKodu) || TextUtils.isEmpty(str_restoranAdi)) {

                    Toast.makeText(getApplicationContext(), "Lütfen Boşlukları Doldurunuz", Toast.LENGTH_SHORT).show();
                    pd.dismiss();

                } else if (str_restoranKodu.length() < 8) {

                    Toast.makeText(getApplicationContext(), "Kodunuz Minimum 8 Karakter Olmalı", Toast.LENGTH_SHORT).show();
                    pd.dismiss();

                } else {

                    //Yeni restoran kaydetme metodunu çağır

                    restoranKaydet(str_restoranKodu, str_restoranAdi);

                }

            }
        });


    }

    private void restoranKaydet(String restoranKodu, String restoranAdi) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String restoranId = database.getReference("Restoranlar").push().getKey();

        yol = FirebaseDatabase.getInstance().getReference().child("Restoranlar").child(restoranId);

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("id", restoranId);
        hashMap.put("restoranKodu", restoranKodu);
        hashMap.put("restoranAdi", restoranAdi);
        hashMap.put("restoranLogo", "https://firebasestorage.googleapis.com/v0/b/e-data-restoran.appspot.com/o/placeholder.png?alt=media&token=973dcc5f-e141-4055-a73a-65627706ef1c");

        yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    pd.dismiss();

                    Intent intent = new Intent(RestoranKayitActivity.this, BaslangicActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);

                }

            }

        });

    }

}


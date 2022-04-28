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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GirisActivity extends AppCompatActivity {

    EditText edt_email, edt_sifre;
    Button btn_girisYap;
    TextView txt_kayitSayfasinaGit;

    FirebaseAuth yetki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        edt_email = findViewById(R.id.edt_email_girisActivity);
        edt_sifre = findViewById(R.id.edt_sifre_girisActivity);

        btn_girisYap = findViewById(R.id.btn_giris_activity);

        yetki = FirebaseAuth.getInstance();

        txt_kayitSayfasinaGit = findViewById(R.id.txt_kayitSayfasina_git);

        txt_kayitSayfasinaGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(GirisActivity.this, KaydolActivity.class));

            }
        });

        btn_girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog pd = new ProgressDialog(GirisActivity.this);
                pd.setMessage("Giriş Yapılıyor...");
                pd.show();

                String str_email = edt_email.getText().toString();
                String str_sifre = edt_sifre.getText().toString();

                if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_sifre)) {

                    Toast.makeText(getApplicationContext(), "Lütfen Boşlukları Doldurunuz", Toast.LENGTH_SHORT).show();

                    pd.dismiss();

                }

                else {

                    yetki.signInWithEmailAndPassword(str_email, str_sifre)
                            .addOnCompleteListener(GirisActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        DatabaseReference yol = FirebaseDatabase.getInstance().getReference()
                                                .child("Kullanıcılar").child(yetki.getCurrentUser().getUid());

                                        yol.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                pd.dismiss();

                                                Intent intent = new Intent(GirisActivity.this, AnaSayfaActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                pd.dismiss();
                                            }
                                        });

                                    }else {

                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), "Giriş Başarısız", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                }

            }
        });

    }
}
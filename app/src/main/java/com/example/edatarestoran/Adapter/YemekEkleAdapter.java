package com.example.edatarestoran.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.edatarestoran.Model.Yemek;
import com.example.edatarestoran.R;
import com.example.edatarestoran.YemekDuzenleActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class YemekEkleAdapter extends RecyclerView.Adapter<YemekEkleAdapter.ViewHolder> {

    public Context mContext;
    public List<Yemek> mYemek;

    public String restoranId;

    private FirebaseUser mevcutFirebaseUser;

    public YemekEkleAdapter(Context mContext, List<Yemek> mYemek) {
        this.mContext = mContext;
        this.mYemek = mYemek;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.yemek_ogesi, parent, false);

        return new YemekEkleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Yemek yemek = mYemek.get(position);
        Glide.with(mContext).load(yemek.getYemekResmiUrl()).into(holder.yemekResmi);
        holder.txt_yemekAdi.setText(yemek.getYemekAdi());

        String yemekFiyati = yemek.getYemekFiyati() + " TL";

        holder.txt_yemekFiyati.setText(yemekFiyati);


        //yemekBilgileri(holder.yemekResmi, holder.txt_yemekAdi, holder.txt_yemekFiyati, yemek.getYemekSahibi());

        holder.btn_yemekEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        holder.btn_yemekDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, YemekDuzenleActivity.class);
                intent.putExtra("id", yemek.getId());
                intent.putExtra("yemekHakkinda", yemek.getYemekHakkinda());
                intent.putExtra("kategori", yemek.getKategori());
                intent.putExtra("yemekAdi", yemek.getYemekAdi());
                intent.putExtra("yemekFiyati", yemek.getYemekFiyati());
                intent.putExtra("yemekResmiUrl", yemek.getYemekResmiUrl());
                intent.putExtra("yemekSahibi", yemek.getYemekSahibi());

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mYemek.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView yemekResmi;
        public TextView txt_yemekAdi, txt_yemekFiyati;
        public Button btn_yemekEkle, btn_yemekDuzenle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            yemekResmi = itemView.findViewById(R.id.img_yemekResmi_yemekOgesi);

            txt_yemekAdi = itemView.findViewById(R.id.txt_yemekAdi_yemekOgesi);
            txt_yemekFiyati = itemView.findViewById(R.id.txt_yemekFiyati_yemekOgesi);

            btn_yemekEkle = itemView.findViewById(R.id.btn_sepeteEkle_yemekOgesi);
            btn_yemekDuzenle = itemView.findViewById(R.id.btn_duzenle_yemekOgesi);

        }
    }

}

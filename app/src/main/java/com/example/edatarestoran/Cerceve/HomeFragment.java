package com.example.edatarestoran.Cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edatarestoran.Adapter.YemekEkleAdapter;
import com.example.edatarestoran.Model.Yemek;
import com.example.edatarestoran.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private YemekEkleAdapter yemekEkleAdapter;
    private List<Yemek> yemekListeleri;
    public FirebaseUser mevcutKullanici;

    private TextView txt_restoranAdi, txt_kullaniciAdi;
    private ImageView img_restoranLogo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyler_view_HomeFragment);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        yemekListeleri = new ArrayList<>();

        yemekEkleAdapter = new YemekEkleAdapter(getContext(), yemekListeleri);

        recyclerView.setAdapter(yemekEkleAdapter);

        //yemekleriGoster();
        restoranKontrol();

        return view;

    }

    private void restoranKontrol() {

        DatabaseReference yemekyolu = FirebaseDatabase.getInstance().getReference("Yemekler");

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        System.out.println("Mevcut Kullanıcı = " + mevcutKullanici.getUid());

        DatabaseReference restoranIdYolu = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(mevcutKullanici.getUid()).child("restoranId");

        restoranIdYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot restoranId) {

                System.out.println(restoranId);

                yemekyolu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            System.out.println("YemekSahibi: " + dataSnapshot.child("yemekSahibi").getValue());
                            System.out.println("restoranId: " + restoranId);

                            if (dataSnapshot.child("yemekSahibi").getValue().equals(restoranId.getValue()))
                                yemekleriGoster(restoranId.getValue(String.class));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Kullanıcı Adı ve Restoran Adını Alalım.

        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(mevcutKullanici.getUid());

        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kullaniciAdi = snapshot.child("kullaniciAdi").getValue(String.class);

                restoranIdYolu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String restoranId = snapshot.getValue(String.class);

                        DatabaseReference restoranAdiYolu = FirebaseDatabase.getInstance().getReference().child("Restoranlar").child(restoranId).child("restoranAdi");

                        restoranAdiYolu.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String restoranAdi = snapshot.getValue(String.class);

                                kullaniciBilgileri(kullaniciAdi, restoranAdi);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void yemekleriGoster(String restoranId) {

        DatabaseReference yemekYolu = FirebaseDatabase.getInstance().getReference("Yemekler");

        yemekYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yemekListeleri.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.child("yemekSahibi").getValue(String.class).equals(restoranId)) {

                        Yemek yemek = snapshot.getValue(Yemek.class);

                        yemekListeleri.add(yemek);

                    }

                }

                yemekEkleAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_kullaniciAdi = view.findViewById(R.id.txt_kullaniciAdi_homeFragment);
        txt_restoranAdi = view.findViewById(R.id.txt_restoranAdi_homeFragment);
        img_restoranLogo = view.findViewById(R.id.img_logo_homeFragment);

    }

    private void kullaniciBilgileri(String kullaniciAdi, String restoranAdi) {

        txt_kullaniciAdi.setText(kullaniciAdi);
        txt_restoranAdi.setText(restoranAdi);

    }

}
package com.desailly.backdropsbliss.FragmentosAdministrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.desailly.backdropsbliss.CategoriasAdmin.MusicaA.MusicaA;
import com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA.PeliculasA;
import com.desailly.backdropsbliss.CategoriasAdmin.SeriesA.SeriesA;
import com.desailly.backdropsbliss.CategoriasAdmin.VideojuegosA.VideoJuegosA;
import com.desailly.backdropsbliss.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InicioAdmin extends Fragment {

    Button Peliculas,Series,Musica,Gaming;

    //fecha y bienvenida
    TextView fechaAdmin,NombreTXT;
    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        Peliculas = view.findViewById(R.id.Peliculas);
        Series = view.findViewById(R.id.Series);
        Musica = view.findViewById(R.id.Musica);
        Gaming = view.findViewById(R.id.Gaming);


        /*

        Contiene un calendario y la bienvenida al usuario posible implementacion

        fechaAdmin = view.findViewById(R.id.fechaAdmin);
        NombreTXT = view.findViewById(R.id.NombreTXT);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");

// Inicializar la variable 'date'
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy");
        String StringFecha = simpleDateFormat.format(date);
        fechaAdmin.setText("Hoy es: " + StringFecha);

        BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    // obtener el dato nombre
                    String nombre = "" + snapshot.child("NOMBRES").getValue();
                    NombreTXT.setText(nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error en caso de cancelación
            }
        });

        */


        Peliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PeliculasA.class));
            }
        });
        Series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SeriesA.class));
            }
        });

        Musica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MusicaA.class));
            }
        });

        Gaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VideoJuegosA.class));
            }
        });

        return view;
    }
}
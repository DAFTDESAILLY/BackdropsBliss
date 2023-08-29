package com.desailly.backdropsbliss.FragmentosAdministrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.desailly.backdropsbliss.CategoriasAdmin.MusicaA.MusicaA;
import com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA.PeliculasA;
import com.desailly.backdropsbliss.CategoriasAdmin.SeriesA.SeriesA;
import com.desailly.backdropsbliss.CategoriasAdmin.VideojuegosA.VideoJuegosA;
import com.desailly.backdropsbliss.R;

public class InicioAdmin extends Fragment {

    Button Peliculas,Series,Musica,Gaming;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        Peliculas = view.findViewById(R.id.Peliculas);
        Series = view.findViewById(R.id.Series);
        Musica = view.findViewById(R.id.Musica);
        Gaming = view.findViewById(R.id.Gaming);



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
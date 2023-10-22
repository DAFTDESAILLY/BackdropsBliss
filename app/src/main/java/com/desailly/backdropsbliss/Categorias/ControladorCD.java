package com.desailly.backdropsbliss.Categorias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.desailly.backdropsbliss.CategoriasCliente.MusicaCliente;
import com.desailly.backdropsbliss.CategoriasCliente.PeliculasCliente;
import com.desailly.backdropsbliss.CategoriasCliente.SeriesCliente;
import com.desailly.backdropsbliss.CategoriasCliente.VideojuegosCliente;
import com.desailly.backdropsbliss.R;

public class ControladorCD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlador_cd);


        String CategoriaRecuperada = getIntent().getStringExtra("Categoria");
        if (CategoriaRecuperada.equals("Peliculas")){
            startActivity(new Intent(ControladorCD.this, PeliculasCliente.class));
            finish();
        }
        if (CategoriaRecuperada.equals("Series")){
            startActivity(new Intent(ControladorCD.this, SeriesCliente.class));
            finish();
        }
        if (CategoriaRecuperada.equals("Musica")){
            startActivity(new Intent(ControladorCD.this, MusicaCliente.class));
            finish();
        }
        if (CategoriaRecuperada.equals("Videojuegos")){
            startActivity(new Intent(ControladorCD.this, VideojuegosCliente.class));
            finish();
        }
    }
}
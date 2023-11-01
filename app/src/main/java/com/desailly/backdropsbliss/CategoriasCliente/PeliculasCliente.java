package com.desailly.backdropsbliss.CategoriasCliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA.AgregarPelicula;
import com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA.Pelicula;
import com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA.PeliculasA;
import com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA.ViewHolderPelicula;
import com.desailly.backdropsbliss.DetelleCliente.DetalleCliente;
import com.desailly.backdropsbliss.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PeliculasCliente extends AppCompatActivity {
    RecyclerView recyclerViewPeliculaC;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Pelicula, ViewHolderPelicula> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Pelicula> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas_cliente);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Peliculas");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewPeliculaC =  findViewById(R.id.recyclerViewPeliculaC);
        recyclerViewPeliculaC.setHasFixedSize(true);
        mFirebaseDataBase =  FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("PELICULAS");


        dialog = new Dialog(PeliculasCliente.this);
        ListarImagenesPeliculas();
    }
    private void ListarImagenesPeliculas() {
        options = new FirebaseRecyclerOptions.Builder<Pelicula>().setQuery(mRef,Pelicula.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pelicula, ViewHolderPelicula>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderPelicula viewHolderPelicula , int i, @NonNull Pelicula pelicula) {
                viewHolderPelicula.SeteoPeliculas(
                        getApplicationContext(),
                        pelicula.getNombre(),
                        pelicula.getVistas(),
                        pelicula.getImagen()

                );
            }
            @NonNull
            @Override
            public ViewHolderPelicula onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pelicula,parent,false);
                ViewHolderPelicula viewHolderPelicula = new ViewHolderPelicula(itemView);
                viewHolderPelicula.setOnClickListener(new ViewHolderPelicula.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //Obtener los datos de la imagen
                        String Id = getItem(position).getId();
                        String Imagen = getItem(position).getImagen();
                        String Nombre = getItem(position).getNombre();
                        int Vistas = getItem(position).getVistas();
                        //convertir a string
                        String VistaString = String.valueOf(Vistas);
                        valueEventListener = mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds :  snapshot.getChildren()){
                                    //crear un objeto  clase peliuclas
                                    Pelicula pelicula = ds.getValue(Pelicula.class);

                                    if (pelicula.getId().equals(Id)){
                                        int i = 1;
                                        HashMap<String,Object>hashMap = new HashMap<>();
                                        //el valor que vamos a actualizar
                                        hashMap.put("vistas",Vistas+i);
                                        ds.getRef().updateChildren(hashMap);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //pasamos a la actividad detalle cliente
                        Intent intent = new Intent(PeliculasCliente.this,DetalleCliente.class);
                       //Datos a pasar
                        intent.putExtra("Imagen",Imagen);
                        intent.putExtra("Nombre",Nombre);
                        intent.putExtra("Vista",VistaString);

                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewHolderPelicula;
            }
        };

        sharedPreferences = PeliculasCliente.this.getSharedPreferences("PELICULAS",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar","Dos");

        //eligir el tipo de vista
        if (ordenar_en.equals("Dos")){

            recyclerViewPeliculaC.setLayoutManager(new GridLayoutManager(PeliculasCliente.this,2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPeliculaC.setAdapter(firebaseRecyclerAdapter);
        }
        else if (ordenar_en.equals("Tres")) {
            recyclerViewPeliculaC.setLayoutManager(new GridLayoutManager(PeliculasCliente.this,3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPeliculaC.setAdapter(firebaseRecyclerAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter !=null){
            firebaseRecyclerAdapter.startListening();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mRef != null && valueEventListener != null){
            mRef.removeEventListener(valueEventListener);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_vista,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.Vista){
            Ordenar_Imagenes();
        }
        return super.onOptionsItemSelected(item);
    }
    private void Ordenar_Imagenes(){

        //declarar vista
        TextView OrdenarTXT;
        Button Dos_Columnas,Tres_Columnas;
        //coneccion cuadro de dialog
        dialog.setContentView(R.layout.dialog_ordenar);

        //vistas
        OrdenarTXT = dialog.findViewById(R.id.OrdenarTXT);
        Dos_Columnas = dialog.findViewById(R.id.Dos_Columnas);
        Tres_Columnas = dialog.findViewById(R.id.Tres_Columnas);

        //eventio 2 columnas
        Dos_Columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Ordenar", "Dos");
                editor.apply();
                recreate();
                dialog.dismiss();
            }
        });
        Tres_Columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Ordenar", "Tres");
                editor.apply();
                recreate();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
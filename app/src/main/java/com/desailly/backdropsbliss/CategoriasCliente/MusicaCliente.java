package com.desailly.backdropsbliss.CategoriasCliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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

import com.desailly.backdropsbliss.CategoriasAdmin.MusicaA.Musica;
import com.desailly.backdropsbliss.CategoriasAdmin.MusicaA.ViewHolderMusica;
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

public class MusicaCliente extends AppCompatActivity {

    RecyclerView recyclerViewMusicaC;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;
    FirebaseRecyclerAdapter<Musica, ViewHolderMusica> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Musica> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica_cliente);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Musica");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewMusicaC = findViewById(R.id.recyclerViewMusicaC);
        recyclerViewMusicaC.setHasFixedSize(true);
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("MUSICA");

        dialog = new Dialog(MusicaCliente.this);

        ListarImagenesMusica();

    }

    private void ListarImagenesMusica() {
        options = new FirebaseRecyclerOptions.Builder<Musica>().setQuery(mRef, Musica.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Musica, ViewHolderMusica>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderMusica viewHolderMusica, int i, @NonNull Musica musica) {
                viewHolderMusica.SeteoMusica(
                        getApplicationContext(),
                        musica.getNombre(),
                        musica.getVistas(),
                        musica.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderMusica onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item__musica, parent, false);
                ViewHolderMusica viewHolderMusica = new ViewHolderMusica(itemView);
                viewHolderMusica.setOnClickListener(new ViewHolderMusica.ClickListener() {
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
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    //crear un objeto  clase peliuclas
                                    Musica musica = ds.getValue(Musica.class);

                                    if (musica.getId().equals(Id)) {
                                        int i = 1;
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        //el valor que vamos a actualizar
                                        hashMap.put("vistas", Vistas + i);
                                        ds.getRef().updateChildren(hashMap);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        //pasamos a la actividad detalle cliente
                        Intent intent = new Intent(MusicaCliente.this, DetalleCliente.class);
                        //Datos a pasar
                        intent.putExtra("Imagen", Imagen);
                        intent.putExtra("Nombre", Nombre);
                        intent.putExtra("Vista", VistaString);

                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                });
                return viewHolderMusica;
            }
        };

        sharedPreferences = MusicaCliente.this.getSharedPreferences("MUSICA", MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar", "Dos");

        //eligir el tipo de vista
        if (ordenar_en.equals("Dos")) {

            recyclerViewMusicaC.setLayoutManager(new GridLayoutManager(MusicaCliente.this, 2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewMusicaC.setAdapter(firebaseRecyclerAdapter);
        } else if (ordenar_en.equals("Tres")) {
            recyclerViewMusicaC.setLayoutManager(new GridLayoutManager(MusicaCliente.this, 3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewMusicaC.setAdapter(firebaseRecyclerAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRef != null && valueEventListener != null) {
            mRef.removeEventListener(valueEventListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_vista, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Vista) {
            Ordenar_Imagenes();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Ordenar_Imagenes() {

        //declarar vista
        TextView OrdenarTXT;
        Button Dos_Columnas, Tres_Columnas;
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

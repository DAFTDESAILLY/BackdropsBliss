package com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

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

import com.desailly.backdropsbliss.FragmentosAdministrador.PerfilAdmin;
import com.desailly.backdropsbliss.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class PeliculasA extends AppCompatActivity {

    RecyclerView recyclerViewPelicula;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Pelicula,ViewHolderPelicula>firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Pelicula> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Peliculas");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewPelicula =  findViewById(R.id.recyclerViewPelicula);
        recyclerViewPelicula.setHasFixedSize(true);
        mFirebaseDataBase =  FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("PELICULAS");


        dialog = new Dialog(PeliculasA.this);
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
                        Toast.makeText(PeliculasA.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        String Id = getItem(position).getId();
                        String Nombre = getItem(position).getNombre();
                        String Imagen = getItem(position).getImagen();

                        int Vista = getItem(position).getVistas();
                       final String VistaString = String.valueOf(Vista);

                        AlertDialog.Builder builder = new AlertDialog.Builder(PeliculasA.this);

                        String[]opciones = {"Actualizar","Eliminar"};
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0 ){
                                    Intent intent = new Intent(PeliculasA.this,AgregarPelicula.class);
                                    intent.putExtra("IdEnviado",Id);
                                    intent.putExtra("NombreEnviado",Nombre);
                                    intent.putExtra("ImagenEnviada",Imagen);
                                    intent.putExtra("VistaEnviada",VistaString);
                                    startActivity(intent);
                                }
                                if (i == 1 ){
                                    EliminarDatos(Id,Imagen);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });
                return viewHolderPelicula;
            }
        };

        sharedPreferences = PeliculasA.this.getSharedPreferences("PELICULAS",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar","Dos");

        //eligir el tipo de vista
        if (ordenar_en.equals("Dos")){

            recyclerViewPelicula.setLayoutManager(new GridLayoutManager(PeliculasA.this,2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPelicula.setAdapter(firebaseRecyclerAdapter);
        }
        else if (ordenar_en.equals("Tres")) {
            recyclerViewPelicula.setLayoutManager(new GridLayoutManager(PeliculasA.this,3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPelicula.setAdapter(firebaseRecyclerAdapter);
        }
    }


    private void EliminarDatos(final String IdActual,final String ImagenActual){
        AlertDialog.Builder builder = new AlertDialog.Builder(PeliculasA.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Desea eliminar la imagen?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Eliminar imagen de la db
                Query query = mRef.orderByChild("id").equalTo(IdActual);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(PeliculasA.this, "La imagen se ha eliminado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PeliculasA.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                StorageReference ImagenSeleccionado = getInstance().getReferenceFromUrl(ImagenActual);
                ImagenSeleccionado.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PeliculasA.this, "Eliminado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PeliculasA.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PeliculasA.this, "Se ha Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter !=null){
            firebaseRecyclerAdapter.startListening();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agregar,menu);
        menuInflater.inflate(R.menu.menu_vista,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.Agregar){
            startActivity(new Intent(PeliculasA.this,AgregarPelicula.class));
            finish();
        }
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
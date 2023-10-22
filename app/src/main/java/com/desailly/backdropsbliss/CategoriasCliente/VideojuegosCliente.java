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

import com.desailly.backdropsbliss.CategoriasAdmin.VideojuegosA.AgregarVideojuegos;
import com.desailly.backdropsbliss.CategoriasAdmin.VideojuegosA.VideoJuego;
import com.desailly.backdropsbliss.CategoriasAdmin.VideojuegosA.VideoJuegosA;
import com.desailly.backdropsbliss.CategoriasAdmin.VideojuegosA.ViewHolderVideojuegos;
import com.desailly.backdropsbliss.DetelleCliente.DetalleCliente;
import com.desailly.backdropsbliss.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VideojuegosCliente extends AppCompatActivity {

    RecyclerView recyclerViewVideoJuegosC;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<VideoJuego, ViewHolderVideojuegos> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<VideoJuego> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videojuegos_cliente);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Gaming");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewVideoJuegosC =  findViewById(R.id.recyclerViewVideoJuegosC);
        recyclerViewVideoJuegosC.setHasFixedSize(true);
        mFirebaseDataBase =  FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("VIDEOJUEGOS");

        dialog = new Dialog(VideojuegosCliente.this);

        ListarImagenesVideoJuegos();

    }
    private void ListarImagenesVideoJuegos() {
        options = new FirebaseRecyclerOptions.Builder<VideoJuego>().setQuery(mRef,VideoJuego.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<VideoJuego, ViewHolderVideojuegos>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderVideojuegos viewHolderVideojuegos , int i, @NonNull VideoJuego videoJuego) {
                viewHolderVideojuegos.SeteoVideoJuegos(
                        getApplicationContext(),
                        videoJuego.getNombre(),
                        videoJuego.getVistas(),
                        videoJuego.getImagen()

                );
            }
            @NonNull
            @Override
            public ViewHolderVideojuegos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videojuegos,parent,false);
                ViewHolderVideojuegos viewHolderVideojuegos = new ViewHolderVideojuegos(itemView);
                viewHolderVideojuegos.setOnClickListener(new ViewHolderVideojuegos.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(VideojuegosCliente.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(VideojuegosCliente.this, DetalleCliente.class));

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewHolderVideojuegos;
            }
        };

        sharedPreferences = VideojuegosCliente.this.getSharedPreferences("VIDEOJUEGOS",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar","Dos");

        //eligir el tipo de vista
        if (ordenar_en.equals("Dos")){

            recyclerViewVideoJuegosC.setLayoutManager(new GridLayoutManager(VideojuegosCliente.this,2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewVideoJuegosC.setAdapter(firebaseRecyclerAdapter);
        }
        else if (ordenar_en.equals("Tres")) {
            recyclerViewVideoJuegosC.setLayoutManager(new GridLayoutManager(VideojuegosCliente.this,3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewVideoJuegosC.setAdapter(firebaseRecyclerAdapter);
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



package com.desailly.backdropsbliss.CategoriasAdmin.MusicaA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA.ViewHolderPelicula;
import com.desailly.backdropsbliss.R;
import com.squareup.picasso.Picasso;

public class ViewHolderMusica extends RecyclerView.ViewHolder {
    View mView;
    private ViewHolderMusica.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position); // Corregido el nombre del método
        void onItemLongClick(View view, int position);
    }

    //metodo para poder presionar o mantener presionado un item
    public void setOnClickListener(ViewHolderMusica.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderMusica(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mClickListener.onItemClick(view,getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
              mClickListener.onItemLongClick(view,getBindingAdapterPosition());
              return true;
            }
        });
    }
    public void SeteoMusica(Context context, String nombre, int vista, String imagen){
        ImageView Imagen_Musica;
        TextView NombreImagen_Musica;
        TextView Vista_Musica;

        //Conexion con el item

        Imagen_Musica = mView.findViewById(R.id.Imagen_Musica);
        NombreImagen_Musica = mView.findViewById(R.id.NombreImagen_Musica);
        Vista_Musica = mView.findViewById(R.id.Vista_Musica);

        NombreImagen_Musica.setText(nombre);

        //convertir a string vista
        String VistaString = String.valueOf(vista);
        Vista_Musica.setText(VistaString);

        try {

            Picasso.get().load(imagen).into(Imagen_Musica);
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

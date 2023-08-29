package com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.desailly.backdropsbliss.R;
import com.squareup.picasso.Picasso;

public class ViewHolderPelicula extends RecyclerView.ViewHolder {


    View mView;
    private ViewHolderPelicula.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position); // Corregido el nombre del método
        void onItemLongClick(View view, int position);
    }

    //metodo para poder presionar o mantener presionado un item
    public void setOnClickListener(ViewHolderPelicula.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderPelicula(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, getAdapterPosition()); // Corregido el nombre del método
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemLongClick(v, getAdapterPosition()); // Corregido el nombre del método
                    return true;
                }
                return false;
            }
        });
    }
    public void SeteoPeliculas(Context context, String nombre, int vista, String imagen){
        ImageView ImagePelicula;
        TextView NombreImagenPelicula;
        TextView VistaPelicula;

        //Conexion con el item

        ImagePelicula = mView.findViewById(R.id.ImagenPelicula);
        NombreImagenPelicula = mView.findViewById(R.id.NombreImagenPelicula);
        VistaPelicula = mView.findViewById(R.id.VistaPelicula);

        NombreImagenPelicula.setText(nombre);

        //convertir a string vista
        String VistaString = String.valueOf(vista);
        VistaPelicula.setText(VistaString);

        try {

            Picasso.get().load(imagen).into(ImagePelicula);
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}

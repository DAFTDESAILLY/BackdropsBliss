package com.desailly.backdropsbliss.CategoriasAdmin.SeriesA;

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

public class ViewHolderSerie extends RecyclerView.ViewHolder {

    View mView;
    private ViewHolderSerie.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position); // Corregido el nombre del método
        void onItemLongClick(View view, int position);
    }

    //metodo para poder presionar o mantener presionado un item
    public void setOnClickListener(ViewHolderSerie.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderSerie(@NonNull View itemView) {
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
    public void SeteoSerie(Context context, String nombre, int vista, String imagen){
        ImageView Imagen_Serie;
        TextView NombreImagen_Serie;
        TextView Vista_Serie;

        //Conexion con el item

        Imagen_Serie = mView.findViewById(R.id.Imagen_Serie);
        NombreImagen_Serie = mView.findViewById(R.id.NombreImagen_Serie);
        Vista_Serie = mView.findViewById(R.id.Vista_Serie);

        NombreImagen_Serie.setText(nombre);

        //convertir a string vista
        String VistaString = String.valueOf(vista);
        Vista_Serie.setText(VistaString);

        try {

            Picasso.get().load(imagen).into(Imagen_Serie);
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}

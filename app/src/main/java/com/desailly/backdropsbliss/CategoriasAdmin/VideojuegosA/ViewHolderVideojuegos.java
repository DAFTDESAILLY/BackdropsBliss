package com.desailly.backdropsbliss.CategoriasAdmin.VideojuegosA;

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

public class ViewHolderVideojuegos extends RecyclerView.ViewHolder{
    View mView;
    private ViewHolderVideojuegos.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position); // Corregido el nombre del método
        void onItemLongClick(View view, int position);
    }

    //metodo para poder presionar o mantener presionado un item
    public void setOnClickListener(ViewHolderVideojuegos.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderVideojuegos(@NonNull View itemView) {
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
    public void SeteoVideoJuegos(Context context, String nombre, int vista, String imagen){
        ImageView ImagenVideoJuegos;
        TextView Nombre_VideoJuegos;
        TextView Vista_VideoJuegos;

        //Conexion con el item

        ImagenVideoJuegos = mView.findViewById(R.id.ImagenVideoJuegos);
        Nombre_VideoJuegos = mView.findViewById(R.id.Nombre_VideoJuegos);
        Vista_VideoJuegos = mView.findViewById(R.id.Vista_VideoJuegos);

        Nombre_VideoJuegos.setText(nombre);

        //convertir a string vista
        String VistaString = String.valueOf(vista);
        Vista_VideoJuegos.setText(VistaString);

        try {

            Picasso.get().load(imagen).into(ImagenVideoJuegos);
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}

package com.desailly.backdropsbliss.Categorias.Cat_Firebase;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.desailly.backdropsbliss.R;
import com.squareup.picasso.Picasso;

public class ViewHolderCF extends RecyclerView.ViewHolder {

    View mView;
    private ViewHolderCF.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position); // Corregido el nombre del método
    }

    //metodo para poder presionar o mantener presionado un item
    public void setOnClickListener(ViewHolderCF.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderCF(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getBindingAdapterPosition());
            }
        });
    }
    public void SeteoCategoriaF(Context context, String categoria, String imagen){
        ImageView ImagenCategoriaF;
        TextView NombreCategoriaF;


        //Conexion con el item

        ImagenCategoriaF = mView.findViewById(R.id.ImagenCategoriaF);
        NombreCategoriaF = mView.findViewById(R.id.NombreCategoriaF);


        NombreCategoriaF.setText(categoria);
        try {

            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenCategoriaF);
        }catch (Exception e){
            // Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Picasso.get().load(R.drawable.categoria).into(ImagenCategoriaF);
        }
    }

}

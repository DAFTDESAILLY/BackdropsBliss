package com.desailly.backdropsbliss.Apartado_informativo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.desailly.backdropsbliss.R;
import com.squareup.picasso.Picasso;

public class ViewHolderInformacion  extends RecyclerView.ViewHolder  {

    View mView;
    private ViewHolderInformacion.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position); // Corregido el nombre del método
    }

    //metodo para poder presionar o mantener presionado un item
    public void setOnClickListener(ViewHolderInformacion.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderInformacion(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getBindingAdapterPosition());
            }
        });
    }
    public void SeteoInformacion(Context context, String nombre, String imagen){
        ImageView ImagenInformativo;
        TextView TituloinformativoTXT;


        //Conexion con el item

        ImagenInformativo = mView.findViewById(R.id.ImagenInformativo);
        TituloinformativoTXT = mView.findViewById(R.id.TituloinformativoTXT);


        TituloinformativoTXT.setText(nombre);
        try {

            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenInformativo);
        }catch (Exception e){
            // Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Picasso.get().load(R.drawable.categoria).into(ImagenInformativo);
        }
    }

}

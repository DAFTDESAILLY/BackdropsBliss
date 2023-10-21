package com.desailly.backdropsbliss.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.desailly.backdropsbliss.Detalle.Detalle_administrador;
import com.desailly.backdropsbliss.Modelo.Administrador;
import com.desailly.backdropsbliss.R;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;
public class Adaptador extends RecyclerView.Adapter<Adaptador.MyHolder> {
    private Context context;
    private List<Administrador> administradores;
    public Adaptador(Context context, List<Administrador> administradores) {
        this.context = context;
        this.administradores = administradores;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el admin_layout
        View view = LayoutInflater.from(context).inflate(R.layout.admin_item, parent, false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        // Obtener los datos del modelo
        String UID = administradores.get(position).getUID();
        String IMAGEN = administradores.get(position).getIMAGEN();
        String NOMBRES = administradores.get(position).getNOMBRES();
        String APELLIDOS = administradores.get(position).getAPELLIDOS();
        String CORREO = administradores.get(position).getCORREO();
        int  EDAD = administradores.get(position).getEDAD();
        String EdadString = String.valueOf(EDAD);

        // Seteo de datos
        holder.NombresADMIN.setText(NOMBRES);
        holder.CorreoADMIN.setText(CORREO);
        try {
            // Si existe en la base de datos
            Picasso.get().load(IMAGEN).placeholder(R.drawable.usuario).into(holder.PerfilADMIN);
        } catch (Exception e) {
            // No existe en la base de datos
            Picasso.get().load(R.drawable.usuario).into(holder.PerfilADMIN);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Detalle_administrador.class);
                //pasar datos  a la actividad
                intent.putExtra("UID",UID);
                intent.putExtra("NOMBRES",NOMBRES);
                intent.putExtra("APELLIDOS",APELLIDOS);
                intent.putExtra("CORREO",CORREO);
                intent.putExtra("EDAD",EdadString);
                intent.putExtra("IMAGEN",IMAGEN);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return administradores.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        // Declarar vistas
        CircleImageView PerfilADMIN;
        TextView NombresADMIN, CorreoADMIN;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            PerfilADMIN = itemView.findViewById(R.id.PerfilADMIN);
            NombresADMIN = itemView.findViewById(R.id.NombresADMIN);
            CorreoADMIN = itemView.findViewById(R.id.CorreoADMIN);
        }
    }
}

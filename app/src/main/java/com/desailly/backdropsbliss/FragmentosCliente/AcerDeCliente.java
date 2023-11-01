package com.desailly.backdropsbliss.FragmentosCliente;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.desailly.backdropsbliss.InicioSesion;
import com.desailly.backdropsbliss.MainActivity;
import com.desailly.backdropsbliss.R;


public class AcerDeCliente extends Fragment {


    TextView Ir_website,Ir_facebook,Ir_instagram;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_acer_de_cliente, container, false);


        Ir_website = view.findViewById(R.id.Ir_website);
        Ir_facebook = view.findViewById(R.id.Ir_facebook);
        Ir_instagram = view.findViewById(R.id.Ir_instagram);

        Ir_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Proximamente", Toast.LENGTH_SHORT).show();
             /*   Uri uri = Uri.parse("");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent); */
            }
        });

        Ir_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Proximamente", Toast.LENGTH_SHORT).show();
               /*
               Link a la pag web
               Uri uri = Uri.parse("");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent); */
            }
        });

        Ir_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Proximamente", Toast.LENGTH_SHORT).show();
               /* Uri uri = Uri.parse("");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent); */
            }
        });



        return view;
    }
}
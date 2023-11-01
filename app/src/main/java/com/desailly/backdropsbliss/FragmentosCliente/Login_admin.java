package com.desailly.backdropsbliss.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.desailly.backdropsbliss.InicioSesion;
import com.desailly.backdropsbliss.R;

public class Login_admin extends Fragment {


    Button Acceder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_admin, container, false);


        Acceder = view.findViewById(R.id.Acceder);
        Acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InicioSesion.class));
            }
        });


        return view;
    }
}
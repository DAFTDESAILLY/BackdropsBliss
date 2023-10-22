package com.desailly.backdropsbliss.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.desailly.backdropsbliss.Categorias.Cat_Dispositivo.CategoriaD;
import com.desailly.backdropsbliss.Categorias.Cat_Dispositivo.ViewHolderCD;
import com.desailly.backdropsbliss.Categorias.ControladorCD;
import com.desailly.backdropsbliss.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InicioCliente extends Fragment {


    RecyclerView recyclerViewCategoriasD;
    FirebaseDatabase firebaseDatabaseD;
    DatabaseReference referenceD;
    LinearLayoutManager linearLayoutManagerD;
    FirebaseRecyclerAdapter<CategoriaD, ViewHolderCD> firebaseRecyclerAdapterD;
    FirebaseRecyclerOptions<CategoriaD> optionsD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_inicio_cliente, container, false);

       firebaseDatabaseD = FirebaseDatabase.getInstance();
       referenceD = firebaseDatabaseD.getReference("CATEGORIAS_D");
       linearLayoutManagerD = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

       recyclerViewCategoriasD = view .findViewById(R.id.recyclerViewCategoriasD);
       recyclerViewCategoriasD.setHasFixedSize(true);
       recyclerViewCategoriasD.setLayoutManager(linearLayoutManagerD);

       VerCategoriasD();

        return view;
    }

    private void VerCategoriasD (){
        optionsD =  new FirebaseRecyclerOptions.Builder<CategoriaD>().setQuery(referenceD,CategoriaD.class).build();
        firebaseRecyclerAdapterD = new FirebaseRecyclerAdapter<CategoriaD, ViewHolderCD>(optionsD) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderCD viewHolderCD, int i, @NonNull CategoriaD categoriaD) {
                viewHolderCD.SeteoCategoriaD(
                       getActivity(),
                       categoriaD.getCategoria(),
                       categoriaD.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderCD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_dispositivo,parent,false);
                ViewHolderCD viewHolderCD = new ViewHolderCD(itemView);

                viewHolderCD.setOnClickListener(new ViewHolderCD.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Obtener el nombre de la categoria
                        String categoria = getItem(position).getCategoria();

                        Intent intent = new Intent(view.getContext(), ControladorCD.class);
                        intent.putExtra("Categoria",categoria);
                        startActivity(intent);
                        Toast.makeText(getActivity(), categoria, Toast.LENGTH_SHORT).show();
                    }
                });

                return viewHolderCD;
            }
        };
        recyclerViewCategoriasD.setAdapter(firebaseRecyclerAdapterD);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapterD != null){
            firebaseRecyclerAdapterD.startListening();
        }
    }
}
package com.desailly.backdropsbliss.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.desailly.backdropsbliss.Categorias.Cat_Dispositivo.CategoriaD;
import com.desailly.backdropsbliss.Categorias.Cat_Dispositivo.ViewHolderCD;
import com.desailly.backdropsbliss.Categorias.Cat_Firebase.CategoriaF;
import com.desailly.backdropsbliss.Categorias.Cat_Firebase.ViewHolderCF;
import com.desailly.backdropsbliss.Categorias.ControladorCD;
import com.desailly.backdropsbliss.CategoriasClienteFirebase.ListaCategoriaFirebase;
import com.desailly.backdropsbliss.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.text.SimpleDateFormat;
import java.util.Date;

public class InicioCliente extends Fragment {


    RecyclerView recyclerViewCategoriasD,recyclerViewCategoriasF;
    FirebaseDatabase firebaseDatabaseD,firebaseDatabaseF;
    DatabaseReference referenceD,referenceF;
    LinearLayoutManager linearLayoutManagerD,linearLayoutManagerF;
    FirebaseRecyclerAdapter<CategoriaD, ViewHolderCD> firebaseRecyclerAdapterD;
    FirebaseRecyclerAdapter<CategoriaF, ViewHolderCF> firebaseRecyclerAdapterF;
    FirebaseRecyclerOptions<CategoriaD> optionsD;
    FirebaseRecyclerOptions<CategoriaF> optionsF;

    TextView fecha;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_inicio_cliente, container, false);

       firebaseDatabaseD = FirebaseDatabase.getInstance();
       firebaseDatabaseF = FirebaseDatabase.getInstance();
       
       referenceD = firebaseDatabaseD.getReference("CATEGORIAS_D");
       referenceF = firebaseDatabaseF.getReference("CATEGORIAS_F");
       
       
       linearLayoutManagerD = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
       linearLayoutManagerF = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
       
       

       recyclerViewCategoriasD = view .findViewById(R.id.recyclerViewCategoriasD);
       recyclerViewCategoriasD.setHasFixedSize(true);
       recyclerViewCategoriasD.setLayoutManager(linearLayoutManagerD);
       
        recyclerViewCategoriasF = view .findViewById(R.id.recyclerViewCategoriasF);
        recyclerViewCategoriasF.setHasFixedSize(true);
        recyclerViewCategoriasF.setLayoutManager(linearLayoutManagerF);

        fecha = view.findViewById(R.id.fecha);
        //fecha actual
        Date date   = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d 'de' MMMM 'del' yyyy" );
        String StringFecha = simpleDateFormat.format(date);
        fecha.setText(StringFecha);



       VerCategoriasD();
       VerCategoriasF();
        //verApartadoInformativo();

        return view;
    }

   /* private void InicializarMerlin(){
        merlin = new Merlin.Builder().withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallBacks()
                .build(getActivity());
    } */

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


    private void VerCategoriasF() {
        optionsF =  new FirebaseRecyclerOptions.Builder<CategoriaF>().setQuery(referenceF,CategoriaF.class).build();
        firebaseRecyclerAdapterF = new FirebaseRecyclerAdapter<CategoriaF, ViewHolderCF>(optionsF) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderCF viewHolderCF, int i, @NonNull CategoriaF categoriaF) {
                viewHolderCF.SeteoCategoriaF(
                        getActivity(),
                        categoriaF.getCategoria(),
                        categoriaF.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderCF onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_firebase,parent,false);
                ViewHolderCF viewHolderCF = new ViewHolderCF(itemView);

                viewHolderCF.setOnClickListener(new ViewHolderCF.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //obtener el nombre de la categoria
                        String NOMBRE_CATEGORIA = getItem(position).getCategoria();

                        //pasar el nombre se la categoria de sgte actividad
                        Intent intent = new Intent(view.getContext(), ListaCategoriaFirebase.class);
                        intent.putExtra("NOMBRE_CATEGORIA",NOMBRE_CATEGORIA);
                       // Toast.makeText(view.getContext(), "CATEGORIA SELECCIONADA = "+ NOMBRE_CATEGORIA, Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    }
                });

                return viewHolderCF;
            }
        };
        recyclerViewCategoriasF.setAdapter(firebaseRecyclerAdapterF);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapterD != null && firebaseRecyclerAdapterF != null){
            firebaseRecyclerAdapterD.startListening();
            firebaseRecyclerAdapterF.startListening();
        }
    }
}
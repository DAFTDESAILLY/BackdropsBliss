package com.desailly.backdropsbliss.FragmentosAdministrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.desailly.backdropsbliss.InicioSesion;
import com.desailly.backdropsbliss.MainActivityAdministrador;
import com.desailly.backdropsbliss.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Cambio_Pass extends AppCompatActivity {

    TextView PassActual;
    TextView ActualPassET,NuevaPassET;
    Button CAMBIARPASSBTN,IRINICIOBTN;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;
    FirebaseAuth firebaseAuth;
    FirebaseUser user; // Corrected variable name

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_pass);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Cambiar contraseña");

        PassActual = findViewById(R.id.PassActual);
        ActualPassET = findViewById(R.id.ActualPassET);
        NuevaPassET = findViewById(R.id.NuevaPassET);
        CAMBIARPASSBTN = findViewById(R.id.CAMBIARPASSBTN);
        IRINICIOBTN = findViewById(R.id.IRINICIOBTN);

        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(Cambio_Pass.this);

        //consulta en bd
        Query query = BASE_DE_DATOS_ADMINISTRADORES.orderByChild("CORREO").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds:snapshot.getChildren()){
                    //Obtener la contraseña
                    String pass = ""+ds.child("PASSWORD").getValue();
                    PassActual.setText(pass);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CAMBIARPASSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ACTUAL_PASS = ActualPassET.getText().toString().trim();
                String NUEVO_PASS = NuevaPassET.getText().toString().trim();

                //Condiciones
                if (TextUtils.isEmpty(ACTUAL_PASS)){
                    Toast.makeText(Cambio_Pass.this, "El campo contraseña actual esta  vacio", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(NUEVO_PASS)){
                    Toast.makeText(Cambio_Pass.this, "El campo contraseña nueva esta  vacio", Toast.LENGTH_SHORT).show();
                }
                if (!ACTUAL_PASS.equals("") && !NUEVO_PASS.equals("")&& NUEVO_PASS.length()>=6){
                    Cambio_Password(ACTUAL_PASS,NUEVO_PASS);
                }else{
                    NuevaPassET.setError("La contraseña debe ser mayor o igual a 6");
                    NuevaPassET.setFocusable(true);
                }
            }
        });
        IRINICIOBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cambio_Pass.this, MainActivityAdministrador.class));
            }
        });
    }

    private void Cambio_Password(String pass_actual , final String nuevo_pass) {
        progressDialog.show();
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere por favor");

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(),pass_actual);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(nuevo_pass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        String NUEVO_PASS = NuevaPassET.getText().toString().trim();
                                        HashMap<String,Object> resultado = new HashMap<>();
                                        resultado.put("PASSWORD",NUEVO_PASS);
                                        //Actualizar pass
                                        BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(resultado)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(Cambio_Pass.this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                                                        //cerrar sesion
                                                        firebaseAuth.signOut();
                                                        startActivity(new Intent(Cambio_Pass.this, InicioSesion.class));
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }
                                                });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onBackPressed() {

    }
}
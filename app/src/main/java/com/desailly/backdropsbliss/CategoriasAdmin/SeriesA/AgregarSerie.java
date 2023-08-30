package com.desailly.backdropsbliss.CategoriasAdmin.SeriesA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA.AgregarPelicula;
import com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA.Pelicula;
import com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA.PeliculasA;
import com.desailly.backdropsbliss.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AgregarSerie extends AppCompatActivity {


    EditText Nombre_Serie;
    TextView Vista_Serie;
    ImageView ImagenAgregar_Serie;
    Button Publicar_Serie;

    String RutaDeAlmacenamiento = "Serie_Subida/";
    String RutaDeBaseDeDatos = "SERIE";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;
    ProgressDialog progressDialog;
    int CODIGO_DE_SOLICITUD_IMAGEN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_serie);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Publicar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Vista_Serie = findViewById(R.id.Vista_Serie);
        Nombre_Serie = findViewById(R.id.Nombre_Serie);
        ImagenAgregar_Serie = findViewById(R.id.ImagenAgregar_Serie);
        Publicar_Serie = findViewById(R.id.Publicar_Serie);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarSerie.this);

        ImagenAgregar_Serie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),CODIGO_DE_SOLICITUD_IMAGEN);
            }
        });

        Publicar_Serie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubirImagen();
            }
        });
    }



    private void SubirImagen() {
        if (RutaArchivoUri!=null){
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Subiendo Imagen Serie");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference storageReference2 = mStorageReference.child(RutaDeAlmacenamiento+System.currentTimeMillis()+"."+ObtenerExtensionDelArchivo(RutaArchivoUri));
            storageReference2.putFile(RutaArchivoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());

                            Uri downloadURI = uriTask.getResult();

                            String mNombre = Nombre_Serie.getText().toString();
                            String mVista = Vista_Serie.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Serie serie = new Serie(downloadURI.toString(),mNombre,VISTA);
                            String ID_IMAGEN = DatabaseReference.push().getKey();

                            DatabaseReference.child(ID_IMAGEN).setValue(serie);

                            progressDialog.dismiss();
                            Toast.makeText(AgregarSerie.this, "Subido Exitosamente", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(AgregarSerie.this, SeriesA.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AgregarSerie.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            progressDialog.setTitle("Publicando");
                            progressDialog.setCancelable(false);
                        }
                    });
        }
        else {
            Toast.makeText(this, "DEBE ASIGNAR UNA IMAGEN", Toast.LENGTH_SHORT).show();
        }
    }

    private String ObtenerExtensionDelArchivo(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CODIGO_DE_SOLICITUD_IMAGEN
                && resultCode == RESULT_OK
                && data != null
                && data.getData() !=null){

            RutaArchivoUri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),RutaArchivoUri);
                ImagenAgregar_Serie.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }





}
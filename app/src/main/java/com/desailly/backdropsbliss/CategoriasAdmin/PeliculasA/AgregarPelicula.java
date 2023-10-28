package com.desailly.backdropsbliss.CategoriasAdmin.PeliculasA;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.desailly.backdropsbliss.CategoriasAdmin.MusicaA.AgregarMusica;
import com.desailly.backdropsbliss.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import kotlinx.coroutines.EventLoopImplBase;

public class AgregarPelicula extends AppCompatActivity {

    EditText NombrePeliculas;
    TextView VistaPeliculas,IdPeliculas;
    ImageView ImagenAgregarPelicula;
    Button PublicarPelicula;

    String RutaDeAlmacenamiento = "Pelicula_Subida/";
    String RutaDeBaseDeDatos = "PELICULAS";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;

    ProgressDialog progressDialog;

    String  rId,rNombre,rImagen,rVista;

  //  int CODIGO_DE_SOLICITUD_IMAGEN = 5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pelicula);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Publicar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        IdPeliculas = findViewById(R.id.IdPeliculas);
        VistaPeliculas = findViewById(R.id.VistaPeliculas);
        NombrePeliculas = findViewById(R.id.NombrePeliculas);
        ImagenAgregarPelicula = findViewById(R.id.ImagenAgregarPelicula);
        PublicarPelicula = findViewById(R.id.PublicarPelicula);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarPelicula.this);

        Bundle intent = getIntent().getExtras();
        if (intent != null){
            //recuperar los datos de la actividad anteriror
            rId = intent.getString("IdEnviado");
            rNombre = intent.getString("NombreEnviado");
            rImagen = intent.getString("ImagenEnviada");
            rVista = intent.getString("VistaEnviada");

            //setear
            IdPeliculas.setText(rId);
            NombrePeliculas.setText(rNombre);
            VistaPeliculas.setText(rVista);
            Picasso.get().load(rImagen).into(ImagenAgregarPelicula);

            //Cambiar el nombre del action bar
            actionBar.setTitle("Actualizar");
            String actualizar = "Actualizar";
            //cambiar el nombre del boton
            PublicarPelicula.setText(actualizar);
        }

        ImagenAgregarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SDK 30
                /*
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),CODIGO_DE_SOLICITUD_IMAGEN);*/
                //SDK  31
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                ObtenerImagenGaleria.launch(intent);
            }
        });

        PublicarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PublicarPelicula.getText().equals("Publicar")){
                    SubirImagen();
                }else {
                    EmpezarActualizacion();
                }
            }
        });
    }

    private void EmpezarActualizacion() {
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere por favor");
        progressDialog.show();
        progressDialog.setCancelable(false);
        EliminarImagenAnterior();
    }
    private void EliminarImagenAnterior() {
        StorageReference Imagen = getInstance().getReferenceFromUrl(rImagen);
        Imagen.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //si la imagen se elimina
                Toast.makeText(AgregarPelicula.this, "La imagen a sido eliminada", Toast.LENGTH_SHORT).show();
                SubirNuevaImagen();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarPelicula.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    private void SubirNuevaImagen() {
        String nuevaImagen = System.currentTimeMillis()+".png";
        StorageReference mStorageReference2 = mStorageReference.child(RutaDeAlmacenamiento + nuevaImagen);
        Bitmap bitmap = ((BitmapDrawable)ImagenAgregarPelicula.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte [] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = mStorageReference2.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AgregarPelicula.this, "Nueva imagen cargada ", Toast.LENGTH_SHORT).show();
                Task<Uri>uriTask = taskSnapshot.getStorage().getDownloadUrl()   ;
                while(!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                ActualizarImagenBD(downloadUri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarPelicula.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    private void ActualizarImagenBD(String NuevaImagen) {
        final String nombreActualizar = NombrePeliculas.getText().toString();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("PELICULAS");

        //CONSULTA
        Query query = databaseReference.orderByChild("id").equalTo(rId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //DATOS A ACTUALIZAR
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("nombre").setValue(nombreActualizar);
                    ds.getRef().child("imagen").setValue(NuevaImagen);
                }
                progressDialog.dismiss();
                Toast.makeText(AgregarPelicula.this, "Actualizado Correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AgregarPelicula.this,PeliculasA.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void SubirImagen() {

        String mNombre = NombrePeliculas.getText().toString();
        //validar que el nombre  y la imagen no sean nulas
        if (mNombre.equals("")||RutaArchivoUri==null){
            Toast.makeText(this, "Asigne un nombre o una imagen", Toast.LENGTH_SHORT).show();
        }

        else {
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Subiendo Imagen Pelicula");
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

                            String ID = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss",
                                    Locale.getDefault()).format(System.currentTimeMillis());
                            IdPeliculas.setText(ID);

                            String mNombre = NombrePeliculas.getText().toString();
                            String mId = IdPeliculas.getText().toString();
                            String mVista = VistaPeliculas.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Pelicula pelicula = new Pelicula(mNombre+"/" +mId,downloadURI.toString(),mNombre,VISTA);
                            String ID_IMAGEN = DatabaseReference.push().getKey();

                            DatabaseReference.child(ID_IMAGEN).setValue(pelicula);

                            progressDialog.dismiss();
                            Toast.makeText(AgregarPelicula.this, "Subido Exitosamente", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(AgregarPelicula.this,PeliculasA.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AgregarPelicula.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            progressDialog.setTitle("Publicando");
                            progressDialog.setCancelable(false);
                        }
                    });
        }
    }

    private String ObtenerExtensionDelArchivo(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /*
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
                ImagenAgregarPelicula.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            
        }
    }*/

    //SDK 31
    //obtener imagen galeria
    private ActivityResultLauncher<Intent> ObtenerImagenGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //manejar el resultado de nuestro intent
                    if (result.getResultCode() == Activity.RESULT_OK){
                        //seleccion de imagen
                        Intent data = result.getData();
                        //Obtener uri de la imagen
                        RutaArchivoUri = data.getData();
                        ImagenAgregarPelicula.setImageURI(RutaArchivoUri);
                    }else {
                        Toast.makeText(AgregarPelicula.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

}
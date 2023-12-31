package com.desailly.backdropsbliss.DetelleCliente;

import android.Manifest;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.desailly.backdropsbliss.R;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class DetalleImagen extends AppCompatActivity {

    ImageView ImagenDetalle;
    TextView NombreImagenDetalle;
    TextView VistaDetalle;

    FloatingActionButton fabDescargar, fabCompartir, fabEstablecer;

    Bitmap bitmap; // mapa de bits

    private Uri imageUri = null;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_imagen);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Detalle");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImagenDetalle = findViewById(R.id.ImagenDetalle);
        NombreImagenDetalle = findViewById(R.id.NombreImagenDetalle);
        VistaDetalle = findViewById(R.id.VistaDetalle);

        fabDescargar = findViewById(R.id.fabDescargar);
        fabCompartir = findViewById(R.id.fabCompartir);
        fabEstablecer = findViewById(R.id.fabEstablecer);

        dialog = new Dialog(DetalleImagen.this);

        String imagen = getIntent().getStringExtra("Imagen");
        String Nombre = getIntent().getStringExtra("Nombre");
        String Vista = getIntent().getStringExtra("Vista");

        try {
            Picasso.get().load(imagen).placeholder(
                    R.drawable.detalle_imagen).into(ImagenDetalle);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.detalle_imagen).into(ImagenDetalle);
        }

        NombreImagenDetalle.setText(Nombre);
        VistaDetalle.setText(Vista);

        fabDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (ContextCompat.checkSelfPermission(
                            DetalleImagen.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        DescargarImagen_11();
                    } else {
                        SolicitudPermisoDescargaAndroid_11_o_Superior.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(DetalleImagen.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        DescargarImagen();
                    } else {
                        SolicitudPermisoDescarga.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    DescargarImagen();
                }
            }
        });

        fabCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompartirImagen_Actualizado();
            }
        });

        fabEstablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EstablecerImagen();
            }
        });

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void DescargarImagen_11() {
        bitmap = ((BitmapDrawable) ImagenDetalle.getDrawable()).getBitmap();
        OutputStream fos;
        String nombre_imagen = NombreImagenDetalle.getText().toString();
        try {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, nombre_imagen);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "Image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "BackdropsBliss");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Objects.requireNonNull(fos);
            Animacion_Descarga_Exitosa();
            // Toast.makeText(this, "Imagen descargada", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo descargar la imagen" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void DescargarImagen() {
        bitmap = ((BitmapDrawable) ImagenDetalle.getDrawable()).getBitmap();
        String FechaDescarga = new SimpleDateFormat("'Fecha Descarga: ' yyyy_MM_dd 'Hora: ' HH:mm:ss",
                Locale.getDefault()).format(System.currentTimeMillis());
        File ruta = Environment.getExternalStorageDirectory();
        File NombreCarpeta = new File(ruta + "BackdropsBliss");
        NombreCarpeta.mkdir();
        String ObtenerNombreImagen = NombreImagenDetalle.getText().toString();
        String NombreImagen = ObtenerNombreImagen + " " + FechaDescarga + ".JPEG";
        File file = new File(NombreCarpeta, NombreImagen);
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
           Animacion_Descarga_Exitosa();
            // Toast.makeText(this, "La imagen se ha descargado con exito", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void CompartirImagen_Actualizado() {
        Uri contentUri = getContenUri();
        Intent sharedIntent = new Intent(Intent.ACTION_SEND);
        sharedIntent.setType("image/jpeg");
        sharedIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto");
        sharedIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        sharedIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(sharedIntent);
    }

    private void EstablecerImagen(){
        //obtener el mapa de bits
        bitmap = ((BitmapDrawable)ImagenDetalle.getDrawable()).getBitmap();
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        try {
            wallpaperManager.setBitmap(bitmap);
            Animacion_Establecido();
            //Toast.makeText(this, "Establecido con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private Uri getContenUri() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ImagenDetalle.getDrawable();
        bitmap = bitmapDrawable.getBitmap();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            }
        } catch (Exception e) {

        }
        File imageFolder = new File(getCacheDir(), "image");
        Uri contentUri = null;
        try {
            imageFolder.mkdir();
            File file = new File(imageFolder, "shared_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            contentUri = FileProvider.getUriForFile(this, "com.desailly.backdropsbliss.fileprovider", file);
        } catch (Exception e) {

        }
        return contentUri;
    }

    private ActivityResultLauncher<String> SolicitudPermisoDescarga =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    DescargarImagen();
                } else {
                    Toast.makeText(this, "El permiso a sido denegado", Toast.LENGTH_SHORT).show();
                }
            });

    private ActivityResultLauncher<String> SolicitudPermisoDescargaAndroid_11_o_Superior =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    DescargarImagen_11();
                } else {
                    Animacion_Active_Permisos();
                    //Toast.makeText(this, "El permiso a sido denegado", Toast.LENGTH_SHORT).show();
                }
            });

    private void Animacion_Active_Permisos(){
        Button OKPERMISOS;

        //conexion con el cuadro de dialo
        dialog.setContentView(R.layout.animacion_permiso);

        OKPERMISOS = dialog.findViewById(R.id.OKPERMISOS);

        OKPERMISOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }


    private void  Animacion_Descarga_Exitosa(){
        Button OKDESCARGA;
        dialog.setContentView(R.layout.animacion_descarga_exitosa);

        OKDESCARGA = dialog.findViewById(R.id.OKDESCARGA);

        OKDESCARGA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }

    private  void Animacion_Establecido(){
        Button OKESTABLECIDO;
        dialog.setContentView(R.layout.animacion_establecido);
        OKESTABLECIDO = dialog.findViewById(R.id.OKESTABLECIDO);
        OKESTABLECIDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

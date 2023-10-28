package com.desailly.backdropsbliss.CategoriasClienteFirebase;

public class ImgCatFirebaseElegida {
    String imagen;
    String nombre;
    int vistas;


    public ImgCatFirebaseElegida() {
    }

    public ImgCatFirebaseElegida(String imagen, String nombre, int vistas) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.vistas = vistas;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }
}

package com.example.fitsync.data.model;

public class GroupPost {
    private String autorNombre;
    private String grupo;
    private String tiempo;
    private String texto;
    private int likes;
    private int comentarios;
    private int compartidos;

    public GroupPost(String autorNombre, String grupo, String tiempo,
                     String texto, int likes, int comentarios, int compartidos) {
        this.autorNombre  = autorNombre;
        this.grupo        = grupo;
        this.tiempo       = tiempo;
        this.texto        = texto;
        this.likes        = likes;
        this.comentarios  = comentarios;
        this.compartidos  = compartidos;
    }

    public String getAutorNombre()  { return autorNombre; }
    public String getGrupo()        { return grupo; }
    public String getTiempo()       { return tiempo; }
    public String getTexto()        { return texto; }
    public int getLikes()           { return likes; }
    public int getComentarios()     { return comentarios; }
    public int getCompartidos()     { return compartidos; }
}
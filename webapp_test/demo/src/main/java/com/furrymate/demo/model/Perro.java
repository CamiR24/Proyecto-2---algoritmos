package com.furrymate.demo.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

@Node("Perro")
public class Perro {
     @Id @GeneratedValue
    private Long id;

    @Property("nombre")
    private String nombre;

    @Property("raza")
    private String raza;

    @Property("ubicacion")
    private String ubicacion;

    @Property("edad")
    private int edad;

    @Property("peso")
    private int peso;

    @Property("tamaño")
    private String tamaño;

    @Property("color")
    private String color;

    @Property("haTenidoPareja")
    private boolean haTenidoPareja;

    @Property("quedarseCria")
    private boolean quedarseCria;

    @Property("nombreDueno")
    private String nombreDueno;

    @Property("tienePedigree")
    private boolean tienePedigree;

    @Property("sexo")
    private String sexo;

    @Property("tieneEntrenamiento")
    private boolean tieneEntrenamiento;

    @Property("antecedentes")
    private String antecedentes;

    @Property("tieneEnfermedades")
    private boolean tieneEnfermedades;

    @Property("cria")
    private boolean cria;

    public Perro(){
        
    }

    public Perro(String nombre, String raza, String ubicacion, String tamaño, int edad, boolean tienePedigree,
        boolean tieneEntrenamiento, int peso, String sexo, boolean tieneEnfermedades, boolean cria) {
        this.nombre = nombre;
        this.raza = raza;
        this.ubicacion = ubicacion;
        this.tamaño = tamaño;
        this.edad = edad;
        this.tienePedigree = tienePedigree;
        this.tieneEntrenamiento = tieneEntrenamiento;
        this.peso = peso;
        this.sexo = sexo;
        this.tieneEnfermedades = tieneEnfermedades;
        this.cria = cria;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public String getColor() {
        return color;
    }
    public int getEdad() {
        return edad;
    }
    
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreDueno() {
        return nombreDueno;
    }

    public int getPeso() {
        return peso;
    }

    public String getSexo() {
        return sexo;
    }

    public String getRaza() {
        return raza;
    }

    public String getTamaño() {
        return tamaño;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public boolean getHaTenidoPareja() {
        return haTenidoPareja;
    }

    public boolean getQuedarseCria() {
        return quedarseCria;
    }

    public boolean getTienePedigree() {
        return tienePedigree;
    }

    public boolean getTieneEntrenamiento() {
        return tieneEntrenamiento;
    }    

    public boolean getTieneEnfermedades() {
        return tieneEnfermedades;
    }    

    public boolean getCria() {
        return cria;
    }    

    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setHaTenidoPareja(boolean haTenidoPareja) {
        this.haTenidoPareja = haTenidoPareja;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNombreDueno(String nombreDueno) {
        this.nombreDueno = nombreDueno;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public void setQuedarseCria(boolean quedarseCria) {
        this.quedarseCria = quedarseCria;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }

    public void setTieneEntrenamiento(boolean tieneEntrenamiento) {
        this.tieneEntrenamiento = tieneEntrenamiento;
    }

    public void setTienePedigree(boolean tienePedigree) {
        this.tienePedigree = tienePedigree;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setTieneEnfermedades(boolean tieneEnfermedades) {
        this.tieneEnfermedades = tieneEnfermedades;
    }

    public void setParaCria(boolean cria) {
        this.cria = cria;
    }
}



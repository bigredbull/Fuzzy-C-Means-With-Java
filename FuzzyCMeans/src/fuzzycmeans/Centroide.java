/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzycmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Fredy Guerrero
 */
public class Centroide {
    
    private float coordx;
    private float coordy;
    private int codigo;
    private String col;

    public String getCol() {
        return col;
    }

    public void setCol(String color) {
        this.col = color;
    }

    public float getCoordx() {
        return coordx;
    }

    public void setCoordx(float coordx) {
        this.coordx = coordx;
    }

    public float getCoordy() {
        return coordy;
    }

    public void setCoordy(float coordy) {
        this.coordy = coordy;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    public ArrayList<Centroide> generarCentroidesIniciales(int numeroClusters){
        List colores = new ArrayList();
        // generamos una lista con los nombres de los colore que hemos guardado previamente.
        colores.add("AZUL");
        colores.add("NARANJA");
        colores.add("VERDE");
        colores.add("NEGRO");
        colores.add("GRIS CLARO");
        colores.add("AMARILLO");
        colores.add("AZUL CLARO");
        colores.add("ROSADO");
        colores.add("ROJO");
        colores.add("GRIS OSCURO");
        colores.add("GRIS");
        
        ArrayList<Centroide> arregloCentroides = new ArrayList<Centroide>();
        Centroide c;
        Random rand;// variable aleatoria pra generar las posiciones iniciales.
        
        for (int i=0; i<numeroClusters; i++){
            rand = new Random();
            c = new Centroide();
            c.setCodigo(i);
            c.setCoordx(rand.nextFloat());// generación aleatoria de la coordenada x
            c.setCoordy(rand.nextFloat());// generacion aleatoria de la coordenada y
            try{
            c.setCol(colores.get(i).toString());// fijamos al centride i-ésimo el color i-ésimo de la lista
            }catch(Exception e){
            c.setCol(colores.get(0).toString());// cuando se acaban los colores fijamos por defecto el primer color de la lista.
            }
            arregloCentroides.add(c);
        }
        return arregloCentroides;// lista de centroides generados.
    }
    
}

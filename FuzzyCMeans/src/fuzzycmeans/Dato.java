/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzycmeans;

import java.util.List;

/**
 *
 * @author Fredy Guerrero
 */
public class Dato {
    
    private int codigo;
    private float notaMatematicas;
    private float notaSociales;
    private int codigoCluster;
    private List listaPertenenciaOtrosClusters;
    private List listaDistanciaClusters;
    String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List getListaDistanciaClusters() {
        return listaDistanciaClusters;
    }

    public void setListaDistanciaClusters(List listaDistanciaClusters) {
        this.listaDistanciaClusters = listaDistanciaClusters;
    }

    public int getCodigoCluster() {
        return codigoCluster;
    }

    public void setCodigoCluster(int codigoCluster) {
        this.codigoCluster = codigoCluster;
    }

    public List getListaPertenenciaOtrosClusters() {
        return listaPertenenciaOtrosClusters;
    }

    public void setListaPertenenciaOtrosClusters(List listaPertenenciaOtrosClusters) {
        this.listaPertenenciaOtrosClusters = listaPertenenciaOtrosClusters;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public float getNotaMatematicas() {
        return notaMatematicas;
    }

    public void setNotaMatematicas(float notaMatematicas) {
        this.notaMatematicas = notaMatematicas;
    }

    public float getNotaSociales() {
        return notaSociales;
    }

    public void setNotaSociales(float notaSociales) {
        this.notaSociales = notaSociales;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzycmeans;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Fredy Guerrero
 */
public class Dibujo2D extends JPanel{
    
    public void dibujarDato(Graphics g, int x, int y, String color){
        g.setColor(Color.getColor(color));// obtenemos las propiedades del color por el nombre que fijams previamente.
        g.drawRect(x, y, 5, 5);// dibujamos un rectángulo hueco de ancho 5 y alto 5 en las coordenadas x, y
    }
    
    public void dibujarCentroide(Graphics g, int x, int y, String col){// repetimos el proceso anterior pero ahora con los centroides.
        g.setColor(Color.getColor(col));
        g.fillOval(x, y, 7, 7);// dibujamos circulo relleno
    }
}

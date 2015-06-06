/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzycmeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Fredy Guerrero
 */
public class FuzzyCMeans {
    
    public ArrayList<Dato> leerFicheroMapeoArreglo(String nombreArchivo) throws FileNotFoundException, IOException{
        // este método lee un txt y mapea su contenido en una lista de objetos del tipo Dato
        ArrayList<Dato> listaRegistros = new ArrayList<Dato>();
        ArrayList<String> lista;
        Dato registro;
        String linea;
        FileReader archivo = new FileReader(nombreArchivo);// almacenaos el archivo a partir de su ruta
        BufferedReader buffer = new BufferedReader(archivo);// lo cargamos al buffer de lectura
        int i=0;
        // cargamos las primeras lineas que son informativas y no se deben tener en cuenta n el dataset
        buffer.readLine();
        buffer.readLine();
        buffer.readLine();
        buffer.readLine();
        buffer.readLine();
        buffer.readLine();
        buffer.readLine();
        
        while((linea = buffer.readLine())!=null) {// mientras exista información cargamos la siguiente línea
            registro = new Dato();
            
            lista = new ArrayList<String>(Arrays.asList(linea.split(",")));// lo mapeamos a una lista separando por comas
            
            registro.setCodigo(i);// fijamos el código del dato
            registro.setNotaSociales(Float.parseFloat(lista.get(0))/5);// fijamos la "nota de sociales" que corresponde a la coordenada y del dato
            registro.setNotaMatematicas(Float.parseFloat(lista.get(1))/5);// fijamos la coordenada x que corresponde en los datos a la nota de matemáticas
            registro.setColor("ROJO");// por defecto todos los datos son de color rojo.
            
            listaRegistros.add(registro);
            i++;
        }

        buffer.close();
        archivo.close();
        return listaRegistros; // se devuelve la lista de datos ecolectada.        
    }
    
    public ArrayList<Dato> asignarDistanciasAlCuadrado (ArrayList<Centroide> centroides, ArrayList<Dato> datos){
        // este método calcula las distancias al cuadrado entre cada dato y todos los clusters.
        List distancias;
        float cordxDato, cordyDato;
        float cordxCentroide, cordyCentroide;
        
        for(int i=0; i< datos.size(); i++){
            distancias = new ArrayList();
            for(int j=0; j< centroides.size(); j++){
                //matemáticas en x sociales en y
                // extraémos las coordenadas para operar la distancia
                cordxDato = datos.get(i).getNotaMatematicas();
                cordyDato = datos.get(i).getNotaSociales();
                
                cordxCentroide = centroides.get(j).getCoordx();
                cordyCentroide = centroides.get(j).getCoordy();
                
                distancias.add((float) Math.pow(cordxDato-cordxCentroide, 2)  + (float) Math.pow(cordyDato-cordyCentroide, 2)); // se calcula la distancia euclidiana al cuadrado
            }
            datos.get(i).setListaDistanciaClusters(distancias);// se fijan las distancias a cada dato
        }
        return datos;
    }
    
    public ArrayList<Dato> asignarDatosACluster(ArrayList<Dato> datos, int numeroClusters, ArrayList<Centroide> centroides){
        // este método le asigna un dato a un clúster cuando es el más cercano
        for(int i=0; i<datos.size(); i++ ){
            float menor = (float)datos.get(i).getListaDistanciaClusters().get(0);// se fija al menor como el primero
            int codigoMenor = 0;
            for(int j=1; j< numeroClusters; j++){
                if((float)datos.get(i).getListaDistanciaClusters().get(j) < menor){// comparamos si la distancia al siguiente clúster es menor, en ese caso se le asigna como menor
                    menor = (float)datos.get(i).getListaDistanciaClusters().get(j);
                    codigoMenor = j;// se fija el código del menor
                }
            }
            datos.get(i).setCodigoCluster(codigoMenor);// al finalizar se fija el código del centroide más cercno al dato correspondiente
            datos.get(i).setColor(centroides.get(codigoMenor).getCol());// se le asigna al dato el mismo color de su centroide.

        }
        return datos;
    }
    
    public ArrayList<Dato> calcularPertenenciaDatos(ArrayList<Dato> listaDatos, float parametroFuzzy){
        // este método calcula la pertenencia de cada dato a cada clúster.
        float pertenencia;
        int tam;
        float sumaDistancias;
        
        List distancias;
        List listaPertenencia;
        
        for(int i=0; i< listaDatos.size(); i++){ 
            distancias = new ArrayList();
            distancias = listaDatos.get(i).getListaDistanciaClusters();
            tam = distancias.size();
            sumaDistancias = 0;
            
            for(int k=0; k< tam; k++){
                sumaDistancias += (float)Math.pow((1/((float)distancias.get(k))), (1/(parametroFuzzy-1)));// se alcula la suma de las distancias de cada dato a todos los clústers
            }
            listaPertenencia = new ArrayList();
            for(int j=0; j< tam; j++){
                pertenencia = ((float) Math.pow((1/((float)distancias.get(j))), (1/(parametroFuzzy-1))))/sumaDistancias;
                listaPertenencia.add(pertenencia);// se calcula la pertenencia a cada clúster y se asigna el parámetro a cada dato.
            }
            listaDatos.get(i).setListaPertenenciaOtrosClusters(listaPertenencia);// se fija en el dato correspondiente.
        }
        return listaDatos;
    }
    
    public ArrayList<Centroide> calcularCentroides(ArrayList<Centroide> centroides, ArrayList<Dato> datos, float parametroFuzzy){
        // este método recalcula la posición de cada centroide.
        int codigoCentroide = 0;
        float coorxCentroide;
        float cooryCentroide;
        float coorxDato;
        float cooryDato;
        float pertenencia;
        float sumaNumerador;
        float SumaDenominador;
        // cálculo de la coordenada x del centroide
        for(int i=0; i<centroides.size(); i++){
            codigoCentroide = centroides.get(i).getCodigo();
            coorxCentroide = 0;
            sumaNumerador = 0;
            SumaDenominador = 0;
            // cálculo numerador
            for(int j=0; j < datos.size(); j++){
                pertenencia = (float)datos.get(i).getListaPertenenciaOtrosClusters().get(codigoCentroide);
                coorxDato = datos.get(i).getNotaMatematicas();
                sumaNumerador += (float)((Math.pow(pertenencia, parametroFuzzy))*coorxDato);
            }
            // cálculo denominador
            for(int j=0; j < datos.size(); j++){
                pertenencia = (float)datos.get(i).getListaPertenenciaOtrosClusters().get(codigoCentroide);
                SumaDenominador += (float)(Math.pow(pertenencia, parametroFuzzy));
            }
            coorxCentroide = sumaNumerador/SumaDenominador;// cálculo de la coordenada en x del centroide.
            centroides.get(i).setCoordx(coorxCentroide);
        }
        
        // cálculo de la coordenada y
        
        for(int i=0; i<centroides.size(); i++){
            codigoCentroide = centroides.get(i).getCodigo();
            cooryCentroide = 0;
            sumaNumerador = 0;
            SumaDenominador = 0;
            //cálculo de numerador
            for(int j=0; j < datos.size(); j++){
                pertenencia = (float)datos.get(i).getListaPertenenciaOtrosClusters().get(codigoCentroide);
                cooryDato = datos.get(i).getNotaSociales();
                sumaNumerador += (float)((Math.pow(pertenencia, parametroFuzzy))*cooryDato);
            }
            //cálculo denominador
            for(int j=0; j < datos.size(); j++){
                pertenencia = (float)datos.get(i).getListaPertenenciaOtrosClusters().get(codigoCentroide);
                SumaDenominador += (float)(Math.pow(pertenencia, parametroFuzzy));
            }
            cooryCentroide = sumaNumerador/SumaDenominador;
            centroides.get(i).setCoordy(cooryCentroide);
        }
        
        return centroides;
    }
    
    public void generarReporte(ArrayList<Dato> datosReporte, ArrayList<Centroide> clusters, String rutaCreacion) throws IOException{
        // este método genera dos reportes en la ruta especificada. Uno con información de los clústers y otro con la información obtenida de cada dato.
        File reportePuntos = new File(rutaCreacion+" - Información de Puntos.txt");
        BufferedWriter bw;
        
        bw = new BufferedWriter(new FileWriter(reportePuntos));
                bw.write("--------------------------------------------------------------\n"
                        +"//////////////////////////////////////////////////////////////\n"
                        +"////////// REPORTE EJECUCIÓN FUZZY C-MEANS CLUSTERING ////////\n"
                        +"//////////////////////////////////////////////////////////////\n"
                        +"////////// INFORMACIÓN DE LOS ELEMENTOS DEL DATA SET /////////\n"
                        +"//////////////////////////////////////////////////////////////\n"
                        +"--------------------------------------------------------------\n\n");
                
        for(int i=0; i<datosReporte.size(); i++){
                bw.write("Código Estudiante         :    "+datosReporte.get(i).getCodigo()+"\n"
                        +"Nota Matemáticas          :    "+datosReporte.get(i).getNotaMatematicas()*5+"\n"
                        +"Nota Sociales             :    "+datosReporte.get(i).getNotaSociales()*5+"\n"
                        +"Código Cluster + Cercano  :    "+datosReporte.get(i).getCodigoCluster()+"\n"
                        +"______________________________________________________________\n");
            for(int j=0; j< datosReporte.get(i).getListaDistanciaClusters().size(); j++){
                bw.write("Distancia al cluster "+j+"    :    "+datosReporte.get(i).getListaDistanciaClusters().get(j)+"\n"
                        +"______________________________________________________________\n");
            }
            for(int j=0; j< datosReporte.get(i).getListaDistanciaClusters().size(); j++){
                try{
                bw.write("Pertenencia al cluster "+j+"    :    "+datosReporte.get(i).getListaPertenenciaOtrosClusters().get(j)+"\n");                       
                }catch(Exception e){}
            }
                 bw.write("\n//////////////////////////////////////////////////////////////\n"
                        + "--------------------------------------------------------------\n"
                        + "//////////////////////////////////////////////////////////////\n\n");
        }
        bw.close();
        
        File reporteClusters = new File(rutaCreacion+" - Información de Clusters.txt");
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(reporteClusters));
                    bw2.write("--------------------------------------------------------------\n"
                            +"//////////////////////////////////////////////////////////////\n"
                            +"////////// REPORTE EJECUCIÓN FUZZY C-MEANS CLUSTERING ////////\n"
                            +"//////////////////////////////////////////////////////////////\n"
                            +"///////////////// INFORMACIÓN DE LOS CLUSTERS ////////////////\n"
                            +"//////////////////////////////////////////////////////////////\n"
                            +"--------------------------------------------------------------\n\n");
                for(int i=0; i< clusters.size(); i++){
                    bw2.write("Código Cluster         :      "+clusters.get(i).getCodigo()+"\n"
                            +"Coordenada X           :      "+clusters.get(i).getCoordx()+"\n"
                            +"Coordenada Y           :      "+clusters.get(i).getCoordy()+"\n"
                            +"Color                  :      "+clusters.get(i).getCol()+"\n"
                            +"Elementos del Cluster  :      \n");
                    int codigoCluster = clusters.get(i).getCodigo();
                    int numElementos = 0;
                    for(int j=0; j<datosReporte.size(); j++){
                        if(datosReporte.get(j).getCodigoCluster() == codigoCluster){
                    bw2.write("\t\t\t\t\t\tEstudiante  "+datosReporte.get(j).getCodigo()+"\n");
                    numElementos++;
                        }
                    }
                    bw2.write("Número de elementos   :      "+numElementos+"\n");
                    bw2.write("\n//////////////////////////////////////////////////////////////\n"
                            + "--------------------------------------------------------------\n"
                            + "//////////////////////////////////////////////////////////////\n\n");
                }
                bw2.close();
    }
}

package com.compilador.analizadorSintactico;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class AnalizadorSintactico {

    public static Vector<String> listaAnalisis = new Vector<>();

    public static Vector<String> listaErroresSintacticos = new Vector<>();

    public static void agregarAnalisis(String analisis){
        listaAnalisis.add(analisis);
    }

    public static void agregarError(String error){
        listaErroresSintacticos.add(error);

    }

    public static Parser p = new Parser();

    public static void main(String[] args) {

        try{
            System.out.println("Analisis de Archivo (Lexico + Sintactico):");
            System.out.println("Ingrese la ruta del archivo:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
            String entrada = Files.readString(Paths.get(path));


            entrada = entrada.replaceAll("\r\n", "\n");
            AnalizadorLexico.archivo = entrada + "$";

            if (p.yyparse() == 0){
                System.out.println("Parser finalizo");
                listaAnalisis.forEach((data)->{
                    System.out.println(data);
                });
                HashMap<Integer, TDSObject> data = AnalizadorLexico.tablaDeSimbolos;
                data.forEach((k,v)->{
                    System.out.println("RefID: " + k + "Data: " + v.imprimir());
                });
            }else{
                System.out.println("Parser no finalizo");
            }
        }catch(IOException e) {}



    }







}

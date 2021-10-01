package com.compilador.analizadorSintactico;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
            String auxFile = Paths.get(path).getFileName().toString();
            int indexDot = auxFile.lastIndexOf(".");
            int indexPath = path.lastIndexOf('\\');
            if(indexPath == -1){
                indexPath = auxFile.lastIndexOf("/");
            }
            String originalPath = path.substring(0,indexPath +1);
            String fileName = auxFile.substring(0,indexDot);

            entrada = entrada.replaceAll("\r\n", "\n");
            AnalizadorLexico.archivo = entrada + "$";

            if (p.yyparse() == 0){
                System.out.println("Parser finalizo");
            }else{
                System.out.println("Parser no finalizo");
            }

            String archivoAlName =  originalPath + fileName + "_analisisLexico.txt";
            Path file = Paths.get(archivoAlName);
            Files.write(file,AnalizadorLexico.salidaTokens, StandardCharsets.UTF_8);

            String archivoLAName = originalPath + fileName + "_analisisSintactico.txt";
            Path file2 = Paths.get(archivoLAName);
            Files.write(file2,listaAnalisis, StandardCharsets.UTF_8);

            String archivoTDSName = originalPath + fileName + "_tablaDeSimbolos.txt";
            Path file3 = Paths.get(archivoTDSName);
            Vector<String> tdsData = new Vector<>();
            HashMap<String, TDSObject> data = AnalizadorLexico.tablaDeSimbolos;
            data.forEach((k,v)->{
                tdsData.add("Lexema: " + k + " | " + v.imprimir());
            });
            Files.write(file3, tdsData, StandardCharsets.UTF_8);

        }catch(IOException e) {}



    }







}

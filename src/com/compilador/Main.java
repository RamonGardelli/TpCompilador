package com.compilador;
import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class Main {

    public static void main(String[] args) {

        try {
            System.out.println("----------ANALIZADOR LÉXICO-----------");
            System.out.println("Ingrese la ruta del archivo a analizar:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
            String entrada = Files.readString(Paths.get(path));

            //C:\Users\ramon\Documents\TpCompilador\archivos\validos\literales.txt
            entrada = entrada.replaceAll("\r\n", "\n");
            AnalizadorLexico.archivo = entrada + "$";
            while (AnalizadorLexico.finArchivo == false)
                AnalizadorLexico.yylex();
            HashMap<Integer,TDSObject> data = AnalizadorLexico.tablaDeSimbolos;
            if (data.isEmpty()) {
                System.out.println("----------------");
                System.out.println("No se detectaron tokens.");
            }
            data.forEach((k,v)->{
                System.out.println("RefID: " + k + "Data: " + v.imprimir());
            });
            Vector<String> errores= AnalizadorLexico.listaDeErrores;
            Vector<String> warn= AnalizadorLexico.listaDeWarnings;

            errores.forEach((err) ->{
                System.out.println(err);
            });
            warn.forEach((err) ->{
                System.out.println(err);
            });


        } catch(IOException e) {}

    }
}

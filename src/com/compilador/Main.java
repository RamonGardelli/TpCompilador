package com.compilador;
import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;
import com.compilador.analizadorLexico.Token;

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

            //C:\Users\ramon\Documents\TpCompilador\archivos\flotante.txt
            AnalizadorLexico.archivo = entrada + "$";
            System.out.println(AnalizadorLexico.archivo);
            System.out.println(AnalizadorLexico.archivo.length());
            while (AnalizadorLexico.finArchivo == false)
                AnalizadorLexico.yylex();
            //Vector<Token> tokens = AnalizadorLexico.tokenList;
            HashMap<Integer,TDSObject> data = AnalizadorLexico.tablaDeSimbolos;
            if (data.isEmpty()) {
                System.out.println("----------------");
                System.out.println("No se detectaron tokens.");
            }
            data.forEach((k,v)->{
                System.out.println("RefID: " + k + "Data: " + v.imprimir());
            });
            /*for (Token token : tokens) {
                System.out.println("----------------");
                System.out.println("Token: " + token.getTokenId());
                System.out.println("Lexema: " + token.getReading());
                System.out.println("Linea: " + token.getNumLine());
            }*/

        } catch(IOException e) {}

    }
}

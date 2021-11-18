package com.compilador.analizadorSintactico;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;
import com.compilador.arbolSintactico.Nodo;

import java.io.*;
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

    public static Parser p = new Parser();
    
    public static Nodo arbol;
    
    public static Nodo arbolFunc;

    public static String ambitoActual = "@main";

    public static String tipoActual = "";

    public static void agregarAnalisis(String analisis){
        listaAnalisis.add(analisis);
    }

    public static void agregarError(String error){
        listaErroresSintacticos.add(error);
    }

    public static String getReferenciaPorAmbito(String input){
        String fullKey = input + ambitoActual;
        while(!fullKey.equals(input)){
            if(!AnalizadorLexico.tablaDeSimbolos.containsKey(fullKey)){
                fullKey = fullKey.substring(0,fullKey.lastIndexOf("@"));
            }
            else
                break;
        }
        if(fullKey.equals(input))
            return null;

        return fullKey;
    }

    public static boolean esVariableRedeclarada(String input) {
        TDSObject value = AnalizadorLexico.getLexemaObject(input);
        if( value != null) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        try{
            System.out.println("Analisis de Archivo (Lexico + Sintactico):");
            System.out.println("Ingrese la ruta del archivo:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
           //String entrada = Files.readString(Paths.get(path));
            BufferedReader br = new BufferedReader(new FileReader(path));

            String linea;
            StringBuilder c = new StringBuilder();
            while ((linea = br.readLine()) != null) {
                c.append(linea).append("\n");
            }

            String entrada = c + "$";

            String auxFile = Paths.get(path).getFileName().toString();
            int indexDot = auxFile.lastIndexOf(".");
            int indexPath = path.lastIndexOf('\\');
            if(indexPath == -1){
                indexPath = auxFile.lastIndexOf("/");
            }
            String originalPath = path.substring(0,indexPath +1);
            String fileName = auxFile.substring(0,indexDot);

            entrada = entrada.replaceAll("\r\n", "\n");
            AnalizadorLexico.archivo = entrada;

            if (p.yyparse() == 0){
                System.out.println("Parser finalizo");
                
            }else{
                System.out.println("Parser no finalizo");
            }

            String archivoAlName =  originalPath + fileName + "_analisisLexico.txt";
            Path file = Paths.get(archivoAlName);
            Files.write(file,AnalizadorLexico.salidaTokens, StandardCharsets.UTF_8);

            if(AnalizadorLexico.listaDeErrores.size() != 0){
                String archivoAlErrorName =  originalPath + fileName + "_ErroresLexicos.txt";
                Path fileError = Paths.get(archivoAlErrorName);
                Files.write(fileError,AnalizadorLexico.listaDeErrores , StandardCharsets.UTF_8);
            }

            if(AnalizadorLexico.listaDeWarnings.size() != 0 ){
                String archivoAlWarningName =  originalPath + fileName + "_WarnLexicos.txt";
                Path fileWarning = Paths.get(archivoAlWarningName);
                Files.write(fileWarning,AnalizadorLexico.listaDeWarnings , StandardCharsets.UTF_8);
            }

            String archivoLAName = originalPath + fileName + "_analisisSintactico.txt";
            Path file2 = Paths.get(archivoLAName);
            Files.write(file2,listaAnalisis, StandardCharsets.UTF_8);

            if(listaErroresSintacticos.size() !=0){
                String archivoASerror = originalPath + fileName + "ErroresSintacticos.txt";
                Path fileErrorSint = Paths.get(archivoASerror);
                Files.write(fileErrorSint,listaErroresSintacticos, StandardCharsets.UTF_8);
            }
            imprimirArbol(arbol);
            imprimirArbol(arbolFunc);
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
    
    public static void imprimirArbol(Nodo n) {
    	if (n != null)
    	{
    		imprimirArbol(n.getLeft());
    		System.out.printf(n.getRef());
    		imprimirArbol(n.getRight());
    	}
    }


}

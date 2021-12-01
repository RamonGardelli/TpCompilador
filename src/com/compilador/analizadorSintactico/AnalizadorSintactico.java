package com.compilador.analizadorSintactico;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;
import com.compilador.arbolSintactico.Nodo;
import com.compilador.assembler.Registro;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class AnalizadorSintactico {

    public static String codigoAssembler = " ";

    public static ArrayList<String> listaAnalisis = new ArrayList<>();

    public static ArrayList<String> listaErroresSintacticos = new ArrayList<>();

    public static Parser p = new Parser();

    public static Nodo arbol;

    public static Nodo arbolFunc;

    public static String ambitoActual = "@main";

    public static String tipoActual = "";

    public static boolean errorPrograma = false;

    public static void agregarAnalisis(String analisis) {
        listaAnalisis.add(analisis);
    }

    public static void agregarError(String error) {
        listaErroresSintacticos.add(error);
    }

    public static String getReferenciaPorAmbito(String input) {
        String fullKey = input + ambitoActual;
        while (!fullKey.equals(input)) {
            if (!AnalizadorLexico.tablaDeSimbolos.containsKey(fullKey)) {
                fullKey = fullKey.substring(0, fullKey.lastIndexOf("@"));
            } else
                break;
        }
        if (fullKey.equals(input))
            return null;


        return fullKey;
    }

    public static boolean esVariableRedeclarada(String input) {
        TDSObject value = AnalizadorLexico.getLexemaObject(input);
        if (value != null) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        try {
            if (args.length != 0 || true) {

                //File filex = new File(args[0]);
                File filex = new File("C:\\Users\\ramon\\IdeaProjects\\TpCompilador\\archivos\\programa\\testprograma.txt");

                String originalPath = filex.getAbsoluteFile().getParent() + File.separator;
                String fileName = filex.getName().split("\\.")[0];
                String dataFile = new String(Files.readAllBytes(Paths.get(filex.getAbsolutePath())));

                String entrada = dataFile + "$";

                entrada = entrada.replaceAll("\r\n", "\n");
                AnalizadorLexico.archivo = entrada;

                if (p.yyparse() == 0) {
                    System.out.println("Parser finalizo");

                } else {
                    System.out.println("Parser no finalizo");
                }

                String archivoAlName = originalPath + fileName + "_analisisLexico.txt";
                Path file = Paths.get(archivoAlName);
                Files.write(file, AnalizadorLexico.salidaTokens, StandardCharsets.UTF_8);

                if (AnalizadorLexico.listaDeErrores.size() == 0) {
                    AnalizadorLexico.listaDeErrores.add("No se han detectado errores lexicos.");
                }else{
                    errorPrograma = true;
                }
                String archivoAlErrorName = originalPath + fileName + "_ErroresLexicos.txt";
                Path fileError = Paths.get(archivoAlErrorName);
                Files.write(fileError, AnalizadorLexico.listaDeErrores, StandardCharsets.UTF_8);


                if (AnalizadorLexico.listaDeWarnings.size() == 0) {
                    AnalizadorLexico.listaDeWarnings.add("No se han detectado warnings lexicos.");
                }
                String archivoAlWarningName = originalPath + fileName + "_WarnLexicos.txt";
                Path fileWarning = Paths.get(archivoAlWarningName);
                Files.write(fileWarning, AnalizadorLexico.listaDeWarnings, StandardCharsets.UTF_8);


                String archivoLAName = originalPath + fileName + "_analisisSintactico.txt";
                Path file2 = Paths.get(archivoLAName);
                Files.write(file2, listaAnalisis, StandardCharsets.UTF_8);

                if (listaErroresSintacticos.size() == 0) {
                    listaErroresSintacticos.add("No se han detectado errores sintacticos.");
                }else{
                    errorPrograma = true;
                }
                String archivoASerror = originalPath + fileName + "ErroresSintacticos.txt";
                Path fileErrorSint = Paths.get(archivoASerror);
                Files.write(fileErrorSint, listaErroresSintacticos, StandardCharsets.UTF_8);


                String archivoTDSName = originalPath + fileName + "_tablaDeSimbolos.txt";
                Path file3 = Paths.get(archivoTDSName);
                ArrayList<String> tdsData = new ArrayList<>();
                HashMap<String, TDSObject> data = AnalizadorLexico.tablaDeSimbolos;
                data.forEach((k, v) -> {
                    tdsData.add("Lexema: " + k + " | " + v.imprimir());
                });
                Files.write(file3, tdsData, StandardCharsets.UTF_8);

                if (!errorPrograma) {
                    imprimirArbol(arbol);
                    imprimirArbol(arbolFunc);
                    Registro r1 = new Registro("EAX");
                    Registro r2 = new Registro("EBX");
                    Registro r3 = new Registro("ECX");
                    Registro r4 = new Registro("EDX");

                    String codigo = "";
                    Registro[] r = {r1, r2, r3, r4};
                    arbol.generarCodigo(r);
                    System.out.println(codigoAssembler);
                }else{
                    System.out.println("Error de tipo lexico/sintactico detectado, abortando generacion de codigo.");
                }
            } else {
                System.out.println("Error al cargar archivo, revisa ruta e intenta nuevamente.");
            }
        } catch (IOException e) {
            System.out.println("Error en el programa, abortando.");
        }

    }

    public static void imprimirArbol(Nodo n) {
        if (n != null) {
            imprimirArbol(n.getLeft());
            System.out.printf(n.getRef());
            imprimirArbol(n.getRight());
        }
    }


}

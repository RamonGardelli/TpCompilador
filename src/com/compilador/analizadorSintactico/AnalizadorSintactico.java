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
import java.util.*;

public class AnalizadorSintactico {

    public static StringBuilder codigoAssembler = new StringBuilder();
    
    public static StringBuilder codigoAssemblerFinal = new StringBuilder();


    public static ArrayList<String> listaAnalisis = new ArrayList<>();

    public static ArrayList<String> listaErroresSintacticos = new ArrayList<>();

    public static Stack<String> pilaRegistros= new Stack<String>();

    public static Stack<String> pilaLabels = new Stack<String>();

    public static int contadorLabel=0;

    public static int contadorAuxLong=0;
    
    public static int contadorAuxSingle=0;
    
    public static Parser p = new Parser();

    public static Nodo arbol;

    public static Nodo arbolFunc;

    public static String ambitoActual = "@main";

    public static String tipoActual = "";

    public static boolean errorPrograma = false;
        
    public static HashMap<String, Integer> flagsFunc = new HashMap<String, Integer>();

    public static HashMap<String, Integer> idCadenas = new HashMap<String, Integer>();

    public static int contadorCadenas=1;

    public static int contadorFunc=1;
    
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
            	File filex = new File("C:\\Users\\Admin\\Desktop\\prueba\\prueba.txt");
                //File filex = new File(args[0]);
            	//File filex = new File("C:\\Users\\ramon\\IdeaProjects\\TpCompilador\\archivos\\programa\\testprograma.txt");


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
                String archivoASerror = originalPath + fileName + "_ErroresSintacticos.txt";
                Path fileErrorSint = Paths.get(archivoASerror);
                Files.write(fileErrorSint, listaErroresSintacticos, StandardCharsets.UTF_8);


                String archivoTDSName = originalPath + fileName + "_tablaDeSimbolos.txt";
                Path file3 = Paths.get(archivoTDSName);
                ArrayList<String> tdsData = new ArrayList<>();
                HashMap<String, TDSObject> data = AnalizadorLexico.tablaDeSimbolos;
                data.forEach((k, v) -> {
                    tdsData.add("Lexema: " + k + " | " + v.toString());
                });
                Files.write(file3, tdsData, StandardCharsets.UTF_8);

                if (!errorPrograma) {        	
                	try {
                		obtenerFunciones();
                        toString(arbol);
                    	//toString(arbolFunc);
                        System.out.println(" ");
                        Registro r1 = new Registro("EAX");
                        Registro r2 = new Registro("EBX");
                        Registro r3 = new Registro("ECX");
                        Registro r4 = new Registro("EDX");
                        Registro[] r = {r1, r2, r3, r4};
                        codigoAssembler.append("\n");
                        creacionAssembler();
                        memoriaPrograma();
                        arbol.generarCodigo(r);
                        imprimirVariablesAuxiliares();
                        StringBuilder codigoprograma = new StringBuilder(codigoAssembler);
                        codigoAssembler.setLength(0);
                        codigoAssemblerFinal.append("\n");
                        codigoAssemblerFinal.append(".code");
                        codigoAssemblerFinal.append("\n");
                        arbolFunciones(arbolFunc, r);
                        codigoAssemblerFinal.append(codigoAssembler);
                        codigoAssembler.setLength(0);
                        codigoAssemblerFinal.append("\n");
                        codigoAssemblerFinal.append("start:");
                        codigoAssemblerFinal.append("\n");
                        codigoAssemblerFinal.append(codigoprograma);
                        codigoAssemblerFinal.append("\n");
                        codigoAssemblerFinal.append("JMP @LABEL_END");
                        codigoAssemblerFinal.append("\n");
                        labels();
                        codigoAssemblerFinal.append("invoke ExitProcess, 0");
                        codigoAssemblerFinal.append("\n");
                        codigoAssemblerFinal.append("end start");
                        System.out.println(codigoAssemblerFinal);
                        String archivoasm = originalPath + fileName + "_assembly.asm";
                        Path fileasm = Paths.get(archivoasm);
                        ArrayList<String> auxasm = new ArrayList<>();
                        auxasm.add(codigoAssemblerFinal.toString());
                        Files.write(fileasm, auxasm, StandardCharsets.UTF_8);
                	}
                	catch(IOException e) {
                        System.out.println("Error al generar el codigo assembler.");
                	}          	
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

    public static void toString(Nodo n) {
        if (n != null) {
            toString(n.getLeft());
            if(n.EsHoja()){
                System.out.println("        "+n.getRef());
            }else {
                if(n.getRef() == "/" || n.getRef() == "*" || n.getRef() == "+" || n.getRef() == "-" || n.getRef().contains(">")  || n.getRef().contains("<")  ||
                        n.getRef().contains("=") || n.getRef().contains("|") || n.getRef().contains("&") || n.getRef().contains("LF") || n.getRef().contains("PRINT"))
                    System.out.println("    "+n.getRef());
                else
                    System.out.println(n.getRef());
            }
            toString(n.getRight());
        }
    }

    public static void creacionAssembler() {
    	codigoAssemblerFinal.append(".386");
    	codigoAssemblerFinal.append("\n");
    	codigoAssemblerFinal.append(".model flat, stdcall");
        codigoAssemblerFinal.append("\n");
    	codigoAssemblerFinal.append("option casemap :none");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("include \\masm32\\include\\windows.inc");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("include \\masm32\\include\\kernel32.inc");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("include \\masm32\\include\\user32.inc");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("includelib \\masm32\\lib\\kernel32.lib");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("includelib \\masm32\\lib\\user32.lib");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("include \\masm32\\include\\masm32rt.inc");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("dll_dllcrt0 PROTO C");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("printf PROTO C :VARARG");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("\n");
    	codigoAssemblerFinal.append(".data");
        codigoAssemblerFinal.append("\n");
    }
    
    public static void arbolFunciones(Nodo arbol, Registro r[]) {
    	if(arbol!=null) {
	    	if (arbol.getLeft()!=null) {
	    		codigoAssemblerFinal.append(arbol.getLeft().getRef()).append(":\n");
	    		int aux = AnalizadorSintactico.flagsFunc.get(arbol.getLeft().getRef());
	    		AnalizadorSintactico.codigoAssembler.append("CMP _funcFlag_").append(aux).append(", 1");
	    		AnalizadorSintactico.codigoAssembler.append("\n");
	    		AnalizadorSintactico.codigoAssembler.append("JE @LABEL_RECURSION");
	    		AnalizadorSintactico.codigoAssembler.append("\n");
	    		AnalizadorSintactico.codigoAssembler.append("MOV _funcFlag_").append(aux).append(", 1");
	    		AnalizadorSintactico.codigoAssembler.append("\n");
	        	arbol.getLeft().generarCodigo(r);
	    		AnalizadorSintactico.codigoAssembler.append("MOV _funcFlag_").append(aux).append(", 0\n");
	    		codigoAssembler.append("ret");
	    		codigoAssembler.append("\n");
	    	}
	    	if (arbol.getRight()!=null) {
	        	arbolFunciones(arbol.getRight(), r);
	    	}
    	}	
    }
    
    public static void memoriaPrograma() {
    	 codigoAssemblerFinal.append("msj_division_cero db \" ERROR, el divisor es igual a cero. No se puede proceder con la operacion \",0\n");
         codigoAssemblerFinal.append("msj_overflow_producto db \"ERROR, se detecto overflow. No se puede proceder con la operacion\",0\n");
         codigoAssemblerFinal.append("msj_recursion db \"ERROR, se detecto una recursion. No se puede proceder con la operacion\",0\n");
         codigoAssemblerFinal.append("aux_mem_2bytes dw ?\n");
         
         AnalizadorLexico.tablaDeSimbolos.entrySet().forEach(entry->{
 			if (entry.getValue().getTipoVariable()=="CADENA") {
 				String aux = entry.getKey().replace(" ", "_");
 				aux = aux.replace("	", "_");
                 if(aux.charAt(0) != '_'){
                     aux = "_"+ aux;
                 }
                 idCadenas.put(aux,contadorCadenas);

 	 			codigoAssemblerFinal.append("cad_").append(contadorCadenas).append(" DB ").append("\"").append(entry.getKey()).append("\",0\n");
                  contadorCadenas++;
				}
 			else {
 			String auxValor=entry.getKey();
 			auxValor=auxValor.replace(".", "_");
            auxValor=auxValor.replace("-", "_");
 			codigoAssemblerFinal.append("_").append(auxValor);
	 			if (entry.getValue().getTipoVariable()=="ID") {
	 				if(entry.getValue().getTipoContenido()=="LONG") {
	 					codigoAssemblerFinal.append(" DD ? \n");
	 				}
	 				else {
	 					codigoAssemblerFinal.append(" DQ ? \n");
	 				}
	 			}
	 			else if (entry.getValue().getTipoVariable()=="SINGLE") {
	 				String aux = entry.getKey();
	 				if (entry.getKey().charAt(0) == '_')
	                    aux = "0" + aux;
	 				codigoAssemblerFinal.append(" DQ ").append(aux).append("\n");
	 			}
		 			else if (entry.getValue().getTipoVariable()=="LONG") {
		 				codigoAssemblerFinal.append(" DD ").append(entry.getKey()).append("\n");
		 			}
 			}
	 		}); 
         	flagsFunc.entrySet().forEach(entry->{
     			codigoAssemblerFinal.append("_funcFlag_").append(entry.getValue());
				codigoAssemblerFinal.append(" DB 0 \n");
 	 		}); 
         	
        	flagsFunc.entrySet().forEach(entry->{
     			codigoAssemblerFinal.append("_retFunc_").append(entry.getValue());
     			if (AnalizadorLexico.tablaDeSimbolos.get(entry.getKey()).getTipoContenido()=="LONG")
     				codigoAssemblerFinal.append(" DD ? \n");
     			else
     				codigoAssemblerFinal.append(" DQ ? \n");
 	 		}); 

	}

    public static void imprimirVariablesAuxiliares(){
        for (int k=1; k<=AnalizadorSintactico.contadorAuxLong;k++) {
            codigoAssemblerFinal.append("@auxLong").append(k).append(" DD ? \n");
        }
        for (int k=1; k<=AnalizadorSintactico.contadorAuxSingle;k++) {
            codigoAssemblerFinal.append("@auxSingle").append(k).append(" DQ ? \n");
        }
    }
    
    public static void labels () {
    	codigoAssemblerFinal.append("@LABEL_OVF:");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("invoke MessageBox, NULL, addr msj_overflow_producto, addr msj_overflow_producto, MB_OK");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("JMP @LABEL_END");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("@LABEL_RECURSION:");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("invoke MessageBox, NULL, addr msj_recursion, addr msj_recursion, MB_OK");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("JMP @LABEL_END");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("@LABEL_DIVCERO:");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("invoke MessageBox, NULL, addr msj_division_cero, addr msj_division_cero, MB_OK");
        codigoAssemblerFinal.append("\n");
        codigoAssemblerFinal.append("@LABEL_END:");
        codigoAssemblerFinal.append("\n");
    }
    
    public static void obtenerFunciones() {
        AnalizadorLexico.tablaDeSimbolos.entrySet().forEach((entry)->{
  			if (entry.getValue().esFuncion()) {
  				flagsFunc.put(entry.getKey(), contadorFunc);
  				contadorFunc++;
  			}
 	 		}); 
 	}
}

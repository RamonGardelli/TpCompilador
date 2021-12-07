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

    public static String codigoAssembler = "";
    
    public static String codigoAssemblerFinal = "";
    
    public static String variablesCodigoAssembler = " ";

    public static ArrayList<String> listaAnalisis = new ArrayList<>();

    public static ArrayList<String> listaErroresSintacticos = new ArrayList<>();

    public static Stack<String> pilaRegistros= new Stack<String>();

    public static Stack<String> pilaLabels = new Stack<String>();

    public static int contadorLabel=0;

    public static Parser p = new Parser();

    public static Nodo arbol;

    public static Nodo arbolFunc;

    public static String ambitoActual = "@main";

    public static String tipoActual = "";

    public static boolean errorPrograma = false;
        
    public static HashMap<String, Integer> flagsFunc = new HashMap<String, Integer>();
    
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
            	//File filex = new File("C:\\Users\\Admin\\Desktop\\prueba\\prueba.txt");
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
                String archivoASerror = originalPath + fileName + "_ErroresSintacticos.txt";
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
                	obtenerFunciones();
                    //imprimirArbol(arbol);
                	imprimirArbol(arbolFunc);
                    System.out.println(" ");
                    Registro r1 = new Registro("EAX");
                    Registro r2 = new Registro("EBX");
                    Registro r3 = new Registro("ECX");
                    Registro r4 = new Registro("EDX");

                    Registro[] r = {r1, r2, r3, r4};
                    codigoAssembler+="\n";
                    creacionAssembler();
                    memoriaPrograma();
                    codigoAssemblerFinal+=variablesCodigoAssembler;
                    codigoAssemblerFinal+="\n";
                    codigoAssemblerFinal+=(".code");
                    codigoAssemblerFinal+="\n";
                    arbolFunciones(arbolFunc, r);
                    codigoAssemblerFinal+=codigoAssembler;
                    codigoAssembler="";
                    codigoAssemblerFinal+=("start:");
                    codigoAssemblerFinal+="\n";
                    arbol.generarCodigo(r);
                    codigoAssemblerFinal+=codigoAssembler;
                    codigoAssemblerFinal+="JMP @LABEL_END";
                    codigoAssemblerFinal+="\n";
                    labels();
                    codigoAssemblerFinal+="invoke ExitProcess, 0";
                    codigoAssemblerFinal+="\n";
                    codigoAssemblerFinal+="end start";
                    System.out.println(codigoAssemblerFinal);

                    String archivoasm = originalPath + fileName + "_assembly.asm";
                    Path fileasm = Paths.get(archivoasm);
                    ArrayList<String> auxasm = new ArrayList<>();
                    auxasm.add(codigoAssemblerFinal);
                    Files.write(fileasm, auxasm, StandardCharsets.UTF_8);
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

    public static void creacionAssembler() {
    	codigoAssemblerFinal+=(".386");
    	codigoAssemblerFinal+="\n";
    	codigoAssemblerFinal+=(".model flat, stdcall");
    	codigoAssemblerFinal+="\n";
    	codigoAssemblerFinal+=("option casemap :none");
    	codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+=("include \\masm32\\include\\windows.inc");
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+=("include \\masm32\\include\\kernel32.inc");
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+=("include \\masm32\\include\\user32.inc");
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+=("includelib \\masm32\\lib\\kernel32.lib");
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+=("includelib \\masm32\\lib\\user32.lib");
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+="\n";
    	codigoAssemblerFinal+=(".data");
        codigoAssemblerFinal+="\n";
    }
    
    public static void arbolFunciones(Nodo arbol, Registro r[]) {
    	if(arbol!=null) {
	    	if (arbol.getLeft()!=null) {
	    		codigoAssemblerFinal+=(arbol.getLeft().getRef()+":");
	        	arbol.getLeft().generarCodigo(r);
	    		codigoAssembler+=("ret");
                codigoAssembler+="\n";
	    		codigoAssembler+="\n";
	    	}
	    	if (arbol.getRight()!=null) {
	        	arbolFunciones(arbol.getRight(), r);
	    	}
    	}	
    }
    
    public static void memoriaPrograma() {
    	 codigoAssemblerFinal+="msj_division_cero db \" ERROR, el divisor es igual a cero. No se puede proceder con la operacion \",0\n";
         codigoAssemblerFinal+="msj_overflow_producto db \"ERROR, se detecto overflow. No se puede proceder con la operacion\",0\n";
         codigoAssemblerFinal+="msj_recursion db \"ERROR, se detecto una recursion. No se puede proceder con la operacion\",0\n";
         codigoAssemblerFinal+="aux_mem_2bytes dw ?\n";
         
         AnalizadorLexico.tablaDeSimbolos.entrySet().forEach(entry->{
 			if (entry.getValue().getTipoVariable()=="CADENA") {
 				String aux = entry.getKey().replace(" ", "_");
 				aux = aux.replace("	", "_");
                 if(aux.charAt(0) != '_'){
                     aux = "_"+ aux;
                 }
 	 			codigoAssemblerFinal+=(aux+" DB "+"\""+entry.getKey()+"\",0"+"\n");
				}
 			else {
 			String auxValor=entry.getKey();
 			auxValor=auxValor.replace(".", "_");
             auxValor=auxValor.replace("-", "_");
 			codigoAssemblerFinal+=("_"+auxValor);  
	 			if (entry.getValue().getTipoVariable()=="ID") {
	 				if(entry.getValue().getTipoContenido()=="LONG") {
	 					codigoAssemblerFinal+=" DD ? \n";
	 				}
	 				else {
	 					codigoAssemblerFinal+=" DQ ? \n";
	 				}
	 			}
	 			else if (entry.getValue().getTipoVariable()=="SINGLE") {
	 				String aux = entry.getKey();
	 				if (entry.getKey().charAt(0) == '_')
	                    aux = "0" + aux;
	 				codigoAssemblerFinal+=" DQ "+aux+"\n";
	 			}
		 			else if (entry.getValue().getTipoVariable()=="LONG") {
		 				codigoAssemblerFinal+=" DD "+entry.getKey()+"\n";
		 			}
 			}
	 		}); 
         	flagsFunc.entrySet().forEach(entry->{
     			codigoAssemblerFinal+=("_funcFlag_"+entry.getValue());  
				codigoAssemblerFinal+=" DB 0 \n";
 	 		}); 
         	
        	flagsFunc.entrySet().forEach(entry->{
     			codigoAssemblerFinal+=("_retFunc_"+entry.getValue());  
     			if (AnalizadorLexico.tablaDeSimbolos.get(entry.getKey()).getTipoContenido()=="LONG")
     				codigoAssemblerFinal+=" DD ? \n";
     			else
     				codigoAssemblerFinal+=" DQ ? \n";
 	 		}); 
	}
    
    public static void labels () {
    	codigoAssemblerFinal+="@LABEL_OVF:";
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+="invoke MessageBox, NULL, addr msj_overflow_producto, addr msj_overflow_producto, MB_OK";
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+="JMP @LABEL_END";
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+="@LABEL_RECURSION:";
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+="invoke MessageBox, NULL, addr msj_recursion, addr msj_recursion, MB_OK";
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+="JMP @LABEL_END";
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+="@LABEL_DIVCERO:";
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+="invoke MessageBox, NULL, addr msj_division_cero, addr msj_division_cero, MB_OK";
        codigoAssemblerFinal+="\n";
        codigoAssemblerFinal+="@LABEL_END:";
        codigoAssemblerFinal+="\n";	
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

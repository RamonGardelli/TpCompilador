package com.compilador.analizadorLexico;

import com.compilador.analizadorLexico.accionSemantica.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AnalizadorLexico {

    public static String archivo;
    public static String reading = "";
    public static int indexArchivo = 0;
    public static int token = -1;
    public static String refTDS = "";
    public static int estado = 0;
    public static int numLinea = 1;
    public static int indexTDS = 0;
    public static boolean finArchivo =false;

    public static HashMap<String, TDSObject> tablaDeSimbolos = new HashMap<>();
    public static Vector<String> listaDeErrores = new Vector<>();
    public static Vector<String> listaDeWarnings = new Vector<>();

    public static Vector<String> salidaTokens = new Vector<>();

    public static final int MAX_ID_VALUE = 22;
    public static final int MAX_TOKEN_ID = 283;

    private static AccionSemantica AS1 = new AS1();
    private static AccionSemantica AS2 = new AS2();
    private static AccionSemantica AS3 = new AS3();
    private static AccionSemantica AS4 = new AS4();
    private static AccionSemantica AS5 = new AS5();
    private static AccionSemantica AS6 = new AS6();
    private static AccionSemantica AS7 = new AS7();
    private static AccionSemantica AS8 = new AS8();
    private static AccionSemantica AS9 = new AS9();
    private static AccionSemantica AS10 = new AS10();
    private static AccionSemantica AS11 = new AS11();
    private static AccionSemantica AS12 = new AS12();
    private static AccionSemantica AS13 = new AS13();
    private static AccionSemantica AS14 = new AS14();


    public static Map<Integer, String> listaDeTokens;
    static {
        listaDeTokens = new HashMap<>();
        listaDeTokens.put(257, "ELSE");
        listaDeTokens.put(258, "THEN");
        listaDeTokens.put(259, "ENDIF");
        listaDeTokens.put(260, "PRINT");
        listaDeTokens.put(261, "FUNC");
        listaDeTokens.put(262, "RETURN");
        listaDeTokens.put(263, "WHILE");
        listaDeTokens.put(264, "LONG");
        listaDeTokens.put(265, "BEGIN");
        listaDeTokens.put(266, "END");
        listaDeTokens.put(267, "SINGLE");
        listaDeTokens.put(268, "BREAK");
        listaDeTokens.put(269, "DO");
        listaDeTokens.put(270, "CADENA");
        listaDeTokens.put(271, "ID");
        listaDeTokens.put(272, "CTE");
        listaDeTokens.put(273, "==");
        listaDeTokens.put(274, "<>");
        listaDeTokens.put(275, ">=");
        listaDeTokens.put(276, "<=");
        listaDeTokens.put(277, "CONTRACT");
        listaDeTokens.put(278, "TRY");
        listaDeTokens.put(279, "CATCH");
        listaDeTokens.put(280, "IF");
        listaDeTokens.put(281, "&&");
        listaDeTokens.put(282, "||");
        listaDeTokens.put(283, ":=");
        listaDeTokens.put((int) '>', ">");
        listaDeTokens.put((int) '<', "<");
        listaDeTokens.put((int) '(', "(");
        listaDeTokens.put((int) ')', ")");
        listaDeTokens.put((int) '{', "{");
        listaDeTokens.put((int) '}', "}");
        listaDeTokens.put((int) '.', ".");
        listaDeTokens.put((int) ',', ",");
        listaDeTokens.put((int) ';', ";");
        listaDeTokens.put((int) ':', ":");
        listaDeTokens.put((int) '+', "+");
        listaDeTokens.put((int) '-', "-");
        listaDeTokens.put((int) '*', "*");
        listaDeTokens.put((int) '/', "/");
        listaDeTokens.put((int) '%', "%");
    }




    private static final int[][] MatrizTransicionEstados = {
            // 0    -1 ERROR , 0 FINAL/inic
            {16, 2, 1, 1, 0, 0, 0, 3, 5, 6, 7, 8, 9, 0, 0, 0,10, 12, 1, -1},
            // 1
            {0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,1, 0, 1, 0},
            // 2
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0},
            // 3
            {0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, -1},
            // 4
            {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 4,4, 4, 4, -1},
            // 5
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, -1},
            // 6
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, -1},
            // 7
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1,-1, -1, -1, -1, -1},
            // 8
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1,-1, -1, -1, -1, -1},
            // 9
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1,-1 ,-1, -1, -1, -1},
            // 10
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 10, 11, 0, -1},
            // 11
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 11, 0, 13, -1},
            //12
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, 11, -1, -1, -1},
            // 13
            {-1, -1, -1, -1, -1, 14, 14, -1, -1, -1, -1, -1, -1, -1, -1,-1, 15, -1, -1, -1},
            // 14
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, 15, -1, -1, -1},
            // 15
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 15, 0, 0, -1},
            // 16
            {0, 16, 16, 16, 16, 17, 16, 16, 16, 16, 16, 16, 16, 16, 19,16, 16, 16, 16, -1},
            // 17
            {16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 16,16, 16, 16, -1},
            // 18
            {19, 19, 19, 19, 19, 16, 19, 19, 19, 19, 19, 19, 19, 19, -1,19, 19, 19, 19, -1},
            // 19
            {0, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1,16,16, 16, 16, -1},

    };

    private static final Object[][] MatrizAccionSemantica = {
            // 0
            {AS11, AS1, AS1, AS1, AS12, AS12, AS12, AS1 , AS1, AS1, AS1, AS1, AS1, AS12, AS11,AS11, AS1, AS1, AS1, null},
            // 1
            {AS13, AS13, AS2, AS2, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13,AS13, AS2, AS13, AS2, AS13},
            // 2
            {AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS10, AS9, AS9, AS9, AS9,AS9, AS9, AS9, AS9, null},
            // 3
            {AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS6, AS9, AS9, AS9, AS9, AS9, AS9, AS9,AS9, AS9, AS9, AS9, null},
            // 4
            {AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS6,AS11, AS11, AS11, AS11, null},
            //5
            {AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS10, AS10, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, null},
            //6
            {AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS10, AS9, AS9, AS9, AS9,AS9, AS9, AS9, AS9, null},
            // 7
            {null, null, null, null, null, null, null, null, null, null, AS10, null, null, null, null, null, null, null, null,null},
            // 8
            {null, null, null, null, null, null, null, null, null, null, null, AS14, null, null, null, null, null, null, null,null},
            // 9
            {null, null, null, null, null, null, null, null, null, null, null, null, AS14, null, null, null, null, null, null,null},
            // 10
            {AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7,AS7, AS2, AS2, AS7, null},
            // 11
            {AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5,AS5, AS2, AS5, AS2, null},
            // 12
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, AS2, null, null, null},
            // 13
            {null, null, null, null, null, AS2, AS2, null, null, null, null, null, null, null, null,null, AS2, null, null, null},
            // 14
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, AS2, null, null, null},
            // 15
            {AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5,AS5, AS2, AS5, AS5, null},
            // 16
            {AS8, AS2, AS2, AS2, AS2, AS11, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS11,AS2, AS2, AS2, AS2, null},
            // 17
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, AS11,AS2, null, null, null, null},
            // 18
            {AS2, AS2, AS2, AS2, AS2, AS11, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, null, AS2,AS2, AS2, AS2, null},
            // 19
            {AS8, AS2, AS2, AS2, AS2, AS11, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, null,AS2, AS2, AS2, AS2, null}
    };


    public static int existeEnTDS(String input) {
        TDSObject result = tablaDeSimbolos.get(input);
        if(result != null){
        	result.setContRef(result.getContRef()+1);
            return result.getRefId();
        }
        return -1;
    }

    public static TDSObject getLexemaObject(String input) {
        return tablaDeSimbolos.get(input);
    }
    


    public static int getIdToken(String input) {
        for (int i = 257; i < MAX_TOKEN_ID; i++) {
            if (input.equals(listaDeTokens.get(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public static void agregarNegativoTDS(String ref)
    {
    	TDSObject result = tablaDeSimbolos.get(ref);
        if(result != null){
        	if (result.getContRef()==1)
        		{
        			tablaDeSimbolos.put("-"+ref, new TDSObject (result.getRefId(), result.getTipoVariable()));
        			tablaDeSimbolos.remove(ref);
        		}
        	else 
        	{
        		TDSObject result2 = tablaDeSimbolos.get("-"+ref);
        		if (result2 != null)
        			{
        			result2.setContRef(result2.getContRef()+1);
        			result.setContRef(result.getContRef()-1);
        			}
        		else 
        		{
        			tablaDeSimbolos.put("-"+ref, new TDSObject (indexTDS, result.getTipoVariable()));
        			indexTDS++;
        			result.setContRef(result.getContRef()-1);
        		}
        		
        	}
        }
        else 
        	{
        	TDSObject result2 = tablaDeSimbolos.get("-"+ref);
    		if (result2 != null)
    			{
    			result2.setContRef(result2.getContRef()+1);
    			}
        	}
    }

    public static int yylex() {
        token = -1;
        refTDS = "";
        reading = "";
        estado = 0;
        boolean errorCadena=false;

        while ((indexArchivo < archivo.length()) && (token == -1)) {

            //System.out.println("Leo: " + archivo.charAt(indexArchivo));
            //System.out.println("Estado actual: " + estado);
            //System.out.println("index Archivo: " + indexArchivo);

            switch (archivo.charAt(indexArchivo)) {

                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (MatrizAccionSemantica[estado][16] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][16]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][16]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][16];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '*':
                    if (MatrizAccionSemantica[estado][7] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][7]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][7]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][7];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '/':
                    if (MatrizAccionSemantica[estado][4] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][4]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][4]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][4];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '(':
                case ')':
                case '{':
                case '}':
                case ',':
                case ';':
                    if (MatrizAccionSemantica[estado][13] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][13]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][13]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][13];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case ':':
                    if (MatrizAccionSemantica[estado][1] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][1]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][1]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][1];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '.':
                    if (MatrizAccionSemantica[estado][17] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][17]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][17]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][17];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '|':
                    if (MatrizAccionSemantica[estado][11] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][11]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][11]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][11];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '&':
                    if (MatrizAccionSemantica[estado][12] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][12]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][12]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][12];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '>':
                    if (MatrizAccionSemantica[estado][9] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][9]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][9]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][9];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '<':
                    if (MatrizAccionSemantica[estado][8] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][8]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][8]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][8];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '=':
                    if (MatrizAccionSemantica[estado][10] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][10]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][10]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][10];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '+':
                    if (MatrizAccionSemantica[estado][5] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][5]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][5]);
                    }
                    estado = MatrizTransicionEstados[estado][5];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '-':
                    if (MatrizAccionSemantica[estado][6] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][6]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][6]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][6];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '%':
                    if (MatrizAccionSemantica[estado][0] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][0]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][0]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][0];

                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '_':
                    if (MatrizAccionSemantica[estado][3] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][3]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][3]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][3];

                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case 'S':
                    if (MatrizAccionSemantica[estado][18] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][18]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][18]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][18];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case ' ':
                case '	':
                    if (MatrizAccionSemantica[estado][15] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][15]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][15]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][15];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    if (MatrizAccionSemantica[estado][2] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][2]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][2]);
                    }
                    if((estado == 19 && errorCadena) || estado == 18){
                        listaDeWarnings.add("WARNING Linea " + numLinea +": falta el simbolo + despues de salto de linea en la cadena.");
                    }
                    estado = MatrizTransicionEstados[estado][2];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '\n':
                    if (MatrizAccionSemantica[estado][14] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][14]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][14]);
                    }
                    if(estado == 0 || estado == 17 || estado == 16 || estado == 4){
                        numLinea++;
                    }
                    if(estado == 16 && !errorCadena){
                        listaDeWarnings.add("WARNING Linea " + (numLinea - 1) +": falta el simbolo + antes de salto de linea en la cadena.");
                        errorCadena =true;
                    }else if (estado == 16 && errorCadena){
                        listaDeErrores.add("ERROR Linea " + (numLinea - 1) +": cadena multilinea no puede llevar dos nuevas lineas seguidas..");
                        finArchivo=true;
                        token=0;
                        indexArchivo=0;
                        numLinea=0;
                        break;
                    }
                    if(estado == 18){
                        listaDeErrores.add("ERROR Linea " + (numLinea - 1) +": cadena multilinea no puede llevar dos nuevas lineas seguidas..");
                        finArchivo=true;
                        token=0;
                        indexArchivo=0;
                        numLinea=0;
                        break;
                    }
                    estado = MatrizTransicionEstados[estado][14];
                    //System.out.println("Nuevo Estado: " + estado);
                    break;

                case '$':
                    if (MatrizAccionSemantica[estado][19] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][19]).ejecutar(archivo.charAt(indexArchivo));
                        //System.out.println(MatrizAccionSemantica[estado][19]);
                        estado = MatrizTransicionEstados[estado][19];
                        break;
                    }
                    if (estado == 0) {
                        token = 0;
                        indexArchivo = 0;
                        finArchivo=true;
                        //System.out.println("Final de archivo: " );

                    } else {
                        if(indexArchivo == archivo.length() - 1 ){ //final de archivo y no hizo \n para cerrar comentario o cadena
                            if(estado == 4){
                                listaDeErrores.add("ERROR Linea "+ numLinea + " : comentario mal cerrado, falta salto de linea." );
                                indexArchivo++;
                                finArchivo=true;
                                //System.out.println("$ error comentario mal cerrado " );

                            }/*else{
                                listaDeErrores.add("ERROR Linea " + numLinea + ": caracter $ (EOF) invalido en posible cte/cadena/id.");
                                indexArchivo++;
                                finArchivo=true;
                                System.out.println("$ invalido en posible cte/cadena/id " );
                            }*/
                        }else{
                            listaDeErrores.add("ERROR Linea " + numLinea + ": caracter $ (EOF) invalido.");
                            indexArchivo++;
                            //System.out.println("$ invalido " );
                        }

                    }
                    break;

                default:
                    listaDeErrores.add("ERROR Linea " + numLinea + ": caracter invalido \"" + archivo.charAt(indexArchivo) + "\"");
                    //System.out.println("CHAR NO DETECTADO : " + archivo.charAt(indexArchivo) );
                    indexArchivo++;
                    break;

            }

            if (estado == -1) {//RESINCRONIZACION
                listaDeErrores.add("ERROR Linea " + numLinea + ": token \"" + reading + archivo.charAt(indexArchivo) + "\" no reconocido");
                if (archivo.charAt(indexArchivo) != ';' && archivo.charAt(indexArchivo) != '\n') {
                    //System.out.println("RESYNC");
                    indexArchivo++;
                }
                estado = 0;
            }
        }
        //System.out.println("Devuelvo token: " +token);

        salidaTokens.add("AL - Linea : " + numLinea + " Token : " + token );

        return token;
    }

}

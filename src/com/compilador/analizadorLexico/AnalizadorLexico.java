package com.compilador.analizadorLexico;

import com.compilador.analizadorLexico.accionSemantica.*;

import java.util.HashMap;
import java.util.Vector;

public class AnalizadorLexico {

    public static String archivo;
    public static String reading = "";
    public static int indexArchivo = 0;
    public static int token = -1;
    public static int refTDS = -1;
    public static int estado = 0;
    public static int numLinea = 1;
    public static int indexTDS = 0;

    public static HashMap<Integer, TDSObject> tablaDeSimbolos = new HashMap<>();
    public static Vector<String> listaDeErrores = new Vector<>();
    public static Vector<String> listaDeWarnings = new Vector<>();

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

    public static HashMap<Integer, String> listaDeTokens = new HashMap<>() {{
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
    }};


    private static final int[][] MatrizTransicionEstados = {
            // 0    -1 ERROR , 0 FINAL/inic
            {16, 2, 1, 1, 0, 0, 0, 3, 5, 6, 7, 8, 9, 0, 0, 10, 12, 1, -1},
            // 1
            {0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, -1},
            // 2
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            // 3
            {0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1},
            // 4
            {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 4, 4, 4, -1},
            // 5
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1},
            // 6
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1},
            // 7
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1},
            // 8
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1},
            // 9
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1},
            // 10
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 11, 0, -1},
            // 11
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 13, -1},
            //12
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1},
            // 13
            {-1, -1, -1, -1, -1, 14, 14, -1, -1, -1, -1, -1, -1, -1, -1, 15, -1, -1, -1},
            // 14
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 15, -1, -1, -1},
            // 15
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, -1},
            // 16
            {0, 16, 16, 16, 16, 17, 16, 16, 16, 16, 16, 16, 16, 16, 19, 16, 16, 16, -1},
            // 17
            {16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, -1},
            // 18
            {19, 19, 19, 19, 19, 16, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, -1},
            // 19
            {0, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, 16, 16, 16, -1},

    };

    private static final Object[][] MatrizAccionSemantica = {
            // 0
            {AS11, AS1, AS1, AS1, AS12, AS12, AS12, AS12, AS1, AS1, AS1, AS1, AS1, AS12, AS11, AS1, AS1, AS1, null},
            // 1
            {AS13, AS13, AS2, AS2, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS2, AS13, AS13, null},
            // 2
            {AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS10, AS9, AS9, AS9, AS9, AS9, AS9, AS9, null},
            // 3
            {AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS6, AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS12, null},
            // 4
            {AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS6, AS11, AS11, AS11, null},
            //5
            {AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS10, AS10, AS9, AS9, AS9, AS9, AS9, AS9, AS9, null},
            //6
            {AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS10, AS9, AS9, AS9, AS9, AS9, AS9, AS9, null},
            // 7
            {null, null, null, null, null, null, null, null, null, null, AS10, null, null, null, null, null, null, null, null},
            // 8
            {null, null, null, null, null, null, null, null, null, null, null, AS14, null, null, null, null, null, null, null},
            // 9
            {null, null, null, null, null, null, null, null, null, null, null, null, AS14, null, null, null, null, null, null},
            // 10
            {AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS2, AS2, AS7, null},
            // 11
            {AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS2, AS5, AS2, null},
            // 12
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, AS2, null, null, null},
            // 13
            {null, null, null, null, AS2, AS2, null, null, null, null, null, null, null, null, null, AS2, null, null, null},
            // 14
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, AS2, null, null, null},
            // 15
            {AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS2, AS5, AS5, null},
            // 16
            {AS8, AS2, AS2, AS2, AS2, AS11, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS11, AS2, AS2, AS2, null},
            // 17
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, AS2, null, null, null, null},
            // 18
            {AS2, AS2, AS2, AS2, AS2, AS11, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, null, AS2, AS2, AS2, null},
            // 19
            {AS8, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, null, AS2, AS2, AS2, null}
    };


    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }


    public static boolean existeEnTDS(String input) {
        for (int i : tablaDeSimbolos.keySet()) {
            if (tablaDeSimbolos.get(i).getLexema().equals(input)) {
                return true;
            }
        }
        return false;
    }

    public static int esPalabraReservada(String input) {
        for (int i = 257; i < MAX_TOKEN_ID; i++) {
            if (input.equals(listaDeTokens.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static int yylex() {
        token = -1;
        refTDS = -1;
        reading = "";
        estado = 0;

        while ((indexArchivo < archivo.length()) && (token == -1) && (token != 0)) {

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
                    if (MatrizAccionSemantica[estado][15] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][15]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][15];
                    break;

                case '*':
                    if (MatrizAccionSemantica[estado][7] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][7]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][7];
                    break;

                case '/':
                    if (MatrizAccionSemantica[estado][4] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][4]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][4];
                    break;

                case '(':
                case ')':
                case '{':
                case '}':
                case ',':
                case ';':
                    if (MatrizAccionSemantica[estado][13] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][13]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][13];
                    break;

                case ':':
                    if (MatrizAccionSemantica[estado][1] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][1]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][1];
                    break;

                case '.':
                    if (MatrizAccionSemantica[estado][16] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][16]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][16];
                    break;

                case '|':
                    if (MatrizAccionSemantica[estado][11] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][11]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][11];
                    break;

                case '&':
                    if (MatrizAccionSemantica[estado][12] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][12]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][12];
                    break;

                case '>':
                    if (MatrizAccionSemantica[estado][9] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][9]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][9];
                    break;

                case '<':
                    if (MatrizAccionSemantica[estado][8] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][8]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][8];
                    break;

                case '=':
                    if (MatrizAccionSemantica[estado][10] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][10]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][10];
                    break;

                case '+':
                    if (MatrizAccionSemantica[estado][5] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][5]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][5];
                    break;

                case '-':
                    if (MatrizAccionSemantica[estado][6] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][6]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][6];
                    break;

                case '%':
                    if (MatrizAccionSemantica[estado][0] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][0]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][0];
                    break;

                case '_':
                    if (MatrizAccionSemantica[estado][3] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][3]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][3];
                    break;

                case 'S':
                    if (MatrizAccionSemantica[estado][17] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][17]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][17];
                    break;

                case ' ':
                case '	':
                    if (MatrizAccionSemantica[estado][14] != null) {
                        ((AccionSemantica) MatrizAccionSemantica[estado][14]).ejecutar(archivo.charAt(indexArchivo));
                    }
                    estado = MatrizTransicionEstados[estado][14];
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
                    }
                    estado = MatrizTransicionEstados[estado][2];
                    break;

                case '\n':

                case '$':
                    if (estado == 0) {
                        token = 0;
                        indexArchivo = 0;
                    } else {
                        listaDeErrores.add("ERROR(19) Linea " + numLinea + ": caracter $ (EOF) invalido.");
                        indexArchivo++;
                    }
                    break;

                default:
                    listaDeErrores.add("ERROR Linea " + numLinea + ": caracter invalido \"" + archivo.charAt(indexArchivo) + "\"");
                    indexArchivo++;
                    break;

            }

            if (estado == -1) {//RESINCRONIZACION
                listaDeErrores.add("ERROR Linea " + numLinea + ": token \"" + reading + archivo.charAt(indexArchivo) + "\" no reconocido");
                if (archivo.charAt(indexArchivo) != ';' && archivo.charAt(indexArchivo) != '\n') {
                    indexArchivo++;
                }
                estado = 0;
            }
        }
        return token;
    }

}

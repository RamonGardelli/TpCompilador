package com.compilador.analizadorLexico;

import com.compilador.analizadorLexico.accionSemantica.*;

import java.util.HashMap;
import java.util.Vector;

public class AnalizadorLexico {

    private String archivo;
    public static String reading = "";
    public static int indexArchivo = 0;
    public static int token = -1;
    private int estado = 0;
    public static int numLinea = 1;
    public static int indexTDS = 0;

    public static HashMap<Integer, TDSObject> tablaDeSimbolos = new HashMap<>();
    public static Vector<String> listaDeErrores = new Vector<>();
    public static Vector<String> listaDeWarnings = new Vector<>();

    public static final int MAX_ID_VALUE = 22;
    public static final int MAX_TOKEN_ID = 283;

    private AccionSemantica AS1 = new AS1();
    private AccionSemantica AS2 = new AS2();
    private AccionSemantica AS3 = new AS3();
    private AccionSemantica AS4 = new AS4();
    private AccionSemantica AS5 = new AS5();
    private AccionSemantica AS6 = new AS6();
    private AccionSemantica AS7 = new AS7();
    private AccionSemantica AS8 = new AS8();
    private AccionSemantica AS9 = new AS9();
    private AccionSemantica AS10 = new AS10();
    private AccionSemantica AS11 = new AS11();
    private AccionSemantica AS12 = new AS12();
    private AccionSemantica AS13 = new AS13();
    private AccionSemantica AS14 = new AS14();

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
        listaDeTokens.put(281, "CTE");
        listaDeTokens.put(282, "&&");
        listaDeTokens.put(283, "||");
        listaDeTokens.put((int) '=', "=");
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
    }};


    private int[][] MatrizTransicionEstados = {
            //FILA 0    -1 ERROR , 0 FINAL/inic
            {16, 2, 1, 1, 0, 0, 0, 3, 5, 6, 7, 8, 9, 0, 0, 10, 12, 1, -1},
            //FILA 1
            {0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, -1},
            //FILA 2
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1},
            //FILA 3
            {0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1},
            //FILA 4
            {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 4, 4, 4, -1},
            //FILA 5
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1},
            //FILA 6
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1},
            //FILA 7
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1},
            //FILA 8
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1},
            //FILA 9
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1},
            //FILA 10
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 11, 0, -1},
            //FILA 11
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 13, -1},
            //FILA 12
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1},
            //FILA 13
            {-1, -1, -1, -1, -1, 14, 14, -1, -1, -1, -1, -1, -1, -1, -1, 15, -1, -1, -1},
            //FILA 14
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 15, -1, -1, -1},
            //FILA 15
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, -1},
            //FILA 16
            {0, 16, 16, 16, 16, 17, 16, 16, 16, 16, 16, 16, 16, 16, 19, 16, 16, 16, -1},
            //FILA 17
            {16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, -1},
            //FILA 18
            {19, 19, 19, 19, 19, 16, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, -1},
            //FILA 19
            {0, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, 16, 16, 16, -1},

    };

    private Object[][] MatrizAccionSemantica = {
            //FILA 0
            {AS11, AS1, AS1, AS1, AS12, AS12, AS12, AS12, AS1, AS1, AS1, AS1, AS1, AS12, AS11, AS1, AS1, AS1, null},
            //FILA 1
            {AS13, AS13, AS2, AS2, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS2, AS13, AS13, null},
            //FILA 2
            {null, null, null, null, null, null, null, null, null, null, AS10, null, null, null, null, null, null, null, null},
            //FILA 3
            {AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS6, AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS12, null},
            //FILA 4
            {AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS6, AS11, AS11, AS11, null},
            //FILA 5
            {AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS10, AS10, AS9, AS9, AS9, AS9, AS9, AS9, AS9, null},
            //FILA 6
            {AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS10, AS9, AS9, AS9, AS9, AS9, AS9, AS9, null},
            //FILA 7
            {null, null, null, null, null, null, null, null, null, null, AS10, null, null, null, null, null, null, null, null},
            //FILA 8
            {null, null, null, null, null, null, null, null, null, null, null, AS14, null, null, null, null, null, null, null},
            //FILA 9
            {null, null, null, null, null, null, null, null, null, null, null, null, AS14, null, null, null, null, null, null},
            //FILA 10
            {AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS2, AS2, AS7, null},
            //FILA 11
            {AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS2, AS5, AS2, null},
            //FILA 12
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, AS2, null, null, null},
            //FILA 13
            {null, null, null, null, AS2, AS2, null, null, null, null, null, null, null, null, null, AS2, null, null, null},
            //FILA 14
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, AS2, null, null, null},
            //FILA 15
            {AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS2, AS5, AS5, null},
            //FILA 16
            {AS8, AS2, AS2, AS2, AS2, AS11, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS11, AS2, AS2, AS2, null},
            //FILA 17
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, AS2, null, null, null, null},
            //FILA 18
            {AS2, AS2, AS2, AS2, AS2, AS11, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, null, AS2, AS2, AS2, null},
            //FILA 19
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
            if (input.equals(listaDeTokens.get(i))){
                return i;
            }
        }
        return -1;
    }




}

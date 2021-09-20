package com.compilador.analizadorLexico;

import com.compilador.analizadorLexico.accionSemantica.*;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalizadorLexico {

    private String archivo;
    private String buffer;
    private int indexArchivo = 0;
    private int token = -1;
    private int estado=0;

    public static HashMap<Integer, String[]> tablaDeSimbolos = new HashMap<>();
    public static ArrayList<String> listaDeErrores = new ArrayList<>();
    public static ArrayList<String> listaDeWarnings = new ArrayList<>();

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
            {-1, -1,-1 , -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1},
            //FILA 8
            {-1, -1,-1 , -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1},
            //FILA 9
            {-1, -1,-1 , -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1},
            //FILA 10
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 11, 0, -1},
            //FILA 11
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 13, -1},
            //FILA 12
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1},
            //FILA 13
            {-1, -1, -1, -1, -1, 14, 14, -1, -1, -1,-1, -1, -1, -1, -1, 15, -1, -1, -1},
            //FILA 14
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1, -1, -1, -1, 15, -1, -1, -1},
            //FILA 15
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 15, 0, 0, -1},
            //FILA 16
            {0, 16, 16, 16, 16, 17, 16, 16, 16, 16, 16, 16, 16, 16, 19, 16, 16, 16, -1},
            //FILA 17
            {16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, -1},
            //FILA 18
            {19, 19, 19, 19, 19, 16, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, -1},
            //FILA 19
            {0, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, 16, 16, 16, -1},

    };

    private  Object[][] MatrizAccionSemantica = {
            //FILA 0
            {AS11, AS1, AS1, AS1, AS12, AS12, AS12, AS12, AS1, AS1, AS1, AS1, AS1, AS12, AS11, AS1, AS1, AS1, null},
            //FILA 1
            {AS13, AS13, AS2, AS2, AS13, AS13, AS13,AS13, AS13, AS13, AS13, AS13, AS13, AS13, AS13,AS2, AS13, AS13, null},
            //FILA 2
            {null, null, null, null, null, null, null,null, null, null, AS10, null, null, null, null,null, null, null, null},
            //FILA 3
            {AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS6, AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS12, AS12, null},
            //FILA 4
            {AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS6, AS11, AS11, AS11, null},
            //FILA 5
            {AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS9,AS10,AS10,AS9,AS9,AS9,AS9,AS9,AS9,AS9,null},
            //FILA 6
            {AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS9, AS10, AS9, AS9, AS9, AS9, AS9, AS9, AS9, null},
            //FILA 7
            {null, null,null,null,null,null,null,null,null,null,AS10,null,null,null,null,null,null,null,null},
            //FILA 8
            {null, null,null,null,null,null,null,null,null,null,null,AS14,null,null,null,null,null,null,null},
            //FILA 9
            {null, null,null,null,null,null,null,null,null,null,null,null,AS14,null,null,null,null,null,null},
            //FILA 10
            {AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS2,AS2,AS7,null},
            //FILA 11
            {AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS2, AS5, AS2, null},
            //FILA 12
            {null, null,null,null,null,null,null,null,null,null,null,null,null,null,null,AS2,null,null,null},
            //FILA 13
            {null, null,null,null,AS2,AS2,null,null,null,null,null,null,null,null,null,AS2,null,null,null},
            //FILA 14
            {null, null,null,null,null,null,null,null,null,null,null,null,null,null,null,AS2,null,null,null},
            //FILA 15
            {AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS2,AS5,AS5,null},
            //FILA 16
            {AS11, AS2, AS2, AS2, AS2, AS11, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,AS11, AS2, AS2, AS2,null},
            //FILA 17
            {null, null,null,null,null,null,null,null,null,null,null,null,null,null,AS2,null,null,null,null},
            //FILA 18
            {null, null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null},
            //FILA 19
            {AS11, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,null, AS2, AS2, AS2,null}
    };








}

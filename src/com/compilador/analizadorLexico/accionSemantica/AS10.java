package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS10 extends AccionSemantica{

    //Devolver comparador doble
    @Override
    public void ejecutar( char input) {

        AnalizadorLexico.reading += input;

        switch (AnalizadorLexico.reading) {
            case ">=":
                AnalizadorLexico.token = 275;
                break;
            case "<=":
                AnalizadorLexico.token = 276;
                break;
            case "==":
                AnalizadorLexico.token = 273;
                break;
            case ":=":
                AnalizadorLexico.token = 283;
                break;
            default:
                AnalizadorLexico.token = 274; // "<>"
                break;
        }

        AnalizadorLexico.indexArchivo++;

    }
}

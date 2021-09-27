package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS9 extends AccionSemantica {

    //Devolver comparador simple
    @Override
    public void ejecutar( char input) {

        switch (AnalizadorLexico.reading){
            case ">":
                AnalizadorLexico.token = 62;
                break;
            case "<":
                AnalizadorLexico.token = 60;
                break;
            case "*":
                AnalizadorLexico.token = 42;
                break;
            default:
                AnalizadorLexico.token = 58; //':'
                break;
        }

    }
}

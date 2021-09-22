package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS1 extends AccionSemantica {

    //Inicializo el buffer y agrego caracter

    @Override
    public void ejecutar(char input) {

        AnalizadorLexico.reading = "" + input;

        AnalizadorLexico.indexArchivo++;


    }
}

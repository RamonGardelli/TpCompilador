package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS2 extends AccionSemantica {

    //Agregar caracter al buffer
    @Override
    public void ejecutar(char input) {

        AnalizadorLexico.reading += String.valueOf(input);

        AnalizadorLexico.indexArchivo++;



    }
}

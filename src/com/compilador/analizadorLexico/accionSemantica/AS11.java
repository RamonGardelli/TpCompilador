package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS11 extends AccionSemantica {

    //Ignorar caracter ( Avanzar en archivo )
    @Override
    public void ejecutar(char input) {
        AnalizadorLexico.indexArchivo++;
    }
}

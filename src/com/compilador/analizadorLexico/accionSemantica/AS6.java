package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

public class AS6 extends AccionSemantica {

    //Limpiar buffer
    @Override
    public void ejecutar( char input) {


        AnalizadorLexico.reading = "";
        AnalizadorLexico.indexArchivo++;
    }
}

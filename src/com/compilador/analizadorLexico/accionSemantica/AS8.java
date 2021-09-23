package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

public class AS8 extends AccionSemantica{

    //Validar cadena caracteres (AVANZA y guarda cadena)
    @Override
    public void ejecutar(char input) {

        AnalizadorLexico.reading += String.valueOf(input);
        String s7 = AnalizadorLexico.reading.replaceAll("-\n","");

        //Agregar a TDS si no existe
        int _indexTDS = AnalizadorLexico.indexTDS;

        if(!AnalizadorLexico.existeEnTDS(AnalizadorLexico.reading)){
            AnalizadorLexico.tablaDeSimbolos.put(_indexTDS,new TDSObject(AnalizadorLexico.reading,"CADENA")); //está bien poner CADENA?
            AnalizadorLexico.indexTDS++;
        }

        //return token
        AnalizadorLexico.token = AnalizadorLexico.esPalabraReservada("CADENA");
        AnalizadorLexico.refTDS = _indexTDS;

        // Avanzar el iterador
        AnalizadorLexico.indexArchivo++;






    }
}

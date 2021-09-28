package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

public class AS8 extends AccionSemantica{

    //Validar cadena caracteres (AVANZA y guarda cadena)
    @Override
    public void ejecutar(char input) {

        //AnalizadorLexico.reading += String.valueOf(input);
        String _reading = AnalizadorLexico.reading.replaceAll("-\n","");

        //Agregar a TDS si no existe
        int _indexTDS = AnalizadorLexico.indexTDS;
        int result = AnalizadorLexico.existeEnTDS(_reading);
        if(result == -1){
            AnalizadorLexico.tablaDeSimbolos.put(_indexTDS,new TDSObject(_reading,"CADENA")); //está bien poner CADENA?
            AnalizadorLexico.indexTDS++;
        }else{
            _indexTDS = result;
        }

        //return token
        AnalizadorLexico.token = AnalizadorLexico.getIdToken("CADENA");
        AnalizadorLexico.refTDS = _indexTDS;

        // Avanzar el iterador
        AnalizadorLexico.indexArchivo++;






    }
}

package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

import static com.compilador.analizadorLexico.AnalizadorLexico.MAX_ID_VALUE;

public class AS3  extends AccionSemantica{

    //Controlar que si es identificador y excede la long maxima, truncar el resto
    @Override
    public void ejecutar( char input) {

        //Truncar
        String _reading = AnalizadorLexico.reading;
        if(_reading.length() > MAX_ID_VALUE){
            AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea + " : el identificador " + AnalizadorLexico.reading + " fue truncado.");
            _reading = _reading.substring(0,MAX_ID_VALUE - 1);
            AnalizadorLexico.reading = _reading;
        }
        //Agregar a TDS si no existe
        int _indexTDS = AnalizadorLexico.indexTDS;

        if(!AnalizadorLexico.existeEnTDS(_reading)){
            AnalizadorLexico.tablaDeSimbolos.put(_indexTDS,new TDSObject(_reading,"STRING")); //está bien poner string?
            AnalizadorLexico.indexTDS++;
        }
        //return token
        //AnalizadorLexico.token =
        //ref tabla de simbolos?





    }
}

package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

import java.math.BigDecimal;

public class AS7 extends AccionSemantica{

    public static final long MAX_LONG = 2147483647;
    public static final long MIN_LONG = -2147483648;

    //Controlar rango de enteros, si esta agregar a TS, si no, error
    @Override
    public void ejecutar(char input) {

        long _reading;

        if(AnalizadorLexico.reading.length() > 10){
            _reading = Long.parseLong(AnalizadorLexico.reading.substring(0,10));
        }else{
            _reading = Long.parseLong(AnalizadorLexico.reading);
        }

        if( _reading >= MIN_LONG && _reading <= (Math.abs(MIN_LONG))){
            //valid
        }else{
            //warning
            AnalizadorLexico.listaDeWarnings.add("Warning Linea " + AnalizadorLexico.numLinea + " : constante LONG fuera de rango.");
            _reading = 2147483648L;
        }

        int _indexTDS = AnalizadorLexico.indexTDS;
        int result = AnalizadorLexico.existeEnTDS(String.valueOf(_reading));
        if(result == -1){
            AnalizadorLexico.tablaDeSimbolos.put(String.valueOf(_reading),new TDSObject("LONG"));
            AnalizadorLexico.indexTDS++;
        }else{
            _indexTDS = result;
        }

        //return token
        AnalizadorLexico.token = AnalizadorLexico.getIdToken("CTE");
        AnalizadorLexico.refTDS = String.valueOf(_reading);

        //AnalizadorLexico.indexArchivo++;


    }
}

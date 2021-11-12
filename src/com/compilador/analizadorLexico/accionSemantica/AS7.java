package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

public class AS7 extends AccionSemantica{

    public static final int MAX_LONG = 2147483647;
    public static final int MIN_LONG = -2147483648;


    //Controlar rango de enteros, si esta agregar a TS, si no, error
    @Override
    public void ejecutar(char input) {

        long _reading = Long.parseLong(AnalizadorLexico.reading);
        if( _reading >= MIN_LONG && _reading <= MAX_LONG){
            int _indexTDS = AnalizadorLexico.indexTDS;
            int result = AnalizadorLexico.existeEnTDS(AnalizadorLexico.reading);
            if(result == -1){
                AnalizadorLexico.tablaDeSimbolos.put(AnalizadorLexico.reading,new TDSObject("LONG"));
                AnalizadorLexico.indexTDS++;
            }else{
                _indexTDS = result;
            }

            //return token
            AnalizadorLexico.token = AnalizadorLexico.getIdToken("CTE");
            AnalizadorLexico.refTDS = AnalizadorLexico.reading;


        }else{
            //error lexico
            AnalizadorLexico.listaDeErrores.add("ERROR Linea " + AnalizadorLexico.numLinea + " : constante LONG fuera de rango.");
            AnalizadorLexico.token = 0;
            AnalizadorLexico.finArchivo =true;
            AnalizadorLexico.indexArchivo=0;
            AnalizadorLexico.numLinea=0;
        }

        //AnalizadorLexico.indexArchivo++;


    }
}

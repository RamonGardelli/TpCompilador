package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

public class AS5 extends AccionSemantica {

    public static final float MIN_FLOAT = 1.17549435E-38f;
    public static final float MAX_FLOAT = 3.40282347E+38f;


    //Controlar rango del float, si esta en rango agregar a TS, si no, error
    @Override
    public void ejecutar(char input) {

        float _reading = Float.parseFloat((AnalizadorLexico.reading.replace('S','E')));

        boolean enRango=false;

        if(AnalizadorLexico.reading.equals(("0.0")) || AnalizadorLexico.reading.equals(("0.")) || AnalizadorLexico.reading.equals((".0"))){
            enRango =true;
        }else if(MIN_FLOAT < _reading && _reading < MAX_FLOAT){
            enRango =true;
        }else{
            AnalizadorLexico.listaDeErrores.add("ERROR Linea " + AnalizadorLexico.numLinea + " : la constante FLOAT esta fuera de rango.");
            AnalizadorLexico.token = 0;
            AnalizadorLexico.finArchivo =true;
            AnalizadorLexico.indexArchivo=0;
            AnalizadorLexico.numLinea=0;
        }

        if (enRango){

            int _indexTDS = AnalizadorLexico.indexTDS;
            int result = AnalizadorLexico.existeEnTDS(AnalizadorLexico.reading);

            if (result == -1) {
                AnalizadorLexico.tablaDeSimbolos.put(AnalizadorLexico.reading, new TDSObject("SINGLE"));
                AnalizadorLexico.indexTDS++;
            }else{
                _indexTDS = result;
            }

            AnalizadorLexico.token = AnalizadorLexico.getIdToken("CTE");
            AnalizadorLexico.refTDS = AnalizadorLexico.reading;
            AnalizadorLexico.indexTDS++;

        }









    }
}

package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

public class AS5 extends AccionSemantica {

    public static final float MIN_FLOAT = 1.17549435E-38f;
    public static final float MAX_FLOAT = 3.40282347E+38f;
    public static final float correctionPositivo = 3.40282346E+38f;
    public static final float correctionNegativo = 1.17549436E-38f;


    //Controlar rango del float, si esta en rango agregar a TS, si no, error
    @Override
    public void ejecutar(char input) {

        float _reading = Float.parseFloat((AnalizadorLexico.reading.replace('S','E')));


        if(AnalizadorLexico.reading.equals(("0.0")) || AnalizadorLexico.reading.equals(("0.")) || AnalizadorLexico.reading.equals((".0"))){
            //valid
        }else if(MIN_FLOAT < _reading && _reading < MAX_FLOAT){
           //valid
        }else if(_reading  < MIN_FLOAT) {
            AnalizadorLexico.listaDeWarnings.add("Warning Linea " + AnalizadorLexico.numLinea + " : constante FLOAT fuera de rango.");
            _reading = correctionNegativo;
        }else if(_reading > MAX_FLOAT){
            AnalizadorLexico.listaDeWarnings.add("Warning Linea " + AnalizadorLexico.numLinea + " : constante FLOAT fuera de rango.");
            _reading = correctionPositivo;
        }

            int _indexTDS = AnalizadorLexico.indexTDS;
            int result = AnalizadorLexico.existeEnTDS(String.valueOf(_reading));

            if (result == -1) {
                AnalizadorLexico.tablaDeSimbolos.put(String.valueOf(_reading), new TDSObject("SINGLE"));
                AnalizadorLexico.indexTDS++;
            }else{
                _indexTDS = result;
            }

            AnalizadorLexico.token = AnalizadorLexico.getIdToken("CTE");
            AnalizadorLexico.refTDS = String.valueOf(_reading);
            AnalizadorLexico.indexTDS++;

        }

}

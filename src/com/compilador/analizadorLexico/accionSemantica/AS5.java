package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

public class AS5 extends AccionSemantica {


    public static final double MIN_FLOAT = 1.17549436E-38;
    public static final double MAX_FLOAT = 3.40282346E+38 ;

    
    //Controlar rango del float, si esta en rango agregar a TS, si no, error
    @Override
    public void ejecutar(char input) {

        //float _reading = Float.parseFloat((AnalizadorLexico.reading.replace('S','E')));
    	double numero = 0;

        if(AnalizadorLexico.reading.equals(("0.0")) || AnalizadorLexico.reading.equals(("0.")) || AnalizadorLexico.reading.equals((".0"))){
            //valid
        }
        else {

            if (AnalizadorLexico.reading.contains("S")) {
                String[] parts = AnalizadorLexico.reading.split("S");
                
                
                numero = (Double.valueOf(parts[0]) * Math.pow(10, Double.valueOf(parts[1])));
            } else {
                numero = Double.valueOf(AnalizadorLexico.reading);
            }
            if (numero < MIN_FLOAT)
            {
            	numero = MIN_FLOAT;
            	AnalizadorLexico.listaDeWarnings.add("Warning Linea " + AnalizadorLexico.numLinea + " : constante SINGLE fuera de rango.");
            }
            else if (numero > MAX_FLOAT) { numero = MAX_FLOAT;
            AnalizadorLexico.listaDeWarnings.add("Warning Linea " + AnalizadorLexico.numLinea + " : constante SINGLE fuera de rango.");
            	}
            	
            
        }
            int _indexTDS = AnalizadorLexico.indexTDS;
            int result = AnalizadorLexico.existeEnTDS(String.valueOf(numero));

            if (result == -1) {
                AnalizadorLexico.tablaDeSimbolos.put(String.valueOf(numero), new TDSObject("SINGLE"));
                AnalizadorLexico.indexTDS++;
            }else{
                _indexTDS = result;
            }

            AnalizadorLexico.token = AnalizadorLexico.getIdToken("CTE");
            AnalizadorLexico.refTDS = String.valueOf(numero);
            AnalizadorLexico.indexTDS++;

        }

}

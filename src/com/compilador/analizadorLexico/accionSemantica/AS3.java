package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;

import static com.compilador.analizadorLexico.AnalizadorLexico.MAX_ID_VALUE;

public class AS3 extends AccionSemantica {

    //Controlar que si es identificador y excede la long maxima, truncar el resto
    @Override
    public void ejecutar(char input) {

        boolean validID = true;

        //Truncar
        String _reading = AnalizadorLexico.reading;

        if ((Character.isLetter(_reading.charAt(0))) || (_reading.charAt(0) == '_')) {

            for (int i = 0; i < _reading.length(); i++) {
                char c = _reading.charAt(i);
                if (!(Character.isDigit(c) || (Character.isLetter(c) || c == '_')))
                    validID = false;
            }

            if (validID) {

                if (_reading.length() > MAX_ID_VALUE) {
                    AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea + " : el identificador " + AnalizadorLexico.reading + " fue truncado.");
                    _reading = _reading.substring(0, MAX_ID_VALUE - 1);
                    AnalizadorLexico.reading = _reading;
                }
                //Agregar a TDS si no existe
                int _indexTDS = AnalizadorLexico.indexTDS;

                if (!AnalizadorLexico.existeEnTDS(_reading)) {
                    AnalizadorLexico.tablaDeSimbolos.put(_indexTDS, new TDSObject(_reading, "ID"));
                    AnalizadorLexico.indexTDS++;
                }
                //return token
                AnalizadorLexico.token = AnalizadorLexico.esPalabraReservada("ID");
                AnalizadorLexico.refTDS = _indexTDS;
            }
        }

        if(!validID){
            AnalizadorLexico.listaDeErrores.add("ERROR linea " + AnalizadorLexico.numLinea + " : " + AnalizadorLexico.reading + " no es un identificador o una palabra reservada.");
            AnalizadorLexico.token = 0;
            AnalizadorLexico.finArchivo =true;
            AnalizadorLexico.indexArchivo=0;
            AnalizadorLexico.numLinea=0;
        }
    }
}

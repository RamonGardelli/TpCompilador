package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS14 extends AccionSemantica {

    //Devolver Op Logico
    @Override
    public void ejecutar(char input) {

        AnalizadorLexico.reading += input;

        switch (AnalizadorLexico.reading) {
            case "&&":
                AnalizadorLexico.token = 281;
                break;
            case "||":
                AnalizadorLexico.token = 282;
                break;

        }

        AnalizadorLexico.indexArchivo++;

    }
}

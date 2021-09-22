package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS12  extends AccionSemantica{

    //Devolver Literales ( + - / * [ ] { } )
    @Override
    public void ejecutar( char input) {

        AnalizadorLexico.reading = String.valueOf(input);

        switch (AnalizadorLexico.reading) {
            case "+":
                AnalizadorLexico.token = 43;
                break;
            case "-":
                AnalizadorLexico.token = 45;
                break;
            case "*":
                AnalizadorLexico.token = 42;
                break;
            case "/":
                AnalizadorLexico.token = 47;
                break;
            case "(":
                AnalizadorLexico.token = 40;
                break;
            case ")":
                AnalizadorLexico.token = 41;
                break;
            case "{":
                AnalizadorLexico.token = 123;
                break;
            case "}":
                AnalizadorLexico.token = 125;
                break;
            case ",":
                AnalizadorLexico.token = 44;
                break;
            case ";":
                AnalizadorLexico.token = 59;
                break;
            case ":":
                AnalizadorLexico.token = 58;
                break;
            case ".":
                AnalizadorLexico.token = 46;
                break;

        }

        AnalizadorLexico.indexArchivo++;

    }
}

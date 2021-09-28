package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS4 extends AccionSemantica {

    //Validar palabra reservada
    @Override
    public void ejecutar(char input) {

        switch (AnalizadorLexico.reading){
            case "ELSE":
            case "THEN":
            case "ENDIF":
            case "PRINT":
            case "FUNC":
            case "RETURN":
            case "WHILE":
            case "LONG":
            case "BEGIN":
            case "END":
            case "SINGLE":
            case "BREAK":
            case "DO":
            case "CONTRACT":
            case "TRY":
            case "CATCH":
            case "IF":
                AnalizadorLexico.token = AnalizadorLexico.getIdToken(AnalizadorLexico.reading);
        }



    }
}

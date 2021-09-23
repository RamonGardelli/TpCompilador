package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS4 extends AccionSemantica {

    //Validar palabra reservada
    @Override
    public void ejecutar(char input) {
        boolean result=false;

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
                result = true;
        }


        if (result) {
            AnalizadorLexico.token = AnalizadorLexico.esPalabraReservada(AnalizadorLexico.reading);
        }
    }
}

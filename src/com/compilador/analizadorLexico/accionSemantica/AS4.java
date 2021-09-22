package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS4 extends AccionSemantica {

    //Validar palabra reservada
    @Override
    public void ejecutar(char input) {

        int result = AnalizadorLexico.esPalabraReservada(AnalizadorLexico.reading);

        if (result != -1) {
            //AnalizadorLexico.token = result
        }else{
            //si no es reservada? whats happen?????? saludos?????
        }
    }
}

package com.compilador.analizadorLexico.accionSemantica;

import com.compilador.analizadorLexico.AnalizadorLexico;

public class AS13  extends AccionSemantica{

    private AccionSemantica AS3 = new AS3();
    private AccionSemantica AS4 = new AS4();

    //Controlar Palabra Reservada o Identificador, si no, error (AS3 + AS4)
    @Override
    public void ejecutar(char input) {

        this.AS4.ejecutar(input);
        if(AnalizadorLexico.token == -1){
            this.AS3.ejecutar(input);
        }



    }
}

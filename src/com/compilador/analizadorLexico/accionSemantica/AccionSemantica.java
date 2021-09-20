package com.compilador.analizadorLexico.accionSemantica;

public abstract class AccionSemantica {

    public abstract void ejecutar(String buffer, char input);
}

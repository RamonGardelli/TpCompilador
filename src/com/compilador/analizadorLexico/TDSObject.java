package com.compilador.analizadorLexico;

public class TDSObject {

    private String lexema;
    private String tipoVariable;

    public TDSObject(String lexema, String tipoVariable){
        this.lexema = lexema;
        this.tipoVariable = tipoVariable;

    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getTipoVariable() {
        return tipoVariable;
    }

    public void setTipoVariable(String tipoVariable) {
        this.tipoVariable = tipoVariable;
    }

    public String imprimir(){
        return "Lexema: " + lexema + " | Tipo Variable: "+ tipoVariable;
    }
    
}

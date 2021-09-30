package com.compilador.analizadorLexico;

public class TDSObject {

    private int refID;
    private String tipoVariable;

    public TDSObject(int refID, String tipoVariable){
        this.refID = refID;
        this.tipoVariable = tipoVariable;

    }

    public int getRefId() {
        return refID;
    }

    public void setRefID(int refID) {
        this.refID = refID;
    }

    public String getTipoVariable() {
        return tipoVariable;
    }

    public void setTipoVariable(String tipoVariable) {
        this.tipoVariable = tipoVariable;
    }

    public String imprimir(){
        return "RefID: " + refID + " | Tipo Variable: "+ tipoVariable;
    }
    
}

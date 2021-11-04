package com.compilador.analizadorLexico;

public class TDSObject {

    private int refID;
    private int contRef = 0;
    private String tipoVariable;
    private String ambito;
    private String tipoContenido;

    public TDSObject(int refID, String tipoVariable){
        this.refID = refID;
        this.tipoVariable = tipoVariable;
        this.contRef++;

    }

    public int getRefId() {
        return refID;
    }
    
    public int getContRef() {
        return contRef;
    }
    
    public void setContRef(int contRef) {
        this.contRef = contRef;
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

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public String getTipoContenido() {
		return tipoContenido;
	}

	public void setTipoContenido(String tipoContenido) {
		this.tipoContenido = tipoContenido;
	}
    
}

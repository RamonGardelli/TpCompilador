package com.compilador.analizadorLexico;

public class TDSObject {

    private int contRef = 0;
    private String tipoVariable;
    private String tipoContenido;
    private float valor;
    private boolean esFuncion = false;
    private String nombreParametro;
    
    
    
    public String getNombreParametro() {
		return nombreParametro;
	}

	public void setNombreParametro(String nombreParametro) {
		this.nombreParametro = nombreParametro;
	}

	public boolean esFuncion() {
        return esFuncion;
    }

    public void setEsFuncion(boolean esFuncion) {
        this.esFuncion = esFuncion;
    }



    public String getTipoParametro() {
        return tipoParametro;
    }

    public void setTipoParametro(String tipoParametro) {
        this.tipoParametro = tipoParametro;
    }

    private String tipoParametro;

    public TDSObject( String tipoVariable){
        this.tipoVariable = tipoVariable;
        this.contRef++;
    }
    
    public int getContRef() {
        return contRef;
    }
    
    public void setContRef(int contRef) {
        this.contRef = contRef;
    }

    public String getTipoVariable() {
        return tipoVariable;
    }

    public void setTipoVariable(String tipoVariable) {
        this.tipoVariable = tipoVariable;
    }

    public String toString(){
        return  " Tipo Variable: "+ tipoVariable + "|| TIPO Contenido: " + tipoContenido + "|| CONT REF: " + contRef;
    }

	public String getTipoContenido() {
		return tipoContenido;
	}

	public void setTipoContenido(String tipoContenido) {
		this.tipoContenido = tipoContenido;
	}
    
	public long getValorLong() {
		return (long)this.valor;
	}
	
	public float getValorFloat() {
		return this.valor;
	}
	
	public void setValor(float valor) {
		this.valor=valor;
	}
}

package com.compilador.arbolSintactico;

import com.compilador.assembler.Registro;

public class Nodo {

    private Nodo left;
    private Nodo right;
    private String ref; //referencia a tabla de simbolo o simplemente operacion/valor aritmetico. Lo determina si es hoja.
    private boolean esHoja; // si es hoja, es una ref a tabla de simbolos.
    private String tipo;
    private boolean esRegistro;

    public Nodo(String aritmetic, Nodo reglaDer1, Nodo reglaDer2) {
        this.ref = aritmetic;
        this.left = reglaDer1;
        this.right = reglaDer2;
        this.esHoja = false;
        this.esRegistro = false;
    }

    public Nodo(String refTDS){
        this.ref = refTDS;
        this.esHoja = true;
    }

    public Nodo getLeft() {
        return left;
    }
    public void setLeft(Nodo left) {
        this.left = left;
    }
    public Nodo getRight() {
        return right;
    }
    public void setRight(Nodo right) {
        this.right = right;
    }
    public String getRef() {
        return ref;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean EsHoja() {
        return esHoja;
    }

    public void setEsHoja(boolean esHoja) {
        this.esHoja = esHoja;
    }
    
    public void setRegistro(boolean esRegistro) {
    	this.esRegistro=esRegistro;
    	if (!esRegistro) 
    		//DECIRLE AL REGISTRO QUE ESTA DISPONIBLE
    }


	public void generarCodigo(String codigo, Registro r[]) {
    	int i=0;
    
    	if ((this.left.EsHoja())&&(!this.right.EsHoja())){
    			this.right.generarCodigo(codigo, r);
    		}
    	
    	if ((!this.left.EsHoja())&&(this.right.EsHoja())){
			this.left.generarCodigo(codigo, r);
		}
    	
    	if ((!this.left.EsHoja())&&(!this.right.EsHoja())){
			this.left.generarCodigo(codigo, r);
			this.right.generarCodigo(codigo, r);
		}
    	
    	if ((this.left.EsHoja())&&(this.right.EsHoja())) {
    		boolean registroLibre=false;
    		for (i=0; (i<r.length && (!registroLibre)) ; i++) {
    			if (r[i].estaLibre()) {
    				registroLibre=true;
   					r[i].setLibre(false);
   				}
   			}
    		this.creacionCodigo(this.ref, codigo, i);
    		}
    
		this.ref = "R"+i;
		this.esRegistro=true;
		this.setEsHoja(true);
		this.left=null;
		this.right=null;
    }
	
	private void creacionCodigo(String r, String codigo, int i) {
		if (r=="+") {
			codigo += ("MOV R"+i+", "+ this.left.getRef());
   			codigo += ("ADD R"+i+", "+ this.right.getRef());
		}
		else if (r=="*") {
			codigo += ("MOV R"+i+", "+ this.left.getRef());
   			codigo += ("ADD R"+i+", "+ this.right.getRef());
		}
	}
}



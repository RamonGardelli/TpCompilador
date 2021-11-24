package com.compilador.arbolSintactico;

import com.compilador.analizadorSintactico.AnalizadorSintactico;
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
    
    public void setRegistro(boolean esRegistro, Registro r[]) {
    	if ((this.esRegistro) && (!esRegistro)) {
    		r[Character.getNumericValue(this.ref.charAt(1))].setLibre(true);
    	}
    	this.esRegistro=esRegistro;
    }

	public void generarCodigo(Registro r[]) {
    	int i=0;
    	if (!(this.right==null)) {
	    	if ((this.left.EsHoja())&&(!this.right.EsHoja())){
	    		this.right.generarCodigo(r);
	    		}
	    	
	    	if ((!this.left.EsHoja())&&(this.right.EsHoja())){
				this.left.generarCodigo(r);
			}
	    	
	    	if ((!this.left.EsHoja())&&(!this.right.EsHoja())){
				this.left.generarCodigo(r);
				this.right.generarCodigo(r);
			}
	    	boolean registroLibre=false;
	    	
	    	for (i=0; (i<r.length && (!registroLibre)) ; i++) {
	    		if (r[i].estaLibre()) {
	    			registroLibre=true;
	   				r[i].setLibre(false);
	   			}
	   		}
	    	
	    	this.creacionCodigo(this.ref, i, r);
	    	this.ref = "R"+i;
			this.esRegistro=true;
			this.setEsHoja(true);
			this.left.setRegistro(false,r);
			this.left=null;
			this.right.setRegistro(false,r);
			this.right=null;
    	}
    	else if (this.right==null) {
			this.left.generarCodigo(r);
    	}
    	
    }
	
	private void creacionCodigo(String r, int i, Registro reg[]) {
		String izquierda = this.left.getRef();
		String derecha = this.right.getRef();
		if (this.left.getRef().contains("@")){
			izquierda = this.left.getRef().split("@")[0];
		}
		
		if (this.right.getRef().contains("@")){
			derecha = this.right.getRef().split("@")[0];
		}
		
		if (r==":=") {
			AnalizadorSintactico.codigoAssembler += ("MOV "+"R"+i+", _"+derecha);
			AnalizadorSintactico.codigoAssembler += ("\n");
			AnalizadorSintactico.codigoAssembler += ("MOV "+izquierda+", _"+"R"+i);
		}
		else
			if (r=="+") { 
				
				AnalizadorSintactico.codigoAssembler += ("MOV R"+i+", _"+ izquierda);
				AnalizadorSintactico.codigoAssembler += ("\n");
				AnalizadorSintactico.codigoAssembler += ("ADD R"+i+", _"+ derecha);
			}
			else 
				if (r=="*") {
					AnalizadorSintactico.codigoAssembler += ("MOV R"+i+", _"+ izquierda);
					AnalizadorSintactico.codigoAssembler += ("\n");
					AnalizadorSintactico.codigoAssembler += ("SUB R"+i+", _"+ derecha);
			}
				else 
					if (r=="-") {
						AnalizadorSintactico.codigoAssembler += ("MOV R"+i+", _"+ izquierda);
						AnalizadorSintactico.codigoAssembler += ("\n");
						AnalizadorSintactico.codigoAssembler += ("SUB R"+i+", _"+ derecha);
					}
					else 
						if (r=="/") {
							AnalizadorSintactico.codigoAssembler += ("MOV R"+i+", _"+ izquierda);
							AnalizadorSintactico.codigoAssembler += ("\n");
							AnalizadorSintactico.codigoAssembler += ("DIV R"+i+", _"+ derecha);
						}
						else 
							if (r=="Then") {
								AnalizadorSintactico.codigoAssembler += ("JLE Label1");
								this.left.generarCodigo(reg);
							}					
							else 
								if (r=="Else") {
									AnalizadorSintactico.codigoAssembler += ("JMP Label2");
									this.left.generarCodigo(reg);
								}
								else 
									if (r=="Cond") {
										this.left.generarCodigo(reg);
									}
		AnalizadorSintactico.codigoAssembler += ("\n");	
	}	
}


package com.compilador.arbolSintactico;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;
import com.compilador.analizadorLexico.accionSemantica.AS5;
import com.compilador.analizadorLexico.accionSemantica.AS7;
import com.compilador.analizadorSintactico.AnalizadorSintactico;
import com.compilador.assembler.Registro;

public class Nodo {
	
    private Nodo left;
    private Nodo right;
    private String ref; //referencia a tabla de simbolo o simplemente operacion/valor aritmetico. Lo determina si es hoja.
    private boolean esHoja; // si es hoja, es una ref a tabla de simbolos.
    private String tipo;
    private boolean esRegistro;
    private float valor;

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
    
    public String getTipoNodo() {
    	if (this.esRegistro)
    		return "R";
    	return (AnalizadorLexico.getLexemaObject(this.right.getRef()).getTipoVariable());
    }
    
    public float getValor() {
        if (!esRegistro) {
        	if ((AnalizadorLexico.getLexemaObject(this.getRef()).getTipoVariable())=="ID") {
        		return AnalizadorLexico.getLexemaObject(this.getRef()).getValorFloat();    
        	}
        	return Float.parseFloat(this.getRef());
        }
		return this.valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public void setRegistro(boolean esRegistro, Registro r[]) {
    	if ((this.esRegistro) && (!esRegistro)) {
    		for (int i = 0; i<r.length; i++) {
    			if (r[i].getNombre()==this.ref) {
    				r[i].setLibre(true);
    			}
    		}
    	}
    	this.esRegistro=esRegistro;
    }

	public void generarCodigo(Registro r[]) {
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
	    	
	    	int i = this.registroLibre(r);
	    	this.creacionCodigo(this.ref, i, r);

    	}
    	else if (this.right==null) {
			this.left.generarCodigo(r);
			if ((this.ref=="Then")||(this.ref=="Else")) {
		    	int i = this.registroLibre(r);
				this.creacionCodigo(this.ref, i, r);
			}
    	}
    }
	
	private void creacionCodigo(String r, int i, Registro reg[]) {			
	    	this.ref = reg[i].getNombre();
			
			String izquierda = this.left.getRef();
			String derecha = this.right.getRef();
			if (this.left.getRef().contains("@")){
				izquierda = this.left.getRef().split("@")[0];
			}
			
			if (this.right.getRef().contains("@")){
				derecha = this.right.getRef().split("@")[0];
			}
			
			if (r==":=") {
				AnalizadorSintactico.codigoAssembler += ("MOV "+reg[i].getNombre()+", _"+derecha);
				AnalizadorSintactico.codigoAssembler += ("\n");
				AnalizadorSintactico.codigoAssembler += ("MOV "+izquierda+", _"+""+reg[i].getNombre());
				TDSObject value = AnalizadorLexico.getLexemaObject(this.left.getRef());
				value.setValor(this.right.getValor());
				AnalizadorLexico.tablaDeSimbolos.put(this.left.getRef(), value);
			}
	
			else
				if (r=="+") { 
					AnalizadorSintactico.codigoAssembler += ("MOV "+reg[i].getNombre()+", _"+ izquierda);
					AnalizadorSintactico.codigoAssembler += ("\n");
					AnalizadorSintactico.codigoAssembler += ("ADD "+reg[i].getNombre()+", _"+ derecha);
					this.valor=(this.left.getValor()+this.right.getValor());
					if (!this.valorPermitido()) {
						//Aqui deberia cortar la ejecucion, enviando un mensaje de error.
					}
				}
				else 
					if (r=="*") {
						AnalizadorSintactico.codigoAssembler += ("MOV "+reg[i].getNombre()+", _"+ izquierda);
						AnalizadorSintactico.codigoAssembler += ("\n");
						AnalizadorSintactico.codigoAssembler += ("SUB "+reg[i].getNombre()+", _"+ derecha);
						this.valor=(this.left.getValor()*this.right.getValor());
						if (!this.valorPermitido()) {
							//Aqui deberia cortar la ejecucion, enviando un mensaje de error.
						}
				}
					else 
						if (r=="-") {
							AnalizadorSintactico.codigoAssembler += ("MOV "+reg[i].getNombre()+", _"+ izquierda);
							AnalizadorSintactico.codigoAssembler += ("\n");
							AnalizadorSintactico.codigoAssembler += ("SUB "+reg[i].getNombre()+", _"+ derecha);
							this.valor=(this.left.getValor()-this.right.getValor());
							if (!this.valorPermitido()) {
							}
						}
						else 
							if (r=="/") {
								if (this.right.getValor()==0) {
									//Aqui deberia cortar la ejecucion, enviando un mensaje de error.
								}
								AnalizadorSintactico.codigoAssembler += ("MOV "+reg[i].getNombre()+", _"+ izquierda);
								AnalizadorSintactico.codigoAssembler += ("\n");
								AnalizadorSintactico.codigoAssembler += ("DIV "+reg[i].getNombre()+", _"+ derecha);
								this.valor=(this.left.getValor()/this.right.getValor());
								
								if (!this.valorPermitido()) {
									//Aqui deberia cortar la ejecucion, enviando un mensaje de error.
								}
							}
							
			AnalizadorSintactico.codigoAssembler += ("\n");	
			this.ref=reg[i].getNombre();
			this.esRegistro=true;
			this.setEsHoja(true);
			this.tipo = this.left.getTipo();
			this.left.setRegistro(false,reg);
			this.left=null;
			this.right.setRegistro(false,reg);
			this.right=null;

		}
	
	private boolean valorPermitido() {
		if (this.tipo == "SINGLE") {
			if ((this.valor>AS5.MAX_FLOAT) || (this.valor<AS5.MIN_FLOAT))
				return false;
		}
		
		if (this.tipo == "LONG")
			if ((this.valor>AS7.MAX_LONG) || (this.valor<AS7.MIN_LONG))
				return false;
		
		return true;
	}
	
	private int registroLibre(Registro[] r) {
		for (int i=0; (i<r.length) ; i++) {
    		if (r[i].estaLibre()) {
   				r[i].setLibre(false);
   				return i;
   			}
   		}
		return -1;
	}
}


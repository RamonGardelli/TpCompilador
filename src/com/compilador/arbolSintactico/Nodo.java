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
    private String tipo;
    private boolean esRegistro;

    public Nodo(String aritmetic, Nodo reglaDer1, Nodo reglaDer2) {
        this.ref = aritmetic;
        this.left = reglaDer1;
        this.right = reglaDer2;
        this.esRegistro = false;
    }

    public boolean isRegistro() {
        return esRegistro;
    }

    public Nodo(String refTDS) {
        this.ref = refTDS;
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
        if ((this.right == null) && (this.left == null)) {
            return true;
        }
        return false;
    }


    public String getTipoNodo() {
        if (this.esRegistro)
            return "R";
        return (AnalizadorLexico.getLexemaObject(this.right.getRef()).getTipoVariable());
    }


    public void setRegistro(boolean Registro, Registro r[]) {
        if ((this.esRegistro) && (!Registro)) {
            for (int i = 0; i < r.length; i++) {
                if (r[i].getNombre() == this.ref) {
                    r[i].setLibre(true);
                }
            }
        }
        this.esRegistro = Registro;
    }

    public void generarCodigo(Registro r[]) {

        if (!(this.right == null)) {
            if (this.ref == "WHILE") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += (aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
            }

            if ((this.left.EsHoja()) && (!this.right.EsHoja())) {
                this.right.generarCodigo(r);
            }

            if ((!this.left.EsHoja()) && (this.right.EsHoja())) {
                this.left.generarCodigo(r);
                
            }

            if ((!this.left.EsHoja()) && (!this.right.EsHoja())) {
                this.left.generarCodigo(r);
                if (this.ref == "S") {
                    this.left.setRegistro(false, r);
                }
                this.right.generarCodigo(r);
            }
            int i = AnalizadorSintactico.contadorAuxLong;
            if ((this.right.getTipo() == "LONG") || (this.left.getTipo() == "LONG")) {
                this.creacionCodigoLong(this.ref, i, r);
            } else if ((this.right.getTipo() == "SINGLE") || (this.left.getTipo() == "SINGLE")) {
                this.creacionCodigoSingle(this.ref, r);
            } else if ((this.ref == "WHILE")) {
                this.creacionCodigoSingle(this.ref, r);
            }
              else if  (this.ref == "LF") {
            	  if (this.getTipo()=="LONG") {
                      this.creacionCodigoLong(this.ref, i, r);
            	  }
            	  else if (this.getTipo()=="SINGLE")
                      this.creacionCodigoSingle(this.ref, r);
              }
            

        } else if (this.right == null) {
        	if (this.ref == "PRINT") {
 		        String aux = left.ref.replace(" ", "_");
        		if (this.left.getTipo()=="CADENA") {
      		       	aux = aux.replace("	", "_");
        			if(aux.charAt(0) != '_'){
                        aux = "_"+ aux;
                    }
                    AnalizadorSintactico.codigoAssembler += ("invoke MessageBox, NULL, addr " +"cad"+ aux + ", addr " + "cad" + aux + ", MB_OK"+"\n");
        		}
        		else {
                     AnalizadorSintactico.codigoAssembler += ("invoke MessageBox, NULL, addr _" + aux + ", addr _" + aux + ", MB_OK"+"\n");
        		}
            } 
        	
            if (this.getRef() == "Else") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.codigoAssembler += ("JMP " + aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += (AnalizadorSintactico.pilaLabels.pop());
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                this.left.generarCodigo(r);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += (AnalizadorSintactico.pilaLabels.pop());
            } else {
                if (this.getLeft() != null) {
                    if (this.getLeft().getRef() == "Then") {
                        this.left.generarCodigo(r);
                        AnalizadorSintactico.codigoAssembler += (AnalizadorSintactico.pilaLabels.pop());
                    } else if (this.getLeft().getRef() == "BF") {
                        this.left.getLeft().generarCodigo(r);
                        if (this.getLeft().getRight() != null) {
                            this.left.getRight().generarCodigo(r);
                            int aux = AnalizadorSintactico.flagsFunc.get(this.getRef());
                            
                            if (this.getLeft().getRight().getTipo()=="LONG") {
                            	
                            	if (this.getLeft().getRight().isRegistro()){
                            		
                                    AnalizadorSintactico.codigoAssembler += ("MOV _retFunc_" + aux + ", " + this.getLeft().getRight().getRef());
                                    AnalizadorSintactico.codigoAssembler += ("\n");
                                }else{
                                	int j = this.registroLibre(r);
                                	String auxValor = this.getLeft().getRight().getRef();
                                	auxValor=auxValor.replace(".", "_");
                                    AnalizadorSintactico.codigoAssembler += ("MOV " + r[j].getNombre() + ", _" + auxValor);
                                    AnalizadorSintactico.codigoAssembler += ("\n");
                                    AnalizadorSintactico.codigoAssembler += ("MOV _retFunc_" + aux + ", " + r[j].getNombre());
                                    AnalizadorSintactico.codigoAssembler += ("\n");
                                    r[j].setLibre(true);
                                }}
                            else if  (this.getLeft().getRight().getTipo()=="SINGLE") {
                            	String auxValor = this.getLeft().getRight().getRef();
                            	auxValor=auxValor.replace(".", "_");
                                AnalizadorSintactico.codigoAssembler += ("FLD _" + auxValor);
                                AnalizadorSintactico.codigoAssembler += ("\n");
                                AnalizadorSintactico.codigoAssembler += ("FSTP _retFunc_" + aux);
                                AnalizadorSintactico.codigoAssembler += ("\n");

                            }
                        }
                    } else {
                        this.left.generarCodigo(r);
                    }
                }
            }
        }
    }

    private void creacionCodigoLong(String r, int i, Registro reg[]) {
        String derecha = this.right.getRef();
        String izquierda = this.left.getRef();

        if (this.ref == "LF") {
            AnalizadorSintactico.codigoAssembler += ("CALL " + this.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler += "\n";
        }
        
        if (!this.left.isRegistro()) {
            izquierda = "_" + izquierda;
        }

        if (!this.right.isRegistro()) {
            derecha = "_" + derecha;
        }

        izquierda=izquierda.replace("-", "_");
        derecha=derecha.replace("-", "_");

        int j = this.registroLibre(reg);
        
        if (r == ":=") {
            AnalizadorSintactico.codigoAssembler += ("MOV " + reg[j].getNombre() + ", " + derecha);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("MOV " + izquierda + ", " + reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler += ("\n");

            
        } else if (r == "+") {
        	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("MOV " + reg[j].getNombre() + ", " + izquierda);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("ADD " + reg[j].getNombre() +", "+ derecha);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("MOV " + "@auxLong" + i+", " + reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler += ("\n");

        } else if (r == "*") {
           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("MOV " + reg[j].getNombre() + ", " + izquierda);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("IMUL " + reg[j].getNombre() + ", " + derecha);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("MOV " + "@auxLong" + i+", " + reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("JO @LABEL_OVF");  
            AnalizadorSintactico.codigoAssembler += ("\n");

        } else if (r == "-") {
           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("MOV " + reg[j].getNombre() + ", " + izquierda);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("SUB " + reg[j].getNombre() + ", "+derecha);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("MOV " + "@auxLong" + i+", " + reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler += ("\n");

        } else if (r == "/") {
           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
        	AnalizadorSintactico.codigoAssembler += ("MOV EAX" + ", " + izquierda);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("IDIV " + derecha);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("MOV " + "@auxLong" + i + ", EAX");
            AnalizadorSintactico.codigoAssembler += ("\n");

        } else if ((r == "==") || (r == ">=") || (r == "<=") || (r == "<>") || (r == ">") || (r == "<")) {
        	
            AnalizadorSintactico.codigoAssembler += ("MOV " + reg[j].getNombre() + ", " + izquierda);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("CMP " + reg[j].getNombre() + ", " + derecha);
            AnalizadorSintactico.codigoAssembler += ("\n");

            if (r == "==") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += ("JNE " + aux);
                AnalizadorSintactico.codigoAssembler += ("\n");

            }
            if (r == ">=") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += ("JL " + aux);
                AnalizadorSintactico.codigoAssembler += ("\n");

            }

            if (r == "<=") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += ("JG " + aux);
                AnalizadorSintactico.codigoAssembler += ("\n");

            }
            if (r == "<>") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += ("JE " + aux);
                AnalizadorSintactico.codigoAssembler += ("\n");

            }

            if (r == ">") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += ("JBE " + aux);
                AnalizadorSintactico.codigoAssembler += ("\n");

            }

            if (r == "<") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += ("JGE " + aux);
                AnalizadorSintactico.codigoAssembler += ("\n");

            }
        } else if (r == "WHILE") {
            String aux1 = (AnalizadorSintactico.pilaLabels.pop());
            String aux2 = (AnalizadorSintactico.pilaLabels.pop());
            AnalizadorSintactico.codigoAssembler += ("JMP " + aux2);
            AnalizadorSintactico.codigoAssembler += ("\n");

            AnalizadorSintactico.codigoAssembler += (aux1);
            AnalizadorSintactico.codigoAssembler += ("\n");

            
        }
  
        this.ref = "@auxLong"+i;
        this.esRegistro = true;
        this.tipo = this.left.getTipo();
        reg[j].setLibre(true);
        this.left = null;
        this.right = null;
    }

    private void creacionCodigoSingle(String r, Registro reg[]) {
        String izquierda = this.left.getRef();
        String derecha = this.right.getRef();
        
        
        if (derecha == "LF") {
            AnalizadorSintactico.codigoAssembler += ("CALL " + this.right.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler += "\n";
        }
        if (izquierda == "LF") {
            AnalizadorSintactico.codigoAssembler += ("CALL " + this.left.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler += "\n";
        }
        

        if (!this.left.isRegistro()) {
            izquierda = "_" + izquierda;
        }

        if (!this.right.isRegistro()) {
            derecha = "_" + derecha;
        }
        if (derecha.charAt(0) == '.')
            derecha = "0" + derecha;

        if (izquierda.charAt(0) == '.')
            izquierda = "0" + izquierda;
        
        izquierda=izquierda.replace(".", "_");
        derecha=derecha.replace(".", "_");

        izquierda=izquierda.replace("-", "_");
        derecha=derecha.replace("-", "_");


        if (r == ":=") {
            this.imprimirAsignacion(derecha, izquierda);
        } else if (r == "+") {
            this.imprimirSuma(derecha, izquierda);
        } else if (r == "*") {
            this.imprimirMultiplicacion(derecha, izquierda);
        } else if (r == "-") {
            this.imprimirResta(derecha, izquierda);
        } else if (r == "/") {
            this.imprimirDivision(derecha, izquierda);
        } else if ((r == "==") || (r == ">=") || (r == "<=") || (r == "<>") || (r == ">") || (r == "<")) {
            AnalizadorSintactico.codigoAssembler += ("FCOM ");
            AnalizadorSintactico.codigoAssembler += ("\n");

            if (r == "==") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("JNE " + aux);
            }
            if (r == ">=") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("JL " + aux);
            }

            if (r == "<=") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("JG " + aux);
            }
            if (r == "<>") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += ("JE " + aux);
            }

            if (r == ">") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += ("JBE " + aux);
            }

            if (r == "<") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label " + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler += ("\n");
                AnalizadorSintactico.codigoAssembler += ("JGE " + aux);
            }


        } else if (r == "WHILE") {
            String aux1 = (AnalizadorSintactico.pilaLabels.pop());
            String aux2 = (AnalizadorSintactico.pilaLabels.pop());
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("JMP " + aux2);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += (aux1);
        } 

        
        this.ref = "@auxSingle"+AnalizadorSintactico.contadorAuxSingle;
        this.esRegistro = true;
        this.tipo = this.left.getTipo();
        this.left = null;
        this.right = null;
    }

    private int registroLibre(Registro[] r) {
        for (int i = 0; (i < r.length); i++) {
            if (r[i].estaLibre()) {
                r[i].setLibre(false);
                return i;
            }
        }
        return -1;
    }

    private void imprimirAsignacion(String derecha, String izquierda) {
            AnalizadorSintactico.codigoAssembler += ("FLD " + derecha);
            AnalizadorSintactico.codigoAssembler += ("\n");
            AnalizadorSintactico.codigoAssembler += ("FSTP " + izquierda);
            AnalizadorSintactico.codigoAssembler += ("\n");

    }

    private void imprimirSuma(String derecha, String izquierda) {
    	AnalizadorSintactico.contadorAuxSingle++;
    	int i=AnalizadorSintactico.contadorAuxSingle;
        AnalizadorSintactico.codigoAssembler += ("FLD " + izquierda);
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FLD " + derecha);
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FADD");
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FSTP @auxSingle"+i);
        AnalizadorSintactico.codigoAssembler += ("\n");

    }

    private void imprimirResta(String derecha, String izquierda) {
    	AnalizadorSintactico.contadorAuxSingle++;
    	int i=AnalizadorSintactico.contadorAuxSingle;  
        AnalizadorSintactico.codigoAssembler += ("FLD  " + izquierda);
        AnalizadorSintactico.codigoAssembler += ("FLD " + derecha);
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FSUB"); 
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FSTP @auxSingle"+i);
        AnalizadorSintactico.codigoAssembler += ("\n");

    }

    private void imprimirMultiplicacion(String derecha, String izquierda) {
    	AnalizadorSintactico.contadorAuxSingle++;
    	int i=AnalizadorSintactico.contadorAuxSingle;  
        AnalizadorSintactico.codigoAssembler += ("FLD " + izquierda);
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FLD " + derecha);
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FMUL");
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FSTP @auxSingle"+i);
        AnalizadorSintactico.codigoAssembler += ("\n");

    }

    private void imprimirDivision(String derecha, String izquierda) {
    	AnalizadorSintactico.contadorAuxSingle++;
    	int i=AnalizadorSintactico.contadorAuxSingle;
        AnalizadorSintactico.codigoAssembler += ("FLD " + izquierda);
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FLD " + derecha);
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FTST");
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FSTSW aux_mem_2bytes");
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("MOV AX , aux_mem_2bytes");
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("SAHF");
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("JE @LABEL_DIVCERO");
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FDIV");    
        AnalizadorSintactico.codigoAssembler += ("\n");
        AnalizadorSintactico.codigoAssembler += ("FSTP @auxSingle"+i);
        
    }


}


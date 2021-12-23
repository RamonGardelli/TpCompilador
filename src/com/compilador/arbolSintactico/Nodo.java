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
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
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
                    int id = AnalizadorSintactico.idCadenas.get(aux);
                    AnalizadorSintactico.codigoAssembler.append("invoke MessageBox, NULL, addr ").append("cad_").append(id).append(", addr ").append("cad_").append(id).append(", MB_OK\n");
        		}
        		else if (this.left.getTipo().equals("ID-SINGLE")  || this.left.getTipo().equals("CTE-SINGLE") ){
                     AnalizadorSintactico.codigoAssembler.append("invoke printf, cfm$(\"%.20Lf\\n\"), _").append(aux).append("\n");
        		}else{
                    AnalizadorSintactico.codigoAssembler.append("invoke printf, cfm$(\"%d\\n\"), _").append(aux).append("\n");
                }
            } 
        	
            if (this.getRef() == "Else") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.codigoAssembler.append("JMP " + aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(AnalizadorSintactico.pilaLabels.pop()).append(":");
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                this.left.generarCodigo(r);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(AnalizadorSintactico.pilaLabels.pop()).append(":");
            } else {
                if (this.getLeft() != null) {
                    if (this.getLeft().getRef() == "Then") {
                        this.left.generarCodigo(r);
                        AnalizadorSintactico.codigoAssembler.append(AnalizadorSintactico.pilaLabels.pop()).append(":");
                    } else if (this.getLeft().getRef() == "BF") {
                        	this.left.getLeft().generarCodigo(r);
                        if (this.getLeft().getRight() != null) {
                            this.left.getRight().generarCodigo(r);
                            int aux = AnalizadorSintactico.flagsFunc.get(this.getRef());
                            
                            if (this.getLeft().getRight().getTipo()=="LONG") {
                            	
                            	if (this.getLeft().getRight().isRegistro()){
                            		
                                    AnalizadorSintactico.codigoAssembler.append("MOV _retFunc_").append(aux).append(", ").append(this.getLeft().getRight().getRef());
                                    AnalizadorSintactico.codigoAssembler.append("\n");
                                }else{
                                	int j = this.registroLibre(r);
                                	String auxValor = this.getLeft().getRight().getRef();
                                	auxValor=auxValor.replace(".", "_");
                                    AnalizadorSintactico.codigoAssembler.append("MOV ").append(r[j].getNombre()).append(", _").append(auxValor);
                                    AnalizadorSintactico.codigoAssembler.append("\n");
                                    AnalizadorSintactico.codigoAssembler.append("MOV _retFunc_").append(aux).append(", ").append(r[j].getNombre());
                                    AnalizadorSintactico.codigoAssembler.append("\n");
                                    r[j].setLibre(true);
                                }}
                            else if  (this.getLeft().getRight().getTipo()=="SINGLE") {
                            	String auxValor = this.getLeft().getRight().getRef();
                            	auxValor=auxValor.replace(".", "_");
                                AnalizadorSintactico.codigoAssembler.append("FLD _").append(auxValor);
                                AnalizadorSintactico.codigoAssembler.append("\n");
                                AnalizadorSintactico.codigoAssembler.append("FSTP _retFunc_").append(aux);
                                AnalizadorSintactico.codigoAssembler.append("\n");

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
            String parametroName = (AnalizadorLexico.tablaDeSimbolos.get(this.getLeft().getRef())).getNombreParametro();
            AnalizadorSintactico.codigoAssembler.append("MOV EAX").append(", _").append(this.getRight().getRef());
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV _").append(parametroName).append(", ").append("EAX");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CALL ").append(this.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler.append("\n");
            int aux = AnalizadorSintactico.flagsFunc.get(this.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler.append("MOV EAX").append(", ").append("_retFunc_").append(aux);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV _").append(this.getRight().getRef()).append(", ").append("EAX");
            AnalizadorSintactico.codigoAssembler.append("\n");
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
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(reg[j].getNombre()).append(", ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(izquierda).append(", ").append(reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler.append("\n");

            
        } else if (r == "+") {
        	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(reg[j].getNombre()).append(", ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("ADD ").append(reg[j].getNombre()).append(", ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxLong").append(i).append(", ").append(reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler.append("\n");

        } else if (r == "*") {
           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(reg[j].getNombre()).append(", ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("IMUL ").append(reg[j].getNombre()).append(", ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxLong").append(i).append(", ").append(reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JO @LABEL_OVF");
            AnalizadorSintactico.codigoAssembler.append("\n");

        } else if (r == "-") {
           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(reg[j].getNombre()).append(", ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("SUB ").append(reg[j].getNombre()).append(", ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxLong").append(i).append(", ").append(reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler.append("\n");

        } else if (r == "/") {
           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
        	AnalizadorSintactico.codigoAssembler.append("MOV EAX, ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler .append("CMP ").append(derecha).append(", 0");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JE @LABEL_DIVCERO");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CDQ");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("IDIV ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxLong").append(i).append(", EAX");
            AnalizadorSintactico.codigoAssembler.append("\n");

        } else if ((r == "==") || (r == ">=") || (r == "<=") || (r == "<>") || (r == ">") || (r == "<")) {
        	
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(reg[j].getNombre()).append(", ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CMP ").append(reg[j].getNombre()).append(", ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");

           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            
            if (r == "==") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JNE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }
            if (r == ">=") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JL ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }

            if (r == "<=") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JG ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }
            if (r == "<>") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }

            if (r == ">") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JLE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }

            if (r == "<") {
               	AnalizadorSintactico.contadorAuxLong++;
                i = AnalizadorSintactico.contadorAuxLong;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JGE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }
        } else if (r == "WHILE") {
            String aux1 = (AnalizadorSintactico.pilaLabels.pop());
            String aux2 = (AnalizadorSintactico.pilaLabels.pop());
            AnalizadorSintactico.codigoAssembler.append("JMP ").append(aux2+":");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux1);
            AnalizadorSintactico.codigoAssembler.append("\n");
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
        
        if (this.ref == "LF") {
            String parametroName = (AnalizadorLexico.tablaDeSimbolos.get(this.getLeft().getRef())).getNombreParametro();
            AnalizadorSintactico.codigoAssembler.append("MOV EAX").append(", _").append(this.getRight().getRef());
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV _").append(parametroName).append(", ").append("EAX");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CALL ").append(this.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler.append("\n");
            int aux = AnalizadorSintactico.flagsFunc.get(this.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler.append("MOV EAX").append(", ").append("_retFunc_").append(aux);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV _").append(this.getRight().getRef()).append(", ").append("EAX");
            AnalizadorSintactico.codigoAssembler.append("\n");
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
            AnalizadorSintactico.codigoAssembler.append("FLD ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("FLD ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("FCOM ");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("FSTSW aux_mem_2bytes");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV AX , aux_mem_2bytes");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("SAHF");
            AnalizadorSintactico.codigoAssembler.append("\n");

            if (r == "==") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("JNE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }
            if (r == ">=") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("JB ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }

            if (r == "<=") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("JA ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }
            if (r == "<>") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux+":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }

            if (r == ">") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JBE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }

            if (r == "<") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JAE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");

            }


        } else if (r == "WHILE") {
            String aux1 = (AnalizadorSintactico.pilaLabels.pop());
            String aux2 = (AnalizadorSintactico.pilaLabels.pop());
            aux2.replace(":", " ");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JMP ").append(aux2+":");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux1);
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
            AnalizadorSintactico.codigoAssembler.append("FLD ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("FSTP ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");

    }

    private void imprimirSuma(String derecha, String izquierda) {
    	AnalizadorSintactico.contadorAuxSingle++;
    	int i=AnalizadorSintactico.contadorAuxSingle;
        AnalizadorSintactico.codigoAssembler.append("FLD ").append(izquierda);
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FLD ").append(derecha);
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FADD");
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FSTP @auxSingle").append(i);
        AnalizadorSintactico.codigoAssembler.append("\n");

    }

    private void imprimirResta(String derecha, String izquierda) {
    	AnalizadorSintactico.contadorAuxSingle++;
    	int i=AnalizadorSintactico.contadorAuxSingle;  
        AnalizadorSintactico.codigoAssembler.append("FLD  ").append(izquierda);
        AnalizadorSintactico.codigoAssembler.append("FLD ").append(derecha);
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FSUB"); 
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FSTP @auxSingle").append(i);
        AnalizadorSintactico.codigoAssembler.append("\n");

    }

    private void imprimirMultiplicacion(String derecha, String izquierda) {
    	AnalizadorSintactico.contadorAuxSingle++;
    	int i=AnalizadorSintactico.contadorAuxSingle;  
        AnalizadorSintactico.codigoAssembler.append("FLD ").append(izquierda);
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FLD ").append(derecha);
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FMUL");
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FSTP @auxSingle").append(i);
        AnalizadorSintactico.codigoAssembler.append("\n");

    }

    private void imprimirDivision(String derecha, String izquierda) {
    	AnalizadorSintactico.contadorAuxSingle++;
    	int i=AnalizadorSintactico.contadorAuxSingle;
        AnalizadorSintactico.codigoAssembler.append("FLD ").append(izquierda);
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FLD ").append(derecha);
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FTST");
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FSTSW aux_mem_2bytes");
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("MOV AX , aux_mem_2bytes");
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("SAHF");
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("JE @LABEL_DIVCERO");
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FDIV");
        AnalizadorSintactico.codigoAssembler.append("\n");
        AnalizadorSintactico.codigoAssembler.append("FSTP @auxSingle").append(i);
        AnalizadorSintactico.codigoAssembler.append("\n");    
    }

    /*    
    public void generarCodigoAndOr(Registro r[]) {

        if (!(this.right == null)) {
            if (this.ref == "WHILE") {
                AnalizadorSintactico.contadorLabel++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabel;
                AnalizadorSintactico.pilaLabels.push(aux);
                AnalizadorSintactico.codigoAssembler.append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
            }

            if ((this.left.EsHoja()) && (!this.right.EsHoja())) {
                this.right.generarCodigoAndOr(r);
            }

            if ((!this.left.EsHoja()) && (this.right.EsHoja())) {
                this.left.generarCodigoAndOr(r);
                
            }

            if ((!this.left.EsHoja()) && (!this.right.EsHoja())) {
                this.left.generarCodigoAndOr(r);
                if (this.ref == "S") {
                    this.left.setRegistro(false, r);
                }
                this.right.generarCodigoAndOr(r);
            }
            int i = AnalizadorSintactico.contadorAuxLong;
            if ((this.right.getTipo() == "LONG") || (this.left.getTipo() == "LONG")) {
                this.creacionCodigoLongAndOr(this.ref, i, r);
            } else if ((this.right.getTipo() == "SINGLE") || (this.left.getTipo() == "SINGLE")) {
                this.creacionCodigoSingleAndOr(this.ref, r);
            } else if ((this.ref == "WHILE")) {
                this.creacionCodigoSingleAndOr(this.ref, r);
            }
        }
    }
    
    private void creacionCodigoLongAndOr(String r, int i, Registro reg[]) {
        String derecha = this.right.getRef();
        String izquierda = this.left.getRef();

        if (this.ref == "LF") {
            String parametroName = (AnalizadorLexico.tablaDeSimbolos.get(this.getLeft().getRef())).getNombreParametro();
            AnalizadorSintactico.codigoAssembler.append("MOV EAX").append(", _").append(this.getRight().getRef());
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV _").append(parametroName).append(", ").append("EAX");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CALL ").append(this.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler.append("\n");
            int aux = AnalizadorSintactico.flagsFunc.get(this.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler.append("MOV EAX").append(", ").append("_retFunc_").append(aux);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV _").append(this.getRight().getRef()).append(", ").append("EAX");
            AnalizadorSintactico.codigoAssembler.append("\n");
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
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(reg[j].getNombre()).append(", ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(izquierda).append(", ").append(reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler.append("\n");

            
        } else if (r == "+") {
        	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(reg[j].getNombre()).append(", ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("ADD ").append(reg[j].getNombre()).append(", ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxLong").append(i).append(", ").append(reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler.append("\n");

        } else if (r == "*") {
           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(reg[j].getNombre()).append(", ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("IMUL ").append(reg[j].getNombre()).append(", ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxLong").append(i).append(", ").append(reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JO @LABEL_OVF");
            AnalizadorSintactico.codigoAssembler.append("\n");

        } else if (r == "-") {
           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(reg[j].getNombre()).append(", ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("SUB ").append(reg[j].getNombre()).append(", ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxLong").append(i).append(", ").append(reg[j].getNombre());
            AnalizadorSintactico.codigoAssembler.append("\n");

        } else if (r == "/") {
           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
        	AnalizadorSintactico.codigoAssembler.append("MOV EAX, ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler .append("CMP ").append(derecha).append(", 0");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JE @LABEL_DIVCERO");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CDQ");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("IDIV ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxLong").append(i).append(", EAX");
            AnalizadorSintactico.codigoAssembler.append("\n");

        } else if ((r == "==") || (r == ">=") || (r == "<=") || (r == "<>") || (r == ">") || (r == "<")) {
        	
            AnalizadorSintactico.codigoAssembler.append("MOV ").append(reg[j].getNombre()).append(", ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CMP ").append(reg[j].getNombre()).append(", ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");

           	AnalizadorSintactico.contadorAuxLong++;
            i = AnalizadorSintactico.contadorAuxLong;
            
            if (r == "==") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("JNE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxLong+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxLong).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");
            }
            if (r == ">=") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("JB ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxLong+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxLong).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

            }

            if (r == "<=") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("JA ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxLong+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxLong).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

            }
            if (r == "<>") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxLong+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxLong).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

            }

            if (r == ">") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JBE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxLong+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxLong).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

            }

            if (r == "<") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JAE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxLong+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxLong).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");
            }


        } else if (r == "WHILE") {
            String aux1 = (AnalizadorSintactico.pilaLabels.pop());
            String aux2 = (AnalizadorSintactico.pilaLabels.pop());
            aux2.replace(":", " ");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JMP ").append(aux2+":");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux1);
        } 
        
        if (r == "&&") {
            AnalizadorSintactico.contadorLabelAndOr++;
            String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
            AnalizadorSintactico.contadorLabelAndOr++;
            String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CMP ").append(izquierda).append(", 0");
            AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
            AnalizadorSintactico.codigoAssembler.append("CMP ").append(derecha).append(", 0");
            AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxLong+", 1");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux).append(":");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxLong).append(", 0");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

        }
        
        if (r == "||") {
            AnalizadorSintactico.contadorLabelAndOr++;
            String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
            AnalizadorSintactico.contadorLabelAndOr++;
            String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CMP ").append(izquierda).append(", 1");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CMP ").append(derecha).append(", 1");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxLong+", 0");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux).append(":");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxLong).append(", 1");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

        }
        
        this.ref = "@auxLong"+i;
        this.esRegistro = true;
        this.tipo = this.left.getTipo();
        reg[j].setLibre(true);
        this.left = null;
        this.right = null;
    }

    private void creacionCodigoSingleAndOr(String r, Registro reg[]) {
        String izquierda = this.left.getRef();
        String derecha = this.right.getRef();
        
        if (this.ref == "LF") {
            String parametroName = (AnalizadorLexico.tablaDeSimbolos.get(this.getLeft().getRef())).getNombreParametro();
            AnalizadorSintactico.codigoAssembler.append("MOV EAX").append(", _").append(this.getRight().getRef());
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV _").append(parametroName).append(", ").append("EAX");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CALL ").append(this.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler.append("\n");
            int aux = AnalizadorSintactico.flagsFunc.get(this.getLeft().getRef());
            AnalizadorSintactico.codigoAssembler.append("MOV EAX").append(", ").append("_retFunc_").append(aux);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV _").append(this.getRight().getRef()).append(", ").append("EAX");
            AnalizadorSintactico.codigoAssembler.append("\n");
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
        } else if ((r == "==") || (r == ">=") || (r == "<=") || (r == "<>") || (r == ">") || (r == "<")|| (r == "||")|| (r == "&&")) {
            AnalizadorSintactico.codigoAssembler.append("FLD ").append(derecha);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("FLD ").append(izquierda);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("FCOM ");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("FSTSW aux_mem_2bytes");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV AX , aux_mem_2bytes");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("SAHF");
            AnalizadorSintactico.codigoAssembler.append("\n");
           	AnalizadorSintactico.contadorAuxSingle++;

            if (r == "==") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabel++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("JNE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxSingle+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxSingle).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");
            }
            if (r == ">=") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabel++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("JB ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxSingle+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxSingle).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

            }

            if (r == "<=") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabel++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("JA ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxSingle+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxSingle).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

            }
            if (r == "<>") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxSingle+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxSingle).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

            }

            if (r == ">") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JBE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxSingle+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxSingle).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

            }

            if (r == "<") {
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.contadorLabelAndOr++;
                String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JAE ").append(aux);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxSingle+", 1");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux).append(":");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxSingle).append(", 0");
                AnalizadorSintactico.codigoAssembler.append("\n");
                AnalizadorSintactico.codigoAssembler.append(aux2).append(":");
            }


        } else if (r == "WHILE") {
            String aux1 = (AnalizadorSintactico.pilaLabels.pop());
            String aux2 = (AnalizadorSintactico.pilaLabels.pop());
            aux2.replace(":", " ");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JMP ").append(aux2+":");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux1);
        } 
        
        if (r == "&&") {
            AnalizadorSintactico.contadorLabelAndOr++;
            String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
            AnalizadorSintactico.contadorLabelAndOr++;
            String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CMP ").append(izquierda).append(", 0");
            AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
            AnalizadorSintactico.codigoAssembler.append("CMP ").append(derecha).append(", 0");
            AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxSingle+", 1");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux).append(":");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxSingle).append(", 0");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

        }
        
        if (r == "||") {
            AnalizadorSintactico.contadorLabelAndOr++;
            String aux = "@Label_" + AnalizadorSintactico.contadorLabelAndOr;
            AnalizadorSintactico.contadorLabelAndOr++;
            String aux2="@Label_" + AnalizadorSintactico.contadorLabelAndOr;
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CMP ").append(izquierda).append(", 1");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("CMP ").append(derecha).append(", 1");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JE ").append(aux);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle"+AnalizadorSintactico.contadorAuxSingle+", 0");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("JMP").append(aux2);
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux).append(":");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append("MOV ").append("@auxSingle").append(AnalizadorSintactico.contadorAuxSingle).append(", 1");
            AnalizadorSintactico.codigoAssembler.append("\n");
            AnalizadorSintactico.codigoAssembler.append(aux2).append(":");

        }
        
        this.ref = "@auxSingle"+AnalizadorSintactico.contadorAuxSingle;
        this.esRegistro = true;
        this.tipo = this.left.getTipo();
        this.left = null;
        this.right = null;
    }
*/
}


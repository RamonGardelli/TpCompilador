%{

package com.compilador.analizadorSintactico;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorLexico.TDSObject;
import com.compilador.analizadorSintactico.AnalizadorSintactico;
import com.compilador.arbolSintactico.Nodo;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;



%}

%token ELSE THEN ENDIF PRINT FUNC RETURN WHILE LONG BEGIN END SINGLE BREAK DO CADENA ID CTE IGUAL DISTINTO MAYORIG MENORIG CONTRACT TRY CATCH IF AND OR ASIGN


%start programa

%%

programa:ID bloqueDeclarativo bloqueEjecutable {AnalizadorSintactico.agregarAnalisis("Programa reconocido. (Linea " + AnalizadorLexico.numLinea + ")");
			$$= new ParserVal(new Nodo($1.sval, null, ((Nodo)$3.obj)));
			AnalizadorSintactico.arbol = (Nodo)$$.obj;}
	;

bloqueDeclarativo:bloqueDeclarativo sentenciasDeclarativas 
			{
			   if ($2.obj != null)
			   { if ($1.obj != null)
				 {$$= new ParserVal(new Nodo("Func", (Nodo)$2.obj, (Nodo)$1.obj));}
			     else {$$= new ParserVal(new Nodo("Func", (Nodo)$2.obj, null));}
			     AnalizadorSintactico.arbolFunc = (Nodo)$$.obj;
			   }
			   
			}
		 | sentenciasDeclarativas {if ($1.obj != null){
						$$= new ParserVal(new Nodo("Func", (Nodo)$1.obj, null));
						AnalizadorSintactico.arbolFunc = (Nodo)$$.obj;}}
		 ;



bloqueEjecutable :bloqueTRYCATCH {$$=$1;}
				 |bloqueEjecutableNormal {$$=$1;}
				 ;

bloqueTRYCATCH: TRY sentEjecutables CATCH BEGIN sentSoloEjecutables END {$$= new ParserVal (new Nodo("TC", (Nodo)$2.obj, (Nodo)$5.obj));}
	      | sentEjecutables CATCH BEGIN sentSoloEjecutables END {AnalizadorSintactico.agregarError("error: falta TRY en (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY CATCH BEGIN sentSoloEjecutables END {AnalizadorSintactico.agregarError("error TRY-CATCH vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY sentEjecutables BEGIN sentSoloEjecutables END {AnalizadorSintactico.agregarError("error falta CATCH (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY sentEjecutables CATCH sentSoloEjecutables END {AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY sentEjecutables CATCH BEGIN sentSoloEjecutables error {AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
	      ;
		  
TRYCATCHFunc: BEGIN bloqueTRYCATCH RETURN '(' retorno ')' ';' END {ParserVal aux= new ParserVal (new Nodo("S", (Nodo)$2.obj, null));
																	$$= new ParserVal(new Nodo("BF", (Nodo)aux.obj, (Nodo)$5.obj));}
			| BEGIN sentEjecutableFunc bloqueTRYCATCH RETURN '(' retorno ')' ';' END {ParserVal aux= new ParserVal (new Nodo("S", (Nodo)$2.obj, (Nodo)$3.obj));
																					  $$= new ParserVal(new Nodo("BF", (Nodo)aux.obj, (Nodo)$6.obj));}
			| BEGIN sentEjecutableFunc bloqueTRYCATCH sentEjecutableFunc RETURN '(' retorno ')' ';' END {ParserVal aux= new ParserVal (new Nodo("S", (Nodo)$2.obj, (Nodo)$3.obj));
																										 ParserVal aux2= new ParserVal (new Nodo("S", (Nodo)aux.obj, (Nodo)$4.obj));
																										 $$= new ParserVal(new Nodo("BF", (Nodo)aux.obj, (Nodo)$7.obj));}
			| BEGIN bloqueTRYCATCH sentEjecutableFunc RETURN '(' retorno ')' ';' END {ParserVal aux= new ParserVal (new Nodo("S", (Nodo)$2.obj, (Nodo)$3.obj));
																					  $$= new ParserVal(new Nodo("BF", (Nodo)aux.obj, (Nodo)$6.obj));}
			;
			
bloqueEjecutableNormal: BEGIN sentSoloEjecutables END 
			{
			    $$=$2;
			}
		      | sentSoloEjecutables END {AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
		      | BEGIN sentSoloEjecutables error {AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
		      ;


bloqueEjecutableFunc: BEGIN sentEjecutableFunc RETURN '(' retorno ')' ';' END
            {
                $$ = new ParserVal(new Nodo("BF",(Nodo)$2.obj,(Nodo)$5.obj));
            }
			|TRYCATCHFunc {$$=$1;}
		    | sentEjecutableFunc RETURN '(' retorno ')' END {AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc '(' retorno ')' END {AnalizadorSintactico.agregarError("error falta RETURN (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc RETURN  retorno ')' END {AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc RETURN '(' ')' END {AnalizadorSintactico.agregarError("error falta retorno (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc RETURN '(' retorno END {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
		    ;



sentEjecutableFunc: sentEjecutableFunc sentEjecutables
				{ if (($1.obj != null) && ($2.obj != null))
				 	{$$= new ParserVal(new Nodo("S", (Nodo)$2.obj, (Nodo)$1.obj));}
				  else if(($1.obj == null)) {$$= new ParserVal(new Nodo("S", (Nodo)$2.obj, null));}
				       else if (($2.obj == null)) {$$= new ParserVal(new Nodo("S", (Nodo)$1.obj, null));}
				}
		  | sentEjecutableFunc sentenciaCONTRACT
				{ if (($1.obj != null) && ($2.obj != null))
				 	{$$= new ParserVal(new Nodo("S", (Nodo)$2.obj, (Nodo)$1.obj));}
				  else if(($1.obj == null)) {$$= new ParserVal(new Nodo("S", (Nodo)$2.obj, null));}
				       else if (($2.obj == null)) {$$= new ParserVal(new Nodo("S", (Nodo)$1.obj, null));}
				}
		  | sentEjecutables
		  		{
          		  $$= new ParserVal(new Nodo("S",((Nodo)$1.obj), null));
          		}
		  ;

sentenciaCONTRACT: CONTRACT ':' '(' condicion ')' ';' {AnalizadorSintactico.agregarAnalisis("sent control (Linea " + AnalizadorLexico.numLinea + ")");
            $$ =  new ParserVal(new Nodo("CONTRACT",(Nodo)$4.obj,null));
            }
		 | CONTRACT '(' condicion ')' ';'{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':' condicion ')' ';'{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':' '('')' ';'{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':''(' condicion ';' {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':''(' condicion ')' error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
		 ;



sentSoloEjecutables : sentSoloEjecutables sentEjecutables   
				{ if (($1.obj != null) && ($2.obj != null))
				 	{$$= new ParserVal(new Nodo("S", (Nodo)$2.obj, (Nodo)$1.obj));}
				  else if(($1.obj == null)) {$$= new ParserVal(new Nodo("S", (Nodo)$2.obj, null));}
				       else if (($2.obj == null)) {$$= new ParserVal(new Nodo("S", (Nodo)$1.obj, null));}
				}
		    | sentEjecutables	
				{
				    $$= new ParserVal(new Nodo("S",((Nodo)$1.obj), null));				    
				}
		    ; 



sentenciasDeclarativas : declaraVariable 
		       | declaraFunc {$$=$1;}
		       | declaraVarFunc 
		       ;

declaraVariable: tipo listaVariables ';' {
                    AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
	       | listaVariables ';' {AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
	       | tipo listaVariables error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
	       ;

listaVariables: listaVariables ',' ID {
                		     if( AnalizadorSintactico.esVariableRedeclarada($3.sval + AnalizadorSintactico.ambitoActual)){
                                   //corto arbol
                             }else{
                                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove($3.sval);
                                aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                                AnalizadorLexico.tablaDeSimbolos.put($3.sval + AnalizadorSintactico.ambitoActual,aux);
                             }
                            }
	      |ID {
	                if( AnalizadorSintactico.esVariableRedeclarada($1.sval + AnalizadorSintactico.ambitoActual)){
                        //corto arbol
                    }else{
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
                     aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    AnalizadorLexico.tablaDeSimbolos.put($1.sval + AnalizadorSintactico.ambitoActual,aux);
                }
	      }
	      | listaVariables ',' {AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
	      // listaVariables ID error por falta de coma, da shift reduce
	      ;

declaraFunc: declaracionFunc bloqueDeclarativo bloqueEjecutableFunc {
        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
        $$=new ParserVal (new Nodo($1.sval, (Nodo)$3.obj, null));
        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
        }
	   | declaracionFunc bloqueEjecutableFunc {
	        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
	        $$=new ParserVal (new Nodo($1.sval, (Nodo)$2.obj, null));
	        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
	   }
	   ;

declaraVarFunc:  encabezadoFunc listaVariables ';' {AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
	      | encabezadoFunc ';' {AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
	      | encabezadoFunc listaVariables error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
	      ;

encabezadoFunc: tipo FUNC '(' tipo ')' {
            AnalizadorSintactico.tipoActual = $1.sval;
           }
	      | FUNC '(' tipo ')' {AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
	      | tipo FUNC tipo ')'{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | tipo FUNC '(' ')' {AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
	      | tipo FUNC '(' tipo error {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
	      ;


retorno : expAritmetica {$$=$1;}
	| tipo '(' expAritmetica ')'
	;



expAritmetica: expAritmetica '+' termino  {
                      if (((Nodo)$1.obj).getTipo().equals(((Nodo)$3.obj).getTipo())){
                        	$$= new ParserVal(new Nodo("+", (Nodo)$1.obj, (Nodo)$3.obj));
                        	((Nodo)$$.obj).setTipo(((Nodo)$3.obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos + (Linea " + AnalizadorLexico.numLinea + ")");
                      }
                      }
	     | expAritmetica '-' termino  {
                      if (((Nodo)$1.obj).getTipo().equals(((Nodo)$3.obj).getTipo())){
                        	$$= new ParserVal(new Nodo("-", (Nodo)$1.obj, (Nodo)$3.obj));
                        	((Nodo)$$.obj).setTipo(((Nodo)$3.obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos - (Linea " + AnalizadorLexico.numLinea + ")");
                      }
					  }
	     | termino	 {
	                 $$=$1;
	                 }
	     ;





termino : termino '*' factor	{
                      if (((Nodo)$1.obj).getTipo().equals(((Nodo)$3.obj).getTipo())){
                        	$$= new ParserVal(new Nodo("*", (Nodo)$1.obj, (Nodo)$3.obj));
                        	((Nodo)$$.obj).setTipo(((Nodo)$3.obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos * (Linea " + AnalizadorLexico.numLinea + ")");
                      }
				                }
	| termino '/' factor	{
                      if (((Nodo)$1.obj).getTipo().equals(((Nodo)$3.obj).getTipo())){
                        	$$= new ParserVal(new Nodo("/", (Nodo)$1.obj, (Nodo)$3.obj));
                        	((Nodo)$$.obj).setTipo(((Nodo)$3.obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos / (Linea " + AnalizadorLexico.numLinea + ")");
                      }
				            }
	| factor		        {
	                        $$ = $1;
	                        }
	;





factor  : ID  	{
                     String lexema = AnalizadorSintactico.getReferenciaPorAmbito($1.sval);
                     if(lexema != null){
                        AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
                        TDSObject value = AnalizadorLexico.getLexemaObject(lexema);
                        $$= new ParserVal(new Nodo($1.sval));
                        ((Nodo)$$.obj).setTipo(value.getTipoVariable());
                        //((Nodo)$$.obj).setTipoContenido("VAR");
                     }else{
                         AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                         //stop generacion de arbol
                     }
                }
	| CTE		{
                    if ($1.sval != null){
                        $$= new ParserVal(new Nodo($1.sval));
                    }
	            }
	| '-' CTE	{
	                AnalizadorLexico.agregarNegativoTDS($2.sval);
			        $$= new ParserVal(new Nodo("-"+$2.sval));
			        TDSObject value = AnalizadorLexico.getLexemaObject("-"+$2.sval);
                    if( value != null){
                        if ( value.getTipoVariable() == "LONG" ) {
                                long l = Long.parseLong("-"+$2.sval);
                                if( !((l >= -2147483648) && (l <= 2147483647))){
                                   AnalizadorSintactico.agregarError("CTE LONG fuera de Rango (Linea " + AnalizadorLexico.numLinea + ")");
                                   //break
                                }else{
                                      ((Nodo)$$.obj).setTipo(value.getTipoVariable());
                                }
                        }else{
                                float f = Float.parseFloat(("-"+$2.sval).replace('S','E'));
                                if( f != 0.0f  ){
                                    if( !((f > -3.40282347E+38) && (f < -1.17549435E-38 ))){
                                       AnalizadorSintactico.agregarError("CTE FLOAT fuera de Rango (Linea " + AnalizadorLexico.numLinea + ")");
                                    //break
                                     }else{
                                        ((Nodo)$$.obj).setTipo(value.getTipoVariable());
                                     }
                                }else{((Nodo)$$.obj).setTipo(value.getTipoVariable());}
                        }
                    }
			    }
	| llamadoFunc {$$=$1;}
	;

llamadoFunc: ID '(' ID ')'
		{
		  String variable = AnalizadorSintactico.getReferenciaPorAmbito($1.sval);
		  if(variable != null){
		    AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
		    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
		    if(value.getTipoContenido().equals("FUNC")){
		        ParserVal aux2= new ParserVal($3.sval);
		        ParserVal aux= new ParserVal($1.sval);
		        $$= new ParserVal(new Nodo("LF",(Nodo)aux.obj, (Nodo)aux2.obj ));
		        ((Nodo)$$.obj).setTipo(value.getTipoVariable());
		    }else{
		        AnalizadorSintactico.agregarError("ID pertenece a variable, no a Funcion (Linea " + AnalizadorLexico.numLinea + ")");
		        //error
		    }
		  }else{
             AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
             //error
		  }
		  }
	   | ID '(' ')' ';' {
	   		  String variable = AnalizadorSintactico.getReferenciaPorAmbito($1.sval);
       		  if(variable != null){
       		    AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
       		    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
       		    if(value.getTipoContenido().equals("FUNC")){
       		        ParserVal aux= new ParserVal($1.sval);
       		        $$= new ParserVal(new Nodo("LF",(Nodo)aux.obj, null ));
       		        ((Nodo)$$.obj).setTipo(value.getTipoVariable());
       		    }else{
       		        AnalizadorSintactico.agregarError("ID pertenece a variable, no a Funcion (Linea " + AnalizadorLexico.numLinea + ")");
       		        //error
       		    }
       		  }else{
                    AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
                    //error
       		  }
	   }
	   ; 



declaracionFunc : tipo FUNC ID '(' parametro ')' {AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
            if( AnalizadorSintactico.esVariableRedeclarada($3.sval + AnalizadorSintactico.ambitoActual)){
                //corto arbol
            }else{
                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove($3.sval);
                aux.setTipoContenido("FUNC");
                AnalizadorLexico.tablaDeSimbolos.put($3.sval + AnalizadorSintactico.ambitoActual,aux);
                $$=$3;
            }
            AnalizadorSintactico.ambitoActual += "@"+ $3.sval;
			}
		|tipo FUNC ID '(' ')' {AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
		     if( AnalizadorSintactico.esVariableRedeclarada($3.sval + AnalizadorSintactico.ambitoActual)){
                   //corto arbol
             }else{
                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove($3.sval);
                aux.setTipoContenido("FUNC");
                AnalizadorLexico.tablaDeSimbolos.put($3.sval + AnalizadorSintactico.ambitoActual,aux);
                $$=$3;
             }
             AnalizadorSintactico.ambitoActual += "@"+ $3.sval;
            }
		|FUNC ID '(' parametro ')' {AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo ID '(' parametro ')' {AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo FUNC ID parametro ')' {AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo FUNC ID '(' parametro error {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
		;


parametro : tipo ID {$$= new ParserVal(new Nodo($2.sval));
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove($2.sval);
                     aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    AnalizadorLexico.tablaDeSimbolos.put($2.sval + AnalizadorSintactico.ambitoActual,aux);
		     }
	  | ID {AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
	  ;



sentEjecutables : asignacion {$$=$1;}
		| sentenciaIF {$$=$1;}
		| sentenciaPRINT {$$=$1;}
		| sentenciaWHILE {$$=$1;}
		;

asignacion: ID ASIGN expAritmetica ';' {
                    String variable = AnalizadorSintactico.getReferenciaPorAmbito($1.sval);
                    if(variable == null){
                       AnalizadorSintactico.agregarError("Variable no definida (Linea " + AnalizadorLexico.numLinea + ")");
                       //corta arbol
                    }else{
                        AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion (Linea " + AnalizadorLexico.numLinea + ")");
					    ParserVal aux = new ParserVal(new Nodo($1.sval));
					    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
					    $$= new ParserVal(new Nodo(":=", (Nodo)aux.obj, (Nodo)$3.obj));
                        if(((Nodo)aux.obj).getTipo() == ((Nodo)$3.obj).getTipo()){
                            ((Nodo)$$.obj).setTipo(((Nodo)aux.obj).getTipo());
                        }else{
                        	 AnalizadorSintactico.agregarError("Tipo Incompatible (" + ((Nodo)aux.obj).getTipo() + "," +  ((Nodo)$3.obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
                        }
                     }
					}
	  | ID ASIGN tipo '(' expAritmetica ')' ';' {AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
	  | ASIGN expAritmetica ';' {AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
	  | ID expAritmetica ';' {AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
	  | ID ASIGN expAritmetica error {AnalizadorSintactico.agregarError("Error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
	  ;


sentenciaIF: IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)$4.obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)$6.obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		$$= new ParserVal(new Nodo("IF", (Nodo)$2.obj, (Nodo)auxCuerpo.obj));
		}
	   | IF condicion  THEN bloqueEjecutable ENDIF ';'{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");}
	   | condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';'{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
	   |IF condicion bloqueEjecutable ELSE bloqueEjecutable ENDIF ';'{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
	   |IF condicion THEN ELSE bloqueEjecutable ENDIF ';'{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF condicion THEN bloqueEjecutable ELSE ENDIF ';'{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	   |IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ';'{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
	   |IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF THEN bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF condicion bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF condicion THEN ENDIF ';' {AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF condicion THEN bloqueEjecutable ';' {AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF condicion THEN bloqueEjecutable ENDIF error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
	   ;

sentenciaPRINT: PRINT '(' CADENA ')' ';' {AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo($3.sval));
		$$= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}

	      | '(' CADENA ')' ';' {AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT CADENA ')' ';' {AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '('')' ';' {AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '(' CADENA ';' {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '(' CADENA ')' error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '(' ID ')' ';'  //(util para Debug, errores innecesarios)
	      | PRINT '(' CTE ')' ';'
	      ;

sentenciaWHILE: WHILE condicion DO bloqueEjecutable {AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		$$= new ParserVal(new Nodo("WHILE", (Nodo)$2.obj, (Nodo)$4.obj));}
	      | condicion DO bloqueEjecutable {AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
	      | WHILE DO bloqueEjecutable {AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
	      | WHILE condicion bloqueEjecutable {AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
	      | WHILE condicion DO error {AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	      ;


condicion: '(' comparaciones ')' {$$=$2;}
	 ;

comparaciones: comparaciones opLogico comparacion 		
	     | comparacion {$$ = new ParserVal(new Nodo("Cond", (Nodo)$1.obj, null));}
	     | comparaciones opLogico error {AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
	     ;

comparacion: expAritmetica comparador expAritmetica
		{ 
		  $$ = new ParserVal(new Nodo($2.sval,(Nodo) $1.obj,(Nodo)$3.obj));
		}
	   | tipo '(' expAritmetica ')' comparador expAritmetica
	   | expAritmetica comparador tipo '(' expAritmetica ')'
	   | tipo '(' expAritmetica ')' comparador tipo '(' expAritmetica ')'
	   | expAritmetica comparador error {AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
	   |comparador expAritmetica {AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
	   ;

comparador: '>' 	{$$ = new ParserVal(">");}
	  | '<'		{$$ = new ParserVal("<");}
	  | IGUAL	{$$ = new ParserVal("==");}
	  | DISTINTO	{$$ = new ParserVal("!=");}
	  | MAYORIG	{$$ = new ParserVal(">=");}
	  | MENORIG	{$$ = new ParserVal("<=");}
	  ;

opLogico: AND //{$$= new ParserVal("&&");}
	| OR  //{$$= new ParserVal("||");}
	;


tipo: LONG 	{AnalizadorSintactico.tipoActual = "LONG";
             $$ = new ParserVal("LONG");
            }
    | SINGLE {AnalizadorSintactico.tipoActual = "SINGLE";
             $$ = new ParserVal("SINGLE");
             }
    ;





%%


public int yylex() {
    int value = AnalizadorLexico.yylex();
    yylval = new ParserVal(AnalizadorLexico.refTDS); 
    return value;
}

public void yyerror(String string) {
	AnalizadorSintactico.agregarError("Parser: " + string);
}
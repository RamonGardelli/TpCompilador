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
			$$= new ParserVal(new Nodo($1.sval, (Nodo)$3.obj, null));
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



bloqueEjecutable : bloqueEjecutableNormal {$$=$1;}
				 ;

bloqueTRYCATCH: TRY sentenciaCONTRACT CATCH BEGIN sentSoloEjecutables END {$$= new ParserVal (new Nodo("TC", (Nodo)$2.obj, (Nodo)$5.obj));}
	      | sentenciaCONTRACT CATCH BEGIN sentSoloEjecutables END {AnalizadorSintactico.agregarError("error: falta TRY en (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY CATCH BEGIN sentSoloEjecutables END {AnalizadorSintactico.agregarError("error TRY vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY sentenciaCONTRACT BEGIN sentSoloEjecutables END {AnalizadorSintactico.agregarError("error falta CATCH (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY sentenciaCONTRACT CATCH sentSoloEjecutables END {AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY sentenciaCONTRACT CATCH BEGIN sentSoloEjecutables error {AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
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
            |BEGIN RETURN '(' retorno ')' ';' END {
                $$ = new ParserVal(new Nodo("BF",null,(Nodo)$4.obj));
            }
		    | sentEjecutableFunc RETURN '(' retorno ')' END {AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc '(' retorno ')' END {AnalizadorSintactico.agregarError("error falta RETURN (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc RETURN  retorno ')' END {AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc RETURN '(' ')' END {AnalizadorSintactico.agregarError("error falta retorno (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc RETURN '(' retorno END {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
		    ;



sentEjecutableFunc: sentEjecutableFunc sentEjecutablesFunc
                { if (($1.obj != null) && ($2.obj != null))
                     {$$= new ParserVal(new Nodo("S", (Nodo)$2.obj, (Nodo)$1.obj));}
                  else if(($1.obj == null)) {$$= new ParserVal(new Nodo("S", (Nodo)$2.obj, null));}
                       else if (($2.obj == null)) {$$= new ParserVal(new Nodo("S", null, (Nodo)$1.obj));}
                }
          | sentEjecutablesFunc
                  {
                    if($1.obj == null){
                       $$= new ParserVal(new Nodo("S",null, null));
                    }else{
                        $$= new ParserVal(new Nodo("S",((Nodo)$1.obj), null));
                    }
                  }
          ;

sentEjecutablesFunc: sentEjecutables { $$ = $1; }
          | bloqueTRYCATCH { $$ = $1; }
          ;

sentenciaCONTRACT: CONTRACT ':' condicion  ';' {

            AnalizadorSintactico.agregarAnalisis("sent contract (Linea " + AnalizadorLexico.numLinea + ")");
            if($3.obj == null)
                break;
            $$ =  new ParserVal(new Nodo("CONTRACT",(Nodo)$4.obj,null));
            }
		 | CONTRACT '(' condicion ')' ';'{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':' condicion ')' ';'{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':' '('')' ';'{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':''(' condicion ';' {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':''(' condicion ')' error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
		 ;



sentSoloEjecutables : sentEjecutables sentSoloEjecutables
                { if (($2.obj != null) && ($1.obj != null))
                     {$$= new ParserVal(new Nodo("S", (Nodo)$1.obj, (Nodo)$2.obj));}
                  else if(($2.obj == null)) {$$= new ParserVal(new Nodo("S", (Nodo)$1.obj, null));}
                       else if (($1.obj == null)) {$$= new ParserVal(new Nodo("S", null, (Nodo)$2.obj));}
                }
            | sentEjecutables
                {
                    if($1.obj == null){
                       $$= new ParserVal(new Nodo("S",null, null));
                    }else{
                       $$= new ParserVal(new Nodo("S",((Nodo)$1.obj), null));
                    }
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
	       | tipo listaVariables error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
	       | ID error{
	       	        AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
           	        AnalizadorLexico.listaDeErrores.add("Tipo de variable debe ser en mayuscula (Linea " + (AnalizadorLexico.numLinea-1) + ")");
           	      }
           | tipo error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
	       ;

listaVariables: listaVariables ',' ID {
                		     if( AnalizadorSintactico.esVariableRedeclarada($3.sval + AnalizadorSintactico.ambitoActual)){
                                   AnalizadorSintactico.agregarError("Variable redeclarada (Linea " + AnalizadorLexico.numLinea + ")");
                             }else{
                                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove($3.sval);
                                aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                                AnalizadorLexico.tablaDeSimbolos.put($3.sval + AnalizadorSintactico.ambitoActual,aux);
                             }
                            }
	      |ID {
	                if( AnalizadorSintactico.esVariableRedeclarada($1.sval + AnalizadorSintactico.ambitoActual)){
                        AnalizadorSintactico.agregarError("Variable redeclarada (Linea " + AnalizadorLexico.numLinea + ")");
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
        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
        $$=new ParserVal (new Nodo($1.sval+AnalizadorSintactico.ambitoActual, (Nodo)$3.obj, null));
        
        }
	   | declaracionFunc bloqueEjecutableFunc {
	        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
	        
	        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
	         
	        $$=new ParserVal (new Nodo($1.sval+AnalizadorSintactico.ambitoActual, (Nodo)$2.obj, null));
	   }
	   ;

declaraVarFunc:  encabezadoFunc listaVariables ';' {AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
	      | encabezadoFunc ';' {AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
	      | encabezadoFunc listaVariables error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
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
                       if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                       	break;
                      if (((Nodo)$1.obj).getTipo().equals(((Nodo)$3.obj).getTipo())){
                        	$$= new ParserVal(new Nodo("+", (Nodo)$1.obj, (Nodo)$3.obj));
                        	((Nodo)$$.obj).setTipo(((Nodo)$3.obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos + (Linea " + AnalizadorLexico.numLinea + ")");
                      }
                      }
	     | expAritmetica '-' termino  {
	                   if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                       	break;
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
                        if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                        	break;
                      if (((Nodo)$1.obj).getTipo().equals(((Nodo)$3.obj).getTipo())){
                        	$$= new ParserVal(new Nodo("*", (Nodo)$1.obj, (Nodo)$3.obj));
                        	((Nodo)$$.obj).setTipo(((Nodo)$3.obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos * (Linea " + AnalizadorLexico.numLinea + ")");
                      }
				                }
	| termino '/' factor	{

	            if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
    	            break;
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
                        $$= new ParserVal(new Nodo(lexema));
                        ((Nodo)$$.obj).setTipo(value.getTipoContenido());
                        //((Nodo)$$.obj).setTipoContenido("VAR");
                     }else{
                         AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                         AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
                         //stop generacion de arbol
                         
                     }
                }
	| CTE		{
	                String var = $1.sval;
	                TDSObject value = AnalizadorLexico.getLexemaObject(var);
                    if (value.getTipoVariable() == "LONG"){
                      if(var.equals("2147483648")){
                           var = "2147483647";
                           TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
                           AnalizadorLexico.tablaDeSimbolos.put(var,aux);
                      }
                    }
                    $$= new ParserVal(new Nodo(var));
                    if( value != null){
                         ((Nodo)$$.obj).setTipo(value.getTipoVariable());
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
                                    AnalizadorLexico.listaDeWarnings.add("Warning Linea " + AnalizadorLexico.numLinea + " : constante LONG fuera de rango.");
                                }else{
                                      ((Nodo)$$.obj).setTipo(value.getTipoVariable());
                                }
                        }else{
                                float f = Float.parseFloat(("-"+$2.sval).replace('S','E'));
                                if( f != 0.0f  ){
                                    if( !((f > -3.40282347E+38) && (f < -1.17549435E-38 ))){
                                       AnalizadorLexico.listaDeWarnings.add("Warning Linea " + AnalizadorLexico.numLinea + " : constante FLOAT fuera de rango.");;
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
		  String variable2 = AnalizadorSintactico.getReferenciaPorAmbito($3.sval);
		  if(variable != null && variable2!= null){
		    AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
			AnalizadorLexico.tablaDeSimbolos.remove($3.sval);
		    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
			TDSObject value2 = AnalizadorLexico.getLexemaObject(variable2);
			if (!value.getTipoParametro().equals(value2.getTipoContenido())){
			    AnalizadorSintactico.agregarError("El tipo enviado como parametro es distinto al esperado (Linea " + AnalizadorLexico.numLinea + ")");
			}
			
			ParserVal aux= new ParserVal(new Nodo(variable, null, null ));
			ParserVal aux2= new ParserVal(new Nodo(variable2, null, null ));
		
		    
		    $$= new ParserVal(new Nodo("LF",(Nodo)aux.obj, (Nodo)aux2.obj ));
		    ((Nodo)$$.obj).setTipo(value.getTipoContenido());
		  }else{
             AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
             //error
		  }
		  }

	   ;



declaracionFunc : tipo FUNC ID '(' parametro ')' {AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
            if( AnalizadorSintactico.esVariableRedeclarada($3.sval + AnalizadorSintactico.ambitoActual)){
                AnalizadorSintactico.agregarError("ERROR: ID ya fue utilizado (Linea " + AnalizadorLexico.numLinea + ")");
            }else{
                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove($3.sval);
                aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                aux.setEsFuncion(true);
                aux.setTipoParametro(((TDSObject)((Object[])($5.obj))[1]).getTipoContenido());
                aux.setNombreParametro(((Object[])($5.obj))[0] + AnalizadorSintactico.ambitoActual+"@"+$3.sval);
                AnalizadorLexico.tablaDeSimbolos.put($3.sval + AnalizadorSintactico.ambitoActual,aux);
                $$=$3;
            }
            AnalizadorSintactico.ambitoActual += "@"+ $3.sval;
            AnalizadorLexico.tablaDeSimbolos.put(((Object[])($5.obj))[0] + AnalizadorSintactico.ambitoActual,(TDSObject)((Object[])($5.obj))[1]);
			}
		|FUNC ID '(' parametro ')' {AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo ID '(' parametro ')' {AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo FUNC ID parametro ')' {AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo FUNC ID '(' parametro error {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
		;


parametro : tipo ID {
                    Object[] obj = new Object[2] ;
                    obj[0] = $2.sval;
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove($2.sval);
                    aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    obj[1] = aux;
                    $$= new ParserVal(obj);
		     }
	  | ID {AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
	  ;



sentEjecutables : asignacion  {$$=$1;}
		| sentenciaIF {$$=$1;}
		| sentenciaPRINT {$$=$1;}
		| sentenciaWHILE {$$=$1;}
		;

asignacion: ID ASIGN expAritmetica ';' {

                    String variable = AnalizadorSintactico.getReferenciaPorAmbito($1.sval);
                    if(variable == null){
                       AnalizadorSintactico.agregarError("Variable no definida (Linea " + AnalizadorLexico.numLinea + ")");
                       AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
                       //corta arbol
                    }else{
                        AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
                        AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion (Linea " + AnalizadorLexico.numLinea + ")");
                        if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                             break;
					    ParserVal aux = new ParserVal(new Nodo(variable));
					    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
					    $$= new ParserVal(new Nodo(":=", (Nodo)aux.obj, (Nodo)$3.obj));
                        if(value.getTipoContenido().equals( ((Nodo)$3.obj).getTipo())){
                            ((Nodo)$$.obj).setTipo(((Nodo)aux.obj).getTipo());
                        }else{
                        	 AnalizadorSintactico.agregarError("Tipo Incompatible (" + value.getTipoContenido() + "," +  ((Nodo)$3.obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
                        }
                     }
					}
					
	  | '(' tipo ')' ID ASIGN expAritmetica ';' 
					{
						if ($2.sval != "SINGLE") 
						{
							AnalizadorSintactico.agregarError("No se puede castear a este tipo (Linea " + AnalizadorLexico.numLinea + ")");
							AnalizadorLexico.tablaDeSimbolos.remove($4.sval);
						}
						else 
							{
								String variable = AnalizadorSintactico.getReferenciaPorAmbito($4.sval);
								if(variable == null){
									AnalizadorSintactico.agregarError("Variable no definida (Linea " + AnalizadorLexico.numLinea + ")");
									AnalizadorLexico.tablaDeSimbolos.remove($4.sval);
									//corta arbol
								}else{
									AnalizadorLexico.tablaDeSimbolos.remove($4.sval);
									AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion (Linea " + AnalizadorLexico.numLinea + ")");
									if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
										break;
									ParserVal aux = new ParserVal(new Nodo(variable));
									((Nodo)aux.obj).setTipo("SINGLE"); //Mirarlo 
									TDSObject value = AnalizadorLexico.getLexemaObject(variable);
									$$= new ParserVal(new Nodo(":=", (Nodo)aux.obj, (Nodo)$6.obj));
									if( (((Nodo)$6.obj).getTipo()).equals("SINGLE")){
										((Nodo)$$.obj).setTipo(((Nodo)aux.obj).getTipo());
									}else{
										AnalizadorSintactico.agregarError("Tipo Incompatible (SINGLE, " + ((Nodo)$6.obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
									}
								}
                     
							}
					}
	  | ID '=' expAritmetica ';' {

	                        AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea +": falta el simbolo : de la asignacion.");
	                        if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                            	break;
                            String variable = AnalizadorSintactico.getReferenciaPorAmbito($1.sval);
                            if(variable == null){
                               AnalizadorSintactico.agregarError("Variable no definida (Linea " + AnalizadorLexico.numLinea + ")");
                               AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
                               //corta arbol
                            }else{
                                AnalizadorLexico.tablaDeSimbolos.remove($1.sval);
                                AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion (Linea " + AnalizadorLexico.numLinea + ")");
        					    ParserVal aux = new ParserVal(new Nodo(variable));
        					    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
        					    $$= new ParserVal(new Nodo(":=", (Nodo)aux.obj, (Nodo)$3.obj));
                                if(value.getTipoContenido().equals( ((Nodo)$3.obj).getTipo())){
                                    ((Nodo)$$.obj).setTipo(((Nodo)aux.obj).getTipo());
                                }else{
                                	 AnalizadorSintactico.agregarError("Tipo Incompatible (" + value.getTipoContenido() + "," +  ((Nodo)$3.obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
                                }
                             }
        					}
	  | ID ASIGN tipo '(' expAritmetica ')' ';' {AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
	  | ASIGN expAritmetica ';' {AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
	  | ID expAritmetica ';' {AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
	  | ID ASIGN expAritmetica error {AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
	  | ID ASIGN error {AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
	  | ID '='  error {AnalizadorSintactico.agregarError("Error falta ':'(Linea " + (AnalizadorLexico.numLinea -1) + ")");}
	  | ID ASIGN comparacion ';'  {AnalizadorSintactico.agregarError("Error, no puede asignarse un comparador(Linea " + AnalizadorLexico.numLinea + ")");}
	  ;


sentenciaIF: IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if($2.obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)$4.obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)$6.obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		$$= new ParserVal(new Nodo("IF", (Nodo)$2.obj, (Nodo)auxCuerpo.obj));
		}
	   | IF condicion  THEN bloqueEjecutable ENDIF ';'{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");
	                                                    if($2.obj == null)
                                                           break;
                                                        ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)$4.obj, null));
                                                        ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,null));
                                                        $$= new ParserVal(new Nodo("IF", (Nodo)$2.obj, (Nodo)auxCuerpo.obj));}
	   |IF condicion THEN sentEjecutables ELSE bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if($2.obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)$4.obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)$6.obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		$$= new ParserVal(new Nodo("IF", (Nodo)$2.obj, (Nodo)auxCuerpo.obj));
		}
	   |IF condicion THEN bloqueEjecutable ELSE sentEjecutables ENDIF ';' {AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if($2.obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)$4.obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)$6.obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		$$= new ParserVal(new Nodo("IF", (Nodo)$2.obj, (Nodo)auxCuerpo.obj));
		}
	   |IF condicion THEN sentEjecutables ELSE sentEjecutables ENDIF ';' {AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if($2.obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)$4.obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)$6.obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		$$= new ParserVal(new Nodo("IF", (Nodo)$2.obj, (Nodo)auxCuerpo.obj));
		}
	   |IF condicion  THEN sentEjecutables ENDIF ';'{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");
	                                                    if($2.obj == null)
                                                           break;
                                                        ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)$4.obj, null));
                                                        ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,null));
                                                        $$= new ParserVal(new Nodo("IF", (Nodo)$2.obj, (Nodo)auxCuerpo.obj));}
	   | condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';'{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
	   |IF condicion bloqueEjecutable ELSE bloqueEjecutable ENDIF ';'{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
	   |IF condicion THEN ELSE bloqueEjecutable ENDIF ';'{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF condicion THEN bloqueEjecutable ELSE ENDIF ';'{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	   |IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ';'{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
	   |IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
	   | IF THEN bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF condicion bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF condicion THEN ENDIF ';' {AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF condicion THEN bloqueEjecutable ';' {AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
	   | IF condicion THEN bloqueEjecutable ENDIF error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
	   
	   ;

sentenciaPRINT: PRINT '(' CADENA ')' ';' {AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo($3.sval));
		((Nodo)aux.obj).setTipo("CADENA");
		$$= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
	      | '(' CADENA ')' ';' {AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT CADENA ')' ';' {AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '('')' ';' {AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '(' CADENA ';' {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '(' CADENA ')' error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
	      | PRINT '(' ID ')' ';' {
	       	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
	                           String lexema = AnalizadorSintactico.getReferenciaPorAmbito($3.sval);
                               if(lexema != null){
                                  AnalizadorLexico.tablaDeSimbolos.remove($3.sval);
                                  ParserVal aux = new ParserVal(new Nodo(lexema));
                                  ((Nodo)aux.obj).setTipo("ID");
                                  $$= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                               }else{
                                   AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                   AnalizadorLexico.tablaDeSimbolos.remove($3.sval);
                                   //stop generacion de arbol

                               }
	      }
	      | PRINT '(' CTE ')' ';'{
                	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
                               	                           String lexema = AnalizadorSintactico.getReferenciaPorAmbito($3.sval);

                               if(lexema != null){
                                  AnalizadorLexico.tablaDeSimbolos.remove($3.sval);
                                  ParserVal aux = new ParserVal(new Nodo(lexema));
                                  ((Nodo)aux.obj).setTipo("CTE");
                                  
                                  $$= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                               }else{
                                  AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                  AnalizadorLexico.tablaDeSimbolos.remove($3.sval);
                                  //stop generacion de arbol
                               }
                               }
          | PRINT '(' '-' CTE ')' ';'{
                          	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
                                        String lexema = AnalizadorSintactico.getReferenciaPorAmbito("-"+$4.sval);

                                         if(lexema != null){
                                            AnalizadorLexico.tablaDeSimbolos.remove("-"+$4.sval);                                          
                                            ParserVal aux = new ParserVal(new Nodo(lexema));
                                            ((Nodo)aux.obj).setTipo("CTE");
                                            $$= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                                         }else{
                                            AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                            AnalizadorLexico.tablaDeSimbolos.remove("-"+$4.sval);
                                            //stop generacion de arbol
                                         }
	      }
	      ;

sentenciaWHILE: WHILE condicion DO bloqueEjecutable {AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		 if($2.obj == null)
             break;
		$$= new ParserVal(new Nodo("WHILE", (Nodo)$2.obj, (Nodo)$4.obj));}
	      | condicion DO bloqueEjecutable {AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
	      | WHILE DO bloqueEjecutable {AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
	      | WHILE condicion bloqueEjecutable {AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
	      | WHILE condicion DO error {AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	      ;


condicion: '(' comparaciones ')' {
				if($2.obj == null)
					break;
				$$ = new ParserVal(new Nodo("Cond", (Nodo)$1.obj, null));}
	 ;

comparaciones: comparaciones opLogico comparacion {
				if ($1.obj == null || $3.obj == null){
					$$ = new ParserVal (new Nodo($2.sval,(Nodo)$3.obj, (Nodo)$1.obj ));}
					
				else if ($1.obj == null){
					$$ = new ParserVal (new Nodo($2.sval,(Nodo)$3.obj, null));}
				
			}		
	     | comparacion {$$=$1;}
	     | comparaciones opLogico error {AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
	     ;

comparacion: expAritmetica comparador expAritmetica
		{
		  if($1.obj == null || $3.obj == null){
             break;
          }else{
		  $$ = new ParserVal(new Nodo($2.sval,(Nodo) $1.obj,(Nodo)$3.obj));
		  }
		}
	   | expAritmetica comparador error {AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
	   |comparador expAritmetica {AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
	   ;

comparador: '>' 	{$$ = new ParserVal(">");}
	  | '<'		{$$ = new ParserVal("<");}
	  | IGUAL	{$$ = new ParserVal("==");}
	  | DISTINTO	{$$ = new ParserVal("<>");}
	  | MAYORIG	{$$ = new ParserVal(">=");}
	  | MENORIG	{$$ = new ParserVal("<=");}
	  | '=' {
	           AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea +": se esperaba comparacion ==.");
	           $$ = new ParserVal("==");
	        }
	  ;

opLogico: AND {$$= new ParserVal("&&");}
	| OR  {$$= new ParserVal("||");}
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
	//AnalizadorSintactico.agregarError("Parser token error: " + string);
	//System.out.println("token error en linea  "+ AnalizadorLexico.numLinea + ": " +string);
}
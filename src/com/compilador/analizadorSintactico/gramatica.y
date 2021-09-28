%{

package com.compilador;

import com.compilador.analizadorLexico.AnalizadorLexico;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;



%}

%token ELSE THEN ENDIF PRINT FUNC RETURN WHILE LONG BEGIN END SINGLE BREAK DO CADENA ID CTE IGUAL DISTINTO MAYORIG MENORIG CONTRACT TRY CATCH IF AND OR ASIGN


%start programa

%%

programa:ID bloqueDeclarativo bloqueEjecutable {aSintactico.agregarAnalisis("Programa reconocido. (Linea " + AnalizadorLexico.numLinea + ")");}
	;

bloqueDeclarativo:	bloqueDeclarativo sentenciasDeclarativas
			| 		sentenciasDeclarativas
	;

bloqueEjecutable : 'BEGIN' sentSoloEjecutables 'END'
	         | sentSoloEjecutables 'END' error {aSintactico.addErrorSint("Error sintactico en (Linea " + AnalizadorLexico.numLinea +"): falta 'BEGIN' al abrir bloque."); }
		 | 'BEGIN' sentSoloEjecutables error {aSintactico.addErrorSint("Error sintactico en (Linea " + AnalizadorLexico.numLinea +"): falta 'END' al cerrar bloque."); }


sentSoloEjecutables : sentEjecutables sentSoloEjecutables
		    | 		  sentEjecutables
		    ; 

sentenciasDeclarativas : tipo listaVariables ';' {aSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
		       | listaVariables ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea " + AnalizadorLexico.numLinea + "):falta el tipo de 			variable. ");}
		       | tipo ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea " + AnalizadorLexico.numLinea + "):falta de identificadores.");}
		       | tipo listaVariables error {aSintactico.addErrorSintactico("Error sintactico en(Linea " +(AnalizadorLexico.numLinea -1)+"):falta ';' para finalizar la sentencia.");}
		       | declaracionFunc bloqueDeclarativo 'BEGIN' bloqueEjecutable 'RETURN' '(' retorno ')'  'END' {if (!aSintactico.getErrorFunc())
								{
								  aSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");	
								}
							else {
								aSintactico.addErrorSintactico("Error sintactico en (Linea " + AnalizadorLexico.numLinea + "):funcion mal declarada");
								aSintactico.setErrorFunc(true); //todavia no se como se emplea
							     }
							}
		       |declaracionFunc bloqueDeclarativo bloqueEjecutable 'END' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta BEGIN ");}
		       |declaracionFunc bloqueDeclarativo 'BEGIN' bloqueEjecutable error  {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta END ");}
		       |declaracionFunc bloqueDeclarativo 'BEGIN' 'END' error {aSintactico.addErrorSintactico("Warning (Linea" + AnalizadorLexico.numLinea + "): la funcion esta vacia ");}
		       ;


retorno : expAritmetica
	;


expAritmetica: expAritmetica '+' termino
	     | expAritmetica '-' termino
	     | termino
	     ;

termino : termino '*' factor
	| termino '/' factor
	| factor
	;

factor  : ID
	| CTE
	| ID '(' parametro ')'    //Funcion
	| ID '(' ')' // Funcion que no usa un parametro
	| ID '(' parametro error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta ')' ");}
	| ID parametro ')' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta '(' ");}
	;



declaracionFunc : tipo FUNC ID '(' parametro ')' {aSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo FUNC ID '(' ')' {aSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
		| FUNC ID '(' parametro ')' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta tipo ");}
		| tipo ID '(' parametro ')' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta 'FUNC' ");}
		| tipo FUNC ID '(' parametro  error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta ')' ");}
		| tipo FUNC ID  parametro ')' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta '(' ");}
		;

parametro : tipo ID
	  | tipo error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta IDparametro ");}
	  | ID error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta tipo parametro ");}
	  ;

sentEjecutables : ID ':''=' expAritmetica ';' {aSintactico.agregarAnalisis("Sentencia ejecutable (Linea " + AnalizadorLexico.numLinea + ")");}
		| IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';' {aSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");}
		| IF condicion  THEN bloqueEjecutable ENDIF ';'{aSintactico.agregarAnalisis("sentencia 'IF' (Linea " + AnalizadorLexico.numLinea + ")");}
		| PRINT '(' CADENA ')';
		| WHILE condicion DO bloqueEjecutable; {aSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");}
		|':''=' expAritmetica ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta ID ");}
		| ID expAritmetica ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta ':=' ");}
		| ID ':''=' ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta expAritmetica ");}
		| ID ':''=' expAritmetica error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "): falta ';' ");}
		| condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + 				AnalizadorLexico.numLinea + "): falta IF ");}
		|IF THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + 						AnalizadorLexico.numLinea + "): falta condicion");}
		|IF condicion bloqueEjecutable ELSE bloqueEjecutable ENDIF ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + 					AnalizadorLexico.numLinea + "):falta THEN ");}
		|IF condicion THEN ELSE bloqueEjecutable ENDIF ';' {aSintactico.addErrorSintactico("Warning (Linea"+ AnalizadorLexico.numLinea +"):IF vacio");}
		|IF condicion THEN bloqueEjecutable ELSE ENDIF ';' {aSintactico.addErrorSintactico("Warning(Linea"+ AnalizadorLexico.numLinea +"):ELSE 				vacio");}
		|IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + 					AnalizadorLexico.numLinea + "):falta  ENDIF ");}
		|IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + 					AnalizadorLexico.numLinea + "):falta ';' ");}
		| condicion THEN bloqueEjecutable ENDIF ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + 							AnalizadorLexico.numLinea + "):falta 'IF' ");}
		| IF THEN bloqueEjecutable ENDIF ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta 				'condicion' ");}
		| IF condicion bloqueEjecutable ENDIF ';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + 				"):falta 'THEN' ");}
		| IF condicion THEN ENDIF ';' {aSintactico.addErrorSintactico("Warning (Linea" + AnalizadorLexico.numLinea + "):IF vacio ");}
		| IF condicion THEN bloqueEjecutable';' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + 				"):falta ENDIF ");}
		| IF condicion THEN bloqueEjecutable ENDIF error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + 				"):falta ';' ");}
		|PRINT '(' CADENA error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta ')' ");}
		|PRINT CADENA ')' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta '(' ");}
		|PRINT '('')' {aSintactico.addErrorSintactico("Warning (Linea" + AnalizadorLexico.numLinea + "):Print vacio ");}
		| condicion DO bloqueEjecutable; error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta WHILE ");}
		|WHILE DO bloqueEjecutable error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta condicion ");}
		|WHILE condicion bloqueEjecutable error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta do ");}
		|WHILE condicion DO error{aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):while vacio infinito ");}
		;


condicion: '(' comparacion ')'
	 | '('comparacion error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta ')' ");}
	 | comparacion ')' error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta '(' ");}
	 | '(' ')'error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):no hay comparacion ");}
	 ;

comparacion: expAritmetica comparador expAritmetica
	   | comparacion opLogico comparacion
	   | expAritmetica comparador error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta una expAritmetica");}
	   | comparacion opLogico error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta otra comparacion");}
	   | comparador comparador error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta un opLogico");}
	   | expAritmetica expAritmetica error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta un comparador");}
	   ;

comparador: '>'
	  | '<'
	  | IGUAL
	  | DISTINTO
	  | MAYORIG
	  | MENORIG
	  ;

opLogico: AND
	| OR  //consultar si van todo junto o separado entre las comillas
	;


tipo: LONG
    | SINGLE
    ;


listaVariables: ID
	      | listaVariables ',' ID
	      | listaVariables ID error {aSintactico.addErrorSintactico("Error sintactico en (Linea" + AnalizadorLexico.numLinea + "):falta ','");}
	      ;


%%


public int yylex() {
    return AnalizadorLexico.yylex();
}

public void yyerror(String string) {
	AnalizadorLexico.listaDeErrores("par: " + string);
}
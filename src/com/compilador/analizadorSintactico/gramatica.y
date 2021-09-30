%{

package com.compilador;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorSintactico.AnalizadorSintactico;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;



%}

%token ELSE THEN ENDIF PRINT FUNC RETURN WHILE LONG BEGIN END SINGLE BREAK DO CADENA ID CTE IGUAL DISTINTO MAYORIG MENORIG CONTRACT TRY CATCH IF AND OR ASIGN


%start programa

%%

programa:ID bloqueDeclarativo bloqueEjecutable {AnalizadorSintactico.agregarAnalisis("Programa reconocido. (Linea " + AnalizadorLexico.numLinea + ")");}
	;

bloqueDeclarativo:bloqueDeclarativo sentenciasDeclarativas
		 | sentenciasDeclarativas
		 ;

bloqueEjecutable :TRY sentEjecutables CATCH BEGIN sentSoloEjecutables END
		 |BEGIN sentSoloEjecutables END
		 ;

bloqueEjecutableFunc: BEGIN sentEjecutableFunc RETURN '(' retorno ')' END
		    ;

sentEjecutableFunc: sentEjecutableFunc sentEjecutables;
		  | sentEjecutableFunc CONTRACT ':' '(' condicion ')' ';'  //no se si una funcion puede solo tener contract
		  | sentEjecutables
		  ;



sentSoloEjecutables : sentSoloEjecutables sentEjecutables 
		    | sentEjecutables
		    ; 

sentenciasDeclarativas : tipo listaVariables ';' {AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}

		       | declaracionFunc bloqueDeclarativo bloqueEjecutableFunc {AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");}
		       | encabezadoFunc listaVariables ';' {AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
		       ;

encabezadoFunc: tipo FUNC '(' tipo ')'
	      ;


retorno : expAritmetica
	| tipo '(' expAritmetica ')'
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
	| llamadoFunc
	;

llamadoFunc: ID '(' parametro ')'
	   | ID '(' ')'
	   ; 



declaracionFunc : tipo FUNC ID '(' parametro ')' {AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo FUNC ID '(' ')' {AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
		;

parametro : tipo ID
	  ;

sentEjecutables : ID ASIGN expAritmetica ';' {AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable (Linea " + AnalizadorLexico.numLinea + ")");}
		| ID ASIGN tipo '(' expAritmetica ')' ';'
		| IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");}
		| IF condicion  THEN bloqueEjecutable ENDIF ';'{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + AnalizadorLexico.numLinea + ")");}
		| PRINT '(' CADENA ')' ';'
		| PRINT '(' ID ')' ';'
		| PRINT '(' CTE ')' ';'
		| WHILE condicion DO bloqueEjecutable; {AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");}
		;


condicion: '(' comparaciones ')'
	 ;

comparaciones: comparaciones opLogico comparacion
	     | comparacion
	     ;

comparacion: expAritmetica comparador expAritmetica
	   | tipo '(' expAritmetica ')' comparador expAritmetica
	   | expAritmetica comparador tipo '(' expAritmetica ')'
	   | tipo '(' expAritmetica ')' comparador tipo '(' expAritmetica ')'
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


listaVariables: listaVariables ',' ID
	      |ID
	      ;


%%


public int yylex() {
    return AnalizadorLexico.yylex();
}

public void yyerror(String string) {
	AnalizadorSintactico.agregarError("Parser: " + string);
}
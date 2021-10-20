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



bloqueEjecutable :bloqueTRYCATCH
		 |bloqueEjecutableNormal
		 ;

bloqueTRYCATCH: TRY sentEjecutables CATCH BEGIN sentSoloEjecutables END
	      | sentEjecutables CATCH BEGIN sentSoloEjecutables END {AnalizadorSintactico.agregarError("error: falta TRY en (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY CATCH BEGIN sentSoloEjecutables END {AnalizadorSintactico.agregarError("error TRY-CATCH vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY sentEjecutables BEGIN sentSoloEjecutables END {AnalizadorSintactico.agregarError("error falta CATCH (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY sentEjecutables CATCH sentSoloEjecutables END {AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
	      | TRY sentEjecutables CATCH BEGIN sentSoloEjecutables error {AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
	      ;

bloqueEjecutableNormal: BEGIN sentSoloEjecutables END
		      | sentSoloEjecutables END {AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
		      | BEGIN sentSoloEjecutables error {AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
		      ;


bloqueEjecutableFunc: BEGIN sentEjecutableFunc RETURN '(' retorno ')' END
		    | sentEjecutableFunc RETURN '(' retorno ')' END {AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc '(' retorno ')' END {AnalizadorSintactico.agregarError("error falta RETURN (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc RETURN  retorno ')' END {AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc RETURN '(' ')' END {AnalizadorSintactico.agregarError("error falta retorno (Linea " + AnalizadorLexico.numLinea + ")");}
		    | BEGIN sentEjecutableFunc RETURN '(' retorno END {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}

		    ;


sentEjecutableFunc: sentEjecutableFunc sentEjecutables 
		  | sentEjecutableFunc sentenciaCONTRACT
		  | sentEjecutables
		  ;

sentenciaCONTRACT: CONTRACT ':' '(' condicion ')' ';' {AnalizadorSintactico.agregarAnalisis("sent control (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT '(' condicion ')' ';'{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':' condicion ')' ';'{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':' '('')' ';'{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':''(' condicion ';' {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
		 | CONTRACT ':''(' condicion ')' error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
		 ;



sentSoloEjecutables : sentSoloEjecutables sentEjecutables 
		    | sentEjecutables
		    ; 



sentenciasDeclarativas : declaraVariable
		       | declaraFunc
		       | declaraVarFunc
		       ;

declaraVariable: tipo listaVariables ';' {AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
	       | listaVariables ';' {AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
	       | tipo listaVariables error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
	       ;

declaraFunc: declaracionFunc bloqueDeclarativo bloqueEjecutableFunc {AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");}
	   | declaracionFunc bloqueEjecutableFunc {AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");}
	   ;

declaraVarFunc:  encabezadoFunc listaVariables ';' {AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
	      | encabezadoFunc ';' {AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
	      | encabezadoFunc listaVariables error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
	      ;

encabezadoFunc: tipo FUNC '(' tipo ')'
	      | FUNC '(' tipo ')' {AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
	      | tipo FUNC tipo ')'{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | tipo FUNC '(' ')' {AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
	      | tipo FUNC '(' tipo error {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
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
	| '-' CTE
	| llamadoFunc
	;

llamadoFunc: ID '(' parametro ')'
	   | ID '(' ')'
	   ; 



declaracionFunc : tipo FUNC ID '(' parametro ')' {AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo FUNC ID '(' ')' {AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
		|FUNC ID '(' parametro ')' {AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo ID '(' parametro ')' {AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo FUNC ID parametro ')' {AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
		|tipo FUNC ID '(' parametro error {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
		;


parametro : tipo ID
	  | ID {AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
	  ;



sentEjecutables : asignacion
		| sentenciaIF
		| sentenciaPRINT
		| sentenciaWHILE
		;

asignacion: ID ASIGN expAritmetica ';' {AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion (Linea " + AnalizadorLexico.numLinea + ")");}
	  | ID ASIGN tipo '(' expAritmetica ')' ';' {AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
	  | ASIGN expAritmetica ';' {AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
	  | ID expAritmetica ';' {AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
	  | ID ASIGN expAritmetica error {AnalizadorSintactico.agregarError("Error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
	  ;


sentenciaIF: IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';' {AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");}
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

sentenciaPRINT: PRINT '(' CADENA ')' ';' {AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");}
	      | '(' CADENA ')' ';' {AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT CADENA ')' ';' {AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '('')' ';' {AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '(' CADENA ';' {AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '(' CADENA ')' error {AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | PRINT '(' ID ')' ';'  //(util para Debug, errores innecesarios)
	      | PRINT '(' CTE ')' ';'
	      ;

sentenciaWHILE: WHILE condicion DO bloqueEjecutable {AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");}
	      | condicion DO bloqueEjecutable {AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
	      | WHILE DO bloqueEjecutable {AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
	      | WHILE condicion bloqueEjecutable {AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
	      | WHILE condicion DO error {AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
	      ;


condicion: '(' comparaciones ')'
	 ;

comparaciones: comparaciones opLogico comparacion
	     | comparacion
	     // comparaciones comparacion error por falta de opLogico pero shift reduce
	     | comparaciones opLogico error {AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
	     ;

comparacion: expAritmetica comparador expAritmetica
	   | tipo '(' expAritmetica ')' comparador expAritmetica
	   | expAritmetica comparador tipo '(' expAritmetica ')'
	   | tipo '(' expAritmetica ')' comparador tipo '(' expAritmetica ')'
	   //expAritmetica expAritmetica error pero da Shift Reduce
	   | expAritmetica comparador error {AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
	   |comparador expAritmetica {AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
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
	      | listaVariables ',' {AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
	      // listaVariables ID error por falta de coma, da shift reduce
	      ;


%%


public int yylex() {
    return AnalizadorLexico.yylex();
}

public void yyerror(String string) {
	AnalizadorSintactico.agregarError("Parser: " + string);
}
package com.compilador.assembler;

import com.compilador.arbolSintactico.Nodo;

public class CodigoAssembler {
	Registro registros[];
	String codigoAssembler;

	public CodigoAssembler(Registro[] registros) {
		this.registros = new Registro[4];
		codigoAssembler = "";
	}
	
	public void generarCodigo(Nodo nodo) {
		if (nodo.EsHoja()) {
			
		}
	}
	
}

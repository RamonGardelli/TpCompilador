package com.compilador.assembler;

public class Registro {
	private boolean libre;
	private String nombre;
	
	public Registro(String nombre) {
		this.libre = true;
		this.nombre = nombre;
	}

	public boolean estaLibre() {
		return this.libre;
	}

	public void setLibre(boolean b) {
		this.libre=b;
	}

}

package model;

import br.com.serialexperimentscarina.pilhaobject.PilhaObject;

public class Trabalho {
	
	private int codigo;
	private String tipo;
	private String tema;
	private String area;
	private String subarea;
	private String integrantes;
	
	// Cada trabalho possui uma pilha de suas orientações, entende-se que a orientação mais importante é sempre a última (mais recente).
	private PilhaObject orientacoes = new PilhaObject();
	
	// toString para gravação em arquivo
	@Override
	public String toString() {
		return (codigo + ";" + tipo + ";" + tema + ";" + area + ";" + subarea + ";" + integrantes);
	}

	// Código hash
	public int hashCodigo() {
		return (codigo % 10);
	}
	
	public int hashSubarea() {
		return (subarea.toLowerCase().charAt(0) - 97);
	}

	// Getters e setters
	public int getCodigo() {
		return codigo;
	}


	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getTema() {
		return tema;
	}


	public void setTema(String tema) {
		this.tema = tema;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getSubarea() {
		return subarea;
	}


	public void setSubarea(String subarea) {
		this.subarea = subarea;
	}


	public String getIntegrantes() {
		return integrantes;
	}


	public void setIntegrantes(String integrantes) {
		this.integrantes = integrantes;
	}


	public PilhaObject getOrientacoes() {
		return orientacoes;
	}


	public void setOrientacoes(PilhaObject orientacoes) {
		this.orientacoes = orientacoes;
	}
	
	

}

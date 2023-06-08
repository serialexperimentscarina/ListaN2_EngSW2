package model;

public class Orientacao {
	
	private int dia;
	private int mes;
	private int ano;
	private String pontos;
	
	// toString para gravação em arquivo
	@Override
	public String toString() {
		return (dia + ";" + mes + ";" + ano + ";" + pontos);
	}

	// Getters e setters
	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getPontos() {
		return pontos;
	}

	public void setPontos(String pontos) {
		this.pontos = pontos;
	}
	
	

}

package model;

public class Orientacao {
	
	public int dia;
	public int mes;
	public int ano;
	public String pontos;
	
	@Override
	public String toString() {
		return (dia + ";" + mes + ";" + ano + ";" + pontos);
	}

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

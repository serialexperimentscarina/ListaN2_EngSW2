package model;

public class Aluno {
	
	private String nome;
	private String ra;
	
	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRa() {
		return ra;
	}

	public void setRa(String ra) {
		this.ra = ra;
	}

	@Override
	public String toString() {
		return (nome + ";" + ra);
	}
	
	@Override
	public int hashCode() {
		return Integer.parseInt(ra.substring(ra.length() - 1));
	}

}

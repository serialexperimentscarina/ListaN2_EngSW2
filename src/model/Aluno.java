package model;

public class Aluno {
	
	private String nome;
	private String ra;
	
	
	// Getters e setters
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

	// toString para gravação em arquivo
	@Override
	public String toString() {
		return (nome + ";" + ra);
	}
	
	// Código hash
	@Override
	public int hashCode() {
		return Integer.parseInt(ra.substring(ra.length() - 1));
	}

}

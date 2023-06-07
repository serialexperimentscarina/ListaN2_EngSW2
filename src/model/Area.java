package model;

import br.com.serialexperimentscarina.listastrings.ListaStrings;

public class Area {
	
	private int codigo;
	private String nome;
	private String descricao;
	private ListaStrings subareas = new ListaStrings();
	
	public int getCodigo() {
		return codigo;
	}


	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public ListaStrings getSubareas() {
		return subareas;
	}


	public void addSubarea(String subarea) throws Exception {
		this.subareas.addLast(subarea);
	}
	
	public void removeSubarea(ListaStrings subareas) {
		this.subareas = subareas;
	}


	@Override
	public String toString() {
		StringBuffer stringSubareas = new StringBuffer("\"");
		int numSubareas = subareas.size();
		
		for (int i = 0; i < numSubareas; i++) {
			try {
				stringSubareas.append(subareas.get(i) + ";");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (codigo + ";" + nome + ";" + descricao + ";" + (stringSubareas.toString().substring(0, stringSubareas.length() - 1)) + "\"");
	}

	
	@Override
	public int hashCode() {
		return (nome.toLowerCase().charAt(0)) % 10;
	}
	
}

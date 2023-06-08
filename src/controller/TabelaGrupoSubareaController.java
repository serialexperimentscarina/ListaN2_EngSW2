package controller;

import br.com.serialexperimentscarina.listaobject.ListaObject;
import model.Trabalho;

public class TabelaGrupoSubareaController {

	ListaObject[] tabelaDeEspalhamento;
	
	public TabelaGrupoSubareaController() {
		tabelaDeEspalhamento = new ListaObject[26];
		
		for (int i = 0; i < 26; i++) {
			tabelaDeEspalhamento[i] = new ListaObject();
		}
	}
	
	// Adiciona trabalho na tabela
	public void adiciona(Trabalho trabalho) throws Exception {
		int hash = trabalho.hashSubarea();
		ListaObject l = tabelaDeEspalhamento[hash];
		
		l.addFirst(trabalho);
	}

	// Consulta trabalho na tabela
	public String busca(Trabalho trabalho) throws Exception {
		int hash = trabalho.hashSubarea();
		ListaObject l = tabelaDeEspalhamento[hash];
		int tamanho = l.size();
		
		String trabalhos = "";
		for (int i = 0; i < tamanho; i++) {
			Trabalho trbl = (Trabalho) l.get(i);
			if(trabalho.getSubarea().equals(trbl.getSubarea())) {
				trabalhos += ("Código: " + trbl.getCodigo() + ", Tipo: " + trbl.getTipo() + ", Tema: " + trbl.getTema() + ", Área:" + trbl.getArea() + ", Subárea: " + trbl.getSubarea() + ", Integrantes: " + trbl.getIntegrantes() + System.getProperty("line.separator"));
			}
		}
		
		return !trabalho.equals("") ? trabalhos : null;
	}
	

}

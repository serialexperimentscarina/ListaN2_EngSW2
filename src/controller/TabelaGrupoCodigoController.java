package controller;

import br.com.serialexperimentscarina.listaobject.ListaObject;
import model.Trabalho;

public class TabelaGrupoCodigoController {

	ListaObject[] tabelaDeEspalhamento;
	
	public TabelaGrupoCodigoController() {
		tabelaDeEspalhamento = new ListaObject[10];
		
		for (int i = 0; i < 10; i++) {
			tabelaDeEspalhamento[i] = new ListaObject();
		}
	}
	
	// Adiciona trabalho na tabela
	public void adiciona(Trabalho trabalho) throws Exception {
		int hash = trabalho.hashCodigo();
		ListaObject l = tabelaDeEspalhamento[hash];
		
		l.addFirst(trabalho);
	}

	// Consulta trabalho na tabela
	public Trabalho busca(Trabalho trabalho) throws Exception {
		int hash = trabalho.hashCodigo();
		ListaObject l = tabelaDeEspalhamento[hash];
		int tamanho = l.size();
		
		for (int i = 0; i < tamanho; i++) {
			Trabalho trbl = (Trabalho) l.get(i);
			if(trabalho.getCodigo() == trbl.getCodigo()) {
				return trbl;
			}
		}
		return null;
	}
	
	// Deleta aluno da tabela
	public boolean remove(Trabalho trabalho) throws Exception {
		int hash = trabalho.hashCodigo();
		ListaObject l = tabelaDeEspalhamento[hash];
		int tamanho = l.size();
		
		for (int i = 0; i < tamanho; i++) {
			Trabalho trbl = (Trabalho) l.get(i);
			if(trbl.getCodigo() == trabalho.getCodigo()) {
				l.remove(i);
				return true;
			}
		}
		return false;
	}
	
	// Lista todos os trabalhos presentes na tabela
	public String lista() throws Exception {
		StringBuffer trabalhos = new StringBuffer("");
		for (int i = 0; i < 10; i++) {
			ListaObject l = tabelaDeEspalhamento[i];
			int tamanho = l.size();
			
			for (int j = 0; j < tamanho; j++) {
				Trabalho trabalho = (Trabalho) l.get(j);
				trabalhos.append("Código: " + trabalho.getCodigo() + ", Tipo: " + trabalho.getTipo() + ", Tema: " + trabalho.getTema() + ", Área:" + trabalho.getArea() + ", Subárea: " + trabalho.getSubarea() + ", Integrantes: " + trabalho.getIntegrantes() + System.getProperty("line.separator"));
			}
		}
		return trabalhos.toString();
		
	}
	

}

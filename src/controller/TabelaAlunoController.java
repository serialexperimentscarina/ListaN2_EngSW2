package controller;

import br.com.serialexperimentscarina.listaobject.ListaObject;
import model.Aluno;

public class TabelaAlunoController {
	
ListaObject[] tabelaDeEspalhamento;
	
	public TabelaAlunoController() {
		tabelaDeEspalhamento = new ListaObject[10];
		
		for (int i = 0; i < 10; i++) {
			tabelaDeEspalhamento[i] = new ListaObject();
		}
	}
	
	// Adiciona aluno na tabela
	public void adiciona(Aluno aluno) throws Exception {
		int hash = aluno.hashCode();
		ListaObject l = tabelaDeEspalhamento[hash];
		
		l.addFirst(aluno);
	}

	// Consulta aluno na tabela
	public Aluno busca(Aluno aluno) throws Exception {
		int hash = aluno.hashCode();
		ListaObject l = tabelaDeEspalhamento[hash];
		int tamanho = l.size();
		
		for (int i = 0; i < tamanho; i++) {
			Aluno al = (Aluno) l.get(i);
			if(al.getRa().equals(aluno.getRa())) {
				return al;
			}
		}
		return null;
	}
	
	// Deleta aluno da tabela
	public boolean remove(Aluno aluno) throws Exception {
		int hash = aluno.hashCode();
		ListaObject l = tabelaDeEspalhamento[hash];
		int tamanho = l.size();
		
		for (int i = 0; i < tamanho; i++) {
			Aluno al = (Aluno) l.get(i);
			if(al.getRa().equals(aluno.getRa())) {
				l.remove(i);
				return true;
			}
		}
		return false;
	}
	
	// Lista todos os alunos presentes na tabela
	public String lista() throws Exception {
		StringBuffer alunos = new StringBuffer("");
		for (int i = 0; i < 10; i++) {
			ListaObject l = tabelaDeEspalhamento[i];
			int tamanho = l.size();
			
			for (int j = 0; j < tamanho; j++) {
				Aluno aluno = (Aluno) l.get(j);
				alunos.append("Nome: " + aluno.getNome() + "; RA: " + aluno.getRa() + System.getProperty("line.separator"));
			}
		}
		
		return alunos.toString();
		
	}

}

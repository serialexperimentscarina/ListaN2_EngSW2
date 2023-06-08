package controller;

public interface IOperacoes {
	
	// Interface para as operações comuns entre todos os controllers
	public void gravar() throws Exception;
	public void excluir() throws Exception;
	public void buscar() throws Exception;
	public void limparBusca() throws Exception; 

}

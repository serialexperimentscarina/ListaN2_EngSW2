package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Orientacao;
import model.Trabalho;

public class OrientacaoController implements ActionListener, IOperacoes{
	
	private JTextField tfOrientacaoBusca;
	private JTextArea taOrientacaoLista;
	private JTextField tfTrabalhoOrientacao;
	private JLabel lblOrientacaoTrabalho;
	private JTextField tfDiaOrientacao;
	private JTextField tfMesOrientacao;
	private JTextField tfAnoOrientacao;
	private JTextArea taPontosOrientacao;
	
	public OrientacaoController(JTextField tfOrientacaoBusca, JTextArea taOrientacaoLista, JTextField tfTrabalhoOrientacao,
			JLabel lblOrientacaoTrabalho, JTextField tfDiaOrientacao, JTextField tfMesOrientacao, JTextField tfAnoOrientacao, 
			JTextArea taPontosOrientacao) {
		this.tfOrientacaoBusca = tfOrientacaoBusca;
		this.taOrientacaoLista = taOrientacaoLista;
		this.tfTrabalhoOrientacao = tfTrabalhoOrientacao;
		this.lblOrientacaoTrabalho = lblOrientacaoTrabalho;
		this.tfDiaOrientacao = tfDiaOrientacao;
		this.tfMesOrientacao = tfMesOrientacao;
		this.tfAnoOrientacao = tfAnoOrientacao;
		this.taPontosOrientacao = taPontosOrientacao;
		
		try {
			InicializaPilhas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		try {
			switch (cmd) {
				case "Gravar":
					gravar();	
					break;
				case "Excluir última":
					excluir();
					break;
				case "Buscar última":
					buscar();	
					break;
				case "Adicionar":
					adicionar();
					break;
				case "Limpar Busca":
					limparBusca();
					break;
				default:
					break;
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a execução do programa", "ERRO!", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	@Override
	public void gravar() throws Exception {
		Orientacao orientacao = new Orientacao();
		
		String dia = tfDiaOrientacao.getText().toString();
		String mes = tfMesOrientacao.getText().toString();
		String ano = tfAnoOrientacao.getText().toString();
		
		if ((dia.equals("") || !dia.matches("[0-9]+") || Integer.valueOf(dia) < 1 || Integer.valueOf(dia) > 31) ||
			(mes.equals("") || !mes.matches("[0-9]+") || Integer.valueOf(mes) < 1 || Integer.valueOf(mes) > 12)	||
			(ano.equals("") || !ano.matches("[0-9]+") || ano.length() > 4 || ano.length() < 4 )) {
			JOptionPane.showMessageDialog(null, "Formato de data vazio ou inválido", "ERRO", JOptionPane.ERROR_MESSAGE);
			return;
		}
		orientacao.setDia(Integer.parseInt(dia));
		orientacao.setMes(Integer.parseInt(mes));
		orientacao.setAno(Integer.parseInt(ano));
		
		orientacao.setPontos(taPontosOrientacao.getText().toString());
		if (orientacao.getPontos().equals("")) {
			JOptionPane.showMessageDialog(null, "Nenhum ponto foi definido para a orientação", "ERRO", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (lblOrientacaoTrabalho.getText().toString().equals("")) {
			JOptionPane.showMessageDialog(null, "Orientação não foi associada à um trabalho!", "ERRO", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Trabalho trabalho = new Trabalho();
		trabalho.setCodigo(Integer.parseInt(lblOrientacaoTrabalho.getText()));
		TrabalhoController.tabelaEspalhamentoGrupoCodigo.busca(trabalho).getOrientacoes().push(orientacao);
		
		gravaOrientacao(orientacao.toString() + ";" + trabalho.getCodigo());
		limparBusca();
		tfDiaOrientacao.setText("");
		tfMesOrientacao.setText("");
		tfAnoOrientacao.setText("");
		taPontosOrientacao.setText("");
		tfTrabalhoOrientacao.setText("");
		JOptionPane.showMessageDialog(null, "Orientação adicionada com sucesso!");
	}

	private void gravaOrientacao(String csvOrientacao) throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File dir = new File(path);

		if (!dir.exists()) {
			dir.mkdir();
		}

		File arq = new File(path, "orientacoes.csv");
		boolean arqExiste = arq.exists();

		FileWriter fw = new FileWriter(arq, arqExiste);
		PrintWriter pw = new PrintWriter(fw);
		pw.write(csvOrientacao + System.getProperty("line.separator"));
		pw.flush();
		pw.close();
		fw.close();
	}
	@Override
	public void excluir() throws Exception {
		if (tfOrientacaoBusca.getText().equals("") || !tfOrientacaoBusca.getText().matches("[0-9]+")) {
			JOptionPane.showMessageDialog(null, "Código inválido!", "ERRO!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Trabalho trabalho = new Trabalho();
		trabalho.setCodigo(Integer.parseInt(tfOrientacaoBusca.getText()));
		trabalho = TrabalhoController.tabelaEspalhamentoGrupoCodigo.busca(trabalho);
		
		if (trabalho != null && !trabalho.getOrientacoes().isEmpty()) {
			Orientacao orientacao = (Orientacao) trabalho.getOrientacoes().pop();
			JOptionPane.showMessageDialog(null, "Última orientação removida com sucesso");
			excluirOrientacao(trabalho.getCodigo(), orientacao);
		} else {
			JOptionPane.showMessageDialog(null, "Trabalho não existe ou não possui orientações", "ERRO!", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private void excluirOrientacao(int codigo, Orientacao orientacao) throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File arq = new File(path, "orientacoes.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader bufferR = new BufferedReader(isr);
			
			File novoArq = new File(path, "temp.csv");
			StringBuffer bufferW = new StringBuffer();
			FileWriter fWriter = new FileWriter(novoArq);
			PrintWriter pWriter = new PrintWriter(fWriter);
			
			String linha = bufferR.readLine();
			boolean removido = false;
			while (linha != null && !removido) {
				String[] vetLinha = linha.split(";");
				if (codigo != Integer.parseInt(vetLinha[4]) || orientacao.getDia() != Integer.parseInt(vetLinha[0]) || orientacao.getMes() != Integer.parseInt(vetLinha[1]) || orientacao.getAno() != Integer.parseInt(vetLinha[2])) {
					bufferW.append(linha + System.getProperty("line.separator"));
				} else {
					removido = true;
				}
				linha = bufferR.readLine();
			}
			
			bufferR.close();
			isr.close();
			fis.close();
			pWriter.write(bufferW.toString());
			pWriter.flush();
			pWriter.close();
			fWriter.close();
			
			arq.delete();
			novoArq.renameTo(arq);
			limparBusca();
		}
	}


	private void adicionar() throws Exception {
		Trabalho trabalho = new Trabalho();
		trabalho.setCodigo(Integer.parseInt(tfTrabalhoOrientacao.getText()));
		trabalho = TrabalhoController.tabelaEspalhamentoGrupoCodigo.busca(trabalho);
		if (trabalho != null) {
			lblOrientacaoTrabalho.setText(String.valueOf(trabalho.getCodigo()));
			tfTrabalhoOrientacao.setText("");
		} else {
			JOptionPane.showMessageDialog(null, "Trabalho com o código informado não encontrado no sistema.",
					"ERRO!", JOptionPane.ERROR_MESSAGE);

		}
	}
	@Override
	public void buscar() throws Exception {
		Trabalho trabalho = new Trabalho();
		trabalho.setCodigo(Integer.parseInt(tfOrientacaoBusca.getText()));
		trabalho = TrabalhoController.tabelaEspalhamentoGrupoCodigo.busca(trabalho);
		if (trabalho != null) {
			try {
				Orientacao orientacaoMaisRecente = (Orientacao) trabalho.getOrientacoes().top();
				taOrientacaoLista.setText("Trabalho #" + trabalho.getCodigo() + " (" + orientacaoMaisRecente.getDia() + "/" + orientacaoMaisRecente.getMes() + "/" + orientacaoMaisRecente.getAno() + ") - " + orientacaoMaisRecente.getPontos() + System.getProperty("line.separator"));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Trabalho com o código informado não possui encontros",
						"ERRO!", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Trabalho com o código informado não encontrado no sistema.",
					"ERRO!", JOptionPane.ERROR_MESSAGE);

		}
	}

	private void InicializaPilhas() throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File arq = new File(path, "orientacoes.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				Orientacao orientacao = new Orientacao();
				orientacao.setDia(Integer.parseInt(vetLinha[0]));
				orientacao.setMes(Integer.parseInt(vetLinha[1]));
				orientacao.setAno(Integer.parseInt(vetLinha[2]));
				orientacao.setPontos(vetLinha[3]);
				Trabalho trabalho = new Trabalho();
				trabalho.setCodigo(Integer.parseInt(vetLinha[4]));
				trabalho = TrabalhoController.tabelaEspalhamentoGrupoCodigo.busca(trabalho);
				
				if (trabalho != null) {
					trabalho.getOrientacoes().push(orientacao);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}

		limparBusca();
	}
	

	@Override
	public void limparBusca() throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File arq = new File(path, "orientacoes.csv");

		taOrientacaoLista.setText("");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				taOrientacaoLista.append("Trabalho #" + vetLinha[4] + " (" + vetLinha[0] + "/" + vetLinha[1] + "/" + vetLinha[2] + ") - " + vetLinha[3] + System.getProperty("line.separator"));
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		
	}
}

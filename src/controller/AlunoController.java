package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.com.serialexperimentscarina.listaobject.ListaObject;
import model.Aluno;

public class AlunoController implements ActionListener, IOperacoes, IUpload {

	private JTextField tfAlunoNome;
	private JTextField tfAlunoRa;
	private JTextArea taAlunoLista;
	private JTextField tfAlunoBusca;
	
	public static TabelaAlunoController tabelaEspalhamentoAluno;
	
	public AlunoController(JTextField tfAlunoNome, JTextField tfAlunoRa, JTextArea taAlunoLista,
			JTextField tfAlunoBusca) {
		this.tfAlunoNome = tfAlunoNome;
		this.tfAlunoRa = tfAlunoRa;
		this.taAlunoLista = taAlunoLista;
		this.tfAlunoBusca = tfAlunoBusca;
		
		tabelaEspalhamentoAluno = new TabelaAlunoController();
		try {
			populaTabela();
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
				case "Excluir por RA":
					excluir();	
					break;
				case "Buscar por RA":
					buscar();	
					break;
				case "Limpar Busca":
					limparBusca();	
					break;
				case "Upload por CSV":
					upload();	
					break;
				default:
					break;
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a execução do programa", "ERRO!", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		
	}

	// Gravar novo aluno
	@Override
	public void gravar() throws Exception {
		Aluno aluno = new Aluno();
		aluno.setNome(tfAlunoNome.getText());
		aluno.setRa(tfAlunoRa.getText());
		
		if (!aluno.getNome().equals("") && (!aluno.getRa().equals("") && aluno.getRa().matches("[0-9]+"))) {
			gravaAluno(aluno.toString());
			tabelaEspalhamentoAluno.adiciona(aluno);
			limparBusca();
			
			tfAlunoNome.setText("");
			tfAlunoRa.setText("");
			JOptionPane.showMessageDialog(null, "Aluno gravado com sucesso.");
		} else {
			JOptionPane.showMessageDialog(null, "Um ou mais campos vazios ou possuem caracteres inválidos", "ERRO!", JOptionPane.ERROR_MESSAGE);
		}

	}
	

	private void gravaAluno(String csvAluno) throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File dir = new File(path);
		
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		File arq = new File(path, "aluno.csv");
		boolean arqExiste = arq.exists();
		
		FileWriter fw = new FileWriter(arq, arqExiste);
		PrintWriter pw = new PrintWriter(fw);
		pw.write(csvAluno + System.getProperty("line.separator"));
		pw.flush();
		pw.close();
		fw.close();
	}
	
	// Deletar aluno gravado
	@Override
	public void excluir() throws Exception {
		Aluno aluno = new Aluno();
		aluno.setRa(tfAlunoBusca.getText());
		
		if (aluno.getRa().equals("") || !aluno.getRa().matches("[0-9]+")) {
			JOptionPane.showMessageDialog(null, "RA Inválido!", "ERRO!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (tabelaEspalhamentoAluno.remove(aluno)) {
			JOptionPane.showMessageDialog(null, "Aluno removido com sucesso");
			excluirAluno(aluno);
		} else {
			JOptionPane.showMessageDialog(null, "Aluno não encontrado", "Erro", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private void excluirAluno(Aluno aluno) throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File arq = new File(path, "aluno.csv");
		
		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader bufferR = new BufferedReader(isr);
			
			File novoArq = new File(path, "temp.csv");
			StringBuffer bufferW = new StringBuffer();
			FileWriter fWriter = new FileWriter(novoArq);
			PrintWriter pWriter = new PrintWriter(fWriter);
			
			String linha = bufferR.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				if (!aluno.getRa().equals(vetLinha[1])) {
					bufferW.append(linha + System.getProperty("line.separator"));
					
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
	
	// Consultar por um aluno
	@Override
	public void buscar() throws Exception {
		Aluno aluno = new Aluno();
		aluno.setRa(tfAlunoBusca.getText());
		
		if (aluno.getRa().equals("") || !aluno.getRa().matches("[0-9]+")) {
			JOptionPane.showMessageDialog(null, "RA Inválido!", "ERRO!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		aluno = tabelaEspalhamentoAluno.busca(aluno);
		if (aluno != null) {
			taAlunoLista.setText("Nome: " + aluno.getNome() + "; RA: " + aluno.getRa());
		} else {
			JOptionPane.showMessageDialog(null, "Aluno não encontrado!", "ERRO!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Limpar consulta
	@Override
	public void limparBusca() throws Exception {
		taAlunoLista.setText(tabelaEspalhamentoAluno.lista());
	}
	
	private void populaTabela() throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File arq = new File(path, "aluno.csv");
		
		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			
			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				Aluno aluno = new Aluno();
				aluno.setNome(vetLinha[0]);
				aluno.setRa(vetLinha[1]);
				tabelaEspalhamentoAluno.adiciona(aluno);
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		limparBusca();
	}
	
	// Gravação de alunos por arquivo CSV
	@Override
	public void upload() throws Exception {
		UploadController uploadCrtl = new UploadController();
		File arquivo = uploadCrtl.uploadArquivo();
		ListaObject listaAluno = new ListaObject();
		
		if (arquivo != null) {
			FileInputStream fInStr = new FileInputStream(arquivo);
			InputStreamReader InStrReader = new InputStreamReader(fInStr);
			BufferedReader bufferReader = new BufferedReader(InStrReader);
			String linha = bufferReader.readLine();
			
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				
				if (vetLinha.length == 2 && !vetLinha[0].equals("") && (!vetLinha[1].equals("") && vetLinha[1].matches("[0-9]+"))) {
					Aluno aluno = new Aluno();
					aluno.setNome(vetLinha[0]);
					aluno.setRa(vetLinha[1]);
					listaAluno.addFirst(aluno);
				} else {
					JOptionPane.showMessageDialog(null, "Um ou mais campos inválidos passados por CSV, verifique seu arquivo e tente novamente", "ERRO!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				linha = bufferReader.readLine();
			}
			bufferReader.close();
			InStrReader.close();
			fInStr.close();
			
			while (!listaAluno.isEmpty()) {
				Aluno aluno = (Aluno) listaAluno.get(0);
				gravaAluno(aluno.toString());
				tabelaEspalhamentoAluno.adiciona(aluno);
				listaAluno.removeFirst();
			}
			
			JOptionPane.showMessageDialog(null, "Upload feito com sucesso", "Upload concluído", JOptionPane.PLAIN_MESSAGE);
			limparBusca();
		}
	}
	
	
}
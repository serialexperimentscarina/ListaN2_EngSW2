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

import model.Area;

public class AreaController implements ActionListener, IOperacoes{
	
	private JTextField tfCodigoArea;
	private JTextField tfNomeArea;
	private JTextArea taDescArea;
	private JTextField tfSubareas;
	private JTextArea taSubareas;
	private JTextArea taAreaLista;
	private JTextField tfAreaBusca;
	
	public static TabelaAreaController tabelaEspalhamentoArea;
	
	public AreaController(JTextField tfCodigoArea, JTextField tfNomeArea, JTextArea taDescArea, JTextField tfSubareas,
			JTextArea taSubareas, JTextArea taAreaLista, JTextField tfAreaBusca) {
		this.tfCodigoArea = tfCodigoArea;
		this.tfNomeArea = tfNomeArea;
		this.taDescArea = taDescArea;
		this.tfSubareas = tfSubareas;
		this.taSubareas = taSubareas;
		this.taAreaLista = taAreaLista;
		this.tfAreaBusca = tfAreaBusca;
		
		tabelaEspalhamentoArea = new TabelaAreaController();
		
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
				case "Adicionar":
					adicionar();	
					break;
				case "Remover":
					remover();
					break;
				case "Excluir":
					excluir();	
					break;
				case "Buscar":
					buscar();	
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
		Area area = new Area();
		if (!tfCodigoArea.getText().equals("") && tfCodigoArea.getText().matches("[0-9]+")) {
			area.codigo = Integer.parseInt(tfCodigoArea.getText());
		} else {
			JOptionPane.showMessageDialog(null, "Código de área não pode estar vazio e deve ser numérico", "ERRO!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		area.nome = tfNomeArea.getText();
		area.descricao = taDescArea.getText();
		
		if (!taSubareas.getText().equals("")) {
			String[] listaSubareas = taSubareas.getText().split(System.getProperty("line.separator"));
			int numSubareas = listaSubareas.length;
			for (int i = 0; i < numSubareas; i++) {
				area.subareas.addFirst(listaSubareas[i]); 
			}
		} else {
			JOptionPane.showMessageDialog(null, "É necessário cadastrar subáreas para a área", "ERRO!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (!area.nome.equals("") && !area.descricao.equals("")) {
			gravaArea(area.toString());
			tabelaEspalhamentoArea.adiciona(area);
			limparBusca();
			
			tfCodigoArea.setText("");
			tfNomeArea.setText("");
			taDescArea.setText("");
			tfSubareas.setText("");
			taSubareas.setText("");
			
			JOptionPane.showMessageDialog(null, "Área gravada com sucesso.");
		} else {
			JOptionPane.showMessageDialog(null, "Um ou mais campos vazios ou possuem caracteres inválidos", "ERRO!", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void gravaArea(String csvArea) throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File dir = new File(path);
		
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		File arq = new File(path, "area.csv");
		boolean arqExiste = arq.exists();
		
		FileWriter fw = new FileWriter(arq, arqExiste);
		PrintWriter pw = new PrintWriter(fw);
		pw.write(csvArea + System.getProperty("line.separator"));
		pw.flush();
		pw.close();
		fw.close();
	}
	
	private void adicionar() {
		String subarea = tfSubareas.getText();
		if (!subarea.equals("")) {
			taSubareas.append(subarea + System.getProperty("line.separator"));
			tfSubareas.setText("");
			
			JOptionPane.showMessageDialog(null, "Subárea adicionada com sucesso.");
		}
	}
	
	private void remover() throws Exception {
		String subarea = tfSubareas.getText();
		if (!subarea.equals("") && taSubareas.getText().contains(subarea)) {
			String[] subareas = taSubareas.getText().split(System.getProperty("line.separator"));
			
			StringBuffer buffer = new StringBuffer();
			for (String sub : subareas) {
				if (!sub.equals(subarea)) {
					buffer.append(sub + System.getProperty("line.separator"));
				}
			}
		
			taSubareas.setText(buffer.toString());
			JOptionPane.showMessageDialog(null, "Subárea removida com sucesso.");
		} else {
			JOptionPane.showMessageDialog(null, "Subárea não adicionada.", "ERRO!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void excluir() throws Exception {
		Area area = new Area();
		area.nome = tfAreaBusca.getText();
		
		if (tabelaEspalhamentoArea.remove(area)) {
			JOptionPane.showMessageDialog(null, "Área removida com sucesso");
			excluirArea(area);
		} else {
			JOptionPane.showMessageDialog(null, "Área não encontrada", "ERRO!", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private void excluirArea(Area area) throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File arq = new File(path, "area.csv");
		
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
				if (!area.nome.equals(vetLinha[2])) {
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

	@Override
	public void buscar() throws Exception {
		Area area = new Area();
		area.nome =tfAreaBusca.getText();
		
		if (area.nome.equals("")) {
			JOptionPane.showMessageDialog(null, "Nome Inválido!", "ERRO!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		area = tabelaEspalhamentoArea.busca(area);
		if (area != null) {
			String stringSubareas = "; Subáreas: ";
			int numSubareas = area.subareas.size();
			for (int i = 0; i < numSubareas; i++) {
				stringSubareas += (area.subareas.get(i) + ", ");
			}
			taAreaLista.setText("Código: " + area.codigo + "; Área: " + area.nome + "; Descrição: " + area.descricao + stringSubareas.substring(0, stringSubareas.length() - 2));
		} else {
			JOptionPane.showMessageDialog(null, "Área não encontrada!", "ERRO!", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void limparBusca() throws Exception {
			taAreaLista.setText(tabelaEspalhamentoArea.lista());
	}
	
	private void populaTabela() throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File arq = new File(path, "area.csv");
		
		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			
			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
				Area area = new Area();
				area.codigo = Integer.parseInt(vetLinha[0]);
				area.nome = vetLinha[1];
				area.descricao = vetLinha[2];
				
				String[] listaDeSubareas = vetLinha[3].substring(1, vetLinha[3].length() - 1).split(";");
				int numSubareas = listaDeSubareas.length;
				for (int i = 0; i < numSubareas; i++) {
					area.subareas.addFirst(listaDeSubareas[i]); 
				}
				tabelaEspalhamentoArea.adiciona(area);
				linha = buffer.readLine();
				
			}
			buffer.close();
			isr.close();
			fis.close();
			
			limparBusca();
		}
	}
	
	

}

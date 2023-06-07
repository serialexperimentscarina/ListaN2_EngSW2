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

import br.com.serialexperimentscarina.listaobject.ListaObject;
import model.Aluno;
import model.Area;
import model.Trabalho;

public class TrabalhoController implements ActionListener, IOperacoes {

	private JTextField tfTrabalhoCodigo;
	private JTextField tfTrabalhoTipo;
	private JTextField tfTrabalhoTema;
	private JTextField tfTrabalhoArea;
	private JTextField tfTrabalhoSubarea;
	private JLabel lblBuscaIntegrante;
	private JTextField tfTrabalhoBusca;
	private JTextArea taTrabalhoLista;
	private JTextField tfBuscaIntegrante;
	
	public static TabelaGrupoCodigoController tabelaEspalhamentoGrupoCodigo;
	public static TabelaGrupoSubareaController tabelaEspalhamentoGrupoSubarea;
	public static String cmd = "";

	private static int numIntegrantes;

	public TrabalhoController(JTextField tfTrabalhoCodigo, JTextField tfTrabalhoTipo, JTextField tfTrabalhoTema,
			JTextField tfTrabalhoArea, JTextField tfTrabalhoSubarea, JLabel lblBuscaIntegrante,
			JTextField tfTrabalhoBusca, JTextArea taTrabalhoLista, JTextField tfBuscaIntegrante) {
		this.tfTrabalhoCodigo = tfTrabalhoCodigo;
		this.tfTrabalhoTipo = tfTrabalhoTipo;
		this.tfTrabalhoTema = tfTrabalhoTema;
		this.tfTrabalhoArea = tfTrabalhoArea;
		this.tfTrabalhoSubarea = tfTrabalhoSubarea;
		this.lblBuscaIntegrante = lblBuscaIntegrante;
		this.tfTrabalhoBusca = tfTrabalhoBusca;
		this.taTrabalhoLista = taTrabalhoLista;
		this.tfBuscaIntegrante = tfBuscaIntegrante;
	
		numIntegrantes = 0;
		
		tabelaEspalhamentoGrupoCodigo = new TabelaGrupoCodigoController();
		tabelaEspalhamentoGrupoSubarea = new TabelaGrupoSubareaController();
		try {
			populaTabelas();
			limparBusca();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a execução do programa", "ERRO!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		cmd = e.getActionCommand();
		try {
			switch (cmd) {
				case "Gravar":
					gravar();	
					break;
				case "Excluir":
					excluir();
					break;
				case "Buscar por código":
					buscar();	
					break;
				case "Buscar por subárea":
					buscar();	
					break;
				case "Upload por CSV":
					upload();	
					break;
				case "Limpar Busca":
					limparBusca();
					break;
				case "Adicionar":
					adicionar();	
					break;
				case "Remover":
					remover();	
					break;
				default:
					break;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
	
	private void adicionar() throws Exception {
		Aluno aluno = new Aluno();
		if (tfBuscaIntegrante.getText().toString().equals("") || !tfBuscaIntegrante.getText().toString().matches("[0-9]+")) {
			JOptionPane.showMessageDialog(null, "Número de RA do aluno inválido");
			return;
		}
		
		aluno.setRa(tfBuscaIntegrante.getText());
		aluno = AlunoController.tabelaEspalhamentoAluno.busca(aluno);
		
		
		if (numIntegrantes < 4) {
			if (aluno != null) {
				String integrantes = lblBuscaIntegrante.getText().toString();
				
				if(integrantes.contains(aluno.getNome())) {
					JOptionPane.showMessageDialog(null, "Aluno já está adicionado neste trabalho.");
					return;
				}
				
				numIntegrantes++;
				if (integrantes.equals("")) {
					lblBuscaIntegrante.setText(aluno.getNome());
				} else {
					lblBuscaIntegrante.setText(lblBuscaIntegrante.getText().toString() + ", " + aluno.getNome());
				}
				JOptionPane.showMessageDialog(null, "Aluno adicionado com sucesso.");
				tfBuscaIntegrante.setText("");
			} else {
				JOptionPane.showMessageDialog(null, "Nenhum aluno encontrado com esse RA.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Grupo está com o limite de integrantes!");
		}
	}
	
	private void remover() throws Exception {
		if (tfBuscaIntegrante.getText().toString().equals("") || !tfBuscaIntegrante.getText().toString().matches("[0-9]+")) {
			JOptionPane.showMessageDialog(null, "Número de RA do aluno inválido");
			return;
		}
		
		Aluno aluno = new Aluno();
		aluno.setRa(tfBuscaIntegrante.getText());
		aluno = AlunoController.tabelaEspalhamentoAluno.busca(aluno);
		String listaDeNomes = lblBuscaIntegrante.getText();
		StringBuilder listaDeNomesAtualizada = new StringBuilder();

		if (listaDeNomes.contains(aluno.getNome())) {
			String[] nomes = listaDeNomes.split(", ");

			for (String nome : nomes) {
				if (nome.equals(aluno.getNome())) {
					JOptionPane.showMessageDialog(null, "Aluno removido com sucesso.");
					numIntegrantes--;
				} else {
					if (listaDeNomesAtualizada.length() > 0) {
						listaDeNomesAtualizada.append(", ");
					}
					listaDeNomesAtualizada.append(nome);
				}
			}
			lblBuscaIntegrante.setText(listaDeNomesAtualizada.toString());
		} else {
			JOptionPane.showMessageDialog(null, "Aluno não encontrado.");
		}
	}
	@Override
	public void excluir() throws Exception {
		if (tfTrabalhoBusca.getText().equals("") || !tfTrabalhoBusca.getText().matches("[0-9]+")) {
			JOptionPane.showMessageDialog(null, "Código inválido!", "ERRO!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Trabalho trabalho = new Trabalho();
		trabalho.setCodigo(Integer.parseInt(tfTrabalhoBusca.getText()));
		
		if (tabelaEspalhamentoGrupoCodigo.remove(trabalho)) {
			JOptionPane.showMessageDialog(null, "Trabalho removido com sucesso");
			excluirTrabalho(trabalho);
		} else {
			JOptionPane.showMessageDialog(null, "Área não encontrada", "ERRO!", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private void excluirTrabalho(Trabalho trabalho) throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File arq = new File(path, "trabalho.csv");
		
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
				if (trabalho.getCodigo() != Integer.parseInt(vetLinha[0])) {
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
		if (cmd.equals("Buscar por código")) {
			buscarCodigo();
		} else if (cmd.equals("Buscar por subárea")) {
			buscarArea();
		} 
	}


	private void buscarCodigo() throws Exception {
		if (tfTrabalhoBusca.getText().equals("") || !tfTrabalhoBusca.getText().matches("[0-9]+")) {
			JOptionPane.showMessageDialog(null, "Código inválido!", "ERRO!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Trabalho trabalho = new Trabalho();
		trabalho.setCodigo(Integer.parseInt(tfTrabalhoBusca.getText()));
		
		trabalho = tabelaEspalhamentoGrupoCodigo.busca(trabalho);
		if (trabalho != null) {
			taTrabalhoLista.setText("Código: " + trabalho.getCodigo() + ", Tipo: " + trabalho.getTipo() + ", Tema: " + trabalho.getTema() + ", Área:" + trabalho.getArea() + ", Subárea: " + trabalho.getSubarea() + ", Integrantes: " + trabalho.getIntegrantes());
		} else {
			JOptionPane.showMessageDialog(null, "Trabalho não encontrado!", "ERRO!", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void buscarArea() throws Exception {
		Trabalho trabalho = new Trabalho();
		trabalho.setSubarea(tfTrabalhoBusca.getText());
		
		String trabalhos = tabelaEspalhamentoGrupoSubarea.busca(trabalho);
		if (!trabalhos.equals("")) {
			taTrabalhoLista.setText(trabalhos);
		} else {
			JOptionPane.showMessageDialog(null, "Trabalho não encontrado!", "ERRO!", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	@Override
	public void gravar() throws Exception {
		if (numIntegrantes >= 1) {
			Trabalho trabalho = new Trabalho();
			trabalho.setCodigo(Integer.parseInt(tfTrabalhoCodigo.getText()));
			trabalho.setTipo(tfTrabalhoTipo.getText());
			trabalho.setTema(tfTrabalhoTema.getText());
			trabalho.setSubarea(tfTrabalhoSubarea.getText());
			trabalho.setIntegrantes(lblBuscaIntegrante.getText());
			
			if (trabalho.getTema().equals("") || trabalho.getTipo().equals("")) {
				JOptionPane.showMessageDialog(null, "Um ou mais campos vazios ou possuem caracteres inválidos", "ERRO!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Trocar aqui
			Area area = new Area();
			area.nome = tfTrabalhoArea.getText();
			area = AreaController.tabelaEspalhamentoArea.busca(area);
			
			if (area == null) {
				JOptionPane.showMessageDialog(null, "Área não cadastrada no sistema", "ERRO!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			trabalho.setArea(area.nome);
			
			boolean subareaExiste = false;
			int totalSubareas = area.subareas.size();
			
			for (int i = 0; i < totalSubareas; i++) {

				if (area.subareas.get(i).equals(trabalho.getSubarea())) {
					subareaExiste = true;
					break;
				}
			}
			if(!subareaExiste) {
				JOptionPane.showMessageDialog(null, "Subárea não cadastrada no sistema", "ERRO!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			gravaTrabalho(trabalho.toString());
			tabelaEspalhamentoGrupoCodigo.adiciona(trabalho);
			tabelaEspalhamentoGrupoSubarea.adiciona(trabalho);
			tfTrabalhoCodigo.setText("");
			tfTrabalhoTipo.setText("");
			tfTrabalhoTema.setText("");
			tfTrabalhoArea.setText("");
			tfTrabalhoSubarea.setText("");
			lblBuscaIntegrante.setText("");
			numIntegrantes = 0;
			JOptionPane.showMessageDialog(null, "Trabalho gravado com sucesso!");
		} else {
			JOptionPane.showMessageDialog(null, "Quantidade de integrantes inválida! Mínimo: 2");
		}
	}

	private void gravaTrabalho(String csvTrabalho) throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File dir = new File(path);

		if (!dir.exists()) {
			dir.mkdir();
		}

		File arq = new File(path, "trabalho.csv");
		boolean arqExiste = arq.exists();

		FileWriter fw = new FileWriter(arq, arqExiste);
		PrintWriter pw = new PrintWriter(fw);
		pw.write(csvTrabalho + System.getProperty("line.separator"));
		pw.flush();
		pw.close();
		fw.close();

	}

	private void upload() throws Exception {
		UploadController uploadCrtl = new UploadController();
		File arquivo = uploadCrtl.uploadArquivo();
		ListaObject listaTrabalho = new ListaObject();

		if (arquivo != null) {
			FileInputStream fInStr = new FileInputStream(arquivo);
			InputStreamReader InStrReader = new InputStreamReader(fInStr);
			BufferedReader bufferReader = new BufferedReader(InStrReader);
			String linha = bufferReader.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");
				if (vetLinha[0].matches("[0-9]+") && (!vetLinha[1].equals("")) && (!vetLinha[2].equals(""))
						&& (!vetLinha[3].equals("")) && (!vetLinha[4].equals(""))) {
					Trabalho trabalho = new Trabalho();
					trabalho.setCodigo(Integer.parseInt(vetLinha[0]));
					trabalho.setTipo(vetLinha[1]);
					trabalho.setTema(vetLinha[2]);
					// Trocar aqui
					Area area = new Area();
					area.nome = vetLinha[3];
					area = AreaController.tabelaEspalhamentoArea.busca(area);
					
					if (area == null) {
						JOptionPane.showMessageDialog(null, "Um dos campos de área possui uma área não cadastrada no sistema", "ERRO!",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					trabalho.setArea(vetLinha[3]);
					trabalho.setSubarea(vetLinha[4]);
					
					boolean subareaExiste = false;
					// Trocar aqui
					int totalSubareas = area.subareas.size();
					
					for (int i = 0; i < totalSubareas; i++) {
						// Trocar aqui
						if (area.subareas.get(i).equals(trabalho.getSubarea())) {
							subareaExiste = true;
							break;
						}
					}
					if(!subareaExiste) {
						JOptionPane.showMessageDialog(null, "Um dos campos de subárea possui uma subárea não cadastrada no sistema", "ERRO!",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					String[] integrantes = vetLinha[5].split(",");
					if(integrantes.length < 2 || integrantes.length > 4) {
						JOptionPane.showMessageDialog(null, "Um dos campos de integrante possui um número inválido de integrantes", "ERRO!",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					trabalho.setIntegrantes(vetLinha[5]);
					
					listaTrabalho.addFirst(trabalho);
				} else {
					JOptionPane.showMessageDialog(null,
							"Um ou mais campos inválidos passados por CSV, verifique seu arquivo e tente novamente",
							"ERRO!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				linha = bufferReader.readLine();
			}
			bufferReader.close();
			InStrReader.close();
			fInStr.close();

			while (!listaTrabalho.isEmpty()) {
				Trabalho trabalho = (Trabalho) listaTrabalho.get(0);
				gravaTrabalho(trabalho.toString());
				tabelaEspalhamentoGrupoCodigo.adiciona(trabalho);
				tabelaEspalhamentoGrupoSubarea.adiciona(trabalho);
				listaTrabalho.removeFirst();
			}

			limparBusca();
			JOptionPane.showMessageDialog(null, "Upload feito com sucesso", "Upload concluído",
					JOptionPane.PLAIN_MESSAGE);

		}
	}

	private void populaTabelas() throws Exception {
		String path = (System.getProperty("user.home") + File.separator + "SistemaTCC");
		File arq = new File(path, "trabalho.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				Trabalho trabalho = new Trabalho();
				trabalho.setCodigo(Integer.parseInt(vetLinha[0]));
				trabalho.setTipo(vetLinha[1]);
				trabalho.setTema(vetLinha[2]);
				trabalho.setArea(vetLinha[3]);
				trabalho.setSubarea(vetLinha[4]);
				trabalho.setIntegrantes(vetLinha[5]);
				tabelaEspalhamentoGrupoCodigo.adiciona(trabalho);
				tabelaEspalhamentoGrupoSubarea.adiciona(trabalho);
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
	}

	

	@Override
	public void limparBusca() throws Exception {
		taTrabalhoLista.setText(tabelaEspalhamentoGrupoCodigo.lista());
	}
	
}

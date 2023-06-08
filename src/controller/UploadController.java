package controller;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UploadController {

	// Método comúm para upload de arquivos CSV
	public File uploadArquivo() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos separados por vírgula (.CSV)", "csv");
		String diretorio = (System.getProperty("user.home") + "/Desktop");
		File dir = new File(diretorio);
		JFileChooser chooser = new JFileChooser();
		
		chooser.setCurrentDirectory(dir);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(filter);
		
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		return null;
	}

}

package hidensource.aftp;

import processing.core.PApplet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class AFTPClient {

	PApplet parent;
	private FTPClient ftp;
	private String fileName = "";
	private String rootDir = "public_html/";

	/**
	 * Contructor
	 * 
	 * @param parent
	 *            PApplet (this)
	 * @param host
	 *            String Dominio del host
	 * @param port
	 *            int Puerto / Port
	 * @param user
	 *            String Usuario de la cuenta FTP / FTP user account
	 * @param pwd
	 *            Pass de la cuenta FTP / FTP pass
	 *
	 */
	public AFTPClient(PApplet parent, String host, int port, String user, String pwd) {
		ftp = new FTPClient();
		try {
			ftp.connect(host, port);
			if (!ftp.login(user, pwd)) {
				ftp.logout();
				PApplet.println("no se pudo loguear");
			} else {
				ftp.enterLocalPassiveMode();
				ftp.setFileType(FTP.BINARY_FILE_TYPE);
				System.out.println("Current directory is " + ftp.printWorkingDirectory());
			}
		} catch (Exception e) {
			PApplet.println("no se pudo inicializar libreria");
		}

	}

	/**
	 * 
	 * @param fn
	 *            String nombre del archivo / File name
	 */

	public void setFileName(String fn) {
		fileName = fn;
	}

	/**
	 * 
	 * @param dir
	 *            Nombre del directorio a crear / Dir name
	 * @return
	 */
	public boolean createDir(String dir) {
		try {
			ftp.makeDirectory(dir);
			return true;
		} catch (Exception e) {
			PApplet.println("createDir no pudo crear el directorio");
			return true;
		}
	}

	/**
	 * 
	 * @param _rd
	 *            Configura el directorio raiz
	 * 
	 *            public_html por defecto / default
	 */
	public void setRootDir(String _rd) {
		rootDir = _rd;
	}

	/**
	 * 
	 * @param _rd
	 * @return String directorio raiz
	 */
	public String getRootDir() {
		return rootDir;
	}

	/**
	 * 
	 * @param localfile
	 * @param hostDir
	 * @throws Exception
	 */
	public void uploadFile(String localfile, String hostDir) throws Exception {
		if (fileName == "") {

			Path p = Paths.get(localfile);
			fileName = p.getFileName().toString();
		}
		ftp.changeWorkingDirectory("public_html/" + hostDir);
		System.out.println("Current directory is " + ftp.printWorkingDirectory());
		// APPROACH #1: uploads first file using an InputStream
		File firstLocalFile = new File(localfile);

		String firstRemoteFile = fileName;
		InputStream inputStream = new FileInputStream(firstLocalFile);

		System.out.println("Start uploading first file");
		boolean done = ftp.storeFile(firstRemoteFile, inputStream);
		inputStream.close();
		if (done) {
			System.out.println("The first file is uploaded successfully.");
		}
	}

	/**
	 * Desconecta
	 * 
	 */
	public void disconnect() {
		if (this.ftp.isConnected()) {
			try {
				this.ftp.logout();
				this.ftp.disconnect();
			} catch (IOException f) {
				// do nothing as file is already saved to server
			}
		}
	}

}

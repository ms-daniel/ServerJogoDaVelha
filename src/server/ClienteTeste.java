package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ClienteTeste {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket conexao = new Socket("25.68.251.110", 6789);
		
		BufferedReader conexao_entrada = new BufferedReader(
				new InputStreamReader(conexao.getInputStream()));
		
		//aqui manda vara pro cliente
		DataOutputStream conexao_saida = new DataOutputStream(
				conexao.getOutputStream());
		
		String msg = "";
		while(true) {
			msg = JOptionPane.showInputDialog(null, "sei la");
			System.out.println("cliente: " + msg);
			conexao_saida.writeBytes(msg + '\n');
			if(msg == "sair") 
				break;
		}
		
		conexao_entrada.close();
		conexao_saida.close();
	}

}

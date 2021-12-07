package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.swing.JOptionPane;

public class ClienteTeste {

	public static void main(String[] args) throws IOException {
		Socket conexao = new Socket("170.233.205.93", 9000);
		
		
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

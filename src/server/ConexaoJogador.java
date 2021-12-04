package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;

public class ConexaoJogador extends Thread{
	private Socket conexao;
	private int cont = 3;
	
	//recebe dado do cliente
	private BufferedReader conexao_entrada;
	
	//aqui manda dado pro cliente
	private DataOutputStream conexao_saida;
	
	public ConexaoJogador(Socket cone) {
		this.conexao = cone;
	}
	
	public void rodar() {
		try {
			//aqui recebe a vara do cliente
			conexao_entrada = new BufferedReader(
					new InputStreamReader(conexao.getInputStream()));
			
			//aqui manda vara pro cliente
			conexao_saida = new DataOutputStream(
					conexao.getOutputStream());
			
			conexao.setSoTimeout(60000);
			String msg;
			
			while(!conexao_entrada.equals(null) || conexao_entrada.toString().equals(
					new String("sair"))) {
				System.out.println("tempo restante: " + conexao.getSoTimeout());
				
				try {
					msg ="Cliente: " + conexao_entrada.readLine();
					JOptionPane.showMessageDialog(null, msg);
					System.out.println("mandando!");
					conexao_saida.writeBytes("foda-se" + '\n');

					
				}catch(SocketException e) {
					System.out.println("Conex�o perdida!");
					
					conexao_saida.writeBytes("sair" + '\n');
					
					conexao_entrada.close();
					conexao_saida.close();
					conexao.close();
					break;
				}catch(SocketTimeoutException f) {
					System.out.println("Conex�o limite atingida!");
					conexao_saida.writeBytes("sair" + '\n');
					
					conexao_entrada.close();
					conexao_saida.close();
					conexao.close();
					break;
				}
					cont--;
			}
			
			//conexao_saida.writeBytes("foda-se otario" + '\n');
			
			conexao_entrada.close();
			conexao_saida.close();
			conexao.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void beClose() throws IOException {
		conexao_saida.writeBytes("sair" + '\n');
		conexao_entrada.close();
		conexao_saida.close();
		conexao.close();
	}

}

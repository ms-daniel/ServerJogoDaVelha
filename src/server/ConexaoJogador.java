package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ConexaoJogador extends Thread{
	private Socket conexao;
	private Servidor serve;
	private int cont = 3;
	private boolean first;
	
	private boolean win = false; //se há jogador
	
	private JTextField boxPlay;
	
	//recebe dado do cliente
	private BufferedReader conexao_entrada;
	
	//aqui manda dado pro cliente
	private DataOutputStream conexao_saida;
	
	public ConexaoJogador(Socket cone, JTextField boxP) {
		this.conexao = cone;
		this.boxPlay = boxP;
	}
	
	//quando o jogador 2 se conecta é mandada uma notificação ao jogador 1
	public void notificar(Socket p1, Socket p2) {
		try {
			//mandar done pro socket 1
			DataOutputStream conexaop1 = new DataOutputStream(p1.getOutputStream());
			conexaop1.writeBytes("D" + '\n');
			
			//mandar done pro socket 2
			DataOutputStream conexaop2 = new DataOutputStream(p2.getOutputStream());
			conexaop2.writeBytes("D" + '\n');
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//usdo para saber qual jogador começa primeiro
	public void First(boolean tf) {
		this.first = tf;
		
		//fazer a conexão dos canais de entrada e de saida de dados
		try {
			//aqui recebe do cliente
			conexao_entrada = new BufferedReader(
					new InputStreamReader(conexao.getInputStream()));
			//aqui manda pro cliente
			conexao_saida = new DataOutputStream(
					conexao.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rodar() {
		while(!win) {
			synchronized (conexao_entrada) {
				 
			}
		}
		try {
			
			
			
			conexao.setSoTimeout(10000);
			
			while(!conexao_entrada.equals(null) || conexao_entrada.toString().equals(
					new String("sair"))) {
				System.out.println("tempo restante: " + conexao.getSoTimeout());
				
				try {
					conexao_entrada.readLine(); //recebendo do cliente
//					System.out.println("recebido!");
					
				}catch(SocketException e) {
					System.out.println("Conexão perdida!");
				
					//metodo da class servidor diminui quantidade de jogadores
					Servidor.lessPlayers();
					
					conexao_saida.writeBytes("sair" + '\n');
					
					conexao_entrada.close();
					conexao_saida.close();
					conexao.close();
					break;
				}catch(SocketTimeoutException f) {
					System.out.println("Passou a vez!");
//					conexao_saida.writeBytes("sair" + '\n');
//					
//					//metodo da class servidor diminui quantidade de jogadores
//					Servidor.lessPlayers();
//					
//					conexao_entrada.close();
//					conexao_saida.close();
//					conexao.close();
					break;
				}
					cont--;
			}
			
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
	
	public boolean hasConnection() {
		if(this.conexao.isConnected())
			return true;
		else	
			return false;
	}

}

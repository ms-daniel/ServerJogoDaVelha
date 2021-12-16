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
	private boolean first; //pra saber quem começa
	private String nome;
	private String toCliente = "";
	
	private boolean win = false; //se há jogador
	
	private JTextField boxPlay;
	
	//recebe dado do cliente
	private BufferedReader conexao_entrada;
	
	//aqui manda dado pro cliente
	private DataOutputStream conexao_saida;
	
	private DataOutputStream conexao_saida_p2; //saida para player 2
	
	public ConexaoJogador(Socket cone, JTextField boxP) {
		this.conexao = cone;
		this.boxPlay = boxP;
	}
	
	public void setNome(String name) {
		nome = name;
	}
	
	public void setSaidaPlayer2(DataOutputStream player) {
		conexao_saida_p2 = player;
	}
	
	public boolean Winner() {
		return win;
	}
	
	//quando o jogador 2 se conecta é mandada uma notificação ao jogador 1
	public void notificar(Socket p1, Socket p2) {
		try {
			//um sleep para poder esperar e os usuarios receberem a msg de done
			//Thread.sleep(1000);
			//mandar done pro socket 1
			DataOutputStream conexaop1 = new DataOutputStream(p1.getOutputStream());
			conexaop1.writeBytes("D" + '\n');
			
			//mandar done pro socket 2
			DataOutputStream conexaop2 = new DataOutputStream(p2.getOutputStream());
			conexaop2.writeBytes("D" + '\n');
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	//usdo para saber qual jogador começa primeiro
	public void First(boolean tf) {
		this.first = tf;
		String msg = "";
		
		if(tf) 
			msg = "first";
		else
			msg = "second";
		
		//fazer a conexão dos canais de entrada e de saida de dados
		try {
			//aqui recebe do cliente
			conexao_entrada = new BufferedReader(
					new InputStreamReader(conexao.getInputStream()));
			//aqui manda pro cliente
			conexao_saida = new DataOutputStream(
					conexao.getOutputStream());
			
			conexao_saida.writeBytes(msg + '\n');
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void iniciar() {
		rodar();
	}
	
	public void rodar() {
			try {
					System.out.println(nome);
					try {
							System.out.println("do cliente: " + (toCliente = conexao_entrada.readLine()));
							conexao_saida_p2.writeBytes(toCliente + '\n');
					} catch (SocketException e) {
						Servidor.lessPlayers();
						
						conexao_saida.writeBytes("sair" + '\n');
						
						conexao_entrada.close();
						conexao_saida.close();
						conexao_saida_p2.close();
						conexao.close();
						e.printStackTrace();
					} catch(SocketTimeoutException e) {
						Servidor.lessPlayers();
					}catch (IOException e) {
						
						e.printStackTrace();
					}
				
			
			}catch (IOException e) {
				
			}

//		try {
//			
//			
//			
//			conexao.setSoTimeout(10000);
//			
//			while(!conexao_entrada.equals(null) || conexao_entrada.toString().equals(
//					new String("sair"))) {
//				System.out.println("tempo restante: " + conexao.getSoTimeout());
//				
//				try {
//					conexao_entrada.readLine(); //recebendo do cliente
////					System.out.println("recebido!");
//					
//				}catch(SocketException e) {
//					System.out.println("Conexão perdida!");
//				
//					//metodo da class servidor diminui quantidade de jogadores
//					Servidor.lessPlayers();
//					
//					conexao_saida.writeBytes("sair" + '\n');
//					
//					conexao_entrada.close();
//					conexao_saida.close();
//					conexao.close();
//					break;
//				}catch(SocketTimeoutException f) {
//					System.out.println("Passou a vez!");
////					conexao_saida.writeBytes("sair" + '\n');
////					
////					//metodo da class servidor diminui quantidade de jogadores
////					Servidor.lessPlayers();
////					
////					conexao_entrada.close();
////					conexao_saida.close();
////					conexao.close();
//					break;
//				}
//					cont--;
//			}
//			
//			conexao_entrada.close();
//			conexao_saida.close();
//			conexao.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void beClose() throws IOException {
		conexao_saida.writeBytes("sair" + '\n');
		conexao_entrada.close();
		conexao_saida.close();
		conexao.close();
	}
	
	public Socket getSocket() {
		return this.conexao;
	}
	
	public boolean hasConnection() {
		if(this.conexao.isConnected())
			return true;
		else	
			return false;
	}

}

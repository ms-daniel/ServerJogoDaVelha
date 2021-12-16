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
	
	private int freq = 0;
	private static String win_number = "";
	private static boolean win = false; //se há jogador vencedor
	private static int[][] matriz = new int[3][3]; //matriz do tavbuleiro
	private static int cont = 0;
	
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
		
			e.printStackTrace();
		}
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
					System.out.println("jogador : " + nome);
					try {
						if(!win)
							System.out.println("do cliente: " + (toCliente = conexao_entrada.readLine()));
						
							if(toCliente.matches("[0-9]+")) {
								putMatriz(Integer.parseInt(toCliente), Integer.parseInt(nome));
								cont++;
								PrintMatriz();
							}
							
							
							if(!win) {
								conexao_saida_p2.writeBytes(toCliente + '\n'); //envia dado para o outro cliente
							}
							else {
								System.out.println("\n\nVencedor: " + win_number + "\n nome: " + nome);
								if(!win_number.equals(nome))
									conexao_saida_p2.writeBytes("V" + '\n');
								else
									conexao_saida_p2.writeBytes("P" + '\n');
							}
							if(cont > 3 && HaveWinner()) {
								System.out.println("Temos um vencedor");
								win = true;
							}
							
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
	}
	
	//poe numeros na matriz
	private void putMatriz(int pos, int cli) { //recebe a posição e o numero do cliente
		if(pos == 1) {
			matriz[0][0] = cli;
		}
		else if(pos == 2) {
			matriz[0][1] = cli;
		}
		else if(pos == 3) {
			matriz[0][2] = cli;
		}
		else if(pos == 4) {
			matriz[1][0] = cli;
		}
		else if(pos == 5) {
			matriz[1][1] = cli;
		}
		else if(pos == 6) {
			matriz[1][2] = cli;
		}
		else if(pos == 7) {
			matriz[2][0] = cli;
		}
		else if(pos == 8) {
			matriz[2][1] = cli;
		}
		else if(pos == 9) {
			matriz[2][2] = cli;
		}
	}
	
	//mostra a matriz, futuramente colocar isso em text field na parte grafica e atualizar em tempo real
	private void PrintMatriz() {
		System.out.println("\nMatriz: ");
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				System.out.print(matriz[i][j] + " ");
			}
			System.out.println("");
		}
	}
	
	private boolean HaveWinner() {
		
		//linha a linha
		for(int i = 0; i <3; i++) {
			for(int j = 0; j <3; j++) {
				
				if(j == 0 && matriz[i][j] != 0) 
					freq = matriz[i][j];
				
				if(j > 0 && (matriz[i][j] == 0 || matriz[i][j] != freq)) {
					freq = 0;
					break;
				}
					
			}
			if(freq != 0) {
				win_number = Integer.toString(freq);
				return true;
			}
		}
		
		//coluna por coluna
		for(int i = 0; i <3; i++) {
			for(int j = 0; j <3; j++) {
				
				if(j == 0 && matriz[j][i] != 0)
					freq = matriz[j][i];
				
				if(j > 0 && (matriz[j][i] == 0 || matriz[j][i] != freq)) {
					freq = 0;
					break;
				}
					
			}
			if(freq != 0) {
				win_number = Integer.toString(freq);
				return true;
			}
		}
		
		if(matriz[1][1] != 0) { //verifica se o meio do tabuleiro é diferente de zero
			freq = matriz[1][1];
			
			
	
			
			if(matriz[0][0] != freq || matriz[2][2] != freq) { //verifica primeira diagonal

			}else {
				win_number = Integer.toString(freq);
				return true;
			}
			
			
			if(matriz[0][2] != freq || matriz[2][0] != freq) { //verifica segunda diagonal
				
			}else {
				win_number = Integer.toString(freq);
				return true;
			}
			
			freq = 0;
		}
		
		return false;
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

package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class Servidor extends Thread{
	private int port = 0;
	private ServerSocket servidor;
	private Socket p1;
	private InetAddress ip_serv;  
	//private SocketAddress serveAddress = new InetSocketAddress("10.20.6.186", 0);
	private ConexaoJogador t1;
	private ConexaoJogador t2;
	
	public Servidor(int port) {
		this.port = port;
	}
	
	public void run(){
		//try catch iniciar servidor socket
		if(this.port == 0) {
			try {
				servidor = new ServerSocket(9000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();}
			
		} else {
			
			try {
				servidor = new ServerSocket(port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//	=======================================
			
		while(true) { //fazer um "semnaforo aqui"
			try {
				//System.out.println(InetAddress.getByName("localhost"));
				p1 = servidor.accept();
				System.out.println("Connectado! Ip: " + p1.getInetAddress());
				System.out.println(p1.getRemoteSocketAddress()+" connected\n");
				if(t1.equals(null)) {
					System.out.println("conecxtado");
					t1 = new ConexaoJogador(p1);
					t1.rodar();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*public void iniciar() throws IOException {
		if(this.port == 0)
			servidor = new ServerSocket(6789);
		else
			servidor = new ServerSocket(port);
		
		while(true) {
			Socket p1 = servidor.accept();
			System.out.println("Connectado! Ip: " + p1.getInetAddress());
			ConexaoJogador t1 = new ConexaoJogador(p1);
			t1.rodar();
		}
	}*/
	
	public boolean setPort(int n) {
		if(servidor.isClosed()) {
			this.port = n;
		}else
			return false;
		
		return true;
	}
	public int getPort() {
		return this.port;
	}
	
	public boolean closeServer() throws IOException {
		servidor.close();
		p1.close();
		t1.beClose();
		
		this.port = 0;
		if(servidor.isClosed() && p1.isClosed())
			return true;
		else
			return false;
	}
	
	public boolean isClose() {
		return servidor.isClosed();
	}
	
}

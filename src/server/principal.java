package server;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.BindException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class principal {
	//janela grafica
	private JFrame Janela;
	
	//espaço para digitar
	private JTextField ip;
	private String ip_ser;
	
	private JTextField port;
	private String port_ser;
	
	//=========================================//
	
	//botao para desligar/ligar
	private JButton on_off;
	//imagem se ta desligado ou ligado
	private JLabel status;
	private JTextField play;
	
	//variavel pra saber quantos clientes há no servidor
	private int n_clie;
	
	//icones para desligar e ligar
	private ImageIcon on = new ImageIcon(getClass().getClassLoader().getResource("on.png"));
	private ImageIcon off = new ImageIcon(getClass().getClassLoader().getResource("off.png"));
	
	//servidor
	private Servidor server;
	private String port_server;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					principal window = new principal();
					window.Janela.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public principal() throws IOException {
		initialize();
	}
	
	private void initialize() throws IOException {
		//cria a janela
		Janela = new JFrame("Servidor JdV");
		Janela.setBounds(320, 80, 280, 250);
		Janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Janela.setIconImage(logo.getImage());
		Janela.getContentPane().setLayout(null);
		
		ip = new JTextField();
		ip.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		ip.setDocument(new JTextFieldLimit(15));
		ip.setText("000.000.000.000");
		ip.setHorizontalAlignment(JTextField.CENTER);
		ip.setEnabled(false);
		ip.setBounds(10, 10, 250, 40);
		Janela.getContentPane().add(ip);
		
		port = new JTextField();
		port.setDocument(new JTextFieldLimit(5));
		port.setText("9000");
		port.setBounds(10, 60, 60, 40);
		port.setHorizontalAlignment(JTextField.CENTER);
		port.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		Janela.getContentPane().add(port);
		
		play = new JTextField("0");
		play.setBounds(120, 60, 40, 40);
		play.setHorizontalAlignment(JTextField.CENTER);
		play.setEnabled(false);
		play.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		Janela.getContentPane().add(play);
		
		status = new JLabel();
		status.setIcon(off);
		status.setBounds(180,160,70,31);
		Janela.getContentPane().add(status);
		
		on_off = new JButton("On/Off");
		on_off.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		on_off.setFocusPainted(false);
		on_off.setBounds(50, 150, 100, 50);
		Janela.getContentPane().add(on_off);
		
		on_off.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				if(status.getIcon().equals(off)) {
					server = new Servidor(Integer.parseInt(port.getText()));
					server.setFieldPlayer(play);
				    System.out.println(server.getPort());
				   try {
					server.start();
					status.setIcon(on);
				   }catch(IllegalThreadStateException e) {
					   JOptionPane.showMessageDialog(Janela, "Servidor já esta rodando");
				   }

				}
				else {
					try {
						if(server.closeServer()) {
							JOptionPane.showMessageDialog(Janela, "Fechado com sucesso");
						}
						else {
							JOptionPane.showMessageDialog(Janela, "Erro ao fechar servidor");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					status.setIcon(off);
				}
				
			}
		});
	}

}

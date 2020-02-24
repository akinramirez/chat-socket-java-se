package chat.socket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;

public class ClienteChat {

  public static void main(String[] args) {
    MarcoClienteChat mimarco = new MarcoClienteChat();
    mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

class LaminaClienteChat extends JPanel implements Runnable{

  private JTextField campo1, nick, ip;
  private JButton miboton;
  private JTextArea areaChat;

  public LaminaClienteChat() {
    nick = new JTextField(5);
    add(nick);
    JLabel cliente = new JLabel("-- CHAT --");
    add(cliente);
    ip = new JTextField(8);
    add(ip);
    areaChat = new JTextArea(12, 20);
    add(areaChat);
    campo1 = new JTextField(20);
    add(campo1);
    miboton = new JButton("Enviar");
    miboton.addActionListener(new ActionListener() {
      // Envio datos de formulario
      @Override
      public void actionPerformed(ActionEvent e) {
        //System.out.println(campo1.getText());
        try {
          // Creacion de Socket (Via de comunicacion)
          Socket miSocket = new Socket("192.168.0.6", 9999);
          EnvioPaqueteDatos datos = new EnvioPaqueteDatos();
          // Seteo de objeto          
          datos.setNick(nick.getText());
          datos.setIp(ip.getText());
          datos.setTextoCliente(campo1.getText());
          // ENvio de objeto
          ObjectOutputStream flujoSalidaPaquete = new ObjectOutputStream(miSocket.getOutputStream());
          flujoSalidaPaquete.writeObject(datos);
          // Cierre de objeto
          miSocket.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });
    add(miboton);
    // Crear instancia de la clase thread (hilo)
    Thread miHilo = new Thread(this);
    miHilo.start();
  }

  @Override
  public void run() {
    try {
      ServerSocket escuchaCliente = new ServerSocket(9090);
      Socket cliente;
      EnvioPaqueteDatos paqueteRecibido;
      
      while(true){
        cliente = escuchaCliente.accept();
        ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());
        paqueteRecibido = (EnvioPaqueteDatos) flujoEntrada.readObject();
        areaChat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getTextoCliente());
      }
      
    } catch (IOException ex1) {
      ex1.printStackTrace();
    } catch (ClassNotFoundException ex2) {
      ex2.printStackTrace();
    }
  }
}

class MarcoClienteChat extends JFrame {

  public MarcoClienteChat() {
    setBounds(600, 180, 280, 350);
    LaminaClienteChat milamina = new LaminaClienteChat();
    add(milamina);
    setVisible(true);
  }
}

class EnvioPaqueteDatos implements Serializable {

  private String nick, ip, textoCliente;

  public EnvioPaqueteDatos() {
  } 

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getTextoCliente() {
    return textoCliente;
  }

  public void setTextoCliente(String textoCliente) {
    this.textoCliente = textoCliente;
  }

}

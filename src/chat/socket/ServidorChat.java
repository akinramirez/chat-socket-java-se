package chat.socket;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;


public class ServidorChat {

  public static void main(String[] args) {
    MarcoServidorChat mimarco = new MarcoServidorChat();
    mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

class MarcoServidorChat extends JFrame implements Runnable {

  private JTextArea areatexto;

  @Override
  public void run() {

    try {
      ServerSocket miServidor = new ServerSocket(9999); // Puerto a la escucha
      String nick, ip, mensaje;
      EnvioPaqueteDatos paqueteRecibido;
      
      while (true) {

        Socket miSocket = miServidor.accept(); // Acepta todas las conexiones que viajan por el socket
        //Flujo de entrada de datos
        ObjectInputStream flujoDatosEntrada = new ObjectInputStream(miSocket.getInputStream());
        paqueteRecibido = (EnvioPaqueteDatos)flujoDatosEntrada.readObject();
        nick = paqueteRecibido.getNick();
        ip = paqueteRecibido.getIp();
        mensaje = paqueteRecibido.getTextoCliente();
        areatexto.append("\n" + "nick" + " : " + nick + "mensaje: " + mensaje + "IP:" + ip);
        miSocket.close();
      }

    } catch (IOException ex1) {
      ex1.printStackTrace();
    } catch (ClassNotFoundException ex2){
      ex2.printStackTrace();
    }
  }

  public MarcoServidorChat() {
    setBounds(800, 180, 280, 300);
    JPanel milamina = new JPanel();
    milamina.setLayout(new BorderLayout());
    areatexto = new JTextArea();
    milamina.add(areatexto, BorderLayout.CENTER);
    add(milamina);
    setVisible(true);

    Thread miHilo = new Thread(this);
    miHilo.start();
  }

}

package chat.socket.servidor;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;

import javax.swing.*;

public class Servidor {

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

      while (true) {

        Socket miSocket = miServidor.accept(); // Acepta todas las conexiones que viajan por el socket
        //Flujo de entrada de datos
        DataInputStream flujoEntrada = new DataInputStream(miSocket.getInputStream());
        String mensajeTexto = flujoEntrada.readUTF();
        areatexto.append("\n" + mensajeTexto);
        miSocket.close();
      }

    } catch (IOException ex) {
      ex.printStackTrace();
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

package chat.socket;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
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

  // Hilo
  @Override
  public void run() {

    try {
      ServerSocket miServidor = new ServerSocket(9999); // Puerto a la escucha
      String nick, ip, mensaje;
      ArrayList<String> listaIpConectados = new ArrayList<String>();
      //Instancia de objeto recibido
      EnvioPaqueteDatos paqueteRecibido;
      

      while (true) {
        // Creacion de Socket (Via de comunicacion)
        Socket miSocket = miServidor.accept(); // Acepta todas las conexiones que viajan por el socket              
        //Flujo de entrada de datos
        ObjectInputStream flujoDatosEntrada = new ObjectInputStream(miSocket.getInputStream());
        // Lectura de objeto recibio
        paqueteRecibido = (EnvioPaqueteDatos) flujoDatosEntrada.readObject();
        // Getter de objeto recibido
        nick = paqueteRecibido.getNick();
        ip = paqueteRecibido.getIp();
        mensaje = paqueteRecibido.getTextoCliente();
        if (!mensaje.equals("ONLINE")) {
          // Muestra en el area de texto de servidor
          areatexto.append("\n" + "nick" + " : " + nick + " mensaje: " + mensaje + " IP:" + ip);
          // Creando socket para reenvio de objeto al cliente
          Socket reenvioDestinatario = new Socket(ip, 9090);
          ObjectOutputStream paqueteReenvio = new ObjectOutputStream(reenvioDestinatario.getOutputStream());
          paqueteReenvio.writeObject(paqueteRecibido);
          reenvioDestinatario.close();
          miSocket.close();
        } else {
          // Direcciones IP de clientes
          InetAddress dirCliente = miSocket.getInetAddress();
          String ipClientesConectados = dirCliente.getHostAddress();
          System.out.println("Direccion Remota Conectada: " + ipClientesConectados);
          listaIpConectados.add(ipClientesConectados);
          paqueteRecibido.setIPs(listaIpConectados);
          for (String IPs:listaIpConectados){
            System.out.println("ArrayList listaIpConectados: " + IPs);
            // Envio de ArrayList listaIpConectados
            Socket reenvioDestinatario = new Socket(IPs, 9090);
            ObjectOutputStream paqueteReenvio = new ObjectOutputStream(reenvioDestinatario.getOutputStream());
            paqueteReenvio.writeObject(paqueteRecibido);
            reenvioDestinatario.close();
            miSocket.close();
          }
        }
      }

    } catch (IOException ex1) {
      ex1.printStackTrace();
    } catch (ClassNotFoundException ex2) {
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

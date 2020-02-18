package SocketPractica;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;

public class Cliente {

  public static void main(String[] args) {
    MarcoClienteChat mimarco = new MarcoClienteChat();
    mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

class LaminaClienteChat extends JPanel {

  private JTextField campo1;
  private JButton miboton;

  public LaminaClienteChat() {
    JLabel cliente = new JLabel("CLIENTE");
    add(cliente);
    campo1 = new JTextField(20);
    add(campo1);
    miboton = new JButton("Enviar");
    miboton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //System.out.println(campo1.getText());
        
        try {
          // Creacion de Socket (Via de comunicacion)
          Socket miSocket = new Socket("192.168.0.6",9999);
          
          //Creacion del flujo de datos
          DataOutputStream flujoSalida = new DataOutputStream(miSocket.getOutputStream());
          
          //Enviar string del textbox al server
          flujoSalida.writeUTF(campo1.getText());
          flujoSalida.close();
          
        } catch (IOException ex) {
          ex.printStackTrace();
        }        
      }
    });
    add(miboton);
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

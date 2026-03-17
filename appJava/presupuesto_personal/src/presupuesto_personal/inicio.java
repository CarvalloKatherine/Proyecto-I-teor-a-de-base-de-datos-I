package presupuesto_personal;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class inicio extends JFrame {
    public JPanel panel; //crear panel como atributo 
    JFrame currentFrame = this; 
    private JPanel panelBotones;
    FondoPanel fondo = new FondoPanel();

    
    
  public inicio(){
  this.setSize(712,506);//establecer tamaño de la ventana
  setTitle("Menu Inicio"); //titulo 
  //setLocation(250,100);//establece la posición inicial 
  //setBounds(250,100,712,506);//une setsize y location, primero location y luego size, ancho/alto
  setLocationRelativeTo(null);      //pone la pantalla centrada
  setDefaultCloseOperation(EXIT_ON_CLOSE);//cerrar el programa con el boton
  this.setContentPane(fondo);
  iniciarComponentes();
  }  
  
  private void iniciarComponentes(){
      colocarEtiquetas();
      colocarBotones();
  }
  
  private void colocarEtiquetas(){
    fondo.setLayout(null); 
    
    JLabel etiqueta = new JLabel("Menú Inicio");
    etiqueta.setBounds(245, 83, 300, 50);
    etiqueta.setForeground(Color.decode("#c7326b"));
    etiqueta.setFont(new Font("Tahoma", Font.BOLD, 36));
    
    fondo.add(etiqueta);
      
  }
  
  private void colocarBotones(){
  panelBotones = new JPanel();
  panelBotones.setLayout(null);
  panelBotones.setOpaque(false);
  fondo.setLayout(null);
  fondo.add(panelBotones);
  
  JButton logIn = new JButton("LOG IN");//O usar set text 
  logIn.setBounds(240, 170, 214, 46);
  logIn.setForeground(Color.WHITE);
  logIn.setFont(new Font("Tahoma",Font.BOLD,24));
  logIn.setBackground(Color.decode("#a1162b"));
  fondo.add(logIn);
  
   logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new logIn().setVisible(true);
                dispose(); // Cierra la ventana actual.
            }
        });
  
  JButton crearUsuario = new JButton("CREAR USUARIO");//O usar set text 
  crearUsuario.setBounds(240, 240, 214, 46);
  crearUsuario.setForeground(Color.WHITE);
  crearUsuario.setFont(new Font("Tahoma",Font.BOLD,20));
  crearUsuario.setBackground(Color.decode("#a1162b"));
  fondo.add(crearUsuario);
  
  crearUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new crearUsuario().setVisible(true); // Abre ventana para crear jugador.
                dispose(); 
            }
        });
  
  JButton salir = new JButton("SALIR");//O usar set text 
  salir.setBounds(240, 310, 214, 46);
  salir.setForeground(Color.WHITE);
  salir.setFont(new Font("Tahoma",Font.BOLD,24));
  salir.setBackground(Color.decode("#a1162b"));
  fondo.add(salir);
  
  ActionListener s = new ActionListener(){
  public void actionPerformed(ActionEvent ae){
        //currentFrame.dispose();
        System.exit(0);
}
  };
  salir.addActionListener(s);
  
  }
 
  
  class FondoPanel extends JPanel{
  
      private Image imagen; 
      
      @Override
      public void paintComponent(Graphics g){
          super.paintComponent(g);
          imagen = new ImageIcon(getClass().getResource("/imagenes/inicio.png")).getImage();
          g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
          
      
      }
      
      
      
  
  }
 
    
}

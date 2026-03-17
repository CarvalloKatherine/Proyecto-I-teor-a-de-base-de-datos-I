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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class logIn extends JFrame {
    public JPanel panel; //crear panel como atributo 
    JFrame currentFrame = this; 
    private JPanel panelBotones;
    JTextField u;
    JPasswordField c;
    
    
    
    
   public logIn(){
  this.setSize(712,506);//establecer tamaño de la ventana
  setTitle("LogIn"); //titulo 
  //setLocation(250,100);//establece la posición inicial 
  //setBounds(250,100,712,506);//une setsize y location, primero location y luego size, ancho/alto
  setLocationRelativeTo(null);      //pone la pantalla centrada
  setDefaultCloseOperation(EXIT_ON_CLOSE);//cerrar el programa con el boton
  //this.setContentPane(fondo);
  iniciarComponentes();
  }  
  
  private void iniciarComponentes(){
      colocarEtiquetas();
      colocarBotones();
      colocarText();
  }
  
  private void colocarEtiquetas(){
      panel = new JPanel();// instanciar panel 
      panel.setLayout(null);
      this.getContentPane().add(panel);//agregar panel a la ventana
      panel.setOpaque(true);
      //panel.setBackground(Color.PINK);//color del panel 
      //panel.getContentPane().setBackground(new Color(0xEBB8DD)); HEX COLRS 
      
      JLabel etiqueta = new JLabel("LogIn");
      panel.add(etiqueta);//agregar etiqueta al panel
      etiqueta.setBounds(280,85,300,50);
      etiqueta.setForeground(Color.decode("#d23b43"));
      etiqueta.setFont(new Font("Tahoma",Font.BOLD,36));
      
  }
  
  private void colocarBotones(){
  panelBotones = new JPanel();
  panelBotones.setLayout(null);
  panelBotones.setOpaque(false);
  panel.setLayout(null);
  panel.add(panelBotones);
  
  JButton logIn = new JButton("LOG IN");//O usar set text 
  logIn.setBounds(180, 350, 150, 40);
  logIn.setForeground(Color.WHITE);
  logIn.setFont(new Font("Tahoma",Font.BOLD,20));
  logIn.setBackground(Color.decode("#a1162b"));
  panel.add(logIn);
  
   logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
                dispose(); // Cierra la ventana actual.
            }
        });
  
  
  JButton salir = new JButton("SALIR");//O usar set text 
  salir.setBounds(350, 350, 150, 40);
  salir.setForeground(Color.WHITE);
  salir.setFont(new Font("Tahoma",Font.BOLD,24));
  salir.setBackground(Color.decode("#a1162b"));
  panel.add(salir);
  
  ActionListener s = new ActionListener(){
  public void actionPerformed(ActionEvent ae){
        currentFrame.dispose();
        new inicio().setVisible(true);
}
  };
  salir.addActionListener(s);
  
  }
 
  private void colocarText(){
  
  JLabel usser = new JLabel("USUARIO:");
      panel.add(usser);
      usser.setBounds(200,140,300,50);
      usser.setForeground(Color.BLACK);
      usser.setFont(new Font("Tahoma",Font.BOLD,18));
      
      
      u = new JTextField();
      panel.add(u);
      u.setBounds(200, 180, 290, 37);
  
      
      JLabel contra = new JLabel("CONTRASEÑA:  (5 caracteres)");
      panel.add(contra);
      contra.setBounds(200,220,300,50);
      contra.setForeground(Color.BLACK);
      contra.setFont(new Font("Tahoma",Font.BOLD,18));
      
      c = new JPasswordField();
      panel.add(c);
      c.setBounds(200, 260, 290, 37);
  
  
  }
 
private void iniciarSesion(){
    
    if(u.getText().isEmpty() || c.getPassword().length == 0){
    JOptionPane.showMessageDialog(this, "No puede dejar vacio la clave y correo, por favor llenarlo.");
    return;
    }
    String correo = u.getText();
    String clave = String.valueOf(c.getPassword());
    
    gestorUsuario gestor = new gestorUsuario();
    Usuario usuario = gestor.validarLogIn(correo, clave);
    
    if (usuario != null) {
        JOptionPane.showMessageDialog(this, "¡Bienvenid@ " + usuario.getPrimerNombre() + "!");
        
        // abrir menu principal
        this.dispose(); 
    } else {
        JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
    }
    
}
    
}

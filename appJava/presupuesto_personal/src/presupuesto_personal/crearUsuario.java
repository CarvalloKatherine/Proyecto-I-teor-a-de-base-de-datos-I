package presupuesto_personal;

import javax.swing.JFrame;
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

public class crearUsuario extends JFrame{
    public JPanel panel; 
    JFrame currentFrame = this; 
    private JPanel panelBotones;
    private JTextField txtP_Nombre, txtS_Nombre, txtP_Apellido, txtS_Apellido, txtCorreo, txtSalario;
    private JPasswordField txtClave;
    
    
    public crearUsuario(){
  this.setSize(712,506);//establecer tamaño de la ventana
  setTitle("Registrar Usuario"); //titulo 
  //setLocation(250,100);//establece la posición inicial 
  //setBounds(250,100,712,506);//une setsize y location, primero location y luego size, ancho/alto
  setLocationRelativeTo(null);      //pone la pantalla centrada
  setDefaultCloseOperation(EXIT_ON_CLOSE);//cerrar el programa con el boton
  iniciarComponentes();
  }  
  
  private void iniciarComponentes(){
      colocarEtiquetas();
      colocarCampos();
      colocarBotones();
  }
  
  private void colocarEtiquetas(){
      panel = new JPanel();// instanciar panel 
      panel.setLayout(null);
      this.getContentPane().add(panel);//agregar panel a la ventana
      panel.setOpaque(false);
      //fondo.add(panel);
      panel.setBackground(Color.PINK);//color del panel 
      
      JLabel etiqueta = new JLabel("Crear cuenta");
      panel.add(etiqueta);//agregar etiqueta al panel
      etiqueta.setBounds(245,20,300,50);//tamaño y posición de la etiqueta
      etiqueta.setForeground(Color.decode("#c7326b"));//ponerle color a las letras 
      etiqueta.setFont(new Font("Tahoma",Font.BOLD,28));
      
      String[] labels = {"1er Nombre:", "2do Nombre:", "1er Apellido:", "2do Apellido:", "Correo:", "Contraseña:", "Salario Base:"};
        int yPos = 80;
        for (String l : labels) {
            JLabel label = new JLabel(l);
            label.setBounds(100, yPos, 150, 30);
            label.setFont(new Font("Tahoma", Font.PLAIN, 14));
            panel.add(label);
            yPos += 45;
        }
  }
  
  private void colocarCampos() {
        
        txtP_Nombre = new JTextField(); txtP_Nombre.setBounds(260, 80, 250, 30);
        txtS_Nombre = new JTextField(); txtS_Nombre.setBounds(260, 125, 250, 30);
        txtP_Apellido = new JTextField(); txtP_Apellido.setBounds(260, 170, 250, 30);
        txtS_Apellido = new JTextField(); txtS_Apellido.setBounds(260, 215, 250, 30);
        txtCorreo = new JTextField(); txtCorreo.setBounds(260, 260, 250, 30);
        txtClave = new JPasswordField(); txtClave.setBounds(260, 305, 250, 30);
        txtSalario = new JTextField(); txtSalario.setBounds(260, 350, 250, 30);

        panel.add(txtP_Nombre);
        panel.add(txtS_Nombre);
        panel.add(txtP_Apellido);
        panel.add(txtS_Apellido);
        panel.add(txtCorreo);
        panel.add(txtClave);
        panel.add(txtSalario);
    }
  
  private void colocarBotones(){
  panelBotones = new JPanel();
  panelBotones.setLayout(null);
  panelBotones.setOpaque(false);
  panel.setLayout(null);
  panel.add(panelBotones);
  
  JButton logIn = new JButton("REGISTRAR");//O usar set text 
  logIn.setBounds(180, 410, 150, 40);
  logIn.setForeground(Color.WHITE);
  logIn.setFont(new Font("Tahoma",Font.BOLD,15));
  logIn.setBackground(Color.decode("#a1162b"));
  panel.add(logIn);
  
   logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrar();
                dispose(); // Cierra la ventana actual.
            }
        });
  

  
  JButton salir = new JButton("SALIR");//O usar set text 
  salir.setBounds(350, 410, 150, 40);
  salir.setForeground(Color.WHITE);
  salir.setFont(new Font("Tahoma",Font.BOLD,24));
  salir.setBackground(Color.decode("#a1162b"));
  panel.add(salir);
  
  ActionListener s = new ActionListener(){
  public void actionPerformed(ActionEvent ae){
        //currentFrame.dispose();
        System.exit(0);
}
  };
  salir.addActionListener(s);
  
  }
 
 private void registrar() {
        
        if (txtP_Nombre.getText().isEmpty() || txtP_Apellido.getText().isEmpty() || 
            txtCorreo.getText().isEmpty() || txtClave.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Por favor llene los campos obligatorios.");
            return;
        }

        try {
            double salario = Double.parseDouble(txtSalario.getText());
            
            Usuario nuevo = new Usuario(
                0, 
                String.valueOf(txtClave.getPassword()), 
                txtP_Nombre.getText(), 
                txtS_Nombre.getText(), 
                txtP_Apellido.getText(), 
                txtS_Apellido.getText(), 
                txtCorreo.getText(), 
                null, 
                salario, 
                true, 
                txtCorreo.getText(), 
                null, null, null
            );

            
            gestorUsuario gestor = new gestorUsuario();
            String respuesta = gestor.registrar(nuevo);
            
            JOptionPane.showMessageDialog(this, respuesta);
            
            if (respuesta.contains("correctamente")) {
                new inicio().setVisible(true);
                dispose();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un número válido.");
        }
    }
 
    
}



package presupuesto_personal;

import javax.swing.JFrame;

import javax.swing.*;
import java.awt.*;

public class MenuAdmin extends JFrame {
    private Usuario usuarioActivo;
    private Color colorFondo = new Color(255, 245, 245); 
    private Color colorBotones = new Color(180, 210, 230); 

    public MenuAdmin(Usuario u) {
        this.usuarioActivo = u;
        
        setTitle("Panel de Administración");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear el panel de pestañas
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Agregar las dos pestañas
        pestañas.addTab("Nueva Categoría", crearPanelCategoria());
        pestañas.addTab("Nueva Subcategoría", crearPanelSubcategoria());

        add(pestañas, BorderLayout.CENTER);
        
        // Label de bienvenida en la parte superior
        JLabel lblBienvenida = new JLabel("Hola Admin", SwingConstants.CENTER);
        //lblBienvenida.setBorder(BorderFactory.createEmptyPadding(10, 10, 10, 10));
        add(lblBienvenida, BorderLayout.NORTH);
    }

    private JPanel crearPanelCategoria() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(colorFondo);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        JLabel lblNombre = new JLabel("Nombre de la Categoría:");
        JTextField txtNombre = new JTextField(15);
        JButton btnGuardar = new JButton("Guardar Categoría");
        btnGuardar.setBackground(colorBotones);

   
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblNombre, gbc);
        gbc.gridy = 1;
        panel.add(txtNombre, gbc);
        gbc.gridy = 2;
        panel.add(btnGuardar, gbc);

        
        return panel;
    }

    private JPanel crearPanelSubcategoria() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(colorFondo);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel lblCat = new JLabel("Seleccionar Categoría Padre:");
        JComboBox<String> comboCategorias = new JComboBox<>(); 
        
        JLabel lblSub = new JLabel("Nombre de la Subcategoría:");
        JTextField txtSub = new JTextField(15);
        
        JButton btnGuardar = new JButton("Guardar Subcategoría");
        btnGuardar.setBackground(colorBotones);

        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblCat, gbc);
        gbc.gridy = 1;
        panel.add(comboCategorias, gbc);
        gbc.gridy = 2;
        panel.add(lblSub, gbc);
        gbc.gridy = 3;
        panel.add(txtSub, gbc);
        gbc.gridy = 4;
        panel.add(btnGuardar, gbc);

        return panel;
    }
}

package presupuesto_personal;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class configuracionInicial extends JFrame {

    private JPanel contentPane;
    private JTextField txtPresupuestoMensual;
    private JTabbedPane tabbedPane;

    public configuracionInicial() {
        setTitle("Configuración de Mi Presupuesto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(712, 506);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // --- ENCABEZADO ---
        JLabel lblTitulo = new JLabel("Configuración Paso a Paso");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitulo.setForeground(Color.decode("#c7326b"));
        lblTitulo.setBounds(30, 20, 400, 40);
        contentPane.add(lblTitulo);

        // --- TABS (PASOS) ---
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(30, 70, 640, 330);
        tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPane.add(tabbedPane);

        // PASO 1: PRESUPUESTO GENERAL
        JPanel pnlPaso1 = new JPanel();
        pnlPaso1.setBackground(Color.decode("#FFF0F5")); // Lavanda/Rosa muy claro
        pnlPaso1.setLayout(null);
        tabbedPane.addTab("1. Mi Salario", null, pnlPaso1, "Define tu base mensual");

        JLabel lblMonto = new JLabel("¿Cuál es tu presupuesto mensual base?");
        lblMonto.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblMonto.setBounds(50, 50, 400, 30);
        pnlPaso1.add(lblMonto);

        txtPresupuestoMensual = new JTextField();
        txtPresupuestoMensual.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtPresupuestoMensual.setBounds(50, 90, 250, 40);
        txtPresupuestoMensual.setBorder(BorderFactory.createLineBorder(Color.decode("#c7326b"), 2));
        pnlPaso1.add(txtPresupuestoMensual);

        JLabel lblInfo = new JLabel("<html><body>Este monto servirá para calcular tus porcentajes de ahorro y gastos fijos automáticamente.</body></html>");
        lblInfo.setFont(new Font("Tahoma", Font.ITALIC, 12));
        lblInfo.setBounds(50, 140, 400, 40);
        pnlPaso1.add(lblInfo);

        // PASO 2: CATEGORÍAS (CATÁLOGO)
        JPanel pnlPaso2 = new JPanel();
        pnlPaso2.setBackground(Color.WHITE);
        pnlPaso2.setLayout(null);
        tabbedPane.addTab("2. Categorías", null, pnlPaso2, "Selecciona tus gastos");

        // Ejemplo de Catálogo con Checkboxes
        JPanel pnlCatalogo = new JPanel();
        pnlCatalogo.setLayout(new GridLayout(0, 2, 10, 10)); // 2 columnas
        pnlCatalogo.setBackground(Color.WHITE);
        pnlCatalogo.setBorder(new TitledBorder(null, "Catálogo Sugerido", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
        
        JScrollPane scrollCategorias = new JScrollPane(pnlCatalogo);
        scrollCategorias.setBounds(20, 20, 590, 250);
        pnlPaso2.add(scrollCategorias);

        // Añadiendo elementos al catálogo (simulación)
        String[] cats = {"Alimentación", "Vivienda", "Transporte", "Suscripciones", "Salud", "Educación", "Ocio", "Ahorro"};
        for (String c : cats) {
            JCheckBox cb = new JCheckBox(c);
            cb.setBackground(Color.WHITE);
            cb.setFont(new Font("Tahoma", Font.PLAIN, 14));
            pnlCatalogo.add(cb);
        }

        // PASO 3: SUBCATEGORÍAS
        JPanel pnlPaso3 = new JPanel();
        pnlPaso3.setBackground(Color.WHITE);
        pnlPaso3.setLayout(null);
        tabbedPane.addTab("3. Detalles", null, pnlPaso3, "Subcategorías específicas");

        JLabel lblSub = new JLabel("Escribe subcategorías separadas por coma (ej: Netflix, Spotify):");
        lblSub.setBounds(30, 30, 500, 30);
        pnlPaso3.add(lblSub);

        JTextArea txtSubCategorias = new JTextArea();
        txtSubCategorias.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txtSubCategorias.setBounds(30, 70, 570, 150);
        pnlPaso3.add(txtSubCategorias);

        // --- BOTONES DE NAVEGACIÓN ---
        JButton btnFinalizar = new JButton("FINALIZAR");
        btnFinalizar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnFinalizar.setBackground(Color.decode("#a1162b"));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setBounds(520, 410, 150, 40);
        contentPane.add(btnFinalizar);

        JButton btnCancelar = new JButton("REINICIAR");
        btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCancelar.setBackground(Color.GRAY);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBounds(360, 410, 150, 40);
        contentPane.add(btnCancelar);

        // --- BARRA DE PROGRESO INFORMATIVA ---
        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(33); // Cambia el valor según el paso
        progressBar.setBounds(440, 35, 230, 15);
        progressBar.setForeground(Color.decode("#c7326b"));
        contentPane.add(progressBar);
    }
}
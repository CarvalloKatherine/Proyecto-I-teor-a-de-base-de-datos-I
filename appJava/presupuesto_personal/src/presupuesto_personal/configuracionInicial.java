package presupuesto_personal;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class configuracionInicial extends JFrame {

    private JPanel contentPane, pnlContenedorTarjetas, pnlListaCategorias;
    private JTextField txtNombrePresupuesto;
    private JComboBox<String> cbMesIni, cbMesFin;
    private JSpinner spAnioIni, spAnioFin;
    private Usuario usuarioActivo;

    private HashMap<Integer, JPanel> tarjetasActivas = new HashMap<>();

    private gestorCategoria gCat = new gestorCategoria();
    private gestorSubCategoria gSub = new gestorSubCategoria();
    private gestorPresupuesto gPres = new gestorPresupuesto();
    private gestorPresupuestoDetalle gDetalle = new gestorPresupuestoDetalle();

    public configuracionInicial(Usuario user) {
        this.usuarioActivo = user;
        setTitle("Configurar presupuesto");
        setSize(950, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // encabezado del presupuesto
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBounds(20, 15, 895, 90);
        pnlHeader.setBackground(Color.decode("#FFF0F5"));
        pnlHeader.setBorder(new LineBorder(Color.decode("#c7326b"), 1, true));
        pnlHeader.setLayout(null);
        contentPane.add(pnlHeader);

        pnlHeader.add(new JLabel("Nombre:")).setBounds(20, 15, 60, 25);
        txtNombrePresupuesto = new JTextField();
        txtNombrePresupuesto.setBounds(80, 15, 220, 25);
        pnlHeader.add(txtNombrePresupuesto);

        pnlHeader.add(new JLabel("Desde:")).setBounds(20, 50, 50, 25);
        cbMesIni = new JComboBox<>(new String[]{"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"});
        cbMesIni.setBounds(75, 50, 100, 25);
        pnlHeader.add(cbMesIni);

        spAnioIni = new JSpinner(new SpinnerNumberModel(2026, 2026, 2030, 1));
        spAnioIni.setBounds(180, 50, 65, 25);
        pnlHeader.add(spAnioIni);

        pnlHeader.add(new JLabel("Hasta:")).setBounds(270, 50, 50, 25);
        cbMesFin = new JComboBox<>(new String[]{"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"});
        cbMesFin.setBounds(320, 50, 100, 25);
        pnlHeader.add(cbMesFin);

        spAnioFin = new JSpinner(new SpinnerNumberModel(2026, 2026, 2030, 1));
        spAnioFin.setBounds(425, 50, 65, 25);
        pnlHeader.add(spAnioFin);

        // ── PANEL CATEGORÍAS 
        pnlListaCategorias = new JPanel();
        pnlListaCategorias.setLayout(new BoxLayout(pnlListaCategorias, BoxLayout.Y_AXIS));
        pnlListaCategorias.setBackground(Color.WHITE);
        cargarCategoriasDesdeDB();

        JScrollPane scrollCatalogo = new JScrollPane(pnlListaCategorias);
        scrollCatalogo.setBounds(20, 125, 200, 380);
        scrollCatalogo.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "1. Elige Categorías"));
        contentPane.add(scrollCatalogo);

        // ── PANEL TARJETAS 
        pnlContenedorTarjetas = new JPanel();
        pnlContenedorTarjetas.setLayout(new BoxLayout(pnlContenedorTarjetas, BoxLayout.Y_AXIS));
        pnlContenedorTarjetas.setBackground(Color.decode("#F8F9FA"));

        JScrollPane scrollDetalle = new JScrollPane(pnlContenedorTarjetas);
        scrollDetalle.setBounds(240, 125, 675, 380);
        scrollDetalle.setBorder(new TitledBorder("2. Asigna montos a subcategorías"));
        contentPane.add(scrollDetalle);

        // ── BOTONES 
        JButton btnRegresar = new JButton("REGRESAR");
        btnRegresar.setBounds(290, 530, 250, 45);
        btnRegresar.setBackground(Color.DARK_GRAY);
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.addActionListener(e -> {
            new inicio().setVisible(true);
            this.dispose();
        });
        contentPane.add(btnRegresar);

        JButton btnGuardar = new JButton("GUARDAR PRESUPUESTO");
        btnGuardar.setBounds(565, 530, 350, 45);
        btnGuardar.setBackground(Color.decode("#a1162b"));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnGuardar.addActionListener(e -> guardarTodo());
        contentPane.add(btnGuardar);
    }

    // ── CARGA DE CATEGORÍAS 
    private void cargarCategoriasDesdeDB() {
        List<Categoria> lista = gCat.listarCategoriasParaCatalogo();
        for (Categoria cat : lista) {
            JCheckBox chk = new JCheckBox(cat.getNombre_categoria());
            chk.setBackground(Color.WHITE);
            if (cat.getColor_hex() != null) {
                chk.setForeground(Color.decode(cat.getColor_hex()));
            }
            chk.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    agregarTarjeta(cat);
                } else {
                    removerTarjeta(cat.getId_categoria());
                }
            });
            pnlListaCategorias.add(chk);
        }
    }

    private void agregarTarjeta(Categoria cat) {
        List<SubCategoria> subs = gSub.listarSubcategoriasPorCategoria(cat.getId_categoria());
        if (subs.isEmpty()) return;

        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setName(cat.getTipo_categoria() != null ? cat.getTipo_categoria() : "");
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new CompoundBorder(
            new LineBorder(Color.decode("#c7326b"), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        tarjeta.setMaximumSize(new Dimension(600, 60 + subs.size() * 30));

        JLabel lblTitulo = new JLabel(cat.getNombre_categoria().toUpperCase());
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTitulo.setForeground(Color.decode("#c7326b"));
        tarjeta.add(lblTitulo, BorderLayout.NORTH);

        JPanel pnlCampos = new JPanel(new GridLayout(0, 2, 10, 8));
        pnlCampos.setBackground(Color.WHITE);
        for (SubCategoria sub : subs) {
            pnlCampos.add(new JLabel(sub.getNombre_subcategoria() + ":"));
            JTextField txtMonto = new JTextField();
            txtMonto.setName(String.valueOf(sub.getId_subcategoria()));
            pnlCampos.add(txtMonto);
        }

        tarjeta.add(pnlCampos, BorderLayout.CENTER);
        tarjetasActivas.put(cat.getId_categoria(), tarjeta);
        pnlContenedorTarjetas.add(tarjeta);
        pnlContenedorTarjetas.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlContenedorTarjetas.revalidate();
        pnlContenedorTarjetas.repaint();
    }

    private void removerTarjeta(int idCategoria) {
        JPanel tarjeta = tarjetasActivas.get(idCategoria);
        if (tarjeta != null) {
            pnlContenedorTarjetas.remove(tarjeta);
            tarjetasActivas.remove(idCategoria);
            pnlContenedorTarjetas.revalidate();
            pnlContenedorTarjetas.repaint();
        }
    }

    
    private List<JTextField> obtenerCampos(JPanel tarjeta) {
        List<JTextField> campos = new ArrayList<>();
        for (Component c : tarjeta.getComponents()) {
            if (c instanceof JPanel) {
                for (Component inner : ((JPanel) c).getComponents()) {
                    if (inner instanceof JTextField) {
                        campos.add((JTextField) inner);
                    }
                }
            }
        }
        return campos;
    }

    private double sumarTarjeta(JPanel tarjeta) {
        double suma = 0;
        for (JTextField txt : obtenerCampos(tarjeta)) {
            String val = txt.getText().trim();
            if (!val.isEmpty()) {
                suma += Double.parseDouble(val);
            }
        }
        return suma;
    }

    // guardar el presupuesto
    private void guardarTodo() {
        try {
            double planIngresos = 0, planGastos = 0, planAhorros = 0;

            for (JPanel tarjeta : tarjetasActivas.values()) {
                String tipo = tarjeta.getName();
                if (tipo == null || tipo.isBlank()) continue;

                double suma = sumarTarjeta(tarjeta);
                if ("Ingreso".equalsIgnoreCase(tipo)) planIngresos += suma;
                else if ("Gasto".equalsIgnoreCase(tipo)) planGastos += suma;
                else if ("Ahorro".equalsIgnoreCase(tipo)) planAhorros += suma;
            }

            Presupuesto p = new Presupuesto();
            p.setId_usuario(usuarioActivo.getIdUsuario());
            p.setNombre_descriptivo(txtNombrePresupuesto.getText());
            p.setMes_inicio(cbMesIni.getSelectedIndex() + 1);
            p.setAnio_inicio((int) spAnioIni.getValue());
            p.setMes_fin(cbMesFin.getSelectedIndex() + 1);
            p.setAnio_fin((int) spAnioFin.getValue());
            p.setTotal_ingresos(planIngresos);
            p.setTotal_gastos(planGastos);
            p.setTotal_ahorro(planAhorros);
            p.setCreado_por(usuarioActivo.getCorreo());

            int idPresupuesto = gPres.registrarPresupuesto(p);

            if (idPresupuesto > 0) {
                for (JPanel tarjeta : tarjetasActivas.values()) {
                    for (JTextField txt : obtenerCampos(tarjeta)) {
                        String val = txt.getText().trim();
                        if (val.isEmpty()) continue;

                        double monto = Double.parseDouble(val);
                        if (monto <= 0) continue;

                        PresupuestoDetalle pd = new PresupuestoDetalle();
                        pd.setId_presupuesto(idPresupuesto);
                        pd.setId_subcategoria(Integer.parseInt(txt.getName()));
                        pd.setMonto_mensual_asignado(monto);
                        pd.setObservaciones("Configuración inicial");
                        pd.setCreado_por(usuarioActivo.getCorreo());
                        gDetalle.registrarPresupuestoDetalle(pd);
                    }
                }
                JOptionPane.showMessageDialog(this, "¡Presupuesto guardado correctamente!");
                new panelTransacciones().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo generar el presupuesto.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ingresa solo números válidos en los montos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
        }
    }
}
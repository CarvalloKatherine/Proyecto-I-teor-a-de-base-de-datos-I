package presupuesto_personal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import presupuesto_personal.Categoria;
import java.util.List;
import java.util.ArrayList;

public class panelTransacciones extends JFrame {

    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JComboBox<SubCategoria> cbSubCat; 
    private JComboBox<Obligación> cbMisObligaciones;
    private gestorSubCategoria gsub = new gestorSubCategoria();
    private gestorObligacion gobli = new gestorObligacion();
    private Usuario usuarioActivo;
    private int idPresupuesto; 
    private gestorPresupuestoDetalle gDetalle = new gestorPresupuestoDetalle(); 
    private gestorTransaccion gtran = new gestorTransaccion();


    public panelTransacciones(Usuario user, int idPresupuesto) {
        this.usuarioActivo = user;
        this.idPresupuesto = idPresupuesto; 
        setTitle("Panel General");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 240, 245)); // Rosa muy claro
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // DISEÑO DEL TABBED PANE 
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        //pestañas
        tabbedPane.addTab("Resumen General", crearPanelResumen());
        tabbedPane.addTab("Registrar Movimiento", crearPanelFormulario());
        tabbedPane.addTab("Mi Cuenta", crearPanelPerfil());
        cargarSubcategorias();
        cargarObligaciones();
    }

    private JPanel crearPanelResumen() {
        JPanel pnl = new JPanel();
        pnl.setBackground(Color.WHITE);
        pnl.setLayout(null);

        JLabel lblInstruccion = new JLabel("Seleccione el reporte que desea exportar a PDF:");
        lblInstruccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInstruccion.setBounds(30, 20, 400, 25);
        pnl.add(lblInstruccion);

        
        int x1 = 50, x2 = 450; 
        int y = 70;
        int ancho = 350, alto = 50;
        int espacioY = 70;

        // BOTONES DE REPORTES 
        //REPORTE 1
        JButton btnR1 = new JButton("1. Resumen Mensual ");
        btnR1.setBounds(x1, y, ancho, alto);
        pnl.add(btnR1);
        btnR1.addActionListener(e -> {
            gestorPresupuesto gPres = new gestorPresupuesto();
            Presupuesto p = gPres.obtenerPresupuesto(idPresupuesto);
            gestorReportes gen = new gestorReportes(idPresupuesto, usuarioActivo.getIdUsuario());
            gen.generarReporte1(p.getAnio_inicio(), p.getMes_inicio());
        });
        

        JButton btnR2 = new JButton("2. Distribución de Gastos por Categoría");
        btnR2.setBounds(x2, y, ancho, alto);
        pnl.add(btnR2);
        btnR2.addActionListener(e -> {
            gestorPresupuesto gPres = new gestorPresupuesto();
            Presupuesto p = gPres.obtenerPresupuesto(idPresupuesto);
            gestorReportes gen = new gestorReportes(idPresupuesto, usuarioActivo.getIdUsuario());
            gen.generarReporte2(p.getAnio_inicio(), p.getMes_inicio());
        });

        y += espacioY;

        JButton btnR3 = new JButton("3. Cumplimiento de Presupuesto");
        btnR3.setBounds(x1, y, ancho, alto);
        pnl.add(btnR3);
        btnR3.addActionListener(e -> {
            gestorPresupuesto gPres = new gestorPresupuesto();
            Presupuesto p = gPres.obtenerPresupuesto(idPresupuesto);
            gestorReportes gen = new gestorReportes(idPresupuesto, usuarioActivo.getIdUsuario());
            gen.generarReporte3(p.getAnio_inicio(), p.getMes_inicio());
        });

        JButton btnR4 = new JButton("4. Tendencia de Gastos en el Tiempo");
        btnR4.setBounds(x2, y, ancho, alto);
        pnl.add(btnR4);
        btnR4.addActionListener(e -> {
            gestorReportes gen = new gestorReportes(idPresupuesto, usuarioActivo.getIdUsuario());
            gen.generarReporte4();
        });

        y += espacioY;

        JButton btnR5 = new JButton("5. Estado de Obligaciones Fijas");
        btnR5.setBounds(x2, y, ancho, alto);
        pnl.add(btnR5);
        btnR5.addActionListener(e -> {
            JTextField txtMes = new JTextField();
            JTextField txtAnio = new JTextField("2026");

            Object[] campos = {
                "Mes (1-12):", txtMes,
                "Año:", txtAnio
            };

            int result = JOptionPane.showConfirmDialog(null, campos,
                    "Reporte 5 - Obligaciones", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int mes = Integer.parseInt(txtMes.getText().trim());
                    int anio = Integer.parseInt(txtAnio.getText().trim());
                    gestorReportes gen = new gestorReportes(idPresupuesto, usuarioActivo.getIdUsuario());
                    gen.generarReporte5(anio, mes);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingresa valores numéricos válidos.");
                }
            }
        });

        // Estilo para todos los botones
        Component[] comps = pnl.getComponents();
        for (Component c : comps) {
            if (c instanceof JButton) {
                c.setBackground(new Color(255, 182, 193)); 
                c.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
                ((JButton) c).setFocusPainted(false);
            }
        }

        return pnl;
    }

    private JPanel crearPanelFormulario() {
    JPanel pnl = new JPanel();
    pnl.setBackground(Color.WHITE);
    pnl.setLayout(null);

    JLabel title = new JLabel("Registrar Nueva Transacción");
    title.setFont(new Font("Segoe UI", Font.BOLD, 18));
    title.setBounds(30, 20, 300, 30);
    pnl.add(title);
    
    JLabel lblVincular = new JLabel("¿Pagar obligación existente?:");
    lblVincular.setFont(new Font("Segoe UI", Font.ITALIC, 12));
    lblVincular.setBounds(450, 25, 180, 25);
    pnl.add(lblVincular);

    cbMisObligaciones = new JComboBox<>(); 
    cbMisObligaciones.setBounds(630, 25, 220, 25);
    pnl.add(cbMisObligaciones);
    
    // transacción
    int y = 70;
    int xLabel = 30;
    int xField = 180;

    
    JLabel lblAnio = new JLabel("Año Imputación:");
    lblAnio.setBounds(xLabel, y, 120, 25);
    pnl.add(lblAnio);
    JSpinner spnAnio = new JSpinner(new SpinnerNumberModel(2026, 2020, 2035, 1));
    spnAnio.setBounds(150, y, 70, 25);
    pnl.add(spnAnio);

    JLabel lblMes = new JLabel("Mes:");
    lblMes.setBounds(240, y, 40, 25);
    pnl.add(lblMes);
    String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    JComboBox<String> cbMes = new JComboBox<>(meses);
    cbMes.setBounds(285, y, 110, 25);
    pnl.add(cbMes);

    
    y += 40;
    JLabel lblSub = new JLabel("Subcategoría:");
    lblSub.setBounds(xLabel, y, 150, 25);
    pnl.add(lblSub);
    cbSubCat = new JComboBox<>(); 
    cbSubCat.setBounds(xField, y, 215, 25);
    pnl.add(cbSubCat);
    
    y += 40;
    JLabel lblDesc = new JLabel("Descripción:");
    lblDesc.setBounds(xLabel, y, 150, 25);
    pnl.add(lblDesc);
    JTextField txtDesc = new JTextField();
    txtDesc.setBounds(xField, y, 215, 25);
    pnl.add(txtDesc);

    
    y += 40;
    JLabel lblMonto = new JLabel("Monto (L.):");
    lblMonto.setBounds(xLabel, y, 150, 25);
    pnl.add(lblMonto);
    JTextField txtMonto = new JTextField();
    txtMonto.setBounds(xField, y, 215, 25);
    pnl.add(txtMonto);

    
    y += 40;
    JLabel lblFecha = new JLabel("Fecha (yyyy-mm-dd):");
    lblFecha.setBounds(xLabel, y, 150, 25);
    pnl.add(lblFecha);
    JTextField txtFecha = new JTextField();
    txtFecha.setBounds(xField, y, 215, 25);
    pnl.add(txtFecha);

    
    y += 40;
    JLabel lblMetodo = new JLabel("Método de Pago:");
    lblMetodo.setBounds(xLabel, y, 150, 25);
    pnl.add(lblMetodo);
    JComboBox<String> cbMetodo = new JComboBox<>(new String[]{"Efectivo", "Tarjeta de Débito", "Tarjeta de Crédito", "Transferencia"});
    cbMetodo.setBounds(xField, y, 215, 25);
    pnl.add(cbMetodo);

    
    y += 40;
    JLabel lblFactura = new JLabel("Num. Factura (Opc):");
    lblFactura.setBounds(xLabel, y, 150, 25);
    pnl.add(lblFactura);
    JTextField txtFactura = new JTextField();
    txtFactura.setBounds(xField, y, 215, 25);
    pnl.add(txtFactura);

    y += 40;
    JLabel lblObs = new JLabel("Observaciones:");
    lblObs.setBounds(xLabel, y, 150, 25);
    pnl.add(lblObs);
    JTextField txtObs = new JTextField();
    txtObs.setBounds(xField, y, 215, 25);
    pnl.add(txtObs);

    //obligación 
    y += 45;
    JCheckBox chkEsObligacion = new JCheckBox("¿Convertir en Obligación Fija?");
    chkEsObligacion.setBackground(Color.WHITE);
    chkEsObligacion.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
    chkEsObligacion.setBounds(xLabel, y, 250, 25);
    pnl.add(chkEsObligacion);

    
    JPanel pnlOb = new JPanel();
    pnlOb.setLayout(null);
    pnlOb.setBackground(new Color(255, 245, 247)); // Rosa Kattificación suave
    pnlOb.setBorder(BorderFactory.createTitledBorder("Configuración de Obligación Mensual"));
    pnlOb.setBounds(450, 70, 400, 310); 
    pnlOb.setVisible(false);
    pnl.add(pnlOb);

    int oy = 35;
    JLabel l1 = new JLabel("Nombre Oblig.:"); l1.setBounds(20, oy, 120, 25); pnlOb.add(l1);
    JTextField tNomOb = new JTextField(); tNomOb.setBounds(150, oy, 220, 25); pnlOb.add(tNomOb);

    oy += 45;
    JLabel l2 = new JLabel("Descripción:"); l2.setBounds(20, oy, 120, 25); pnlOb.add(l2);
    JTextField tDescOb = new JTextField(); tDescOb.setBounds(150, oy, 220, 25); pnlOb.add(tDescOb);

    oy += 45;
    JLabel l3 = new JLabel("Monto Fijo:"); l3.setBounds(20, oy, 120, 25); pnlOb.add(l3);
    JTextField tMontoOb = new JTextField(); tMontoOb.setBounds(150, oy, 220, 25); pnlOb.add(tMontoOb);

    oy += 45;
    JLabel l4 = new JLabel("Día Pago (1-31):"); l4.setBounds(20, oy, 120, 25); pnlOb.add(l4);
    JSpinner spnDia = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
    spnDia.setBounds(150, oy, 60, 25);
    pnlOb.add(spnDia);

    oy += 45;
    JLabel l5 = new JLabel("Fecha Inicio:"); l5.setBounds(20, oy, 120, 25); pnlOb.add(l5);
    JTextField tIniOb = new JTextField("2026-03-20"); tIniOb.setBounds(150, oy, 150, 25); pnlOb.add(tIniOb);

    oy += 45;
    JLabel l6 = new JLabel("Fecha Fin:"); l6.setBounds(20, oy, 120, 25); pnlOb.add(l6);
    JTextField tFinOb = new JTextField("yyyy-mm-dd"); tFinOb.setBounds(150, oy, 150, 25); pnlOb.add(tFinOb);

    // botones
    JButton btnGuardar = new JButton("Finalizar Registro");
    btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btnGuardar.setBackground(new Color(255, 182, 193));
    btnGuardar.setBounds(xField, y + 60, 215, 45);
    pnl.add(btnGuardar);
    btnGuardar.addActionListener(e -> {
        try {
            
            SubCategoria subSeleccionada = (SubCategoria) cbSubCat.getSelectedItem();
            int idSub = subSeleccionada.getId_subcategoria();
            String nombreSub = subSeleccionada.getNombre_subcategoria();
            int idDetalle = gDetalle.obtenerIdDetalle(idPresupuesto, idSub);
            
            if (idDetalle <= 0) {
            JOptionPane.showMessageDialog(this, "Esta subcategoría no está en el presupuesto.");
            return;
        }
            
           
            Integer idObliFinal = null;
            if(chkEsObligacion.isSelected()){
       
            double montoFijo = Double.parseDouble(tMontoOb.getText());
            int dia = (int) spnDia.getValue();
            
            java.sql.Date fechai = java.sql.Date.valueOf(tIniOb.getText());
            java.sql.Date fechaf = null; 
            String finTexto = tFinOb.getText().trim();
            if (!finTexto.isEmpty() && !finTexto.equals("yyyy-mm-dd")) {
            fechaf = java.sql.Date.valueOf(finTexto);
            }
                
            Obligación o = new Obligación(
            idSub,tNomOb.getText(), tDescOb.getText(), montoFijo, dia, fechai, fechaf, usuarioActivo.getCorreo(), nombreSub );
            idObliFinal = gobli.registrarObligacion(o);
             System.out.println("ID obligacion generado: " + idObliFinal);
             
             if (idObliFinal <= 0) {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la obligación.");
            return; 
                 }
             
            }else if (cbMisObligaciones.getSelectedItem() != null) {
                Obligación seleccionada = (Obligación) cbMisObligaciones.getSelectedItem();
                idObliFinal = seleccionada.getId_obligacion();
            }
            java.sql.Date fechaTran = java.sql.Date.valueOf(txtFecha.getText());
            if (!gtran.validarVigencia(fechaTran, idPresupuesto)) {
            JOptionPane.showMessageDialog(this, "La fecha no está dentro del rango del presupuesto.");
            return;
            }

            
            Transaccion t = new Transaccion();
            t.setId_presupuesto_detalle(idDetalle);
            t.setAnio((int) spnAnio.getValue());
            t.setMes(cbMes.getSelectedIndex() + 1);
            t.setDescripcion(txtDesc.getText());
            t.setMonto(Double.parseDouble(txtMonto.getText()));
            t.setFecha(fechaTran);
            t.setMetodo_pago((String) cbMetodo.getSelectedItem());
            t.setObservaciones(txtObs.getText());
            t.setCreado_por(usuarioActivo.getCorreo());
            t.setTipo_transaccion(subSeleccionada.getTipo_categoria());
            t.setId_obligacion(idObliFinal);
            String facturaStr = txtFactura.getText();
            t.setNum_factura(facturaStr.isEmpty() ? null : Integer.parseInt(facturaStr));
            
            
            System.out.println("idObliFinal vale: " + idObliFinal);
            System.out.println("id_obligacion en t: " + t.getId_obligacion());
            if (gtran.registrar(t)) {
                JOptionPane.showMessageDialog(this, "¡Transacción guardada con éxito!");

            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la transacción.");
            }
            
            

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Por favor, ingresa un monto válido.");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
    }
    });
    //acciones
    chkEsObligacion.addActionListener(e -> {
        pnlOb.setVisible(chkEsObligacion.isSelected());
        // Al marcarlo, sugerimos copiar el monto y nombre de arriba para ahorrar tiempo
        if(chkEsObligacion.isSelected()){
            tNomOb.setText(txtDesc.getText());
            tMontoOb.setText(txtMonto.getText());
            tIniOb.setText(txtFecha.getText());
        }
        pnl.repaint();
    });

    return pnl;
}

    private JPanel crearPanelPerfil() {
    JPanel pnl = new JPanel();
    pnl.setBackground(Color.WHITE);
    pnl.setLayout(null);

   
    JLabel lblIcono = new JLabel("PERFIL", SwingConstants.CENTER);
    lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 30));
    lblIcono.setBounds(350, 40, 150, 150);
    pnl.add(lblIcono);

    
    JLabel lblNombre = new JLabel("Usuario: " + usuarioActivo.getPrimerNombre() + " " + usuarioActivo.getPrimerApellido(), SwingConstants.CENTER);
    lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblNombre.setBounds(250, 200, 350, 30);
    pnl.add(lblNombre);

    
    JLabel lblCorreo = new JLabel(usuarioActivo.getCorreo(), SwingConstants.CENTER);
    lblCorreo.setFont(new Font("Segoe UI", Font.ITALIC, 14));
    lblCorreo.setForeground(Color.GRAY);
    lblCorreo.setBounds(250, 230, 350, 30);
    pnl.add(lblCorreo);

    JButton btnEdit = new JButton("Modificar Datos");
    btnEdit.setBounds(350, 280, 150, 35);
    btnEdit.setBackground(new Color(180, 210, 230));
    pnl.add(btnEdit);

    
    btnEdit.addActionListener(e -> {
        abrirDialogoEdicionPerfil(lblNombre);
    });

    return pnl;
}
    
    private void abrirDialogoEdicionPerfil(JLabel lblNombreUI) {
    JTextField txtNombre1 = new JTextField(usuarioActivo.getPrimerNombre());
    JTextField txtNombre2 = new JTextField(usuarioActivo.getSegundoNombre());
    JTextField txtApellido1 = new JTextField(usuarioActivo.getPrimerApellido());
    JTextField txtApellido2 = new JTextField(usuarioActivo.getSegundoApellido());
    JTextField txtSalario = new JTextField(String.valueOf(usuarioActivo.getSalario()));

    Object[] formulario = {
        "Primer Nombre:", txtNombre1,
        "Segundo Nombre:", txtNombre2,
        "Primer Apellido:", txtApellido1,
        "Segundo Apellido:", txtApellido2,
        "Salario Mensual:", txtSalario
    };

    int opcion = JOptionPane.showConfirmDialog(this, formulario, "Actualizar Perfil", JOptionPane.OK_CANCEL_OPTION);

    if (opcion == JOptionPane.OK_OPTION) {
        try {
            usuarioActivo.setPrimerNombre(txtNombre1.getText());
            usuarioActivo.setSegundoNombre(txtNombre2.getText());
            usuarioActivo.setPrimerApellido(txtApellido1.getText());
            usuarioActivo.setSegundoApellido(txtApellido2.getText());
            usuarioActivo.setSalario(Double.parseDouble(txtSalario.getText()));
            usuarioActivo.setModificadoPor(usuarioActivo.getPrimerNombre());

            gestorUsuario gU = new gestorUsuario();
            if (gU.updateUsuario(usuarioActivo)) {
                JOptionPane.showMessageDialog(this, "Perfil actualizado con éxito.");
                lblNombreUI.setText("Usuario: " + usuarioActivo.getPrimerNombre() + " " + usuarioActivo.getPrimerApellido());
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar en la base de datos.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un número válido.");
        }
    }
}
    
    private void cargarSubcategorias() {
        cbSubCat.removeAllItems();
        List<SubCategoria> lista = gsub.listarSubcategorias(idPresupuesto);
        for (SubCategoria sub : lista) {
        cbSubCat.addItem(sub);
        }
    }
    
    private void cargarObligaciones(){
        cbMisObligaciones.removeAllItems();
        List<Obligación> lista = gobli.listarObligaciones(usuarioActivo.getCorreo(), true);
        for (Obligación o : lista) {
        cbMisObligaciones.addItem(o);
    }
     }
    
    
    

}




    














    
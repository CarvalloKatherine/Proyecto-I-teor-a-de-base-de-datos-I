package presupuesto_personal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MenuAdmin extends JFrame {
    private Usuario usuarioActivo;
    private gestorUsuario gUsuario = new gestorUsuario(); 
    private gestorCategoria gCategoria = new gestorCategoria(); 
    private gestorSubCategoria gSubCat = new gestorSubCategoria();
    private gestorPresupuesto gPresupuesto = new gestorPresupuesto();
    private gestorObligacion gObli = new gestorObligacion();
    private gestorTransaccion gTran = new gestorTransaccion();
    private gestorPresupuestoDetalle gDetalle = new gestorPresupuestoDetalle();
    
    private Color colorFondo = new Color(255, 245, 245); 
    private Color colorBotones = new Color(180, 210, 230); 

    public MenuAdmin(Usuario u) {
        this.usuarioActivo = u;
        
        setTitle("Panel Administrativo");
        setSize(1100, 750); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane pestañas = new JTabbedPane(JTabbedPane.LEFT);
        pestañas.setFont(new Font("SansSerif", Font.BOLD, 12));

        // paneles
        pestañas.addTab("Usuarios", crearPanelGenerico("Usuarios", new String[]{"ID", "Nombre Completo", "Correo", "Salario"}));
        pestañas.addTab("Categorías", crearPanelGenerico("Categorías", new String[]{"ID", "Nombre", "Tipo", "Color"}));
        pestañas.addTab("Subcategorías", crearPanelGenerico("Subcategorías", new String[]{"ID", "Nombre", "ID Padre"}));
        pestañas.addTab("Presupuestos", crearPanelGenerico("Presupuestos", new String[]{"ID", "Usuario", "Año", "Mes", "Estado"}));
        pestañas.addTab("Detalles", crearPanelGenerico("Detalles", new String[]{"ID", "ID Presupuesto", "Subcategoría", "Monto"}));
        pestañas.addTab("Obligaciones", crearPanelGenerico("Obligaciones", new String[]{"ID", "Nombre", "Monto", "Estado"}));
        pestañas.addTab("Transacciones", crearPanelGenerico("Transacciones", new String[]{"ID", "Fecha", "Monto", "Descripción"}));

        add(pestañas, BorderLayout.CENTER);
        
        JLabel lblBienvenida = new JLabel("Administración del Sistema - " + u.getPrimerNombre(), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblBienvenida.setOpaque(true);
        lblBienvenida.setBackground(colorBotones);
        lblBienvenida.setPreferredSize(new Dimension(0, 50));
        add(lblBienvenida, BorderLayout.NORTH);
    }

    private JPanel crearPanelGenerico(String titulo, String[] columnas) {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(colorFondo);

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Listado de " + titulo));
        panelPrincipal.add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelBotones.setBackground(colorFondo);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");

        //restricciones en botones
        boolean esSoloConsulta = titulo.equals("Detalles") || titulo.equals("Obligaciones") || titulo.equals("Transacciones");
        if (esSoloConsulta) {
            btnNuevo.setVisible(false);
            btnEditar.setVisible(false);
        }

        JButton[] botones = {btnNuevo, btnEditar, btnEliminar, btnRefrescar};
        for (JButton b : botones) {
            b.setBackground(colorBotones);
            b.setPreferredSize(new Dimension(130, 35));
            panelBotones.add(b);
        }

        btnRefrescar.addActionListener(e -> ejecutarAccionDB("LISTAR", titulo, modelo, null));

        btnNuevo.addActionListener(e -> {
            if (titulo.equals("Categorías")) abrirFormularioCategoria(modelo);
            else if (titulo.equals("Subcategorías")) abrirFormularioSubcategoria(modelo);
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                if (confirmarAccion("¿Eliminar registro ID: " + id + " de " + titulo + "?")) {
                    ejecutarAccionDB("ELIMINAR", titulo, modelo, id);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una fila primero.");
            }
        });

        //mouse
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    if (titulo.equals("Detalles") || titulo.equals("Transacciones")) {
                        return; 
                    }
                    
                    int id = Integer.parseInt(modelo.getValueAt(tabla.getSelectedRow(), 0).toString());
                    ejecutarAccionDB("CONSULTAR", titulo, modelo, id);
                }
            }
        });

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        SwingUtilities.invokeLater(() -> btnRefrescar.doClick());

        return panelPrincipal;
    }

    private void ejecutarAccionDB(String accion, String entidad, DefaultTableModel modelo, Integer id) {
        switch (entidad) {
            case "Usuarios": manejarUsuarios(accion, modelo, id); break;
            case "Categorías": manejarCategorias(accion, modelo, id); break;
            case "Subcategorías": manejarSubcategorias(accion, modelo, id); break;
            case "Presupuestos": manejarPresupuestos(accion, modelo, id); break;
            case "Detalles": manejarDetalles(accion, modelo, id); break;
            case "Obligaciones": manejarObligaciones(accion, modelo, id); break;
            case "Transacciones": manejarTransacciones(accion, modelo, id); break;
        }
    }

    // LOGICA 

    private void manejarUsuarios(String accion, DefaultTableModel modelo, Integer id) {
        if (accion.equals("LISTAR")) {
            modelo.setRowCount(0);
            List<Usuario> lista = gUsuario.listarUsuarios();
            for (Usuario user : lista) {
                modelo.addRow(new Object[]{user.getIdUsuario(), user.getPrimerNombre() + " " + user.getPrimerApellido(), user.getCorreo(),user.getSalario()});
            }
        } else if (accion.equals("ELIMINAR")) {
            if (gUsuario.eliminarUsuario(id, usuarioActivo.getPrimerNombre())) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                manejarUsuarios("LISTAR", modelo, null);
            }
        } else if (accion.equals("CONSULTAR")) {
            Usuario u = gUsuario.consultarUsuario(id);
            if (u != null) JOptionPane.showMessageDialog(this, "Usuario: " + u.getPrimerNombre() + " " + u.getPrimerApellido());
        }
    }

    private void manejarCategorias(String accion, DefaultTableModel modelo, Integer id) {
        if (accion.equals("LISTAR")) {
            modelo.setRowCount(0);
            List<Categoria> lista = gCategoria.listarCategoriasParaCatalogo();
            for (Categoria cat : lista) {
                modelo.addRow(new Object[]{cat.getId_categoria(), cat.getNombre_categoria(), cat.getTipo_categoria(), cat.getColor_hex()});
            }
        } else if (accion.equals("ELIMINAR")) {
            if (gCategoria.eliminarCategoria(id)) {
                JOptionPane.showMessageDialog(this, "Categoría eliminada con éxito");
                manejarCategorias("LISTAR", modelo, null);
            }
        }
    }

    private void manejarSubcategorias(String accion, DefaultTableModel modelo, Integer id) {
        if (accion.equals("LISTAR")) {
            modelo.setRowCount(0);
            List<SubCategoria> lista = gSubCat.listarSubcategoriasCatalogo();
            for (SubCategoria sc : lista) {
                modelo.addRow(new Object[]{sc.getId_subcategoria(), sc.getNombre_subcategoria(), sc.getId_categoria()});
            }
        } else if (accion.equals("ELIMINAR")) {
            if (gSubCat.eliminarSubcategoria(id)) {
                JOptionPane.showMessageDialog(this, "Subcategoría eliminada");
                manejarSubcategorias("LISTAR", modelo, null);
            }
        }
    }

    private void manejarDetalles(String accion, DefaultTableModel modelo, Integer id) {
        if (accion.equals("LISTAR")) {
            modelo.setRowCount(0);
            List<PresupuestoDetalle> lista = gDetalle.listarDetallesCatalogo();
            for (PresupuestoDetalle pd : lista) {
                modelo.addRow(new Object[]{pd.getId_presupuesto_detalle(), pd.getId_presupuesto(), pd.getId_subcategoria(), pd.getMonto_mensual_asignado()});
            }
        } else if (accion.equals("ELIMINAR")) {
            if (gDetalle.eliminarDetalle(id)) {
                JOptionPane.showMessageDialog(this, "Detalle eliminado");
                manejarDetalles("LISTAR", modelo, null);
            }
        }
    }

    private void manejarObligaciones(String accion, DefaultTableModel modelo, Integer id) {
        if (accion.equals("LISTAR")) {
            modelo.setRowCount(0);
            List<Obligación> lista = gObli.listarCatalogoObligaciones();
            for (Obligación o : lista) {
                modelo.addRow(new Object[]{o.getId_obligacion(), o.getNombre_obligacion(),o.getMonto_fijo_mensual(), o.isVigente() ? "Activa" : "Inactiva"});
            }
        } else if (accion.equals("ELIMINAR")) {
            if (gObli.eliminarObligacion(id, usuarioActivo.getPrimerNombre())) {
                JOptionPane.showMessageDialog(this, "Obligación eliminada.");
                manejarObligaciones("LISTAR", modelo, null);
            }
        } else if (accion.equals("CONSULTAR")) {
            Obligación o = gObli.consultarObligacion(id);
            if (o != null) JOptionPane.showMessageDialog(this, "Obligación: " + o.getNombre_obligacion() + "\nMonto: " + o.getMonto_fijo_mensual());
        }
    }

    private void manejarTransacciones(String accion, DefaultTableModel modelo, Integer id) {
        if (accion.equals("LISTAR")) {
            modelo.setRowCount(0);
            List<Transaccion> lista = gTran.listarTransaccionesCatalogo();
            for (Transaccion t : lista) {
                modelo.addRow(new Object[]{t.getId_transaccion(), t.getFecha(), t.getMonto(), t.getDescripcion()});
            }
        } else if (accion.equals("ELIMINAR")) {
            if (gTran.eliminarTransaccion(id)) {
                JOptionPane.showMessageDialog(this, "Transacción eliminada con éxito");
                manejarTransacciones("LISTAR", modelo, null);
            }
        }
    }

    private void manejarPresupuestos(String accion, DefaultTableModel modelo, Integer id) {
        if (accion.equals("LISTAR")) {
            modelo.setRowCount(0);
            List<Presupuesto> lista = gPresupuesto.listarPresupuestosCatalogo();
            for (Presupuesto p : lista) {
                modelo.addRow(new Object[]{p.getId_presupuesto(), p.getId_usuario(), p.getAnio_inicio(), p.getMes_inicio(), p.getEstado_presupuesto()});
            }
        } else if (accion.equals("ELIMINAR")) {
            if (gPresupuesto.eliminarPresupuesto(id, usuarioActivo.getPrimerNombre())) {
                JOptionPane.showMessageDialog(this, "Presupuesto eliminado");
                manejarPresupuestos("LISTAR", modelo, null);
            }
        }
    }

    // --- FORMULARIOS ---

    private void abrirFormularioCategoria(DefaultTableModel modelo) {
        JTextField txtNombre = new JTextField();
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"ingreso", "gasto", "ahorro"});
        JTextField txtColor = new JTextField("#");
        Object[] campos = {"Nombre:", txtNombre, "Tipo:", cbTipo, "Color Hex:", txtColor};
        int s = JOptionPane.showConfirmDialog(this, campos, "Nueva Categoría", JOptionPane.OK_CANCEL_OPTION);
        if (s == JOptionPane.OK_OPTION) {
            Categoria n = new Categoria();
            n.setNombre_categoria(txtNombre.getText());
            n.setTipo_categoria(cbTipo.getSelectedItem().toString());
            n.setColor_hex(txtColor.getText());
            n.setCreado_por(usuarioActivo.getPrimerNombre());
            if (gCategoria.registrarCategoria(n)) manejarCategorias("LISTAR", modelo, null);
        }
    }

    private void abrirFormularioSubcategoria(DefaultTableModel modelo) {
        JTextField txtIdCat = new JTextField();
        JTextField txtNombre = new JTextField();
        Object[] campos = {"ID Categoría Padre:", txtIdCat, "Nombre Subcategoría:", txtNombre};
        int s = JOptionPane.showConfirmDialog(this, campos, "Nueva Subcategoría", JOptionPane.OK_CANCEL_OPTION);
        if (s == JOptionPane.OK_OPTION) {
            SubCategoria sc = new SubCategoria();
            sc.setId_categoria(Integer.parseInt(txtIdCat.getText()));
            sc.setNombre_subcategoria(txtNombre.getText());
            sc.setCreado_por(usuarioActivo.getPrimerNombre());
            if (gSubCat.registrarCategoria(sc)) manejarSubcategorias("LISTAR", modelo, null);
        }
    }

    private boolean confirmarAccion(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
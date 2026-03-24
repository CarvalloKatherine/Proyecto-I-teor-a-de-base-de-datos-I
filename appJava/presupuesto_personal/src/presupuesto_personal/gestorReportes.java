package presupuesto_personal;

import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import com.itextpdf.text.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;



public class gestorReportes {
     private int idPresupuesto;
    private int idUsuario;

    public gestorReportes(int idPresupuesto, int idUsuario) {
        this.idPresupuesto = idPresupuesto;
        this.idUsuario = idUsuario;
    }

    public void generarReporte1(int anio, int mes) {
        double ingresos = 0, gastos = 0, ahorros = 0, balance = 0;

        String sql = "{ call dba.sp_calcular_balance_mensual(?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection con = Conexion.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idUsuario);
            cs.setInt(2, idPresupuesto);
            cs.setInt(3, anio);
            cs.setInt(4, mes);
            cs.registerOutParameter(5, java.sql.Types.DECIMAL);
            cs.registerOutParameter(6, java.sql.Types.DECIMAL);
            cs.registerOutParameter(7, java.sql.Types.DECIMAL);
            cs.registerOutParameter(8, java.sql.Types.DECIMAL);
    
    //cambio el execute por se lee el select dl metodo
            //cs.execute();
            //ingresos = cs.getDouble(5);
            //gastos   = cs.getDouble(6);
            //ahorros  = cs.getDouble(7); la base de datos no devuelve todos estos 
            //balance  = cs.getDouble(8);

        try (java.sql.ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                ingresos = rs.getDouble("ingresos");
                gastos   = rs.getDouble("gastos");
                ahorros  = rs.getDouble("ahorros");
                balance  = rs.getDouble("balance");
            }
            }

            
            //se ponen los datos 
            DefaultCategoryDataset datos = new DefaultCategoryDataset(); 
            //agrega al grafico en este orden valor, nombre barra, etiqueta eje x 
            datos.setValue(ingresos, "ingresos", mes+"/"+anio);
            datos.setValue(gastos, "gastos", mes+"/"+anio);
            datos.setValue(ahorros, "ahorros", mes+"/"+anio);
            datos.setValue(balance, "balance", mes+"/"+anio);
            
            //se crea el grafico
            JFreeChart grafico_barra = ChartFactory.createBarChart(
                    "Balance mensual", //nombre del grafico   
                    "periodo", //nombre de las barras o columnas
                    "monto", //nombre de numeración 
                    datos, //datos del grafico
                    PlotOrientation.VERTICAL, //orientación
                    true, //legenda de barras individuales por color (que se muestr lo de abajo )
                    true, //herramientas(se ve tooltips al pasar mouse)
                    false //url del grafico (generar url para web)
            );
            
            try {//crea el documento y el tamaño de la pagina
                Document documento = new Document(PageSize.A4.rotate());
                //conecta el documento con el archivo en la ruta especificada- fileoutput es que el crea el pdf
                PdfWriter.getInstance(documento, new FileOutputStream("C:/Users/Lenovo/Desktop/TeoriaBDatosI/metabase/reporte1.pdf"));
                documento.open(); //al abrirlo se permite poner la info adentro
                
                //cea la el grafico a imagen 
                BufferedImage imagen = grafico_barra.createBufferedImage(700, 400);
                Image imgPdf = Image.getInstance(imagen, null);
                imgPdf.setAlignment(Image.ALIGN_CENTER);
                documento.add(imgPdf);
                
                documento.close();//al finalizar
                JOptionPane.showMessageDialog(null, "PDF guardado correctamente.");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void generarReporte3(int anio, int mes) {
    String sql = "{ call dba.sp_reporte3(?, ?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idPresupuesto);
        cs.setInt(2, anio);
        cs.setInt(3, mes);

        //guardo los datos en una lista
        List<String> subcats       = new ArrayList<>();
        List<Double> presupuestados = new ArrayList<>();
        List<Double> ejecutados     = new ArrayList<>();
        List<Double> porcentajes    = new ArrayList<>();
        List<String> categorias     = new ArrayList<>();
        List<Double> diferencias    = new ArrayList<>();

        try (java.sql.ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                categorias.add(rs.getString("nombre_categoria"));
                subcats.add(rs.getString("nombre_subcategoria"));
                presupuestados.add(rs.getDouble("presupuestado"));
                ejecutados.add(rs.getDouble("ejecutado"));
                porcentajes.add(rs.getDouble("porcentaje"));
            }
        }

        //se agregan los datos 
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < subcats.size(); i++) {
            dataset.setValue(presupuestados.get(i), "Presupuestado", subcats.get(i));
            dataset.setValue(ejecutados.get(i),     "Ejecutado",     subcats.get(i));
        }
        //se crea el grafico 
        JFreeChart grafico = ChartFactory.createBarChart(
            "Cumplimiento de Presupuesto " + mes + "/" + anio,
            "Subcategoría", "Monto ",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );

        // poner colores
        org.jfree.chart.plot.CategoryPlot plot = grafico.getCategoryPlot();
        org.jfree.chart.renderer.category.BarRenderer renderer =
            new org.jfree.chart.renderer.category.BarRenderer() {
                @Override
                public java.awt.Paint getItemPaint(int row, int col) {
                    if (row == 0) return new java.awt.Color(199, 50, 107); // presupuestado: rosa
                    // el ejecutado es color segun el porcentaje 
                    double porcentaje = porcentajes.get(col);
                    if (porcentaje < 80)        return new java.awt.Color(144, 238, 144); // verde
                    else if (porcentaje <= 100) return new java.awt.Color(255, 215, 0);   // amarillo
                    else return new java.awt.Color(255, 80, 80);    // rojo
                }
            };
        plot.setRenderer(renderer);

        // pasarlo a pdf
        Document documento = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(documento, new FileOutputStream(
            "C:/Users/Lenovo/Desktop/TeoriaBDatosI/metabase/reporte3.pdf"));
        documento.open();
        // crea imagen a grafico 
        BufferedImage imgGrafico = grafico.createBufferedImage(750, 380);
        Image imgPdf = Image.getInstance(imgGrafico, null);
        imgPdf.setAlignment(Image.ALIGN_CENTER);
        documento.add(imgPdf);
        
        documento.close();
        JOptionPane.showMessageDialog(null, "Reporte 3 guardado ");

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, ex.getMessage());
    }
}
    
    public void generarReporte2(int anio, int mes) {
    String sql = "{ call dba.sp_reporte2(?, ?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idPresupuesto);
        cs.setInt(2, anio);
        cs.setInt(3, mes);

        org.jfree.data.general.DefaultPieDataset dataset =
            new org.jfree.data.general.DefaultPieDataset();
        
        List <String> nombreCats = new ArrayList<>();

        try (java.sql.ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                String cat   = rs.getString("nombre_categoria");
                double monto = rs.getDouble("total_gastado");
                dataset.setValue(cat, monto);
                nombreCats.add(cat);
            }
        }
        
        JFreeChart grafico = ChartFactory.createPieChart(
            "Distribución de Gastos por Categoría " + mes + "/" + anio,
            dataset,
            true,  
            true,
            false
        );

        // Estilo
        org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) grafico.getPlot();
        plot.setBackgroundPaint(java.awt.Color.WHITE);

        java.awt.Color[] colores = {
            new java.awt.Color(144, 238, 144),
            new java.awt.Color(255, 215, 0),
            new java.awt.Color(255, 80, 80),
            new java.awt.Color(100, 149, 237),
            new java.awt.Color(255, 165, 0)
        };

        int i = 0;
        for (Object key : dataset.getKeys()) {
            plot.setSectionPaint((Comparable) key, colores[i % colores.length]);
            i++;

        }

        // PDF
        Document documento = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(documento, new FileOutputStream(
            "C:/Users/Lenovo/Desktop/TeoriaBDatosI/metabase/reporte2.pdf"));
        documento.open();


        BufferedImage imagen = grafico.createBufferedImage(700, 450);
        Image imgPdf = Image.getInstance(imagen, null);
        imgPdf.setAlignment(Image.ALIGN_CENTER);
        documento.add(imgPdf);

        documento.close();
        JOptionPane.showMessageDialog(null, "Reporte 2 guardado en el escritorio.");

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
    }
}
    
    public void generarReporte4() {
    String sql = "{ call dba.sp_reporte4(?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idPresupuesto);

        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (java.sql.ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                String categoria = rs.getString("nombre_categoria");
                int anio         = rs.getInt("anio");
                int mes          = rs.getInt("mes");
                double monto     = rs.getDouble("total_gastado");

                dataset.setValue(monto, categoria, mes + "/" + anio);
            }
        }

        JFreeChart grafico = ChartFactory.createLineChart(
            "Tendencia de Gastos por Categoría",
            "Período",
            "Monto",
            dataset,
            PlotOrientation.VERTICAL,
            true,  // leyenda
            true,
            false
        );

        // Estilo de lineas
        org.jfree.chart.plot.CategoryPlot plot = grafico.getCategoryPlot();
        org.jfree.chart.renderer.category.LineAndShapeRenderer renderer =
            new org.jfree.chart.renderer.category.LineAndShapeRenderer();

        // 
        for (int i = 0; i < dataset.getRowCount(); i++) {
            renderer.setSeriesStroke(i, new java.awt.BasicStroke(2.5f));
            renderer.setSeriesShapesVisible(i, true); // puntos en cada uno 
        }
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(java.awt.Color.WHITE);

        // PDF
        Document documento = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(documento, new FileOutputStream(
            "C:/Users/Lenovo/Desktop/TeoriaBDatosI/metabase/reporte4.pdf"));
        documento.open();

        BufferedImage imagen = grafico.createBufferedImage(750, 420);
        Image imgPdf = Image.getInstance(imagen, null);
        imgPdf.setAlignment(Image.ALIGN_CENTER);
        documento.add(imgPdf);

        documento.close();
        JOptionPane.showMessageDialog(null, "Reporte 4 guardado");

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, ex.getMessage());
    }
}
  
    public void generarReporte5(int anio, int mes) {
    String sql = "{ call dba.sp_reporte5(?, ?, ?) }";

    try (Connection con = Conexion.getConexion();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idUsuario);
        cs.setInt(2, anio);
        cs.setInt(3, mes);

        List<String> nombres      = new ArrayList<>();
        List<String> categorias   = new ArrayList<>();
        List<Double> montos       = new ArrayList<>();
        List<Integer> dias        = new ArrayList<>();
        List<String> estados      = new ArrayList<>();
        List<String> ultimosPagos = new ArrayList<>();

        int pagadas = 0, pendientes = 0;

        try (java.sql.ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                String estado = rs.getString("estado_pago");
                nombres.add(rs.getString("nombre_obligacion"));
                categorias.add(rs.getString("nombre_categoria"));
                montos.add(rs.getDouble("monto_fijo_mensual"));
                dias.add(rs.getInt("dia_vencimiento"));
                estados.add(estado);
                ultimosPagos.add(rs.getString("ultimo_pago"));

                if (estado.equals("Pagada")) pagadas++;
                else pendientes++;
            }
        }

        // --- GRÁFICO DE PIE ---
        org.jfree.data.general.DefaultPieDataset dataset =
            new org.jfree.data.general.DefaultPieDataset();
        if (pagadas > 0)    dataset.setValue("Pagadas (" + pagadas + ")",       pagadas);
        if (pendientes > 0) dataset.setValue("Pendientes (" + pendientes + ")", pendientes);

        JFreeChart grafico = ChartFactory.createPieChart(
            "Estado de Obligaciones " + mes + "/" + anio,
            dataset, true, true, false
        );

        org.jfree.chart.plot.PiePlot plot =
            (org.jfree.chart.plot.PiePlot) grafico.getPlot();
        plot.setSectionPaint("Pagadas (" + pagadas + ")",       new java.awt.Color(144, 238, 144)); // verde
        plot.setSectionPaint("Pendientes (" + pendientes + ")", new java.awt.Color(255, 215, 0));   // amarillo
        plot.setBackgroundPaint(java.awt.Color.WHITE);

        // --- PDF ---
        Document documento = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(documento, new FileOutputStream(
            "C:/Users/Lenovo/Desktop/TeoriaBDatosI/metabase/reporte5.pdf"));
        documento.open();

        // Título
        com.itextpdf.text.Font fuenteTitulo = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 16,
            com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Paragraph titulo = new com.itextpdf.text.Paragraph(
            "Reporte 5: Estado de Obligaciones - " + mes + "/" + anio, fuenteTitulo);
        titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        titulo.setSpacingAfter(15);
        documento.add(titulo);

        // --- TABLA ---
        com.itextpdf.text.pdf.PdfPTable tabla =
            new com.itextpdf.text.pdf.PdfPTable(6);
        tabla.setWidthPercentage(100);

        // Encabezados
        String[] encabezados = {"Obligación", "Categoría", "Monto", "Día Vence", "Estado", "Último Pago"};
        for (String enc : encabezados) {
            com.itextpdf.text.pdf.PdfPCell celda =
                new com.itextpdf.text.pdf.PdfPCell(
                    new com.itextpdf.text.Phrase(enc));
            celda.setBackgroundColor(new com.itextpdf.text.BaseColor(180, 210, 230));
            celda.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            tabla.addCell(celda);
        }

        // Filas
        for (int i = 0; i < nombres.size(); i++) {
            com.itextpdf.text.BaseColor color = estados.get(i).equals("Pagada")
                ? new com.itextpdf.text.BaseColor(220, 255, 220)  // verde claro
                : new com.itextpdf.text.BaseColor(255, 255, 200); // amarillo claro

            String[] fila = {
                nombres.get(i),
                categorias.get(i),
                "L. " + montos.get(i),
                "Día " + dias.get(i),
                estados.get(i),
                ultimosPagos.get(i)
            };

            for (String valor : fila) {
                com.itextpdf.text.pdf.PdfPCell celda =
                    new com.itextpdf.text.pdf.PdfPCell(
                        new com.itextpdf.text.Phrase(valor));
                celda.setBackgroundColor(color);
                celda.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                tabla.addCell(celda);
            }
        }
        documento.add(tabla);

        // Gráfico debajo de la tabla
        documento.add(new com.itextpdf.text.Paragraph(" "));
        BufferedImage imagen = grafico.createBufferedImage(500, 300);
        Image imgPdf = Image.getInstance(imagen, null);
        imgPdf.setAlignment(Image.ALIGN_CENTER);
        documento.add(imgPdf);

        documento.close();
        JOptionPane.showMessageDialog(null, "Reporte 5 guardado");

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
    }
}
    
   
}



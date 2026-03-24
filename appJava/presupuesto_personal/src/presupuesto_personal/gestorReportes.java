package presupuesto_personal;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
            
            System.out.println("idUsuario: " + idUsuario);
            System.out.println("idPresupuesto: " + idPresupuesto);
            System.out.println("anio: " + anio);
            System.out.println("mes: " + mes);
    
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

            
            System.out.println("Ingresos: " + ingresos);
            System.out.println("Gastos: "   + gastos);
            System.out.println("Ahorros: "  + ahorros);
            System.out.println("Balance: "  + balance);
            
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
            "C:/Users/Lenovo/Desktop/Reporte3_Cumplimiento.pdf"));
        documento.open();
        // crea imagen a grafico 
        BufferedImage imgGrafico = grafico.createBufferedImage(750, 380);
        Image imgPdf = Image.getInstance(imgGrafico, null);
        imgPdf.setAlignment(Image.ALIGN_CENTER);
        documento.add(imgPdf);
        
        documento.close();
        JOptionPane.showMessageDialog(null, "Reporte 3 guardado en el escritorio.");

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
    }
}

}



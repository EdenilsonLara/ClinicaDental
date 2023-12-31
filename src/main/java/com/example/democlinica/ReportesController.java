package com.example.democlinica;

import com.example.democlinica.BaseDatos.Conexion;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Cell;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class ReportesController {
    private int contadorReporte = 1; // Inicializamos el contador de reporte

    @FXML
    private void generarReportePDF(ActionEvent event) {
        // Ruta donde deseas guardar el archivo PDF
        String rutaDirectorioPDF = "C:\\Users\\lara3\\Desktop\\pdf\\Citas\\";
        String nombreArchivoPDF;

        Connection connection = null;

        try {
            connection = Conexion.getConnection();
            File pdfFile;

            do {
                nombreArchivoPDF = "cita" + contadorReporte + ".pdf";
                pdfFile = new File(rutaDirectorioPDF + nombreArchivoPDF);
                contadorReporte++;
            } while (pdfFile.exists()); // Comprobar si el archivo ya existe

            PdfWriter pdfWriter = new PdfWriter(pdfFile);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            String sql = "SELECT codigoCita, apellidosPaciente, codigoPaciente, codigoSucursal, estado FROM Tabladecitas";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Agregar título al inicio del informe
            document.add(new Paragraph("Reporte de Citas").setFontSize(16).setBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
            document.add(new LineSeparator(new SolidLine()));

            while (resultSet.next()) {
                String codigoCita = resultSet.getString("codigoCita");
                String apellidoPaciente = resultSet.getString("apellidosPaciente");
                String codigoPaciente = resultSet.getString("codigoPaciente");
                String codigoSucursal = resultSet.getString("codigoSucursal");
                String estado = resultSet.getString("estado");

                // Agregar detalles de cada cita
                document.add(new Paragraph("Codigo de la cita: " + codigoCita));
                document.add(new Paragraph("Apellidos del paciente: " + apellidoPaciente));
                document.add(new Paragraph("Codigo del paciente: " + codigoPaciente));
                document.add(new Paragraph("Codigo de la sucursal: " + codigoSucursal));
                document.add(new Paragraph("Estado: " + estado));
                document.add(new LineSeparator(new SolidLine()));
            }

            resultSet.close();
            statement.close();
            document.close();
            mostrarMensaje("Informe PDF generado con éxito en: " + pdfFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("Error al generar el informe PDF.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private int contadorPacientes = 1;


    @FXML
    private void generarPacientes() {
        generarPacientes(null);
    }

    @FXML
    private void generarPacientes(ActionEvent event){
        String rutaDirectorioPDF = "C:\\Users\\lara3\\Desktop\\pdf\\Pacientes\\";
        String nombreArchivoPDF;

        Connection connection = null;

        try {
            connection = Conexion.getConnection();
            File pdfFile;

            do {
                nombreArchivoPDF = "Pacientes" + contadorPacientes + ".pdf";
                pdfFile = new File(rutaDirectorioPDF + nombreArchivoPDF);
                contadorPacientes++;
            } while (pdfFile.exists()); // Comprobar si el archivo ya existe

            PdfWriter pdfWriter = new PdfWriter(pdfFile);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            String sql = "SELECT nombresPaciente,genero,fechaNacimiento,dui,telefono  FROM tablaDePacientes";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            document.add(new Paragraph("Reporte de Pacientes").setFontSize(16).setBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
            document.add(new LineSeparator(new SolidLine()));

            while (resultSet.next()) {
                String nombresPaciente = resultSet.getString("nombresPaciente");
                String genero = resultSet.getString("genero");
                String fechaNacimiento = resultSet.getString("fechaNacimiento");
                String dui = resultSet.getString("dui");
                String telefono = resultSet.getString("telefono");



                document.add(new Paragraph("Nombre del paciente: " + nombresPaciente));
                document.add(new Paragraph("Genero del paciente: " + genero));
                document.add(new Paragraph("Fecha de Naciento : " + fechaNacimiento));
                document.add(new Paragraph("Dui: " + dui));
                document.add(new Paragraph("Telefono: " + telefono));
                document.add(new LineSeparator(new SolidLine()));
            }
            resultSet.close();
            statement.close();
            document.close();
            mostrarMensaje("Informe PDF generado con éxito en: " + pdfFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("Error al generar el informe PDF.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private int contadorTratamientos = 1;


    @FXML
    private void generarTratamientos() {
        generarTratameintos(null);
    }

    @FXML
    private void generarTratameintos(ActionEvent event){
        String rutaDirectorioPDF = "C:\\Users\\lara3\\Desktop\\pdf\\Tratamientos\\";
        String nombreArchivoPDF;

        Connection connection = null;

        try {
            connection = Conexion.getConnection();
            File pdfFile;

            do {
                nombreArchivoPDF = "Tratamientos" + contadorTratamientos+ ".pdf";
                pdfFile = new File(rutaDirectorioPDF + nombreArchivoPDF);
                contadorTratamientos++;
            } while (pdfFile.exists()); // Comprobar si el archivo ya existe

            PdfWriter pdfWriter = new PdfWriter(pdfFile);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            String sql = "SELECT codigoTratamiento,tipoServicio,costo FROM tablaDeTratamientos";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            document.add(new Paragraph("Reporte de Tratamientos").setFontSize(16).setBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
            document.add(new LineSeparator(new SolidLine()));


            while (resultSet.next()) {
                String codigoTratamiento = resultSet.getString("codigoTratamiento");
                String tipoServicio = resultSet.getString("tipoServicio");
                String costo = resultSet.getString("costo");



                document.add(new Paragraph("Codigo del Tratameinto: " + codigoTratamiento));
                document.add(new Paragraph("Tipo de Servicio: " + tipoServicio));
                document.add(new Paragraph("Costo: " + costo));
                document.add(new LineSeparator(new SolidLine()));
            }
            resultSet.close();
            statement.close();
            document.close();
            mostrarMensaje("Informe PDF generado con éxito en: " + pdfFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("Error al generar el informe PDF.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private int contadorInventario= 1;


    @FXML
    private void generarInventario() {
        generarInventario(null);
    }

    @FXML
    private void generarInventario(ActionEvent event){
        String rutaDirectorioPDF = "C:\\Users\\lara3\\Desktop\\pdf\\Inventario\\";
        String nombreArchivoPDF;

        Connection connection = null;

        try {
            connection = Conexion.getConnection();
            File pdfFile;

            do {
                nombreArchivoPDF = "Inventario" + contadorInventario + ".pdf";
                pdfFile = new File(rutaDirectorioPDF + nombreArchivoPDF);
                contadorInventario++;
            } while (pdfFile.exists()); // Comprobar si el archivo ya existe

            PdfWriter pdfWriter = new PdfWriter(pdfFile);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            String sql = "SELECT codigoInventario,nombreEquipo,descripcion,precio,cantidad FROM tablaDeInventario";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            document.add(new Paragraph("Reporte de Inventario").setFontSize(16).setBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
            document.add(new LineSeparator(new SolidLine()));


            while (resultSet.next()) {
                String codigoInventario = resultSet.getString("codigoInventario");
                String nombreEquipo = resultSet.getString("nombreEquipo");
                String descripcion = resultSet.getString("descripcion");
                String precio = resultSet.getString("precio");
                String cantidad = resultSet.getString("cantidad");



                document.add(new Paragraph("Codigo del Inventario: " + codigoInventario ));
                document.add(new Paragraph("Nombre Del Equipo: " + nombreEquipo));
                document.add(new Paragraph("Descripcion: " + descripcion));
                document.add(new Paragraph("Precio: " + precio ));
                document.add(new Paragraph("Cantidad: " + cantidad));
                document.add(new LineSeparator(new SolidLine()));
            }

            resultSet.close();
            statement.close();
            document.close();
            mostrarMensaje("Informe PDF generado con éxito en: " + pdfFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("Error al generar el informe PDF.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private int contadorEmpleados= 1;


    @FXML
    private void generarEmpleados() {
        generarEmpleados(null);
    }

    @FXML
    private void generarEmpleados(ActionEvent event){
        String rutaDirectorioPDF = "C:\\Users\\lara3\\Desktop\\pdf\\Empleados\\";
        String nombreArchivoPDF;

        Connection connection = null;

        try {
            connection = Conexion.getConnection();
            File pdfFile;

            do {
                nombreArchivoPDF = "Empleados" + contadorEmpleados + ".pdf";
                pdfFile = new File(rutaDirectorioPDF + nombreArchivoPDF);
                contadorEmpleados++;
            } while (pdfFile.exists()); // Comprobar si el archivo ya existe

            PdfWriter pdfWriter = new PdfWriter(pdfFile);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            String sql = "SELECT nombresEmpleado,fechaNacimiento,genero,dui,telefono  FROM tablaDeEmpleados";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            document.add(new Paragraph("Reporte de Empleado").setFontSize(16).setBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
            document.add(new LineSeparator(new SolidLine()));


            while (resultSet.next()) {
                String nombresEmpleado = resultSet.getString("nombresEmpleado");
                String fechaNacimiento = resultSet.getString("fechaNacimiento");
                String genero = resultSet.getString("genero");
                String dui = resultSet.getString("dui");
                String telefono = resultSet.getString("telefono");



                document.add(new Paragraph("Nombre Del Empleado: " + nombresEmpleado ));
                document.add(new Paragraph("Fecha Del Nacimiento: " + fechaNacimiento));
                document.add(new Paragraph("Genero: " + genero));
                document.add(new Paragraph("Dui: " + dui ));
                document.add(new Paragraph("Telefono: " + telefono));
                document.add(new LineSeparator(new SolidLine()));
            }

            resultSet.close();
            statement.close();
            document.close();
            mostrarMensaje("Informe PDF generado con éxito en: " + pdfFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("Error al generar el informe PDF.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private int contadorProveedores= 1;


    @FXML
    private void generarProveedores() {
        generarProveedores(null);
    }

    @FXML
    private void generarProveedores(ActionEvent event){
        String rutaDirectorioPDF = "C:\\Users\\lara3\\Desktop\\pdf\\Proveedores\\";
        String nombreArchivoPDF;

        Connection connection = null;

        try {
            connection = Conexion.getConnection();
            File pdfFile;

            do {
                nombreArchivoPDF = "Proveedores" + contadorProveedores + ".pdf";
                pdfFile = new File(rutaDirectorioPDF + nombreArchivoPDF);
                contadorProveedores++;
            } while (pdfFile.exists()); // Comprobar si el archivo ya existe

            PdfWriter pdfWriter = new PdfWriter(pdfFile);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            String sql = "SELECT nombreEmpresa,telefono,direccion,nombreContacto,codigoSucursal  FROM tablaDeProveedores";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            document.add(new Paragraph("Reporte de Proveedores").setFontSize(16).setBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
            document.add(new LineSeparator(new SolidLine()));


            while (resultSet.next()) {
                String nombreEmpresa = resultSet.getString("nombreEmpresa");
                String telefono = resultSet.getString("telefono");
                String direccion = resultSet.getString("direccion");
                String nombreContacto = resultSet.getString("nombreContacto");
                String codigoSucursal = resultSet.getString("codigoSucursal");



                document.add(new Paragraph("Nombre Del Empleado: " + nombreEmpresa ));
                document.add(new Paragraph("Fecha Del Nacimiento: " + telefono));
                document.add(new Paragraph("Genero: " + direccion));
                document.add(new Paragraph("nombreContacto: " + nombreContacto ));
                document.add(new Paragraph("Telefono: " + codigoSucursal));
                document.add(new LineSeparator(new SolidLine()));
            }

            resultSet.close();
            statement.close();
            document.close();
            mostrarMensaje("Informe PDF generado con éxito en: " + pdfFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("Error al generar el informe PDF.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText("el reporte a sido creado exitosamente.");
        alert.showAndWait();
        // Implementa un cuadro de diálogo o notificación para mostrar el mensaje.
        // Puedes utilizar Alert u otro componente según tus preferencias.
    }


    @FXML
    private void irATratamientos(ActionEvent event) throws IOException {
        // Cargar la vista de tratamientos desde tratamientos.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tratamiento.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la vista de tratamientos (si es necesario)
        tratamientoController tratamientosController = loader.getController();

        // Crear una nueva escena con la vista de tratamientos
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        // Mostrar la nueva vista
        stage.show();
    }
    @FXML
    private void irACitas(ActionEvent event) throws IOException {
        // Cargar la vista de citas desde citas.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("citas.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la vista de citas
        citasController citasController = loader.getController();

        // Crear una nueva escena con la vista de citas
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        // Mostrar la nueva vista
        stage.show();
    }


    @FXML
    private void irAInicio(ActionEvent event) throws IOException {
        // Cargar la vista de inicio desde inicio.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("inicio.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la vista de inicio
        Inicio inicioController = loader.getController();

        // Crear una nueva escena con la vista de inicio
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        // Mostrar la nueva vista
        stage.show();
    }

    @FXML
    private void irAPacientes(ActionEvent event) throws IOException {
        // Cargar la vista de pacientes desde pacientes.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pacientes.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la vista de pacientes (si es necesario)
        pacienteControler pacientesController = loader.getController();

        // Crear una nueva escena con la vista de pacientes
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        // Mostrar la nueva vista
        stage.show();
    }

    @FXML
    private void irAInventario(ActionEvent event) throws IOException {
        // Cargar la vista de inventario desde inventario.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Inventario.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la vista de inventario (si es necesario)
        inventarioController inventarioController = loader.getController();

        // Crear una nueva escena con la vista de inventario
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        // Mostrar la nueva vista
        stage.show();
    }
    @FXML
    private void irAEmpleados(ActionEvent event) throws IOException {
        // Cargar la vista de empleados desde empleados.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Empleado.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la vista de empleados (si es necesario)
        EmpleadoController empleadoController = loader.getController();

        // Crear una nueva escena con la vista de empleados
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        // Mostrar la nueva vista
        stage.show();
    }
    @FXML
    private void irAProveedor(ActionEvent event) throws IOException {
        // Cargar la vista de proveedor desde proveedor.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Proveedor.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la vista de proveedor (si es necesario)
        ProveedorController proveedorController = loader.getController();

        // Crear una nueva escena con la vista de proveedor
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        // Mostrar la nueva vista
        stage.show();
    }
    public void userLogOut(ActionEvent event) {
        // Mostrar un cuadro de diálogo de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de salida");
        alert.setHeaderText("¿Está seguro de que desea salir del sitio?");
        alert.setContentText("Seleccione Aceptar para salir o Cancelar para continuar.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // El usuario seleccionó Aceptar, cerrar la aplicación
            Platform.exit();
        }

    }



    @FXML
    private void irAReportes(ActionEvent event) throws IOException {
        // Cargar la vista de reportes desde reportes.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Reportes.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la vista de reportes (si es necesario)
        ReportesController reportesController = loader.getController();

        // Crear una nueva escena con la vista de reportes
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        // Mostrar la nueva vista
        stage.show();
    }
}

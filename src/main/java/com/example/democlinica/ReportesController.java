package com.example.democlinica;

import com.example.democlinica.BaseDatos.Conexion;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
        String rutaDirectorioPDF = "C:\\Users\\DELL\\Desktop\\pdf\\";
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

            while (resultSet.next()) {
                String codigoCita = resultSet.getString("codigoCita");
                String apellidoPaciente = resultSet.getString("apellidosPaciente");
                String codigoPaciente = resultSet.getString("codigoPaciente");
                String codigoSucursal = resultSet.getString("codigoSucursal");
                String estado = resultSet.getString("estado");

                document.add(new Paragraph("Codigo de la cita: " + codigoCita));
                document.add(new Paragraph("Apellidos del paciente: " + apellidoPaciente));
                document.add(new Paragraph("Codigo del paciente: " + codigoPaciente));
                document.add(new Paragraph("Codigo de la surcusal: " + codigoSucursal));
                document.add(new Paragraph("Estado: " + estado));
                document.add(new Paragraph("-------------------------------------------------------"));
            }
            resultSet.close();
            statement.close();
            document.close();
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

    // Resto del código de la clase...




// Resto del código de la clase...





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

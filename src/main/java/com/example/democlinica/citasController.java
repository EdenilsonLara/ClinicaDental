package com.example.democlinica;

import com.example.democlinica.BaseDatos.Conexion;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class citasController {

    @FXML
    private TextField nombresTextField;
    @FXML
    private TextField apellidosTextField;
    @FXML
    private DatePicker fechaConsultaDatePicker;
    @FXML
    private TextField motivoTextField;
    @FXML
    private TextField codigoTratamientoTextField;
    @FXML
    private TextField costoTextField;
    @FXML
    private ComboBox<String> estadoComboBox;
    @FXML
    private TextField codigoPacienteTextField;
    @FXML
    private Label errorLabel;

    @FXML
    private void guardarCita(ActionEvent event) {
        String nombres = nombresTextField.getText();
        String apellidos = apellidosTextField.getText();
        String motivo = motivoTextField.getText();
        String codigoTratamiento = codigoTratamientoTextField.getText();
        String costo = costoTextField.getText();
        String fechaConsulta = fechaConsultaDatePicker.getValue().toString();
        String estado = estadoComboBox.getValue();
        String codigoPaciente = codigoPacienteTextField.getText();

        // Validación de campos
        if (nombres.isEmpty() || apellidos.isEmpty() || motivo.isEmpty() || codigoTratamiento.isEmpty() || costo.isEmpty() || fechaConsulta.isEmpty() || estado == null ) {
            mostrarAlerta("Campos Vacíos", "Por favor, complete todos los campos, incluyendo el estado.");
            return;
        }


        if (!esValidoNombre(nombres) || !esValidoNombre(apellidos)) {
            mostrarError("Nombres y apellidos no pueden contener números.");
            return;
        }


        if (!esValidoDUI(codigoPaciente)) {
            mostrarError("DUI inválido o formato incorrecto.");
            return;
        }


        if (!esValidoTelefono(costo)) {
            mostrarError("Costo inválido o formato incorrecto.");
            return;
        }


        try {
            Connection conn = Conexion.getConnection();
            String sql = "INSERT INTO tablaDeCitas (nombresPaciente, apellidosPaciente, motivoConsulta, codigoTratamiento, costo, fecha, estado, codigoPaciente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombres);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, motivo);
            pstmt.setString(4, codigoTratamiento);
            pstmt.setString(5, costo);
            pstmt.setString(6, fechaConsulta);
            pstmt.setString(7, estado);


            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Cita Guardada", "La cita se ha guardado correctamente en la base de datos.");
            } else {
                mostrarAlerta("Error", "No se pudo guardar la cita en la base de datos.");
            }

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
        }
    }

    // Otras funciones y métodos

    // Función para validar nombres y apellidos
    private boolean esValidoNombre(String texto) {
        return !texto.matches(".*\\d.*") && texto.length() <= 31;
    }

    // Función para validar DUI y el formato de número de identificación salvadoreño
    private boolean esValidoDUI(String dui) {
        // El formato del DUI es de 9 números con un guión
        Pattern pattern = Pattern.compile("\\d{8}-\\d");
        Matcher matcher = pattern.matcher(dui);
        return matcher.matches();
    }

    // Función para validar teléfono y el formato de 8 dígitos con guión
    private boolean esValidoTelefono(String telefono) {
        // El formato del teléfono es de 8 números con un guión en el medio
        Pattern pattern = Pattern.compile("\\d{4}-\\d{4}");
        Matcher matcher = pattern.matcher(telefono);
        return matcher.matches();
    }

    // Función para mostrar mensajes de error
    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
    }






    // Método para cambiar a la vista de inicio
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


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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


    public void salircita(ActionEvent event) {
        // Mostrar un cuadro de diálogo de confirmación
        Alert alert = new Alert(AlertType.CONFIRMATION);
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

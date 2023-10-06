package com.example.democlinica;

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
import java.util.Optional;

public class citasController {

    @FXML
    private Button guardarButton;
    @FXML
    private TextField pacienteTextField;
    @FXML
    private TextField motivoTextField;
    @FXML
    private TextField tratamientoTextField;
    @FXML
    private TextField costoTextField;
    @FXML
    private DatePicker fechaDatePicker;
    @FXML
    private Button iniciobtn;





    @FXML
    private void guardarCita(ActionEvent event) {
        String paciente = pacienteTextField.getText().trim();
        String motivo = motivoTextField.getText().trim();
        String tratamiento = tratamientoTextField.getText().trim();
        String costo = costoTextField.getText().trim();

        if (paciente.isEmpty() || motivo.isEmpty() || tratamiento.isEmpty() || costo.isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Por favor, complete todos los campos.");
            return;
        }


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



}

package com.example.democlinica;

import com.example.democlinica.BaseDatos.Conexion;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import javafx.collections.ObservableList;


public class ProveedorController {

    @FXML
    private TextField nombresProveedorTextField;
    @FXML
    private TextField direccionProveedorTextField;
    @FXML
    private TextField telefonoProveedorTextField;
    @FXML
    private TextField nombreContactoProveedorTextField;
    @FXML
    private TableView<Proveedor> proveedoresTable;
    @FXML
    private TableColumn<Proveedor, Integer> codigoProveedorColumn;
    @FXML
    private TableColumn<Proveedor, String> nombresProveedorColumn;
    @FXML
    private TableColumn<Proveedor, String> direccionProveedorColumn;
    @FXML
    private TableColumn<Proveedor, String> telefonoProveedorColumn;
    @FXML
    private TableColumn<Proveedor, String> nombreContactoProveedorColumn;
    @FXML
    private TableView<Proveedor> proveedoresTableView;

    private ObservableList<Proveedor> proveedores = FXCollections.observableArrayList();




    @FXML
    private void guardarProveedor(ActionEvent event) {
        String nombresProveedor = nombresProveedorTextField.getText();
        String direccionProveedor = direccionProveedorTextField.getText();
        String telefonoProveedor = telefonoProveedorTextField.getText();
        String nombreContactoProveedor = nombreContactoProveedorTextField.getText();

        if (nombresProveedor.isEmpty() || direccionProveedor.isEmpty() || telefonoProveedor.isEmpty() || nombreContactoProveedor.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos.");
            return;
        }
        // Validación de nombres y nombre de contacto
        if (!nombresProveedor.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            mostrarAlerta("Error", "El nombre del proveedor solo debe contener letras.");
            return;
        }

        if (!nombreContactoProveedor.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            mostrarAlerta("Error", "El nombre de contacto solo debe contener letras.");
            return;
        }
        // Validación del campo de teléfono
        if (!telefonoProveedor.matches("^[0-9 -]+$")) {
            mostrarAlerta("Error", "El campo de teléfono debe contener solo números.");
            return;
        }

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO tablaDeProveedores (nombreEmpresa, direccion, telefono, nombreContacto) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, nombresProveedor);
            pstmt.setString(2, direccionProveedor);
            pstmt.setString(3, telefonoProveedor);
            pstmt.setString(4, nombreContactoProveedor);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Proveedor Guardado", "El proveedor se ha guardado correctamente en la base de datos.");
                limpiarCampos();
                actualizarTablaProveedores();
            } else {
                mostrarAlerta("Error", "No se pudo guardar el proveedor en la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
        }
    }

    @FXML
    private void initialize() {
        // Agregar un listener para el campo de teléfono
        telefonoProveedorTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() == 4 && !newValue.contains("-")) {
                    telefonoProveedorTextField.setText(newValue + "-");
                } else if (newValue.length() > 9) {
                    telefonoProveedorTextField.setText(oldValue); // Restaurar el valor anterior si se superan 8 dígitos
                }
            }
        });
        // Configura las columnas para mostrar los datos
        codigoProveedorColumn.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
        nombresProveedorColumn.setCellValueFactory(new PropertyValueFactory<>("nombresProveedor"));
        direccionProveedorColumn.setCellValueFactory(new PropertyValueFactory<>("direccionProveedor"));
        telefonoProveedorColumn.setCellValueFactory(new PropertyValueFactory<>("telefonoProveedor"));
        nombreContactoProveedorColumn.setCellValueFactory(new PropertyValueFactory<>("nombreContactoProveedor"));

        // Habilita la edición en las columnas necesarias (por ejemplo, nombresProveedor, direccionProveedor, etc.)
        nombresProveedorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nombresProveedorColumn.setOnEditCommit(event -> {
            Proveedor proveedor = event.getRowValue();
            proveedor.setNombresProveedor(event.getNewValue());
            actualizarProveedorEnBaseDeDatos(proveedor);
        });

        direccionProveedorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        direccionProveedorColumn.setOnEditCommit(event -> {
            Proveedor proveedor = event.getRowValue();
            proveedor.setDireccionProveedor(event.getNewValue());
            actualizarProveedorEnBaseDeDatos(proveedor);
        });

        // Configura la TableView para permitir la edición
        proveedoresTableView.setEditable(true);

        // Llama a la función para cargar los proveedores desde la base de datos

            actualizarTablaProveedores();

    }


    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        nombresProveedorTextField.clear();
        direccionProveedorTextField.clear();
        telefonoProveedorTextField.clear();
        nombreContactoProveedorTextField.clear();
    }


    @FXML
    private void editarProveedor(ActionEvent event) {
        Proveedor proveedorSeleccionado = proveedoresTableView.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado != null) {
            // Obtener datos del proveedor seleccionado
            String nuevosNombresProveedor = nombresProveedorTextField.getText();
            String nuevaDireccionProveedor = direccionProveedorTextField.getText();
            String nuevoTelefonoProveedor = telefonoProveedorTextField.getText();
            String nuevoNombreContactoProveedor = nombreContactoProveedorTextField.getText();

            // Realizar la validación de los campos como sea necesario

            try {
                Connection conn = Conexion.getConnection();
                String sql = "UPDATE tablaDeProveedores SET nombreEmpresa = ?, direccion = ?, telefono = ?, nombreContacto = ? WHERE codigoProveedor = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nuevosNombresProveedor);
                pstmt.setString(2, nuevaDireccionProveedor);
                pstmt.setString(3, nuevoTelefonoProveedor);
                pstmt.setString(4, nuevoNombreContactoProveedor);
                pstmt.setInt(5, proveedorSeleccionado.getCodigoProveedor());

                int filasActualizadas = pstmt.executeUpdate();

                if (filasActualizadas > 0) {
                    mostrarAlerta("Proveedor Actualizado", "Los datos del proveedor se han actualizado correctamente en la base de datos.");
                    actualizarTablaProveedores();
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar el proveedor en la base de datos.");
                }

                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
            }
        }
    }

    @FXML
    private void eliminarProveedor(ActionEvent event) {
        Proveedor proveedorSeleccionado = proveedoresTableView.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado != null) {
            try {
                Connection conn = Conexion.getConnection();
                String sql = "DELETE FROM tablaDeProveedores WHERE codigoProveedor = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, proveedorSeleccionado.getCodigoProveedor());

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlerta("Proveedor Eliminado", "El proveedor se ha eliminado correctamente de la base de datos.");
                    actualizarTablaProveedores();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el proveedor de la base de datos.");
                }

                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
            }
        }
    }
















    private void actualizarTablaProveedores() {
        proveedores.clear();
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tablaDeProveedores")) {
            while (rs.next()) {
                int codigoProveedor = rs.getInt("codigoProveedor");
                String nombreEmpresa = rs.getString("nombreEmpresa");
                String telefono = rs.getString("telefono");
                String direccion = rs.getString("direccion");
                String nombreContacto = rs.getString("nombreContacto");
                Proveedor proveedor = new Proveedor(codigoProveedor, nombreEmpresa, telefono, direccion, nombreContacto);
                proveedores.add(proveedor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
        }
        proveedoresTableView.setItems(proveedores);
    }

    private void actualizarProveedorEnBaseDeDatos(Proveedor proveedor) {
        try {
            Connection conn = Conexion.getConnection();
            String sql = "UPDATE tablaDeProveedores SET nombreEmpresa = ?, telefono = ?, direccion = ?, nombreContacto = ? WHERE codigoProveedor = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, proveedor.getNombresProveedor());
            pstmt.setString(2, proveedor.getTelefonoProveedor());
            pstmt.setString(3, proveedor.getDireccionProveedor());
            pstmt.setString(4, proveedor.getNombreContactoProveedor());
            pstmt.setInt(5, proveedor.getCodigoProveedor());

            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                mostrarAlerta("Proveedor Actualizado", "Los datos del proveedor se han actualizado correctamente en la base de datos.");
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el proveedor en la base de datos.");
            }

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
        }
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

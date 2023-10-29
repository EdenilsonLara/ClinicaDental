package com.example.democlinica;

import com.example.democlinica.BaseDatos.Conexion;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalDate;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;


public class EmpleadoController {

    @FXML
    private TableView<Empleado> empleadosTable;
    @FXML
    private TableColumn<Empleado, Integer> codigoColumn;
    @FXML
    private TableColumn<Empleado, String> nombresColumn;
    @FXML
    private TableColumn<Empleado, String> apellidosColumn;
    @FXML
    private TableColumn<Empleado, LocalDate> fechaNacimientoColumn;
    @FXML
    private TableColumn<Empleado, String> generoColumn;
    @FXML
    private TableColumn<Empleado, String> duiColumn;
    @FXML
    private TableColumn<Empleado, String> telefonoColumn;
    @FXML
    private TextField nombresTextField;
    @FXML
    private TextField apellidosTextField;
    @FXML
    private DatePicker fechaNacimientoDatePicker;
    @FXML
    private TextField generoTextField;
    @FXML
    private TextField duiTextField;
    @FXML
    private TextField telefonoTextField;
    @FXML
    private TableView<Empleado> empleadosTableView;
    @FXML
    private ComboBox<String> generoComboBox;



    public EmpleadoController() {
    }


    @FXML
    private void crearEmpleado(ActionEvent event) {
        try (Connection connection = Conexion.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO tablaDeEmpleados (nombresEmpleado, apellidosEmpleado, fechaNacimiento, genero, dui, telefono, codigoSucursal) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            // Obtén los datos de los campos de la interfaz de usuario (nombre, apellidos, etc.)
            String nombres = nombresTextField.getText();
            String apellidos = apellidosTextField.getText();
            LocalDate fechaNacimiento = fechaNacimientoDatePicker.getValue();
            String genero = generoComboBox.getValue();
            String dui = duiTextField.getText();
            String telefono = telefonoTextField.getText();
            int codigoSucursal = 1; // Opcional: obten el código de sucursal de alguna manera

            // Validación de campos vacíos
            if (nombres.isEmpty() || apellidos.isEmpty() || fechaNacimiento == null || genero == null || dui.isEmpty() || telefono.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return;
            }
            // Validación de nombres y apellidos
            if (!nombres.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$") || !apellidos.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
                mostrarAlerta("Error", "Los nombres y apellidos solo deben contener letras.");
                return;
            }
            // Validación del campo de DUI
            if (!dui.matches("^[0-9 -]+$")) {
                mostrarAlerta("Error", "El campo de Dui debe contener solo números.");
                return;
            }
            // Validación del campo de teléfono
            if (!telefono.matches("^[0-9 -]+$")) {
                mostrarAlerta("Error", "El campo de teléfono debe contener solo números.");
                return;
            }

            // Configura los parámetros en la consulta preparada
            stmt.setString(1, nombres);
            stmt.setString(2, apellidos);
            stmt.setDate(3, Date.valueOf(fechaNacimiento));
            stmt.setString(4, genero);
            stmt.setString(5, dui);
            stmt.setString(6, telefono);
            stmt.setInt(7, codigoSucursal);

            // Ejecuta la consulta
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Éxito: el empleado se creó correctamente
                mostrarMensaje("Empleado creado con éxito.", Alert.AlertType.INFORMATION);
                limpiarCampos();
                actualizarTablaEmpleados();
            } else {
                // Error: no se pudo crear el empleado
                mostrarMensaje("No se pudo crear el empleado.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensaje("Error de base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarMensaje(String mensaje, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        // Agregar un listener para el campo de teléfono
        telefonoTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() == 4 && !newValue.contains("-")) {
                    telefonoTextField.setText(newValue + "-");
                } else if (newValue.length() > 9) {
                    telefonoTextField.setText(oldValue); // Restaurar el valor anterior si se superan 8 dígitos
                }
            }
        });


        duiTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() == 8 && !newValue.contains("-")) {
                    duiTextField.setText(newValue + "-");
                } else if (newValue.length() > 10) {
                    duiTextField.setText(oldValue); // Restaurar el valor anterior si se superan 11 dígitos
                }
            }
        });
        // Configura las columnas para mostrar los datos de los empleados
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
        nombresColumn.setCellValueFactory(new PropertyValueFactory<>("nombresEmpleado"));
        apellidosColumn.setCellValueFactory(new PropertyValueFactory<>("apellidosEmpleado"));
        generoColumn.setCellValueFactory(new PropertyValueFactory<>("genero"));
        fechaNacimientoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        duiColumn.setCellValueFactory(new PropertyValueFactory<>("dui"));

        // Habilita la edición en las columnas necesarias (por ejemplo, nombresEmpleado, apellidosEmpleado, etc.)
        nombresColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nombresColumn.setOnEditCommit(event -> {
            Empleado empleado = event.getRowValue();
            empleado.setNombresEmpleado(event.getNewValue());
            actualizarEmpleadoEnBaseDeDatos(empleado);
        });

        apellidosColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        apellidosColumn.setOnEditCommit(event -> {
            Empleado empleado = event.getRowValue();
            empleado.setApellidosEmpleado(event.getNewValue());
            actualizarEmpleadoEnBaseDeDatos(empleado);
        });

        // Configura la TableView para permitir la edición
        empleadosTable.setEditable(true);

        // Llama a la función para cargar los empleados desde la base de datos
        try {
            actualizarTablaEmpleados();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void editarEmpleado(ActionEvent event) {
        Empleado empleadoSeleccionado = empleadosTableView.getSelectionModel().getSelectedItem();
        if (empleadoSeleccionado != null) {
            // Obtener datos del empleado seleccionado desde los campos de edición
            String nuevosNombres = nombresTextField.getText();
            String nuevosApellidos = apellidosTextField.getText();
            LocalDate nuevaFechaNacimiento = fechaNacimientoDatePicker.getValue();
            String nuevoGenero = generoComboBox.getValue();
            String nuevoTelefono = telefonoTextField.getText();
            String nuevoDUI = duiTextField.getText();

            // Realiza validación de campos, similar a la validación en tratamientoController

            try {
                Connection conn = Conexion.getConnection();
                String sql = "UPDATE tablaDeEmpleados SET nombresEmpleado = ?, apellidosEmpleado = ?, fechaNacimiento = ?, genero = ?, telefono = ?, dui = ? WHERE codigoEmpleado = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nuevosNombres);
                pstmt.setString(2, nuevosApellidos);
                pstmt.setDate(3, Date.valueOf(nuevaFechaNacimiento));
                pstmt.setString(4, nuevoGenero);
                pstmt.setString(5, nuevoTelefono);
                pstmt.setString(6, nuevoDUI);
                pstmt.setInt(7, empleadoSeleccionado.getCodigoEmpleado());

                int filasActualizadas = pstmt.executeUpdate();

                if (filasActualizadas > 0) {
                    mostrarAlerta("Empleado Actualizado", "Los datos del empleado se han actualizado correctamente en la base de datos.");
                    actualizarTablaEmpleados();
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar el empleado en la base de datos.");
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
    private void eliminarEmpleado(ActionEvent event) {
        Empleado empleadoSeleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (empleadoSeleccionado != null) {
            try {
                Connection conn = Conexion.getConnection();
                String sql = "DELETE FROM tablaDeEmpleados WHERE codigoEmpleado = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, empleadoSeleccionado.getCodigoEmpleado());

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlerta("Empleado Eliminado", "El empleado se ha eliminado correctamente de la base de datos.");
                    actualizarTablaEmpleados();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el empleado de la base de datos.");
                }

                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
            }
        }
    }


    private void limpiarCampos() {
        nombresTextField.clear();
        apellidosTextField.clear();
        fechaNacimientoDatePicker.setValue(null);
        duiTextField.clear();
        telefonoTextField.clear();
    }

    private void actualizarTablaEmpleados() throws SQLException {
        empleadosTable.getItems().clear(); // Borra los elementos actuales de la tabla
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM tablaDeEmpleados";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int codigoEmpleado = rs.getInt("codigoEmpleado");
                String nombresEmpleado = rs.getString("nombresEmpleado");
                String apellidosEmpleado = rs.getString("apellidosEmpleado");
                LocalDate fechaNacimiento = rs.getDate("fechaNacimiento").toLocalDate();
                String genero = rs.getString("genero");
                String dui = rs.getString("dui");
                String telefono = rs.getString("telefono");
                // Agregar cualquier otro campo que necesites aquí

                Empleado empleado = new Empleado(codigoEmpleado, nombresEmpleado, apellidosEmpleado, fechaNacimiento, genero, dui, telefono);
                empleadosTable.getItems().add(empleado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void actualizarEmpleadoEnBaseDeDatos(Empleado empleado) {
        try {
            Connection conn = Conexion.getConnection();
            String sql = "UPDATE tablaDeEmpleados SET nombresEmpleado = ?, apellidosEmpleado = ?, fechaNacimiento = ?, genero = ?, telefono = ?, dui = ? WHERE codigoEmpleado = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, empleado.getNombresEmpleado());
            pstmt.setString(2, empleado.getApellidosEmpleado());
            pstmt.setDate(3, Date.valueOf(empleado.getFechaNacimiento()));
            pstmt.setString(4, empleado.getGenero());
            pstmt.setString(5, empleado.getTelefono());
            pstmt.setString(6, empleado.getDui());
            pstmt.setInt(7, empleado.getCodigoEmpleado());

            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                mostrarAlerta("Empleado Actualizado", "Los datos del empleado se han actualizado correctamente en la base de datos.");
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el empleado en la base de datos.");
            }

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
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

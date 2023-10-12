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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.util.converter.IntegerStringConverter;


public class tratamientoController {

    @FXML
    private TextField tipoServicioTextField;
    @FXML
    private TextField costoTextField;
    @FXML
    private TableView<Tratamiento> tratamientoTableView;
    @FXML
    private TableColumn<Tratamiento, Integer> codigoTratamientoColumn;
    @FXML
    private TableColumn<Tratamiento, String> tipoServicioColumn;
    @FXML
    private TableColumn<Tratamiento, Integer> costoColumn;
    @FXML
    private Button actualizar;


    private ObservableList<Tratamiento> tratamientos = FXCollections.observableArrayList();





    @FXML
    private void guardarTratamiento(ActionEvent event) {
        String tipoServicio = tipoServicioTextField.getText();
        String costoText = costoTextField.getText();

        // Validación de campos
        if (tipoServicio.isEmpty() || costoText.isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Por favor, complete todos los campos.");
            return;
        }

        try {
            int costo = Integer.parseInt(costoText); // Convertir el costo a entero

            Connection conn = Conexion.getConnection();
            String sql = "INSERT INTO tablaDeTratamientos (TipoServicio, Costo) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tipoServicio);
            pstmt.setInt(2, costo);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Tratamiento Guardado", "El tratamiento se ha guardado correctamente en la base de datos.");
                actualizarTablaTratamientos();
            } else {
                mostrarAlerta("Error", "No se pudo guardar el tratamiento en la base de datos.");
            }

            pstmt.close();
            conn.close();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error en el Costo", "El costo debe ser un valor numérico.");
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
        }
    }

    @FXML
    private void initialize() {
        // Configura las columnas para mostrar los datos
        codigoTratamientoColumn.setCellValueFactory(new PropertyValueFactory<>("codigoTratamiento"));
        tipoServicioColumn.setCellValueFactory(new PropertyValueFactory<>("tipoServicio"));
        costoColumn.setCellValueFactory(new PropertyValueFactory<>("costo"));

        // Habilita la edición en la columna "tipoServicio"
        tipoServicioColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tipoServicioColumn.setOnEditCommit(event -> {
            Tratamiento tratamiento = event.getRowValue();
            tratamiento.setTipoServicio(event.getNewValue());
            actualizarTratamientoEnBaseDeDatos(tratamiento);
        });
        // Habilita la edición en la columna "costo"
        costoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        costoColumn.setOnEditCommit(event -> {
            Tratamiento tratamiento = event.getRowValue();
            tratamiento.setCosto(event.getNewValue());
            actualizarTratamientoEnBaseDeDatos(tratamiento);
        });
        // Llama a la función para cargar los tratamientos
        try {
            actualizarTablaTratamientos();
        } catch (SQLException e) {
            e.printStackTrace();}

        tratamientoTableView.setEditable(true);

  //      tipoServicioColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // Permite la edición en la columna tipoServicio
   //     costoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

    }






    @FXML
    private void actualizarTablaTratamientos() throws SQLException {
        // Recarga la lista de tratamientos desde la base de datos
        tratamientos.clear();
        Connection conn = Conexion.getConnection();
        String sql = "SELECT * FROM tablaDeTratamientos";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int codigoTratamiento = rs.getInt("codigoTratamiento");
                String tipoServicio = rs.getString("tipoServicio");
                int costo = rs.getInt("costo");
                Tratamiento tratamiento = new Tratamiento(codigoTratamiento, tipoServicio, costo);
                tratamientos.add(tratamiento);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Actualiza la tabla con la lista de tratamientos
        tratamientoTableView.setItems(tratamientos);
    }
    private boolean eliminarTratamientoDeBaseDeDatos(int codigoTratamiento) throws SQLException {
        // Establece la conexión a la base de datos
        Connection conn = Conexion.getConnection();

        try {
            // Define la sentencia SQL para eliminar el tratamiento por su código
            String sql = "DELETE FROM tablaDeTratamientos WHERE codigoTratamiento = ?";

            // Crea una PreparedStatement
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, codigoTratamiento); // Establece el valor del parámetro

            // Ejecuta la sentencia SQL para eliminar el tratamiento
            int filasAfectadas = stmt.executeUpdate();

            // Verifica si la eliminación fue exitosa
            if (filasAfectadas > 0) {
                // La eliminación fue exitosa, cierra la conexión
                stmt.close();
                conn.close();
                return true;
            } else {
                // No se encontró ningún tratamiento con el código proporcionado
                // Cierra la conexión
                stmt.close();
                conn.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Hubo un error al ejecutar la eliminación
        }
    }
    @FXML
    private void eliminarTratamiento() throws SQLException {
        Tratamiento tratamientoSeleccionado = tratamientoTableView.getSelectionModel().getSelectedItem();
        if (tratamientoSeleccionado != null) {
            // Eliminar de la base de datos
            if (eliminarTratamientoDeBaseDeDatos(tratamientoSeleccionado.getCodigoTratamiento())) {
                // Si la eliminación en la base de datos fue exitosa, entonces procede a eliminar de la lista
                tratamientos.remove(tratamientoSeleccionado);
                // Luego, actualiza la tabla
                tratamientoTableView.refresh();
            }
        }

    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        // Tu código para limpiar los campos de entrada, por ejemplo:
        tipoServicioTextField.clear();
        costoTextField.clear();
    }

    private void actualizarTratamientoEnBaseDeDatos(Tratamiento tratamiento) {
        try {
            Connection conn = Conexion.getConnection();
            String sql = "UPDATE tablaDeTratamientos SET tipoServicio = ?, costo = ? WHERE codigoTratamiento = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tratamiento.getTipoServicio());
            stmt.setInt(2, tratamiento.getCosto());
            stmt.setInt(3, tratamiento.getCodigoTratamiento());
            int filasActualizadas = stmt.executeUpdate();
            stmt.close();
            conn.close();
            if (filasActualizadas > 0) {
                // Actualización exitosa en la base de datos
            } else {
                // Maneja el caso en el que la actualización no tuvo éxito
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Maneja errores de la base de datos
        }
    }
    @FXML
    private void editarButtonClicked() {
        tratamientoTableView.edit(tratamientoTableView.getSelectionModel().getSelectedIndex(), tipoServicioColumn);

    }





    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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


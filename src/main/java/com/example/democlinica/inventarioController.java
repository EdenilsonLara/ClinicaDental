package com.example.democlinica;

import com.example.democlinica.BaseDatos.Conexion;
import javafx.application.Platform;
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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.democlinica.ItemInventario;

public class inventarioController {

    @FXML
    private TextField nombreEquipoTextField;
    @FXML
    private TextField descripcionTextField;
    @FXML
    private TextField precioTextField;
    @FXML
    private TextField cantidadTextField;
    @FXML
    private TextField codigoProveedorTextField;

    @FXML
    private TableView<ItemInventario> inventarioTableView;
    @FXML
    private TableColumn<ItemInventario, Integer> codigoInventarioColumn;
    @FXML
    private TableColumn<ItemInventario, String> nombreEquipoColumn;
    @FXML
    private TableColumn<ItemInventario, String> descripcionColumn;
    @FXML
    private TableColumn<ItemInventario, Double> precioColumn;
    @FXML
    private TableColumn<ItemInventario, Integer> cantidadColumn;
    @FXML
    private TableColumn<ItemInventario, Integer> codigoProveedorColumn;
    private int codigoProveedor;

    private ObservableList<ItemInventario> inventario = FXCollections.observableArrayList();



    @FXML
    private void guardarInventario(ActionEvent event) throws SQLException {
        String nombreEquipo = nombreEquipoTextField.getText();
        String descripcion = descripcionTextField.getText();
        double precio = Double.parseDouble(precioTextField.getText());
        int cantidad = Integer.parseInt(cantidadTextField.getText());
        int codigoProveedor = Integer.parseInt(codigoProveedorTextField.getText());


        Connection conn = null;

        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO tablaDeInventario (nombreEquipo, descripcion, precio, cantidad, codigoProveedor) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreEquipo);
            pstmt.setString(2, descripcion);
            pstmt.setDouble(3, precio);
            pstmt.setInt(4, cantidad);
            pstmt.setInt(5, codigoProveedor);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Inventario Guardado", "El artículo se ha guardado correctamente en la base de datos.");
            } else {
                mostrarAlerta("Error", "No se pudo guardar el artículo en la base de datos.");
            }

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // Actualiza la tabla de inventario.
            actualizarTablaInventario();
        }

        limpiarCampos();
    }

    @FXML
    private void initialize() {
        // Configura las columnas para mostrar los datos
        codigoInventarioColumn.setCellValueFactory(new PropertyValueFactory<>("codigoInventario"));
        nombreEquipoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        codigoProveedorColumn.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));

        // Habilita la edición en las columnas necesarias (por ejemplo, nombreEquipo, descripcion, precio, cantidad, etc.)
        nombreEquipoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreEquipoColumn.setOnEditCommit(event -> {
            ItemInventario item = event.getRowValue();
            item.setNombreEquipo(event.getNewValue());
            actualizarItemEnBaseDeDatos(item);
        });

        descripcionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descripcionColumn.setOnEditCommit(event -> {
            ItemInventario item = event.getRowValue();
            item.setDescripcion(event.getNewValue());
            actualizarItemEnBaseDeDatos(item);
        });

        precioColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        precioColumn.setOnEditCommit(event -> {
            ItemInventario item = event.getRowValue();
            item.setPrecio(event.getNewValue());
            actualizarItemEnBaseDeDatos(item);
        });

        cantidadColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        cantidadColumn.setOnEditCommit(event -> {
            ItemInventario item = event.getRowValue();
            item.setCantidad(event.getNewValue());
            actualizarItemEnBaseDeDatos(item);
        });

        // Configura la TableView para permitir la edición
        inventarioTableView.setEditable(true);

        // Llama a la función para cargar los artículos de inventario desde la base de datos
        try {
            actualizarTablaInventario();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void editarInventario(ActionEvent event) {
        // Obtener el artículo de inventario seleccionado
        ItemInventario itemSeleccionado = inventarioTableView.getSelectionModel().getSelectedItem();

        if (itemSeleccionado != null) {
            // Obtener los nuevos datos del artículo de inventario
            String nuevoNombreEquipo = nombreEquipoTextField.getText();
            String nuevaDescripcion = descripcionTextField.getText();
            double nuevoPrecio = Double.parseDouble(precioTextField.getText());
            int nuevaCantidad = Integer.parseInt(cantidadTextField.getText());
            int nuevoCodigoProveedor = Integer.parseInt(codigoProveedorTextField.getText());

            // Realiza la validación de campos, si es necesario

            try {
                Connection conn = Conexion.getConnection();
                String sql = "UPDATE tablaDeInventario SET nombreEquipo = ?, descripcion = ?, precio = ?, cantidad = ?, codigoProveedor = ? WHERE codigoInventario = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nuevoNombreEquipo);
                pstmt.setString(2, nuevaDescripcion);
                pstmt.setDouble(3, nuevoPrecio);
                pstmt.setInt(4, nuevaCantidad);
                pstmt.setInt(5, nuevoCodigoProveedor);
                pstmt.setInt(6, itemSeleccionado.getCodigoInventario());

                int filasActualizadas = pstmt.executeUpdate();

                if (filasActualizadas > 0) {
                    mostrarAlerta("Inventario Actualizado", "Los datos del artículo de inventario se han actualizado correctamente en la base de datos.");
                    actualizarTablaInventario();
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar el artículo de inventario en la base de datos.");
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
    private void eliminarInventario(ActionEvent event) {
        // Obtener el artículo de inventario seleccionado
        ItemInventario itemSeleccionado = inventarioTableView.getSelectionModel().getSelectedItem();

        if (itemSeleccionado != null) {
            try {
                Connection conn = Conexion.getConnection();
                String sql = "DELETE FROM tablaDeInventario WHERE codigoInventario = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, itemSeleccionado.getCodigoInventario());

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlerta("Artículo de Inventario Eliminado", "El artículo de inventario se ha eliminado correctamente de la base de datos.");
                    actualizarTablaInventario();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el artículo de inventario de la base de datos.");
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
    private void limpiarCampos() {
        nombreEquipoTextField.clear();
        descripcionTextField.clear();
        precioTextField.clear();
        cantidadTextField.clear();
        codigoProveedorTextField.clear();
    }


    private void actualizarTablaInventario() throws SQLException {
        // Limpiar la lista de artículos de inventario
        inventario.clear();
        Connection conn = Conexion.getConnection();
        String sql = "SELECT * FROM tablaDeInventario";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int codigoInventario = rs.getInt("codigoInventario");
                String nombreEquipo = rs.getString("nombreEquipo");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                int codigoProveedor = rs.getInt("codigoProveedor");
                ItemInventario item = new ItemInventario(codigoInventario, nombreEquipo, descripcion, precio, cantidad, codigoProveedor);
                inventario.add(item);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Actualizar la tabla con los datos del inventario
        inventarioTableView.setItems(inventario);
    }

    private void actualizarItemEnBaseDeDatos(ItemInventario item) {
        try {
            Connection conn = Conexion.getConnection();
            String sql = "UPDATE tablaDeInventario SET nombreEquipo = ?, descripcion = ?, precio = ?, cantidad = ?, codigoProveedor = ? WHERE codigoInventario = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getNombreEquipo());
            pstmt.setString(2, item.getDescripcion());
            pstmt.setDouble(3, item.getPrecio());
            pstmt.setInt(4, item.getCantidad());
            pstmt.setInt(5, item.getCodigoProveedor());
            pstmt.setInt(6, item.getCodigoInventario());

            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                mostrarAlerta("Item de Inventario Actualizado", "Los datos del artículo de inventario se han actualizado correctamente en la base de datos.");
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el artículo de inventario en la base de datos.");
            }

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "Hubo un error al conectar con la base de datos.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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

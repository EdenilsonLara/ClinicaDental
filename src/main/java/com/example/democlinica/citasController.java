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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;



import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;


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
    private TableColumn<Cita, Integer> codigoCitaColumn;
    @FXML
    private TableColumn<Cita, String> nombresPacienteColumn;
    @FXML
    private TableColumn<Cita, String> apellidosPacienteColumn;
    @FXML
    private TableColumn<Cita, String> motivoConsultaColumn;
    @FXML
    private TableColumn<Cita, Integer> codigoTratamientoColumn;
    @FXML
    private TableColumn<Cita, String> costoColumn;
    @FXML
    private TableColumn<Cita, LocalDate> fechaColumn;
    @FXML
    private TableColumn<Cita, String> estadoColumn;
    @FXML
    private TableColumn<Cita, Integer> codigoPacienteColumn;
    @FXML
    private TableView<Cita> citasTableView;
    @FXML
    private TextField codigoCitaTextField;



    private ObservableList<Cita> citas = FXCollections.observableArrayList();






    @FXML
    private void guardarCita(ActionEvent event) throws SQLException {
        String nombres = nombresTextField.getText();
        String apellidos = apellidosTextField.getText();
        String motivo = motivoTextField.getText();
        int codigoTratamiento = Integer.parseInt(codigoTratamientoTextField.getText());
        double costo = Double.parseDouble(costoTextField.getText());
        String estado = estadoComboBox.getValue();
        int codigoPaciente = Integer.parseInt(codigoPacienteTextField.getText());

        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO tablaDeCitas (nombresPaciente, apellidosPaciente, motivoConsulta, codigoTratamiento, costo, estado, codigoPaciente) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombres);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, motivo);
            pstmt.setInt(4, codigoTratamiento);
            pstmt.setDouble(5, costo);
            pstmt.setString(6, estado);
            pstmt.setInt(7, codigoPaciente);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta("Cita Guardada", "La cita se ha guardado correctamente en la base de datos.");
                actualizarTablaCitas(); // Asegúrate de tener un método para actualizar la tabla de citas
            } else {
                mostrarAlerta("Error", "No se pudo guardar la cita en la base de datos.");
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
        }

        limpiarCampos();
    }



    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        nombresTextField.clear();
        apellidosTextField.clear();
        motivoTextField.clear();
        codigoTratamientoTextField.clear();
        costoTextField.clear();
        estadoComboBox.getSelectionModel().clearSelection();
        codigoPacienteTextField.clear();
    }

    @FXML
    private void initialize() {
        // Configura las columnas para mostrar los datos
        codigoCitaColumn.setCellValueFactory(new PropertyValueFactory<>("codigoCita"));
        nombresPacienteColumn.setCellValueFactory(new PropertyValueFactory<>("nombresPaciente"));
        apellidosPacienteColumn.setCellValueFactory(new PropertyValueFactory<>("apellidosPaciente"));
        motivoConsultaColumn.setCellValueFactory(new PropertyValueFactory<>("motivoConsulta"));
        codigoTratamientoColumn.setCellValueFactory(new PropertyValueFactory<>("codigoTratamiento"));
        costoColumn.setCellValueFactory(new PropertyValueFactory<>("costo"));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        codigoPacienteColumn.setCellValueFactory(new PropertyValueFactory<>("codigoPaciente"));
        codigoCitaTextField = new TextField();

        // Habilita la edición en las columnas necesarias (por ejemplo, nombresPaciente, apellidosPaciente, etc.)
        nombresPacienteColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nombresPacienteColumn.setOnEditCommit(event -> {
            Cita cita = event.getRowValue();
            cita.setNombresPaciente(event.getNewValue());
            try {
                actualizarCitaEnBaseDeDatos(cita);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        apellidosPacienteColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        apellidosPacienteColumn.setOnEditCommit(event -> {
            Cita cita = event.getRowValue();
            cita.setApellidosPaciente(event.getNewValue());
            try {
                actualizarCitaEnBaseDeDatos(cita);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        motivoConsultaColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        motivoConsultaColumn.setOnEditCommit(event -> {
            Cita cita = event.getRowValue();
            cita.setMotivoConsulta(event.getNewValue());
            try {
                actualizarCitaEnBaseDeDatos(cita);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        // Configura la TableView para permitir la edición
        citasTableView.setEditable(true);

        // Llama a la función para cargar las citas desde la base de datos
        try {
            actualizarTablaCitas();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    @FXML
    private void editarCita(ActionEvent event) {
        String codigoCitaText = codigoCitaTextField.getText();
        String nombres = nombresTextField.getText();
        String apellidos = apellidosTextField.getText();
        String motivo = motivoTextField.getText();
        String codigoTratamientoText = codigoTratamientoTextField.getText();
        String costoText = costoTextField.getText();
        String estado = estadoComboBox.getValue();
        String codigoPacienteText = codigoPacienteTextField.getText();

        if (codigoCitaText.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || motivo.isEmpty() ||
                codigoTratamientoText.isEmpty() || costoText.isEmpty() || estado == null || codigoPacienteText.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos obligatorios.");
            return;
        }

        int codigoCita = 0;
        int codigoTratamiento = 0;
        int codigoPaciente = 0;
        double costo = 0.0;

        try {
            codigoCita = Integer.parseInt(codigoCitaText);
            codigoTratamiento = Integer.parseInt(codigoTratamientoText);
            codigoPaciente = Integer.parseInt(codigoPacienteText);
            costo = Double.parseDouble(costoText);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Entrada", "Los campos de código de cita, código de tratamiento, costo y código de paciente deben ser números válidos.");
            return;
        }

        // Verificar si la cita existe antes de intentar actualizar
        if (!citaExiste(codigoCita)) {
            mostrarAlerta("Cita No Encontrada", "No se encontró una cita con el código especificado.");
            return;
        }

        // Crear una conexión a la base de datos y realizar la actualización
        try (Connection connection = Conexion.getConnection()) {
            String sql = "UPDATE tablaDeCitas SET nombresPaciente = ?, apellidosPaciente = ?, motivoConsulta = ?, " +
                    "codigoTratamiento = ?, costo = ?, estado = ?, codigoPaciente = ? WHERE codigoCita = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nombres);
                statement.setString(2, apellidos);
                statement.setString(3, motivo);
                statement.setInt(4, codigoTratamiento);
                statement.setDouble(5, costo);
                statement.setString(6, estado);
                statement.setInt(7, codigoPaciente);
                statement.setInt(8, codigoCita);

                int filasAfectadas = statement.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlerta("Cita Actualizada", "La cita se ha actualizado con éxito.");
                    limpiarCampos();
                } else {
                    mostrarAlerta("Error al Actualizar", "Hubo un error al actualizar la cita.");
                }
            }
        } catch (SQLException e) {
            mostrarAlerta("Error de Base de Datos", "Error al conectarse a la base de datos: " + e.getMessage());
        }
    }

    private boolean citaExiste(int codigoCita) {
        // Verificar si una cita con el código especificado existe en la base de datos
        try (Connection connection = Conexion.getConnection()) {
            String sql = "SELECT codigoCita FROM tablaDeCitas WHERE codigoCita = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, codigoCita);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            mostrarAlerta("Error de Base de Datos", "Error al verificar la existencia de la cita: " + e.getMessage());
        }

        return false;
    }

    @FXML
    private void eliminarCita(ActionEvent event) {
        // Obtener la fila seleccionada
        Cita citaSeleccionada = citasTableView.getSelectionModel().getSelectedItem();

        if (citaSeleccionada == null) {
            mostrarAlerta("Selección de Cita", "Por favor, seleccione una cita para eliminar.");
            return;
        }

        // Mostrar un cuadro de diálogo de confirmación antes de eliminar la cita
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación de Eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar esta cita?");
        confirmacion.setContentText("Esta acción no se puede deshacer. Seleccione Aceptar para confirmar o Cancelar para mantener la cita.");

        Optional<ButtonType> result = confirmacion.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Eliminar la cita de la base de datos
            eliminarCitaDeBaseDeDatos(citaSeleccionada.getCodigoCita());

            // Eliminar la cita seleccionada de la lista de citas
            citas.remove(citaSeleccionada);

            // Actualizar la tabla de la vista
            citasTableView.setItems(citas);
        }
    }


    private void eliminarCitaDeBaseDeDatos(int codigoCita) {
        // Eliminar la cita de la base de datos
        try (Connection connection = Conexion.getConnection()) {
            String sql = "DELETE FROM tablaDeCitas WHERE codigoCita = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, codigoCita);
                int filasAfectadas = statement.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlerta("Cita Eliminada", "La cita se ha eliminado con éxito.");
                    limpiarCampos();
                } else {
                    mostrarAlerta("Error al Eliminar", "Hubo un error al eliminar la cita.");
                }
            }
        } catch (SQLException e) {
            mostrarAlerta("Error de Base de Datos", "Error al conectarse a la base de datos: " + e.getMessage());
        }
    }
    private void actualizarTablaCitas() throws SQLException {
        citas.clear();
        Connection conn = Conexion.getConnection();
        String sql = "SELECT * FROM tablaDeCitas";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int codigoCita = rs.getInt("codigoCita");
                String nombresPaciente = rs.getString("nombresPaciente");
                String apellidosPaciente = rs.getString("apellidosPaciente");
                String motivoConsulta = rs.getString("motivoConsulta");
                int codigoTratamiento = rs.getInt("codigoTratamiento");
                double costo = rs.getDouble("costo");
                Timestamp timestamp = rs.getTimestamp("fecha");
                LocalDateTime fecha = null;

                if (timestamp != null) {
                    fecha = timestamp.toLocalDateTime();
                }

                String estado = rs.getString("estado");
                int codigoPaciente = rs.getInt("codigoPaciente");
                int codigoSucursal = rs.getInt("codigoSucursal");

                // Crea un objeto de cita y agrégalo a la lista
                Cita cita = new Cita(codigoCita, nombresPaciente, apellidosPaciente, motivoConsulta, codigoTratamiento, costo, fecha, estado, codigoPaciente, codigoSucursal);
                citas.add(cita);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        citasTableView.setItems(citas);
    }
    private void actualizarCitaEnBaseDeDatos(Cita cita) throws SQLException {
        Connection conn = Conexion.getConnection();

        if (conn != null) {
            try {
                String sql = "UPDATE tablaDeCitas SET " +
                        "nombresPaciente = ?, " +
                        "apellidosPaciente = ?, " +
                        "motivoConsulta = ?, " +
                        "codigoTratamiento = ?, " +
                        "costo = ?, " +
                        "fecha = ?, " +
                        "estado = ?, " +
                        "codigoPaciente = ? " +
                        "WHERE codigoCita = ?";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, cita.getNombresPaciente());
                pstmt.setString(2, cita.getApellidosPaciente());
                pstmt.setString(3, cita.getMotivoConsulta());
                pstmt.setInt(4, cita.getCodigoTratamiento());
                pstmt.setDouble(5, cita.getCosto());

                // Verifica si la fecha de la cita no es nula antes de convertirla a Timestamp
                if (cita.getFecha() != null) {
                    pstmt.setTimestamp(6, Timestamp.valueOf(cita.getFecha()));
                } else {
                    pstmt.setNull(6, Types.TIMESTAMP);
                }

                pstmt.setString(7, cita.getEstado());
                pstmt.setInt(8, cita.getCodigoPaciente());
                pstmt.setInt(9, cita.getCodigoCita());

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("La cita se actualizó en la base de datos.");
                } else {
                    System.out.println("No se pudo actualizar la cita en la base de datos.");
                }

                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

package controle.telas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entidades.sistema.Produtos;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;

public class ProdutosControl {

	private static final String URL = "jdbc:mariadb://localhost:3306/loja?allowMultiQueries=true";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	private LongProperty id = new SimpleLongProperty(0);
	private StringProperty nome = new SimpleStringProperty("");
	private StringProperty marca = new SimpleStringProperty("");
	private DoubleProperty preco = new SimpleDoubleProperty(0);
	private StringProperty tipo = new SimpleStringProperty("");

	private ObservableList<Produtos> listaDeProdutos = FXCollections.observableArrayList();

	private TableView<Produtos> table = new TableView<>();

	public void adicionar() {
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
			String sql = String.format("INSERT INTO produtos(nome, marca, preco, tipo) VALUES (?, ?, ?, ?)");
			System.out.println("SQL => " + sql);
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, nome.get());
			stmt.setString(2, marca.get());
			stmt.setDouble(3, preco.get());
			stmt.setString(4, tipo.get());
			int i = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void pesquisarPorTipo() {
		listaDeProdutos.clear();
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
			String sql = "SELECT * FROM produtos WHERE tipo LIKE ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, "%" + tipo.get() + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Produtos p = new Produtos();
				p.setId(rs.getLong("id"));
				p.setNome(rs.getString("nome"));
				p.setMarca(rs.getString("marca"));
				p.setPreco(rs.getDouble("preco"));
				p.setTipo(rs.getString("tipo"));
				listaDeProdutos.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setEntity(Produtos p) {
		if (p != null) {
			id.set(p.getId());
			nome.set(p.getNome());
			marca.set(p.getMarca());
			preco.set(p.getPreco());
			tipo.set(p.getTipo());
		}
	}

	public Produtos getEntity() {
		Produtos p = new Produtos();
		p.setId(id.get());
		p.setNome(nome.get());
		p.setPreco(preco.get());
		p.setTipo(tipo.get());
		return p;
	}

	public void generatedTable() {
		TableColumn<Produtos, Long> colId = new TableColumn<>("ID");
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Produtos, String> colNome = new TableColumn<>("Nome");
		colNome.setCellValueFactory(new PropertyValueFactory<Produtos, String>("nome"));

		TableColumn<Produtos, String> colMarca = new TableColumn<>("Marca");
		colMarca.setCellValueFactory(new PropertyValueFactory<Produtos, String>("marca"));

		TableColumn<Produtos, String> colPreco = new TableColumn<>("Preço");
		colPreco.setCellValueFactory(new PropertyValueFactory<Produtos, String>("preco"));

		TableColumn<Produtos, String> colTipo = new TableColumn<>("Tipo");
		colTipo.setCellValueFactory(new PropertyValueFactory<Produtos, String>("tipo"));

		TableColumn<Produtos, String> colOperacao = new TableColumn<>("Operações");
		Callback<TableColumn<Produtos, String>, TableCell<Produtos, String>> cellFactory = new Callback<TableColumn<Produtos, String>, TableCell<Produtos, String>>() {
			public TableCell<Produtos, String> call(final TableColumn<Produtos, String> coluna) {
				return new TableCell<Produtos, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							FlowPane fp = new FlowPane();
							final Button btnApagar = new Button("Apagar");
							final Button btnEditar = new Button("Editar");
							fp.getChildren().addAll(btnApagar, btnEditar);
							btnApagar.setOnAction((e) -> {
								listaDeProdutos.remove(getIndex());
								removerDoBanco();
							});
							setGraphic(fp);
							setText(null);
						}
					}
				};
			}
		};

		colOperacao.setCellFactory(cellFactory);

		table.getColumns().addAll(colId, colNome, colMarca, colPreco, colTipo, colOperacao);

		table.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
			setEntity(novo);
		});

		table.setItems(listaDeProdutos);
	}

	public LongProperty getId() {
		return id;
	}

	public StringProperty getNome() {
		return nome;
	}

	public StringProperty getMarca() {
		return marca;
	}

	public DoubleProperty getPreco() {
		return preco;
	}

	public StringProperty getTipo() {
		return tipo;
	}

	public void setId(LongProperty id) {
		this.id = id;
	}

	public void setNome(StringProperty nome) {
		this.nome = nome;
	}

	public void setMarca(StringProperty marca) {
		this.marca = marca;
	}

	public void setPreco(DoubleProperty preco) {
		this.preco = preco;
	}

	public void setTipo(StringProperty tipo) {
		this.tipo = tipo;
	}
	
	public TableView<Produtos> getTable() {
        return table;
    }

	public void removerDoBanco() {
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)){
			Produtos prod = table.getSelectionModel().getSelectedItem();
			if (prod != null) {
				String sql = "DELETE FROM produtos WHERE id = ?";
				PreparedStatement stmt = con.prepareStatement(sql);
				stmt.setLong(1, prod.getId());
				ResultSet rs = stmt.executeQuery();
				rs.deleteRow();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

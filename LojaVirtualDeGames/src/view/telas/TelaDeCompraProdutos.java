package view.telas;

import controle.telas.ProdutosControl;
import entidades.sistema.Produtos;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;

public class TelaDeCompraProdutos extends Application implements EventHandler<ActionEvent>{
	
	private ProdutosControl control = new ProdutosControl();
	
	private TextField txtId = new TextField();
	private TextField txtNomeProdutos = new TextField();
	private TextField txtMarcaProdutos = new TextField();
	private TextField txtProdutosPreco = new TextField();
	private TextField txtTipoProdutos = new TextField();
	
	private Button btnAdicionar = new Button("Adicionar");
	private Button btnPesquisar = new Button("Pesquisar");

	@Override
	public void start(Stage stage) throws Exception {
		GridPane painel = new GridPane();
		BorderPane painelBorder = new BorderPane();
		Scene scn = new Scene(painelBorder, 700, 400);
		
		Label lblProdutos = new Label("Produto(s)");
		Label lblQtd = new Label("Quantidade");
		
		Label lblProdutosNome = new Label("Nome do Produto");
		Label lblProdutosMarca = new Label("Marca do Produto");
		Label lblProdutosPreco = new Label("Pre�o");
		Label lblProdutosTipo = new Label("Tipo do Produto");
		
		painel.add(lblProdutos, 0, 0);
		painel.add(lblQtd, 1, 0);
		painel.add(lblProdutosNome, 0, 1);
		painel.add(txtNomeProdutos, 1, 1);
		painel.add(lblProdutosMarca, 0, 2);
		painel.add(txtMarcaProdutos, 1, 2);
		painel.add(lblProdutosPreco, 0, 3);
		painel.add(txtProdutosPreco, 1, 3);
		painel.add(lblProdutosTipo, 0, 4);
		painel.add(txtTipoProdutos, 1, 4);
		
		control.generatedTable();
		painelBorder.setTop(painel);
		painelBorder.setCenter(control.getTable());
		
		btnAdicionar.setOnAction((e)->{
			control.adicionar();
		});
		
		btnPesquisar.setOnAction((e)->{
			control.pesquisarPorTipo();
		});
		
		painel.add(btnAdicionar, 0, 5);
		painel.add(btnPesquisar, 1, 5);
		
		StringConverter longToStringConverter = new LongStringConverter();
        StringConverter doubleToStringConverter = new DoubleStringConverter();

		Bindings.bindBidirectional(txtId.textProperty(), control.getId(), longToStringConverter);
        Bindings.bindBidirectional(txtNomeProdutos.textProperty(), control.getNome());
        Bindings.bindBidirectional(txtMarcaProdutos.textProperty(), control.getMarca());
        Bindings.bindBidirectional(txtProdutosPreco.textProperty(), control.getPreco(), doubleToStringConverter);
        Bindings.bindBidirectional(txtTipoProdutos.textProperty(), control.getTipo());
		
		stage.setScene(scn);
		stage.setTitle("RECTUM COPIA");
		stage.show();
	}

	@Override
	public void handle(ActionEvent event) {}
	
	public static void main(String[] args) {
		Application.launch(TelaDeCompraProdutos.class, args);
	}
	
}

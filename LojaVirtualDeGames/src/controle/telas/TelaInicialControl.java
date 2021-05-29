package controle.telas;

import java.util.ArrayList;
import java.util.List;

import entidades.sistema.Roteador;

public class TelaInicialControl {
	
	private List<Roteador> listaDeRoteadores = new ArrayList<>();
	
	public void adicionar(Roteador r) {
		listaDeRoteadores.add(r);
	}

	public Roteador pesquisarPorNome(String nome) {
		for(Roteador r : listaDeRoteadores) {
			if(r.getNome().contains(nome)) {
				return r;
			}
		}
		return null;
	}
	
	

}

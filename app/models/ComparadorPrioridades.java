package models;


import java.util.Comparator;


public class ComparadorPrioridades implements Comparator<Meta>{
	private GerenciadorMetas gerenciador = new GerenciadorMetas();

	@Override
	public int compare(Meta meta1, Meta meta2) {
		return gerenciador.ordenaPorPrioridade(meta1, meta2);
	}
	


	
}

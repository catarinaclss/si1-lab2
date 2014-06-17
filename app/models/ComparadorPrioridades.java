package models;

import java.text.ParseException;
import java.util.Comparator;


public class ComparadorPrioridades implements Comparator<Meta>{
		
	private GerenciadorMetas gerenciador = new GerenciadorMetas();

	@Override
	public int compare(Meta meta1, Meta meta2) {
		int result = 0;
		try {
			if (gerenciador.ordenaPorData(meta1, meta2) == 1 && gerenciador.ordenaPorPrioridade(meta1, meta2) == 1) {
				result = 1;
			} else if (gerenciador.ordenaPorData(meta1, meta2) == -1 && gerenciador.ordenaPorPrioridade(meta1, meta2) == -1) {
				result = -1;
			} else {
				result = 0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
}

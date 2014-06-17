package models;


import java.util.Comparator;


public class ComparadorPrioridades implements Comparator<Meta>{
		

	@Override
	public int compare(Meta meta1, Meta meta2) {
		if(meta1.getPrioridade() < meta2.getPrioridade()){
			return -1;
		}else if (meta1.getPrioridade() > meta2.getPrioridade()){
			return 1;
		}
		return 0;
	}
	
	
	
}

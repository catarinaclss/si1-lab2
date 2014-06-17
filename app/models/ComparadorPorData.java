package models;

import java.util.Comparator;

public class ComparadorPorData implements Comparator<Meta>{
	

	@Override
	public int compare(Meta meta1, Meta meta2) {

		if (meta1.getDataFinal().compareTo(meta2.getDataFinal()) > 0){
			return 1;
		}else if(meta1.getDataFinal().compareTo(meta2.getDataFinal()) < 0){
			return -1;
		}else{
			return 0;
		}
	
	}
}	
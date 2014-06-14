package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import model.Meta;


public class GerenciadorMetas {
	
	private List<Meta> listMetas;
	private final int MAX_DIAS = 42;
	
	public GerenciadorMetas(){
		listMetas = new ArrayList<Meta>();
	}

	public boolean isEmpty(){
		return listMetas.isEmpty();
	}
	
	public void addMeta(Meta meta) throws ParseException, MetaInvalidaException{
		if(podeInserir(meta) == true){
			listMetas.add(meta);
		}else{
			throw new MetaInvalidaException("Data Invalida");
		}
	
	}
	
	public int getSemana(Meta meta) throws ParseException{
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
		Date data_final = format.parse(meta.getDataFinal());
		Calendar data = new GregorianCalendar();
		data.setTime(data_final);
		return data.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static String getDataAtual(){
		
		Calendar cal = Calendar.getInstance(); 
		String day = String.valueOf((cal.get(Calendar.DAY_OF_MONTH)));
		String month = String.valueOf(cal.get(Calendar.MONTH)+1);
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String currentDate = day  + "/" + month + "/" + year; 
		return currentDate;
	}

	
	@SuppressWarnings("deprecation")
	public boolean podeInserir(Meta meta1) throws ParseException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");  
		Date dataMinima = df.parse(getDataAtual());
		
		Date dataMaxima =  df.parse(getDataAtual());
		dataMaxima.setDate(dataMaxima.getDate() + MAX_DIAS); 
		Date dataFinal = df.parse(meta1.getDataFinal());  
		
		if(dataMaxima.compareTo(dataFinal) >= 0 && dataMinima.compareTo(dataFinal) <= 0){
			return true;
		}
		return false;
	}
	
	public List<Meta> getListMetas(){
		return listMetas;
	}

	public int ordenaPorData(Meta meta1, Meta meta2) throws ParseException {
		int result;
		if (meta1.getDataFinal().compareTo(meta2.getDataFinal()) > 0){
			result = 1;
		}else if(meta1.getDataFinal().compareTo(meta2.getDataFinal()) < 0){
			result = -1;
		}else{
			result = 0;
		}
		return result;
		
	}
	
	public int ordenaPorPrioridade(Meta meta1, Meta meta2) {
		int result;
		if (meta1.getPrioridade() > meta2.getPrioridade()){
			result = -1;
		}else if(meta1.getPrioridade() < meta2.getPrioridade()){
			result = 1;
		}else{
			result = 0;
		}
		return result;
		
	}
	
	
	public class ComparatorPrioridade implements Comparator<Meta>{
		

		@Override
		public int compare(Meta meta1, Meta meta2) {
			int result = 0;
			try {
				if (ordenaPorData(meta1, meta2) == 1 && ordenaPorPrioridade(meta1, meta2) == 1){
					result = 1;
				}else if (ordenaPorData(meta1, meta2) == -1 && ordenaPorPrioridade(meta1, meta2) == -1){
					result = -1;
				}else{
					result = 0;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return result;
		}
	
	}
	
}

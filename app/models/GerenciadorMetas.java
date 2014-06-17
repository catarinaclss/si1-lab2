package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;


public class GerenciadorMetas {
	
	private List<Meta> listaMetas;
	private final int MAX_DIAS = 42;
	private GenericDAO dao = new GenericDAOImpl(); 
	
	public GerenciadorMetas(){
		listaMetas = new ArrayList<Meta>();
	}

	public boolean isEmpty(){
		return listaMetas.isEmpty();
	}
	
	public void adicionaMeta(Meta meta) throws ParseException, MetaInvalidaException{
		if(podeInserir(meta) == true){
			getListMetas().add(meta);
			getDao().merge(getListMetas());
		}else{
			throw new MetaInvalidaException("Data Invalida");
		}
	
	}
	
	public int getSemana(Meta meta) throws ParseException{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
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
		String currentDate = year  + "-" + month + "-" + day ; 
		return currentDate;
	}

	
	@SuppressWarnings("deprecation")
	public boolean podeInserir(Meta meta1) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
		Date dataMinima = df.parse(getDataAtual());
		
		Date dataMaxima =  df.parse(getDataAtual());
		dataMaxima.setDate(dataMaxima.getDate() + MAX_DIAS); 
		Date dataFinal = df.parse(meta1.getDataFinal());  
		
		if(!meta1.equals(null) && dataMaxima.compareTo(dataFinal) >= 0 && dataMinima.compareTo(dataFinal) <= 0){
			return true;
		}
		return false;
	}
	
	public List<Meta> getListMetas(){
        carregaListaMetas();
        return listaMetas;
	}

	public void carregaListaMetas() {
        if (listaMetas.isEmpty()){
			listaMetas = new ArrayList<Meta>();
            listaMetas = getDao().findAllByClassName("Meta");
        }else{
			listaMetas = listaMetas;
        }
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
	
	
	public GenericDAO getDao() {
		return dao;
	}


	
}

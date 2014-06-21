import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import models.ComparadorPorData;
import models.ComparadorPrioridade;
import models.GerenciadorMetas;
import models.Meta;
import models.MetaInvalidaException;

import org.junit.Before;
import org.junit.Test;


public class TargAppTest {
	
	private GerenciadorMetas gerenciador;
	private Meta meta1, meta2, meta3;
	
	@Before
	public void setup() {
		gerenciador = new GerenciadorMetas();
		meta1 = new Meta();
		meta2 = new Meta();
		meta3 = new Meta();
	}

	@Test
	public void iniciaSemMetas() {
		assertTrue(gerenciador.isEmpty());
	}

	@Test
	public void devePossuirLimiteParaInsercao() throws ParseException{
		
		meta1.setDescricao("descricao");
		meta1.setDataFinal("2014-08-31");
		
		try{
			gerenciador.adicionaMeta(meta1);
		}catch (Exception e){
			assertEquals(e.getMessage(), "Data Invalida");
		}
		
		assertFalse(gerenciador.podeInserir(meta1));
		assertFalse(gerenciador.getListMetas().contains(meta1));
		
		meta1.setDataFinal("2014-07-18");
		assertTrue(gerenciador.podeInserir(meta1));
		
	}
	
	@Test
	public void deveOrdenarPorData() throws ParseException{
		
		meta1.setDataFinal("2014-06-18");
		meta2.setDataFinal("2014-06-15");
		meta3.setDataFinal("2014-06-13");
		
		assertTrue(new ComparadorPorData().compare(meta1, meta2) == 1);
		assertTrue(new ComparadorPorData().compare(meta3, meta2) == -1);
	}
	
	@Test
	public void deveOrdenarPorPrioridade() throws ParseException{
		
		meta1.setPrioridade(1);
		meta2.setPrioridade(2);
		meta3.setPrioridade(3);
		assertTrue(new ComparadorPrioridade().compare(meta1, meta2) == 1);
		assertTrue(new ComparadorPrioridade().compare(meta3, meta2) == -1);
		

		
	}
	

	
	
	
	
}

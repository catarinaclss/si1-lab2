package controllers;
 
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.ComparadorPorData;
import models.ComparadorPrioridade;
import models.GerenciadorMetas;
import models.Meta;
import models.MetaInvalidaException;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
 
public class Application extends Controller {
 
	private static GerenciadorMetas gerenciador = new GerenciadorMetas();
	private static final Form<Meta> metaForm = Form.form(Meta.class);
	private static int feitas = 0;
	private static int pendentes = 0;
 
	@Transactional
	public static Result listaMetas() throws ParseException, MetaInvalidaException{
		povoaLista();
	
		List<Meta> metas = gerenciador.getDao().findAllByClassName("Meta");
		pendentes = metas.size() - feitas;
		
    	List<Integer> semanas = new ArrayList<>();
    	Collections.sort(semanas);
		
		int aux = 0;
		for (Meta meta : metas) {
			if(aux != gerenciador.getSemana(meta)){
				aux = gerenciador.getSemana(meta);
				semanas.add(aux);		
			}
		}

		Collections.sort(metas, new ComparadorPrioridade());
		Collections.sort(metas, new ComparadorPorData());
		Collections.sort(semanas);
		return ok(index.render(semanas, metas, metaForm, feitas, pendentes));
	}
	
	@Transactional
	public static Result cadastro() {
		List<Meta> metas = gerenciador.getDao().findAllByClassName("Meta");
		Collections.sort(metas, new ComparadorPrioridade());
		Collections.sort(metas, new ComparadorPorData());
		return ok(cadastro.render(metaForm, feitas, pendentes));
	}
 
	@Transactional
	public static Result novaMeta() throws ParseException {
		Form<Meta> filledForm = metaForm.bindFromRequest();
		Meta meta = filledForm.get();
	
		if (filledForm.hasErrors() || !gerenciador.podeInserir(meta)) {
			
			flash("sucess", "Não foi possivel inserir meta. Tente novamente.");
			return badRequest(cadastro.render(filledForm, feitas, pendentes ));
			
		} else {
			gerenciador.getDao().persist(meta);
			gerenciador.getDao().flush();
			return redirect(routes.Application.listaMetas());
		}
	}
 
	@Transactional
 	public static Result deletaMeta(Long id) {
 		if(id!= null){
 			boolean thisMarked = getMeta(id).getStatus();
 			gerenciador.getDao().removeById(Meta.class, id);
 			gerenciador.getDao().flush();
 			if(thisMarked) 
 				setNumMetasPorStatus(0, -1);
 		}
 		return redirect(routes.Application.listaMetas());
 	}
	
	@Transactional
	public static Result iniciarEdicao() {
		DynamicForm requestData = Form.form().bindFromRequest();
		long id = Long.parseLong(requestData.get("ID"));
		Form<Meta> metaForm = Form.form(Meta.class).fill(gerenciador.getDao().findByEntityId(Meta.class, id));
		return ok(editar.render(id, metaForm, feitas, pendentes));
	}
	
	@Transactional
	public static Result editarMeta(Long id) {
		boolean thisMarked = getMeta(id).getStatus();
		Form<Meta> alterarForm = Form.form(Meta.class).bindFromRequest();
		if (alterarForm.hasErrors()) {
			return badRequest(editar.render(id, alterarForm, feitas, pendentes ));
		}
		gerenciador.getDao().merge(alterarForm.get());
		gerenciador.getDao().flush();
		setStatusMeta(id);
 		if(thisMarked){
 			setNumMetasPorStatus(1, -1);
 		}
		return redirect(routes.Application.listaMetas());
	}
	
	@Transactional
 	public static Result setStatusMeta(Long id){
 		Meta meta = getMeta(id);
 		
 		if(meta.getStatus()){
 			meta.setStatus(false);
 			setNumMetasPorStatus(1, -1);
 		}else{
 			meta.setStatus(true);
 			setNumMetasPorStatus(-1, 1);
 			
 		}
 		gerenciador.setMetasConcluidas(feitas);
 		gerenciador.getDao().merge(meta);
 		gerenciador.getDao().flush();
 		return redirect(routes.Application.listaMetas());
 	}
	
	private static void setNumMetasPorStatus(int numPendencias, int numConcluidas){
		feitas += numConcluidas;
		pendentes += numPendencias;
		
	}
	
	private static Meta getMeta(Long id){
		return gerenciador.getDao().findByEntityId(Meta.class, id);
	}
	
	private static void povoaLista() throws MetaInvalidaException {
		if(gerenciador.getDao().findAllByClassName("Meta").isEmpty()){

			Meta meta1 = new Meta("Fazer correção dos labs", 1, "2014-06-22");
			gerenciador.getDao().persist(meta1);

			Meta meta2 = new Meta("Fazer lista de fisica moderna", 2,
					"2014-07-21");
			gerenciador.getDao().persist(meta2);

			Meta meta3 = new Meta(
					"Terminar manual do desenvolvedor para projeto", 1,
					"2014-07-03");
			gerenciador.getDao().persist(meta3);

			Meta meta4 = new Meta("Organizar o quarto", 3, "2014-07-01");
			gerenciador.getDao().persist(meta4);

			Meta meta5 = new Meta("Verificar pendencias para viagem", 3,
					"2014-07-01");
			gerenciador.getDao().persist(meta5);

			Meta meta6 = new Meta("Resolver problemas com RG", 1, "2014-06-29");
			gerenciador.getDao().persist(meta6);

			Meta meta7 = new Meta("Voltar para a academia", 2, "2014-07-05");
			gerenciador.getDao().persist(meta7);

			Meta meta8 = new Meta("fazer manutenção no carro", 3, "2014-07-06");
			gerenciador.getDao().persist(meta8);

			Meta meta9 = new Meta("estudar para provas", 2, "2014-06-29");
			gerenciador.getDao().persist(meta9);

			Meta meta10 = new Meta("Terminar documentação para projeto", 1,
					"2014-07-15");
			gerenciador.getDao().persist(meta10);
    		
    	}
	}
 
}
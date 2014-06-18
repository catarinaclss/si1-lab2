package controllers;
 
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.ComparadorPrioridades;
import models.ComparadorPorData;
import models.GerenciadorMetas;
import models.Meta;
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
	public static Result listaMetas() throws ParseException{
		
		List<Meta> metas = gerenciador.getDao().findAllByClassName("Meta");
		pendentes = metas.size() - feitas;
		
    	List<Integer> semanas = new ArrayList<>();
 
		
		int aux = 0;
		for (Meta meta : metas) {
			if(aux != gerenciador.getSemana(meta)){
				aux = gerenciador.getSemana(meta);
				semanas.add(aux);
			}
		}
		Collections.sort(metas, new ComparadorPrioridades());
		Collections.sort(metas, new ComparadorPorData());
		
		Collections.sort(semanas);
		return ok(index.render(semanas, metas, metaForm, feitas, pendentes));
	}
	
	@Transactional
	public static Result cadastro() {
		List<Meta> metas = gerenciador.getDao().findAllByClassName("Meta");
		Collections.sort(metas, new ComparadorPrioridades());
		Collections.sort(metas, new ComparadorPorData());
		return ok(cadastro.render(metaForm, feitas, pendentes));
	}
 
	@Transactional
	public static Result novaMeta() throws ParseException {
 
		List<Meta> result = gerenciador.getDao().findAllByClassName("Meta");
 
		Form<Meta> filledForm = metaForm.bindFromRequest();
		Meta meta = filledForm.get();
	
		if (filledForm.hasErrors() || !gerenciador.podeInserir(meta)) {
			
			flash("sucess", "Não foi possível inserir meta. Tente novamente.");
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
 			gerenciador.getDao().removeById(Meta.class, id);
 			gerenciador.getDao().flush();
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
        boolean thisMarked = gerenciador.getDao().findByEntityId(Meta.class, id).getStatus();
        Form<Meta> metaForm = Form.form(Meta.class).fill(gerenciador.getDao().findByEntityId(Meta.class, id));
        Form<Meta> alterarForm = Form.form(Meta.class).bindFromRequest();
        if (alterarForm.hasErrors()) {
                return badRequest(editar.render(id, alterarForm,feitas, pendentes ));
        }
        
        gerenciador.getDao().merge(alterarForm.get());
        gerenciador.getDao().flush();
        setStatusMeta(id);
         if(thisMarked){
                 feitas--;
                 pendentes++;
         }
         return redirect(routes.Application.listaMetas());
	}
 
	@Transactional
 	public static Result setStatusMeta(Long id){

		System.out.println("set status");
 		Meta meta = gerenciador.getDao().findByEntityId(Meta.class, id);
 		
 		if(meta.getStatus()){
 			meta.setStatus(false);
 			feitas--;
 			pendentes++;
 		}else{
 			meta.setStatus(true);
 			feitas++;
 			pendentes--;
 			
 		}
 		gerenciador.setMetasConcluidas(feitas);
 		gerenciador.getDao().merge(meta);
 		gerenciador.getDao().flush();
 		return redirect(routes.Application.listaMetas());
 	}
	
	
 
}
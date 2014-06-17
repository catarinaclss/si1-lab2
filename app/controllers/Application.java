package controllers;
 
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import models.ComparadorPrioridades;
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
	//private static List<Long> semanas = new ArrayList<>();
 
 
 
 
//	@Transactional
//    public static Result index()  throws ParseException  {
//    	List<Meta> metas = gerenciador.getListMetas();
//		
//		long aux = 0;
//		for (Meta meta : metas) {
//			if(aux != gerenciador.getSemana(meta)){
//				aux = gerenciador.getSemana(meta);
//				semanas.add(aux);
//			}
//		}
//        return ok(index.render(metas, semanas, gerenciador));
//    }
 
 
	@Transactional
	public static Result listaMetas() {
		List<Meta> metas = gerenciador.getDao().findAllByClassName("Meta");
		Collections.sort(metas, new ComparadorPrioridades());
		return ok(index.render(metas, metaForm));
	}
	
	@Transactional
	public static Result cadastro() {
		List<Meta> metas = gerenciador.getDao().findAllByClassName("Meta");
		Collections.sort(metas, new ComparadorPrioridades());
		return ok(cadastro.render(metaForm));
	}
 
	@Transactional
	public static Result novaMeta() throws ParseException {
 
		List<Meta> result = gerenciador.getDao().findAllByClassName("Meta");
 
		Form<Meta> filledForm = metaForm.bindFromRequest();
		Meta meta = filledForm.get();
	
		if (filledForm.hasErrors() || !gerenciador.podeInserir(meta)) {
			
			flash("sucess", "Não foi possível inserir meta. Tente novamente.");
			return badRequest(cadastro.render(filledForm));
			
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

		return ok(editar.render(id, metaForm));
	}
	
	@Transactional
	public static Result editarMeta(Long id) {
		Form<Meta> metaForm = Form.form(Meta.class).fill(gerenciador.getDao().findByEntityId(Meta.class, id));
		Form<Meta> alterarForm = Form.form(Meta.class).bindFromRequest();
		if (alterarForm.hasErrors()) {
			return badRequest(editar.render(id, alterarForm));
		}
		
		gerenciador.getDao().merge(alterarForm.get());
		gerenciador.getDao().flush();
		return redirect(routes.Application.listaMetas());
	}
 
	@Transactional
 	public static Result setStatusMeta(Long id){
 		long aux1 = id;
 		int aux2 = (int) aux1;
 
 		Meta meta = (Meta) gerenciador.getDao().findAllByClassName("Meta").get(aux2);
 		meta.setStatus(true);
 		gerenciador.getDao().remove(meta);
 		gerenciador.getDao().persist(meta);
 		gerenciador.getDao().flush();
 
 		return redirect(routes.Application.listaMetas());
 	}
 
}
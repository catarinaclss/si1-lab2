package controllers;
 
import java.text.ParseException;
import java.util.List;

import models.GerenciadorMetas;
import models.Meta;
import play.data.Form;
import play.data.validation.ValidationError;
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
 
		return ok(index.render(metas, metaForm));
	}
	
	@Transactional
	public static Result cadastro() {
		List<Meta> metas = gerenciador.getDao().findAllByClassName("Meta");
 
		return ok(cadastro.render(metas, metaForm));
	}
 
	@Transactional
	public static Result novaMeta() throws ParseException {
 
		List<Meta> result = gerenciador.getDao().findAllByClassName("Meta");
 
		Form<Meta> filledForm = metaForm.bindFromRequest();
		Meta meta = filledForm.get();
		System.out.println(gerenciador.podeInserir(meta));
		if (filledForm.hasErrors() || !gerenciador.podeInserir(meta)) {
			
			String errorMsg = "";
            java.util.Map<String, List<play.data.validation.ValidationError>> errorsAll = filledForm.errors();
            for (String field : errorsAll.keySet()) {
                errorMsg += field + " ";
                for (ValidationError error : errorsAll.get(field)) {
                    errorMsg += error.message() + ", ";
                }
            }
            flash("error", "Please correct the following errors: " + errorMsg);
			return badRequest(detail.render(filledForm));
			
		} else {
			gerenciador.getDao().persist(filledForm.get());
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
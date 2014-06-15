package controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import models.GerenciadorMetas;
import models.Meta;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {
	
	private static GerenciadorMetas gerenciador = new GerenciadorMetas();


	@Transactional
    public static Result index()  throws ParseException  {
    	List<Meta> metas = gerenciador.getListMetas();
		
		
		List<Long> semanas = new ArrayList<>();
		
		long aux = 0;
		for (Meta meta : metas) {
			if(aux != gerenciador.getSemana(meta)){
				aux = gerenciador.getSemana(meta);
				semanas.add(aux);
			}
		}
        return ok(index.render(metas, semanas, gerenciador));
    }
	
	
	public static Result cadastro() {
		Form<Meta> form = Form.form(Meta.class);
		return ok(cadastro.render(form));
	}
	
	@Transactional
	public static Result novaMeta() {

		Form<Meta> form = Form.form(Meta.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(cadastro.render(form));
		}
		Meta meta = form.get();
		gerenciador.getDao().persist(meta);
		gerenciador.getDao().flush();

		return redirect(routes.Application.index());
	}

}

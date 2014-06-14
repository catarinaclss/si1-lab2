package controllers;

import java.util.ArrayList;
import java.util.List;

import models.GerenciadorMetas;
import models.Meta;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	@Transactional
    public static Result index() {
    	GerenciadorMetas gerenciador = new GerenciadorMetas();
    	List<Meta> metas = gerenciador.getListMetas();
		
		
		List<Long> semanas = new ArrayList<>();
		
		long aux = 0;
		for (Meta meta : metas) {
			if(aux != gerenciador.getSemana(meta)){
				aux = gerenciador.getSemana(meta);
				semanas.add(aux);
			}
		}
        return ok(index.render(metas));
    }

}

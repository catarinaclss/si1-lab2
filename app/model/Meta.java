package model;

import java.text.ParseException;

import com.google.common.base.Objects;

public class Meta {
	
	private static int ALTA = 1;
	private static int MEDIA = 2;
	private static int BAIXA = 3;
	private Long id;
	private String descricao;
	private int prioridade;
	private String dataFinal;
	private boolean status;

	public Meta(){}
	
	public Meta(Long id, String descricao, int prioridade, String data){
		this.id = id;
		this.descricao = descricao;
		this.prioridade = prioridade;
		this.status = false;
		this.dataFinal = data;

	}
	
	public Long getId() {
		return id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public String getDataFinal() throws ParseException {
		return dataFinal;
	}
	
	public void setDataFinal(String dataFinal){
		this.dataFinal = dataFinal;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Meta)) {
			return false;
		}
		Meta meta = (Meta) obj;
		return Objects.equal(meta.getId(), this.getId());
	}

	
	@Override
	public int hashCode() {
		return Objects.hashCode(this.id);
	}
	
	
	
	


}

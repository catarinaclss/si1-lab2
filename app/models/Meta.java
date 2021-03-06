package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;

import com.google.common.base.Objects;

@Entity(name = "Meta")
public class Meta implements Comparable<Meta> {
	
	@Transient
	private final int ALTA = 1;
	@Transient
	private final int MEDIA = 2;
	@Transient
	private final int BAIXA = 3;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@Column(name = "descricao")
	@Required(message="Campo obrigatório")
	private String descricao;
	@Column(name = "prioridade")
	private int prioridade;
	@Column(name = "dataFinal")
	@Required(message="Data Maxima de 6 Semanas")
	private String dataFinal;
	@Column(name = "status")
	private boolean status;

	public Meta(){}
	
	public Meta(String descricao, int prioridade, String data){
		this.descricao = descricao;
		this.prioridade = prioridade;
		this.status = false;
		this.dataFinal = data;

	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getStringStatus(){
		if(getStatus() == true){
			return "CONCLUIDA";
		}else{
			return "PENDENTE";
		}
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

	public String getDataFinal(){
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
		return Objects.equal(meta.getDescricao(), this.getDescricao()) && 
			   Objects.equal(meta.getPrioridade(), this.getPrioridade()) &&
			   Objects.equal(meta.getStatus(), this.getStatus()) &&
			   Objects.equal(meta.getDataFinal(), this.getDataFinal());
	}

	
	@Override
	public int hashCode() {
		return Objects.hashCode(this.getDescricao());
	}

	@Override
	public int compareTo(Meta meta) {

		return getDataFinal().compareTo(meta.getDataFinal());
	}



	
	
	
	


}

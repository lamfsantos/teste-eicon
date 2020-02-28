package br.com.teste.eicon.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Pedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	private Integer numeroControle;

	@NotNull
	private String nome;

	@NotNull
	private Double valor;

	@NotNull
	private Integer codigoCliente;

	private String dataCadastro;
	private Integer quantidade;

	public Pedido() {
	}

	public Pedido(Integer numeroControle, String dataCadastro, String nome, Double valor, Integer quantidade,
			Integer codigoCliente) {
		super();
		this.numeroControle = numeroControle;

		if (dataCadastro == null) {
			Date date = Calendar.getInstance().getTime();
			DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
			this.dataCadastro = dateFormat.format(date);
		} else {
			this.dataCadastro = dataCadastro;
		}

		this.nome = nome;
		this.valor = valor;
		this.quantidade = (quantidade == null) ? 1 : quantidade;
		this.codigoCliente = codigoCliente;
	}

	public Integer getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(Integer numeroControle) {
		this.numeroControle = numeroControle;
	}

	public String getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(String dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(Integer codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroControle == null) ? 0 : numeroControle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		if (numeroControle == null) {
			if (other.numeroControle != null)
				return false;
		} else if (!numeroControle.equals(other.numeroControle))
			return false;
		return true;
	}

}

package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

	private Filme filme;

	private FilmeBuilder() {

	}

	public static FilmeBuilder umFilme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setNome("Harry Potter");
		builder.filme.setPrecoLocacao(5.0);
		builder.filme.setEstoque(2);
		return builder;
	}
	
	public static FilmeBuilder umFilmeSemEstoque() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setNome("Harry Potter");
		builder.filme.setPrecoLocacao(5.0);
		builder.filme.setEstoque(0);
		return builder;
	}
	
	public FilmeBuilder comValor(Double valor) {
		filme.setPrecoLocacao(valor);
		return this;
	}
	
	public FilmeBuilder semEstoque() {
		filme.setEstoque(0);
		return this;
	}
	
	public Filme agora() {
		return filme;
	}

}

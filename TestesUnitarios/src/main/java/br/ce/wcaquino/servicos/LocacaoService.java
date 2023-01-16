package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	
	Double valorTotal = 0d;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException {

		if (filmes == null)
			throw new LocadoraException("Filme vazio");

		if (filmes == null || filmes.isEmpty())
			throw new FilmeSemEstoqueException();

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0)
				throw new FilmeSemEstoqueException();
		};

		if (usuario == null)
			throw new LocadoraException("Usuario vazio");

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		for (int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			
			switch(i) {
			
			case 2 : valorFilme = valorFilme * 0.75;
			
			case 3 : valorFilme = valorFilme * 0.5;
			
			case 4 : valorFilme = valorFilme * 0.25;
			
			case 5 : valorFilme = 0.0;
			
			}			
			valorTotal += valorFilme;
		}
		locacao.setValor(valorTotal);	

		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY))
			dataEntrega = adicionarDias(dataEntrega, 1);
		
		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		// TODO adicionar método para salvar

		return locacao;
	}
}
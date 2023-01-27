package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {

	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;

	Double valorTotal = 0d;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException {

		if (filmes == null)
			throw new LocadoraException("Filme vazio");

		if (filmes == null || filmes.isEmpty())
			throw new FilmeSemEstoqueException();

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0)
				throw new FilmeSemEstoqueException();
		}
		
		if(spcService.possuiNegativacao(usuario))
			 throw new LocadoraException("Usuário Negativado");
	

		if (usuario == null)
			throw new LocadoraException("Usuario vazio");

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		for (int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();

			switch (i) {

			case 2:
				valorFilme = valorFilme * 0.75;
				break;

			case 3:
				valorFilme = valorFilme * 0.5;
				break;

			case 4:
				valorFilme = valorFilme * 0.25;
				break;

			case 5:
				valorFilme = 0d;
				break;

			}
			valorTotal += valorFilme;
		}
		locacao.setValor(valorTotal);

		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY))
			dataEntrega = adicionarDias(dataEntrega, 1);

		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		dao.salvar(locacao);

		return locacao;
	}
	
	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.obterLocacoesPedentes();
		for(Locacao locacao: locacoes) {
			emailService.notificarAtraso(locacao.getUsuario());
		}
	}

	public void setDao(LocacaoDAO dao) {
		this.dao = dao;
	}
	
	public void setSpcService(SPCService scpService) {
		this.spcService = scpService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
}
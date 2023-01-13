package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {

	private LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector(); // Coletar todas as falhas somente e um tempo de execução no
														// mesmo metodo teste;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private static int contador = 0;

	@Before
	public void setup() {
		System.out.println("Before");
		service = new LocacaoService();
		contador++;
		System.out.println(contador);
	}

	@After
	public void tearDown() {
		System.out.println("After");
	}

	@BeforeClass
	public static void setupClass() {
		System.out.println("Before");
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println("After");
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		// Cenario
		Usuario usuario = new Usuario("Patrick");
		List<Filme> filmes = Arrays.asList(new Filme("Harry Potter", 2, 5.0));

		// Acão
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verificação
		assertEquals(5.0, locacao.getValor(), 0.01);

		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				is(true));

		// assertThat(locacao.getValor(), is(equalTo(5.0)));
		// assertThat(locacao.getValor(), is(not(6.0)));
		// assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),
		// is(true));

		// assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(),
		// DataUtils.obterDataComDiferencaDias(1)));

	}

	@Test(expected = FilmeSemEstoqueException.class) // Deve ser usado somente se o codigo retornar um unico tipo de
														// excecão esperada.
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// Cenario
		Usuario usuario = new Usuario("Patrick");
		List<Filme> filmes = Arrays.asList(new Filme("Harry Potter", 0, 5.0));

		// Acão
		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		// Usuario usuario = new Usuario("Patrick");
		List<Filme> filmes = Arrays.asList(new Filme("Harry Potter", 2, 5.0));

		// Acão
		try {
			service.alugarFilme(null, filmes);
			fail(); // Lançando erro manualmente, para evitar falso positivo
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

		System.out.println("Forma Robusta");
	}

	@Test()
	public void naoDeveAlugarFilmeSemFilme() throws Exception {
		// Cenario
		Usuario usuario = new Usuario("Patrick");
		// Filme filme = new Filme("Harry Potter", 0, 5.0);

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// Acão
		service.alugarFilme(usuario, null);

		System.out.println("Forma nova");

	}

	@Test
	public void devePagar75PctNoFilme3() throws LocadoraException, FilmeSemEstoqueException {
		// cenario
		Usuario usuario = new Usuario();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0));

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verificao
		assertThat(resultado.getValor(), is(11.0));

	}

	@Test
	public void devePagar50PctNoFilme4() throws LocadoraException, FilmeSemEstoqueException {
		// cenario
		Usuario usuario = new Usuario();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 3", 2, 4.0));

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verificao
		assertThat(resultado.getValor(), is(13.0));

	}
}

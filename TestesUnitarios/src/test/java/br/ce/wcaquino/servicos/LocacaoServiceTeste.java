package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito.*;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {

    private LocacaoService service;
    
    private LocacaoDAO dao;
	
	private SPCService spcService;
	
	@Rule
	public ErrorCollector error = new ErrorCollector(); // Coletar todas as falhas somente e um tempo de execução no
														// mesmo metodo teste;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		//System.out.println("Before");
		service = new LocacaoService();
		
        dao = mock(LocacaoDAO.class);
        service.setDao(dao);
        
        spcService = mock(SPCService.class);
        service.setSpcService(spcService);
        
	}

	@After
	public void tearDown() {
		//System.out.println("After");
	}

	@BeforeClass
	public static void setupClass() {
		//System.out.println("Before");
	}

	@AfterClass
	public static void tearDownClass() {
		//System.out.println("After");
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// Cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

		// Acão
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verificação
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
		error.checkThat(locacao.getDataRetorno(), ehHoje());
		
		
		//error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
				//error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				//		is(true));
		//assertEquals(5.0, locacao.getValor(), 0.01);
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
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		// Acão
		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		// Usuario usuario = new Usuario("Patrick");
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// Acão
		try {
			service.alugarFilme(null, filmes);
			fail(); // Lançando erro manualmente, para evitar falso positivo
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

		System.out.println("Forma Robusta");
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws Exception {
		// Cenario
		Usuario usuario = umUsuario().agora();
		// Filme filme = new Filme("Harry Potter", 0, 5.0);

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// Acão
		service.alugarFilme(usuario, null);

		System.out.println("Forma nova");

	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarSabado() throws LocadoraException, FilmeSemEstoqueException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//Cenario
		Usuario usuario = umUsuario().agora();		
		List<Filme> filmes = Arrays.asList(umFilme().agora()); 
		
		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		// verificao
		assertThat(resultado.getDataRetorno(), caiNumaSegunda());	
		
		//assertThat(resultado.getDataRetorno(), caiEm(Calendar.MONDAY));		
		//assertThat(resultado.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
	    //boolean ehSegunda = DataUtils.verificarDiaSemana(resultado.getDataRetorno(), Calendar.MONDAY);
		//assertTrue(ehSegunda);
		// assertThat(retorno.getDataRetorno(),caiEm(Calendar.MONDAY));
		// assertThat(retorno.getDataRetorno(),caiNumaSegunda());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws LocadoraException, FilmeSemEstoqueException {
		//cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("RICARDO").agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spcService.possuiNegativacao(usuario)).thenReturn(true);
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Usuário Negativado");
		
		
		//acao
		service.alugarFilme(usuario2, filmes);
				
	}
	
}

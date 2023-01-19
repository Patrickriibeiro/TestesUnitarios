package br.ce.wcaquino.matchers.suites;

import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.CalculoValorLocacao;
import br.ce.wcaquino.servicos.LocacaoServiceTeste;

//@RunWith(Suite.class) // definir tempo de execução
@SuiteClasses({ CalculadoraTest.class, CalculoValorLocacao.class, LocacaoServiceTeste.class })
public class SuitesExecucao {
	// Remova essa classe se puder, não tem uso;

	/*
	 * @BeforeClass public static void before() { System.out.println("Before"); }
	 * 
	 * @AfterClass public static void after() { System.out.println("After"); }
	 */

}

package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exception.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
	}

	@Test
	public void deveSomarDoisValores() {
		// Cenario
		int a = 5;
		int b = 3;
	
		// Ação
		int resultado = calc.somar(a, b);

		// verificação
		Assert.assertEquals(8, resultado);

	}

	@Test
	public void deveSubtrairDoisValores() {
		// Cenario
		int a = 8;
		int b = 5;

		// Ação
		int resultado = calc.subtrair(a, b);

		// verificação
		Assert.assertEquals(3, resultado);

	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		// Cenario
		int a = 6;
		int b = 3;

		// Ação
		int resultado = calc.dividir(a, b);

		// verificação
		Assert.assertEquals(2, resultado);

	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		// Cenario
		int a = 0;
		int b = 0;
	
		// Ação
		int resultado = calc.dividir(a, b);

		// verificação
		Assert.assertEquals(5, resultado);

	}
	
	

}

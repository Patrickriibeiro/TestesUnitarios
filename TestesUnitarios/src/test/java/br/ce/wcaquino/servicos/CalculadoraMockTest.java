package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {
	
	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		Mockito.when(calc.somar(Mockito.eq(1),Mockito.anyInt())).thenReturn(5);  // Não aceita uma valor fixo e um matcher, ambos devem ser matchers.
	
		//Mockito.eq é equal, oq significa que o teste deve ser feito com o valor 1;
		
	   System.out.println(calc.somar(1, 8));
	}

}

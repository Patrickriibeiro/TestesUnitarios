package br.ce.wcaquino.asserts;


import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {

	@Test
	public void test() {
		Assert.assertTrue(true); // Teste boleano
		//Assert.assertTrue(false);

		//Assert.assertEquals("Erro de comparacao", 2, 2); // teste de valores iguais.
		Assert.assertEquals(0.51, 0.51, 0.01); // terceiro parametro é o delta de comparação
		Assert.assertEquals(Math.PI, 3.14, 0.01);

		int i = 5;
		Integer i2 = 5;
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i, i2.intValue());

		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("bola", "casa");
		Assert.assertTrue("bola".equalsIgnoreCase("bola"));
		Assert.assertTrue("bola".startsWith("ba"));

		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 2");
		Usuario u3 = null;

		Assert.assertEquals(u1, u2);

		Assert.assertSame(u2, u2);
		Assert.assertNotSame(u1, u2); /// Verifica se o objeto deriva da mesma instacia

		Assert.assertNull(u3);
		Assert.assertNotNull(u2);// verifica se o valor é null;

	}

}

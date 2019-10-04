package com.ezgroceries.shoppinglist;

import com.ezgroceries.shoppinglist.contract.Resources;
import com.ezgroceries.shoppinglist.contract.CocktailResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@ContextConfiguration
public class ShoppingListApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void searchForCocktails() throws Exception
	{
		// GIVEN
		String search = "Russian";

		// WHEN
		ResponseEntity<Resources<CocktailResource>> responseEntity = restTemplate.exchange(
				String.format("/cocktails?search=%s", search), HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<CocktailResource>>() {});

		// THEN
		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Resources<CocktailResource> cocktailResources = responseEntity.getBody();
		Assert.assertNotNull(cocktailResources);
		Assert.assertNotNull(cocktailResources.getResources());
		Assert.assertTrue(cocktailResources.getResources().size() > 0);

		for(CocktailResource cocktailResource : cocktailResources.getResources()) {
			Assert.assertNotNull(cocktailResource.getDescription());
			Assert.assertTrue(cocktailResource.getDescription().contains(search));
		}
	}

}

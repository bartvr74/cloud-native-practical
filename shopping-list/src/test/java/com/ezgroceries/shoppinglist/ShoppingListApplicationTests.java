package com.ezgroceries.shoppinglist;

import com.ezgroceries.shoppinglist.dto.CocktailResource;
import com.ezgroceries.shoppinglist.dto.CreateShoppingList;
import com.ezgroceries.shoppinglist.dto.Resources;
import com.ezgroceries.shoppinglist.dto.ShoppingListResource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
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
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class ShoppingListApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test // broad testing with actual the cocktail db http call
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

	@Test // broad testing with actual the cocktail db http call
	public void findShoppingList() throws Exception
	{
		// GIVEN
		UUID listId = createShoppingList("SpringBootLaunchParty");

		// WHEN
		ShoppingListResource shoppingListResource =
				restTemplate.getForObject("/shopping-lists/{listId}", ShoppingListResource.class, listId);

		// THEN
		Assert.assertNotNull(shoppingListResource);
		Assert.assertEquals(listId, shoppingListResource.getId());
	}

	private UUID createShoppingList(String shoppingListName) throws URISyntaxException  {
		// input shopping list creation
		CreateShoppingList createShoppingList = new CreateShoppingList();
		createShoppingList.setName(shoppingListName);

		// fire request
		RequestEntity<CreateShoppingList> requestEntity = new RequestEntity<>(
				createShoppingList, HttpMethod.POST, new URI("/shopping-lists")
		);

		ResponseEntity<ShoppingListResource> responseEntity =
				restTemplate.exchange(requestEntity, new ParameterizedTypeReference<ShoppingListResource>() {});

		// basic check output
		Assert.assertNotNull(responseEntity.getBody());
		System.out.println(responseEntity.getBody());
		Assert.assertNotNull(responseEntity.getBody().getId());
		return responseEntity.getBody().getId();
	}

}

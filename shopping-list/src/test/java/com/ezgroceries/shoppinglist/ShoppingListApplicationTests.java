package com.ezgroceries.shoppinglist;

import com.ezgroceries.shoppinglist.dto.CocktailReferenceDto;
import com.ezgroceries.cocktail.dto.CocktailResourceResponse;
import com.ezgroceries.shoppinglist.dto.CreateShoppingListRequest;
import com.ezgroceries.cocktail.dto.ResourcesResponse;
import com.ezgroceries.shoppinglist.dto.ShoppingListResourceResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@ActiveProfiles({"hsqldb", "test"})
public class ShoppingListApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test // broad testing with actual the cocktail db http call
	public void findCocktails() throws Exception
	{
		// GIVEN
		String search = "Russian";

		// WHEN
		List<CocktailResourceResponse> cocktailResources = searchForCocktails(search);

		// THEN
		Assert.assertNotNull(cocktailResources);
		Assert.assertFalse(cocktailResources.isEmpty());
	}

	@Test // broad testing with actual the cocktail db http call
	public void findShoppingList() throws Exception
	{
		// GIVEN
		UUID listId = createShoppingList("SpringBootLaunchParty");

		// WHEN
		ShoppingListResourceResponse shoppingListResource =
				restTemplate.getForObject("/shopping-lists/{listId}", ShoppingListResourceResponse.class, listId);

		// THEN
		Assert.assertNotNull(shoppingListResource);
		Assert.assertEquals(listId, shoppingListResource.getId());
	}

	@Test // broad testing linking cocktail to shopping list
	public void searchCocktailsAndLinkToNewList() throws Exception {
		// GIVEN
		UUID listId = createShoppingList("LinkingParty");
		List<CocktailResourceResponse> foundCocktails = searchForCocktails("Russian");

		// WHEN
		List<CocktailReferenceDto> cocktailReferences = new ArrayList<>();
		for(CocktailResourceResponse foundCocktail : foundCocktails) {
			CocktailReferenceDto cocktailReference = new CocktailReferenceDto();
			cocktailReference.setCocktailId(foundCocktail.getId());
			cocktailReferences.add(cocktailReference);
		}

		RequestEntity<List<CocktailReferenceDto>> requestEntity =
				new RequestEntity<>(cocktailReferences, HttpMethod.POST,
						new URI(String.format("/shopping-lists/%s/cocktails",listId.toString())));

		ResponseEntity<List<CocktailReferenceDto>> responseEntity =
				restTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<CocktailReferenceDto>>() {});

		// THEN
		Assert.assertNotNull(responseEntity);
		Assert.assertNotNull(responseEntity.getBody());
		Assert.assertTrue(responseEntity.getBody().size() == foundCocktails.size());

		ShoppingListResourceResponse shoppingListResource = restTemplate.getForObject("/shopping-lists/{listId}",
				ShoppingListResourceResponse.class, listId);
		Assert.assertNotNull(shoppingListResource);
		Assert.assertEquals(shoppingListResource.getId(), listId);
		Assert.assertEquals(shoppingListResource.getName(), "LinkingParty");
		Assert.assertNotNull(shoppingListResource.getIngredients());
		Assert.assertTrue(shoppingListResource.getIngredients().size() > 0);
	}

	private List<CocktailResourceResponse> searchForCocktails(String searchString) throws Exception {
		// search cocktails using provided search string
		ResponseEntity<ResourcesResponse<CocktailResourceResponse>> responseEntity = restTemplate.exchange(
				String.format("/cocktails?search=%s", searchString), HttpMethod.GET, null,
				new ParameterizedTypeReference<ResourcesResponse<CocktailResourceResponse>>() {});

		// basic check search output
		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		ResourcesResponse<CocktailResourceResponse> cocktailResources = responseEntity.getBody();
		Assert.assertNotNull(cocktailResources);
		Assert.assertNotNull(cocktailResources.getResources());
		Assert.assertTrue(cocktailResources.getResources().size() > 0);
		for(CocktailResourceResponse cocktailResource : cocktailResources.getResources()) {
			Assert.assertNotNull(cocktailResource.getDescription());
			Assert.assertTrue(cocktailResource.getDescription().contains(searchString));
		}
		return cocktailResources.getResources();
	}

	private UUID createShoppingList(String shoppingListName) throws URISyntaxException  {
		// input shopping list creation
		CreateShoppingListRequest createShoppingList = new CreateShoppingListRequest();
		createShoppingList.setName(shoppingListName);

		// fire request
		RequestEntity<CreateShoppingListRequest> requestEntity = new RequestEntity<>(
				createShoppingList, HttpMethod.POST, new URI("/shopping-lists")
		);

		ResponseEntity<ShoppingListResourceResponse> responseEntity =
				restTemplate.exchange(requestEntity, new ParameterizedTypeReference<ShoppingListResourceResponse>() {});

		// basic check output
		Assert.assertNotNull(responseEntity.getBody());
		Assert.assertNotNull(responseEntity.getBody().getId());
		return responseEntity.getBody().getId();
	}

}

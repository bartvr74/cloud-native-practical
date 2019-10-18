package com.ezgroceries.shoppinglist;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezgroceries.shoppinglist.controller.CocktailController;
import com.ezgroceries.shoppinglist.dto.CocktailResource;
import com.ezgroceries.shoppinglist.dto.Resources;
import com.ezgroceries.shoppinglist.search.SearchCocktailDbClient;
import com.ezgroceries.shoppinglist.search.SearchCocktailDbResponse;
import com.ezgroceries.shoppinglist.search.SearchCocktailDbResponse.DrinkResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(CocktailController.class)
@ComponentScan("com.ezgroceries")
@AutoConfigureDataJpa
public class CocktailControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SearchCocktailDbClient cocktailDbClient; // mock remote db so service logic is also tested

    @Before
    public void prepareMocking() {
        Mockito.when(cocktailDbClient.searchCocktails(Mockito.anyString())).thenReturn(searchCocktailsDummyData());
    }

    @Test
    public void searchForCocktails() throws Exception
    {
        // GIVEN
        String search = "Russian";

        // WHEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/cocktails?search=%s", search))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        MvcResult mvcResult = resultActions.andReturn();
        String jsonContent = mvcResult.getResponse().getContentAsString();
        Resources<CocktailResource> cocktailResources =
                objectMapper.readValue(jsonContent, new TypeReference<Resources<CocktailResource>>() {});

        // THEN
        Assert.assertNotNull(cocktailResources);
        Assert.assertNotNull(cocktailResources.getResources());
        Assert.assertEquals(1, cocktailResources.getResources().size());

        CocktailResource cocktailResource = cocktailResources.getResources().get(0);
        Assert.assertEquals("Margarita", cocktailResource.getDescription());
        Assert.assertEquals("Cocktail glass", cocktailResource.getTypeOfGlass());
        Assert.assertNotNull(cocktailResource.getIngredients());
        Assert.assertTrue(cocktailResource.getIngredients().contains("Tequila"));
    }

    private SearchCocktailDbResponse searchCocktailsDummyData() {
        DrinkResource drinkResource = new DrinkResource();
        drinkResource.setIdDrink("foobar");
        drinkResource.setStrDrink("Margarita");
        drinkResource.setStrDrinkThumb("https://my.url.be/cocktail");
        drinkResource.setStrGlass("Cocktail glass");
        drinkResource.setStrIngredient1("Tequila");

        SearchCocktailDbResponse cocktailDbResponse = new SearchCocktailDbResponse();
        cocktailDbResponse.setDrinks(Arrays.asList(drinkResource));
        return cocktailDbResponse;
    }

}

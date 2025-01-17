package com.ezgroceries.shoppinglist;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezgroceries.shoppinglist.controller.ShoppingListController;
import com.ezgroceries.shoppinglist.dto.CreateShoppingListRequest;
import com.ezgroceries.shoppinglist.dto.ShoppingListResourceResponse;
import com.ezgroceries.cocktail.service.external.SearchCocktailDbClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(ShoppingListController.class)
@ComponentScan("com.ezgroceries")
@AutoConfigureDataJpa
@ActiveProfiles({"hsqldb", "test"})
public class ShoppingListControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SearchCocktailDbClient cocktailDbClient; // mock remote db so service logic is also tested

    @Test
    public void createShoppingList() throws Exception {
        UUID listId = createShoppingList("FoodForFun");
        Assert.assertNotNull(listId);
    }

    @Test
    public void findShoppingList() throws Exception
    {
        // GIVEN
        UUID listId = createShoppingList("FoodForParty");

        // WHEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .get("/shopping-lists/{listId}", listId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        MvcResult mvcResult = resultActions.andReturn();
        String jsonContent = mvcResult.getResponse().getContentAsString();
        ShoppingListResourceResponse shoppingListResource = objectMapper.
                readValue(jsonContent, new TypeReference<ShoppingListResourceResponse>() {});

        // THEN
        Assert.assertNotNull(shoppingListResource);
        Assert.assertNotNull(shoppingListResource.getId());
        Assert.assertNotNull(shoppingListResource.getName());
        Assert.assertEquals(listId, shoppingListResource.getId());
    }

    private UUID createShoppingList(String shoppingListName) throws Exception
    {
        // GIVEN
        CreateShoppingListRequest createShoppingList = new CreateShoppingListRequest();
        createShoppingList.setName(shoppingListName);
        String jsonCreateList = objectMapper.writeValueAsString(createShoppingList);

        // WHEN
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .post("/shopping-lists")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonCreateList))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        MvcResult mvcResult = resultActions.andReturn();
        String jsonContent = mvcResult.getResponse().getContentAsString();
        ShoppingListResourceResponse shoppingListResource = objectMapper.
                readValue(jsonContent, new TypeReference<ShoppingListResourceResponse>() {});

        // THEN
        Assert.assertNotNull(shoppingListResource);
        Assert.assertNotNull(shoppingListResource.getId());
        Assert.assertNotNull(shoppingListResource.getName());
        Assert.assertEquals(shoppingListName, shoppingListResource.getName());

        // DONE
        return shoppingListResource.getId();
    }

}

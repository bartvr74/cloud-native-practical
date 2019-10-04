package com.ezgroceries.shoppinglist;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezgroceries.shoppinglist.controller.CocktailController;
import com.ezgroceries.shoppinglist.model.CocktailResource;
import com.ezgroceries.shoppinglist.model.Resources;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(CocktailController.class)
public class CocktailControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void searchForCocktails() throws Exception
    {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .get("/cocktails?search=Russian")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        MvcResult mvcResult = resultActions.andReturn();
        String jsonContent = mvcResult.getResponse().getContentAsString();
        Resources<CocktailResource> cocktailResources =
                objectMapper.readValue(jsonContent, new TypeReference<Resources<CocktailResource>>() {});

        System.out.println(jsonContent);

        Assert.assertNotNull(cocktailResources);
        Assert.assertNotNull(cocktailResources.getResources());
        Assert.assertEquals(2, cocktailResources.getResources().size());
        Assert.assertEquals(UUID.fromString("23b3d85a-3928-41c0-a533-6538a71e17c4"),
                cocktailResources.getResources().get(0).getId());
    }

    // todo add other tests and check other actual json output data

}

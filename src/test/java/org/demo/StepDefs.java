package org.demo;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriver;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


@WebAppConfiguration
@ContextConfiguration(classes = Config.class)
public class StepDefs {
	@Autowired private WebApplicationContext context;
	MockMvcHtmlUnitDriver driver;
	IndexPage indexPage;

	@Before
	public void setup() throws IOException {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		
		//	The tests always failed when executing JavaScript such as document.addEventListener()
		//	(as found in JQuery).  Adding this chrome capability patched up the problem.
		//	Update: chrome doesn't seem to get AJAX replies from the server to work.  FireFox does:
		Capabilities capabilities = DesiredCapabilities.firefox(); 
		driver = new MockMvcHtmlUnitDriver(mockMvc, capabilities);
	}

	@After
	public void destroy() {
		if(driver != null) {
			driver.close();
		}
	}

	//	TODO:  WHERE I LEFT OFF
	//	I can get to the landing page as long as I use the chrome capabilities above.
	//	The movie selection box was empty.  This was because the result of the AJAX call was never processed by the JQuery code.  Switching from chrome to FF resolved this.
	
	@Given("I am on the first page")	
	@When("I go to the landing page")
	public void i_go_to_the_landing_page() throws Throwable {
		indexPage = IndexPage.to(driver);
	}


	@Then("I expect to see a list of Monty Python movies")
	public void i_expect_to_see_movie_list() throws Throwable {
		assertTrue("Should have at least 3 options in the list", indexPage.getNumberOfMovieOptions() > 3);  //Presently failing now that I'm populating this with AJAX, static was fine.
	}

	@Then("one of the options should be 'Holy Grail'")
	public void i_expect_holy_grail() throws Throwable {
		assertTrue("Holy Grail doesn't seem to be one of the options.", indexPage.isMovieOptionPresent("Holy Grail"));
	}

	@When("I select 'Holy Grail'")
	public void i_select_category() throws Throwable {
		indexPage.selectMovieOption("Holy Grail");
	}

	@When("I select 'What do the Knights of Ni say'")
	public void i_select_question() throws Throwable { 
		indexPage.selectQuestionOption("What do the Knights of Ni say?");
	}
	
	@And("^I press submit$")
	public void submit() throws Throwable { }

	@Then("^I should see the answer page$")
	public void on_answer_page() throws Throwable { }

	@And("^I should see the question displayed$")
	public void question_displayed() throws Throwable { }

	@And("^I should see the answer 'Ni!'$")
	public void answer_displayed() throws Throwable { }
}

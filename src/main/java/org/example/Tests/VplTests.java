package org.example.Tests;


import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import org.testng.Assert;
import org.example.PageObjects.HomePage;
import org.example.PageObjects.ResultsPage;

@Listeners(Listener.class)
public class VplTests extends BaseTest
{

    private static final String KEYWORD = "java";

    @Test(groups = "High")
    public void keyword_Search_Returns_Results_Test()
    {

        HomePage homePage = openHomePage();

        ResultsPage resultsPage = homePage.searchFor(KEYWORD);

        Assert.assertTrue(resultsPage.totalCount() > 0,
                "total results count is not positive!");

    }


    @Test(groups = "Medium")
    public void any_Page_Has_Results_Test()
    {

        HomePage homePage = openHomePage();

        ResultsPage resultsPage = homePage.searchFor(KEYWORD);
        Assert.assertEquals(resultsPage.resultsPerPage(), "1 to 10");

        ResultsPage resultsPage3 = resultsPage.goToPage(3);
        Assert.assertEquals(resultsPage3.resultsPerPage(), "21 to 30");

        ResultsPage resultsPage5 = resultsPage3.goToPage(5);
        Assert.assertEquals(resultsPage5.resultsPerPage(), "41 to 50");

    }


    @Test(groups = "Low")
    public void next_Page_Has_Results_Test()
    {

        HomePage homePage = openHomePage();

        ResultsPage resultsPage = homePage.searchFor(KEYWORD);
        Assert.assertEquals(resultsPage.resultsPerPage(), "1 to 10");

        ResultsPage resultsPage2 = resultsPage.goToNextPage();
        Assert.assertEquals(resultsPage2.resultsPerPage(), "11 to 20");

        ResultsPage resultsPage3 = resultsPage2.goToNextPage();
        Assert.assertEquals(resultsPage3.resultsPerPage(), "21 to 30");

    }

}
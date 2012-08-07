package net.billylieurance.azuresearch.test;

/*
Copyright 2012 William Lieurance

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import net.billylieurance.azuresearch.AbstractAzureSearchQuery.AZURESEARCH_QUERYTYPE;
import net.billylieurance.azuresearch.AbstractAzureSearchResult;
import net.billylieurance.azuresearch.AzureSearchCompositeQuery;
import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebResult;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class AzureSearchCompositeTest {

	AbstractAzureSearchResult asr;
	
	@Test
	public void TestAppid(){
		final String reason = "You need a valid Azure Appid as the static final String AZURE_APPID in net.billylieurance.azuresearch.test.AzureAppid to run these tests.";
		Assert.assertNotNull(AzureAppid.AZURE_APPID, reason);
		Assert.assertNotEquals(AzureAppid.AZURE_APPID, "", reason);		
	}
	
	@Test 
	public void TestConstructor(){
		AzureSearchCompositeQuery aq = new AzureSearchCompositeQuery();
		Assert.assertNotNull(aq, "Did not generate an actual query object.");
	}
	
	@Test
	(dependsOnMethods = "TestConstructor")
	public void buildQuery(){
		AzureSearchCompositeQuery aq = new AzureSearchCompositeQuery();
		aq.setAppid(AzureAppid.AZURE_APPID);
		aq.setQuery("Oklahoma Sooners");
		aq.setSources(new AZURESEARCH_QUERYTYPE[] {AZURESEARCH_QUERYTYPE.WEB, AZURESEARCH_QUERYTYPE.NEWS, AZURESEARCH_QUERYTYPE.IMAGE, AZURESEARCH_QUERYTYPE.VIDEO});
		
		
		Assert.assertEquals(aq.getQueryPath(), "/Data.ashx/Bing/Search/v1/Composite");
		Assert.assertEquals(aq.getUrlQuery(),"Query='Oklahoma Sooners'&$top=15&$format=Atom&Sources='web+news+image+video'");
	}
	
	@Test
	(dependsOnMethods = {"TestConstructor", "TestAppid"})
	public void buildQueryResult(){
		AzureSearchCompositeQuery aq = new AzureSearchCompositeQuery();
		aq.setAppid(AzureAppid.AZURE_APPID);
		aq.setQuery("Oklahoma Sooners");
		aq.setSources(new AZURESEARCH_QUERYTYPE[] {AZURESEARCH_QUERYTYPE.WEB, AZURESEARCH_QUERYTYPE.NEWS, AZURESEARCH_QUERYTYPE.IMAGE});
		
		aq.doQuery();
		Document ad = aq.getRawResult();
		Assert.assertNotNull(ad);
		
		AzureSearchResultSet<AbstractAzureSearchResult> ars = aq.getQueryResult();
		Assert.assertNotNull(ars, "getQueryResult returned null");
		Assert.assertNotNull(ars.getASRs(), "getQueryResult.getASRs returned null");
		Assert.assertFalse(ars.getASRs().isEmpty(), "getQueryResult returned no results");
	
		asr = ars.getASRs().get(0);
		Assert.assertNotNull(asr, "Unparseable result from result.");
		
		
		
	}
	
	@Test
	(dependsOnMethods = "buildQueryResult")
	public void checkIsAWebResult(){		
		Assert.assertTrue(asr instanceof AzureSearchWebResult);
	}
	
	
	
	
	//Below this are the abstract tests
	
			@Test
			(dependsOnMethods = "buildQueryResult")
			public void getId(){		
				Assert.assertNotNull(asr.getId(), "Unparseable ID from result");
			}
			
			@Test
			(dependsOnMethods = "buildQueryResult")
			public void getTitle(){		
				Assert.assertNotNull(asr.getTitle(), "Unparseable Title from result");
			}
	
	
}

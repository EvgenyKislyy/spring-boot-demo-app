package com.demoapp.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ //
		ProductTest.class, //
		CategoryTest.class, //
		OrderTest.class, //
		OrderItemTest.class, //
		ReportTest.class })
public class AllIntegrationTests {

}

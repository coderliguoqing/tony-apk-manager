package com.tony.test;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.tony.admin.web.TonyApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TonyApplication.class)
public class SampleTest {
	
	@Ignore
	public void test(){

	}

}

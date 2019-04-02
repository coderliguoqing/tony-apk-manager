package com.tony.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tony.admin.web.TonyApplication;
import com.tony.admin.web.mapper.MBannerMapper;
import com.tony.admin.web.model.MBanner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TonyApplication.class)
public class SampleTest {
	
	@Autowired
	private MBannerMapper bannerMapper;
	
	@Test
	public void test() {
		List<MBanner> list = bannerMapper.selectAll();
		list.forEach(banner->{
			System.out.println(banner);
		});
	}

}

package com.sdu.inas;

import com.sdu.inas.controller.ParamController;
import com.sdu.inas.repository.HbaseDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InasApplicationTests {

	@Autowired
	ParamController paramController;

	@Autowired
	HbaseDao hbaseDao;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testDel() throws UnsupportedEncodingException {
		String ss="曹操T3HRGAS9%%曹操";
		paramController.deleteParam(ss);
	}

	@Test
	public void deleteRele(){
		hbaseDao.delColumnByQualifier("Rele_Infos","e47715e0-ed94-457f-a592-db6","ReleParam","description");
		hbaseDao.delColumnByQualifier("Rele_Infos","e47715e0-ed94-457f-a592-db6","ReleParam","sourceEntityId");
		hbaseDao.delColumnByQualifier("Rele_Infos","e47715e0-ed94-457f-a592-db6","ReleParam","sourceEventId");
		hbaseDao.delColumnByQualifier("Rele_Infos","e47715e0-ed94-457f-a592-db6","ReleParam","targetEntityId");
		hbaseDao.delColumnByQualifier("Rele_Infos","e47715e0-ed94-457f-a592-db6","ReleParam","targetEventId");
	}
}

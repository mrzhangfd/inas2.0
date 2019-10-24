package com.sdu.inas;

import com.sdu.inas.beans.Event;
import com.sdu.inas.beans.HbaseModel;
import com.sdu.inas.beans.RealEntity;
import com.sdu.inas.controller.ExtractionController;
import com.sdu.inas.controller.ParamController;
import com.sdu.inas.repository.EventRepository;
import com.sdu.inas.repository.HbaseDao;
import com.sdu.inas.service.ObjectService;
import com.sdu.inas.util.HbaseModelUtil;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
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

    @Autowired
    ObjectService objectService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ExtractionController extractionController;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testDel() throws UnsupportedEncodingException {
        String ss = "曹操T3HRGAS9%%曹操";
        paramController.deleteParam(ss);
    }

    @Test
    public void findById() throws Exception {
        String objectId = "曹操T3HRGAS9";
        hbaseDao.getDataFromRowkey(HbaseModelUtil.BASICTABLE, objectId);
        Result result = hbaseDao.getDataFromRowkey(HbaseModelUtil.BASICTABLE, objectId);
        System.out.println("=================================================================================");
        System.out.println(result);
    }

    @Test
    public void deleteEventByEventId() {
        String eventId = "94be7609-5a60-4a14-923e-631d440aefb1";
        String eventId2 = "f1f05246-05b3-45c8-bafc-d2c65309ef62";

        eventRepository.delete(eventId);
        System.out.println("=================================================================================");
    }

    @Test
	public void insertObject(){
    	hbaseDao.insertData("Object","2","Eventlist","0202","event2",null);
	}

	@Test
    public void getEventList() throws InterruptedException {
        String rawInfo="0184，黄巾起义爆发，曹操成为骑都尉";
        extractionController.getExtractResult(rawInfo);
    }

    @Test
    public void deleteRele() {
        hbaseDao.delColumnByQualifier("Rele_Infos", "e47715e0-ed94-457f-a592-db6", "ReleParam", "description");
        hbaseDao.delColumnByQualifier("Rele_Infos", "e47715e0-ed94-457f-a592-db6", "ReleParam", "sourceEntityId");
        hbaseDao.delColumnByQualifier("Rele_Infos", "e47715e0-ed94-457f-a592-db6", "ReleParam", "sourceEventId");
        hbaseDao.delColumnByQualifier("Rele_Infos", "e47715e0-ed94-457f-a592-db6", "ReleParam", "targetEntityId");
        hbaseDao.delColumnByQualifier("Rele_Infos", "e47715e0-ed94-457f-a592-db6", "ReleParam", "targetEventId");
    }
}

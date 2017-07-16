package com.sinohealth.eszservice.service.visit.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sinohealth.eszorm.entity.visit.BodySignValueEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszservice.common.utils.DateUtils;
import com.sinohealth.eszservice.service.visit.IBodySignValueService;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;

@RunWith(SpringJUnit4ClassRunner.class)
// 使用junit4进行测试
@ContextConfiguration({ "/applicationContext.xml" })
// 加载配置文件
public class BodySignValueServiceImplTest {
	@Autowired
	IBodySignValueService service;

	@Test
	@Transactional
	// 标明此方法需使用事务
	@Rollback(true)
	// 标明使用完此方法后事务不回滚,true时为回滚
	public void testSaveDailyResult() throws ValueOutOfRangeException {
		int sickId = 10000001;

		Date reportDate = DateUtils.getDateStart(new Date());
		List<BodySignValueEntity> values = new ArrayList<>();
		BodySignValueEntity value0 = new BodySignValueEntity();
		VisitItemEntity item0 = new VisitItemEntity();
		item0.setItemId(1101);
		value0.setItem(item0);
		value0.setReportValue("100");
		values.add(value0);
		BodySignValueEntity value1 = new BodySignValueEntity();
		VisitItemEntity item1 = new VisitItemEntity();
		item1.setItemId(1102);
		value1.setItem(item1);
		value1.setReportValue("100");
		values.add(value1);
		BodySignValueEntity value2 = new BodySignValueEntity();
		VisitItemEntity item2 = new VisitItemEntity();
		item2.setItemId(1103);
		value2.setItem(item2);
		value2.setReportValue("150");
		values.add(value2);

		service.saveBodySignValues(sickId, reportDate, values);

		// 获取
	}

}

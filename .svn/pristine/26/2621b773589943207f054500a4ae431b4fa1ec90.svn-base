package com.sinohealth.eszservice.service.visit.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinohealth.eszorm.entity.visit.VisitImgEntity;
import com.sinohealth.eszorm.entity.visit.VisitItemEntity;
import com.sinohealth.eszorm.entity.visit.props.RadioCascadeOptions;
import com.sinohealth.eszorm.entity.visit.props.RadioCascadeOptions.SecondOption;
import com.sinohealth.eszservice.common.config.Global;
import com.sinohealth.eszservice.common.persistence.Parameter;
import com.sinohealth.eszservice.dao.visit.IVisitItemDao;
import com.sinohealth.eszservice.service.visit.IVisitItemService;
import com.sinohealth.eszservice.service.visit.exception.StringValueLengthException;
import com.sinohealth.eszservice.service.visit.exception.ValueOutOfRangeException;

@Service
public class VisitItemServiceImpl implements IVisitItemService {
	private final Logger logger = LoggerFactory
			.getLogger(VisitItemServiceImpl.class);

	static ObjectMapper mapper = new ObjectMapper();

	/**
	 * = 一个等号指两个数字相等
	 */
	public final static String OP_EQ = "=";
	public final static String OP_GRATER_THAN = ">";
	public final static String OP_GRATER_OR_EQ = ">=";
	public final static String OP_LESS_THAN = "<";
	public final static String OP_LESS_OR_EQ = "<=";
	public final static String OP_BETWEEN = "between";
	/**
	 * “eq”指两个字符串相同
	 */
	public final static String OP_SAME = "eq";

	@Autowired
	IVisitItemDao dao;

	@Autowired
	private RedisTemplate<String, VisitItemEntity> redisTemplate;

	private BoundHashOperations<String, Integer, VisitItemEntity> ops;

	@Override
	public int getWarnLevel(Integer itemId, String value, Integer sex)
			throws ValueOutOfRangeException {
		if ((0 == itemId) || (null == value) || ("".endsWith(value))) {
			return 0;
		}
		VisitItemEntity item = get(itemId);
		if (null == item) {
			return 0;
		}
		if (0 == item.getType()) { // 0为数值类型
			return getWarnLevelForNumber(item, value, sex);
		}
		return 0;
	}

	public int getWarnLevelForNumber(VisitItemEntity item, String value,
			Integer sex) throws ValueOutOfRangeException {
		sex = (null == sex) || (sex > 2) || (sex < 0) ? 2 : sex.intValue();
		// System.err.println("itemId:"+itemId+" value:"+value);
		// 判断男或女血脂（高密度脂蛋白）是否超标
		Class<? extends VisitItemEntity> cls = item.getClass();
		for (int i = 1; i < 6; i++) { // 一共5项
			try {
				Method opMethod = cls.getMethod("getOp" + i);
				if (null == opMethod) {
					break;// 如果没有操作符，后台的都不处理了
				}
				Method sexMethod = cls.getMethod("getSex" + i);
				Object object = sexMethod.invoke(item);
				int nSex = null == object ? 2 : (int) object; // 如果没有设置性别值值，就认为是2-不限制
				if ((nSex != 2) && (nSex != sex)) { // 如果不是2，就说明此项是限制性别的
					continue;
				}
				Method minMethod = cls.getMethod("getMinValue" + i);
				Method maxMethod = cls.getMethod("getMaxValue" + i);
				if (1 == compare((String) opMethod.invoke(item), value,
						(String) minMethod.invoke(item),
						(String) maxMethod.invoke(item))) {
					Method levelMethod = cls.getMethod("getWarnLevel" + i);
					return (int) levelMethod.invoke(item);
				}
			} catch (NoSuchMethodException | SecurityException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// 找不到对应的方法
				e.printStackTrace();
			}
		}

		throw new ValueOutOfRangeException(item.getItemId(), value);
	}

	/**
	 * 当value在计算符号op计算的值min和max范围内返回1，不在则返回0。返回-1表示计算符号错误
	 * 
	 * @param value
	 *            值
	 * @param op
	 *            计算符号
	 * @param min
	 *            小端
	 * @param max
	 *            大端
	 */
	private int compare(String op, String value, String min, String max) {
		int res = -1;
		if (OP_EQ.equals(op) || OP_GRATER_THAN.equals(op)
				|| OP_GRATER_OR_EQ.equals(op) || OP_LESS_THAN.equals(op)
				|| OP_LESS_OR_EQ.equals(op)) {
			try {
				double v = Double.parseDouble(value);
				double m = Double.parseDouble(min);
				res = compare(op, v, m);
			} catch (NumberFormatException e) {
				return res;
			}
		} else if (OP_BETWEEN.equals(op)) {
			try {
				double v = Double.parseDouble(value);
				double mn = Double.parseDouble(min);
				double mx = Double.parseDouble(max);
				res = compare(op, v, mn, mx);
			} catch (NumberFormatException e) {
				return res;
			}
		} else if (OP_SAME.equals(op)) {
			res = (op.equals(min) || op.equals(max)) ? 1 : 0;
		}
		return res;
	}

	/**
	 * 返回整数，1为value比another与op比较数合适，0指不合适，-1指计算符op输入错误
	 * 
	 * @param op
	 * @param value
	 * @param another
	 * @return
	 */
	private int compare(String op, double value, double another) {
		int res = -1;

		switch (op) {
		case OP_EQ:
			res = (value == another) ? 1 : 0;
			break;
		case OP_GRATER_THAN:
			res = (value > another) ? 1 : 0;
			break;
		case OP_LESS_THAN:
			res = (value < another) ? 1 : 0;
			break;
		case OP_GRATER_OR_EQ:
			res = (value >= another) ? 1 : 0;
			break;
		case OP_LESS_OR_EQ:
			res = (value <= another) ? 1 : 0;
			break;
		}

		return res;
	}

	/**
	 * 返回整数，1为value比another与op比较数合适，0指不合适，-1指计算符op输入错误
	 * 
	 * @param op
	 * @param value
	 * @param another
	 * @return
	 */
	private int compare(String op, double value, double min, double max) {
		int res = -1;
		switch (op) {
		case OP_BETWEEN:
			res = ((value >= min) && (value <= max)) ? 1 : 0;
			break;
		}
		return res;
	}

	/**
	 * 获取项目，做了redis缓存
	 * 
	 */
	@Override
	public VisitItemEntity get(int itemId) {
		return dao.get(itemId);
	}

	/**
	 * 获取项目，做了redis缓存
	 * 
	 */
	@Override
	public VisitItemEntity get(int itemId, boolean cache) {
		VisitItemEntity o = null;
		if (cache) {
			if (ops().hasKey(itemId)) {
				o = ops().get(itemId);
			} else {
				o = dao.get(itemId);
				ops().put(itemId, o);
			}
		} else {
			o = get(itemId);
		}
		return o;
	}

	private BoundHashOperations<String, Integer, VisitItemEntity> ops() {
		if (null == ops) {
			ops = redisTemplate.boundHashOps(redisKey());
			int expiredTime = Integer.parseInt(Global
					.getConfig("visitItem.data.cache.expired"));
			if (0 == expiredTime) {
				expiredTime = 1;
			}
			ops.expire(expiredTime, TimeUnit.HOURS);
		}
		return ops;
	}

	private String redisKey() {
		return "table:visitItem";
	}

	@Override
	public List<VisitItemEntity> getItemsByPhase(Integer templPhaseId) {
		Parameter params = new Parameter(templPhaseId);

		return dao
				.findByHql(
						"SELECT vie FROM VisitItemEntity  vie JOIN vie.phases phase WHERE phase.templPhaseId=:p1",
						params);
	}

	/**
	 * 判断Set<VisitImgEntity> items中是否同时存在"检查" 与 "检验"项， 返回值为true同时存在，反之不同时存在
	 * 
	 * @param items
	 * @return
	 */
	public boolean isCheckItem(Set<VisitImgEntity> items) {
		if (null == items) {
			return false;
		}
		boolean isCheckItem = false;
		boolean checkFlag = false;
		boolean checkItemFlag = false;
		for (VisitImgEntity visitImg : items) {
			if (String.valueOf(visitImg.getItemId()).startsWith("13")) {
				if (null != visitImg) {
					checkItemFlag = true;
				}
			}
			if (String.valueOf(visitImg.getItemId()).startsWith("14")) {
				if (null != visitImg) {
					checkFlag = true;
				}
			}
		}
		if (checkFlag && checkItemFlag) {
			isCheckItem = true;
		}
		return isCheckItem;
	}

	/**
	 * 检验输入的值
	 * 
	 * @param item
	 * @param value
	 */
	@Override
	public void validate(int itemId, String value) {
		VisitItemEntity item = get(itemId, true);
		if (item.getType() == VisitItemEntity.TYPE_TEXT) { // 如果是文本输入类型
			validateCharValue(item, value);
		}
	}

	/**
	 * 检验输入的值
	 * 
	 * @param item
	 * @param value
	 */
	@Override
	public void validate(VisitItemEntity item, String value) {
		if (item.getType() == VisitItemEntity.TYPE_TEXT) { // 如果是文本输入类型
			validateCharValue(item, value);
		}
	}

	/**
	 * 检验输入的字符串值
	 * 
	 * @param item
	 * @param value
	 * @throws StringValueLengthException
	 */
	public void validateCharValue(VisitItemEntity item, String value) {
		int len = value.length();
		try {
			// 只判断介于操作。
			if (OP_BETWEEN.equals(item.getOp1())) {
				if ((null != item.getMinValue1())
						&& (null != item.getMaxValue1())) {
					int min = Integer.parseInt(item.getMinValue1());
					int max = Integer.parseInt(item.getMaxValue1());

					if ((len > max) || (len < min)) {
						throw new StringValueLengthException(item, value, min,
								max);
					}
				}
			}
		} catch (NumberFormatException e) {
			// 无法转为数字
		}

	}

	@Override
	public List<VisitItemEntity> getListByDiseaseId(String diseaseId,
			int... visitItemCat) {
		StringBuffer hql = new StringBuffer(
				"SELECT vie FROM VisitItemEntity  vie JOIN vie.diseaseEntitys disease WHERE disease.id=:diseaseId");

		Parameter params = new Parameter();
		params.put("diseaseId", diseaseId);

		if ((null != visitItemCat) && (visitItemCat.length > 0)) {
			hql.append(" AND cat IN  ( ");
			for (int i = 0; i < visitItemCat.length; i++) {
				if (i != 0) {
					hql.append(",");
				}
				hql.append(visitItemCat[i]);
			}
			hql.append("  ) ");
		}

		return dao.findByHql(hql.toString(), params);
	}

	@Override
	public List<VisitItemEntity> getListByTemplAndCat(int templId, int cat) {
		String sql = "SELECT item  FROM VisitItemEntity item where item.itemId in ( SELECT bs.item.itemId from  BodySignEntity bs where bs.template.templId=:templId and bs.cat=:cat )";
		Parameter params = new Parameter();
		params.put("templId", templId);
		params.put("cat", cat);
		return dao.findByHql(sql, params);
	}

	@Override
	public List<VisitItemEntity> getListByItemList(Integer[] items) {
		String s_item = "";
		for (int i = 0; i < items.length; i++) {
			if (i < items.length - 1) {
				s_item = s_item + items[i].intValue() + ",";
			} else {
				s_item = s_item + items[i].intValue();
			}
		}
		String sql = "SELECT item  FROM VisitItemEntity item where item.itemId in ("
				+ s_item + ")";
		return dao.findByHql(sql, new Parameter());
	}

	@Override
	public String getRadioCascadeText(String value, String value2, int itemId) {
		VisitItemEntity item = get(itemId); // TODO 缓存options
		if (!StringUtils.hasLength(value)) {
			return "";
		}

		Assert.isTrue(item.getType() == 10,
				String.format("itemId:%d，不是单选级联类型", itemId));

		StringBuffer rs = new StringBuffer();

		String options = item.getOptions();

		RadioCascadeOptions opts;
		try {
			opts = mapper.readValue(options, RadioCascadeOptions.class);
		} catch (IOException e) {
			logger.error("itemId:{}的options参数错误:{}", itemId, options);
			throw new RuntimeException();
		}

		List<String> value2List = new ArrayList<>();
		if (StringUtils.hasLength(value2)) {
			if (opts.getType() == 2) {
				value2List.add(value2);
			} else if (opts.getType() == 3) {
				String[] value2Arr = value2.split(",");
				if (null != value2Arr) {
					value2List.addAll(Arrays.asList(value2Arr));
				}
			}
		}

		for (RadioCascadeOptions.Option it : opts.getList()) {

			if (value.equals(it.getValue())) { // 一级选项的title

				rs.append(it.getTitle());

				StringBuffer text = new StringBuffer();
				if ((opts.getType() == 2) || (opts.getType() == 3)) { // 如果是选项或复选类型
					for (SecondOption it2 : it.getOptions().getList()) {
						int idx = value2List.indexOf(it2.getValue());
						if (idx != -1) { // 找到了项目
							if (text.length() > 0) {
								text.append("、");
							}
							text.append(it2.getTitle());
						}
					}
				} else {
					text.append(value2);
				}
				if (text.length() > 0) {
					rs.append("，");
					rs.append(text);
				}

				break;
			}
		}

		return rs.toString();
	}
}

package com.sinohealth.eszservice.service.help;
/**
 * 问题反馈
 * @author 黄洪根
 *
 */
public interface IQuestionService {
	/**
	 * 获取单条问题内容
	 * @param questionId 问题ID
	 * @return
	 */
	String getQuestionContent(Integer id);

	/**
	 * 获取问题列表
	 * @param CategoryId 栏目ID
	 * @param isOften    是否是常见问题(0 - 否,1 - 是)
	 * @return
	 */
	String getQuestionList(String appName,Integer ver);
	String getQuestionList(String appName,String isOften,Integer ver);
}

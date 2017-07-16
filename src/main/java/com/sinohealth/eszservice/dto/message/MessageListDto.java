package com.sinohealth.eszservice.dto.message;

import java.util.ArrayList;
import java.util.List;

import com.sinohealth.eszorm.message.MessageEntity;
import com.sinohealth.eszservice.common.dto.BaseDto;

/**
 * 消息列表DTO
 * 
 * @author 黄世莲
 * 
 */
public class MessageListDto extends BaseDto {

	private static final long serialVersionUID = -8800951900889677978L;

	private MessageElem message = new MessageElem();

	public MessageElem getMessage() {
		return message;
	}

	public void setMessage(MessageElem message) {
		this.message = message;
	}

	public final class MessageElem {

		private int lastPage = 0;

		private List<MessageEntity> list = new ArrayList<>();

		public int getLastPage() {
			return lastPage;
		}

		public void setLastPage(int lastPage) {
			this.lastPage = lastPage;
		}

		public List<MessageEntity> getList() {
			return list;
		}

		public void setList(List<MessageEntity> list) {
			this.list = list;
		}

	}

}

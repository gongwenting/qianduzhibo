package com.qiandu.live.model;

/**
 * @description: 消息体类
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class ChatEntity {
	
	private String grpSendName;
	private String context;
	private String id;
	private int  type;

	public ChatEntity() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSenderName() {
		return grpSendName;
	}

	public void setSenderName(String grpSendName) {
		this.grpSendName = grpSendName;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ChatEntity)) return false;

		ChatEntity that = (ChatEntity) o;

		if (getType() != that.getType()) return false;
		if (grpSendName != null ? !grpSendName.equals(that.grpSendName) : that.grpSendName != null)
			return false;
		return getContext() != null ? getContext().equals(that.getContext()) : that.getContext() == null;

	}

	@Override
	public int hashCode() {
		int result = grpSendName != null ? grpSendName.hashCode() : 0;
		result = 31 * result + (getContext() != null ? getContext().hashCode() : 0);
		result = 31 * result + getType();
		return result;
	}
}

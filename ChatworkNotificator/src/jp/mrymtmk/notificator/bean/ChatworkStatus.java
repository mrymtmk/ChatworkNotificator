package jp.mrymtmk.notificator.bean;

import jp.mrymtmk.notificator.util.PropertyUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class ChatworkStatus implements ChatworkInfo {

	private static final String UNREAD_KEY;
	private static final String MENTION_KEY;
	private static final String MYTASK_KEY;
	private static final String ERROR_KEY;

	static {
		UNREAD_KEY = PropertyUtil.getProperty("json.key.my.unread");
		MENTION_KEY = PropertyUtil.getProperty("json.key.my.mention");
		MYTASK_KEY = PropertyUtil.getProperty("json.key.my.mytask");
		ERROR_KEY = PropertyUtil.getProperty("json.key.errors");
	}

	private int unread = -1;
	private int mention = -1;
	private int mytask = -1;
	private String errorMsg = null;
	private boolean failed = false;

	@Override
	public void fromJSON(JsonNode json) {
		if (!json.has(ERROR_KEY)) {
			unread = json.get(UNREAD_KEY).intValue();
			mention = json.get(MENTION_KEY).intValue();
			mytask = json.get(MYTASK_KEY).intValue();
			failed = false;
		} else {
			errorMsg = json.get(ERROR_KEY).textValue();
			failed = true;
		}
	}

	@Override
	public boolean failed() {
		return failed;
	}

	/**
	 * @return unread
	 */
	public int getUnread() {
		return unread;
	}

	/**
	 * @return mention
	 */
	public int getMention() {
		return mention;
	}

	/**
	 * @return mytask
	 */
	public int getMytask() {
		return mytask;
	}

	/**
	 * @return errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @return failed
	 */
	public boolean isFailed() {
		return failed;
	}
}

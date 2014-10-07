package jp.mrymtmk.notificator.bean;

import jp.mrymtmk.notificator.util.PropertyUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class ChatworkUserInfo implements ChatworkInfo {

	private static final String USERNAME_KEY;
	private static final String USERIMG_KEY;
	private static final String ERROR_KEY;

	static {
		USERNAME_KEY = PropertyUtil.getProperty("json.key.me.name");
		USERIMG_KEY = PropertyUtil.getProperty("json.key.me.image");
		ERROR_KEY = PropertyUtil.getProperty("json.key.errors");
	}

	private String username = null;
	private String userimg = null;
	private String errorMsg = null;
	private boolean failed = false;

	@Override
	public void fromJSON(JsonNode json) {
		if (!json.has(ERROR_KEY)) {
			username = json.get(USERNAME_KEY).textValue();
			userimg = json.get(USERIMG_KEY).textValue();
			failed = false;
		} else {
			errorMsg = json.get(ERROR_KEY).get(0).textValue();
			failed = true;
		}
	}

	@Override
	public boolean failed() {
		return failed;
	}

	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return userimg
	 */
	public String getUserimg() {
		return userimg;
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

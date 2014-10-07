package jp.mrymtmk.notificator.bean;

import com.fasterxml.jackson.databind.JsonNode;

public interface ChatworkInfo {

	/**
	 * ここで各インスタンスのメンバに値をセットする
	 *
	 * @param json
	 *            JSON形式の応答文字列
	 */
	public void fromJSON(JsonNode json);

	/**
	 * 失敗したかどうか
	 *
	 * @return 失敗したかどうか
	 */
	public boolean failed();
}

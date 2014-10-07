package jp.mrymtmk.notificator.event;

import jp.mrymtmk.notificator.bean.ChatworkInfo;

import com.fasterxml.jackson.databind.JsonNode;

public class ReceiveEvent<T extends ChatworkInfo> {

	/**
	 * 戻り値情報
	 */
	private T info = null;

	/**
	 * 発生元
	 */
	private Object source = null;

	/**
	 * 応答時イベント
	 * @param source
	 * @param json
	 * @param clazz
	 */
	public ReceiveEvent(Object source, JsonNode json, Class<T> clazz) {
		try {
			info = clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("インスタンス化失敗", e);
		}
		this.source = source;
		info.fromJSON(json);
	}

	/**
	 * 応答結果を取得する
	 * @return
	 */
	public T getInfo() {
		return info;
	}

	/**
	 * @return source
	 */
	public Object getSource() {
		return source;
	}
}

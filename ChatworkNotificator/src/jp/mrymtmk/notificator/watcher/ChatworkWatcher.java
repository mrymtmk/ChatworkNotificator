package jp.mrymtmk.notificator.watcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.mrymtmk.notificator.bean.ChatworkInfo;
import jp.mrymtmk.notificator.event.ReceiveEvent;
import jp.mrymtmk.notificator.event.listener.ReceiveEventListener;
import jp.mrymtmk.notificator.util.PropertyUtil;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ChatworkWatcher<T extends ChatworkInfo> implements Runnable {

	/**
	 * ベースURL
	 */
	protected static final String CHATWORK_BASE_URL;

	/**
	 * APIキー
	 */
	protected static final String CHATWORK_API_KEY;

	/**
	 * リクエストヘッダキー(APIキー)
	 */
	protected static final String CHATWORK_HTTP_REQEST_HEADER_API_KEY;

	/**
	 * ウォッチに失敗したかどうか
	 */
	private boolean failed = false;

	/**
	 * リスナ
	 */
	private ReceiveEventListener<T> listner = null;

	static {
		CHATWORK_BASE_URL = PropertyUtil.getProperty("chatwork.api.url");
		CHATWORK_API_KEY = PropertyUtil.getProperty("chatwork.api.key");
		CHATWORK_HTTP_REQEST_HEADER_API_KEY = PropertyUtil.getProperty("chatwork.api.http.key");
	}

	/**
	 * コンストラクタ
	 *
	 * @param listner
	 *            リスナ
	 */
	public ChatworkWatcher(ReceiveEventListener<T> listner) {
		this.listner = listner;
	}

	/**
	 * リクエストURLを構築する
	 *
	 * @return リクエストURL
	 */
	private String createRequestURL() {
		StringBuilder url = new StringBuilder();
		url.append(CHATWORK_BASE_URL).append(this.getURI());
		url.append(this.appendToURL());
		return url.toString();
	}

	/**
	 * リクエストを送信して結果を文字列で返す
	 *
	 * @param method
	 *            リクエストメソッド
	 * @return 結果文字列
	 */
	private String getResult(String method) {
		StringBuilder result = new StringBuilder();

		try {
			URL url = new URL(this.createRequestURL());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty(CHATWORK_HTTP_REQEST_HEADER_API_KEY, CHATWORK_API_KEY);
			connection.setRequestMethod(method);
			connection.setConnectTimeout(PropertyUtil.getPropertyInt("http.connection.timeout"));
			connection.connect();

			// HTTPレスポンス取得
			BufferedReader reader;
			if (200 == connection.getResponseCode()) {
				this.failed = false;
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} else {
				this.failed = true;
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			}
			String line = "";
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			reader.close();
			connection.disconnect();
		} catch (Exception e) {
			throw new RuntimeException("リクエスト失敗", e);
		}

		return result.toString();
	}

	/**
	 * GETメソッドの実行
	 *
	 * @return 結果のJsonNode
	 */
	protected JsonNode doGet() {

		String jsonString = this.getResult("GET");
		return this.fromJson(jsonString);
	}

	/**
	 * POSTメソッドの実行
	 *
	 * @return 結果のJsonNode
	 */
	protected JsonNode doPost() {

		// 未実装
		return null;
	}

	/**
	 * Json文字列をJsonNodeへ変換
	 *
	 * @param jsonString
	 *            Json文字列
	 * @return 変換後のJsonNode
	 */
	private JsonNode fromJson(String jsonString) {
		JsonNode head = null;
		try {
			JsonFactory jfactory = new JsonFactory();
			JsonParser parser = jfactory.createParser(jsonString);
			ObjectMapper mapper = new ObjectMapper();
			head = mapper.readTree(parser);
		} catch (Exception e) {
			throw new RuntimeException("Json変換失敗", e);
		}

		return head;
	}

	/**
	 * イベントの発火(リスナへのアクセサ)
	 *
	 * @param e
	 *            イベント
	 */
	protected void fireReceiveEvent(ReceiveEvent<T> e) {
		listner.onReceive(e);
	}

	/**
	 * エラーイベントの発火(リスナへのアクセサ)
	 *
	 * @param e
	 *            イベント
	 */
	protected void fireErrorevent(ReceiveEvent<T> e) {
		listner.onError(e);
	}

	/**
	 * URL末尾へURLを追加
	 *
	 * @return 追加する文字列
	 */
	protected String appendToURL() {
		return "";
	}

	/**
	 * @return failed
	 */
	public boolean isFailed() {
		return failed;
	}

	/**
	 * @param failed
	 *            セットする failed
	 */
	protected void setFailed(boolean failed) {
		this.failed = failed;
	}

	/**
	 * タスクごとのURIを返す
	 *
	 * @return タスク用のURI
	 */
	protected abstract String getURI();
}

package jp.mrymtmk.notificator.util;

import java.io.IOException;
import java.util.Properties;

/**
 * プロパティラッパー
 *
 * @author mrymtmk
 *
 */
public class PropertyUtil {

	/**
	 * プロパティクラス
	 */
	private static volatile Properties props = null;

	/**
	 * コンストラクタ newの禁止
	 */
	private PropertyUtil() {
	}

	/**
	 * プロパティを呼ばれた最初のみ初期化
	 * @throws IOException リソースの読み込みに失敗した場合
	 */
	private static synchronized void initProps() throws IOException {
		if (null == props) {
			props = new Properties();
			props.load(props.getClass().getResourceAsStream("/app.properties"));
		}
	}

	/**
	 * プロパティを取得
	 * @param key プロパティのキー
	 * @return 取得された値
	 */
	public static String getProperty(String key) {
		try {
			initProps();
			return props.getProperty(key);
		} catch (IOException e) {
			throw new RuntimeException("取得失敗", e);
		}
	}

	/**
	 * プロパティをIntで取得
	 * @param key プロパティのキー
	 * @return 取得された値
	 */
	public static int getPropertyInt(String key) {
		try {
			initProps();
			return Integer.parseInt(props.getProperty(key));
		} catch (IOException | NumberFormatException e) {
			throw new RuntimeException("取得失敗", e);
		}
	}

	/**
	 * プロパティに値を設定
	 * @param key プロパティのキー
	 * @param value 設定する値
	 * @return キーに対する以前の値、なかった場合はnull
	 */
	public static Object setProperty(String key, Object value) {
		try {
			initProps();
			return props.setProperty(key, String.valueOf(value));
		} catch (IOException e) {
			throw new RuntimeException("セット失敗", e);
		}
	}
}

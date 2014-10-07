package jp.mrymtmk.notificator.main;


/**
 * アプリケーションメイン
 *
 * @author mrymtmk
 *
 */
public class Messanger {

	/**
	 * メインエントリポイント
	 *
	 * @param args
	 *            実行時引数
	 */
	public static void main(String[] args) {

		try {
			// アイコン
			new TaskTrayWorker("/icon16.png");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

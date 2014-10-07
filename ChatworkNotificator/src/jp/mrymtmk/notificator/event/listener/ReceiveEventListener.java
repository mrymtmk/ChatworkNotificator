package jp.mrymtmk.notificator.event.listener;

import java.util.EventListener;

import jp.mrymtmk.notificator.bean.ChatworkInfo;
import jp.mrymtmk.notificator.event.ReceiveEvent;

public interface ReceiveEventListener<T extends ChatworkInfo> extends EventListener {

	/**
	 * Recieveイベント
	 *
	 * @param e イベント
	 */
	public void onReceive(ReceiveEvent<T> e);

	/**
	 * エラー時イベント
	 *
	 * @param e イベント
	 */
	public void onError(ReceiveEvent<T> e);
}

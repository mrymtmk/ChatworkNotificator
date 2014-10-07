package jp.mrymtmk.notificator.main;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;

import jp.mrymtmk.notificator.bean.ChatworkStatus;
import jp.mrymtmk.notificator.bean.ChatworkUserInfo;
import jp.mrymtmk.notificator.event.CloseEvent;
import jp.mrymtmk.notificator.event.ReceiveEvent;
import jp.mrymtmk.notificator.event.listener.CloseEventListener;
import jp.mrymtmk.notificator.event.listener.ReceiveEventListener;
import jp.mrymtmk.notificator.frame.SettingsFrame;
import jp.mrymtmk.notificator.util.PropertyUtil;
import jp.mrymtmk.notificator.watcher.ChatworkWatchWorker;
import jp.mrymtmk.notificator.watcher.MyStatusWatcher;
import jp.mrymtmk.notificator.watcher.UserInfoWatcher;

/**
 * タスクトレイ常駐監視クラス
 */
public class TaskTrayWorker {

	/**
	 *
	 * @throws IOException
	 * @throws AWTException
	 * @throws InterruptedException
	 */
	public TaskTrayWorker(String iconPath) throws IOException {

		Image image = ImageIO.read(this.getClass().getResourceAsStream(iconPath));
		final TrayIcon icon = new TrayIcon(image);

		try {
			// 起動
			this.run(icon);

		} catch (Exception e) {
			e.printStackTrace();
			SystemTray.getSystemTray().remove(icon);
			System.exit(-1);
		}
	}

	/**
	 *
	 * @param icon
	 * @throws Exception
	 */
	public void run(final TrayIcon icon) {

		// 実行サービス
		final ChatworkWatchWorker worker = new ChatworkWatchWorker();

		// ユーザー情報用リスナ
		final ReceiveEventListener<ChatworkUserInfo> loginListner = new ReceiveEventListener<ChatworkUserInfo>() {

			@Override
			public void onReceive(ReceiveEvent<ChatworkUserInfo> e) {
				icon.displayMessage("Chatwork", "ようこそ! " + e.getInfo().getUsername(), MessageType.NONE);
			}

			@Override
			public void onError(ReceiveEvent<ChatworkUserInfo> e) {
				icon.displayMessage("ログインに失敗しました。", e.getInfo().getErrorMsg(), MessageType.ERROR);
			}
		};

		// ステータス取得用リスナ
		final ReceiveEventListener<ChatworkStatus> statusListener = new ReceiveEventListener<ChatworkStatus>() {

			@Override
			public void onReceive(ReceiveEvent<ChatworkStatus> e) {
				ChatworkStatus status = e.getInfo();

				if (0 < status.getUnread() + status.getMention()) {
					icon.displayMessage(
							"未読メッセージがあるようです。",
							String.format("未読：%d 未読To：%d", status.getUnread(), status.getMention()), MessageType.INFO);
				}

				// 取得に成功したら、しかるべきのちにステータスを取りに行く
				worker.entryWatcher(new MyStatusWatcher(this));
				if (!worker.isAlive()) {
					worker.start();
				}
			}

			@Override
			public void onError(ReceiveEvent<ChatworkStatus> e) {
				icon.displayMessage("ステータスの取得に失敗しました。", e.getInfo().getErrorMsg(), MessageType.ERROR);
			}
		};

		// 画面クローズリスナ
		final CloseEventListener closeListner = new CloseEventListener() {

			@Override
			public void onClose(CloseEvent e) {
				worker.entryWatcher(new MyStatusWatcher(statusListener));
				if (!worker.isAlive()) {
					worker.start();
				}
			}
		};
		// メニュー
		PopupMenu menu = new PopupMenu();
		MenuItem gotoCW = new MenuItem("Go to ChatWork");
		gotoCW.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ブラウザOpen
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URI(PropertyUtil.getProperty("chatwork.home.url")));
				} catch (Exception e1) {
					// Open失敗
					icon.displayMessage("エラー", "ブラウザを起動できませんでした。", MessageType.ERROR);
				}
			}
		});
		MenuItem settings = new MenuItem("settings");
		settings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 開く前にstop
				worker.tryStop();

				// 設定画面開く
				SettingsFrame.run(closeListner);
			}
		});
		MenuItem exit = new MenuItem("close");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 開く前にstop
				worker.tryStop();

				// 閉じる
				SystemTray.getSystemTray().remove(icon);
				System.exit(0);
			}
		});

		// アイテム追加
		menu.add(gotoCW);
		menu.add(settings);
		menu.add(exit);
		icon.setPopupMenu(menu);
		icon.setToolTip("ChatworkMessenger");

		// タスクトレイ格納
		try {
			SystemTray.getSystemTray().add(icon);
		} catch (AWTException e) {
			throw new RuntimeException("タスクトレイ格納失敗", e);
		}

		// ログイン
		UserInfoWatcher userInfo = new UserInfoWatcher(loginListner);
		userInfo.run();
		if (userInfo.isFailed()) {

			// ステータス取りに行かないで設定画面開く
			SettingsFrame.run(closeListner);
			return;
		}

		// ステータス取得
		worker.entryWatcher(new MyStatusWatcher(statusListener));
		worker.start();
	}
}

package jp.mrymtmk.notificator.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jp.mrymtmk.notificator.event.CloseEvent;
import jp.mrymtmk.notificator.event.listener.CloseEventListener;
import jp.mrymtmk.notificator.util.PropertyUtil;

import org.eclipse.wb.swing.FocusTraversalOnArray;

/**
 * 設定画面クラス
 * 基本的にデザイナで作成
 */
public class SettingsFrame extends JFrame {
	private CloseEventListener listner;
	private JTextField apiKeyValue;
	private JTextField intervalValue;
	private JTextField timeoutValue;
	private JTextField connectToValue;

	private static SettingsFrame frame = null;

	/**
	 * Launch the application.
	 */
	public static void run(final CloseEventListener listner) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new SettingsFrame();
					frame.listner = listner;
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException
	 */
	private SettingsFrame() throws IOException {
		final JFrame window = this;
		setType(Type.POPUP);
		setResizable(false);
		getContentPane().setBackground(SystemColor.window);
		setFont(new Font("Meiryo UI", Font.PLAIN, 14));
		setForeground(SystemColor.window);
		setBackground(SystemColor.window);
		setTitle("Settings");
		setIconImage(ImageIO.read(this.getClass().getResourceAsStream("/icon16.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		getContentPane().setLayout(null);

		final JLabel lblAboutAPI = new JLabel("Chatwork API ?");
		lblAboutAPI.setHorizontalAlignment(SwingConstants.CENTER);
		lblAboutAPI.setForeground(new Color(0, 0, 255));
		lblAboutAPI.setFont(new Font("Meiryo UI", Font.BOLD, 12));
		lblAboutAPI.setBounds(291, 20, 141, 16);
		lblAboutAPI.setFocusable(true);
		lblAboutAPI.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblAboutAPI.setForeground(new Color(0, 0, 255));
				lblAboutAPI.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblAboutAPI.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				lblAboutAPI.setForeground(new Color(0, 192, 255));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// ブラウザOpen
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URI(PropertyUtil.getProperty("chatwork.about.api.url")));
				} catch (Exception ex) {
					throw new RuntimeException("ブラウザ起動失敗", ex);
				}
			}
		});
		getContentPane().add(lblAboutAPI);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBorderPainted(true);
		btnCancel.setFocusPainted(false);
		btnCancel.setContentAreaFilled(false);
		btnCancel.setOpaque(false);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.dispose();
				listner.onClose(new CloseEvent(this));
			}
		});
		btnCancel.setForeground(new Color(25, 25, 112));
		btnCancel.setFont(new Font("Meiryo UI", Font.BOLD, 15));
		btnCancel.setBounds(68, 285, 120, 66);
		getContentPane().add(btnCancel);

		JButton btnOK = new JButton("Save");
		btnOK.setBorderPainted(true);
		btnOK.setFocusPainted(false);
		btnOK.setContentAreaFilled(false);
		btnOK.setOpaque(false);
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				PropertyUtil.setProperty("chatwork.api.key", apiKeyValue.getText());
				int interval = Integer.valueOf(intervalValue.getText()) * 1000;
				PropertyUtil.setProperty("sleep.interval.ms", interval);
				int timeout = Integer.valueOf(timeoutValue.getText()) * 1000;
				PropertyUtil.setProperty("http.connection.timeout", timeout);

				window.dispose();
				listner.onClose(new CloseEvent(this));
			}
		});
		btnOK.setForeground(new Color(25, 25, 112));
		btnOK.setFont(new Font("Meiryo UI", Font.BOLD, 15));
		btnOK.setBounds(256, 285, 120, 66);
		getContentPane().add(btnOK);

		JPanel apiKey = new JPanel();
		apiKey.setLayout(null);
		apiKey.setBackground(SystemColor.window);
		apiKey.setBounds(12, 36, 420, 50);
		getContentPane().add(apiKey);

		JLabel lblApiKey = new JLabel("API KEY");
		lblApiKey.setForeground(new Color(25, 25, 112));
		lblApiKey.setFont(new Font("Meiryo UI", Font.BOLD, 14));
		lblApiKey.setBounds(12, 10, 66, 30);
		apiKey.add(lblApiKey);

		apiKeyValue = new JTextField();
		apiKeyValue.setFont(new Font("Meiryo UI", Font.PLAIN, 13));
		apiKeyValue.setBounds(121, 10, 287, 30);
		apiKeyValue.setColumns(10);
		apiKeyValue.setText(PropertyUtil.getProperty("chatwork.api.key"));
		apiKey.add(apiKeyValue);

		JLabel require = new JLabel("*");
		require.setFont(new Font("Meiryo UI", Font.BOLD, 13));
		require.setForeground(new Color(255, 69, 0));
		require.setBounds(74, 17, 16, 17);
		apiKey.add(require);

		JPanel interval = new JPanel();
		interval.setLayout(null);
		interval.setBackground(Color.WHITE);
		interval.setBounds(12, 85, 420, 68);
		getContentPane().add(interval);

		JLabel lblInterval = new JLabel("チェック間隔(秒)");
		lblInterval.setForeground(new Color(25, 25, 112));
		lblInterval.setFont(new Font("Meiryo UI", Font.BOLD, 14));
		lblInterval.setBounds(12, 10, 104, 30);
		interval.add(lblInterval);

		intervalValue = new JTextField();
		intervalValue.setFont(new Font("Meiryo UI", Font.PLAIN, 13));
		intervalValue.setColumns(10);
		intervalValue.setBounds(121, 10, 287, 30);
		int interval_ms = PropertyUtil.getPropertyInt("sleep.interval.ms");
		intervalValue.setText(String.valueOf(interval_ms / 1000));
		interval.add(intervalValue);

		JPanel attention = new JPanel();
		FlowLayout flowLayout = (FlowLayout) attention.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		attention.setBackground(SystemColor.window);
		attention.setBounds(121, 39, 287, 22);
		interval.add(attention);

		JLabel lblAttention0 = new JLabel("※");
		lblAttention0.setForeground(new Color(255, 140, 0));
		lblAttention0.setFont(new Font("Meiryo UI", Font.PLAIN, 12));
		attention.add(lblAttention0);

		int limitUnit = PropertyUtil.getPropertyInt("api.request.limit.unit.minutes");
		int limit = PropertyUtil.getPropertyInt("api.request.limit.count");
		JLabel lblAttention1 = new JLabel(String.format("%d分当たり%d回", limitUnit, limit));
		lblAttention1.setForeground(new Color(255, 140, 0));
		lblAttention1.setFont(new Font("Meiryo UI", Font.PLAIN, 12));
		attention.add(lblAttention1);

		JLabel lblAttention2 = new JLabel("を超えるとブロックされるようです。");
		lblAttention2.setForeground(new Color(255, 140, 0));
		lblAttention2.setFont(new Font("Meiryo UI", Font.PLAIN, 12));
		attention.add(lblAttention2);

		JPanel timeout = new JPanel();
		timeout.setLayout(null);
		timeout.setBackground(Color.WHITE);
		timeout.setBounds(12, 152, 420, 50);
		getContentPane().add(timeout);

		JLabel lblTimeout = new JLabel("コネクションタイムアウト(秒)");
		lblTimeout.setForeground(new Color(25, 25, 112));
		lblTimeout.setFont(new Font("Meiryo UI", Font.BOLD, 14));
		lblTimeout.setBounds(12, 10, 161, 30);
		timeout.add(lblTimeout);

		timeoutValue = new JTextField();
		timeoutValue.setFont(new Font("Meiryo UI", Font.PLAIN, 13));
		timeoutValue.setColumns(10);
		timeoutValue.setBounds(185, 10, 223, 30);
		int timeout_ms = PropertyUtil.getPropertyInt("http.connection.timeout");
		timeoutValue.setText(String.valueOf(timeout_ms / 1000));
		timeout.add(timeoutValue);

		JPanel connectTo = new JPanel();
		connectTo.setLayout(null);
		connectTo.setBackground(Color.WHITE);
		connectTo.setBounds(12, 201, 420, 50);
		getContentPane().add(connectTo);

		JLabel lblConnectTo = new JLabel("接続先");
		lblConnectTo.setForeground(new Color(25, 25, 112));
		lblConnectTo.setFont(new Font("Meiryo UI", Font.BOLD, 14));
		lblConnectTo.setBounds(12, 10, 66, 30);
		connectTo.add(lblConnectTo);

		connectToValue = new JTextField();
		connectToValue.setForeground(SystemColor.controlDkShadow);
		connectToValue.setFont(new Font("Meiryo UI", Font.PLAIN, 13));
		connectToValue.setEditable(false);
		connectToValue.setColumns(10);
		connectToValue.setBounds(121, 10, 287, 30);
		connectToValue.setText(PropertyUtil.getProperty("chatwork.api.url"));
		connectTo.add(connectToValue);
		this.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]
		{ lblAboutAPI, apiKeyValue, intervalValue, timeoutValue, btnCancel, btnOK }));

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				listner.onClose(new CloseEvent(this));
				frame.dispose();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
	}
}

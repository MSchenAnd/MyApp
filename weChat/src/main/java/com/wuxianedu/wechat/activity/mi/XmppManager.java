package com.wuxianedu.wechat.activity.mi;

import android.text.TextUtils;
import android.util.Log;

import com.wuxianedu.wechat.activity.bean.SendMessage;
import com.wuxianedu.wechat.activity.bean.User;
import com.wuxianedu.wechat.utils.Constants;
import com.wuxianedu.wechat.utils.PingYinUtil;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.MessageEventManager;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * Xmpp管理类
 */
public class XmppManager {

	private static XmppManager xmppManager = null;
	private XMPPConnection connection = null;
	private final String TAG="XmppManager";
	private XmppManagerCallback xmppManagerCallback;
	private Roster roster;

	public synchronized static XmppManager getInstance(){
		if(xmppManager == null){
			xmppManager = new XmppManager();
		}
		return xmppManager;
	}

	/**
	 * 打开连接
	 */
	public void openConnection() {
		if (connection == null || !connection.isAuthenticated()) {
			try {
				// 配置连接
				ConnectionConfiguration config = new ConnectionConfiguration(Constants.SERVER_HOST,
						Constants.SERVER_PORT, Constants.SERVER_NAME);
				config.setReconnectionAllowed(true);
				config.setSecurityMode(SecurityMode.disabled);
				config.setSASLAuthenticationEnabled(false);
				config.setSendPresence(true); // 状态设为离线，为了取离线消息
				// 配置各种Provider，如果不配置，则会无法解析数据
				configureConnection(ProviderManager.getInstance());
				connection = new XMPPConnection(config);
				connection.connect();
			} catch (XMPPException xe) {
				xe.printStackTrace();
			}
		}
	}

	/**
	 * 获取连接
	 */
	public XMPPConnection getConnection() {
		if (connection == null) {
			openConnection();
		}
		return connection;
	}

	/**
	 * 断开连接
	 */
	public void closeConnection() {
		if (connection != null && connection.isConnected()) {
			connection.disconnect();
			connection = null;
		}
	}

	/**
	 * 登录
	 * @param account 登录帐号
	 * @param password 登录密码
	 * @return
	 */
	public boolean login(String account, String password) {
		try {
			if (getConnection() == null){
				return false;
			}

			if (!getConnection().isAuthenticated() && getConnection().isConnected()) {
				getConnection().login(account, password);
				// 更改在线状态
				Presence presence = new Presence(Presence.Type.available);
				presence.setMode(Presence.Mode.available);
				getConnection().sendPacket(presence);
				//接收消息监听
				ChatManager chatManager = getConnection().getChatManager();
				chatManager.addChatListener(chatManagerListener);
				return true;
			}
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 聊天管理监听器
	 */
	private ChatManagerListener chatManagerListener =  new ChatManagerListener(){
		@Override
		public void chatCreated(Chat chat, boolean able) {
			chat.addMessageListener(new MessageListener() {
				@Override
				public void processMessage(Chat chat2, Message message) {
					if(TextUtils.isEmpty(message.getBody())){
						return;
					}
					if (xmppManagerCallback != null) {
						xmppManagerCallback.receiveMsg(message);
					}
				}
			});
		}
	};

	/**
	 * 注册
	 * @param account 注册帐号
	 * @param password 注册密码
	 * @return
	 */
	public IQ reg(String account, String password) {
		if (getConnection() == null) {
			return null;
		}
		Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(getConnection().getServiceName());
		reg.setUsername(account);
		reg.setPassword(password);

		PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = getConnection().createPacketCollector(filter);
		// 向服务器端，发送注册Packet包，注意其中Registration是Packet的子类
		getConnection().sendPacket(reg);
		IQ result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		collector.cancel(); //停止请求result
		return result;
	}

	/**
	 * 注销登录
	 */
	public void logout() {
		if (getConnection() == null){
			return;
		}
		Presence presence = new Presence(Presence.Type.unavailable);
		getConnection().sendPacket(presence);
		closeConnection();
	}

	/**
	 * 配置链接
	 * @param pm
	 */
	public void configureConnection(ProviderManager pm) {
		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster", new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event", new MessageEventProvider());
		// Chat State
		pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());
		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference", new GroupChatInvitation.Provider());
		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay", new DelayInformationProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup", "http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses", "http://jabber.org/protocol/address", new MultipleAddressesProvider());
		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());
		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}

	/**
	 * 获取用户信息
	 * @param user
	 * @return
	 */
	public VCard getUserInfo(String user) {
		VCard vcard = null;
		try {
			vcard = new VCard();
			// 加入这句代码，解决No VCard for
			ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp", new VCardProvider());
			if (user == null) {
				vcard.load(getConnection());
			} else {
				vcard.load(getConnection(), user + "@" + Constants.SERVER_NAME);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vcard;
	}

	/**
	 * 获取xmpp好友列表
	 */
	public List<User> getFriendList() {
		if (roster == null) {
			roster = getConnection().getRoster();
		}
		List<User> userList = new ArrayList();
		Collection<RosterEntry> entries = roster.getEntries();
		for(RosterEntry entry : entries){
			userList.add(new User(getUsername(entry.getUser()),entry.getType()));

		}
		Collections.sort(userList, new Comparator<User>() {
			@Override
			public int compare(User user1, User user2) {
				String userName1 = PingYinUtil.getPingYin(user1.getUserName());
				String userName2 = PingYinUtil.getPingYin(user2.getUserName());
				return userName1.compareTo(userName2);
			}
		});
		return userList;
	}

	/**
	 * 通过jid获得username
	 * @param fullUsername
	 * @return
	 */
	public static String getUsername(String fullUsername){
		return fullUsername.split("@")[0];
	}

	 /**
	  * 搜索用户
	  * @param userName
	  * @return
	  * @throws XMPPException
	  */
	 public List<String> searchUser(String userName) {
	 	if (getConnection() == null){
			return null;
		}
		List<String> userList = new ArrayList<String>();
		try {
			UserSearchManager search = new UserSearchManager(getConnection());
			Form searchForm = search.getSearchForm("search."+ getConnection().getServiceName());
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", userName.trim());
			ReportedData data = search.getSearchResults(answerForm,"search." + connection.getServiceName());
			Iterator<ReportedData.Row> it = data.getRows();
			ReportedData.Row row = null;
			   while (it.hasNext()) {
				   row = it.next();
				   userList.add(row.getValues("Username").next().toString());
			   }
		} catch (XMPPException e) {
			  e.printStackTrace();
		}
		 return userList;
	 }

	/**
	 * 添加好友(无分组)
	 * @param userName
	 * @return
	 */
	public boolean addFriend(String userName) {
		if (getConnection() == null)
			return false;
		try {
			getConnection().getRoster().createEntry(getFullUsername(userName), getFullUsername(userName), null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除好友
	 */

	public boolean removeFriend(String userName){
		boolean isRemove = false;
		if (roster == null) {
			roster = getConnection().getRoster();
		}
		Collection<RosterEntry> entries = roster.getEntries();
		Iterator iterator = entries.iterator();
		while (iterator.hasNext()){
			RosterEntry rosterEntry = (RosterEntry) iterator.next();
			String name = XmppManager.getUsername(rosterEntry.getUser());
			if (name.equals(userName)){
				try {
					getConnection().getRoster().removeEntry(rosterEntry);
					isRemove = true;
				} catch (XMPPException e) {
					e.printStackTrace();

				}
			}
		}
		return isRemove;
	}
	/**
	 * 通过username获得jid
	 * @param username
	 * @return
	 */
	public static String getFullUsername(String username){
		return username + "@" + Constants.SERVER_NAME;
	}

	/**
	 * 发送文本消息
	 * @param message
	 */
	public void sendMsg(Chat chat, String message) {
		try {
			chat.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建聊天
	 * @param toUser
	 * @return
	 */
	public Chat createChat(String toUser){
		ChatManager chatManager = getConnection().getChatManager();
		Chat newchat = chatManager.createChat(toUser + "@"+ Constants.SERVER_NAME, null);
		return newchat;
	}

	public interface XmppManagerCallback {
		//接收消息回调函数
		void receiveMsg(Message message);
	}

	public void setXmppManagerCallback(XmppManagerCallback xmppManagerCallback) {
		this.xmppManagerCallback = xmppManagerCallback;
	}

}
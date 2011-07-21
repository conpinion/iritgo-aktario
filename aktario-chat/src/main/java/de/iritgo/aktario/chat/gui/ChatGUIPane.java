/**
 * This file is part of the Iritgo/Aktario Framework.
 *
 * Copyright (C) 2005-2011 Iritgo Technologies.
 * Copyright (C) 2003-2005 BueroByte GbR.
 *
 * Iritgo licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iritgo.aktario.chat.gui;


import de.iritgo.aktario.chat.chatter.ChatClientManager;
import de.iritgo.aktario.chat.command.ShowChatter;
import de.iritgo.aktario.chat.command.UserChatCommand;
import de.iritgo.aktario.chat.command.UserJoinCommand;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.CallbackActionListener;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.appcontext.AppContext;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ChatGUIPane extends SwingGUIPane implements ChatGUI
{
	private Map channelTabs;

	private JTabbedPane channels;

	private JTextField messageField;

	private int tabCount;

	/** Date formatter. */
	private DateFormat dateFormat;

	/** The current time. */
	private Date currentTime;

	public ChatGUIPane ()
	{
		super ("common.chatview");
		channelTabs = new HashMap ();
		dateFormat = DateFormat.getTimeInstance (DateFormat.MEDIUM);
		currentTime = new Date ();
	}

	@Override
	public void initGUI ()
	{
		JPanel allPanel = new JPanel ();

		allPanel.setLayout (new GridBagLayout ());
		channels = new JTabbedPane ();

		allPanel.add (channels, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		messageField = new JTextField ();
		messageField.addActionListener (new CallbackActionListener (this, "onChatMessage"));
		allPanel.add (messageField, createConstraints (0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, null));

		content.add (allPanel, createConstraints (0, 1, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		tabCount = 0;
		ShowChatter.setChatterIsVisible ();

		getDisplay ().setIcon (new ImageIcon (ChatGUIPane.class.getResource ("/resources/chat.png")));

		Long appInstanceId = (Long) getDisplay ().getProperties ().get ("aktario.applicationInstanceId");

		AppContext.instance ().put ("applicationPane", this);
		AppContext.instance ().put ("applicationPane." + appInstanceId, this);

		UserJoinCommand userJoinCommand = new UserJoinCommand ("System", AppContext.instance ().getUser ().getName ());

		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (userJoinCommand);
	}

	@Override
	public void loadFromObject (IObject iObject)
	{
		/* empty */
	}

	@Override
	public void storeToObject (IObject iObject)
	{
		/* empty */
	}

	public void joinChannel (String channelId, String user)
	{
		TabChatView tabChatView = (TabChatView) channelTabs.get (new Integer (channelId.hashCode ()));

		if (tabChatView != null)
		{
			MessageFormat mf = new MessageFormat (Engine.instance ().getResourceService ().getStringWithoutException (
							"chat.userjoind"));

			tabChatView.addUser (user);
			tabChatView.chatMessage (user, mf.format (new Object[]
			{
				user
			}, new StringBuffer (), null).toString ());

			return;
		}

		tabChatView = new TabChatView ();

		channelTabs.put (new Integer (channelId.hashCode ()), tabChatView);
		channels.addTab (channelId, tabChatView.createTabChatView (tabCount, channelId));
		channels.revalidate ();
		++tabCount;
	}

	public void leaveChannel (Integer channelId, String user)
	{
		TabChatView tabChatView = (TabChatView) channelTabs.get (channelId);

		if (tabChatView == null)
		{
			return;
		}

		MessageFormat mf = new MessageFormat (Engine.instance ().getResourceService ().getStringWithoutException (
						"chat.userleave"));

		tabChatView.chatMessage (user, mf.format (new Object[]
		{
			user
		}, new StringBuffer (), null).toString ());

		if (user.equals ("") || AppContext.instance ().getUser ().getName ().equals (user))
		{
			channels.removeTabAt (channels.indexOfTab (tabChatView.getTabName ()));
			channelTabs.remove (channelId);
			--tabCount;

			return;
		}

		tabChatView.removeUser (user);
	}

	public void addMessage (String message, int channelId, String user)
	{
		TabChatView tabChatView = (TabChatView) channelTabs.get (new Integer (channelId));

		currentTime.setTime (System.currentTimeMillis ());
		tabChatView.chatMessage (user, "[" + dateFormat.format (currentTime) + "] " + user + ": " + message);
	}

	/**
	 * Register a new User.
	 */
	public void onChatMessage (@SuppressWarnings("unused") ActionEvent event)
	{
		String message = messageField.getText ();

		messageField.setText ("");

		int index = channels.getSelectedIndex ();

		String channel = "System";

		if (index >= 0)
		{
			channel = channels.getTitleAt (index);
		}

		UserChatCommand userChatCommand = new UserChatCommand (message, channel.hashCode (), AppContext.instance ()
						.getUser ().getName ());

		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (userChatCommand);
	}

	@Override
	public void close ()
	{
		ChatClientManager chatManager = (ChatClientManager) Engine.instance ().getManagerRegistry ().getManager (
						"chat.client");

		chatManager.closeAllChannels ();
		super.close ();
		ShowChatter.setChatterIsNotVisible ();

		Long appInstanceId = (Long) getDisplay ().getProperties ().get ("aktario.applicationInstanceId");

		AppContext.instance ().remove ("applicationPane");
		AppContext.instance ().remove ("applicationPane." + appInstanceId);
	}

	@Override
	public void systemClose ()
	{
		close ();
	}

	public Integer getCurrentChannel ()
	{
		return new Integer (channels.getTitleAt (channels.getSelectedIndex ()).hashCode ());
	}

	@Override
	public GUIPane cloneGUIPane ()
	{
		ChatGUIPane registerNewUserGUIPane = new ChatGUIPane ();

		return registerNewUserGUIPane;
	}
}

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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.client.command.ShowWindow;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;


public class TabChatView extends SwingGUIPane
{
	@SuppressWarnings("unused")
	private int channelId;

	private JPanel tabComponent;

	private JTextPane chatArea;

	private JList usersGUI;

	private DefaultListModel users;

	private int tabPos;

	private String tabName;

	/** Default user colors. */
	protected Color[] defaultLineColors =
	{
					Color.RED, Color.BLUE, Color.GREEN.darker(), Color.YELLOW.darker(), Color.ORANGE, Color.MAGENTA,
					Color.CYAN.darker(), Color.PINK, Color.DARK_GRAY
	};

	/** Next default color to choose. */
	protected int nextColor;

	/** Colors by user name. */
	protected Map userColors;

	public TabChatView()
	{
		super("common.tabchatview");
		userColors = new HashMap();
	}

	public IObject getSampleObject()
	{
		return null;
	}

	@Override
	public GUIPane cloneGUIPane()
	{
		return new TabChatView();
	}

	@SuppressWarnings("serial")
	public JPanel createTabChatView(int tabCount, String tabName)
	{
		tabPos = tabCount;
		this.tabName = tabName;

		tabComponent = new JPanel(new BorderLayout());

		JSplitPane split = new JSplitPane();

		tabComponent.add(BorderLayout.CENTER, split);

		chatArea = new JTextPane();

		users = new DefaultListModel();
		usersGUI = new JList(users);
		usersGUI.setCellRenderer(new DefaultListCellRenderer()
		{
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
							boolean cellHasFocus)
			{
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setForeground((Color) userColors.get((String) value));

				return this;
			}
		});

		split.setLeftComponent(new JScrollPane(usersGUI));

		split.setRightComponent(new JScrollPane(chatArea));

		split.setDividerLocation(120);

		return tabComponent;
	}

	public int getTabPos()
	{
		return tabPos;
	}

	public String getTabName()
	{
		return tabName;
	}

	public JPanel getTabPanel()
	{
		return tabComponent;
	}

	public void chatMessage(String userName, String message)
	{
		Color color = (Color) userColors.get(userName);

		if (color == null)
		{
			color = Color.BLACK;
		}

		chatArea.setEditable(true);

		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

		chatArea.setCaretPosition(chatArea.getDocument().getLength());
		chatArea.setCharacterAttributes(aset, false);
		chatArea.replaceSelection(message + "\n");
		chatArea.setEditable(false);
	}

	public void addUser(String user)
	{
		if (userColors.get(user) == null)
		{
			userColors.put(user, defaultLineColors[nextColor]);
		}

		nextColor = (nextColor + 1) % defaultLineColors.length;
		users.addElement(user);
		usersGUI.revalidate();
		usersGUI.repaint();
	}

	public void removeUser(String user)
	{
		users.removeElement(user);
		usersGUI.revalidate();
		usersGUI.repaint();
	}

	/**
	 * Register a new User.
	 */
	public void onUserInformation(@SuppressWarnings("unused") ActionEvent event)
	{
		UserRegistry userRegistry = Client.instance().getUserRegistry();
		String userName = (String) users.get(usersGUI.getSelectedIndex());

		User user = userRegistry.getUser(userName);

		FrameworkProxy userProxy = new FrameworkProxy(user);

		Engine.instance().getProxyRegistry().addProxy(userProxy, user.getTypeId());
		Engine.instance().getBaseRegistry().add(user);

		CommandTools.performAsync(new ShowWindow("main.user_" + userName, user));
	}

	@Override
	public void loadFromObject(IObject iObject)
	{
		/* empty */
	}

	@Override
	public void storeToObject(IObject iObject)
	{
		/* empty */
	}
}

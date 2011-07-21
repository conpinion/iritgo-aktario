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

package de.iritgo.aktario.client.gui;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.SystemProperties;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.ICheckBox;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.simplelife.string.StringTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.StringTokenizer;
import java.util.regex.PatternSyntaxException;


/**
 * UserLoginDialog
 *
 * @version $Id: UserLoginPane.java,v 1.20 2006/10/05 15:00:42 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class UserLoginPane extends SwingGUIPane
{
	/**
	 * The auto login combo box is provided to the developers for
	 * easier server login. Be sure to remove this combobox from the
	 * gui in a release version!
	 */
	private class AutoLoginItem
	{
		/** The user name to connect with. */
		public String userName;

		/** The password to connect with. */
		public String password;

		/** The server to connect with. */
		public String server;

		/**
		 * Create a new AutoLoginItem.
		 *
		 * @param userName The user name to connect with.
		 * @param password The password to connect with.
		 * @param server The server to connect with.
		 */
		public AutoLoginItem (String userName, String password, String server)
		{
			this.userName = userName;
			this.password = password;
			this.server = server;
		}

		/**
		 * Create a string representation of the auto login item.
		 *
		 * @return A string representation of the item.
		 */
		@Override
		public String toString ()
		{
			return userName + "@" + server;
		}
	}

	/** Server name. */
	public JTextField server;

	/** User name. */
	public JTextField username;

	/** User password. */
	public JPasswordField password;

	/** Short cut for the developers. */
	public JComboBox logins;

	/** Check to store the login data. */
	public ICheckBox remember;

	/** Check to store the login data. */
	public ICheckBox autoLogin;

	/** The dialog background. */
	public JLabel background;

	/** Myself. */
	protected UserLoginPane loginPane;

	/**
	 * Log into the server.
	 */
	public Action loginAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			display.close ();

			UserLoginHelper.login (loginPane, server.getText (), username.getText (), new String (password
							.getPassword ()), remember.isSelected (), autoLogin.isSelected ());
		}
	};

	/**
	 * Cancel the login process.
	 */
	public Action cancelAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			display.close ();
			IritgoEngine.instance ().shutdown ();
		}
	};

	/**
	 * Create a new user login dialog.
	 */
	public UserLoginPane ()
	{
		super ("AktarioUserLoginDialog");
	}

	/**
	 * Initialize the gui.
	 */
	@Override
	public void initGUI ()
	{
		loginPane = this;

		try
		{
			SwingEngine swingEngine = new SwingEngine (this);

			swingEngine.setClassLoader (UserLoginPane.class.getClassLoader ());

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource ("/swixml/UserLoginPane.xml"));

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			server.setText ("localhost");

			SystemProperties properties = Engine.instance ().getSystemProperties ();
			String storedAccounts = properties.getString ("account.list", "");

			for (StringTokenizer st = new StringTokenizer (storedAccounts, ","); st.hasMoreTokens ();)
			{
				String accountNum = st.nextToken ();

				logins.addItem (new AutoLoginItem (properties.getString ("account." + accountNum + ".name", ""),
								StringTools.decode (properties.getString ("account." + accountNum + ".password", "")),
								properties.getString ("account." + accountNum + ".server", "")));
			}

			logins.setSelectedIndex (- 1);

			logins.addItemListener (new ItemListener ()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						remember.setSelected (true);

						AutoLoginItem item = (AutoLoginItem) logins.getSelectedItem ();

						username.setText (item.userName);
						password.setText (item.password);
						server.setText (item.server);
					}
				}
			});

			AktarioGUI gui = (AktarioGUI) Client.instance ().getClientGUI ();

			background.setIcon (gui.getLoginBackground ());
		}
		catch (Exception x)
		{
			Log.logError ("client", "UserLoginDialog", x.toString ());
		}

		String lastLogin = Engine.instance ().getSystemProperties ().getProperty ("lastlogin", "");

		if (! lastLogin.equals (""))
		{
			username.setText (lastLogin.substring (0, lastLogin.indexOf ("@")));
			server.setText (lastLogin.substring (lastLogin.indexOf ("@") + 1, lastLogin.length ()));
		}

		if (StringTools.isTrimEmpty (username.getText ()))
		{
			username.grabFocus ();
		}
		else
		{
			password.grabFocus ();
		}
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane ()
	{
		return new UserLoginPane ();
	}

	/**
	 * Remember the current account information.
	 */
	public void rememberAccount ()
	{
		SystemProperties properties = Engine.instance ().getSystemProperties ();
		int nextAccountNum = properties.getInt ("account.next", 0);
		String storedAccounts = properties.getString ("account.list", "");

		for (StringTokenizer st = new StringTokenizer (storedAccounts, ","); st.hasMoreTokens ();)
		{
			String accountNum = st.nextToken ();

			if (properties.getString ("account." + accountNum + ".name", "").equals (username.getText ())
							&& properties.getString ("account." + accountNum + ".server", "")
											.equals (server.getText ()))
			{
				return;
			}
		}

		properties.put ("account." + nextAccountNum + ".name", username.getText ());
		properties.put ("account." + nextAccountNum + ".password", StringTools.encode (new String (password
						.getPassword ())));
		properties.put ("account." + nextAccountNum + ".server", server.getText ());
		properties.put ("account.list", storedAccounts + (storedAccounts.length () > 0 ? "," : "") + nextAccountNum);
		properties.put ("account.next", nextAccountNum + 1);
		Engine.instance ().storeSystemProperties ();
	}

	/**
	 * Check if the current account information was stored in the
	 * properties. In this case remove it from the properties.
	 */
	public void removeAccount ()
	{
		SystemProperties properties = Engine.instance ().getSystemProperties ();
		String storedAccounts = properties.getString ("account.list", "");

		for (StringTokenizer st = new StringTokenizer (storedAccounts, ","); st.hasMoreTokens ();)
		{
			String accountNum = st.nextToken ();

			if (properties.getString ("account." + accountNum + ".name", "").equals (username.getText ())
							&& properties.getString ("account." + accountNum + ".server", "")
											.equals (server.getText ()))
			{
				properties.remove ("account." + accountNum + ".name");
				properties.remove ("account." + accountNum + ".password");
				properties.remove ("account." + accountNum + ".server");

				try
				{
					storedAccounts = storedAccounts.replaceAll ("(^|,)" + accountNum + "(,|$)", "$1$2");
					storedAccounts = storedAccounts.replaceAll ("(^,|,$)", "");
					storedAccounts = storedAccounts.replaceAll ("(,,)", ",");
				}
				catch (PatternSyntaxException ignored)
				{
				}

				properties.put ("account.list", storedAccounts);

				Engine.instance ().storeSystemProperties ();

				return;
			}
		}
	}

	/**
	 * Remember the current account information.
	 */
	public void rememberAutoLogin ()
	{
		SystemProperties properties = Engine.instance ().getSystemProperties ();

		properties.put ("autoLogin", "true");
		properties.put ("autoLoginUser", username.getText ());
		properties.put ("autoLoginPassword", StringTools.encode (new String (password.getPassword ())));
		properties.put ("autoLoginServer", server.getText ());
		Engine.instance ().storeSystemProperties ();
	}

	@Override
	public void loadFromObject (IObject iobject)
	{
		/* empty */
	}

	@Override
	public void storeToObject (IObject iobject)
	{
		/* empty */
	}
}

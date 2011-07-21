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
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.IButton;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.client.command.ShowDialog;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.user.action.UserLoginFailureAction;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;


/**
 * UserLoginFailureDialog.
 *
 * @version $Id: UserLoginFailurePane.java,v 1.10 2006/09/25 10:34:31 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class UserLoginFailurePane extends SwingGUIPane
{
	/** The message panel. */
	public Box messagePanel;

	public IButton button;

	/**
	 * Close the dialog and reopen the login dialog.
	 */
	public Action okAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			display.close ();
			CommandTools.performAsync (new ShowDialog ("AktarioUserLoginDialog"));
		}
	};

	/**
	 * Create a new login failure dialog.
	 */
	public UserLoginFailurePane ()
	{
		super ("AktarioUserLoginFailureDialog");
	}

	/**
	 * Initialize the gui.
	 */
	@Override
	public void initGUI ()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine (this);

			swingEngine.setClassLoader (UserLoginFailurePane.class.getClassLoader ());

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource ("/swixml/UserLoginFailurePane.xml"));

			int failure = ((Integer) getDisplay ().getProperties ().get ("failure")).intValue ();

			String msg;

			if (failure == UserLoginFailureAction.USER_ALREADY_ONLINE)
			{
				msg = Engine.instance ().getResourceService ().getString ("aktario.userLoginFailureUserAlreadyOnline");

				button.setEnabled (false);

				new Thread (new Runnable ()
				{
					public void run ()
					{
						int i = 0;

						while (i <= 9)
						{
							++i;

							try
							{
								Thread.sleep (1000);
							}
							catch (Exception x)
							{
							}

							button.setText ("" + (10 - i));
						}

						display.close ();
					}
				}).start ();
			}
			else if (failure == UserLoginFailureAction.LOGIN_NOT_ALLOWED)
			{
				msg = Engine.instance ().getResourceService ().getString ("aktario.userLoginNotAllowed");
			}
			else
			{
				msg = Engine.instance ().getResourceService ().getString ("aktario.userLoginFailureBadNameOrPassword");
			}

			StringTokenizer st = new StringTokenizer (msg, "\n");

			while (st.hasMoreTokens ())
			{
				messagePanel.add (new JLabel (st.nextToken ()));
			}

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
			x.printStackTrace ();

			Log.logError ("client", "UserLoginFailureDialog", x.toString ());
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
		return new UserLoginFailurePane ();
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

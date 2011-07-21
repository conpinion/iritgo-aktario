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


import de.iritgo.aktario.client.AktarioClientPlugin;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.ITextField;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.user.AktarioUser;
import de.iritgo.aktario.core.user.AktarioUserRegistry;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;


/**
 * This gui pane is used to edit users.
 *
 * @version $Id: EditUserPane.java,v 1.12 2006/10/05 15:00:41 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class EditUserPane extends SwingGUIPane
{
	/**
	 * Items of the role combo box.
	 */
	private class RoleItem
	{
		public String name;

		public int role;

		public RoleItem (String name, int role)
		{
			this.name = name;
			this.role = role;
		}

		@Override
		public String toString ()
		{
			return name;
		}
	}

	/** The user name. */
	public ITextField username;

	/** The user password. */
	public JPasswordField password;

	/** The repeated user password. */
	public JPasswordField passwordRepeat;

	/** The user's email. */
	public ITextField email;

	/** The user's full name. */
	public ITextField fullName;

	/** Roleselection. */
	public JComboBox role;

	/**
	 * Save the data object and close the display.
	 */
	public Action okAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			if (new String (password.getPassword ()).equals (new String (passwordRepeat.getPassword ())))
			{
				if (username.getText ().equals (""))
				{
					JOptionPane.showMessageDialog (content, Engine.instance ().getResourceService ().getString (
									"aktario-user.noname"), Engine.instance ().getResourceService ().getString (
									"app.title"), JOptionPane.OK_OPTION);

					return;
				}

				storeToObject ();
				display.close ();
			}
			else
			{
				JOptionPane.showMessageDialog (content, Engine.instance ().getResourceService ().getString (
								"aktario-user.passwordrepeatfailure"), Engine.instance ().getResourceService ()
								.getString ("app.title"), JOptionPane.OK_OPTION);
			}
		}
	};

	/**
	 * Close the display.
	 */
	public Action cancelAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			if (sessionContext != null)
			{
				AktarioUserRegistry list = (AktarioUserRegistry) sessionContext.get ("userlist");

				list.removeUser ((AktarioUser) getIObject ());
			}

			display.close ();
		}
	};

	/**
	 * Create a new EditUserGUIPane.
	 */
	public EditUserPane ()
	{
		super ("EditUserGUIPane");
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	@Override
	public void initGUI ()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine (this);

			swingEngine.setClassLoader (AktarioClientPlugin.class.getClassLoader ());

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource ("/swixml/EditUserPane.xml"));

			ResourceService resources = Engine.instance ().getResourceService ();

			role.addItem (new RoleItem (resources.getString ("aktario.roleAdmin"), AktarioUser.ROLE_ADMIN));
			role.addItem (new RoleItem (resources.getString ("aktario.roleUser"), AktarioUser.ROLE_USER));

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
			Log.logError ("client", "EditUserGUIPane.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	@Override
	public void loadFromObject (IObject iobject)
	{
		AktarioUser user = (AktarioUser) iobject;

		username.setText (user.getName ());
		password.setText (user.getPassword ());
		passwordRepeat.setText (user.getPassword ());
		email.setText (user.getEmail ());
		fullName.setText (user.getFullName ());

		for (int i = 0; i < role.getItemCount (); ++i)
		{
			if (((RoleItem) role.getItemAt (i)).role == user.getRole ())
			{
				role.setSelectedIndex (i);
			}
		}

		username.selectAll ();
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	@Override
	public void storeToObject (IObject iobject)
	{
		AktarioUser user = (AktarioUser) iobject;

		user.setName (username.getText ());
		user.setPassword (new String (password.getPassword ()));
		user.setEmail (email.getText ());
		user.setFullName (fullName.getText ());
		user.setRole (((RoleItem) role.getSelectedItem ()).role);

		user.update ();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane ()
	{
		return new EditUserPane ();
	}
}

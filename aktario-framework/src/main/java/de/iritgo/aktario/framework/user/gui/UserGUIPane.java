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

package de.iritgo.aktario.framework.user.gui;


import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.ILabel;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.framework.user.User;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;


/**
 *
 */
public class UserGUIPane extends SwingGUIPane
{
	private JTextField userName;

	private JTextField password;

	private JTextField eMail;

	/**
	 * Standard constructor
	 */
	public UserGUIPane ()
	{
		super ("common.userview");
	}

	/**
	 * Return a sample object from this guipane. It is a will use only for a "reloadplugin" command. Refactoring? Redesign?
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new User ();
	}

	/**
	 * Init GUI
	 */
	public void initGUI ()
	{
		JPanel allPanel = new JPanel ();

		allPanel.setLayout (new GridBagLayout ());

		int row = 0;

		ILabel userNameLabel = new ILabel ("common.username");

		allPanel.add (userNameLabel, createConstraints (0, row, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, new Insets (
						5, 15, 5, 15)));
		userName = new JTextField ();
		allPanel.add (userName, createConstraints (1, row++, 1, 1, GridBagConstraints.HORIZONTAL, 100, 0, new Insets (
						5, 15, 5, 15)));

		ILabel emailLabel = new ILabel ("common.email");

		allPanel.add (emailLabel, createConstraints (0, row, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, new Insets (5,
						15, 5, 15)));
		eMail = new JTextField ();
		allPanel.add (eMail, createConstraints (1, row++, 1, 1, GridBagConstraints.HORIZONTAL, 100, 0, new Insets (5,
						15, 5, 15)));

		content.add (allPanel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
	}

	/**
	 * LoadFormObject, load the Data form Object.
	 */
	public void loadFromObject (IObject iObject)
	{
		User user = (User) iObject;

		userName.setText (user.getName ());
		eMail.setText (user.getEmail ());
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject (IObject iObject)
	{
		User user = (User) iObject;
	}

	/**
	 * Close it.
	 */
	public void close ()
	{
		super.close ();
	}

	/**
	 * Register a new User.
	 */
	public void onRegister (ActionEvent event)
	{
	}

	/**
	 * Cancel
	 */
	public void onCancel (ActionEvent event)
	{
		display.close ();
	}

	/**
	 * Return a new instance.
	 */
	public GUIPane cloneGUIPane ()
	{
		UserGUIPane userGUIPane = new UserGUIPane ();

		return userGUIPane;
	}
}

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
import de.iritgo.aktario.core.gui.IButton;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.IObjectTableModel;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.sessioncontext.SessionContext;
import de.iritgo.aktario.core.user.AktarioUser;
import de.iritgo.aktario.framework.client.command.ShowDialog;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.dataobject.SimpleQuery;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * This gui pane displays a list of all users and lets the administrator
 * add, edit, and delete users.
 *
 * @version $Id: UserListPane.java,v 1.10 2006/09/25 10:34:31 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class UserListPane extends SwingGUIPane
{
	/** The table of all users. */
	public JTable userTable;

	/** Model for the user table. */
	private IObjectTableModel userModel;

	/** The edit button. */
	public IButton btnEdit;

	/** The delete button. */
	public IButton btnDelete;

	/**
	 * Create a new user.
	 */
	public Action addUserAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			SimpleQuery list = (SimpleQuery) getIObject ();

			AktarioUser user = new AktarioUser ();
			SessionContext sessionContext = new SessionContext ("user");

			sessionContext.add ("userlist", list);
			list.getIObjectListResults ().add (user);
			CommandTools.performAsync (new ShowDialog ("EditUserGUIPane", user, sessionContext));
		}
	};

	/**
	 * Edit an existing user.
	 */
	public Action editUserAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			int selectedRow = userTable.getSelectedRow ();

			if (selectedRow < 0)
			{
				return;
			}

			CommandTools.performAsync (new ShowDialog ("EditUserGUIPane", (IObject) ((SimpleQuery) getIObject ())
							.getIObjectListResults ().get (selectedRow)));
		}
	};

	/**
	 * Delete an existing user.
	 */
	public Action deleteUserAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			SimpleQuery list = (SimpleQuery) getIObject ();

			int selectedRow = userTable.getSelectedRow ();

			if (selectedRow < 0)
			{
				return;
			}

			if (JOptionPane.showConfirmDialog (content, Engine.instance ().getResourceService ().getString (
							"aktario.removeConfirm"), "Clasroom", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				list.getIObjectListResults ().remove (
								((SimpleQuery) getIObject ()).getIObjectListResults ().get (selectedRow));
			}
		}
	};

	/**
	 * Close the display
	 */
	public Action okAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			display.close ();
		}
	};

	/**
	 * Create a new UserListGUIPane.
	 */
	public UserListPane ()
	{
		super ("UserListGUIPane");
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

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource ("/swixml/UserListPane.xml"));

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			userModel = new IObjectTableModel ()
			{
				private String[] columnNames = new String[]
				{
								Engine.instance ().getResourceService ().getString ("aktario.name"),
								Engine.instance ().getResourceService ().getString ("aktario.role")
				};

				public int getColumnCount ()
				{
					return columnNames.length;
				}

				@Override
				public String getColumnName (int col)
				{
					return columnNames[col];
				}

				public Object getValueAt (int row, int col)
				{
					IObjectList list = ((SimpleQuery) getIObject ()).getIObjectListResults ();

					AktarioUser aktarioUser = (AktarioUser) list.get (row);

					switch (col)
					{
						case 0:
							return aktarioUser.getName ();

						case 1:
							return aktarioUser.getRoleString ();

						default:
							return null;
					}
				}
			};
			userTable.setModel (userModel);

			userTable.addMouseListener (new MouseAdapter ()
			{
				@Override
				public void mouseClicked (MouseEvent e)
				{
					if (e.getClickCount () == 2)
					{
						editUserAction.actionPerformed (null);
					}
				}
			});

			userTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
			{
				public void valueChanged (ListSelectionEvent e)
				{
					btnEdit.setEnabled (userTable.getSelectedRow () != - 1);
					btnDelete.setEnabled (userTable.getSelectedRow () != - 1);
				}
			});

			getDisplay ().putProperty ("weighty", new Double (0.5));
		}
		catch (Exception x)
		{
			Log.logError ("client", "UserListGUIPane.initGUI", x.toString ());
		}
	}

	/**
	 * Load the data object into the gui.
	 */
	@Override
	public void loadFromObject (IObject iobject)
	{
		// 		AktarioUserRegistry userRegistry = (AktarioUserRegistry) iobject;
		//
		// 		userModel.update (userRegistry.getUsers (), transaction);
		//
		SimpleQuery simpleQuery = (SimpleQuery) iobject;

		userModel.update (simpleQuery.getIObjectListResults ());
	}

	/**
	 * Store the gui values to the data object.
	 */
	@Override
	public void storeToObject (IObject iobject)
	{
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane ()
	{
		return new UserListPane ();
	}
}

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
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.ITextField;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectTableModel;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.room.Room;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;


/**
 * This gui pane is used to edit a room.
 *
 * @version $Id: EditRoomPane.java,v 1.9 2006/09/25 10:34:31 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class EditRoomPane extends SwingGUIPane
{
	/** The room name. */
	public ITextField name;

	/** Table containing all participants. */
	public JTable participantTable;

	/** Participant table model. */
	private IObjectTableModel participantModel;

	/**
	 * Save the data object and close the display.
	 */
	public Action okAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			storeToObject ();
			display.close ();
		}
	};

	/**
	 * Close the display.
	 */
	public Action cancelAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			display.close ();
		}
	};

	/**
	 * Create a new EditRoomPane.
	 */
	public EditRoomPane ()
	{
		super ("EditRoomPane");
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

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource ("/swixml/EditRoomPane.xml"));

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			participantModel = new IObjectTableModel ()
			{
				private String[] columnNames = new String[]
				{
					getResources ().getString ("aktario.name")
				};

				private Class[] columnClasses = new Class[]
				{
					String.class
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

				@Override
				public Class getColumnClass (int col)
				{
					return columnClasses[col];
				}

				@Override
				public boolean isCellEditable (int row, int column)
				{
					return false;
				}

				public Object getValueAt (int row, int col)
				{
					return "";
				}

				@Override
				public void setValueAt (Object value, int row, int col)
				{
				}
			};
			participantTable.setModel (participantModel);
		}
		catch (Exception x)
		{
			Log.logError ("client", "EditRoomPane.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	@Override
	public void loadFromObject (IObject iobject)
	{
		Room room = (Room) iobject;

		setTitle (getResources ().getString ("aktario.room") + " - " + room.getName ());

		name.setText (room.getName ());
		name.selectAll ();

		participantModel.update (room.getParticipants ());
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	@Override
	public void storeToObject (IObject iobject)
	{
		Room room = (Room) iobject;

		room.setName (name.getText ());

		room.update ();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane ()
	{
		return new EditRoomPane ();
	}
}

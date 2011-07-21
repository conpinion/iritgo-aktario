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
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectTableModel;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.room.Room;
import de.iritgo.aktario.core.user.AktarioUser;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.client.command.ShowDialog;
import de.iritgo.aktario.framework.client.command.ShowWindow;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.simplelife.math.NumberTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Properties;


/**
 * RoomControlPane.
 *
 * @version $Id: RoomControlPane.java,v 1.12 2006/10/05 15:00:42 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class RoomControlPane extends SwingGUIPane
{
	/** Table containing all rooms. */
	public JTable roomTable;

	/** Room table model. */
	private IObjectTableModel roomModel;

	/** ScrollPane containing the room table. */
	public JScrollPane roomScrollPane;

	/** Room icon. */
	private ImageIcon roomIcon;

	/** Active room icon. */
	private ImageIcon activeRoomIcon;

	/** The index of the currently active room (or -1). */
	private int activeRoomIndex;

	/**
	 * Create a new room.
	 */
	public Action newRoomAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
		}
	};

	/**
	 * Open a room.
	 */
	public Action openRoomAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			AktarioUser user = (AktarioUser) getIObject ();

			activeRoomIndex = roomTable.getSelectedRow ();

			Room room = user.getRoom (activeRoomIndex);

			if (NumberTools.toBool (getDisplay ().getProperty ("openNewFrame"), false))
			{
				Properties props = new Properties ();

				props.put ("closable", Boolean.FALSE);
				props.put ("iconifiable", Boolean.FALSE);
				props.put ("maximizable", Boolean.FALSE);
				props.put ("maximized", Boolean.TRUE);
				props.put ("titlebar", Boolean.FALSE);

				CommandTools.performAsync (new ShowWindow ("RoomPane", "roomFrame", room), props);
			}
			else
			{
				JDesktopPane frame = ((AktarioGUI) Client.instance ().getClientGUI ()).getDesktopPane ();
				Rectangle bounds = frame.getBounds ();

				Properties props = new Properties ();

				props.put ("iconifiable", new Boolean (false));
				props.put ("bounds", new Rectangle (0, 0, bounds.width - bounds.width / 6, bounds.height));

				CommandTools.performAsync (new ShowWindow ("RoomPane", room), props);
			}

			roomTable.repaint ();
			openRoomAction.setEnabled (false);
		}
	};

	/**
	 * Manage the participants of a room.
	 */
	public Action participantsAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
		}
	};

	/**
	 * Edit the selected room.
	 */
	public Action editRoomAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			CommandTools.performAsync (new ShowDialog ("EditRoomPane", ((AktarioUser) getIObject ()).getRoom (roomTable
							.getSelectedRow ())));
		}
	};

	/**
	 * Create a new RoomControlPane.
	 */
	public RoomControlPane ()
	{
		super ("RoomControlPane");
		activeRoomIndex = - 1;
	}

	/**
	 * Initialize the gui.
	 */
	@Override
	public void initGUI ()
	{
		try
		{
			final ResourceService resources = Engine.instance ().getResourceService ();

			SwingEngine swingEngine = new SwingEngine (this);

			swingEngine.setClassLoader (RoomControlPane.class.getClassLoader ());

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource ("/swixml/RoomControlPane.xml"));

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			roomIcon = new ImageIcon (RoomControlPane.class.getResource ("/resources/room.png"));

			activeRoomIcon = new ImageIcon (RoomControlPane.class.getResource ("/resources/room-active.png"));

			roomModel = new IObjectTableModel ()
			{
				private String[] columnNames = new String[]
				{
					resources.getString ("aktario.rooms")
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
					AktarioUser user = (AktarioUser) getIObject ();

					Room room = user.getRoom (row);

					switch (col)
					{
						case 0:
							return room.getName ();

						default:
							return null;
					}
				}

				@Override
				public void setValueAt (Object value, int row, int col)
				{
				}
			};
			roomTable.setModel (roomModel);

			roomTable.getColumnModel ().getColumn (0).setCellRenderer (new DefaultTableCellRenderer ()
			{
				Color disabledFg;

				Font enabledFont;

				@Override
				public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
								boolean hasFocus, int row, int column)
				{
					super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);

					if (activeRoomIndex == row)
					{
						if (enabledFont == null)
						{
							enabledFont = new Font (getFont ().getFamily (), Font.BOLD, getFont ().getSize ());
						}

						setIcon (activeRoomIcon);
						setFont (enabledFont);
					}
					else
					{
						if (disabledFg == null)
						{
							disabledFg = new Color ((getForeground ().getRed () + getBackground ().getRed ()) / 2,
											(getForeground ().getGreen () + getBackground ().getGreen ()) / 2,
											(getForeground ().getBlue () + getBackground ().getBlue ()) / 2);
						}

						setIcon (roomIcon);

						if (! isSelected)
						{
							setForeground (disabledFg);
						}

						setFont (table.getFont ());
					}

					return this;
				}
			});

			roomTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
			{
				public void valueChanged (ListSelectionEvent e)
				{
					openRoomAction.setEnabled (roomTable.getSelectedRow () != - 1 && activeRoomIndex == - 1);
					participantsAction.setEnabled (roomTable.getSelectedRow () != - 1);
					editRoomAction.setEnabled (roomTable.getSelectedRow () != - 1);
				}
			});

			roomTable.setShowGrid (false);
			roomTable.setRowHeight (Math.max (roomTable.getRowHeight () + 4, roomIcon.getIconHeight () + 4));
			roomScrollPane.getColumnHeader ().setVisible (false);

			openRoomAction.setEnabled (false);
			participantsAction.setEnabled (false);
			editRoomAction.setEnabled (false);

			getDisplay ().setIcon (new ImageIcon (RoomControlPane.class.getResource ("/resources/rooms.png")));

			AppContext.instance ().put ("roomControlPane", this);
		}
		catch (Exception x)
		{
			Log.logError ("client", "RoomNavigator.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	@Override
	public void loadFromObject (IObject iobject)
	{
		AktarioUser user = (AktarioUser) iobject;

		roomModel.update (user.getRooms ());
	}

	/**
	 * Store the current gui values into the data object attributes.
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
		return new RoomControlPane ();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new AktarioUser ();
	}

	/**
	 * This method is called when a room display was closed.
	 */
	public void roomClosed ()
	{
		activeRoomIndex = - 1;
		roomTable.repaint ();
		openRoomAction.setEnabled (roomTable.getSelectedRow () != - 1);
	}
}

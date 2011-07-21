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
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectTableModel;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.user.AktarioUser;
import de.iritgo.aktario.core.user.AktarioUserRegistry;
import de.iritgo.aktario.core.user.AktarioUserStateServerAction;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.command.CommandTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;


/**
 * ParticipantControlPane.
 *
 * @version $Id: ParticipantControlPane.java,v 1.11 2006/09/25 10:34:31 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class ParticipantControlPane extends SwingGUIPane
{
	/** Main tabbed pane. */
	public JTabbedPane tabs;

	/** Table containing all participants. */
	public JTable participantTable;

	/** Participant table model. */
	private IObjectTableModel participantModel;

	/** ScrollPane containing the participant table. */
	public JScrollPane participantScrollPane;

	/** Table containing all contacts. */
	public JTable contactTable;

	/** ScrollPane containing the contact table. */
	public JScrollPane contactScrollPane;

	/** Online ready participant icons. */
	private ImageIcon[] readyParticipantIcons;

	/** Online busy participant icons. */
	private ImageIcon[] busyParticipantIcons;

	/** Offline participant. */
	private ImageIcon offlineParticipantIcon;

	/** Participants that are online. */
	private Set onlineParticipants;

	/** Participants that are busy. */
	private Set busyParticipants;

	/** Participants that are allowed to work on the board. */
	private Set boardParticipants;

	/**
	 * Send a message to a participant.
	 */
	public Action messageAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
		}
	};

	/**
	 * Search for a user.
	 */
	public Action searchAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
		}
	};

	/**
	 * Delete a participant.
	 */
	public Action deleteAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
		}
	};

	/**
	 * Display participant information.
	 */
	public Action infoAction = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
		}
	};

	/**
	 * Create a new ParticipantControlPane.
	 */
	public ParticipantControlPane ()
	{
		super ("ParticipantControlPane");
		onlineParticipants = new HashSet ();
		busyParticipants = new HashSet ();
		boardParticipants = new HashSet ();
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

			Properties props = new Properties ();
			final int role = ((Integer) CommandTools.performSimple ("GetUserRole", props)).intValue ();

			SwingEngine swingEngine = new SwingEngine (this);

			swingEngine.setClassLoader (ParticipantControlPane.class.getClassLoader ());

			String participantSwixmlDescription = properties.getProperty ("participantSwixmlDescription",
							"ParticipantControlPane.xml");

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource (
							"/swixml/" + participantSwixmlDescription));

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			offlineParticipantIcon = new ImageIcon (ParticipantControlPane.class
							.getResource ("/resources/user-offline.png"));

			ImageIcon onlineParticipantIcon = new ImageIcon (ParticipantControlPane.class
							.getResource ("/resources/user-online.png"));

			ImageIcon[] emblems = new ImageIcon[3];

			emblems[0] = new ImageIcon (ParticipantControlPane.class.getResource ("/resources/admin-emblem.png"));

			emblems[1] = new ImageIcon (ParticipantControlPane.class.getResource ("/resources/student-emblem.png"));

			emblems[2] = new ImageIcon (ParticipantControlPane.class.getResource ("/resources/teacher-emblem.png"));

			readyParticipantIcons = new ImageIcon[3];

			for (int i = 0; i < 3; ++i)
			{
				BufferedImage image = new BufferedImage (onlineParticipantIcon.getIconWidth (), onlineParticipantIcon
								.getIconHeight (), BufferedImage.TYPE_INT_ARGB);
				Graphics g = image.getGraphics ();

				g.drawImage (onlineParticipantIcon.getImage (), 0, 0, panel);
				g.drawImage (emblems[i].getImage (),
								onlineParticipantIcon.getIconWidth () - emblems[i].getIconWidth (),
								onlineParticipantIcon.getIconHeight () - emblems[i].getIconHeight (), panel);
				readyParticipantIcons[i] = new ImageIcon (image);
			}

			ImageIcon busyEmblem = new ImageIcon (ParticipantControlPane.class
							.getResource ("/resources/busy-emblem.png"));

			busyParticipantIcons = new ImageIcon[3];

			for (int i = 0; i < 3; ++i)
			{
				BufferedImage image = new BufferedImage (onlineParticipantIcon.getIconWidth (), onlineParticipantIcon
								.getIconHeight (), BufferedImage.TYPE_INT_ARGB);
				Graphics g = image.getGraphics ();

				g.drawImage (readyParticipantIcons[i].getImage (), 0, 0, panel);
				g.drawImage (busyEmblem.getImage (),
								onlineParticipantIcon.getIconWidth () - busyEmblem.getIconWidth (), 0, panel);
				busyParticipantIcons[i] = new ImageIcon (image);
			}

			participantModel = new IObjectTableModel ()
			{
				private String[] columnNames = new String[]
				{
					resources.getString ("aktario.participant")
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
					AktarioUserRegistry users = (AktarioUserRegistry) getIObject ();

					AktarioUser user = (AktarioUser) users.getUser (row);

					switch (col)
					{
						case 0:
							return user.getFullName ();

						default:
							return null;
					}
				}

				@Override
				public void setValueAt (Object value, int row, int col)
				{
					AktarioUserRegistry users = (AktarioUserRegistry) getIObject ();

					@SuppressWarnings("unused")
					AktarioUser user = (AktarioUser) users.getUser (row);

					switch (col)
					{
						case 0:
							break;

						default:
							break;
					}
				}
			};
			participantTable.setModel (participantModel);

			participantTable.getColumnModel ().getColumn (0).setCellRenderer (new DefaultTableCellRenderer ()
			{
				Color disabledFg;

				Font enabledFont;

				@Override
				public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
								boolean hasFocus, int row, int column)
				{
					super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);

					AktarioUserRegistry users = (AktarioUserRegistry) getIObject ();

					AktarioUser user = (AktarioUser) users.getUser (row);

					if (onlineParticipants.contains (user))
					{
						if (enabledFont == null)
						{
							enabledFont = new Font (getFont ().getFamily (), Font.BOLD, getFont ().getSize ());
						}

						setIcon (busyParticipants.contains (user) ? busyParticipantIcons[user.getRole ()]
										: readyParticipantIcons[user.getRole ()]);
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

						setIcon (offlineParticipantIcon);

						if (! isSelected)
						{
							setForeground (disabledFg);
						}

						setFont (table.getFont ());
					}

					return this;
				}
			});

			participantTable.setShowGrid (false);
			participantTable.setRowHeight (Math.max (participantTable.getRowHeight () + 4, offlineParticipantIcon
							.getIconHeight () + 4));
			participantScrollPane.getColumnHeader ().setVisible (false);

			if (role == AktarioUser.ROLE_ADMIN)
			{
				participantTable.addMouseListener (new MouseAdapter ()
				{
					@Override
					public void mouseClicked (MouseEvent e)
					{
						if (e.getClickCount () == 1)
						{
							sendEnableLessonAction ();
						}
					}
				});
			}

			participantTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
			{
				public void valueChanged (ListSelectionEvent e)
				{
					messageAction.setEnabled (participantTable.getSelectedRow () != - 1);
					deleteAction.setEnabled (participantTable.getSelectedRow () != - 1);
					infoAction.setEnabled (participantTable.getSelectedRow () != - 1);
				}
			});

			tabs.setTitleAt (0, resources.getString ("aktario.contacts"));

			if (tabs.getTabCount () > 1)
			{
				tabs.setTitleAt (1, resources.getString ("aktario.room"));
			}

			messageAction.setEnabled (false);
			deleteAction.setEnabled (false);
			infoAction.setEnabled (false);

			getDisplay ().setIcon (new ImageIcon (ParticipantControlPane.class.getResource ("/resources/people.png")));

			AppContext.instance ().put ("participantControlPane", this);
		}
		catch (Exception x)
		{
			Log.logError ("client", "Navigator", x.toString ());
		}

		CommandTools.performAsync (new Command ()
		{
			@Override
			public void perform ()
			{
				AktarioUserStateServerAction action = new AktarioUserStateServerAction (AppContext.instance ()
								.getUser ());
				ClientTransceiver transceiver = new ClientTransceiver (AppContext.instance ().getChannelNumber ());

				transceiver.addReceiver (AppContext.instance ().getChannelNumber ());
				action.setTransceiver (transceiver);
				ActionTools.sendToServer (action);
			}
		});
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	@Override
	public void loadFromObject (IObject iobject)
	{
		AktarioUserRegistry userRegistry = (AktarioUserRegistry) iobject;

		participantModel.update (userRegistry.getUsers ());
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
		return new ParticipantControlPane ();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new AktarioUserRegistry ();
	}

	/**
	 * Send an action to enable/disable lesson displays.
	 */
	public void sendEnableLessonAction ()
	{
		// 		ClientTransceiver transceiver =
		// 			new ClientTransceiver(AppContext.instance ().getChannelNumber ());
		// 		transceiver.addReceiver (AppContext.instance ().getChannelNumber ());
		// 		AktarioUserRegistry users = (AktarioUserRegistry) iobject;
		// 		EnableLessonServerAction action = new EnableLessonServerAction();
		// 		for (Iterator i = boardParticipants.iterator (); i.hasNext ();)
		// 		{
		// 			action.addUser ((AktarioUser) i.next ());
		// 		}
		// 		action.setTransceiver (transceiver);
		// 		ActionTools.sendToServer (action);
	}

	/**
	 * Set the connection state of a participant.
	 *
	 * @param participantIds The ids of the participants.
	 * @param connected True if the participant is currently connected.
	 */
	public void setUserConnectionState (List participantIds, boolean connected)
	{
		for (Iterator i = participantIds.iterator (); i.hasNext ();)
		{
			long participantId = ((Long) i.next ()).longValue ();

			AktarioUser user = ((AktarioUserRegistry) getIObject ()).getUserById (participantId);

			if (connected)
			{
				onlineParticipants.add (user);
			}
			else
			{
				onlineParticipants.remove (user);
			}
		}

		participantTable.repaint ();
	}

	/**
	 * Set all participants busy.
	 */
	public void setUsersBusy ()
	{
		for (Iterator i = ((AktarioUserRegistry) getIObject ()).userIterator (); i.hasNext ();)
		{
			AktarioUser user = (AktarioUser) i.next ();

			if (user.getRole () == AktarioUser.ROLE_USER)
			{
				busyParticipants.add (user);
			}
		}

		participantTable.repaint ();
	}

	/**
	 * Set a participant ready.
	 */
	public void setUserReady (long participantId, boolean ready)
	{
		AktarioUser user = ((AktarioUserRegistry) getIObject ()).getUserById (participantId);

		if (ready)
		{
			busyParticipants.remove (user);
		}
		else
		{
			busyParticipants.add (user);
		}

		participantTable.repaint ();
	}

	/**
	 * Update the enabled state display.
	 *
	 * @param enabledParticipantIds List of ids of the enabled participants.
	 */
	public void setEnabledUserIds (List enabledParticipantIds)
	{
		boardParticipants.clear ();

		AktarioUserRegistry users = (AktarioUserRegistry) getIObject ();

		for (Iterator i = enabledParticipantIds.iterator (); i.hasNext ();)
		{
			long participantId = ((Long) i.next ()).longValue ();

			boardParticipants.add (users.getUserById (participantId));
		}

		participantTable.repaint ();
	}
}

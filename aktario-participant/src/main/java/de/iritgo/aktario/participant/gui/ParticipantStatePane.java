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

package de.iritgo.aktario.participant.gui;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.IButton;
import de.iritgo.aktario.core.gui.ITableSorter;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.IObjectTableModelSorted;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.client.command.ShowDialog;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.dataobject.DynDataObject;
import de.iritgo.aktario.participant.ParticipantManager;
import de.iritgo.aktario.participant.ParticipantPlugin;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * This gui pane displays a list of all users and lets the administrator
 * add, edit, and delete users.
 *
 * @version $Id: ParticipantStatePane.java,v 1.19 2006/09/25 10:34:30 grappendorf Exp $
 */
public class ParticipantStatePane extends SwingGUIPane implements TableModelListener
{
	/** The table of all users. */
	public JTable participantStateTable;

	/** Model for the user table. */
	private IObjectTableModelSorted participantStateModel;

	/** The edit button. */
	public IButton btnEdit;

	/** The delete button. */
	public IButton btnDelete;

	private ParticipantManager participantManager;

	private List childGUIPanes;

	/** ScrollPane containing the participant state table. */
	public JScrollPane participantStateScrollPane;

	public Border border = BorderFactory.createLineBorder (Color.black);

	private ITableSorter tableSorter;

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
	public ParticipantStatePane ()
	{
		super ("ParticipantStatePane");
		childGUIPanes = new LinkedList ();
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		try
		{
			participantManager = (ParticipantManager) Engine.instance ().getManagerRegistry ().getManager (
							"ParticipantClientManager");

			SwingEngine swingEngine = new SwingEngine (this);

			AppContext.instance ().put (onScreenUniqueId, this);

			swingEngine.setClassLoader (ParticipantPlugin.class.getClassLoader ());

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource ("/swixml/ParticipantStatePane.xml"));

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			Properties props = new Properties ();

			props.put ("content", content);

			// 			for (Iterator i = participantManager.contentCommandIterator (); i.hasNext ();)
			// 			{
			// 				childGUIPanes.add (CommandTools.performSimple ((Command) i.next (), props));
			// 			}
			participantStateModel = new IObjectTableModelSorted ()
			{
				public int getColumnCount ()
				{
					DynDataObject pts = null;

					try
					{
						pts = (DynDataObject) Engine.instance ().getIObjectFactory ().newInstance ("ParticipantState");
					}
					catch (NoSuchIObjectException x)
					{
						x.printStackTrace ();
					}

					return pts.getNumAttributes ();
				}

				public String getColumnName (int col)
				{
					DynDataObject pts = null;

					try
					{
						pts = (DynDataObject) Engine.instance ().getIObjectFactory ().newInstance ("ParticipantState");
					}
					catch (NoSuchIObjectException x)
					{
						x.printStackTrace ();
					}

					return Engine.instance ().getResourceService ().getStringWithoutException (
									(String) new LinkedList (pts.getAttributes ().keySet ()).get (col));
				}

				public boolean isCellEditable (int row, int col)
				{
					return false;
				}

				public Object getValueAt (int row, int col)
				{
					IObjectList query = ((DataObject) getIObject ()).getIObjectListAttribute ("participants");

					DynDataObject participantState = (DynDataObject) query.get (row);

					Object object = new LinkedList (participantState.getAttributes ().values ()).get (col);

					return object;
				}
			};

			participantStateTable.setShowGrid (false);

			participantStateTable.setCellSelectionEnabled (false);

			participantStateTable.setRowSelectionAllowed (true);

			participantStateTable.setSelectionMode (0);

			participantStateTable.setRowHeight (Math.max (participantStateTable.getRowHeight () + 4, 24 + 4));

			DynDataObject pts = null;

			try
			{
				pts = (DynDataObject) Engine.instance ().getIObjectFactory ().newInstance ("ParticipantState");

				tableSorter = participantStateModel.getTableSorter ();
				participantStateTable.setModel (tableSorter);
				participantStateModel.addTableModelListener (this);

				int j = 0;

				Properties properties = new Properties ();

				properties.setProperty ("participantStateName", onScreenUniqueId);

				for (Iterator i = new LinkedList (pts.getAttributes ().keySet ()).iterator (); i.hasNext ();)
				{
					String attributeName = (String) i.next ();

					Command command = participantManager.getAttributeRenderCommand (attributeName);

					if (command != null)
					{
						participantStateTable.getColumnModel ().getColumn (j).setCellRenderer (
										(DefaultTableCellRenderer) CommandTools.performSimple (command, properties));
					}

					command = participantManager.getAttributeEditorCommand (attributeName);

					if (command != null)
					{
						participantStateTable.getColumnModel ().getColumn (j).setCellEditor (
										(TableCellEditor) CommandTools.performSimple (command, properties));
					}

					++j;
				}
			}
			catch (NoSuchIObjectException x)
			{
				x.printStackTrace ();
			}

			participantStateTable.addMouseListener (new MouseAdapter ()
			{
				public void mouseClicked (MouseEvent e)
				{
					int col = participantStateTable.columnAtPoint (e.getPoint ());
					int row = tableSorter.getRealRow (participantStateTable.getSelectedRow ());

					if ((col < 0) || (row < 0))
					{
						return;
					}

					IObjectList list = ((DataObject) getIObject ()).getIObjectListAttribute ("participants");

					DynDataObject participantState = (DynDataObject) list.get (row);

					String attributeName = (String) new LinkedList (participantState.getAttributes ().keySet ())
									.get (col);

					if (e.getClickCount () == 2)
					{
						Properties props = new Properties ();

						props.put ("bounds", new Rectangle (0, 0, 300, 100));
						props.setProperty ("targetUser", participantState.getStringAttribute ("iritgoUserName"));
						props.put ("weightx", new Double (3.0));
						props.put ("weighty", new Double (1.5));
						CommandTools.performAsync (new ShowDialog ("InstantMessageSendPane"), props);

						return;
					}

					Command command = participantManager.getAttributeCommand (attributeName);

					if (command != null)
					{
						Properties props = command.getProperties ();

						props.put ("participantState", participantState);
						props.put ("mousePosition", e.getPoint ());
						props.put ("table", participantStateTable);
						CommandTools.performSimple (command);
					}
				}
			});

			participantStateTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
			{
				public void valueChanged (ListSelectionEvent e)
				{
				}
			});

			//   			participantStateScrollPane.getColumnHeader ().setVisible (false);
		}
		catch (Exception x)
		{
			Log.logError ("client", "ParticipantStatePane.initGUI", x.toString ());
			x.printStackTrace ();
		}
	}

	/**
	 * Load the data object into the gui.
	 */
	public void loadFromObject (IObject iobject)
	{
		try
		{
			DataObject buddyListGroup = (DataObject) iobject;

			participantStateModel.update (buddyListGroup.getIObjectListAttribute ("participants"));

			// 			for (Iterator i = childGUIPanes.iterator (); i.hasNext ();)
			// 			{
			// 				((GUIPane) i.next ()).loadFromObject (iobject);
			// 			}
		}
		catch (Exception x)
		{
			x.printStackTrace ();
		}

		// 		SimpleQuery simpleQuery = (SimpleQuery) iobject;

		// 		

		// 		participantStateModel.update (simpleQuery.getIObjectListResults (), transaction);
		// 		

		// 		for (Iterator i = childGUIPanes.iterator (); i.hasNext ();)
		// 		{
		// 			((GUIPane) i.next ()).loadFromObject (iobject);
		// 		}
	}

	public void tableChanged (TableModelEvent e)
	{
		// 		IObject iObject = getIObject ();

		// 		for (Iterator i = childGUIPanes.iterator (); i.hasNext ();)
		// 		{
		// 			((GUIPane) i.next ()).loadFromObject (iObject);
		// 		}
		tableSorter.reallocateIndexesUpdate ();
		tableSorter.sortByColumn (2);
	}

	/**
	 * Store the gui values to the data object.
	 */
	public void storeToObject (IObject iobject)
	{
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new ParticipantStatePane ();
	}

	public DynDataObject getParticipantState (int row)
	{
		IObjectList list = ((DataObject) getIObject ()).getIObjectListAttribute ("participants");

		DynDataObject participantState = (DynDataObject) list.get (tableSorter.getRealRow (row));

		return participantState;
	}
}

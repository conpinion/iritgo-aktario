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
import de.iritgo.aktario.core.user.AktarioUserRegistry;
import org.swixml.SwingEngine;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.awt.GridBagConstraints;


/**
 * ToolControlPane.
 *
 * @version $Id: ToolControlPane.java,v 1.9 2006/09/25 10:34:31 grappendorf Exp $
 */
public class ToolControlPane extends SwingGUIPane
{
	/** Table containing all tools. */
	public JTable toolTable;

	/** Tool table model. */
	private IObjectTableModel toolModel;

	/** ScrollPane containing the tool table. */
	public JScrollPane toolScrollPane;

	/** Tool icons. */
	private ImageIcon[] toolIcons;

	/**
	 * Create a new ToolControlPane.
	 */
	public ToolControlPane ()
	{
		super ("ToolControlPane");
	}

	/**
	 * Initialize the gui.
	 */
	@SuppressWarnings("serial")
	@Override
	public void initGUI ()
	{
		try
		{
			final ResourceService resources = Engine.instance ().getResourceService ();

			SwingEngine swingEngine = new SwingEngine (this);

			swingEngine.setClassLoader (ToolControlPane.class.getClassLoader ());

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource ("/swixml/ToolControlPane.xml"));

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			toolIcons = new ImageIcon[1];

			toolIcons[0] = new ImageIcon (RoomControlPane.class.getResource ("/resources/tool-big-message.png"));

			toolModel = new IObjectTableModel ()
			{
				private String[] columnNames = new String[]
				{
					resources.getString ("aktario.tools")
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
				public int getRowCount ()
				{
					return 1;
				}

				@Override
				public boolean isCellEditable (int row, int column)
				{
					return false;
				}

				public Object getValueAt (int row, int col)
				{
					return "Nachrichten";
				}

				@Override
				public void setValueAt (Object value, int row, int col)
				{
				}
			};
			toolTable.setModel (toolModel);

			toolTable.getColumnModel ().getColumn (0).setCellRenderer (new DefaultTableCellRenderer ()
			{
				@Override
				public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
								boolean hasFocus, int row, int column)
				{
					super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);

					setIcon (toolIcons[0]);

					return this;
				}
			});

			toolTable.setShowGrid (false);
			toolTable.setRowHeight (Math.max (toolTable.getRowHeight () + 4, toolIcons[0].getIconHeight () + 4));
			toolScrollPane.getColumnHeader ().setVisible (false);

			getDisplay ().setIcon (new ImageIcon (ToolControlPane.class.getResource ("/resources/tool.png")));
		}
		catch (Exception x)
		{
			Log.logError ("client", "ToolNavigator.initGUI", x.toString ());
			x.printStackTrace ();
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	@Override
	public void loadFromObject (IObject iobject)
	{
		toolTable.revalidate ();
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
		return new ToolControlPane ();
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
}

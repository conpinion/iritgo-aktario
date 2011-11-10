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

package de.iritgo.aktario.infocenter.guinetworkdisplay.gui;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.ILabel;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.framework.client.command.ShowWindow;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.infocenter.manager.InfoCenterClientManager;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;


/**
 *
 */
public class NetworkDisplayGUIPane extends SwingGUIPane implements GUIDisplay
{
	private JTable infosList;

	private JScrollPane scrollpane;

	private final java.util.LinkedList infos = new LinkedList();

	private TableModel data;

	/**
	 * Standard constructor
	 */
	public NetworkDisplayGUIPane()
	{
		super("infocenter.networkdisplay");
	}

	/**
	 * Init GUI
	 */
	public void initGUI()
	{
		InfoCenterClientManager infoCenterClientManager = (InfoCenterClientManager) Engine.instance()
						.getManagerRegistry().getManager("infocenterclient");

		infoCenterClientManager.setGUIDisplay(this);

		JPanel allPanel = new JPanel();

		allPanel.setLayout(new GridBagLayout());

		int row = 0;

		ILabel infosLabel = new ILabel("infocenter.infos");

		allPanel.add(infosLabel, createConstraints(0, row++, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, new Insets(5,
						15, 5, 15)));

		data = new AbstractTableModel()
		{
			public int getColumnCount()
			{
				return 1;
			}

			public int getRowCount()
			{
				return infos.size();
			}

			public boolean isCellEditable(int row, int col)
			{
				return false;
			}

			public Class getColumnClass(int c)
			{
				return new InfoItem("unknown").getPanel().getClass();
			}

			public Object getValueAt(int row, int col)
			{
				InfoItem item = (InfoItem) infos.get(row);

				return item;
			}
		};

		infosList = new JTable(data);

		MouseListener mouseListener = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					int row = infosList.rowAtPoint(e.getPoint());
					InfoItem item = (InfoItem) infosList.getValueAt(row, 0);

					CommandTools
									.performAsync(new ShowWindow(item.getGuiPaneId(), item.getUniqueId(), item
													.getTypeId()));
				}
			}
		};

		infosList.addMouseListener(mouseListener);
		infosList.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		infosList.setDefaultRenderer(infosList.getColumnClass(0), new InfoCellRenderer());

		infosList.setRowHeight(70);

		scrollpane = new JScrollPane(infosList);
		allPanel.add(scrollpane, createConstraints(0, row, 1, 1, GridBagConstraints.BOTH, 100, 100, new Insets(5, 15,
						5, 15)));

		content.add(allPanel, createConstraints(0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
	}

	/**
	 * LoadFormObject, load the Data form Object.
	 */
	public void loadFromObject(IObject iObject)
	{
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject(IObject iObject)
	{
	}

	public void addInfo(final int context, final String category, final String icon, final String message,
					final String guiPaneId, final long uniqueId, final String iObjectTypeId, final int level)
	{
		try
		{
			// Its called form other thread we must sync it with the gui thread
			SwingUtilities.invokeAndWait(new Runnable()
			{
				public void run()
				{
					InfoItem item = new InfoItem(category, icon, "M: " + message, guiPaneId, uniqueId, iObjectTypeId,
									level);

					infos.addFirst(item);
					infosList.revalidate();
				}
			});
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Close it.
	 */
	public void close()
	{
		super.close();
	}

	/**
	 * Return a sample object from this guipane. It will use only for a "reloadplugin" command. Refactoring? Redesign?
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject()
	{
		return null;
	}

	/**
	 * Return a new instance.
	 */
	public GUIPane cloneGUIPane()
	{
		return new NetworkDisplayGUIPane();
	}
}

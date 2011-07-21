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

package de.iritgo.aktario.framework.dataobject.gui;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.ITableSorter;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.IObjectTableModelSorted;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.dataobject.AbstractQuery;
import de.iritgo.aktario.framework.dataobject.gui.DefaultRenderer.ComboboxItem;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 *
 */
public class QueryPane extends SwingGUIPane implements DynPane
{
	private class ComboBoxListener implements ItemListener
	{
		private AbstractQuery query;

		private DefaultRenderer.ComboboxItem lastSelectedItem;

		public ComboBoxListener (AbstractQuery query, DefaultRenderer.ComboboxItem lastSelectedItem)
		{
			this.query = query;
			this.lastSelectedItem = lastSelectedItem;
		}

		public void itemStateChanged (ItemEvent e)
		{
			if (e.getStateChange () == ItemEvent.SELECTED)
			{
				DefaultRenderer.ComboboxItem item = (DefaultRenderer.ComboboxItem) e.getItem ();

				lastSelectedItem = item;

				String id = item.getId ();

				query.setAttribute ("listSearchCategory", id);

				IObjectList results = (IObjectList) query.getIObjectListResults ();

				results.clearIObjectList ();
				query.update ();
			}
		}

		public DefaultRenderer.ComboboxItem getLastSelectedItem ()
		{
			return lastSelectedItem;
		}
	}

	private JPanel panel;

	private JTable table;

	private boolean guiRendered;

	private String rendererTypeId;

	private String controllerTypeId;

	private IObjectTableModelSorted model;

	private JComboBox combobox;

	private Renderer renderer;

	private ComboBoxListener comboBoxListenerImpl;

	/**
	 * Close the display
	 */
	@SuppressWarnings("serial")
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
	public QueryPane ()
	{
		super ("QueryPane");
		combobox = null;
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		rendererTypeId = properties.getProperty ("renderer", ((DataObject) getIObject ())
						.getStringAttribute ("dataObjectTypeId"));
		controllerTypeId = properties.getProperty ("controller", ((DataObject) getIObject ())
						.getStringAttribute ("dataObjectTypeId"));

		GUIManager guiManager = (GUIManager) Engine.instance ().getManagerRegistry ().getManager ("GUIManager");

		guiManager.registerGUIPane (this);

		Controller controller = guiManager.getController (controllerTypeId);

		if (controller == null)
		{
			GUIControllerRequest guiControllerRequest = new GUIControllerRequest (controllerTypeId,
							GUIControllerRequest.QUERY);

			ActionTools.sendToServer (guiControllerRequest);
		}
		else
		{
			if (controller.isValid ())
			{
				updateGUI ();
			}
		}
	}

	public void setModel (IObjectTableModelSorted model)
	{
		this.model = model;
	}

	public IObjectTableModelSorted getModel ()
	{
		return model;
	}

	public String getControllerTypeId ()
	{
		return controllerTypeId;
	}

	public String getRendererTypeId ()
	{
		return controllerTypeId;
	}

	/**
	 * Update the gui if a widget or command description has changed. All
	 * descriptions must have a valid state.
	 */
	public void updateGUI ()
	{
		final QueryPane pane = this;

		try
		{
			SwingUtilities.invokeAndWait (new Runnable ()
			{
				public void run ()
				{
					combobox = new JComboBox ();

					GUIManager guiManager = (GUIManager) Engine.instance ().getManagerRegistry ().getManager (
									"GUIManager");

					final Controller controller = guiManager.getController (controllerTypeId);

					renderer = guiManager.getQueryRenderer (rendererTypeId);

					if (panel != null)
					{
						content.remove (panel);
					}

					panel = new JPanel ();

					content.add (panel, createConstraints (0, 1, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

					renderer.workOn (controller, (DataObject) getIObject (), panel, pane);

					table = ((DefaultQueryRenderer) renderer).getTable ();

					if (properties.containsKey ("autoResizeColumns"))
					{
						table.setAutoResizeMode ((Integer) properties.get ("autoResizeColumns"));
					}

					if (properties.containsKey ("rowHeight"))
					{
						table.setAlignmentY (JTable.TOP_ALIGNMENT);
						table.setRowHeight ((Integer) properties.get ("rowHeight"));
					}

					if (properties.containsKey ("headerOff"))
					{
						table.setTableHeader (null);
					}

					content.revalidate ();
					table.revalidate ();

					guiRendered = true;

					loadFromObject ();

					table.revalidate ();
				}
			});
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Load the gui-controlls from the object. The gui must in allready loaded.
	 *
	 * @param IObject
	 *            The loaded object.
	 */
	public void loadFromObject (IObject iobject)
	{
		if (guiRendered)
		{
			final AbstractQuery query = (AbstractQuery) iobject;

			GUIManager guiManager = (GUIManager) Engine.instance ().getManagerRegistry ().getManager ("GUIManager");

			Controller controller = guiManager.getController (controllerTypeId);

			DefaultRenderer.ComboboxItem lastSelectedItem = null;

			if (controller.getWidgetDescription ("listSearchCategory") != null)
			{
				combobox = (JComboBox) controller.getWidgetDescription ("listSearchCategory").getControl (
								getOnScreenUniqueId () + "_" + "listSearchCategory");

				int selectedIndex = 0;

				if (comboBoxListenerImpl != null)
				{
					lastSelectedItem = comboBoxListenerImpl.getLastSelectedItem ();
					combobox.setSelectedItem (lastSelectedItem);
					selectedIndex = combobox.getSelectedIndex ();
					combobox.removeItemListener (comboBoxListenerImpl);
				}

				combobox.removeAllItems ();

				String[] validValues = ((String) query.getAttribute ("listSearchValues")).split ("\\|");

				String defaultKey = validValues[0];
				int defaultIndex = 0;

				for (int j = 1; j < validValues.length; ++j)
				{
					String key = validValues[j++];

					if (j < validValues.length)
					{
						String value = validValues[j];

						if (key.equals (defaultKey))
						{
							defaultIndex = (j / 2) - 1;
						}

						combobox.addItem (new DefaultRenderer.ComboboxItem (key, value));
					}
				}

				if (lastSelectedItem != null)
				{
					combobox.setSelectedIndex (selectedIndex);
				}
				else
				{
					combobox.setSelectedIndex (defaultIndex);
					selectedIndex = combobox.getSelectedIndex ();
					lastSelectedItem = (ComboboxItem) combobox.getSelectedItem ();

					DefaultRenderer.ComboboxItem item = (DefaultRenderer.ComboboxItem) lastSelectedItem;
					String id = item.getId ();

					query.setAttribute ("listSearchCategory", id);
				}
			}

			model.update (query.getIObjectListResults ());

			if (table != null)
			{
				table.revalidate ();
			}

			comboBoxListenerImpl = new ComboBoxListener ((AbstractQuery) iobject, lastSelectedItem);
			combobox.addItemListener (comboBoxListenerImpl);

			return;
		}
	}

	/**
	 * Store the gui values to the data object.
	 */
	public void storeToObject (IObject iobject)
	{
	}

	/**
	 * This method is called when the gui pane starts waiting for the attributes
	 * of it's iobject.
	 */
	public void waitingForNewObject ()
	{
		if (renderer != null)
		{
			((DefaultQueryRenderer) renderer).setSearchWaitIcon ();
		}
	}

	/**
	 * This method is called when the attributes of the gui pane's iobject are
	 * received.
	 */
	public void waitingForNewObjectFinished ()
	{
		super.waitingForNewObjectFinished ();

		if (renderer != null)
		{
			((DefaultQueryRenderer) renderer).setSearchIcon ();
		}
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new QueryPane ();
	}

	/**
	 * Retrieve the data object by the current selection.
	 */
	public DataObject getSelectedItem ()
	{
		int row = table.getSelectedRow ();

		if (row < 0 || (table.getRowCount () == 0))
		{
			return null;
		}

		DataObject dataObject = (DataObject) ((ITableSorter) table.getModel ()).getObjectByRow (row);

		return dataObject;
	}

	public void refreshQuery ()
	{
		table.clearSelection ();

		AbstractQuery query = (AbstractQuery) getIObject ();

		IObjectList results = (IObjectList) query.getIObjectListResults ();

		results.clearIObjectList ();
		query.update ();

		table.revalidate ();
	}

	public void deleteListEntry (long dataObjectUniqueId, @SuppressWarnings("unused") String dataObjectType)
	{
		AbstractQuery query = (AbstractQuery) getIObject ();
		DataObject dataObject = (DataObject) Engine.instance ().getBaseRegistry ().get (dataObjectUniqueId,
						query.getDataObjectTypeId ());

		query.getIObjectListResults ().remove ((IObject) dataObject);
	}

	/**
	 * Close the display.
	 */
	public void close ()
	{
		if (renderer != null)
		{
			renderer.close ();
		}

		GUIManager guiManager = (GUIManager) Engine.instance ().getManagerRegistry ().getManager ("GUIManager");

		guiManager.unregisterGUIPane (this);

		renderer = null;
		super.close ();
	}

	/**
	 * Close the display.
	 */
	public void systemClose ()
	{
		if (renderer != null)
		{
			renderer.close ();
		}

		GUIManager guiManager = (GUIManager) Engine.instance ().getManagerRegistry ().getManager ("GUIManager");

		guiManager.unregisterGUIPane (this);

		renderer = null;

		super.systemClose ();
	}

	public void refresh ()
	{
		renderer.refresh ();
	}
}

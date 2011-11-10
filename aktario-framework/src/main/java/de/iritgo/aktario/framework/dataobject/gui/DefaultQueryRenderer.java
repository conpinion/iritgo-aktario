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
import de.iritgo.aktario.core.gui.IButton;
import de.iritgo.aktario.core.gui.ITable;
import de.iritgo.aktario.core.gui.ITableSorter;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.IObjectTableModelSorted;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.dataobject.AbstractQuery;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * DefaultQueryRenderer
 */
public class DefaultQueryRenderer extends Renderer implements TableModelListener
{
	public class ColumnHelper
	{
		public WidgetDescription wd;

		public int columnType;

		public ExtensionTile extensionTile;

		public ColumnHelper(WidgetDescription wd, ExtensionTile extensionTile, int columnType)
		{
			this.wd = wd;
			this.columnType = columnType;
			this.extensionTile = extensionTile;
		}
	}

	public static int DATAOBJECT_COLUMN = 0;

	public static int TRANSIENT_COLUMN = 1;

	/** The table of all users. */
	public JTable queryTable;

	public LinkedList dataObjectButtons;

	/** Model for the user table. */
	private IObjectTableModelSorted queryTableModel;

	/** ScrollPane containing the query state table. */
	public JScrollPane queryScrollPane;

	/** The table sorter helper tablemodel **/
	private ITableSorter tableSorter;

	private ImageIcon newIcon;

	private ImageIcon editIcon;

	private ImageIcon saveIcon;

	private ImageIcon deleteIcon;

	private ImageIcon cancelIcon;

	private ImageIcon searchIcon;

	private ImageIcon searchWait;

	private JTextField searchConditionField;

	private DataObject dataObject;

	private QueryPane queryPane;

	private List<ColumnHelper> wdList;

	private JLabel searchLabel;

	/** */
	private ActionListener executeSearch = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			refresh();
		}
	};

	/**
	 * Default constructor
	 */
	public DefaultQueryRenderer()
	{
		super("DefaultQueryRenderer");
		newIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/new.png"));
		editIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/edit.png"));
		saveIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/save.png"));
		deleteIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/delete.png"));
		cancelIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/cancel.png"));
		searchIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/search.png"));
		searchWait = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/search-rotating.gif"));
		wdList = new ArrayList<ColumnHelper>(8);
		dataObjectButtons = new LinkedList();
	}

	/**
	 * Will be called on a query pane to generate all needed swing components
	 *
	 * @param Controller
	 *            The controller for the dataobject.
	 * @param DataObject
	 *            The data object for the display.
	 * @param Object
	 *            The content container. In swing it is a JPanel object.
	 * @param DataObjectGUIPane
	 *            The pane to render on.
	 */
	public void workOn(Controller controller, DataObject dataObject, Object content, DataObjectGUIPane dataObjectGUIPane)
	{
	}

	/**
	 * Will be called on a query pane to generate all needed swing components
	 *
	 * @param Controller
	 *            The controller for the dataobject.
	 * @param DataObject
	 *            The data object for the display.
	 * @param Object
	 *            The content container. In swing it is a JPanel object.
	 * @param QueryPane
	 *            The pane to render on.
	 */
	public void workOn(final Controller controller, final DataObject dataObject, final Object content,
					final QueryPane queryPane)
	{
		this.dataObject = dataObject;
		this.queryPane = queryPane;

		try
		{
			JPanel panel = (JPanel) new JPanel();
			JPanel toolbar = (JPanel) new JPanel();

			panel.setLayout(new GridBagLayout());
			toolbar.setLayout(new GridBagLayout());

			Properties props = queryPane.getProperties();

			JPanel searchPanel = (JPanel) new JPanel();

			searchPanel.setLayout(new GridBagLayout());

			int searchComponentPos = 0;

			if (Client.instance().getGUIExtensionManager().existsExtension(queryPane.getOnScreenUniqueId(),
							"searchPanel"))
			{
				for (Iterator i = Client.instance().getGUIExtensionManager().getExtensionIterator(
								queryPane.getOnScreenUniqueId(), "searchPanel"); i.hasNext();)
				{
					ExtensionTile extensionTileCommand = (ExtensionTile) i.next();

					searchPanel.add(extensionTileCommand.getTile(queryPane, dataObject, null), createConstraints(
									searchComponentPos, 0, 1, 1, 0.1, 0.0, GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTHWEST, new Insets(2, 2, 2, 2)));
					searchComponentPos++;
				}
			}

			WidgetDescription listSearchCategoryComboBox = controller.getWidgetDescription("listSearchCategory");

			if (listSearchCategoryComboBox != null)
			{
				JComboBox combobox = new JComboBox();

				listSearchCategoryComboBox.addControl(queryPane.getOnScreenUniqueId() + "_"
								+ listSearchCategoryComboBox.getWidgetId(), combobox);

				searchPanel.add(combobox, createConstraints(searchComponentPos, 0, 1, 1, 0.1, 0.0,
								GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, new Insets(2, 2, 2, 2)));

				searchComponentPos++;
			}

			if (props.getProperty("searchpanel", "yes").equals("yes"))
			{
				searchConditionField = new JTextField("");
				searchPanel.add(searchConditionField, createConstraints(searchComponentPos++, 0, 1, 1, 0.8, 0.0,
								GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, new Insets(2, 2, 2, 2)));

				searchLabel = new JLabel(searchIcon);
				searchPanel.add(searchLabel, createConstraints(searchComponentPos++, 0, 1, 1, 0.0, 0.0,
								GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, new Insets(2, 2, 2, 2)));

				searchConditionField.addActionListener(executeSearch);

				if (props.getProperty("searchbutton", "yes").equals("yes"))
				{
					IButton button = new IButton("search", searchIcon);

					toolbar.add(button, createConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTHWEST, new Insets(2, 2, 2, 2)));

					button.addActionListener(executeSearch);
					searchComponentPos++;
				}
			}

			searchPanel.revalidate();

			panel.add(searchPanel, createConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL,
							GridBagConstraints.NORTHWEST, new Insets(2, 2, 2, 2)));

			int y = 1;

			configureTableModel(controller);
			queryPane.setModel(queryTableModel);

			configureTable(queryTableModel);

			int verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;

			if (queryPane.getProperties().containsKey("verticalScrollbarPolicy"))
			{
				verticalScrollbarPolicy = (Integer) queryPane.getProperties().get("verticalScrollbarPolicy");
			}

			int horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;

			if (queryPane.getProperties().containsKey("horizontalScrollbarPolicy"))
			{
				horizontalScrollbarPolicy = (Integer) queryPane.getProperties().get("horizontalScrollbarPolicy");
			}

			queryScrollPane = new JScrollPane(queryTable, verticalScrollbarPolicy, horizontalScrollbarPolicy);

			// queryScrollPane.getColumnHeader ().setVisible (true);
			panel.add(queryScrollPane, createConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH,
							GridBagConstraints.NORTHWEST, new Insets(0, 0, 0, 0)));

			JSeparator sep = new JSeparator();

			if (props.getProperty("toolbar", "no").equals("yes"))
			{
				if (controller.getCommandDescriptions().size() != 0)
				{
					toolbar.add(sep, createConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTHWEST, new Insets(4, 8, 4, 8)));

					y = 2;
				}
				else
				{
					y = 1;
				}

				for (Iterator i = controller.getCommandDescriptions().iterator(); i.hasNext();)
				{
					CommandDescription cd = (CommandDescription) i.next();

					DataObjectButton dataObjectButton = null;

					if (cd.getTextId().equals("new"))
					{
						dataObjectButton = new DataObjectButton(cd.getTextId(), newIcon);
					}
					else if (cd.getIconId().equals("edit"))
					{
						dataObjectButton = new DataObjectButton(cd.getTextId(), editIcon);
					}
					else if (cd.getIconId().equals("save"))
					{
						dataObjectButton = new DataObjectButton(cd.getTextId(), saveIcon);
					}
					else if (cd.getIconId().equals("delete"))
					{
						dataObjectButton = new DataObjectButton(cd.getTextId(), deleteIcon);
					}
					else if (cd.getIconId().equals("cancel"))
					{
						dataObjectButton = new DataObjectButton(cd.getTextId(), cancelIcon);
					}
					else
					{
						dataObjectButton = new DataObjectButton(cd.getTextId());
					}

					dataObjectButton.setDataObject(dataObject);
					dataObjectButton.setSwingGUIPane(queryPane);
					dataObjectButton.setCommandDescription(cd);

					toolbar.add(dataObjectButton,
									createConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL,
													GridBagConstraints.NORTHWEST, new Insets(4, 8, 0, 8)));
					dataObjectButtons.add(dataObjectButton);
					++y;
				}

				if (Client.instance().getGUIExtensionManager().existsExtension(queryPane.getOnScreenUniqueId(),
								"toolbar"))
				{
					sep = new JSeparator();
					toolbar.add(sep, createConstraints(0, y, 1, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTHWEST, new Insets(8, 8, 4, 8)));
					++y;

					for (Iterator i = Client.instance().getGUIExtensionManager().getExtensionIterator(
									queryPane.getOnScreenUniqueId(), "toolbar"); i.hasNext();)
					{
						ExtensionTile extensionTile = (ExtensionTile) i.next();

						toolbar.add(extensionTile.getTile(queryPane, dataObject, null), createConstraints(0, y, 1, 1,
										1.0, 0.0, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST,
										new Insets(4, 8, 0, 8)));
						++y;
					}
				}
			}

			// Surrounding Commands
			if (props.getProperty("surroundingcommands", "no").equals("yes"))
			{
				if (Client.instance().getGUIExtensionManager().existsExtension(queryPane.getOnScreenUniqueId(),
								"surroundingcommands"))
				{
					for (Iterator l = Client.instance().getGUIExtensionManager().getExtensionIterator(
									queryPane.getOnScreenUniqueId(), "surroundingcommands"); l.hasNext();)
					{
						ExtensionTile extensionTile = (ExtensionTile) l.next();
						JComponent component = extensionTile.getTile(queryPane, dataObject, null);
						Object constraints = extensionTile.getConstraints();

						if (controller.getCommandDescriptions().size() != 0)
						{
							if (constraints != null)
							{
								component.add(sep, constraints);
							}
							else
							{
								component.add(sep);
							}
						}

						for (Iterator i = controller.getCommandDescriptions().iterator(); i.hasNext();)
						{
							CommandDescription cd = (CommandDescription) i.next();

							DataObjectButton dataObjectButton = null;

							if (cd.getTextId().equals("new"))
							{
								dataObjectButton = new DataObjectButton(cd.getTextId(), newIcon);
							}
							else if (cd.getIconId().equals("edit"))
							{
								dataObjectButton = new DataObjectButton(cd.getTextId(), editIcon);
							}
							else if (cd.getIconId().equals("save"))
							{
								dataObjectButton = new DataObjectButton(cd.getTextId(), saveIcon);
							}
							else if (cd.getIconId().equals("delete"))
							{
								dataObjectButton = new DataObjectButton(cd.getTextId(), deleteIcon);
							}
							else if (cd.getIconId().equals("cancel"))
							{
								dataObjectButton = new DataObjectButton(cd.getTextId(), cancelIcon);
							}
							else
							{
								dataObjectButton = new DataObjectButton(cd.getTextId());
							}

							if (props.getProperty("surroundingcommands.filter", "").indexOf(cd.getTextId() + ";") >= 0)
							{
								continue;
							}

							dataObjectButton.setDataObject(dataObject);
							dataObjectButton.setSwingGUIPane(queryPane);
							dataObjectButton.setCommandDescription(cd);

							dataObjectButtons.add(dataObjectButton);

							if (constraints != null)
							{
								component.add(dataObjectButton, constraints);
							}
							else
							{
								component.add(dataObjectButton);
							}
						}

						if (Client.instance().getGUIExtensionManager().existsExtension(queryPane.getOnScreenUniqueId(),
										"toolbar"))
						{
							for (Iterator i = Client.instance().getGUIExtensionManager().getExtensionIterator(
											queryPane.getOnScreenUniqueId(), "toolbar"); i.hasNext();)
							{
								ExtensionTile extensionTileCommand = (ExtensionTile) i.next();

								if (constraints != null)
								{
									component.add(extensionTileCommand.getTile(queryPane, dataObject, null),
													constraints);
								}
								else
								{
									component.add(extensionTileCommand.getTile(queryPane, dataObject, null));
								}
							}
						}

						component.revalidate();
					}
				}
			}

			((JPanel) content).setLayout(new GridBagLayout());

			((JPanel) content).add(panel, createConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH,
							GridBagConstraints.NORTHWEST, new Insets(0, 0, 4, 0)));

			((JPanel) content).add(toolbar, createConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE,
							GridBagConstraints.NORTHWEST, new Insets(0, 0, 4, 0)));
		}
		catch (Exception x)
		{
			x.printStackTrace();
		}
	}

	/**
	 * @see de.iritgo.aktario.framework.dataobject.gui.Renderer#setError(java.lang.String)
	 */
	@Override
	public void setError(String widgetId)
	{
	}

	/**
	 * @see de.iritgo.aktario.framework.dataobject.gui.Renderer#setNoError(java.lang.String)
	 */
	@Override
	public void setNoError(String widgetId)
	{
	}

	/**
	 * Create and configure a table model.
	 *
	 * @param Controller
	 *            The controller for the data object and model
	 */
	@SuppressWarnings("serial")
	private void configureTableModel(Controller controller)
	{
		GUIExtensionManager guiExtManager = Client.instance().getGUIExtensionManager();

		List<ExtensionTile> tilesCopy = guiExtManager.getExtensionsCopy(queryPane.getOnScreenUniqueId(), "columns");

		WidgetDescription wdGroup = (WidgetDescription) controller.getWidgetDescriptions().get(0);

		for (Iterator i = wdGroup.getWidgetDescriptions().iterator(); i.hasNext();)
		{
			WidgetDescription wd = (WidgetDescription) i.next();

			if (wd.isVisible())
			{
				ExtensionTile tile = guiExtManager.getExtension(queryPane.getOnScreenUniqueId(), "columns", wd
								.getWidgetId());

				wdList.add(new ColumnHelper(wd, tile, DATAOBJECT_COLUMN));
				tilesCopy.remove(tile);
			}
		}

		for (ExtensionTile tile : tilesCopy)
		{
			wdList.add(new ColumnHelper(null, tile, TRANSIENT_COLUMN));
		}

		try
		{
			queryTableModel = new IObjectTableModelSorted()
			{
				public int getColumnCount()
				{
					return wdList.size();
				}

				public String getColumnName(int col)
				{
					ColumnHelper columnHelper = (ColumnHelper) wdList.get(col);
					String text = "";

					if (columnHelper.columnType == DATAOBJECT_COLUMN)
					{
						text = Engine.instance().getResourceService().getStringWithoutException(
										(String) ((WidgetDescription) columnHelper.wd).getLabelId());
					}
					else if (columnHelper.columnType == TRANSIENT_COLUMN)
					{
						text = Engine.instance().getResourceService().getStringWithoutException(
										columnHelper.extensionTile.getLabel());
					}

					return text;
				}

				public boolean isCellEditable(int row, int col)
				{
					return false;
				}

				public Object getValueAt(int row, int col)
				{
					ColumnHelper columnHelper = (ColumnHelper) wdList.get(col);

					Object object = new String("Unknown column");

					if (columnHelper.columnType == DATAOBJECT_COLUMN)
					{
						object = ((DataObject) getObjectByRow(row)).getAttribute(((WidgetDescription) columnHelper.wd)
										.getWidgetId());
					}
					else if (columnHelper.columnType == TRANSIENT_COLUMN)
					{
						object = (DataObject) getObjectByRow(row);
					}

					return object;
				}
			};
		}
		catch (Exception x)
		{
			Log.logFatal("system", "DefaultQueryRenderer.configureTable", x.toString());
			x.printStackTrace();
		}
	}

	/**
	 * Create and configure a JTable for this default query renderer
	 *
	 * @param IObjectTableModelSorted
	 *            The table model for this table.
	 */
	private void configureTable(IObjectTableModelSorted queryTableModel)
	{
		try
		{
			queryTable = new ITable();

			queryTable.setShowGrid(true);

			queryTable.setCellSelectionEnabled(false);

			queryTable.setRowSelectionAllowed(true);

			queryTable.setSelectionMode(0);

			queryTable.setRowHeight(Math.max(queryTable.getRowHeight() + 4, 24 + 4));

			tableSorter = queryTableModel.getTableSorter();
			queryTable.setModel(tableSorter);
			tableSorter.addMouseListenerToHeaderInTable(queryTable);

			queryTableModel.addTableModelListener(this);

			int column = 0;

			for (ColumnHelper columnHelper : wdList)
			{
				if (columnHelper.extensionTile != null)
				{
					queryTable.getColumnModel().getColumn(column).setCellRenderer(
									(DefaultTableCellRenderer) columnHelper.extensionTile.getTile(queryPane,
													dataObject, null));
				}

				++column;
			}

			queryTable.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					int col = queryTable.columnAtPoint(e.getPoint());
					int row = tableSorter.getRealRow(queryTable.getSelectedRow());

					if ((col < 0) || (row < 0))
					{
						return;
					}

					int realColumn = queryTable.getColumnModel().getColumn(col).getModelIndex();

					ColumnHelper columnHelper = (ColumnHelper) wdList.get(realColumn);

					String columnId = "";

					if (columnHelper.columnType == DATAOBJECT_COLUMN)
					{
						columnId = columnHelper.wd.getWidgetId();
					}
					else if (columnHelper.columnType == TRANSIENT_COLUMN)
					{
						columnId = columnHelper.extensionTile.getTileId();
					}

					DataObject dataObject = (DataObject) ((ITableSorter) queryTable.getModel()).getObjectByRow(row);

					Properties props = new Properties();

					props.put("table", queryTable);
					props.put("mousePosition", e.getPoint());

					for (Iterator i = Client.instance().getGUIExtensionManager().getExtensionIterator(
									queryPane.getOnScreenUniqueId(), "listcommands"); i.hasNext();)
					{
						ExtensionTile extensionTile = (ExtensionTile) i.next();

						if (extensionTile.getTileId().equals(columnId))
						{
							if (! extensionTile.isDoubleClickCommand())
							{
								extensionTile.command(queryPane, dataObject, props);
							}

							if (extensionTile.isDoubleClickCommand() && (e.getClickCount() == 2))
							{
								extensionTile.command(queryPane, dataObject, props);
							}
						}
					}
				}
			});
		}
		catch (Exception x)
		{
			Log.logFatal("system", "DefaultQueryRenderer.configureTable", x.toString());
			x.printStackTrace();
		}
	}

	/**
	 * @see de.iritgo.aktario.framework.dataobject.gui.Renderer#close()
	 */
	@Override
	public void close()
	{
		for (Iterator i = dataObjectButtons.iterator(); i.hasNext();)
		{
			DataObjectButton dob = (DataObjectButton) i.next();

			dob.release();
		}

		dataObjectButtons.clear();
	}

	/**
	 * If the tablemodel changed we must update the table sorter
	 *
	 * @param TableModelEvent
	 *            The event.
	 */
	public void tableChanged(TableModelEvent e)
	{
		queryTable.revalidate();
		queryTable.repaint();
	}

	/**
	 * Helper method for creating gridbag constraints.
	 *
	 * @param x
	 *            The grid column.
	 * @param y
	 *            The grid row.
	 * @param width
	 *            The number of occupied columns.
	 * @param height
	 *            The number of occupied rows.
	 * @param fill
	 *            The fill method.
	 * @param anchor
	 *            The anchor.
	 * @param wx
	 *            The horizontal stretch factor.
	 * @param wy
	 *            The vertical stretch factor.
	 * @param insets
	 *            The cell insets.
	 * @return The gridbag constraints.
	 */
	protected GridBagConstraints createConstraints(int x, int y, int width, int height, double wx, double wy, int fill,
					int anchor, Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = anchor;

		if (insets == null)
		{
			gbc.insets = new Insets(0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}

	/**
	 * Describe method setSearchIcon() here.
	 *
	 */
	public void setSearchIcon()
	{
		if (searchLabel != null)
		{
			searchLabel.setIcon(searchIcon);
		}
	}

	/**
	 * Describe method setSearchWaitIcon() here.
	 *
	 */
	public void setSearchWaitIcon()
	{
		if (searchLabel != null)
		{
			searchLabel.setIcon(searchWait);
		}
	}

	/**
	 * Get the Table component.
	 *
	 * @return JTable The table of this default query renderer
	 */
	public JTable getTable()
	{
		return queryTable;
	}

	@Override
	public void refresh()
	{
		AbstractQuery query = (AbstractQuery) dataObject;

		IObjectList results = (IObjectList) query.getIObjectListResults();

		results.clearIObjectList();

		query.setSearchCondition(searchConditionField.getText());
		query.update();
	}
}

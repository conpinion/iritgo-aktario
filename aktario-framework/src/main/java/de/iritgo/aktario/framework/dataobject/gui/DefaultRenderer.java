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
import de.iritgo.aktario.core.gui.ILabel;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.client.Client;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;


/**
 * DefaultRenderer
 */
public class DefaultRenderer extends Renderer
{
	public static class ComboboxItem
	{
		protected String id;

		protected String label;

		public ComboboxItem(String id, String label)
		{
			this.id = id;

			if (label.startsWith("$"))
			{
				this.label = Engine.instance().getResourceService().getStringWithoutException(label.substring(1));
			}
			else
			{
				this.label = label;
			}
		}

		public String toString()
		{
			return label;
		}

		public String getId()
		{
			return id;
		}
	}

	protected ImageIcon newIcon;

	protected ImageIcon editIcon;

	protected ImageIcon saveIcon;

	protected ImageIcon deleteIcon;

	protected ImageIcon cancelIcon;

	protected ImageIcon groupIcon;

	protected ExtendedPanel panel;

	public LinkedList dataObjectButtons;

	public DefaultRenderer()
	{
		super("DefaultRenderer");

		newIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/edit.png"));
		editIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/edit.png"));
		saveIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/save.png"));
		deleteIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/delete.png"));
		cancelIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/cancel.png"));
		groupIcon = new ImageIcon(DefaultQueryRenderer.class.getResource("/resources/group.png"));

		dataObjectButtons = new LinkedList();
	}

	/**
	 * Will called on a query pane to generate all needed swing components
	 *
	 * @param Controller The controller for the dataobject.
	 * @param DataObject The data object for the display.
	 * @param Object The content container. In swing it is a JPanel object.
	 * @param QueryPane The pane to render on.
	 */
	public void workOn(Controller controller, DataObject dataObject, Object content, QueryPane queryPane)
	{
	}

	/**
	 * Will called on a query pane to generate all needed swing components
	 *
	 * @param Controller The controller for the dataobject.
	 * @param DataObject The data object for the display.
	 * @param Object The content container. In swing it is a JPanel object.
	 * @param DataObjectGUIPane The pane to render on.
	 */
	public void workOn(final Controller controller, final DataObject dataObject, final Object content,
					final DataObjectGUIPane dataObjectGUIPane)
	{
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				public void run()
				{
					panel = new ExtendedPanel();
					panel.setBackground(Color.WHITE);
					panel.setBorder(new EmptyBorder(4, 4, 12, 4));

					JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
									JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

					scrollPane.getVerticalScrollBar().setBlockIncrement(16);

					JPanel toolbar = (JPanel) new JPanel();

					panel.setLayout(new GridBagLayout());
					toolbar.setLayout(new GridBagLayout());

					Properties props = dataObjectGUIPane.getProperties();

					int y = 0;

					boolean enabled = true;

					if (props.getProperty("readonly", "no").equals("yes"))
					{
						enabled = false;
					}

					for (Iterator i = controller.getWidgetDescriptions().iterator(); i.hasNext();)
					{
						WidgetDescription wd = (WidgetDescription) i.next();

						if (wd.getRendererId().equals("group"))
						{
							JPanel groupPanel = new JPanel();

							groupPanel.setLayout(new GridBagLayout());

							ILabel label = new ILabel(wd.getLabelId() /*, groupIcon*/);

							label.setFont(label.getFont().deriveFont(Font.BOLD));
							groupPanel.add(label, createConstraints(0, 0, 1, 1, 1.0, 0.0,
											GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, new Insets(0,
															8, 0, 8)));
							groupPanel.setBackground(new Color(234, 250, 189));
							groupPanel.setBorder(new CompoundBorder(new LineBorder(new Color(222, 234, 188)),
											new EmptyBorder(2, 2, 2, 2)));

							panel.add(groupPanel, createConstraints(0, y, 3, 1, 1.0, 0.0,
											GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, new Insets(12,
															8, 12, 8)));
							++y;

							for (Iterator j = wd.getWidgetDescriptions().iterator(); j.hasNext();)
							{
								WidgetDescription wdGroupItem = (WidgetDescription) j.next();

								if (wdGroupItem.getRendererId().equals("textarea"))
								{
									JTextArea textArea = new JTextArea(8, 1);

									textArea.setEnabled(enabled);

									JScrollPane textAreaScrollPane = new JScrollPane(textArea);

									wdGroupItem.addControl(wdGroupItem.getWidgetId(), textArea);
									panel.addComp(wdGroupItem.getWidgetId(), new ILabel(wdGroupItem.getLabelId()),
													textAreaScrollPane, wdGroupItem);
								}
								else if (wdGroupItem.getRendererId().equals("salutation"))
								{
									JComboBox combobox = new JComboBox();

									combobox.setEnabled(enabled);
									insertSalutationItems(combobox);
									wdGroupItem.addControl(wdGroupItem.getWidgetId(), combobox);
									panel.addComp(wdGroupItem.getWidgetId(), new ILabel(wdGroupItem.getLabelId()),
													combobox, wdGroupItem, GridBagConstraints.VERTICAL);
								}
								else if (wdGroupItem.getRendererId().equals("country"))
								{
									JComboBox combobox = new JComboBox();

									combobox.setEnabled(enabled);
									insertCountryItems(combobox);
									wdGroupItem.addControl(wdGroupItem.getWidgetId(), combobox);
									panel.addComp(wdGroupItem.getWidgetId(), new ILabel(wdGroupItem.getLabelId()),
													combobox, wdGroupItem, GridBagConstraints.VERTICAL);
								}
								else if (wdGroupItem.getRendererId().equals("combo"))
								{
									JComboBox combobox = new JComboBox();

									combobox.setEnabled(enabled);
									wdGroupItem.addControl(wdGroupItem.getWidgetId(), combobox);
									panel.addComp(wdGroupItem.getWidgetId(), new ILabel(wdGroupItem.getLabelId()),
													combobox, wdGroupItem, GridBagConstraints.VERTICAL);
								}
								else
								{
									JTextField textField = new JTextField();

									textField.setEnabled(enabled);
									wdGroupItem.addControl(wdGroupItem.getWidgetId(), textField);
									panel.addComp(wdGroupItem.getWidgetId(), new ILabel(wdGroupItem.getLabelId()),
													textField, wdGroupItem);
								}

								++y;
								++y;
							}

							continue;
						}

						++y;
					}

					panel.add(new JLabel(""), createConstraints(0, y, 3, 1, 1.0, 1.0, GridBagConstraints.BOTH,
									GridBagConstraints.NORTHWEST, null));

					// Surrounding Commands
					JSeparator sep = new JSeparator();

					if (props.getProperty("surroundingcommands", "no").equals("yes"))
					{
						if (Client.instance().getGUIExtensionManager().existsExtension(
										dataObjectGUIPane.getOnScreenUniqueId(), "surroundingcommands"))
						{
							for (Iterator l = Client.instance().getGUIExtensionManager().getExtensionIterator(
											dataObjectGUIPane.getOnScreenUniqueId(), "surroundingcommands"); l
											.hasNext();)
							{
								ExtensionTile extensionTile = (ExtensionTile) l.next();
								JComponent component = extensionTile.getTile(dataObjectGUIPane, dataObject, null);
								Object constraints = extensionTile.getConstraints();

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

									if (props.getProperty("surroundingcommands.filter", "").indexOf(
													cd.getTextId() + ";") >= 0)
									{
										continue;
									}

									dataObjectButton.setDataObject(dataObject);
									dataObjectButton.setSwingGUIPane(dataObjectGUIPane);
									dataObjectButton.setCommandDescription(cd);

									dataObjectButtons.add(dataObjectButton);

									boolean allreadySet = false;

									for (int j = 0; j < component.getComponentCount(); ++j)
									{
										try
										{
											DataObjectButton button = (DataObjectButton) component.getComponent(j);

											if (button.getCommandDescription().getCommandId().equals(
															dataObjectButton.getCommandDescription().getCommandId()))
											{
												component.remove(button);

												if (constraints != null)
												{
													component.add(dataObjectButton, constraints);
												}
												else
												{
													component.add(dataObjectButton);
												}

												allreadySet = true;

												break;
											}
										}
										catch (ClassCastException x)
										{
										}
									}

									if (! allreadySet)
									{
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

										if (constraints != null)
										{
											component.add(dataObjectButton, constraints);
										}
										else
										{
											component.add(dataObjectButton);
										}
									}
								}

								if (Client.instance().getGUIExtensionManager().existsExtension(
												dataObjectGUIPane.getOnScreenUniqueId(), "toolbar"))
								{
									for (Iterator i = Client.instance().getGUIExtensionManager().getExtensionIterator(
													dataObjectGUIPane.getOnScreenUniqueId(), "toolbar"); i.hasNext();)
									{
										ExtensionTile extensionTileCommand = (ExtensionTile) i.next();

										if (constraints != null)
										{
											component.add(extensionTileCommand.getTile(dataObjectGUIPane, dataObject,
															null), constraints);
										}
										else
										{
											component.add(extensionTileCommand.getTile(dataObjectGUIPane, dataObject,
															null));
										}
									}
								}

								component.revalidate();
							}
						}
					}

					y = 0;

					if (props.getProperty("toolbar", "no").equals("yes"))
					{
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
							dataObjectButton.setSwingGUIPane(dataObjectGUIPane);
							dataObjectButton.setCommandDescription(cd);

							dataObjectButtons.add(dataObjectButton);

							toolbar.add(dataObjectButton, createConstraints(0, y, 1, 1, 1.0, 0.0,
											GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, new Insets(4,
															8, 0, 8)));
							++y;
						}
					}

					((JPanel) content).setLayout(new GridBagLayout());

					((JPanel) content).add(scrollPane, createConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH,
									GridBagConstraints.NORTHWEST, null));

					((JPanel) content).add(toolbar, createConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE,
									GridBagConstraints.NORTHWEST, null));
				}
			});
		}
		catch (Exception x)
		{
		}
	}

	public void setError(String widgetId)
	{
		panel.setError(widgetId);
	}

	public void setNoError(String widgetId)
	{
		panel.setNoError(widgetId);
	}

	public void close()
	{
		for (Iterator i = dataObjectButtons.iterator(); i.hasNext();)
		{
			DataObjectButton dob = (DataObjectButton) i.next();

			dob.release();
		}

		dataObjectButtons.clear();
		panel.close();
		panel = null;
	}

	/**
	 * Helper method for creating gridbag constraints.
	 *
	 * @param x The grid column.
	 * @param y The grid row.
	 * @param width The number of occupied columns.
	 * @param height The number of occupied rows.
	 * @param fill The fill method.
	 * @param anchor The anchor.
	 * @param wx The horizontal stretch factor.
	 * @param wy The vertical stretch factor.
	 * @param insets The cell insets.
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
	 * Insert salutation combobox items.
	 *
	 * @param combobox The combobox.
	 */
	protected void insertSalutationItems(JComboBox combobox)
	{
		combobox.addItem(new ComboboxItem("", "$optNone"));
		combobox.addItem(new ComboboxItem("mr", "$mr"));
		combobox.addItem(new ComboboxItem("mrs", "$mrs"));
		combobox.addItem(new ComboboxItem("firm", "$firm"));

		combobox.setSelectedIndex(0);
	}

	/**
	 * Insert country combobox items.
	 *
	 * @param combobox The combobox.
	 */
	protected void insertCountryItems(JComboBox combobox)
	{
		combobox.addItem(new ComboboxItem("", "$optNone"));
		combobox.addItem(new ComboboxItem("AF", "$1.country.AF"));
		combobox.addItem(new ComboboxItem("EG", "$1.country.EG"));
		combobox.addItem(new ComboboxItem("AL", "$1.country.AL"));
		combobox.addItem(new ComboboxItem("DZ", "$1.country.DZ"));
		combobox.addItem(new ComboboxItem("AD", "$1.country.AD"));
		combobox.addItem(new ComboboxItem("AO", "$1.country.AO"));
		combobox.addItem(new ComboboxItem("AI", "$1.country.AI"));
		combobox.addItem(new ComboboxItem("AQ", "$1.country.AQ"));
		combobox.addItem(new ComboboxItem("AG", "$1.country.AG"));
		combobox.addItem(new ComboboxItem("GQ", "$1.country.GQ"));
		combobox.addItem(new ComboboxItem("AR", "$1.country.AR"));
		combobox.addItem(new ComboboxItem("AM", "$1.country.AM"));
		combobox.addItem(new ComboboxItem("AW", "$1.country.AW"));
		combobox.addItem(new ComboboxItem("AZ", "$1.country.AZ"));
		combobox.addItem(new ComboboxItem("ET", "$1.country.ET"));
		combobox.addItem(new ComboboxItem("AU", "$1.country.AU"));
		combobox.addItem(new ComboboxItem("BS", "$1.country.BS"));
		combobox.addItem(new ComboboxItem("BH", "$1.country.BH"));
		combobox.addItem(new ComboboxItem("BD", "$1.country.BD"));
		combobox.addItem(new ComboboxItem("BB", "$1.country.BB"));
		combobox.addItem(new ComboboxItem("BE", "$1.country.BE"));
		combobox.addItem(new ComboboxItem("BZ", "$1.country.BZ"));
		combobox.addItem(new ComboboxItem("BJ", "$1.country.BJ"));
		combobox.addItem(new ComboboxItem("BM", "$1.country.BM"));
		combobox.addItem(new ComboboxItem("BT", "$1.country.BT"));
		combobox.addItem(new ComboboxItem("MM", "$1.country.MM"));
		combobox.addItem(new ComboboxItem("BO", "$1.country.BO"));
		combobox.addItem(new ComboboxItem("BA", "$1.country.BA"));
		combobox.addItem(new ComboboxItem("BW", "$1.country.BW"));
		combobox.addItem(new ComboboxItem("BV", "$1.country.BV"));
		combobox.addItem(new ComboboxItem("BR", "$1.country.BR"));
		combobox.addItem(new ComboboxItem("IO", "$1.country.IO"));
		combobox.addItem(new ComboboxItem("BN", "$1.country.BN"));
		combobox.addItem(new ComboboxItem("BG", "$1.country.BG"));
		combobox.addItem(new ComboboxItem("BF", "$1.country.BF"));
		combobox.addItem(new ComboboxItem("BI", "$1.country.BI"));
		combobox.addItem(new ComboboxItem("CL", "$1.country.CL"));
		combobox.addItem(new ComboboxItem("CN", "$1.country.CN"));
		combobox.addItem(new ComboboxItem("CX", "$1.country.CX"));
		combobox.addItem(new ComboboxItem("CK", "$1.country.CK"));
		combobox.addItem(new ComboboxItem("CR", "$1.country.CR"));
		combobox.addItem(new ComboboxItem("DK", "$1.country.DK"));
		combobox.addItem(new ComboboxItem("DE", "$1.country.DE"));
		combobox.addItem(new ComboboxItem("DJ", "$1.country.DJ"));
		combobox.addItem(new ComboboxItem("DM", "$1.country.DM"));
		combobox.addItem(new ComboboxItem("DO", "$1.country.DO"));
		combobox.addItem(new ComboboxItem("EC", "$1.country.EC"));
		combobox.addItem(new ComboboxItem("SV", "$1.country.SV"));
		combobox.addItem(new ComboboxItem("CI", "$1.country.CI"));
		combobox.addItem(new ComboboxItem("ER", "$1.country.ER"));
		combobox.addItem(new ComboboxItem("EE", "$1.country.EE"));
		combobox.addItem(new ComboboxItem("FK", "$1.country.FK"));
		combobox.addItem(new ComboboxItem("FO", "$1.country.FO"));
		combobox.addItem(new ComboboxItem("FJ", "$1.country.FJ"));
		combobox.addItem(new ComboboxItem("FI", "$1.country.FI"));
		combobox.addItem(new ComboboxItem("FR", "$1.country.FR"));
		combobox.addItem(new ComboboxItem("GF", "$1.country.GF"));
		combobox.addItem(new ComboboxItem("PF", "$1.country.PF"));
		combobox.addItem(new ComboboxItem("TF", "$1.country.TF"));
		combobox.addItem(new ComboboxItem("GA", "$1.country.GA"));
		combobox.addItem(new ComboboxItem("GM", "$1.country.GM"));
		combobox.addItem(new ComboboxItem("GE", "$1.country.GE"));
		combobox.addItem(new ComboboxItem("GH", "$1.country.GH"));
		combobox.addItem(new ComboboxItem("GI", "$1.country.GI"));
		combobox.addItem(new ComboboxItem("GD", "$1.country.GD"));
		combobox.addItem(new ComboboxItem("GR", "$1.country.GR"));
		combobox.addItem(new ComboboxItem("GL", "$1.country.GL"));
		combobox.addItem(new ComboboxItem("GB", "$1.country.GB"));
		combobox.addItem(new ComboboxItem("GP", "$1.country.GP"));
		combobox.addItem(new ComboboxItem("GU", "$1.country.GU"));
		combobox.addItem(new ComboboxItem("GT", "$1.country.GT"));
		combobox.addItem(new ComboboxItem("GN", "$1.country.GN"));
		combobox.addItem(new ComboboxItem("GW", "$1.country.GW"));
		combobox.addItem(new ComboboxItem("GY", "$1.country.GY"));
		combobox.addItem(new ComboboxItem("HT", "$1.country.HT"));
		combobox.addItem(new ComboboxItem("HM", "$1.country.HM"));
		combobox.addItem(new ComboboxItem("HN", "$1.country.HN"));
		combobox.addItem(new ComboboxItem("HK", "$1.country.HK"));
		combobox.addItem(new ComboboxItem("IN", "$1.country.IN"));
		combobox.addItem(new ComboboxItem("ID", "$1.country.ID"));
		combobox.addItem(new ComboboxItem("IQ", "$1.country.IQ"));
		combobox.addItem(new ComboboxItem("IR", "$1.country.IR"));
		combobox.addItem(new ComboboxItem("IE", "$1.country.IE"));
		combobox.addItem(new ComboboxItem("IS", "$1.country.IS"));
		combobox.addItem(new ComboboxItem("IL", "$1.country.IL"));
		combobox.addItem(new ComboboxItem("IT", "$1.country.IT"));
		combobox.addItem(new ComboboxItem("JM", "$1.country.JM"));
		combobox.addItem(new ComboboxItem("JP", "$1.country.JP"));
		combobox.addItem(new ComboboxItem("YE", "$1.country.YE"));
		combobox.addItem(new ComboboxItem("JO", "$1.country.JO"));
		combobox.addItem(new ComboboxItem("YU", "$1.country.YU"));
		combobox.addItem(new ComboboxItem("KY", "$1.country.KY"));
		combobox.addItem(new ComboboxItem("KH", "$1.country.KH"));
		combobox.addItem(new ComboboxItem("CM", "$1.country.CM"));
		combobox.addItem(new ComboboxItem("CA", "$1.country.CA"));
		combobox.addItem(new ComboboxItem("CV", "$1.country.CV"));
		combobox.addItem(new ComboboxItem("KZ", "$1.country.KZ"));
		combobox.addItem(new ComboboxItem("KE", "$1.country.KE"));
		combobox.addItem(new ComboboxItem("KG", "$1.country.KG"));
		combobox.addItem(new ComboboxItem("KI", "$1.country.KI"));
		combobox.addItem(new ComboboxItem("CC", "$1.country.CC"));
		combobox.addItem(new ComboboxItem("CO", "$1.country.CO"));
		combobox.addItem(new ComboboxItem("KM", "$1.country.KM"));
		combobox.addItem(new ComboboxItem("CG", "$1.country.CG"));
		combobox.addItem(new ComboboxItem("CD", "$1.country.CD"));
		combobox.addItem(new ComboboxItem("HR", "$1.country.HR"));
		combobox.addItem(new ComboboxItem("CU", "$1.country.CU"));
		combobox.addItem(new ComboboxItem("KW", "$1.country.KW"));
		combobox.addItem(new ComboboxItem("LA", "$1.country.LA"));
		combobox.addItem(new ComboboxItem("LS", "$1.country.LS"));
		combobox.addItem(new ComboboxItem("LV", "$1.country.LV"));
		combobox.addItem(new ComboboxItem("LB", "$1.country.LB"));
		combobox.addItem(new ComboboxItem("LR", "$1.country.LR"));
		combobox.addItem(new ComboboxItem("LY", "$1.country.LY"));
		combobox.addItem(new ComboboxItem("LI", "$1.country.LI"));
		combobox.addItem(new ComboboxItem("LT", "$1.country.LT"));
		combobox.addItem(new ComboboxItem("LU", "$1.country.LU"));
		combobox.addItem(new ComboboxItem("MO", "$1.country.MO"));
		combobox.addItem(new ComboboxItem("MG", "$1.country.MG"));
		combobox.addItem(new ComboboxItem("MW", "$1.country.MW"));
		combobox.addItem(new ComboboxItem("MY", "$1.country.MY"));
		combobox.addItem(new ComboboxItem("MV", "$1.country.MV"));
		combobox.addItem(new ComboboxItem("ML", "$1.country.ML"));
		combobox.addItem(new ComboboxItem("MT", "$1.country.MT"));
		combobox.addItem(new ComboboxItem("MP", "$1.country.MP"));
		combobox.addItem(new ComboboxItem("MA", "$1.country.MA"));
		combobox.addItem(new ComboboxItem("MH", "$1.country.MH"));
		combobox.addItem(new ComboboxItem("MQ", "$1.country.MQ"));
		combobox.addItem(new ComboboxItem("MR", "$1.country.MR"));
		combobox.addItem(new ComboboxItem("MU", "$1.country.MU"));
		combobox.addItem(new ComboboxItem("YT", "$1.country.YT"));
		combobox.addItem(new ComboboxItem("MK", "$1.country.MK"));
		combobox.addItem(new ComboboxItem("MX", "$1.country.MX"));
		combobox.addItem(new ComboboxItem("FM", "$1.country.FM"));
		combobox.addItem(new ComboboxItem("MZ", "$1.country.MZ"));
		combobox.addItem(new ComboboxItem("MD", "$1.country.MD"));
		combobox.addItem(new ComboboxItem("MC", "$1.country.MC"));
		combobox.addItem(new ComboboxItem("MN", "$1.country.MN"));
		combobox.addItem(new ComboboxItem("MS", "$1.country.MS"));
		combobox.addItem(new ComboboxItem("NA", "$1.country.NA"));
		combobox.addItem(new ComboboxItem("NR", "$1.country.NR"));
		combobox.addItem(new ComboboxItem("NP", "$1.country.NP"));
		combobox.addItem(new ComboboxItem("NC", "$1.country.NC"));
		combobox.addItem(new ComboboxItem("NZ", "$1.country.NZ"));
		combobox.addItem(new ComboboxItem("NI", "$1.country.NI"));
		combobox.addItem(new ComboboxItem("NL", "$1.country.NL"));
		combobox.addItem(new ComboboxItem("AN", "$1.country.AN"));
		combobox.addItem(new ComboboxItem("NE", "$1.country.NE"));
		combobox.addItem(new ComboboxItem("NG", "$1.country.NG"));
		combobox.addItem(new ComboboxItem("NU", "$1.country.NU"));
		combobox.addItem(new ComboboxItem("KP", "$1.country.KP"));
		combobox.addItem(new ComboboxItem("NF", "$1.country.NF"));
		combobox.addItem(new ComboboxItem("NO", "$1.country.NO"));
		combobox.addItem(new ComboboxItem("OM", "$1.country.OM"));
		combobox.addItem(new ComboboxItem("AT", "$1.country.AT"));
		combobox.addItem(new ComboboxItem("PK", "$1.country.PK"));
		combobox.addItem(new ComboboxItem("PS", "$1.country.PS"));
		combobox.addItem(new ComboboxItem("PW", "$1.country.PW"));
		combobox.addItem(new ComboboxItem("PA", "$1.country.PA"));
		combobox.addItem(new ComboboxItem("PG", "$1.country.PG"));
		combobox.addItem(new ComboboxItem("PY", "$1.country.PY"));
		combobox.addItem(new ComboboxItem("PE", "$1.country.PE"));
		combobox.addItem(new ComboboxItem("PH", "$1.country.PH"));
		combobox.addItem(new ComboboxItem("PN", "$1.country.PN"));
		combobox.addItem(new ComboboxItem("PL", "$1.country.PL"));
		combobox.addItem(new ComboboxItem("PT", "$1.country.PT"));
		combobox.addItem(new ComboboxItem("PR", "$1.country.PR"));
		combobox.addItem(new ComboboxItem("QA", "$1.country.QA"));
		combobox.addItem(new ComboboxItem("RE", "$1.country.RE"));
		combobox.addItem(new ComboboxItem("RW", "$1.country.RW"));
		combobox.addItem(new ComboboxItem("RO", "$1.country.RO"));
		combobox.addItem(new ComboboxItem("RU", "$1.country.RU"));
		combobox.addItem(new ComboboxItem("LC", "$1.country.LC"));
		combobox.addItem(new ComboboxItem("ZM", "$1.country.ZM"));
		combobox.addItem(new ComboboxItem("AS", "$1.country.AS"));
		combobox.addItem(new ComboboxItem("WS", "$1.country.WS"));
		combobox.addItem(new ComboboxItem("SM", "$1.country.SM"));
		combobox.addItem(new ComboboxItem("ST", "$1.country.ST"));
		combobox.addItem(new ComboboxItem("SA", "$1.country.SA"));
		combobox.addItem(new ComboboxItem("SE", "$1.country.SE"));
		combobox.addItem(new ComboboxItem("CH", "$1.country.CH"));
		combobox.addItem(new ComboboxItem("SN", "$1.country.SN"));
		combobox.addItem(new ComboboxItem("SC", "$1.country.SC"));
		combobox.addItem(new ComboboxItem("SL", "$1.country.SL"));
		combobox.addItem(new ComboboxItem("SG", "$1.country.SG"));
		combobox.addItem(new ComboboxItem("SK", "$1.country.SK"));
		combobox.addItem(new ComboboxItem("SI", "$1.country.SI"));
		combobox.addItem(new ComboboxItem("SB", "$1.country.SB"));
		combobox.addItem(new ComboboxItem("SO", "$1.country.SO"));
		combobox.addItem(new ComboboxItem("GS", "$1.country.GS"));
		combobox.addItem(new ComboboxItem("ES", "$1.country.ES"));
		combobox.addItem(new ComboboxItem("LK", "$1.country.LK"));
		combobox.addItem(new ComboboxItem("SH", "$1.country.SH"));
		combobox.addItem(new ComboboxItem("KN", "$1.country.KN"));
		combobox.addItem(new ComboboxItem("PM", "$1.country.PM"));
		combobox.addItem(new ComboboxItem("VC", "$1.country.VC"));
		combobox.addItem(new ComboboxItem("KR", "$1.country.KR"));
		combobox.addItem(new ComboboxItem("ZA", "$1.country.ZA"));
		combobox.addItem(new ComboboxItem("SD", "$1.country.SD"));
		combobox.addItem(new ComboboxItem("SR", "$1.country.SR"));
		combobox.addItem(new ComboboxItem("SJ", "$1.country.SJ"));
		combobox.addItem(new ComboboxItem("SZ", "$1.country.SZ"));
		combobox.addItem(new ComboboxItem("SY", "$1.country.SY"));
		combobox.addItem(new ComboboxItem("TJ", "$1.country.TJ"));
		combobox.addItem(new ComboboxItem("TW", "$1.country.TW"));
		combobox.addItem(new ComboboxItem("TZ", "$1.country.TZ"));
		combobox.addItem(new ComboboxItem("TH", "$1.country.TH"));
		combobox.addItem(new ComboboxItem("TP", "$1.country.TP"));
		combobox.addItem(new ComboboxItem("TG", "$1.country.TG"));
		combobox.addItem(new ComboboxItem("TK", "$1.country.TK"));
		combobox.addItem(new ComboboxItem("TO", "$1.country.TO"));
		combobox.addItem(new ComboboxItem("TT", "$1.country.TT"));
		combobox.addItem(new ComboboxItem("TD", "$1.country.TD"));
		combobox.addItem(new ComboboxItem("CZ", "$1.country.CZ"));
		combobox.addItem(new ComboboxItem("TN", "$1.country.TN"));
		combobox.addItem(new ComboboxItem("TR", "$1.country.TR"));
		combobox.addItem(new ComboboxItem("TM", "$1.country.TM"));
		combobox.addItem(new ComboboxItem("TC", "$1.country.TC"));
		combobox.addItem(new ComboboxItem("TV", "$1.country.TV"));
		combobox.addItem(new ComboboxItem("UG", "$1.country.UG"));
		combobox.addItem(new ComboboxItem("UA", "$1.country.UA"));
		combobox.addItem(new ComboboxItem("HU", "$1.country.HU"));
		combobox.addItem(new ComboboxItem("UY", "$1.country.UY"));
		combobox.addItem(new ComboboxItem("UZ", "$1.country.UZ"));
		combobox.addItem(new ComboboxItem("VU", "$1.country.VU"));
		combobox.addItem(new ComboboxItem("VA", "$1.country.VA"));
		combobox.addItem(new ComboboxItem("VE", "$1.country.VE"));
		combobox.addItem(new ComboboxItem("AE", "$1.country.AE"));
		combobox.addItem(new ComboboxItem("US", "$1.country.US"));
		combobox.addItem(new ComboboxItem("VN", "$1.country.VN"));
		combobox.addItem(new ComboboxItem("VG", "$1.country.VG"));
		combobox.addItem(new ComboboxItem("VI", "$1.country.VI"));
		combobox.addItem(new ComboboxItem("WF", "$1.country.WF"));
		combobox.addItem(new ComboboxItem("BY", "$1.country.BY"));
		combobox.addItem(new ComboboxItem("EH", "$1.country.EH"));
		combobox.addItem(new ComboboxItem("CF", "$1.country.CF"));
		combobox.addItem(new ComboboxItem("ZW", "$1.country.ZW"));
		combobox.addItem(new ComboboxItem("CY", "$1.country.CY"));

		combobox.setSelectedIndex(0);
	}

	@Override
	public void refresh()
	{
	}
}

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


import de.iritgo.aktario.core.AktarioPlugin;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.application.Application;
import de.iritgo.aktario.core.application.ApplicationInstance;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.SwingDesktopManager;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectComboBoxModel;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.room.Room;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.client.command.ShowWindow;
import de.iritgo.aktario.framework.command.CommandTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.Properties;


/**
 * RoomPane.
 *
 * @version $Id: RoomPane.java,v 1.11 2006/09/25 10:34:31 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class RoomPane extends SwingGUIPane
{
	/** The desktop pane. */
	public JDesktopPane roomDesktop;

	/** Application combobox. */
	public JComboBox applications;

	/** Application combobox model. */
	private IObjectComboBoxModel applicationsModel;

	/** Application that can be added combobox. */
	public JComboBox applicationMenu;

	/** Dummy application for the first combo box entry. */
	Application dummyApplication;

	/** Additional space around the application items. */
	EmptyBorder spacingBorder;

	/**
	 * Close the current application.
	 */
	public Action closeApplicationAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
		}
	};

	/**
	 * Delete the current application.
	 */
	public Action deleteApplicationAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
		}
	};

	/**
	 * Create a new RoomPane.
	 */
	public RoomPane()
	{
		super("RoomPane");

		dummyApplication = new Application("dummy", (AktarioPlugin) Engine.instance().getPluginManager().getPlugin(
						"aktario-client"), "aktario.applications", "/resources/application.png", null);

		spacingBorder = new EmptyBorder(2, 2, 2, 2);
	}

	/**
	 * Initialize the gui.
	 */
	@Override
	public void initGUI()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader(RoomControlPane.class.getClassLoader());

			JPanel panel = (JPanel) swingEngine.render(getClass().getResource("/swixml/RoomPane.xml"));

			content.add(panel, createConstraints(0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			applicationsModel = new IObjectComboBoxModel();
			applications.setModel(applicationsModel);

			applications.setRenderer(new BasicComboBoxRenderer()
			{
				@Override
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
								boolean hasFocus)
				{
					super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);

					ApplicationInstance instance = (ApplicationInstance) value;

					setIcon(null);

					if (instance != null)
					{
						Application application = Application.get(instance.getApplicationId());

						if (application != null)
						{
							setIcon(application.getIcon());
						}
					}

					setBorder(spacingBorder);

					return this;
				}
			});

			applicationMenu.setRenderer(new BasicComboBoxRenderer()
			{
				@Override
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
								boolean hasFocus)
				{
					super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);

					Application application = (Application) value;

					setIcon(application.getIcon());
					setBorder(spacingBorder);

					return this;
				}
			});

			applicationMenu.addItem(dummyApplication);

			for (Iterator i = Application.iterator(); i.hasNext();)
			{
				applicationMenu.addItem(i.next());
			}

			applicationMenu.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange() == ItemEvent.SELECTED)
					{
						if (applicationMenu.getSelectedIndex() != 0)
						{
							addApplication((Application) applicationMenu.getSelectedItem());
							applicationMenu.setSelectedIndex(0);
						}
					}
				}
			});

			roomDesktop.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

			AktarioGUI aktarioGUI = ((AktarioGUI) AppContext.instance().getObject("aktarioGui"));

			((SwingDesktopManager) aktarioGUI.getDesktopManager()).addDesktopPaneNoActivation("room", roomDesktop);

			getDisplay().setIcon(new ImageIcon(RoomControlPane.class.getResource("/resources/rooms.png")));

			AppContext.instance().put("roomPane", this);
		}
		catch (Exception x)
		{
			Log.logError("client", "RoomPane.initGUI", x.toString());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	@Override
	public void loadFromObject(IObject iobject)
	{
		ResourceService resources = Engine.instance().getResourceService();
		Room room = (Room) iobject;

		applicationsModel.update(room.getApplications());

		applications.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					activateApplication((ApplicationInstance) applications.getSelectedItem());
				}
			}
		});

		setTitle(resources.getString("RoomPane") + " - " + room.getName());

		((AktarioGUI) AppContext.instance().getObject("aktarioGui")).setApplicationMenuEnabled(true);
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	@Override
	public void storeToObject(IObject iobject)
	{
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane()
	{
		return new RoomPane();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject()
	{
		return new Room();
	}

	/**
	 * Close the display.
	 */
	@Override
	public void systemClose()
	{
		super.systemClose();

		AktarioGUI aktarioGUI = ((AktarioGUI) AppContext.instance().getObject("aktarioGui"));

		aktarioGUI.getDesktopManager().closeAllDisplays("room");

		((SwingDesktopManager) aktarioGUI.getDesktopManager()).removeDesktopPane("room");

		AppContext.instance().remove("roomPane");

		((RoomControlPane) AppContext.instance().getObject("roomControlPane")).roomClosed();

		aktarioGUI.setApplicationMenuEnabled(false);
	}

	/**
	 * Add an application.
	 *
	 * @parm application The application to add.
	 */
	public void addApplication(Application application)
	{
		Room room = (Room) getIObject();

		if (application.getId().equals("aktario.Chat"))
		{
			for (Iterator i = room.applicationIterator(); i.hasNext();)
			{
				ApplicationInstance ai = (ApplicationInstance) i.next();

				if (ai.getApplicationId().equals("aktario.Chat"))
				{
					new Thread(new Runnable()
					{
						public void run()
						{
							JOptionPane.showMessageDialog(content.getTopLevelAncestor(),
											Engine.instance().getResourceService().getString(
															"aktario.onlyOneChatApplicationCanBeAdded"), "Aktario",
											JOptionPane.OK_OPTION);
						}
					}).start();

					return;
				}
			}
		}

		ApplicationInstance instance = new ApplicationInstance();

		instance.setApplicationId(application.getId());
		room.addApplication(instance);

		Properties props = new Properties();

		props.put("maximizable", new Boolean(false));
		props.put("iconifiable", new Boolean(false));
		props.put("maximized", new Boolean(true));
		props.put("titlebar", new Boolean(false));
		CommandTools.performAsync(new ShowWindow(application.getGuiPaneId(), "room"), props);
	}

	/**
	 * Activate an application instance.
	 *
	 * @parm instance The application instance.
	 */
	public void activateApplication(ApplicationInstance instance)
	{
		GUIPane appPane = (GUIPane) AppContext.instance().getObject("applicationPane." + instance.getUniqueId());

		if (appPane == null)
		{
			Application application = Application.get(instance.getApplicationId());

			Properties props = new Properties();

			props.put("maximizable", new Boolean(false));
			props.put("iconifiable", new Boolean(false));
			props.put("maximized", new Boolean(true));
			props.put("titlebar", new Boolean(false));
			props.put("aktario.applicationInstanceId", new Long(instance.getUniqueId()));
			CommandTools.performAsync(new ShowWindow(application.getGuiPaneId(), "room"), props);
		}
		else
		{
			appPane.getDisplay().bringToFront();
		}
	}
}

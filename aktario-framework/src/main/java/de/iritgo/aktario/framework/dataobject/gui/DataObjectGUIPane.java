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
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.DataObject;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.util.Iterator;


/**
 * DataObjectGUIPane
 */
public class DataObjectGUIPane extends SwingGUIPane implements DynPane
{
	private JPanel panel;

	private String rendererTypeId;

	private String controllerTypeId;

	private boolean loadFromObjectMustFiredAgain;

	private boolean guiRendered;

	private Renderer renderer;

	private ImageIcon iritgoWaitImage;

	private boolean loadFinished = false;

	/**
	 * Create a new DynDataObjectGUIPane.
	 */
	public DataObjectGUIPane()
	{
		super("DataObjectGUIPane");
		loadFromObjectMustFiredAgain = false;

		if (iritgoWaitImage == null)
		{
			iritgoWaitImage = new ImageIcon(getClass().getResource("/resources/app-wait.gif"));
		}
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI()
	{
		if (getIObject() == null)
		{
			return;
		}

		getDisplay().setEnabled(false);

		rendererTypeId = properties.getProperty("renderer", getIObject().getTypeId());
		controllerTypeId = properties.getProperty("controller", getIObject().getTypeId());

		GUIManager guiManager = (GUIManager) Engine.instance().getManagerRegistry().getManager("GUIManager");

		guiManager.registerGUIPane(this);

		Controller controller = guiManager.getController(controllerTypeId);

		if (controller == null)
		{
			GUIControllerRequest guiControllerRequest = new GUIControllerRequest(controllerTypeId,
							GUIControllerRequest.DATAOBJECT);

			ActionTools.sendToServer(guiControllerRequest);
		}
		else
		{
			if (controller.isValid())
			{
				updateGUI();
			}
		}
	}

	public String getControllerTypeId()
	{
		return controllerTypeId;
	}

	public String getRendererTypeId()
	{
		return controllerTypeId;
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane()
	{
		return new DataObjectGUIPane();
	}

	/**
	 * Update the gui if a widget or command description is changed. All descriptions must have a
	 * valid state.
	 */
	public void updateGUI()
	{
		GUIManager guiManager = (GUIManager) Engine.instance().getManagerRegistry().getManager("GUIManager");

		Controller controller = guiManager.getController(controllerTypeId);

		renderer = guiManager.getRenderer(rendererTypeId);

		if (panel != null)
		{
			content.remove(panel);
		}

		panel = new JPanel();

		renderer.workOn(controller, (DataObject) getIObject(), panel, this);

		content.add(panel, createConstraints(0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

		guiRendered = true;

		content.revalidate();

		if (loadFromObjectMustFiredAgain)
		{
			loadFromObject();
		}
	}

	/**
	 * This method is called when the gui pane starts waiting
	 * for the attributes of it's iobject.
	 */
	public void waitingForNewObject()
	{
		loadFinished = false;
	}

	/**
	 * This method is called when the attributes of the gui pane's
	 * iobject are received.
	 */
	public void waitingForNewObjectFinished()
	{
		super.waitingForNewObjectFinished();

		if (! getDisplay().isEnabled())
		{
			getDisplay().setEnabled(true);
		}
	}

	/**
	 * Load the gui-controlls from the object. The gui must in allready loaded.
	 *
	 * @param IObject The loaded object.
	 */
	public void loadFromObject(IObject iobject)
	{
		if (guiRendered)
		{
			//Do controlls
			GUIManager guiManager = (GUIManager) Engine.instance().getManagerRegistry().getManager("GUIManager");

			Controller controller = guiManager.getController(controllerTypeId);

			for (Iterator i = controller.getWidgetDescriptions().iterator(); i.hasNext();)
			{
				WidgetDescription wd = (WidgetDescription) i.next();

				fillControls(wd, (DataObject) iobject);
			}

			if (! getDisplay().isEnabled())
			{
				getDisplay().setEnabled(true);
			}

			return;
		}

		loadFromObjectMustFiredAgain = true;
	}

	/**
	 * Fills the controlls with the data object
	 *
	 * @param WidgetDescription Hold a description of all gui elements.
	 * @param DataObject  The data object to get the content.
	 */
	private void fillControls(WidgetDescription wd, DataObject dataObject)
	{
		ResourceService resources = Engine.instance().getResourceService();

		for (Iterator i = wd.getWidgetDescriptions().iterator(); i.hasNext();)
		{
			WidgetDescription wdItem = (WidgetDescription) i.next();

			if (wdItem.getWidgetDescriptions().size() > 0)
			{
				fillControls(wdItem, dataObject);
			}

			Object object = dataObject.getAttribute(wdItem.getWidgetId());

			if (object != null)
			{
				if (wdItem.getRendererId().equals("textarea"))
				{
					((JTextArea) wdItem.getControl(wdItem.getWidgetId())).setText("" + object);
				}
				else if (wdItem.getRendererId().equals("salutation") || wdItem.getRendererId().equals("country"))
				{
					JComboBox combobox = (JComboBox) wdItem.getControl(wdItem.getWidgetId());

					for (int j = 0; j < combobox.getItemCount(); ++j)
					{
						DefaultRenderer.ComboboxItem item = (DefaultRenderer.ComboboxItem) combobox.getItemAt(j);

						if (item.getId().equals(object))
						{
							combobox.setSelectedIndex(j);

							break;
						}
					}
				}
				else if (wdItem.getRendererId().equals("combo"))
				{
					JComboBox combobox = (JComboBox) wdItem.getControl(wdItem.getWidgetId());

					String[] validValues = ((String) dataObject.getAttribute(wdItem.getWidgetId() + "ValidValues"))
									.split("\\|");

					for (int j = 0; j < validValues.length; ++j)
					{
						String key = validValues[j++];

						if (j < validValues.length)
						{
							String value = validValues[j];

							combobox.addItem(new DefaultRenderer.ComboboxItem(key, value));
						}
					}

					if (validValues.length >= 2)
					{
						combobox.setSelectedIndex(0);
					}

					for (int j = 0; j < combobox.getItemCount(); ++j)
					{
						DefaultRenderer.ComboboxItem item = (DefaultRenderer.ComboboxItem) combobox.getItemAt(j);

						if (item.getId().equals(object))
						{
							combobox.setSelectedIndex(j);

							break;
						}
					}
				}
				else
				{
					((JTextField) wdItem.getControl(wdItem.getWidgetId())).setText("" + object);
				}
			}
		}
	}

	/**
	 * Store all gui controll informations in the data object.
	 *
	 * @param The data object
	 */
	public void storeToObject(IObject iobject)
	{
		GUIManager guiManager = (GUIManager) Engine.instance().getManagerRegistry().getManager("GUIManager");

		Controller controller = guiManager.getController(controllerTypeId);

		for (Iterator i = controller.getWidgetDescriptions().iterator(); i.hasNext();)
		{
			WidgetDescription wd = (WidgetDescription) i.next();

			fillObject(wd, (DataObject) iobject);
		}
	}

	/**
	 * Fills the data object from the controlls.
	 *
	 * @param WidgetDescription Hold a description of all gui elements.
	 * @param DataObject  The data object to get the content.
	 */
	private void fillObject(WidgetDescription wd, DataObject dataObject)
	{
		for (Iterator i = wd.getWidgetDescriptions().iterator(); i.hasNext();)
		{
			WidgetDescription wdItem = (WidgetDescription) i.next();

			if (wdItem.getWidgetDescriptions().size() > 0)
			{
				fillObject(wdItem, dataObject);
			}

			if (wdItem.getRendererId().equals("textarea"))
			{
				dataObject.setAttribute(wdItem.getWidgetId(), ((JTextArea) wdItem.getControl(wdItem.getWidgetId()))
								.getText());
			}
			else if (wdItem.getRendererId().equals("salutation") || wdItem.getRendererId().equals("country")
							|| wdItem.getRendererId().equals("combo"))
			{
				dataObject.setAttribute(wdItem.getWidgetId(), ((DefaultRenderer.ComboboxItem) ((JComboBox) wdItem
								.getControl(wdItem.getWidgetId())).getSelectedItem()).getId());
			}
			else
			{
				dataObject.setAttribute(wdItem.getWidgetId(), ((JTextField) wdItem.getControl(wdItem.getWidgetId()))
								.getText());
			}
		}
	}

	public boolean checkClientErrors()
	{
		boolean errors = false;

		GUIManager guiManager = (GUIManager) Engine.instance().getManagerRegistry().getManager("GUIManager");

		Controller controller = guiManager.getController(getIObject().getTypeId());

		if (controller != null)
		{
			for (Iterator i = controller.getWidgetDescriptions().iterator(); i.hasNext();)
			{
				WidgetDescription wd = (WidgetDescription) i.next();

				if (wd.getRendererId().equals("group"))
				{
					for (Iterator j = wd.getWidgetDescriptions().iterator(); j.hasNext();)
					{
						WidgetDescription wdGroupItem = (WidgetDescription) j.next();

						if (wdGroupItem.isMandatoryField())
						{
							if (((DataObject) getIObject()).getStringAttribute(wdGroupItem.getWidgetId()).equals(""))
							{
								errors = true;
								setError(wdGroupItem.getWidgetId());

								continue;
							}
							else
							{
								setNoError(wdGroupItem.getWidgetId());
							}
						}
					}
				}
			}
		}

		DataObjectGUIValidatorEvent e = new DataObjectGUIValidatorEvent((DataObject) getIObject(), controller);

		Engine.instance().getEventRegistry().fire("system", e);

		return errors || e.isError();
	}

	public void setError(String widgetId)
	{
		renderer.setError(widgetId);
		content.revalidate();
	}

	public void setNoError(String widgetId)
	{
		renderer.setNoError(widgetId);
		content.revalidate();
	}

	/**
	 * Close the display.
	 */
	public void close()
	{
		if (renderer != null)
		{
			renderer.close();
		}

		GUIManager guiManager = (GUIManager) Engine.instance().getManagerRegistry().getManager("GUIManager");

		guiManager.unregisterGUIPane(this);

		renderer = null;
		super.close();
	}

	/**
	 * Close the display.
	 */
	public void systemClose()
	{
		if (renderer != null)
		{
			renderer.close();
		}

		GUIManager guiManager = (GUIManager) Engine.instance().getManagerRegistry().getManager("GUIManager");

		guiManager.unregisterGUIPane(this);

		renderer = null;

		super.systemClose();
	}

	public boolean isToCloseAfterSave()
	{
		return properties.getProperty("closeaftersave", "yes").equals("yes");
	}
}

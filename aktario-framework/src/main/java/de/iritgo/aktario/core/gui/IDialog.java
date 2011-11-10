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

package de.iritgo.aktario.core.gui;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.core.iobject.IObjectProxyRegistry;
import de.iritgo.aktario.core.sessioncontext.SessionContext;
import javax.swing.Icon;
import java.awt.Rectangle;
import java.util.Properties;


/**
 * IDialogs display IObjects in a dialog.
 */
public class IDialog extends BaseObject implements IDisplay
{
	/** The dialog properties. */
	private Properties properties;

	/** The gui pane. */
	private GUIPane guiPane;

	/** Our dialog frame. */
	private IDialogFrame dialogFrame;

	/** Our desktop manager. */
	private IDesktopManager desktopManager;

	/** On screen unique id. */
	private String onScreenUniqueId;

	/**
	 * Create a new IDialog.
	 */
	public IDialog()
	{
		this("IWindow");
	}

	/**
	 * Create a new IDialog.
	 *
	 * @param dialogId The dialog id.
	 */
	public IDialog(String dialogId)
	{
		super(dialogId);
		properties = new Properties();
		onScreenUniqueId = dialogId;
	}

	/**
	 * Create a new IDialog.
	 *
	 * @param dialogId The dialog id.
	 */
	public IDialog(String dialogId, String onScreenUniqueId)
	{
		super(dialogId);
		properties = new Properties();
		this.onScreenUniqueId = onScreenUniqueId;
	}

	/**
	 * Get the on screen unique id of this display.
	 *
	 * @return The display's id.
	 */
	public String getOnScreenUniqueId()
	{
		return onScreenUniqueId;
	}

	/**
	 * Initialize the dialog's gui.
	 *
	 * @param guiPaneId The id of the gui pane to be displayed in this dialog.
	 */
	public void initGUI(String guiPaneId)
	{
		initGUI(guiPaneId, guiPaneId, null, null);
	}

	/**
	 * Initialize the dialog's gui.
	 *
	 * @param guiPaneId The id of the gui pane to be displayed in this dialog.
	 * @param sessionContext The session context.
	 */
	public void initGUI(String guiPaneId, String onScreenUniqueId, SessionContext sessionContext)
	{
		initGUI(guiPaneId, onScreenUniqueId, null, sessionContext);
	}

	/**
	 * Initialize the dialog's gui.
	 *
	 * @param guiPaneId The id of the gui pane to be displayed in this dialog.
	 * @param onScreenUniqueId The id of the guipane on the screen.
	 * @param object The data object to be displayed in this dialog.
	 * @param sessionContext The session context.
	 */
	public void initGUI(String guiPaneId, String onScreenUniqueId, IObject object, SessionContext sessionContext)
	{
		dialogFrame = Engine.instance().getGUIFactory().createDialogFrame(this, getTypeId(), properties);

		guiPane = (GUIPane) GUIPaneRegistry.instance().create(guiPaneId);
		guiPane.setProperties(properties);
		guiPane.setObject(object);
		guiPane.setSessionContext(sessionContext);
		guiPane.setIDisplay(this);
		guiPane.setOnScreenUniqueId(onScreenUniqueId);

		if (properties.get("bounds") != null)
		{
			dialogFrame.setBounds((Rectangle) properties.get("bounds"));
		}

		if (properties.get("title") != null)
		{
			dialogFrame.setTitle((String) properties.get("title"));
		}

		if (properties.get("name") != null)
		{
			dialogFrame.setName((String) properties.get("name"));
		}
		else
		{
			dialogFrame.setName(guiPane.getTypeId());
		}

		guiPane.initGUI();

		if (object != null)
		{
			guiPane.setObject(object);
			guiPane.registerProxyEventListener();

			IObjectProxyRegistry proxyRegistry = Engine.instance().getProxyRegistry();
			IObjectProxy prototypeProxy = (IObjectProxy) proxyRegistry.getProxy(object.getUniqueId(), object
							.getTypeId());

			IObject prototypeable = prototypeProxy.getRealObject();

			guiPane.setObject(prototypeable);

			if (prototypeable != null)
			{
				loadFromObject();
			}
		}
	}

	/**
	 * Set the gui pane of this display.
	 *
	 * @param The gui pane.
	 */
	public void setGUIPane(GUIPane guiPane)
	{
		this.guiPane = guiPane;
	}

	/**
	 *  Get the GUIPane of this dialog.
	 *
	 * @return The gui pane.
	 */
	public GUIPane getGUIPane()
	{
		return guiPane;
	}

	/**
	 * Load the object attributes into the gui.
	 */
	public void loadFromObject()
	{
		guiPane.loadFromObject();
	}

	/**
	 * Store the gui values to the object's attributes.
	 */
	public void storeToObject()
	{
		guiPane.storeToObject();
	}

	/**
	 * Close the dialog.
	 */
	public void close()
	{
		if (guiPane != null)
		{
			guiPane.close();
		}

		if (dialogFrame != null)
		{
			dialogFrame.close();
		}

		desktopManager.removeDisplay(this);
	}

	/**
	 * Called from swing, the dialog will closed.
	 */
	public void systemClose()
	{
		if (guiPane != null)
		{
			guiPane.systemClose();
		}

		if (dialogFrame != null)
		{
			dialogFrame.systemClose();
		}

		desktopManager.removeDisplay(this);
	}

	/**
	 * Set the desktop manager.
	 *
	 * @param desktopManager The desktop manager.
	 */
	public void setDesktopManager(IDesktopManager desktopManager)
	{
		this.desktopManager = desktopManager;
	}

	/**
	 * Get the desktop manager.
	 *
	 * @return The desktop manager.
	 */
	public IDesktopManager getDesktopManager()
	{
		return desktopManager;
	}

	/**
	 * Get the id of the desktop on which this display is displayed.
	 *
	 * @return The desktop id (or null if this display is a dialog).
	 */
	public String getDesktopId()
	{
		return null;
	}

	/**
	 * Set the id of the desktop on which this display is displayed.
	 *
	 * @param desktopId The desktop id.
	 */
	public void setDesktopId(String desktopId)
	{
	}

	/**
	 * Get the data object shown in this display.
	 *
	 * @return The data object.
	 */
	public IObject getDataObject()
	{
		return guiPane.getObject();
	}

	/**
	 * Set the dialog title. This title will be displayed on the
	 * dialog frame's title bar.
	 *
	 * @param title The new title.
	 */
	public void setTitle(String title)
	{
		dialogFrame.setTitle(title);
	}

	/**
	 * Get the dialog title.
	 *
	 * @return The gui pane title.
	 */
	public String getTitle()
	{
		return dialogFrame.getTitle();
	}

	/**
	 * Set a display property.
	 *
	 * @param key The key under which to store the property.
	 * @param value The property value.
	 */
	public void putProperty(String key, Object value)
	{
		properties.put(key, value);
	}

	/**
	 * Get a display property.
	 *
	 * @param key The key of the property to retrieve.
	 * @return The property value.
	 */
	public Object getProperty(String key)
	{
		return properties.get(key);
	}

	/**
	 * Set the display properties.
	 *
	 * @param properties The new properties.
	 */
	public void setProperties(Properties properties)
	{
		if (properties != null)
		{
			this.properties = properties;
		}
	}

	/**
	 * Get the display properties.
	 *
	 * @return The display properties.
	 */
	public Properties getProperties()
	{
		return properties;
	}

	/**
	 * Remove a display property.
	 *
	 * @param key The key of the property to remove.
	 */
	public void removeProperty(String key)
	{
		properties.remove(key);
	}

	/**
	 * Set the window icon. This icon will be displayed on the
	 * window frame's title bar.
	 *
	 * @param icon The new icon.
	 */
	public void setIcon(Icon icon)
	{
		dialogFrame.setIcon(icon);
	}

	/**
	 * Get the display's icon.
	 *
	 * @return The display's icon.
	 */
	public Icon getIcon()
	{
		return dialogFrame.getIcon();
	}

	/**
	 * Get the dialog frame of this IDialog
	 *
	 * @return The dialog frame.
	 */
	public IDialogFrame getDialogFrame()
	{
		return dialogFrame;
	}

	/**
	 * Show the Dialog.
	 */
	public void show()
	{
		dialogFrame.showDialog();
	}

	/**
	 * Enable/disable the dialog.
	 *
	 * @param enabled If true the dialog is enabled.
	 */
	public void setEnabled(boolean enabled)
	{
		dialogFrame.setEnabled(enabled);
	}

	/**
	 * Check wether the display is enabled or not.
	 *
	 * @return True if the display is enabled.
	 */
	public boolean isEnabled()
	{
		return dialogFrame.isEnabled();
	}

	/**
	 * Make this window the topmost window.
	 */
	public void bringToFront()
	{
	}
}

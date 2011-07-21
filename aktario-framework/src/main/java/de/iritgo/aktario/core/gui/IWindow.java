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
 *
 */
public class IWindow extends BaseObject implements IDisplay
{
	/** The window properties. */
	private Properties properties;

	/** The gui pane. */
	private GUIPane guiPane;

	/** The id of the desktop on which we are displayed. */
	private String desktopId;

	/** Our desktop manager. */
	private IDesktopManager desktopManager;

	/** Our window frame. */
	private IWindowFrame windowFrame;

	/** On screen unique id. */
	private String onScreenUniqueId;

	/**
	 * Create a new IWindow.
	 */
	public IWindow ()
	{
		this ("IWindow");
	}

	/**
	 * Create a new IWindow.
	 *
	 * @param windowId The window id.
	 */
	public IWindow (String windowId)
	{
		super (windowId);
		properties = new Properties ();
		onScreenUniqueId = windowId;
	}

	/**
	 * Create a new IWindow.
	 *
	 * @param windowId The window id.
	 */
	public IWindow (String windowId, String onScreenUniqueId)
	{
		super (windowId);
		properties = new Properties ();
		this.onScreenUniqueId = onScreenUniqueId;
	}

	/**
	 * Get the on screen unique id of this display.
	 *
	 * @return The display's id.
	 */
	public String getOnScreenUniqueId ()
	{
		return onScreenUniqueId;
	}

	/**
	 * Initialize the gui.
	 *
	 * @param guiPaneId The id of the gui pane that is to be displayed in this
	 *   window.
	 */
	public void initGUI (String guiPaneId)
	{
		initGUI (guiPaneId, guiPaneId, null, null);
	}

	/**
	 * Initialize the gui.
	 *
	 * @param guiPaneId The id of the gui pane that is to be displayed in this
	 *   window.
	 * @param sessionContext The session contxt.
	 */
	public void initGUI (String guiPaneId, String onScreenUniqueId, SessionContext sessionContext)
	{
		initGUI (guiPaneId, onScreenUniqueId, null, sessionContext);
	}

	/**
	 * Initialize the gui.
	 *
	 * @param guiPaneId The id of the gui pane that is to be displayed in this
	 *   window.
	 * @param object The IObject that is to be displayed in this window.
	 * @param sessionContext The session contxt.
	 */
	public void initGUI (String guiPaneId, String onScreenUniqueId, IObject object, SessionContext sessionContext)
	{
		this.onScreenUniqueId = onScreenUniqueId;
		guiPane = (GUIPane) GUIPaneRegistry.instance ().create (guiPaneId);
		guiPane.setProperties (properties);
		guiPane.setObject (object);
		guiPane.setSessionContext (sessionContext);
		guiPane.setOnScreenUniqueId (onScreenUniqueId);

		boolean resizable = true;
		boolean closable = true;
		boolean maximizable = true;
		boolean iconifiable = true;
		boolean titlebar = true;
		boolean initVisible = true;

		if (properties.get ("resizable") != null)
		{
			resizable = ((Boolean) properties.get ("resizable")).booleanValue ();
		}

		if (properties.get ("closable") != null)
		{
			closable = ((Boolean) properties.get ("closable")).booleanValue ();
		}

		if (properties.get ("maximizable") != null)
		{
			maximizable = ((Boolean) properties.get ("maximizable")).booleanValue ();
		}

		if (properties.get ("iconifiable") != null)
		{
			iconifiable = ((Boolean) properties.get ("iconifiable")).booleanValue ();
		}

		if (properties.get ("titlebar") != null)
		{
			titlebar = ((Boolean) properties.get ("titlebar")).booleanValue ();
		}

		if (properties.get ("visible") != null)
		{
			initVisible = ((Boolean) properties.get ("visible")).booleanValue ();
		}

		windowFrame = Engine.instance ().getGUIFactory ().createWindowFrame (this, getTypeId (), resizable, closable,
						maximizable, iconifiable, titlebar, initVisible, properties);

		windowFrame.setProperties (properties);

		guiPane.setIDisplay (this);

		if (properties.get ("bounds") != null)
		{
			windowFrame.setBounds ((Rectangle) properties.get ("bounds"));
		}

		if (properties.get ("maximized") != null)
		{
			try
			{
				windowFrame.setMaximized (((Boolean) properties.get ("maximized")).booleanValue ());
			}
			catch (Exception x)
			{
			}
		}

		if (properties.get ("title") != null)
		{
			windowFrame.setTitle ((String) properties.get ("title"));
		}

		guiPane.initGUI ();

		if (properties.get ("weightx") != null)
		{
			Rectangle bounds = windowFrame.getBounds ();

			bounds.width *= ((Double) properties.get ("weightx")).doubleValue ();
			windowFrame.setBounds (bounds);
		}

		if (properties.get ("weighty") != null)
		{
			Rectangle bounds = windowFrame.getBounds ();

			bounds.height *= ((Double) properties.get ("eightx")).doubleValue ();
			windowFrame.setBounds (bounds);
		}

		if (properties.get ("name") != null)
		{
			windowFrame.setName ((String) properties.get ("name"));
		}
		else
		{
			windowFrame.setName (guiPane.getTypeId ());
		}

		if (object != null)
		{
			registerIObject (object, guiPane);
		}
	}

	public void registerIObject (IObject object, GUIPane guiPane)
	{
		IObjectProxyRegistry proxyRegistry = Engine.instance ().getProxyRegistry ();
		IObjectProxy prototypeProxy = (IObjectProxy) proxyRegistry
						.getProxy (object.getUniqueId (), object.getTypeId ());

		IObject prototypeable = prototypeProxy.getRealObject ();

		guiPane.setObject (prototypeable);
		guiPane.registerProxyEventListener ();

		if (prototypeable != null)
		{
			loadFromObject ();
		}
	}

	public void initGUIPane ()
	{
		guiPane.initGUI ();
	}

	/**
	 * Set the gui pane of this display.
	 *
	 * @param The gui pane.
	 */
	public void setGUIPane (GUIPane guiPane)
	{
		this.guiPane = guiPane;
	}

	/**
	 * Retrieve the gui pane of this window.
	 *
	 * @return The gui pane.
	 */
	public GUIPane getGUIPane ()
	{
		return guiPane;
	}

	/**
	 * LoadFromObject, loads the GUI from object.
	 */
	public void loadFromObject ()
	{
		guiPane.loadFromObject ();
	}

	/**
	 * StoreToObject, save the GUI to object.
	 */
	public void storeToObject ()
	{
		guiPane.storeToObject ();
	}

	/**
	 * Get the id of the desktop on which this display is displayed.
	 *
	 * @return The desktop id (or null if this display is a dialog).
	 */
	public String getDesktopId ()
	{
		return desktopId;
	}

	/**
	 * Set the id of the desktop on which this display is displayed.
	 *
	 * @param desktopId The desktop id.
	 */
	public void setDesktopId (String desktopId)
	{
		this.desktopId = desktopId;
	}

	/**
	 * Set the desktop manager.
	 *
	 * @param desktopManager The desktop manager.
	 */
	public void setDesktopManager (IDesktopManager desktopManager)
	{
		this.desktopManager = desktopManager;
	}

	/**
	 * Get the desktop manager.
	 *
	 * @return The desktop manager.
	 */
	public IDesktopManager getDesktopManager ()
	{
		return desktopManager;
	}

	/**
	 * Close the window.
	 */
	public void close ()
	{
		if (guiPane != null)
		{
			guiPane.close ();
		}

		if (windowFrame != null)
		{
			windowFrame.close ();
		}

		desktopManager.removeDisplay (this);
		Engine.instance ().getEventRegistry ().fire ("iwindowframe.closed", new IDisplayClosedEvent (this));
	}

	/**
	 * Close the window.
	 */
	public void systemClose ()
	{
		if (guiPane != null)
		{
			guiPane.systemClose ();
		}

		if (windowFrame != null)
		{
			windowFrame.systemClose ();
		}

		desktopManager.removeDisplay (this);
		Engine.instance ().getEventRegistry ().fire ("iwindowframe.closed", new IDisplayClosedEvent (this));
	}

	/**
	 * Get the data object shown in this display.
	 *
	 * @return The data object.
	 */
	public IObject getDataObject ()
	{
		return guiPane.getObject ();
	}

	/**
	 * Set the window title. This title will be displayed on the window frame's
	 * title bar.
	 *
	 * @param title The new title.
	 */
	public void setTitle (String title)
	{
		windowFrame.setTitle (title);
	}

	/**
	 * Get the window title.
	 *
	 * @return The window title.
	 */
	public String getTitle ()
	{
		return windowFrame.getTitle ();
	}

	/**
	 * Set the window icon. This icon will be displayed on the window frame's
	 * title bar.
	 *
	 * @param icon The icon.
	 */
	public void setIcon (Icon icon)
	{
		windowFrame.setIcon (icon);
	}

	/**
	 * Get the display's icon.
	 *
	 * @return The display's icon.
	 */
	public Icon getIcon ()
	{
		return windowFrame.getIcon ();
	}

	/**
	 * Set a display property.
	 *
	 * @param key The key under which to store the property.
	 * @param value The property value.
	 */
	public void putProperty (String key, Object value)
	{
		properties.put (key, value);
	}

	/**
	 * Get a display property.
	 *
	 * @param key The key of the property to retrieve.
	 * @return The property value.
	 */
	public Object getProperty (String key)
	{
		return properties.get (key);
	}

	/**
	 * Set the display properties.
	 *
	 * @param properties The new properties.
	 */
	public void setProperties (Properties properties)
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
	public Properties getProperties ()
	{
		return properties;
	}

	/**
	 * Remove a display property.
	 *
	 * @param key The key of the property to remove.
	 */
	public void removeProperty (String key)
	{
		properties.remove (key);
	}

	/**
	 * Get the window frame of this IWindow
	 *
	 * @return The window frame.
	 */
	public IWindowFrame getWindowFrame ()
	{
		return windowFrame;
	}

	/**
	 * Show the window frame.
	 */
	public void show ()
	{
		windowFrame.showWindow ();

		Engine.instance ().getEventRegistry ().fire ("iwindowframe.opened", new IDisplayOpenedEvent (this));
	}

	/**
	 * Enable/disable the window.
	 *
	 * @param enabled If true the window is enabled.
	 */
	public void setEnabled (boolean enabled)
	{
		windowFrame.setEnabled (enabled);
	}

	/**
	 * Check wether the display is enabled or not.
	 *
	 * @return True if the display is enabled.
	 */
	public boolean isEnabled ()
	{
		return windowFrame.isEnabled ();
	}

	/**
	 * Make this window the topmost window.
	 */
	public void bringToFront ()
	{
		windowFrame.bringToFront ();
	}
}

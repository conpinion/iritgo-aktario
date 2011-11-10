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
import de.iritgo.aktario.core.iobject.IObjectProxyEvent;
import de.iritgo.aktario.core.iobject.IObjectProxyListener;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.sessioncontext.SessionContext;
import java.awt.Component;
import java.util.Properties;


/**
 * GUIPanes display the attributes of IObjects.
 */
public abstract class GUIPane extends BaseObject implements IObjectProxyListener
{
	/** The IObject that is displayed in this gui pane. */
	private IObject iObject;

	/** The IDisplay in which the gui pane is rendered. */
	protected IDisplay display;

	/** The current onScreen uniqueId of the guipane. */
	protected String onScreenUniqueId;

	/** The current session context. */
	protected SessionContext sessionContext;

	/** The properties. */
	protected Properties properties;

	/**
	 * Create a new GUIPane.
	 *
	 * @param guiPaneId The id of the new gui pane.
	 */
	public GUIPane(String guiPaneId, String onScreenUniqueId)
	{
		super(guiPaneId);
		this.onScreenUniqueId = onScreenUniqueId;
	}

	/**
	 * Set the current onScreen unique id of the guipane.
	 */
	public void setOnScreenUniqueId(String onScreenUniqueId)
	{
		this.onScreenUniqueId = onScreenUniqueId;
	}

	/**
	 * Get the current onScreen unique id of the gui pane
	 *
	 * @return The OnScreenUniqueId.
	 */
	public String getOnScreenUniqueId()
	{
		return onScreenUniqueId;
	}

	/**
	 * Set the IOject to display.
	 */
	public void setObject(IObject iObject)
	{
		this.iObject = iObject;
	}

	/**
	 * Get the IObject that is displayed in this gui pane.
	 *
	 * @return The IObject.
	 */
	public IObject getObject()
	{
		return iObject;
	}

	/**
	 * Set the display.
	 *
	 * @param display The new display.
	 */
	public void setIDisplay(IDisplay display)
	{
		this.display = display;
		setIDisplayImpl(display);
	}

	/**
	 * Implementation specific tasks for setting the display.
	 *
	 * @param display The new display.
	 */
	public abstract void setIDisplayImpl(IDisplay display);

	/**
	 * Get the display.
	 *
	 * @return The display.
	 */
	public IDisplay getDisplay()
	{
		return display;
	}

	/**
	 * Set the session context.
	 *
	 * @param sessionContext The new session context.
	 */
	public void setSessionContext(SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
	}

	/**
	 * Get the current session context.
	 *
	 * @return The session context.
	 */
	public SessionContext getSessionContext()
	{
		return sessionContext;
	}

	/**
	 * Register-Proxy-Event-Listener.
	 */
	public void registerProxyEventListener()
	{
		Engine.instance().getProxyEventRegistry().addEventListener(iObject, this);
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI()
	{
	}

	/**
	 * This method is called if a proxy event occurred.
	 *
	 * @param event The proxy event.
	 */
	public void proxyEvent(IObjectProxyEvent event)
	{
		if (iObject == null)
		{
			return;
		}

		if (event.isWaitingForNewObject())
		{
			waitingForNewObject();
		}
		else
		{
			waitingForNewObjectFinished();
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject()
	{
		if (iObject != null)
		{
			loadFromObject(iObject);
		}
	}

	/**
	 * Store the current gui values into the data object attributes.
	 *
	 * @param transaction The transaction object.
	 */
	public void storeToObject()
	{
		if (iObject != null)
		{
			storeToObject(iObject);
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 * @param iObject The iobject.
	 */
	public abstract void loadFromObject(IObject iObject);

	/**
	 * Store the current gui values into the data object attributes.
	 * @param iObject The iobject.
	 */
	public abstract void storeToObject(IObject iObject);

	/**
	 * This method is called when the gui pane starts waiting
	 * for the attributes of it's iobject.
	 */
	public void waitingForNewObject()
	{
	}

	/**
	 * This method is called when the attributes of the gui pane's
	 * iobject are received.
	 */
	public void waitingForNewObjectFinished()
	{
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public abstract GUIPane cloneGUIPane();

	/**
	 * Close the display.
	 */
	public void close()
	{
		if (iObject != null)
		{
			Engine.instance().getProxyEventRegistry().removeEventListener(iObject, this);
		}
	}

	/**
	 * Close the display.
	 */
	public void systemClose()
	{
		if (iObject != null)
		{
			Engine.instance().getProxyEventRegistry().removeEventListener(iObject, this);
		}
	}

	/**
	 * Set the gui pane title. This title will be displayed on the
	 * display frame's title bar, to which this gui pane belongs.
	 *
	 * @param title The new title.
	 */
	public void setTitle(String title)
	{
		display.setTitle(title);
	}

	/**
	 * Get the gui pane title.
	 *
	 * @return The gui pane title.
	 */
	public String getTitle()
	{
		return display.getTitle();
	}

	/**
	 * Get the resources.
	 *
	 * @return The resources.
	 */
	public ResourceService getResources()
	{
		return Engine.instance().getResourceService();
	}

	protected IObject getIObject()
	{
		if (iObject == null)
		{
			return null;
		}

		// Set the object in a transaction state.
		Engine.instance().getBaseRegistry().get(iObject.getUniqueId(), iObject.getTypeId());

		return iObject;
	}

	/**
	 * Set the content pane.
	 *
	 * @param content The new content pane.
	 */
	public void setContentPane(Object content)
	{
	}

	/**
	 * Get the content pane.
	 *
	 * @param content The new content pane.
	 */
	public Component getContentPane()
	{
		return null;
	}

	protected void setProperties(Properties properties)
	{
		this.properties = properties;
	}

	public Properties getProperties()
	{
		return properties;
	}
}

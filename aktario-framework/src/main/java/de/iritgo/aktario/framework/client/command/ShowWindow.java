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

package de.iritgo.aktario.framework.client.command;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.GUIPaneRegistry;
import de.iritgo.aktario.core.gui.IWindow;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.sessioncontext.SessionContext;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.client.Client;


/**
 *
 */
public class ShowWindow extends Command
{
	private String guiPaneId;

	private String desktopId;

	private IObject iObject;

	private SessionContext sessionContext;

	private String onScreenUniqueId;

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId)
	{
		init(guiPaneId, guiPaneId, null, null, null);
	}

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId, String desktopId)
	{
		init(guiPaneId, guiPaneId, desktopId, null, null);
	}

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId, String onScreenUniqueId, String desktopId)
	{
		init(guiPaneId, onScreenUniqueId, desktopId, null, null);
	}

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId, String desktopId, IObject iObject)
	{
		init(guiPaneId, guiPaneId, desktopId, iObject, null);
	}

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId, String onScreenUniqueId, String desktopId, IObject iObject)
	{
		init(guiPaneId, onScreenUniqueId, desktopId, iObject, null);
	}

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId, long unqiueId)
	{
		this(guiPaneId, unqiueId, null);
	}

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId, IObject iObject)
	{
		init(guiPaneId, guiPaneId, null, iObject, null);
	}

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId, long unqiueId, String typeId)
	{
		this(guiPaneId, guiPaneId, unqiueId, typeId, null);
	}

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId, SessionContext sessionContext)
	{
		init(guiPaneId, guiPaneId, null, null, sessionContext);
	}

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId, String onScreenUniqueId, IObject iObject, SessionContext sessionContext,
					String desktopId)
	{
		init(guiPaneId, onScreenUniqueId, desktopId, iObject, sessionContext);
	}

	/**
	 * Standard constructor
	 */
	public ShowWindow(String guiPaneId, String onScreenUniqueId, long unqiueId, String typeId, String desktopId)
	{
		GUIPane guiPane = GUIPaneRegistry.instance().create(guiPaneId);

		IObject iObject = (IObject) Engine.instance().getBaseRegistry().get(unqiueId, typeId);

		if (iObject == null)
		{
			try
			{
				iObject = Engine.instance().getIObjectFactory().newInstance(typeId);
			}
			catch (NoSuchIObjectException e)
			{
				return;
			}

			iObject.setUniqueId(unqiueId);
			Engine.instance().getBaseRegistry().add((BaseObject) iObject);

			FrameworkProxy proxy = new FrameworkProxy(iObject);

			proxy.setSampleRealObject((IObject) iObject);
			Engine.instance().getProxyRegistry().addProxy(proxy, iObject.getTypeId());
		}

		init(guiPaneId, onScreenUniqueId, desktopId, iObject, null);
	}

	/**
	 * Display the IWindow-Pane.
	 */
	public void perform()
	{
		final IWindow window = new IWindow(guiPaneId, onScreenUniqueId);

		window.setProperties(properties);
		window.setDesktopManager(Client.instance().getClientGUI().getDesktopManager());
		window.setDesktopId(desktopId);

		if (iObject == null)
		{
			window.initGUI(guiPaneId, onScreenUniqueId, sessionContext);
		}
		else
		{
			window.initGUI(guiPaneId, onScreenUniqueId, iObject, sessionContext);
		}

		Client.instance().getClientGUI().getDesktopManager().addDisplay(window, desktopId);

		window.show();
	}

	public boolean canPerform()
	{
		return true;
	}

	private void init(String guiPaneId, String onScreenUniqueId, String desktopId, IObject iObject,
					SessionContext sessionContext)
	{
		this.onScreenUniqueId = onScreenUniqueId;
		this.guiPaneId = guiPaneId;
		this.desktopId = desktopId;
		this.iObject = iObject;
		this.sessionContext = sessionContext;
	}
}

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
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.core.iobject.IObjectProxyEvent;
import de.iritgo.aktario.core.iobject.IObjectProxyListener;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


/**
 *
 */
public class GUIManager extends BaseObject implements Manager, IObjectProxyListener, UserListener
{
	private HashMap controllers;

	private HashMap renderers;

	private HashMap guiPanes;

	private DefaultRenderer defaultRenderer;

	private DefaultQueryRenderer defaultQueryRenderer;

	public GUIManager()
	{
		super("GUIManager");
		init();
	}

	public void init()
	{
		controllers = new HashMap();
		renderers = new HashMap();
		guiPanes = new HashMap();
		defaultRenderer = new DefaultRenderer();
		defaultQueryRenderer = new DefaultQueryRenderer();
		Engine.instance().getEventRegistry().addListener("proxyisuptodate", this);
		Engine.instance().getEventRegistry().addListener("User", this);
	}

	/**
	 * This method is called when user has logged in.
	 *
	 * @param event The user event.
	 */
	public void userEvent(UserEvent event)
	{
		if ((event != null) && (event.isLoggedIn()))
		{
			User user = event.getUser();
		}

		if ((event != null) && (event.isLoggedOut()))
		{
			controllers = new HashMap();
			renderers = new HashMap();
			guiPanes = new HashMap();
			defaultRenderer = new DefaultRenderer();
			defaultQueryRenderer = new DefaultQueryRenderer();
		}
	}

	/**
	 * This method is called if a proxy event occurred.
	 *
	 * @param event The proxy event.
	 */
	public void proxyEvent(IObjectProxyEvent event)
	{
		if (event.isWaitingForNewObject())
		{
			return;
		}

		controllerChanged(event.getObject());
	}

	public void controllerChanged(IObject iobject)
	{
		String objectTypeId = iobject.getTypeId();
		Controller controller = null;

		if ((iobject instanceof WidgetDescription) || (iobject instanceof CommandDescription))
		{
			long controllerUniqueId = ((DataObject) iobject).getLongAttribute("controllerUniqueId");

			controller = (Controller) Engine.instance().getBaseRegistry().get(controllerUniqueId, "Controller");
		}
		else
		{
			if (iobject instanceof Controller)
			{
				controller = (Controller) iobject;
			}
			else
			{
				return;
			}
		}

		if ((controller != null) && (controller.isValid()))
		{
			for (Iterator i = getGUIPanes(controller.getControllerTypeId()).iterator(); i.hasNext();)
			{
				((DynPane) i.next()).updateGUI();
			}
		}
	}

	public void registerGUIPane(SwingGUIPane dataObjectGUIPane)
	{
		LinkedList list = (LinkedList) guiPanes.get(((DynPane) dataObjectGUIPane).getControllerTypeId());

		if (list == null)
		{
			list = new LinkedList();
			guiPanes.put(((DynPane) dataObjectGUIPane).getControllerTypeId(), list);
		}

		list.add(dataObjectGUIPane);
	}

	public LinkedList getGUIPanes(String typeId)
	{
		return (LinkedList) guiPanes.get(typeId);
	}

	public void unregisterGUIPane(SwingGUIPane dataObjectGUIPane)
	{
		LinkedList list = (LinkedList) guiPanes.get(((DynPane) dataObjectGUIPane).getControllerTypeId());

		if (list == null)
		{
			return;
		}

		list.remove(dataObjectGUIPane);
	}

	public void addController(String typeId, Controller controller)
	{
		controllers.put(typeId, controller);
	}

	public Controller getController(String typeId)
	{
		return (Controller) controllers.get(typeId);
	}

	public void removeController(Controller controller)
	{
		controllers.remove(controller);
	}

	public void addRenderer(Renderer renderer)
	{
		renderers.put(renderer.getTypeId(), renderer);
	}

	public Renderer getRenderer(String typeId)
	{
		Renderer renderer = (Renderer) renderers.get(typeId);

		if (renderer == null)
		{
			return new DefaultRenderer();
		}

		return renderer;
	}

	public Renderer getQueryRenderer(String typeId)
	{
		Renderer renderer = (Renderer) renderers.get(typeId);

		if (renderer == null)
		{
			return new DefaultQueryRenderer();
		}

		return renderer;
	}

	public void removeRenderer(Renderer renderer)
	{
		renderers.remove(renderer);
	}

	public Controller createDefaultController(String controllerTypeId)
	{
		Controller controller = new Controller();
		long controllerUniqueId = Engine.instance().getPersistentIDGenerator().createId();

		controller.setControllerTypeId(controllerTypeId);
		controller.setUniqueId(controllerUniqueId);

		try
		{
			DataObject sample = (DataObject) Engine.instance().getIObjectFactory().newInstance(controllerTypeId);

			WidgetDescription wdGroup = new WidgetDescription();

			wdGroup.setUniqueId(Engine.instance().getPersistentIDGenerator().createId());
			wdGroup.setControllerUniqueId(controllerUniqueId);
			wdGroup.setLabelId(sample.getTypeId());
			wdGroup.setWidgetId("group");
			wdGroup.setRendererId("group");

			IObjectProxy groupProxy = (IObjectProxy) new FrameworkProxy();

			groupProxy.setSampleRealObject((IObject) wdGroup);
			Engine.instance().getBaseRegistry().add((BaseObject) wdGroup);
			Engine.instance().getProxyRegistry().addProxy(groupProxy, wdGroup.getTypeId());
			controller.addWidgetDescription(wdGroup);

			for (Iterator i = sample.getAttributes().keySet().iterator(); i.hasNext();)
			{
				String key = (String) i.next();
				String objectType = sample.getAttribute(key).getClass().getName();

				WidgetDescription wd = new WidgetDescription();

				wd.setUniqueId(Engine.instance().getPersistentIDGenerator().createId());
				wd.setControllerUniqueId(controllerUniqueId);
				wd.setLabelId(key);
				wd.setWidgetId(key);
				wd.setRendererId(objectType);

				IObjectProxy proxy = (IObjectProxy) new FrameworkProxy();

				proxy.setSampleRealObject((IObject) wd);
				Engine.instance().getBaseRegistry().add((BaseObject) wd);
				Engine.instance().getProxyRegistry().addProxy(proxy, wd.getTypeId());
				wdGroup.addWidgetDescription(wd);
			}
		}
		catch (NoSuchIObjectException x)
		{
			System.out.println("GUIMANAGER - CreateDefaultController error");
			x.printStackTrace();
		}

		return controller;
	}

	public void unload()
	{
		Engine.instance().getEventRegistry().removeListener("proxyisuptodate", this);
		Engine.instance().getEventRegistry().removeListener("User", this);
	}
}

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


import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import de.iritgo.aktario.core.Engine;
import de.iritgo.simplelife.process.Procedure1;


/**
 * Extension of the Swing AbstractAction which adds some convenience methods.
 */
public class IAction extends AbstractAction
{
	/** */
	private static final long serialVersionUID = 1L;

	public IAction()
	{
	}

	public IAction(String name)
	{
		setName(name);
	}

	public IAction(String name, ImageIcon icon)
	{
		setName(name);
		setIcon(icon);
	}

	/**
	 * Set both the small and large icon.
	 *
	 * @param icon The new image icon
	 */
	public void setIcon(ImageIcon icon)
	{
		setSmallIcon(icon);
		setLargeIcon(icon);
	}

	/**
	 * Set the large icon.
	 *
	 * @param icon The new image icon
	 */
	public void setSmallIcon(ImageIcon icon)
	{
		putValue(Action.SMALL_ICON, icon);
	}

	/**
	 * Get the small icon.
	 *
	 * @return The small icon
	 */
	public ImageIcon getSmallIcon()
	{
		return (ImageIcon) getValue(Action.SMALL_ICON);
	}

	/**
	 * Set the small icon.
	 *
	 * @param icon The new image icon
	 */
	public void setLargeIcon(ImageIcon icon)
	{
		putValue(Action.LARGE_ICON_KEY, icon);
	}

	/**
	 * Get the large icon.
	 *
	 * @return The large icon
	 */
	public ImageIcon getLargeIcon()
	{
		return (ImageIcon) getValue(Action.LARGE_ICON_KEY);
	}

	/**
	 * Set the action name. The name is first looked up as a resource key. If no
	 * string resource is found, the name is used literally.
	 *
	 * @param name The new action name
	 */
	public void setName(String name)
	{
		putValue(Action.NAME, Engine.instance().getResourceService().getStringWithoutException(name));
	}

	/**
	 * Get the action name.
	 *
	 * @return The action name
	 */
	public String getName()
	{
		return (String) getValue(Action.NAME);
	}

	public void setToolTipText(String toolTipText)
	{
		putValue(Action.SHORT_DESCRIPTION, Engine.instance().getResourceService()
						.getStringWithoutException(toolTipText));
	}

	/** A list of procedures that are invoked before calling the action handler */
	protected List<Procedure1<IActionEvent>> actionInterceptors = new LinkedList();

	public void addActionInterceptor(Procedure1<IActionEvent> proc)
	{
		actionInterceptors.add(proc);
	}

	public void removeActionInterceptor(Procedure1<IActionEvent> proc)
	{
		actionInterceptors.remove(proc);
	}

	/**
	 * Before the action handler code is actually called, we call each action
	 * interceptor.
	 *
	 * @param e The action event
	 */
	public void actionPerformed(ActionEvent e)
	{
		IActionEvent event = new IActionEvent(e);
		for (Procedure1<IActionEvent> proc : actionInterceptors)
		{
			proc.execute(event);
		}
		action(event);
	}

	/**
	 * Implement your code by overriding this method.
	 *
	 * @param e The action event.
	 */
	public void action(IActionEvent event)
	{
	}

}

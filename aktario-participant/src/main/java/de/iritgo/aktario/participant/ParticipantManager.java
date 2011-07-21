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

package de.iritgo.aktario.participant;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.core.plugin.PluginEventListener;
import de.iritgo.aktario.core.plugin.PluginStateEvent;
import de.iritgo.aktario.framework.dataobject.DynDataObject;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;
import de.iritgo.aktario.participant.gui.RenderNameStateCommand;
import de.iritgo.aktario.participant.gui.RenderNullCommand;
import de.iritgo.aktario.participant.gui.RenderOnlineStateCommand;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * ParticipantManager
 *
 * @version $Id: ParticipantManager.java,v 1.11 2006/09/25 10:34:30 grappendorf Exp $
 */
public class ParticipantManager extends BaseObject implements Manager, PluginEventListener, UserListener
{
	protected DynDataObject participant;

	protected Map attributeRenderCommands;

	protected Map attributeEditorCommands;

	protected Map attributeCommands;

	protected List contentCommands;

	/**
	 * Create manager.
	 */
	public ParticipantManager (String instance)
	{
		super (instance);

		attributeRenderCommands = new HashMap ();
		attributeEditorCommands = new HashMap ();
		attributeCommands = new HashMap ();
		contentCommands = new LinkedList ();

		participant = new DynDataObject ("ParticipantState");
		participant.addAttribute ("onlineUser", false);
		participant.addAttribute ("iritgoUserName", "");
		participant.addAttribute ("aktarioUserName", "");
		participant.addAttribute ("iritgoUserUniqueId", new Long (0));

		addAttributeRenderCommand ("onlineUser", new RenderOnlineStateCommand ());
		addAttributeRenderCommand ("iritgoUserName", new RenderNullCommand ());
		addAttributeRenderCommand ("aktarioUserName", new RenderNameStateCommand ());
		addAttributeRenderCommand ("iritgoUserUniqueId", new RenderNullCommand ());
	}

	/**
	 * Initialize the client manager.
	 */
	public void init ()
	{
		Engine.instance ().getEventRegistry ().addListener ("Plugin", this);
		Engine.instance ().getEventRegistry ().addListener ("User", this);
	}

	/**
	 * Add a attribute to the participant state data object.
	 * Used for other plugins to display more informations on the participant state display.
	 *
	 * @param String The attribute name to add.
	 * @param Object The object type of the attribute
	 */
	public void addAttribute (String attribute, Object attributeType)
	{
		participant.addAttribute (attribute, attributeType);
	}

	/**
	 * Register a command for the renderer in the table.
	 * For every attribute that has a render command it will call the command and the result will
	 * displayed in the participant state window.
	 *
	 * @param String The attribute name
	 * @param Command The command for the attribute.
	 */
	public void addAttributeRenderCommand (String attribute, Command command)
	{
		attributeRenderCommands.put (attribute, command);
	}

	public Command getAttributeRenderCommand (String attribute)
	{
		return (Command) attributeRenderCommands.get (attribute);
	}

	/**
	 * Register a command for the editor cell in the table.
	 * For every attribute that has a editor command it will call the command and the result will
	 * displayed in the participant state window.
	 *
	 * @param String The attribute name
	 * @param Command The command for the attribute.
	 */
	public void addAttributeEditorCommand (String attribute, Command command)
	{
		attributeEditorCommands.put (attribute, command);
	}

	public Command getAttributeEditorCommand (String attribute)
	{
		return (Command) attributeEditorCommands.get (attribute);
	}

	/**
	 * Add a command that called by a click on a cell.
	 *
	 * @param String The attribute name
	 * @param Command The command for the attribute.
	 */
	public void addAttributeCommand (String attribute, Command command)
	{
		attributeCommands.put (attribute, command);
	}

	public Command getAttributeCommand (String attribute)
	{
		return (Command) attributeCommands.get (attribute);
	}

	public void addContentCommand (Command command)
	{
		contentCommands.add (command);
	}

	public Iterator contentCommandIterator ()
	{
		return contentCommands.iterator ();
	}

	public void pluginEvent (PluginStateEvent event)
	{
	}

	public void userEvent (UserEvent event)
	{
		if (event.isLoggedIn ())
		{
			User user = event.getUser ();
			long userUniqueId = user.getUniqueId ();
		}

		if (event.isLoggedOut ())
		{
			User user = event.getUser ();
			long userUniqueId = user.getUniqueId ();
		}
	}

	public DynDataObject getByName (String name)
	{
		for (Iterator i = Engine.instance ().getBaseRegistry ().iterator ("ParticipantState"); i.hasNext ();)
		{
			DynDataObject participantState = (DynDataObject) i.next ();

			if (participantState.getStringAttribute ("iritgoUserName").equals (name))
			{
				return participantState;
			}
		}

		return null;
	}

	/**
	 * Free all client manager resources.
	 */
	public void unload ()
	{
	}
}

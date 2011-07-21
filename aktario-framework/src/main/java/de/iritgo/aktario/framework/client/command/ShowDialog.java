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
import de.iritgo.aktario.core.gui.IDialog;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.sessioncontext.SessionContext;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.client.Client;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;


/**
 *
 */
public class ShowDialog extends Command
{
	private String guiPaneId;

	private String desktopName;

	private IObject prototypeable;

	private SessionContext sessionContext;

	private String onScreenUniqueId;

	/**
	 * Create a new ShowDialog command.
	 *
	 * @param guiPaneId The GUIPane to show.
	 */
	public ShowDialog (String guiPaneId)
	{
		init (guiPaneId, guiPaneId, null, null);
	}

	/**
	 * Standard constructor
	 *
	 * @deprecated
	 */
	public ShowDialog (String guiPaneId, long unqiueId)
	{
		this (guiPaneId, guiPaneId, unqiueId, null);
	}

	/**
	 * Create a new ShowDialog command.
	 *
	 * @param guiPaneId The GUIPane to show.
	 * @param prototypeable A data object prototype.
	 */
	public ShowDialog (String guiPaneId, IObject prototypeable)
	{
		init (guiPaneId, guiPaneId, prototypeable, null);
	}

	/**
	 * Create a new ShowDialog command.
	 *
	 * @param guiPaneId The GUIPane to show.
	 * @param unqiueId The id of the data object
	 */
	public ShowDialog (String guiPaneId, String onScreenUniqueId, long unqiueId, String typeId)
	{
		GUIPane guiPane = GUIPaneRegistry.instance ().create (guiPaneId);

		IObject prototypeable = (IObject) Engine.instance ().getBaseRegistry ().get (unqiueId, typeId);

		if (prototypeable == null)
		{
			try
			{
				prototypeable = Engine.instance ().getIObjectFactory ().newInstance (typeId);
			}
			catch (NoSuchIObjectException e)
			{
				return;
			}

			prototypeable.setUniqueId (unqiueId);
			Engine.instance ().getBaseRegistry ().add ((BaseObject) prototypeable);

			FrameworkProxy appProxy = new FrameworkProxy (prototypeable);

			appProxy.setSampleRealObject ((IObject) prototypeable);
			Engine.instance ().getProxyRegistry ().addProxy (appProxy, prototypeable.getTypeId ());
		}

		// TODO: Release after init?
		init (guiPaneId, onScreenUniqueId, prototypeable, null);
	}

	/**
	 * Create a new ShowDialog command.
	 *
	 * @param guiPaneId The GUIPane to show.
	 * @param sessionContext The session context.
	 */
	public ShowDialog (String guiPaneId, SessionContext sessionContext)
	{
		init (guiPaneId, guiPaneId, null, sessionContext);
	}

	/**
	 * Create a new ShowDialog command.
	 *
	 * @param guiPaneId The GUIPane to show.
	 * @param prototypeable A data object prototype.
	 * @param sessionContext The session context.
	 */
	public ShowDialog (String guiPaneId, IObject prototypeable, SessionContext sessionContext)
	{
		init (guiPaneId, guiPaneId, prototypeable, sessionContext);
	}

	/**
	 * Display the IWindow-Pane.
	 */
	public void perform ()
	{
		//		try
		//		{
		//			SwingUtilities.invokeAndWait (new Runnable ()
		//			{
		//				public void run ()
		//				{
		final IDialog dialog = new IDialog (guiPaneId, onScreenUniqueId);

		dialog.setProperties (properties);

		if (prototypeable == null)
		{
			dialog.initGUI (guiPaneId, guiPaneId, sessionContext);
		}
		else
		{
			dialog.initGUI (guiPaneId, onScreenUniqueId, prototypeable, sessionContext);
		}

		Client.instance ().getClientGUI ().getDesktopManager ().addDisplay (dialog);

		dialog.show ();

		//				}
		//			});
		//		}
		//		catch (InterruptedException x)
		//		{
		//		}
		//		catch (InvocationTargetException x)
		//		{
		//		}
	}

	public boolean canPerform ()
	{
		return true;
	}

	private void init (String guiPaneId, String onScreenUniqueId, IObject prototypeable, SessionContext sessionContext)
	{
		this.guiPaneId = guiPaneId;
		this.onScreenUniqueId = onScreenUniqueId;
		this.prototypeable = prototypeable;
		this.sessionContext = sessionContext;
	}
}

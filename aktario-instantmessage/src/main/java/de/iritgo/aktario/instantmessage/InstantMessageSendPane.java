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

package de.iritgo.aktario.instantmessage;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.*;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.framework.appcontext.*;
import de.iritgo.aktario.framework.command.*;
import de.iritgo.aktario.framework.dataobject.*;
import de.iritgo.aktario.framework.user.*;
import org.swixml.SwingEngine;
import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.event.*;
import java.util.*;


/**
 *
 */
public class InstantMessageSendPane extends SwingGUIPane
{
	public JTextArea message;

	/**
	 * Send a message to a participant.
	 */
	public Action send = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			Properties props = new Properties ();

			props.setProperty ("sourceUser", AppContext.instance ().getUser ().getName ());
			props.setProperty ("targetUser", properties.getProperty ("targetUser"));
			props.setProperty ("message", message.getText ());
			CommandTools.performAsync ("InstantMessageCommand", props);
			display.close ();
		}
	};

	/**
	 * Cancel
	 */
	public Action cancel = new AbstractAction ()
	{
		public void actionPerformed (ActionEvent e)
		{
			display.close ();
		}
	};

	/**
	 * Create a new SipPhonePane.
	 */
	public InstantMessageSendPane ()
	{
		super ("InstantMessageSendPane");
	}

	/**
	 * Initialize the gui.
	 */
	public void initGUI ()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine (this);

			swingEngine.setClassLoader (InstantMessagePlugin.class.getClassLoader ());

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource ("/swixml/InstantMessageSendPane.xml"));

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
			Log.logError ("client", "InstantMessageSendPane.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject (IObject iobject)
	{
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	public void storeToObject (IObject iobject)
	{
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new InstantMessageSendPane ();
	}
}

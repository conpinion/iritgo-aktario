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

package de.iritgo.aktario.client.gui;


import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import org.swixml.SwingEngine;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;


/**
 * MesangerPane.
 *
 * @version $Id: MessangerPane.java,v 1.7 2006/09/25 10:34:31 grappendorf Exp $
 */
public class MessangerPane extends SwingGUIPane
{
	/**
	 * Create a new MessangerPane.
	 */
	public MessangerPane ()
	{
		super ("MessangerPane");
	}

	/**
	 * Initialize the gui.
	 */
	@Override
	public void initGUI ()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine (this);

			swingEngine.setClassLoader (MessangerPane.class.getClassLoader ());

			JPanel panel = (JPanel) swingEngine.render (getClass ().getResource ("/swixml/MessangerPane.xml"));

			content.add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
			Log.logError ("client", "MessangerPane.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	@Override
	public void loadFromObject (IObject iobject)
	{
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	@Override
	public void storeToObject (IObject iobject)
	{
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane ()
	{
		return new MessangerPane ();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return null;
	}
}

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


import de.iritgo.aktario.client.AktarioClientPlugin;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.simplelife.string.StringTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;


/**
 * AboutGUIPane
 *
 * @version $Id: AboutPane.java,v 1.11 2006/09/25 10:34:31 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class AboutPane extends SwingGUIPane
{
	/** The dialog background. */
	public JLabel background;

	/** The application title. */
	public JLabel title;

	/** The application version. */
	public JLabel version;

	/** The application copyright. */
	public JLabel copyright;

	/**
	 * Close the dialog.
	 */
	public Action closeAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			display.close();
		}
	};

	/**
	 * Create a new AboutGUIPane.
	 */
	public AboutPane()
	{
		super("AboutGUIPane");
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	@Override
	public void initGUI()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader(AktarioClientPlugin.class.getClassLoader());

			JPanel panel = (JPanel) swingEngine.render(getClass().getResource("/swixml/AboutPane.xml"));

			content.add(panel, createConstraints(0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			AktarioGUI gui = (AktarioGUI) Client.instance().getClientGUI();

			background.setIcon(gui.getAboutBackground());

			setTitle(Engine.instance().getResourceService().getString("app.about"));

			if (! StringTools.isTrimEmpty(System.getProperty("iritgo.app.title")))
			{
				title.setText(System.getProperty("iritgo.app.title"));
			}

			if (! StringTools.isTrimEmpty(System.getProperty("iritgo.app.version.long")))
			{
				version.setText(System.getProperty("iritgo.app.version.long"));
			}

			if (! StringTools.isTrimEmpty(System.getProperty("iritgo.app.copyright")))
			{
				copyright.setText(System.getProperty("iritgo.app.copyright"));
			}
		}
		catch (Exception x)
		{
			Log.logError("client", "AboutGUIPane.initGUI", x.toString());
		}
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane()
	{
		return new AboutPane();
	}

	@Override
	public void loadFromObject(IObject iobject)
	{
		/* empty */
	}

	@Override
	public void storeToObject(IObject iobject)
	{
		/* empty */
	}
}

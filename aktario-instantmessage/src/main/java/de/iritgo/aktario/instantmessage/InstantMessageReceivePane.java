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
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.util.*;


/**
 *
 */
public class InstantMessageReceivePane extends SwingGUIPane
{
	/** The message pane. */
	public JTextPane message;

	/**
	 * Send a message to a participant.
	 */
	public Action ok = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			display.close();
		}
	};

	/**
	 * Create a new SipPhonePane.
	 */
	public InstantMessageReceivePane()
	{
		super("InstantMessageReceivePane");
	}

	/**
	 * Initialize the gui.
	 */
	public void initGUI()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader(InstantMessagePlugin.class.getClassLoader());

			JPanel panel = (JPanel) swingEngine.render(getClass().getResource("/swixml/InstantMessageReceivePane.xml"));

			content.add(panel, createConstraints(0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			StyledDocument doc = message.getStyledDocument();

			Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

			Style regular = doc.addStyle("regular", def);

			StyleConstants.setFontFamily(regular, "SansSerif");

			Style style = doc.addStyle("italic", regular);

			StyleConstants.setItalic(style, true);

			style = doc.addStyle("bold-green", regular);
			StyleConstants.setBold(style, true);
			StyleConstants.setForeground(style, new Color(0, 100, 0));

			style = doc.addStyle("bold-orange", regular);
			StyleConstants.setBold(style, true);
			StyleConstants.setForeground(style, new Color(171, 103, 0));

			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

			doc.insertString(0, "\n" + properties.getProperty("message") + "\n", doc.getStyle("regular"));

			doc.insertString(0, dateFormat.format(new Date(((Long) properties.get("timestamp")).longValue())) + "\n",
							doc.getStyle("bold-orange"));

			ResourceService rs = Engine.instance().getResourceService();

			doc.insertString(0, rs.getStringWithoutException("instantMessage.at") + " ", doc.getStyle("italic"));

			doc.insertString(0, properties.getProperty("sourceUser") + "\n", doc.getStyle("bold-green"));

			doc.insertString(0, rs.getStringWithoutException("instantMessage.from") + " ", doc.getStyle("italic"));
		}
		catch (Exception x)
		{
			Log.logError("client", "InstantMessageReceivePane.initGUI", x.toString());
			x.printStackTrace();
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject(IObject iobject)
	{
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	public void storeToObject(IObject iobject)
	{
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane()
	{
		return new InstantMessageReceivePane();
	}
}

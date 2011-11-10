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
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.*;
import java.text.DateFormat;
import java.util.*;


/**
 *
 */
public class ArchivInstantMessagePane extends SwingGUIPane implements InstantMessageListener
{
	/** The messages pane. */
	public JTextPane message;

	/** Used to format message timestamps. */
	protected DateFormat dateFormat;

	/** Message count. */
	protected int messageCount;

	/**
	 * Create a new SipPhonePane.
	 */
	public ArchivInstantMessagePane()
	{
		super("ArchivInstantMessagePane");
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

			JPanel panel = (JPanel) swingEngine.render(getClass().getResource("/swixml/ArchivInstantMessagePane.xml"));

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

			style = doc.addStyle("bold-red", regular);
			StyleConstants.setBold(style, true);
			StyleConstants.setForeground(style, new Color(100, 0, 0));

			style = doc.addStyle("bold-orange", regular);
			StyleConstants.setBold(style, true);
			StyleConstants.setForeground(style, new Color(171, 103, 0));

			style = doc.addStyle("message-in", regular);
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
			StyleConstants.setIcon(style, new ImageIcon(getClass().getResource("/resources/message-in.png")));

			style = doc.addStyle("message-out", regular);
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
			StyleConstants.setIcon(style, new ImageIcon(getClass().getResource("/resources/message-out.png")));

			style = doc.addStyle("indent", regular);
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
			StyleConstants.setIcon(style, new ImageIcon(getClass().getResource("/resources/empty-16.png")));

			dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		}
		catch (Exception x)
		{
			Log.logError("client", "ArchivInstantMessagePane.initGUI", x.toString());
		}

		Engine.instance().getEventRegistry().addListener("InstantMessage", this);
	}

	public void instantMessageEvent(InstantMessageEvent event)
	{
		try
		{
			StyledDocument doc = message.getStyledDocument();

			Style style = doc.addStyle("separator" + messageCount, StyleContext.getDefaultStyleContext().getStyle(
							StyleContext.DEFAULT_STYLE));

			StyleConstants.setComponent(style, new JSeparator());

			doc.insertString(0, "\n", doc.getStyle("regular"));

			doc.insertString(0, " ", doc.getStyle("separator" + messageCount));

			doc.insertString(0, "\n" + event.getMessage() + "\n", doc.getStyle("regular"));

			doc.insertString(0, dateFormat.format(new Date(event.getTimestamp())) + "\n", doc.getStyle("bold-orange"));

			ResourceService rs = Engine.instance().getResourceService();

			doc.insertString(0, " " + rs.getStringWithoutException("instantMessage.at") + " ", doc.getStyle("italic"));

			doc.insertString(0, " ", doc.getStyle("indent"));

			if (event.isIncoming())
			{
				doc.insertString(0, event.getSourceUser() + "\n", doc.getStyle("bold-green"));
				doc.insertString(0, " " + rs.getStringWithoutException("instantMessage.from") + " ", doc
								.getStyle("italic"));
				doc.insertString(0, " ", doc.getStyle("message-in"));
			}
			else
			{
				doc.insertString(0, event.getTargetUser() + "\n", doc.getStyle("bold-red"));
				doc.insertString(0, " " + rs.getStringWithoutException("instantMessage.to") + " ", doc
								.getStyle("italic"));
				doc.insertString(0, " ", doc.getStyle("message-out"));
			}

			++messageCount;
		}
		catch (BadLocationException x)
		{
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
		return new ArchivInstantMessagePane();
	}
}

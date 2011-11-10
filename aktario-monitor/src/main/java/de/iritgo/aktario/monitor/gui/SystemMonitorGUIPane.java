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

package de.iritgo.aktario.monitor.gui;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.IMenuItem;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.monitor.*;
import org.swixml.SwingEngine;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.*;
import java.sql.Timestamp;


/**
 * This gui pane is used to display system monitor pane.
 */
public class SystemMonitorGUIPane extends SwingGUIPane
{
	public JLabel registeredUsers;

	public JLabel onlineUsers;

	public JLabel workingThreads;

	public JLabel freeThreads;

	public JLabel freeRam;

	public JLabel clientFreeRam;

	/**
	 * Close the dialog.
	 */
	public Action okAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			display.close();
		}
	};

	/**
	 * Refresh the system monitor.
	 */
	public Action update = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			Engine.instance().getProxyRegistry().getProxy(getIObject().getUniqueId(), getIObject().getTypeId()).reset();
		}
	};

	/**
	 * Create a new SystemMonitor.
	 */
	public SystemMonitorGUIPane()
	{
		super("SystemMonitor");
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI()
	{
		try
		{
			JPanel panel = (JPanel) new SwingEngine(this).render(getClass().getResource("/swixml/SystemMonitor.xml"));

			content.add(panel, createConstraints(0, 0, 1, 2, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
			Log.logError("client", "SystemMonitor.initGUI", x.toString());
		}
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane()
	{
		return new SystemMonitorGUIPane();
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject(IObject iObject)
	{
		SystemMonitor systemMonitor = (SystemMonitor) iObject;

		registeredUsers.setText("" + systemMonitor.getRegisteredUsers());
		onlineUsers.setText("" + systemMonitor.getOnlineUsers());
		workingThreads.setText("" + systemMonitor.getWorkingThreads());
		freeThreads.setText("" + systemMonitor.getFreeThreads());
		freeRam.setText("" + systemMonitor.getFreeRam());
		clientFreeRam.setText("" + Runtime.getRuntime().freeMemory());
	}

	public void storeToObject(IObject iObject)
	{
		/* empty */
	}
}

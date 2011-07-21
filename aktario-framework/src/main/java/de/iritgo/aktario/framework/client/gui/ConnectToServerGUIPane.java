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

package de.iritgo.aktario.framework.client.gui;


import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.ILabel;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.network.ConnectObserver;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;


/**
 *
 */
public class ConnectToServerGUIPane extends SwingGUIPane implements ConnectObserver
{
	private JTextField email;

	private JTextField nickName;

	private int state;

	JProgressBar progress;

	/**
	 * Standard constructor
	 *
	 * @param guiPaneId The guiPaneId
	 */
	public ConnectToServerGUIPane (String guiPaneId)
	{
		super (guiPaneId);
	}

	/**
	 * Init GUI
	 */
	public void initGUI ()
	{
		JPanel allPanel = new JPanel ();

		allPanel.setLayout (new GridBagLayout ());

		int row = 0;

		ILabel connectTotServer = new ILabel (getTypeId ());

		allPanel.add (connectTotServer, createConstraints (0, row++, 1, 1, GridBagConstraints.HORIZONTAL, 100, 0,
						new Insets (5, 15, 5, 15)));

		progress = new JProgressBar (0, 100);
		allPanel.add (progress, createConstraints (0, row++, 1, 1, GridBagConstraints.HORIZONTAL, 100, 0, new Insets (
						5, 15, 5, 15)));

		content.add (allPanel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

		state = 0;

		getDisplay ().putProperty ("weightx", new Double (2.0));
	}

	/**
	 * LoadFormObject, load the Data form Object.
	 */
	public void loadFromObject (IObject iObject)
	{
	}

	/**
	 * StoreFormObject, store the data from the gui components.
	 */
	public void storeToObject (IObject iObject)
	{
	}

	/**
	 * Close it.
	 */
	public void close ()
	{
		super.close ();
	}

	public void notice ()
	{
		progress.setValue (state++);

		if (state == 100)
		{
			state = 0;
		}
	}

	/**
	 * Cancel
	 */
	public void onCancel (ActionEvent event)
	{
		display.close ();
	}

	/**
	 * Return a new instance.
	 */
	public GUIPane cloneGUIPane ()
	{
		ConnectToServerGUIPane pane = new ConnectToServerGUIPane (getTypeId ());

		return pane;
	}
}

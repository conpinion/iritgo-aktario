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

package de.iritgo.aktario.buddylist.gui;


import com.l2fprod.common.swing.JTaskPane;
import de.iritgo.aktario.buddylist.BuddyList;
import de.iritgo.aktario.buddylist.BuddyListGroup;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.IButton;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.util.Iterator;


/**
 * This gui pane displays a list of all users and lets the administrator
 * add, edit, and delete users.
 *
 * @version $Id$
 */
public class BuddyListPane extends SwingGUIPane
{
	/** The table of all users. */
	public JTable participantStateTable;

	/** The edit button. */
	public IButton btnEdit;

	/** The delete button. */
	public IButton btnDelete;

	/** ScrollPane containing the participant state table. */
	public JScrollPane participantStateScrollPane;

	private JTaskPane taskPane;

	/**
	 * Create a new UserListGUIPane.
	 */
	public BuddyListPane()
	{
		super("BuddyListPane");
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
			taskPane = new JTaskPane();

			JScrollPane scrollPane = new JScrollPane(taskPane);

			taskPane.removeAll();

			content.add(scrollPane, createConstraints(0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
			Log.logError("client", "ParticipantStatePane.initGUI", x.toString());
			x.printStackTrace();
		}
	}

	/**
	 * Load the data object into the gui.
	 */
	@Override
	public void loadFromObject(IObject iobject)
	{
		BuddyList buddyList = (BuddyList) iobject;

		taskPane.removeAll();

		for (Iterator i = buddyList.buddyListGroupIterator(); i.hasNext();)
		{
			taskPane.add(new TaskGroup((BuddyListGroup) i.next()));
		}
	}

	/**
	 * Store the gui values to the data object.
	 */
	@Override
	public void storeToObject(IObject iobject)
	{
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane()
	{
		return new BuddyListPane();
	}
}

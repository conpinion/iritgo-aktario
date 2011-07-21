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

package de.iritgo.aktario.core.gui;


import de.iritgo.aktario.core.iobject.IObject;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * This is a Swing implementation of the GUIPane.
 */
public abstract class SwingGUIPane extends GUIPane
{
	/** The content panel. */
	protected JPanel content;

	/**
	 * Create a new SwingGUIPane.
	 *
	 * @param guiPaneId The id of the new gui pane.
	 */
	public SwingGUIPane (String guiPaneId)
	{
		super (guiPaneId, guiPaneId);
	}

	/**
	 * Set the content pane.
	 *
	 * @param content The new content pane.
	 */
	public void setContentPane (Object content)
	{
		this.content = (JPanel) content;
	}

	/**
	 * Implementation specific tasks for setting the display.
	 *
	 * @param display The new display.
	 */
	public void setIDisplayImpl (IDisplay display)
	{
		if (display instanceof IWindow)
		{
			setContentPane (((SwingWindowFrame) (((IWindow) display).getWindowFrame ())).getContentPanel ());
		}
		else if (display instanceof IDialog)
		{
			setContentPane (((SwingDialogFrame) (((IDialog) display).getDialogFrame ())).getContentPanel ());
		}
	}

	/**
	 * This method is called when the attributes of the gui pane's iobject are
	 * received.
	 */
	@Override
	public void waitingForNewObjectFinished ()
	{
		try
		{
			SwingUtilities.invokeAndWait (new Runnable ()
			{
				public void run ()
				{
					IObject iObject = getIObject ();

					if (iObject.isValid ())
					{
						loadFromObject (iObject);
					}
				}
			});
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Helper method for creating gridbag constraints.
	 *
	 * @param x The grid column.
	 * @param y The grid row.
	 * @param width The number of occupied columns.
	 * @param height The number of occupied rows.
	 * @param fill The fill method.
	 * @param wx The horizontal stretch factor.
	 * @param wy The vertical stretch factor.
	 * @param insets The cell insets.
	 * @return The gridbag constraints.
	 */
	protected GridBagConstraints createConstraints (int x, int y, int width, int height, int fill, int wx, int wy,
					Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints ();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		if (insets == null)
		{
			gbc.insets = new Insets (0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}

	/**
	 * Helper method for creating gridbag constraints.
	 *
	 * @param x The grid column.
	 * @param y The grid row.
	 * @param width The number of occupied columns.
	 * @param height The number of occupied rows.
	 * @param fill The fill method.
	 * @param anchor The anchor.
	 * @param wx The horizontal stretch factor.
	 * @param wy The vertical stretch factor.
	 * @param insets The cell insets.
	 * @return The gridbag constraints.
	 */
	protected GridBagConstraints createConstraints (int x, int y, int width, int height, double wx, double wy,
					int fill, int anchor, Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints ();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = anchor;

		if (insets == null)
		{
			gbc.insets = new Insets (0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}

	/**
	 * Set the IDisplay icon.
	 *
	 * @param icon The new icon.
	 */
	public void setIcon (Icon icon)
	{
		if (display instanceof IWindow)
		{
			((IWindow) display).setIcon (icon);
		}
		else if (display instanceof IDialog)
		{
			((IDialog) display).setIcon (icon);
		}
	}

	/**
	 * Get the display's icon.
	 *
	 * @return The display's icon.
	 */
	public Icon getIcon ()
	{
		if (display instanceof IWindow)
		{
			return ((IWindow) display).getIcon ();
		}
		else if (display instanceof IDialog)
		{
			return ((IDialog) display).getIcon ();
		}

		return null;
	}

	/**
	 * Get the content panel.
	 *
	 * @return The content panel.
	 */
	public JPanel getPanel ()
	{
		return content;
	}

	/**
	 * Get the content panel.
	 *
	 * @return The content panel.
	 */
	@Override
	public JPanel getContentPane ()
	{
		return content;
	}

	/**
	 * Load the gui values from the data object attributes.
	 * @param iObject The iobject.
	 */
	@Override
	public abstract void loadFromObject (IObject iObject);

	/**
	 * Store the current gui values into the data object attributes.
	 *
	 * @param iObject The iobject.
	 */
	@Override
	public abstract void storeToObject (IObject iObject);
}

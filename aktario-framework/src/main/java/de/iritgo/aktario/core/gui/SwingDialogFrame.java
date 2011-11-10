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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.framework.client.Client;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 *
 */
public class SwingDialogFrame extends JDialog implements WindowListener, IDialogFrame
{
	/** */
	private static final long serialVersionUID = 1L;

	/** Our dialog. */
	private IDialog dialog;

	/** The glass pane. */
	IGlassPane glassPane;

	/**
	 * Create a new SwingDialogFrame.
	 *
	 * @param dialog The IDialog to which this frame belongs.
	 * @param titleKey The dialog title specified as a resource key.
	 */
	public SwingDialogFrame(IDialog dialog, String titleKey, IDialogFrame owner)
	{
		super((JDialog) owner, Engine.instance().getResourceService().getStringWithoutException(titleKey), true);

		init(dialog);
	}

	/**
	 * Create a new SwingDialogFrame.
	 *
	 * @param dialog The IDialog to which this frame belongs.
	 * @param titleKey The dialog title specified as a resource key.
	 */
	public SwingDialogFrame(IDialog dialog, String titleKey)
	{
		super(((SwingDesktopManager) Client.instance().getClientGUI().getDesktopManager()).getJFrame(), Engine
						.instance().getResourceService().getStringWithoutException(titleKey), false);

		init(dialog);
	}

	/**
	 * Initialize the dialog frame.
	 */
	protected void init(IDialog dialog)
	{
		this.dialog = dialog;

		glassPane = new IGlassPane();

		addWindowListener(this);

		getContentPanel().setLayout(new GridBagLayout());
	}

	/**
	 * Return the content panel.
	 *
	 * @return The content panel.
	 */
	public JPanel getContentPanel()
	{
		return (JPanel) getContentPane();
	}

	/**
	 * Invoked when the Window is set to be the active Window.
	 *
	 * @param event The window event.
	 */
	public void windowActivated(WindowEvent event)
	{
	}

	/**
	 * Invoked when a window has been closed as the result of calling dispose on the window.
	 *
	 * @param event The window event.
	 */
	public void windowClosed(WindowEvent event)
	{
	}

	/**
	 * Invoked when the user attempts to close the window from the window's system menu.
	 *
	 * @param event The window event.
	 */
	public void windowClosing(WindowEvent event)
	{
		dialog.close();
	}

	/**
	 * Invoked when a Window is no longer the active Window.
	 *
	 * @param event The window event.
	 */
	public void windowDeactivated(WindowEvent event)
	{
	}

	/**
	 * Invoked when a window is changed from a minimized to a normal state.
	 *
	 * @param event The window event.
	 */
	public void windowDeiconified(WindowEvent event)
	{
	}

	/**
	 * Invoked when a window is changed from a normal to a minimized state.
	 *
	 * @param event The window event.
	 */
	public void windowIconified(WindowEvent event)
	{
	}

	/**
	 * Invoked the first time a window is made visible.
	 *
	 * @param event The window event.
	 */
	public void windowOpened(WindowEvent event)
	{
	}

	/**
	 * Close the dialog frame.
	 */
	public void close()
	{
		Client.instance().getClientGUI().getDesktopManager().getDesktopFrame().setEnabled(true);
		setVisible(false);
		dispose();
	}

	/**
	 * Close the dialog frame.
	 */
	public void systemClose()
	{
		setVisible(false);
		dispose();
	}

	/**
	 * Show the dialog frame.
	 */
	public void showDialog()
	{
		pack();

		if (dialog.getProperty("weightx") != null)
		{
			Rectangle bounds = getBounds();

			bounds.width *= ((Double) dialog.getProperty("weightx")).doubleValue();
			setBounds(bounds);
		}

		if (dialog.getProperty("weighty") != null)
		{
			Rectangle bounds = getBounds();

			bounds.height *= ((Double) dialog.getProperty("weighty")).doubleValue();
			setBounds(bounds);
		}

		setLocationRelativeTo(((SwingDesktopManager) Client.instance().getClientGUI().getDesktopManager()).getJFrame());

		Client.instance().getClientGUI().getDesktopManager().getDesktopFrame().setEnabled(false);
		setVisible(true);

		//		setGlassPane (glassPane);
		//		glassPane.setVisible (true);
	}

	/**
	 * Set the dialog title. This title will be displayed on the
	 * dialog frame's title bar.
	 *
	 * @param title The new title.
	 */
	@Override
	public void setTitle(String title)
	{
		super.setTitle(title);
	}

	/**
	 * Set the dialog icon. This icon will be displayed on the
	 * dialog frame's title bar.
	 *
	 * @param icon The new icon.
	 */
	public void setIcon(Icon icon)
	{
	}

	/**
	 * Get the dialog's icon.
	 *
	 * @return The dialog's icon.
	 */
	public Icon getIcon()
	{
		return null;
	}

	/**
	 * Enable/disable the dialog frame.
	 *
	 * @param enabled If true the dialog frame is enabled.
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		glassPane.setEnabled(enabled);
	}

	/**
	 * Check wether the display is enabled or not.
	 *
	 * @return True if the display is enabled.
	 */
	@Override
	public boolean isEnabled()
	{
		return glassPane.isEnabled();
	}
}

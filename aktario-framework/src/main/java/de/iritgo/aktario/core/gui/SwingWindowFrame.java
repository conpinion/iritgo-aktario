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
import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Properties;


/**
 *
 */
public class SwingWindowFrame extends JInternalFrame implements InternalFrameListener, IWindowFrame
{
	/** */
	private static final long serialVersionUID = 1L;

	/** The desktop pane on which the window is displayed. */
	private JDesktopPane desktopPane;

	/** Our window. */
	private IWindow window;

	/** The window properties. */
	@SuppressWarnings("unused")
	private Properties properties;

	/** Is the window by the first view visible. */
	private boolean initVisible;

	/** Is the window by the first view visible. */
	private boolean firstView;

	/**
	 * Create a new SwingWindowFrame.
	 *
	 * @param window The IWindow to which this frame belongs.
	 * @param titleKey The window title specified as a resource key.
	 * @param resizable True if the window should be resizable.
	 * @param closable True if the window should be closable.
	 * @param maximizable True if the window should be maximizable.
	 * @param iconifiable True if the window should be iconifiable.
	 * @param titlebar True if the title bar should be displayed.
	 */
	public SwingWindowFrame(IWindow window, String titleKey, boolean resizable, boolean closable, boolean maximizable,
					boolean iconifiable, boolean titlebar, boolean initVisible)
	{
		super(titlebar ? Engine.instance().getResourceService().getStringWithoutException(titleKey)
						: "$$$ IRITGO-HIDE $$$", resizable, closable, maximizable, iconifiable);

		this.initVisible = initVisible;
		firstView = true;

		addInternalFrameListener(this);

		this.window = window;

		this.desktopPane = ((SwingDesktopManager) window.getDesktopManager()).getDesktopPane(window.getDesktopId());

		getContentPanel().setLayout(new GridBagLayout());

		setBounds(getUserBounds());

		// setGlassPane (new IGlassPane());
		desktopPane.add(this);
	}

	/**
	 * Return the ContentPabel.
	 *
	 * @return Return the ContentPanel
	 */
	public JPanel getContentPanel()
	{
		return (JPanel) getContentPane();
	}

	/**
	 * Set the window proprties
	 *
	 * @param properties The properties
	 */
	public void setProperties(Properties properties)
	{
		this.properties = properties;

		if (properties.get("client") != null)
		{
			for (Map.Entry clientProp : ((Properties) properties.get("client")).entrySet())
			{
				putClientProperty(clientProp.getKey(), clientProp.getValue());
			}
		}
	}

	/**
	 * Invoked when the user attempts to close the window from the window's system menu.
	 *
	 * @param e The window event.
	 */
	public void internalFrameClosing(InternalFrameEvent e)
	{
		window.systemClose();
	}

	/**
	 * Invoked when a window has been closed as the result of calling dispose on the window.
	 *
	 * @param e The window event.
	 */
	public void internalFrameClosed(InternalFrameEvent e)
	{
	}

	/**
	 * Invoked the first time a window is made visible.
	 *
	 * @param e The window event.
	 */
	public void internalFrameOpened(InternalFrameEvent e)
	{
	}

	/**
	 * Invoked when a window is changed from a normal to a minimized state.
	 *
	 * @param e The window event.
	 */
	public void internalFrameIconified(InternalFrameEvent e)
	{
	}

	/**
	 * Invoked when a window is changed from a minimized to a normal state.
	 *
	 * @param e The window event.
	 */
	public void internalFrameDeiconified(InternalFrameEvent e)
	{
	}

	/**
	 * Invoked when the Window is set to be the active Window.
	 *
	 * @param e The window event.
	 */
	public void internalFrameActivated(InternalFrameEvent e)
	{
		//		((IGlassPane) getGlassPane ()).setVisible (true);
		saveBounds();

		if (window.getDesktopManager() != null)
		{
			window.getDesktopManager().setActiveDisplay(window);
		}
	}

	/**
	 * Invoked when a Window is no longer the active Window.
	 *
	 * @param e The window event.
	 */
	public void internalFrameDeactivated(InternalFrameEvent e)
	{
		saveBounds();
	}

	/**
	 * Controlls the Minimize and Close functions
	 */
	public void actionPerformed(@SuppressWarnings("unused") ActionEvent event)
	{
	}

	/**
	 * Generates a constraints object.
	 */
	protected GridBagConstraints getConstraints(int x, int y, int width, int height, int anchor, int fill, int wx,
					int wy)
	{
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = anchor;

		return gbc;
	}

	/**
	 * Return the bounds of this IWindow
	 *
	 * @return Return the ContentPanel
	 */
	protected Rectangle getUserBounds()
	{
		Engine engine = Engine.instance();
		long iObjectUniqueId = window.getDataObject() != null ? window.getDataObject().getUniqueId() : 0;

		int x = engine.getSystemProperties().getInt(window.getTypeId() + iObjectUniqueId + ".x", 300);
		int y = engine.getSystemProperties().getInt(window.getTypeId() + iObjectUniqueId + ".y", 300);
		int xw = engine.getSystemProperties().getInt(window.getTypeId() + iObjectUniqueId + ".xw", 300);
		int yw = engine.getSystemProperties().getInt(window.getTypeId() + iObjectUniqueId + ".yw", 300);

		return new Rectangle(x, y, xw, yw);
	}

	/**
	 * Check wether the user has maximized the window.
	 *
	 * @return True for a maxmimized window.
	 */
	public boolean isUserMaximized()
	{
		Engine engine = Engine.instance();
		long iObjectUniqueId = window.getDataObject() != null ? window.getDataObject().getUniqueId() : 0;

		return engine.getSystemProperties().getInt(window.getTypeId() + iObjectUniqueId + ".maximized", 0) != 0;
	}

	/**
	 * Get the window bounds.
	 *
	 * @return The window bounds.
	 */
	public Rectangle getWindowBounds()
	{
		return getBounds();
	}

	/**
	 * Save the window bounds into the system properties.
	 */
	private void saveBounds()
	{
		if (getContentPanel().isVisible())
		{
			Engine engine = Engine.instance();

			long iObjectUniqueId = window.getDataObject() != null ? window.getDataObject().getUniqueId() : 0;

			engine.getSystemProperties().put(window.getTypeId() + iObjectUniqueId + ".x", getBounds().x);
			engine.getSystemProperties().put(window.getTypeId() + iObjectUniqueId + ".y", getBounds().y);
			engine.getSystemProperties().put(window.getTypeId() + iObjectUniqueId + ".xw", getWidth());
			engine.getSystemProperties().put(window.getTypeId() + iObjectUniqueId + ".yw", getHeight());
			engine.getSystemProperties().put(window.getTypeId() + iObjectUniqueId + ".maximized", isMaximum() ? 1 : 0);
		}
	}

	/**
	 * Close the window frame.
	 */
	public void close()
	{
		saveBounds();
		setVisible(false);
		dispose();
		desktopPane.remove(this);
		window = null;
	}

	/**
	 * Close the window frame.
	 */
	public void systemClose()
	{
		saveBounds();
		setVisible(false);
		dispose();
		desktopPane.remove(this);
		window = null;
	}

	/**
	 * Set the window title. This title will be displayed on the
	 * window frame's title bar.
	 *
	 * @param title The new title.
	 */
	@Override
	public void setTitle(String title)
	{
		super.setTitle(title);
	}

	/**
	 * Set the window icon. This icon will be displayed on the
	 * window frame's title bar.
	 *
	 * @param icon The new icon.
	 */
	public void setIcon(Icon icon)
	{
		setFrameIcon(icon);
	}

	/**
	 * Get the frame's icon.
	 *
	 * @return The frame's icon.
	 */
	public Icon getIcon()
	{
		return getFrameIcon();
	}

	/**
	 * Show the window frame
	 */
	public void showWindow()
	{
		try
		{
			if (firstView)
			{
				firstView = false;

				if (! initVisible)
				{
					return;
				}
			}

			setSelected(true);
			setVisible(true);
		}
		catch (java.beans.PropertyVetoException e)
		{
		}
	}

	/**
	 * Set the maxmized state of the window frame.
	 */
	public void setMaximized(boolean maximized)
	{
		try
		{
			setMaximum(maximized);
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Enable/disable the window frame.
	 *
	 * @param enabled If true the window frame is enabled.
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		//		((IGlassPane) getGlassPane ()).setEnabled (enabled);
	}

	/**
	 * Check whether the display is enabled or not.
	 *
	 * @return True if the display is enabled.
	 */
	@Override
	public boolean isEnabled()
	{
		//		return ((IGlassPane) getGlassPane ()).isEnabled ();
		return true;
	}

	/**
	 * Make this window the topmost window.
	 */
	public void bringToFront()
	{
		moveToFront();
	}

	public IWindow getWindow()
	{
		return window;
	}
}

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
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Swing implementation of the IDesktopFrame.
 */
public class SwingDesktopFrame extends JFrame implements IDesktopFrame
{
	/** */
	private static final long serialVersionUID = 1L;

	/** Called when the frame is to be closed. */
	private ActionListener windowClosingEvent;

	/**
	 * Create a new desktop frame.
	 */
	public SwingDesktopFrame ()
	{
		this (Engine.instance ().getResourceService ().getString ("app.title"));
	}

	/**
	 * Create a new desktop frame.
	 *
	 * @param title The desktop frame's title.
	 */
	public SwingDesktopFrame (String title)
	{
		super (title);
		getGlassPane ().addMouseListener (new MouseAdapter ()
		{
		});
		getGlassPane ().addMouseMotionListener (new MouseMotionAdapter ()
		{
		});
		getGlassPane ().addKeyListener (new KeyAdapter ()
		{
		});
		getGlassPane ().setVisible (false);

		setSize (getToolkit ().getScreenSize ());
	}

	/**
	 * Get the swing frame.
	 *
	 * @return The swing frame.
	 */
	public JFrame getJFrame ()
	{
		return this;
	}

	/**
	 * Set the fullscreen mode.
	 *
	 * @param fullScreen If true, the desktop frame is displayed in
	 *   fullscreen mode.
	 */
	public void setFullScreen (boolean fullScreen)
	{
		setUndecorated (fullScreen);
	}

	/**
	 * Check wether the frame is currently displayed in fullscreen mode
	 * or not.
	 *
	 * @return True if the desktop is displayed in fullscreen mode.
	 */
	public boolean isFullScreen ()
	{
		return isUndecorated ();
	}

	/**
	 * Check wether the desktop frame implementation supports fullscreen
	 * mode.
	 *
	 * @return True if the desktop frame supports the fullscreen mode.
	 */
	public boolean canFullScreen ()
	{
		return true;
	}

	/**
	 * Initialize the desktop frame.
	 */
	public void init ()
	{
		addWindowListener (new WindowAdapter ()
		{
			@Override
			public void windowActivated (WindowEvent e)
			{
			}

			@Override
			public void windowClosed (WindowEvent e)
			{
			}

			@Override
			public void windowClosing (WindowEvent e)
			{
				if (windowClosingEvent != null)
				{
					windowClosingEvent.actionPerformed (new ActionEvent (this, 0, "windowClosing"));
				}
			}

			@Override
			public void windowDeactivated (WindowEvent e)
			{
			}

			@Override
			public void windowDeiconified (WindowEvent e)
			{
			}

			@Override
			public void windowIconified (WindowEvent e)
			{
			}

			@Override
			public void windowOpened (WindowEvent e)
			{
			}
		});

		requestFocus ();
	}

	/**
	 * Show or hide the desktop frame.
	 *
	 * @param visible If true the desktop frame is displayed.
	 */
	@Override
	public void setVisible (boolean visible)
	{
		super.setVisible (visible);
	}

	/**
	 * Show the desktop frame.
	 */
	public void setVisible ()
	{
		setVisible (true);
	}

	/**
	 * Get the screen size of the desktop frame.
	 *
	 * @return The screen size.
	 */
	public Dimension getScreenSize ()
	{
		return getToolkit ().getScreenSize ();
	}

	/**
	 * Add a listener that will be called when the desktop manager is
	 * about to be closed.
	 *
	 * @param listener The action listener.
	 */
	public void addCloseListener (ActionListener listener)
	{
		this.windowClosingEvent = listener;
	}

	/**
	 * Close the desktop manager and free all resources.
	 */
	public void close ()
	{
		setVisible (false);
		dispose ();
	}

	/**
	 * Enable or disable the desktop frame.
	 *
	 * @param enabled If true the desktop is enabled.
	 */
	@Override
	public void setEnabled (boolean enabled)
	{
		getGlassPane ().setVisible (! enabled);
	}
}

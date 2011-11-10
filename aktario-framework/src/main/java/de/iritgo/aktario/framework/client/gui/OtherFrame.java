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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.IDockingDesktopLayouter;
import de.iritgo.aktario.core.gui.SwingDesktopFrame;
import de.iritgo.aktario.core.gui.SwingDesktopManager;
import de.iritgo.aktario.core.gui.SwingWindowFrame;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Properties;


/**
 *
 */
public class OtherFrame extends SwingDesktopFrame implements UserListener
{
	public static final Rectangle SIZE_FULL_SCREEN = new Rectangle(- 1, - 1, 0, 0);

	public static final Rectangle SIZE_PACK = new Rectangle(- 2, - 2, 0, 0);

	protected static ImageIcon defaultIcon = new ImageIcon(OtherFrame.class
					.getResource("/resources/aktario-icon-16.png"));

	protected Properties properties;

	protected OtherFrameCloseListener closeListener;

	protected String frameLabel;

	protected String frameId;

	protected Rectangle bounds;

	/**
	 */
	public Action closeAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			close();
		}
	};

	/**
	 * Other frame
	 *
	 */
	public OtherFrame(OtherFrameCloseListener closeListener, String frameLabel, String frameId, Rectangle bounds)
	{
		this.closeListener = closeListener;
		this.frameLabel = frameLabel;
		this.frameId = frameId;
		this.bounds = bounds;
	}

	public String getFrameId()
	{
		return frameId;
	}

	public void initOtherFrame()
	{
		Engine.instance().getEventRegistry().addListener("User", this);

		setTitle(Engine.instance().getResourceService().getString(frameLabel));
		setIconImage(defaultIcon.getImage());

		super.init();
		addCloseListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				closeAction.actionPerformed(e);
			}
		});

		getJFrame().getContentPane().setLayout(new BorderLayout());

		JDesktopPane desktopPane = (JDesktopPane) new OtherJDesktopPane(this);

		desktopPane.setDesktopManager(new IDockingDesktopLayouter());
		getJFrame().getContentPane().add(desktopPane, BorderLayout.CENTER);

		ClientGUI clientGUI = ((ClientGUI) Client.instance().getClientGUI());

		((SwingDesktopManager) clientGUI.getDesktopManager()).addDesktopPaneNoActivation(frameId, desktopPane);

		Dimension dim = getToolkit().getScreenSize();

		if (bounds == null)
		{
			setBounds((int) dim.getWidth() / 4, (int) dim.getHeight() / 4, (int) dim.getWidth() / 2, (int) dim
							.getHeight() / 2);
			setVisible();
		}
		else
		{
			if (bounds.equals(SIZE_FULL_SCREEN))
			{
				setVisible();
				getJFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
			else if (bounds.equals(SIZE_PACK))
			{
			}
			else
			{
				setBounds((int) ((dim.getWidth() / 2) - bounds.getX()), (int) ((dim.getHeight() / 2) - bounds.getY()),
								(int) bounds.getWidth(), (int) bounds.getHeight());

				setVisible();
			}
		}
	}

	public void close()
	{
		Engine.instance().getEventRegistry().removeListener("User", this);

		try
		{
			if (SwingUtilities.isEventDispatchThread())
			{
				ClientGUI clientGUI = ((ClientGUI) Client.instance().getClientGUI());

				SwingDesktopManager desktopManager = ((SwingDesktopManager) clientGUI.getDesktopManager());

				desktopManager.closeAllDisplays(frameId);
				setVisible(false);
				dispose();

				desktopManager.removeDesktopPane(frameId);
			}
			else
			{
				SwingUtilities.invokeAndWait(new Runnable()
				{
					public void run()
					{
						ClientGUI clientGUI = ((ClientGUI) Client.instance().getClientGUI());

						SwingDesktopManager desktopManager = ((SwingDesktopManager) clientGUI.getDesktopManager());

						desktopManager.closeAllDisplays(frameId);
						setVisible(false);
						dispose();
						desktopManager.removeDesktopPane(frameId);
					}
				});
			}
		}
		catch (Exception x)
		{
		}

		if (closeListener != null)
		{
			closeListener.otherFrameClosed(this);
		}
	}

	/**
	 * This will called if the user is loggedin
	 *
	 * @param event The userEvent.
	 */
	public void userEvent(UserEvent event)
	{
		if ((event != null) && (! event.isLoggedIn()))
		{
			close();
		}
	}

	/**
	 * Called by the desktop pane when a new window was added.
	 *
	 * @param comp The added component.
	 */
	public void windowAdded(final Component comp)
	{
		if (! getJFrame().isVisible())
		{
			if (bounds != null && bounds.x == SIZE_PACK.x)
			{
				final Dimension screenBounds = getToolkit().getScreenSize();

				if (comp instanceof SwingWindowFrame)
				{
					comp.addComponentListener(new ComponentAdapter()
					{
						public void componentShown(ComponentEvent e)
						{
							Dimension compBounds = ((SwingWindowFrame) comp).getContentPane().getPreferredSize();
							int width = compBounds.width + 64;
							int height = compBounds.height + 64;

							setBounds((screenBounds.width - width) / 2, (screenBounds.height - height) / 2, width,
											height);

							setVisible();
						}
					});
				}
				else
				{
					Dimension compBounds = getToolkit().getScreenSize();

					setBounds(0, 0, compBounds.width, compBounds.height);
					setVisible();
				}
			}
		}
	}

	/**
	 * Set the default icon.
	 *
	 * @param icon The new default icon.
	 */
	public static void setDefaultIcon(ImageIcon icon)
	{
		defaultIcon = icon;
	}

	/**
	 * Get the default icon.
	 *
	 * @return The default icon.
	 */
	public static ImageIcon getDefaultIcon()
	{
		return defaultIcon;
	}
}

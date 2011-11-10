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

package de.iritgo.aktario.client.gui;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.IDockingDesktopLayouter;
import de.iritgo.aktario.core.gui.SwingDesktopFrame;
import de.iritgo.aktario.core.gui.SwingDesktopManager;
import de.iritgo.aktario.framework.appcontext.AppContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * RoomFrame.
 *
 * @version $Id: RoomFrame.java,v 1.9 2006/09/25 10:34:31 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class RoomFrame extends SwingDesktopFrame
{
	/**
	 */
	public Action closeAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			AktarioGUI aktarioGUI = ((AktarioGUI) AppContext.instance().getObject("aktarioGui"));

			SwingDesktopManager desktopManager = ((SwingDesktopManager) aktarioGUI.getDesktopManager());

			desktopManager.getDisplay("RoomPane").systemClose();
			setVisible(false);
			dispose();
		}
	};

	/**
	 */
	public RoomFrame()
	{
		setTitle(Engine.instance().getResourceService().getString("app.title"));
		setIconImage(new ImageIcon(getClass().getResource("/resources/aktario-icon-16.png")).getImage());
		init();
		addCloseListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				closeAction.actionPerformed(e);
			}
		});

		JFrame jframe = getJFrame();

		jframe.getContentPane().setLayout(new BorderLayout());

		JDesktopPane desktopPane = new JDesktopPane();

		desktopPane.setDesktopManager(new IDockingDesktopLayouter());
		jframe.getContentPane().add(desktopPane, BorderLayout.CENTER);

		AktarioGUI aktarioGUI = ((AktarioGUI) AppContext.instance().getObject("aktarioGui"));

		((SwingDesktopManager) aktarioGUI.getDesktopManager()).addDesktopPaneNoActivation("roomFrame", desktopPane);

		setVisible();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
}

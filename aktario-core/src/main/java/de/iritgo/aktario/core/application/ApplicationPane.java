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

package de.iritgo.aktario.core.application;


import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.IWindow;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.gui.SwingWindowFrame;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.user.AktarioUserReadyServerAction;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;


/**
 * ApplicationPane
 *
 * @version $Id: ApplicationPane.java,v 1.8 2006/09/25 10:34:32 grappendorf Exp $
 */
public abstract class ApplicationPane extends SwingGUIPane
{
	/** Glass pane used to draw things on top of the gui pane. */
	ApplicationGlassPane glassPane;

	/**
	 * Create a new ApplicationPane.
	 */
	public ApplicationPane ()
	{
		super ("ApplicationPane");
	}

	/**
	 * Create a new ApplicationPane.
	 *
	 * @param id The gui id.
	 */
	public ApplicationPane (String id)
	{
		super (id);
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane ()
	{
		return null;
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return null;
	}

	/**
	 * Initialize the gui.
	 */
	@Override
	public void initGUI ()
	{
		final SwingWindowFrame frame = (SwingWindowFrame) ((IWindow) getDisplay ()).getWindowFrame ();

		glassPane = new ApplicationGlassPane ();
		frame.setGlassPane (glassPane);
		glassPane.setVisible (true);

		frame.addInternalFrameListener (new InternalFrameAdapter ()
		{
			@Override
			public void internalFrameActivated (InternalFrameEvent e)
			{
				((JComponent) frame.getGlassPane ()).setVisible (true);
			}
		});

		// 		setEnabled (false);
		getDisplay ().setIcon (new ImageIcon (ApplicationPane.class.getResource ("/resources/lessondisplay.png")));

		Long appInstanceId = (Long) getDisplay ().getProperties ().get ("aktario.applicationInstanceId");

		AppContext.instance ().put ("applicationPane", this);
		AppContext.instance ().put ("applicationPane." + appInstanceId, this);
	}

	/**
	 * Enable / disable the gui pane.
	 *
	 * @param enabled If true the gui pane will be enabled.
	 */
	public void setEnabled (boolean enabled)
	{
		glassPane.setEnabled (enabled);
	}

	/**
	 * Close the display.
	 */
	@Override
	public void close ()
	{
		Long appInstanceId = (Long) getDisplay ().getProperties ().get ("aktario.applicationInstanceId");

		AppContext.instance ().remove ("applicationPane");
		AppContext.instance ().remove ("applicationPane." + appInstanceId);
	}

	/**
	 * Get the glass pane.
	 *
	 * @return The glass pane.
	 */
	public ApplicationGlassPane getGlassPane ()
	{
		return glassPane;
	}

	/**
	 * Context help lookup.
	 */
	public void contextHelp ()
	{
	}

	/**
	 * Send a ready state action.
	 *
	 * @param ready The ready state.
	 */
	protected void sendUserReadyAction (boolean ready)
	{
		AktarioUserReadyServerAction action = new AktarioUserReadyServerAction (AppContext.instance ().getUser (),
						ready);
		ClientTransceiver transceiver = new ClientTransceiver (AppContext.instance ().getChannelNumber ());

		transceiver.addReceiver (AppContext.instance ().getChannelNumber ());
		action.setTransceiver (transceiver);
		ActionTools.sendToServer (action);
	}

	/**
	 * Send a user is ready action.
	 */
	protected void sendUserReadyAction ()
	{
		sendUserReadyAction (true);
	}
}

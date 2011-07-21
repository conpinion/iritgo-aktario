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
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


/**
 * ITitledPanel is an extended JPanel with a titled border that loads it's labels
 * from the application resources.
 */
public class ITitledPanel extends JPanel
{
	/** */
	private static final long serialVersionUID = 1L;

	/** The titled border. */
	TitledBorder border;

	/**
	 * Create a new ITitledPanel.
	 */
	public ITitledPanel ()
	{
		border = new TitledBorder (" ");
		setBorder (border);
	}

	/**
	 * Set the border title.
	 *
	 * @param titleKey The title specified by a resource key.
	 */
	public void setTitle (String titleKey)
	{
		border.setTitle (Engine.instance ().getResourceService ().getStringWithoutException (titleKey));
	}
}

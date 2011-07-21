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
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;


/**
 * IMenuItem is an extended JMenuItem that loads it's labels from the
 * application resources.
 */
public class IMenuItem extends JMenuItem
{
	/** */
	private static final long serialVersionUID = 1L;

	/** The resource key of the item label. */
	private String textKey;

	/**
	 * Creates a menu item with no set text or icon.
	 */
	public IMenuItem ()
	{
		super ();
	}

	/**
	 * Creates a menu item where properties are taken from the Action supplied.
	 *
	 * @param action The Action used to specify the new menu item.
	 */
	public IMenuItem (Action action)
	{
		super (action);
	}

	/**
	 * Creates a menu item with an icon.
	 *
	 * @param icon The Icon image to display on the menu item.
	 */
	public IMenuItem (Icon icon)
	{
		super (icon);
	}

	/**
	 * Creates a menu item with text.
	 *
	 * @param text The text of the menu item.
	 */
	public IMenuItem (String text)
	{
		super (text);
	}

	/**
	 * Creates a menu item with initial text and an icon.
	 *
	 * @param text The text of the menu item.
	 * @param icon The Icon image to display on the menu item.
	 */
	public IMenuItem (String text, Icon icon)
	{
		super (text, icon);
	}

	/**
	 * Set the menu item text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	@Override
	public void setText (String textKey)
	{
		this.textKey = textKey;
		super.setText (Engine.instance ().getResourceService ().getStringWithoutException (textKey));
	}

	/**
	 * Reload the labels of all menu items in this menu bar.
	 */
	public void reloadText ()
	{
		super.setText (Engine.instance ().getResourceService ().getStringWithoutException (textKey));
	}
}

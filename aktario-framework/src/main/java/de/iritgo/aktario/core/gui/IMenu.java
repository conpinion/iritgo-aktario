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
import javax.swing.JMenu;
import java.awt.Component;


/**
 * IMenu is an extended JMenu that loads it's labels from the
 * application resources.
 */
public class IMenu extends JMenu
{
	/** */
	private static final long serialVersionUID = 1L;

	/** The resource key of the menu label. */
	private String textKey;

	/**
	 * Creates a menu item with no set text or icon.
	 */
	public IMenu()
	{
		super();
	}

	/**
	 * Creates a menu item where properties are taken from the Action supplied.
	 *
	 * @param action The Action used to specify the new menu item.
	 */
	public IMenu(Action action)
	{
		super(action);
	}

	/**
	 * Creates a menu item with text.
	 *
	 * @param text The text of the menu item.
	 */
	public IMenu(String text)
	{
		super(text);
	}

	/**
	 * Set the menu item text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	@Override
	public void setText(String textKey)
	{
		this.textKey = textKey;
		super.setText(Engine.instance().getResourceService().getStringWithoutException(textKey));
	}

	/**
	 * Reload the labels of all menu items in this menu.
	 */
	public void reloadText()
	{
		super.setText(Engine.instance().getResourceService().getStringWithoutException(textKey));

		for (int i = 0; i < getMenuComponentCount(); ++i)
		{
			Component item = getMenuComponent(i);

			if (item.getClass().equals(IMenu.class))
			{
				((IMenu) item).reloadText();
			}

			if (item.getClass().equals(IMenuItem.class))
			{
				((IMenuItem) item).reloadText();
			}
		}
	}
}

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
import javax.swing.JButton;


/**
 * IButton is an extended JButton that loads it's labels from the
 * application resources.
 */
public class IButton extends JButton
{
	/** */
	private static final long serialVersionUID = 1L;

	/** The resource key of the button label. */
	private String textKey;

	/** The resource key of the button's tool tip. */
	private String toolTipTextKey;

	/**
	 * Create a button with no text or icon.
	 */
	public IButton()
	{
		super();
	}

	/**
	 * Create a button where properties are taken from the Action supplied.
	 *
	 * @param action The Action used to specify the new button.
	 */
	public IButton(Action action)
	{
		super(action);
	}

	/**
	 * Create a button with an icon.
	 *
	 * @param icon The Icon image to display on the button.
	 */
	public IButton(Icon icon)
	{
		super(icon);
	}

	/**
	 * Create a button with text.
	 *
	 * @param text The text of the button.
	 */
	public IButton(String text)
	{
		super(text);
	}

	/**
	 * Creates a button with initial text and an icon.
	 *
	 * @param text The text of the button.
	 * @param icon The Icon image to display on the button.
	 */
	public IButton(String text, Icon icon)
	{
		super(text, icon);
	}

	/**
	 * Set the button text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	@Override
	public void setText(String textKey)
	{
		if (textKey != null)
		{
			this.textKey = textKey;
			super.setText(Engine.instance().getResourceService().getStringWithoutException(textKey));
		}
	}

	/**
	 * Set the tool tip text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	@Override
	public void setToolTipText(String textKey)
	{
		if (textKey != null)
		{
			this.toolTipTextKey = textKey;
			super.setToolTipText(Engine.instance().getResourceService().getStringWithoutException(textKey));
		}
	}

	/**
	 * Reload the labels of all menu items in this menu bar.
	 */
	public void reloadText()
	{
		setText(textKey);
		setToolTipText(toolTipTextKey);
	}
}

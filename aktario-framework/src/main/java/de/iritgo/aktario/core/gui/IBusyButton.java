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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;


/**
 * IButton is an extended JButton that loads it's labels from the application
 * resources.
 */
public class IBusyButton extends JButton
{
	public enum Style
	{
		BUSY, SEARCH;
	}

	/** */
	private static final long serialVersionUID = 1L;

	/** The resource key of the button label. */
	private String textKey;

	/** The resource key of the button's tool tip. */
	private String toolTipTextKey;

	private Style style = Style.SEARCH;

	private ImageIcon[] idleIcon =
	{
					new ImageIcon(IBusyButton.class.getResource("/resources/busy.png")),
					new ImageIcon(IBusyButton.class.getResource("/resources/search.png"))
	};

	private ImageIcon[] busyIcon =
	{
					new ImageIcon(IBusyButton.class.getResource("/resources/busy-rotating.gif")),
					new ImageIcon(IBusyButton.class.getResource("/resources/search-rotating.gif"))
	};

	/**
	 * Create a busy button with no text.
	 */
	public IBusyButton()
	{
		super();
		setIcon(idleIcon[style.ordinal()]);
	}

	/**
	 * Create a busy button with no text.
	 *
	 * @param style The busy style
	 */
	public IBusyButton(Style style)
	{
		super();
		this.style = style;
		setIcon(idleIcon[style.ordinal()]);
	}

	/**
	 * Create a busy button with text.
	 *
	 * @param text
	 *            The text of the button.
	 */
	public IBusyButton(String text)
	{
		super(text);
		setIcon(idleIcon[style.ordinal()]);
	}

	/**
	 * Create a busy button with text.
	 *
	 * @param text
	 *            The text of the button.
	 * @param style The busy style
	 */
	public IBusyButton(String text, Style style)
	{
		super(text);
		this.style = style;
		setIcon(idleIcon[style.ordinal()]);
	}

	/**
	 * Set the button text.
	 *
	 * @param textKey
	 *            The text specified by a resource key.
	 */
	public void setText(String textKey)
	{
		this.textKey = textKey;
		super.setText(Engine.instance().getResourceService().getStringWithoutException(textKey));
	}

	/**
	 * Set the tool tip text.
	 *
	 * @param textKey
	 *            The text specified by a resource key.
	 */
	@Override
	public void setToolTipText(String textKey)
	{
		this.toolTipTextKey = textKey;
		super.setToolTipText(Engine.instance().getResourceService().getStringWithoutException(textKey));
	}

	/**
	 * Reload the labels of all menu items in this menu bar.
	 */
	public void reloadText()
	{
		setText(textKey);
		setToolTipText(toolTipTextKey);
	}

	public void setStyle(Style style)
	{
		this.style = style;
		setIcon(idleIcon[style.ordinal()]);
	}

	/**
	 * Display the idle state.
	 */
	public void idle()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				setIcon(idleIcon[style.ordinal()]);
			}
		});
	}

	/**
	 * Display the busy state.
	 */
	public void busy()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				setIcon(busyIcon[style.ordinal()]);
			}
		});
	}
}

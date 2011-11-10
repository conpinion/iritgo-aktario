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
import javax.swing.JLabel;
import javax.swing.SwingConstants;


/**
 * ILabel is an extended JLabel that loads it's labels from the
 * application resources.
 */
public class ILabel extends JLabel
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a label with no text or icon.
	 */
	public ILabel()
	{
	}

	/**
	 * Create a label with text.
	 *
	 * @param textKey The text of the label.
	 */
	public ILabel(String textKey)
	{
		super(textKey);
	}

	/**
	 * Create a label with text and icon.
	 *
	 * @param textKey The text of the label.
	 * @param icon The Icon image to display on the label.
	 */
	public ILabel(String textKey, Icon icon)
	{
		super(textKey, icon, SwingConstants.LEFT);
	}

	/**
	 * Create a label with an icon.
	 *
	 * @param icon The Icon image to display on the label.
	 */
	public ILabel(Icon icon)
	{
		super(icon);
	}

	/**
	 * Set the label text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	@Override
	public void setText(String textKey)
	{
		super.setText(Engine.instance().getResourceService().getStringWithoutException(textKey));
	}
}

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
import javax.swing.JLabel;


/**
 * IFieldLabel is an extended JLabel that loads it's labels from the
 * application resources and appends a ':' to them.
 */
public class IFieldLabel extends JLabel
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a field label with no text or icon.
	 */
	public IFieldLabel()
	{
	}

	/**
	 * Create a field label with text.
	 *
	 * @param textKey The text of the label.
	 */
	public IFieldLabel(String textKey)
	{
		super(textKey);
	}

	/**
	 * Set the label text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	@Override
	public void setText(String textKey)
	{
		super.setText(Engine.instance().getResourceService().getStringWithoutException(textKey) + ":");
	}
}

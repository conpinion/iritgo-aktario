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
import javax.swing.JCheckBox;


/**
 * ICheckBox is an extended JCheckBox that loads it's labels from the
 * application resources.
 */
public class ICheckBox extends JCheckBox
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a check box with no text or icon.
	 */
	public ICheckBox ()
	{
		super ();
	}

	/**
	 * Create a check box with text.
	 *
	 * @param textKey The text of the check box.
	 */
	public ICheckBox (String textKey)
	{
		super (textKey);
	}

	/**
	 * Set the check box text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	@Override
	public void setText (String textKey)
	{
		super.setText (Engine.instance ().getResourceService ().getStringWithoutException (textKey));
	}
}

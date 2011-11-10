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

package com.jgoodies.looks.plastic;


import com.jgoodies.looks.plastic.theme.Aktario;
import com.jgoodies.looks.plastic.theme.BueroByte;
import com.jgoodies.looks.plastic.theme.KDE;
import javax.swing.UIDefaults;


public class PlasticXPLookAndFeelIritgo extends PlasticXPLookAndFeel
{
	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static boolean themesInitialized = false;

	/**
	 * @see com.jgoodies.looks.plastic.PlasticLookAndFeel#initClassDefaults(javax.swing.UIDefaults)
	 */
	@Override
	protected void initClassDefaults(UIDefaults table)
	{
		super.initClassDefaults(table);

		table.put("InternalFrameUI", "com.jgoodies.looks.plastic.PlasticInternalFrameUIIritgo");

		if (! themesInitialized)
		{
			installTheme(new Aktario());
			installTheme(new BueroByte());
			installTheme(new KDE());
			themesInitialized = true;
		}
	}
}

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


import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ComponentUI;


/**
 *
 */
public final class PlasticInternalFrameUIIritgo extends PlasticInternalFrameUI
{
	public PlasticInternalFrameUIIritgo(JInternalFrame b)
	{
		super(b);
	}

	public static ComponentUI createUI(JComponent c)
	{
		return new PlasticInternalFrameUIIritgo((JInternalFrame) c);
	}

	@Override
	protected JComponent createNorthPane(JInternalFrame w)
	{
		if (! w.getTitle().equals("$$$ IRITGO-HIDE $$$"))
		{
			return new PlasticInternalFrameTitlePane(w);
		}

		return null;
	}
}

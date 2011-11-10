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


import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.Scrollable;


public class ITable extends JTable
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Returns false to indicate that horizontal scrollbars are required
	 * to display the table while honoring perferred column widths. Returns
	 * true if the table can be displayed in viewport without horizontal
	 * scrollbars.
	 *
	 * @return true if an auto-resizing mode is enabled
	 * and the viewport width is larger than the table's
	 * preferred size, otherwise return false.
	 * @see Scrollable#getScrollableTracksViewportWidth
	 */
	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		if (autoResizeMode != AUTO_RESIZE_OFF)
		{
			if (getParent() instanceof JViewport)
			{
				return (((JViewport) getParent()).getWidth() > getPreferredSize().width);
			}
		}

		return false;
	}
}

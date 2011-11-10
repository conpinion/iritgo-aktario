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

package de.iritgo.aktario.xp;


import org.syntax.jedit.JEditTextArea;
import java.awt.event.AdjustmentListener;


/**
 * @version $Id: CodeEditor.java,v 1.8 2006/09/25 10:34:32 grappendorf Exp $
 */
public class CodeEditor extends JEditTextArea
{
	/** . */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new CodeEditor with the default settings.
	 */
	public CodeEditor()
	{
	}

	/**
	 * Add an adjustment listener to the horizontal scroll bar.
	 *
	 * @param AdjustmentListener The listener to add.
	 */
	public void addHorizontalAdjustmentListener(AdjustmentListener listener)
	{
		horizontal.addAdjustmentListener(listener);
	}

	/**
	 * Add an adjustment listener to the vertical scroll bar.
	 *
	 * @param AdjustmentListener The listener to add.
	 */
	public void addVerticalAdjustmentListener(AdjustmentListener listener)
	{
		vertical.addAdjustmentListener(listener);
	}

	/**
	 * Get the first visible line.
	 *
	 * @return The first visible line.
	 */
	public int getFirstVisibleLine()
	{
		return vertical.getValue();
	}

	/**
	 * Get the first visible column.
	 *
	 * @return The first visible column.
	 */
	public int getFirstVisibleColumn()
	{
		return horizontal.getValue();
	}

	/**
	 * Selects from the start offset to the end offset. This is the
	 * general selection method used by all other selecting methods.
	 * The caret position will be start if start &lt; end, and end
	 * if end &gt; start.
	 * @param start The start offset
	 * @param end The end offset
	 */
	public void select(int start, int end)
	{
		int newStart;
		int newEnd;
		boolean newBias;

		if (start <= end)
		{
			newStart = start;
			newEnd = end;
			newBias = false;
		}
		else
		{
			newStart = end;
			newEnd = start;
			newBias = true;
		}

		if (newStart < 0 || newEnd > getDocumentLength())
		{
			return;
		}

		super.select(start, end);
	}
}

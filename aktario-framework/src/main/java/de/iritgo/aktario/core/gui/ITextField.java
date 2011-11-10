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


import javax.swing.JTextField;
import javax.swing.text.Document;
import java.awt.event.FocusEvent;


/**
 * An enhanced JTextField.
 */
public class ITextField extends JTextField
{
	/** */
	private static final long serialVersionUID = 1L;

	/** If true, the whole text is selected if the component gains the focus. */
	protected boolean autoSelect = false;

	/**
	 * Constructs a new ITextField.
	 */
	public ITextField()
	{
		super();
	}

	/**
	 * Constructs a new JTextField that uses the given text storage model
	 * and the given number of columns.
	 *
	 * @param doc The text storage to use; if this is null, a default will be
	 *   provided by calling the createDefaultModel method
	 * @param text The text to be displayed, or null
	 * @param columns The number of columns to use to calculate the preferred
	 *   width >= 0; if columns  is set to zero, the preferred width will be
	 *   whatever naturally results from the component implementation.
	 */
	public ITextField(Document doc, String text, int columns)
	{
		super(doc, text, columns);
	}

	/**
	 * Constructs a new empty TextField with the specified number of columns.
	 *
	 * @param columns The number of columns to use to calculate the preferred width;
	 *   if columns is set to zero, the preferred width will be whatever naturally
	 *   results from the component implementation
	 */
	public ITextField(int columns)
	{
		super(columns);
	}

	/**
	 * Constructs a new TextField initialized with the specified text.
	 *
	 * @param text The text to be displayed, or null.
	 */
	public ITextField(String text)
	{
		super(text);
	}

	/**
	 * Constructs a new TextField initialized with the specified text and columns.
	 *
	 * @param text The text to be displayed, or null.
	 * @param columns The number of columns to use to calculate the preferred width;
	 *   if columns is set to zero, the preferred width will be whatever naturally
	 *   results from the component implementation
	 */
	public ITextField(String text, int columns)
	{
		super(text, columns);
	}

	/**
	 * Specifiy wether the whole text should be automatically selected if the
	 * text field gains the focus.
	 *
	 * @param autoSelect If true the text is selected if the text field is
	 *   activated.
	 */
	public void setAutoSelect(boolean autoSelect)
	{
		this.autoSelect = autoSelect;
	}

	/**
	 * @see javax.swing.JFormattedTextField#processFocusEvent(java.awt.event.FocusEvent)
	 */
	@Override
	protected void processFocusEvent(FocusEvent e)
	{
		super.processFocusEvent(e);

		if (autoSelect)
		{
			if (e.getID() == FocusEvent.FOCUS_GAINED)
			{
				this.selectAll();
			}
			else
			{
				this.select(0, 0);
			}
		}
	}
}

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


import javax.swing.JComboBox;
import javax.swing.SwingUtilities;


public class IComboBoxIdItem
{
	public Object id;

	public String label;

	public IComboBoxIdItem(Object id, String label)
	{
		this.id = id;
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}

	public static void selectItemWithId(final JComboBox comboBox, Object id)
	{
		for (int index = 0; index < comboBox.getItemCount(); ++index)
		{
			if (((IComboBoxIdItem) comboBox.getItemAt(index)).id.equals(id))
			{
				final int selectIndex = index;

				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						comboBox.setSelectedIndex(selectIndex);
					}
				});

				return;
			}
		}

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				comboBox.setSelectedIndex(comboBox.getItemCount() > 0 ? 0 : - 1);
			}
		});
	}

	public static Object getSelectedId(JComboBox comboBox)
	{
		IComboBoxIdItem item = (IComboBoxIdItem) comboBox.getSelectedItem();

		return item != null ? item.id : null;
	}
}

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

package de.iritgo.aktario.framework.dataobject.gui;


import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import javax.swing.JComponent;
import java.util.Properties;


/**
 *
 */
public interface ExtensionTile
{
	/** Predefined extension tile type */
	public static final String COLUMN = "columns";

	/** Predefined extension tile type */
	public static final String LIST_COMMAND = "listcommands";

	public JComponent getTile (GUIPane guiPane, IObject iObject, Properties properties);

	public void command (GUIPane guiPane, IObject iObject, Properties properties);

	public boolean isDoubleClickCommand ();

	public String getTileId ();

	public String getLabel ();

	public Object getConstraints ();
}

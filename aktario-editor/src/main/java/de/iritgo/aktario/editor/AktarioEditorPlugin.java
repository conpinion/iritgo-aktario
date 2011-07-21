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

package de.iritgo.aktario.editor;


import de.iritgo.aktario.core.AktarioPlugin;
import de.iritgo.aktario.core.plugin.Plugin;


/**
 * AktarioEditorPlugin
 *
 * @version $Id: AktarioEditorPlugin.java,v 1.5 2006/09/25 10:34:32 grappendorf Exp $
 */
public class AktarioEditorPlugin extends AktarioPlugin
{
	/**
	 * Register all data objects in this method.
	 */
	@Override
	protected void registerDataObjects ()
	{
		registerDataObject (new EditorData ());
	}

	/**
	 * Register all actions in this method.
	 */
	@Override
	protected void registerActions ()
	{
		registerAction (new RemoteControlAction ());
		registerAction (new RemoteControlServerAction ());
	}

	@Override
	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new EditorManager ());
	}

	/**
	 * Register all gui panes in this method.
	 */
	@Override
	protected void registerGUIPanes ()
	{
		registerGUIPane (new EditorPane ());
	}

	/**
	 * Register all collaboration applications in this method.
	 */
	@Override
	public void registerApplications ()
	{
		registerApplication ("aktario.editor.Editor", "aktario.softwareReview", "/resources/xp.png", "EditorPane");
	}
}

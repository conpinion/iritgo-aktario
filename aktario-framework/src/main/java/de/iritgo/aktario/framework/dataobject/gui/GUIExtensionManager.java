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


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.manager.Manager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class GUIExtensionManager extends BaseObject implements Manager
{
	private Map<String, Map<String, List<ExtensionTile>>> extensions;

	public GUIExtensionManager ()
	{
		super ("GUIExtensionManager");
		extensions = new HashMap<String, Map<String, List<ExtensionTile>>> ();
		init ();
	}

	public void registerExtension (String extensionId, String extensionTileId, ExtensionTile extensionTile)
	{
		Map extension = (Map) extensions.get (extensionId);

		if (extension == null)
		{
			extension = new HashMap ();
			extensions.put (extensionId, extension);
		}

		List extensionTiles = (List) extension.get (extensionTileId);

		if (extensionTiles == null)
		{
			extensionTiles = new LinkedList ();
			extension.put (extensionTileId, extensionTiles);
		}

		extensionTiles.add (extensionTile);
	}

	public void removeExtension (String extensionId, String extensionTileId, ExtensionTile extensionTile)
	{
		Map extension = (Map) extensions.get (extensionId);

		if (extension == null)
		{
			extension = new HashMap ();
			extensions.put (extensionId, extension);
		}

		List extensionTiles = (List) extension.get (extensionTileId);

		if (extensionTiles == null)
		{
			extensionTiles = new LinkedList ();
			extension.put (extensionTileId, extensionTiles);
		}

		extensionTiles.remove (extensionTile);
	}

	public ExtensionTile getExtension (String extensionId, String extensionTileId, int index)
	{
		Map extension = (Map) extensions.get (extensionId);
		List extensionTiles = (List) extension.get (extensionTileId);

		return (ExtensionTile) extensionTiles.get (index);
	}

	public boolean existsExtension (String extensionId, String extensionTileId)
	{
		Map extension = (Map) extensions.get (extensionId);

		if (extension == null)
		{
			return false;
		}

		List extensionTiles = (List) extension.get (extensionTileId);

		return extensionTiles != null;
	}

	public List<ExtensionTile> getExtensions (String extensionId, String extensionTileId)
	{
		Map extension = (Map) extensions.get (extensionId);

		if (extension == null)
		{
			extension = new HashMap ();
			extensions.put (extensionId, extension);
		}

		List extensionTiles = (List) extension.get (extensionTileId);

		if (extensionTiles == null)
		{
			extensionTiles = new LinkedList ();
		}

		return extensionTiles;
	}

	public Iterator getExtensionIterator (String extensionId, String extensionTileId)
	{
		return getExtensions (extensionId, extensionTileId).iterator ();
	}

	public ExtensionTile getExtension (String extensionId, String extensionTileId, String id)
	{
		if (id == null)
		{
			return null;
		}

		Map<String, List<ExtensionTile>> exts = extensions.get (extensionId);

		if (exts != null)
		{
			List<ExtensionTile> tiles = exts.get (extensionTileId);

			if (tiles != null)
			{
				for (ExtensionTile tile : tiles)
				{
					if (id.equals (tile.getTileId ()))
					{
						return tile;
					}
				}
			}
		}

		return null;
	}

	public List<ExtensionTile> getExtensionsCopy (String extensionId, String extensionTileId)
	{
		Map<String, List<ExtensionTile>> exts = extensions.get (extensionId);

		if (exts != null)
		{
			List<ExtensionTile> tiles = exts.get (extensionTileId);

			if (tiles != null)
			{
				return new LinkedList<ExtensionTile> (tiles);
			}
		}

		return new LinkedList<ExtensionTile> ();
	}

	public void init ()
	{
	}

	public void unload ()
	{
	}
}

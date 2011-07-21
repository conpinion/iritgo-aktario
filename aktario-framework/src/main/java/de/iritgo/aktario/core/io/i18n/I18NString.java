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

package de.iritgo.aktario.core.io.i18n;


import de.iritgo.aktario.core.Engine;


/**
 * I18NStrings hold a key to a string resource instead of the string itself.
 * The getter method automatically translates the key to this string resource.
 */
public class I18NString
{
	/** The string resource key */
	private String key;

	public I18NString ()
	{
	}

	public I18NString (String key)
	{
		this.key = key;
	}

	public String getKey ()
	{
		return key;
	}

	public void setKey (String key)
	{
		this.key = key;
	}

	/**
	 * Get the translated string. If no string resource was found under the
	 * specified key, the key is returned itself.
	 *
	 * @return The translated key or the key itself
	 */
	public String get ()
	{
		return Engine.instance ().getResourceService ().getStringWithoutException (key);
	}

	/**
	 * Same as @see de.iritgo.aktario.core.io.i18n.I18NString#get() but
	 * additional parameters are supplied to replace {n} placeholders in the
	 * string resource.
	 *
	 * @param params The string parameters
	 * @return The translated key or the key itself
	 */
	public String get (Object... params)
	{
		return Engine.instance ().getResourceService ().getStringWithParams (key, params);
	}

	/**
	 * Calls @see de.iritgo.aktario.core.io.i18n.I18NString#get() to return
	 * the translated string.
	 *
	 * @return The translated string resource
	 */
	@Override
	public String toString ()
	{
		return get ();
	}
}

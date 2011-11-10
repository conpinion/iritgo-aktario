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

package de.iritgo.aktario.framework.base.action;


import java.io.IOException;


/**
 *
 */
public class TurnAction extends FrameworkAction
{
	private long turn;

	/**
	 * Standard constructor
	 */
	public TurnAction()
	{
		super(- 1);
	}

	/**
	 * Standard constructor
	 */
	public TurnAction(long turn)
	{
		this.turn = turn;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		turn = stream.readLong();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong(turn);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
	}
}

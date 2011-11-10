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

package de.iritgo.aktario.jdbc;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.uid.IDGenerator;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import javax.sql.DataSource;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;


/**
 * An id generator that stores it's state in a relational database.
 */
public class JDBCIDGenerator extends BaseObject implements IDGenerator
{
	/** The next unique id. */
	protected long id;

	/** The step increment. */
	protected long step;

	/** The chunk size. */
	protected long chunk;

	/** How much ids are left, before we must allocate a new chunk. */
	protected long free;

	/**
	 * Create a new id generator.
	 *
	 * @param start The initial id value.
	 * @param step The step increment.
	 * @param chunk The chunk size.
	 */
	public JDBCIDGenerator(long start, long step, long chunk)
	{
		super("JDBCIDGenerator");
		this.id = start;
		this.step = step;
		this.chunk = chunk;
		this.free = 0;
	}

	/**
	 * Create a new id generator.
	 */
	public JDBCIDGenerator()
	{
		this(1, 1, 10);
	}

	/**
	 * Create a new unique id.
	 *
	 * @return The new unique id.
	 */
	public synchronized long createId()
	{
		if (free == 0)
		{
			allocateIds();
		}

		--free;

		long nextId = id;

		id += step;

		return nextId;
	}

	/**
	 * Get the value of the next id that createId() will return.
	 *
	 * @return The next id value.
	 */
	public long peekNextId()
	{
		return id;
	}

	/**
	 * Create a new instance of the id generator.
	 *
	 * @return The fresh instance.
	 */
	public IObject create()
	{
		return new JDBCIDGenerator();
	}

	/**
	 * Allocate new ids.
	 */
	protected void allocateIds()
	{
		JDBCManager jdbcManager = (JDBCManager) Engine.instance().getManager("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource();

		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = dataSource.getConnection();

			stmt = connection.prepareStatement("update IritgoProperties set value=? where name=?");
			stmt.setLong(1, id + chunk * step);
			stmt.setString(2, "persist.ids.nextvalue");
			stmt.execute();

			free = chunk;

			Log.logVerbose("persist", "JDBCIDGenerator", "Successfully allocated new ids (id=" + id + ")");
		}
		catch (Exception x)
		{
			Log.logFatal("persist", "JDBCIDGenerator", "Error while allocating new ids: " + x);
		}
		finally
		{
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(connection);
		}
	}

	/**
	 * Load the last generator state.
	 */
	public void load()
	{
		JDBCManager jdbcManager = (JDBCManager) Engine.instance().getManager("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource();

		try
		{
			QueryRunner query = new QueryRunner(dataSource);
			Object[] res = (Object[]) query.query("select value from IritgoProperties where name=?",
							"persist.ids.nextvalue", new ArrayHandler());

			id = Long.parseLong((String) res[0]);

			free = 0;

			Log.logDebug("persist", "JDBCIDGenerator", "Successfully loaded the generator state (id=" + id + ")");
		}
		catch (Exception x)
		{
			Log.logError("persist", "JDBCIDGenerator", "Error while loading the generator state: " + x);
		}
	}

	/**
	 * Store the generator state.
	 */
	public void save()
	{
		Log.logDebug("persist", "JDBCIDGenerator", "Successfully saved the generator state (id=" + id + ")");
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject(InputStream stream) throws IOException, ClassNotFoundException
	{
		DataInputStream dataStream = new DataInputStream(stream);

		id = dataStream.readLong();
	}

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	public void writeObject(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);

		dataStream.writeLong(id);
	}

	/**
	 * Serialize the object type information on this object
	 */
	public IObject writeTypeInformations(OutputStream stream, IObject iObject)
	{
		return this;
	}

	/**
	 * Read Serialize type information a given stream
	 * and do some things...
	 */
	public IObject readTypeInformations(InputStream stream, IObject iObject)
	{
		return this;
	}
}

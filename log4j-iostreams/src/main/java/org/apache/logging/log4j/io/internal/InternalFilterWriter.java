/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.logging.log4j.io.internal;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.io.CharStreamLogger;
import org.apache.logging.log4j.spi.ExtendedLogger;

/**
 * Internal class that exists primarily to allow location calculation to work.
 *
 * @since 2.12
 */
public class InternalFilterWriter extends FilterWriter {

    private final CharStreamLogger logger;
    private final String fqcn;

    public InternalFilterWriter(
            final Writer out, final ExtendedLogger logger, final String fqcn, final Level level, final Marker marker) {
        super(out);
        this.logger = new CharStreamLogger(logger, level, marker);
        this.fqcn = fqcn;
    }

    @Override
    public void close() throws IOException {
        this.out.close();
        this.logger.close(this.fqcn);
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public String toString() {
        return "{writer=" + this.out + '}';
    }

    @Override
    public void write(final char[] cbuf) throws IOException {
        this.out.write(cbuf);
        this.logger.put(this.fqcn, cbuf, 0, cbuf.length);
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        this.out.write(cbuf, off, len);
        this.logger.put(this.fqcn, cbuf, off, len);
    }

    @Override
    public void write(final int c) throws IOException {
        this.out.write(c);
        this.logger.put(this.fqcn, (char) c);
    }

    @Override
    public void write(final String str) throws IOException {
        this.out.write(str);
        this.logger.put(this.fqcn, str, 0, str.length());
    }

    @Override
    public void write(final String str, final int off, final int len) throws IOException {
        this.out.write(str, off, len);
        this.logger.put(this.fqcn, str, off, len);
    }
}

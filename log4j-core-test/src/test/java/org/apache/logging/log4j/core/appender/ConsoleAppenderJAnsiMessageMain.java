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
package org.apache.logging.log4j.core.appender;

import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;

import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.test.categories.Layouts;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

/**
 * Shows how to use ANSI escape codes to color messages. Each message is printed to the console in color, but the rest
 * of the log entry (time stamp for example) is in the default color for that console.
 * <p>
 * Running from a Windows command line from the root of the project:
 * </p>
 *
 * <pre>
 * mvn -Dtest=org.apache.logging.log4j.core.appender.ConsoleAppenderJAnsiMessageMain test
 * </pre>
 *
 * or, on Windows:
 *
 * <pre>
 * java -classpath log4j-core\target\test-classes;log4j-core\target\classes;log4j-api\target\classes;%USERPROFILE%\.m2\repository\org\fusesource\jansi\jansi\1.14\jansi-1.14.jar; org.apache.logging.log4j.core.appender.ConsoleAppenderJAnsiMessageMain log4j-core/src/test/resources/log4j2-console-msg-ansi.xml
 * </pre>
 *
 */
@Category(Layouts.Jansi.class)
public class ConsoleAppenderJAnsiMessageMain {

    public static void main(final String[] args) {
        new ConsoleAppenderJAnsiMessageMain().test(args);
    }

    /**
     * This is a @Test method to make it easy to run from a command line with {@code mvn -Dtest=FQCN test}
     */
    @Test
    @ResourceLock(Resources.SYSTEM_PROPERTIES)
    public void test() {
        test(null);
    }

    public void test(final String[] args) {
        System.setProperty("log4j.skipJansi", "false"); // LOG4J2-2087: explicitly enable
        // System.out.println(System.getProperty("java.class.path"));
        final String config =
                args == null || args.length == 0 ? "target/test-classes/log4j2-console-msg-ansi.xml" : args[0];
        try (final LoggerContext ctx =
                Configurator.initialize(ConsoleAppenderAnsiMessagesMain.class.getName(), config)) {
            final Logger logger = LogManager.getLogger(ConsoleAppenderJAnsiMessageMain.class);
            logger.info(ansi().fg(RED).a("Hello").fg(CYAN).a(" World").reset());
            // JAnsi format:
            // logger.info("@|red Hello|@ @|cyan World|@");
            for (final Entry<Object, Object> entry : System.getProperties().entrySet()) {
                logger.info("@|KeyStyle {}|@ = @|ValueStyle {}|@", entry.getKey(), entry.getValue());
            }
        }
    }
}

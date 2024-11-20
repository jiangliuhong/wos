/*
 * Copyright 2020 top.jiangliuhong.wos.sql
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.jiangliuhong.wos.sql.exception;

import top.jiangliuhong.wos.sql.parser.Parser;
import top.jiangliuhong.wos.sql.util.SqlExceptionUtil;
import org.slf4j.Logger;

import java.sql.SQLIntegrityConstraintViolationException;


public class ExceptionTranslator {

    private ExceptionTranslator(){}

    public static SqlRuntimeException onRollback(Class clzz, Exception e, Logger logger) {
        Throwable t = SqlExceptionUtil.unwrapThrowable(e);
        logger.error(SqlExceptionUtil.getMessage(t));
        if ( t instanceof SQLIntegrityConstraintViolationException){
            String msg = t.getMessage();
            if (msg.contains("cannot be null")) {
                String prefix = (clzz == null ? "" : ("Table of "+ Parser.get(clzz).getTableName()));
                throw new SqlRuntimeException(prefix+", " + msg);
            }
        }
        if (t instanceof RuntimeException)
            throw (RuntimeException)t;
        return new SqlRuntimeException(t);
    }

    public static QueryException onQuery(Exception e, Logger logger) {
        Throwable t = SqlExceptionUtil.unwrapThrowable(e);
        logger.error(SqlExceptionUtil.getMessage(t));
        if (t instanceof RuntimeException)
            throw (RuntimeException)t;
        return new QueryException(e);
    }
}

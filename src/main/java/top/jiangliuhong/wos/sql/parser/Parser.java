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
package top.jiangliuhong.wos.sql.parser;

import lombok.extern.slf4j.Slf4j;
import top.jiangliuhong.wos.sql.def.Table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public final class Parser {

    private final static Map<String, Parsed> map = new ConcurrentHashMap<>();

    public static String mappingPrefix;
    public static String mappingSpec;

    private Parser() {
    }

    public static void put(String tableFullName, Parsed parsed) {
        map.put(tableFullName, parsed);
    }

    public static Parsed get(String tableFullName) {
        return map.get(tableFullName);
    }

    public static boolean contains(String tableFullName) {
        return map.containsKey(tableFullName);
    }


    public static void parse(Table table) {
        Parsed parsed = new Parsed(table);
        put(table.getFullName(), parsed);

    }

}

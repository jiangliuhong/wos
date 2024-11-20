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
package top.jiangliuhong.wos.sql.util;

import top.jiangliuhong.wos.sql.builder.internal.SqlScript;
import top.jiangliuhong.wos.sql.exception.ParsingException;
import top.jiangliuhong.wos.sql.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public final class ParserUtil {

    public final static String SQL_KEYWORD_MARK = "`";

    private ParserUtil() {
        super();
    }

    public static String filterSQLKeyword(String mapper) {
        for (String keyWord : SqlScript.KEYWORDS) {
            if (keyWord.equalsIgnoreCase(mapper)) {
                return SQL_KEYWORD_MARK + mapper + SQL_KEYWORD_MARK;
            }
        }
        return mapper;
    }

    public static String getTableName(String alia, Map<String, String> aliaMap) {
        if (aliaMap == null)
            throw new ParsingException("QB.of(Class) not support the key contains '.'");
        String a = aliaMap.get(alia);
        if (SqlStringUtil.isNotNull(a))
            return a;
        return alia;
    }


    public static String getMapper(String property) {

        String AZ = "AZ";
        int min = AZ.charAt(0) - 1;
        int max = AZ.charAt(1) + 1;

        try {
            String spec = Parser.mappingSpec;
            if (SqlStringUtil.isNotNull(spec)) {
                char[] arr = property.toCharArray();
                int length = arr.length;
                List<String> list = new ArrayList<String>();
                StringBuilder temp = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    char c = arr[i];
                    if (c > min && c < max) {
                        String ts = temp.toString();
                        if (SqlStringUtil.isNotNull(ts)) {
                            list.add(temp.toString());
                        }
                        temp = new StringBuilder();
                        String s = String.valueOf(c);
                        temp.append(s.toLowerCase());
                    } else {
                        temp = temp.append(c);
                    }

                    if (i == length - 1) {
                        list.add(temp.toString());
                    }

                }

                String str = "";

                int size = list.size();
                for (int i = 0; i < size; i++) {
                    String s = list.get(i);
                    str += s;
                    if (i < size - 1) {
                        str += "_";
                    }
                }
                return str;
            }

        } catch (Exception e) {

        }
        return property;
    }

}

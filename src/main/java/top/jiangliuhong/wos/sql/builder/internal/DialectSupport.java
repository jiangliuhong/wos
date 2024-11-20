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
package top.jiangliuhong.wos.sql.builder.internal;

import top.jiangliuhong.wos.sql.def.Field;
import top.jiangliuhong.wos.sql.def.TableValue;
import top.jiangliuhong.wos.sql.exception.ParsingException;
import top.jiangliuhong.wos.sql.exception.PersistenceException;
import top.jiangliuhong.wos.sql.util.EnumUtil;
import top.jiangliuhong.wos.sql.util.SqlExceptionUtil;
import top.jiangliuhong.wos.sql.util.SqlJsonUtil;

import java.util.List;


public interface DialectSupport {

    String getKey();

    Object convertJsonToPersist(Object json);

    String getAlterTableUpdate();

    String getAlterTableDelete();

    String getCommandUpdate();

    String getCommandDelete();

    String getLimitOne();

    String getInsertTagged();

    Object filterValue(Object value);


    /**
     * 将参数对象转为集合对象
     *
     * @param list
     * @param tableValue
     * @param tempList
     */
    default void objectToListForCreate(List<Object> list, TableValue tableValue, List<Field> tempList) {
        try {
            for (Field field : tempList) {
                Class<?> type = field.getType();
                Object value = tableValue.get(field.getName());
                if (value == null) {
                    if (EnumUtil.isEnum(type)) {
                        throw new PersistenceException("ENUM CAN NOT NULL, property:" + tableValue.getTable().getName() + "." + field.getName());
                    } else {
                        list.add(null);
                    }
                }else{
                    if (field.getJson()) {
                        String str = SqlJsonUtil.toJson(value);
                        Object jsonStr = convertJsonToPersist(str);
                        list.add(jsonStr);
                    } else if (EnumUtil.isEnum(type)) {
                        list.add(EnumUtil.serialize((Enum) value));
                    } else {
                        value = filterValue(value);
                        list.add(value);
                    }
                }
            }
        } catch (Exception e) {
            SqlExceptionUtil.throwRuntimeExceptionFirst(e);
            throw new ParsingException(e);
        }


    }
}

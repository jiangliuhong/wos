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

import lombok.Getter;
import lombok.Setter;
import top.jiangliuhong.wos.sql.def.Field;
import top.jiangliuhong.wos.sql.def.Table;
import top.jiangliuhong.wos.sql.exception.ParsingException;
import top.jiangliuhong.wos.sql.util.SqlStringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
public final class Parsed {


    private Table table;
    private String keyName;
    private Field keyField;
    private Map<String, Field> fieldMap = new HashMap<>();

    private final List<Field> tagFieldList = new ArrayList<>();

    private List<BeanElement> beanElementList;

    private Map<String, BeanElement> elementMap = new HashMap<>();
    private Map<String, String> propertyMapperMap = new HashMap<>();
    private Map<String, String> mapperPropertyMapLower = new HashMap<>();

    public Parsed(Table table) {
        this.table = table;
    }

    public Field getField(String name) {
        return fieldMap.get(name);
    }

    public Field getFieldExisted(String name) {
        Field field = fieldMap.get(name);
        if (field == null) {
            String message = String.format("Not exist %s from %s", name, this.table.getName());
            throw new ParsingException(message);
        }
        return field;
    }


    public String getTableName(String alia) {
        if (SqlStringUtil.isNullOrEmpty(alia))
            return this.getTableName();
        if (!alia.equalsIgnoreCase(getTableName()))
            return alia;
        return this.getTableName();
    }

    public String getTableName() {
        return this.table.getName();
    }


    public String getMapper(String property) {
        return propertyMapperMap.get(property);
    }


}

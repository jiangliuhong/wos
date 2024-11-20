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

import top.jiangliuhong.wos.sql.builder.Op;
import top.jiangliuhong.wos.sql.builder.Q;
import top.jiangliuhong.wos.sql.def.Field;
import top.jiangliuhong.wos.sql.filter.BaseTypeFilter;
import top.jiangliuhong.wos.sql.filter.UnsafeSyntaxFilter;
import top.jiangliuhong.wos.sql.mapping.Mappable;
import top.jiangliuhong.wos.sql.mapping.Mapper;
import top.jiangliuhong.wos.sql.mapping.Script;
import top.jiangliuhong.wos.sql.mapping.SqlNormalizer;
import top.jiangliuhong.wos.sql.parser.Parsed;
import top.jiangliuhong.wos.sql.parser.Parser;
import top.jiangliuhong.wos.sql.support.TimeSupport;
import top.jiangliuhong.wos.sql.util.EnumUtil;
import top.jiangliuhong.wos.sql.util.SqlStringUtil;

import java.util.Iterator;
import java.util.List;


public interface CondQToSql extends Mapper, SqlNormalizer, UnsafeSyntaxFilter {

    default void buildConditionSql(StringBuilder sb, List<Bb> bbList, Mappable mappable) {
        if (bbList == null || bbList.isEmpty())
            return;
        for (Bb bb : bbList) {

            Op p = bb.getP();
            if (p == Op.LIMIT || p == Op.OFFSET) {
                continue;
            }

            if (p == Op.SUB) {
                if (bb.getSubList().isEmpty())
                    continue;
                bb.getSubList().get(0).setC(Op.NONE);
                sb.append(bb.getC().sql());
                sb.append(SqlScript.LEFT_PARENTTHESIS).append(SqlScript.SPACE);
                buildConditionSql(sb, bb.getSubList(), mappable);
                sb.append(SqlScript.SPACE).append(SqlScript.RIGHT_PARENTTHESIS);
                continue;
            }

            String mapper = null;
            if (p == Op.X){
                String key = bb.getKey();
                if (SqlStringUtil.isNullOrEmpty(key))
                    continue;
                final String str  = normalizeSql(key);
                StringBuilder sbx = new StringBuilder();
                mapping((reg)->str.split(reg), mappable,sbx);
                mapper = sbx.toString();
                sb.append(bb.getC().sql()).append(mapper);
            }else {
                mapper = mapping(bb.getKey(), mappable);
                sb.append(bb.getC().sql()).append(mapper).append(Script.SPACE).append(p.sql()).append(Script.SPACE);
            }

            if (bb.getValue() != null) {
                if (p == Op.IN || p == Op.NOT_IN) {
                    List<Object> inList = (List<Object>) bb.getValue();
                    Object v = inList.get(0);
                    Class<?> vType = v.getClass();
                    if (vType == String.class) {
                        vType = mapClzz(bb.getKey(), mappable);
                    }
                    buildIn(sb, vType, inList);
                } else if (!(p == Op.IS_NULL
                        || p == Op.IS_NOT_NULL
                        || p == Op.X)) {
                    sb.append(SqlScript.PLACE_HOLDER).append(SqlScript.SPACE);
                }
            }

        }

    }


    default void buildIn(StringBuilder sb, Class clzz, List<? extends Object> inList) {

        sb.append(SqlScript.LEFT_PARENTTHESIS).append(SqlScript.SPACE);//"( "

        int length = inList.size();
        if (clzz == String.class) {

            for (int j = 0; j < length; j++) {
                Object value = inList.get(j);
                if (value == null )
                    continue;
                value = filter(value.toString());
                sb.append(SqlScript.SINGLE_QUOTES).append(value).append(SqlScript.SINGLE_QUOTES);//'string'
                if (j < length - 1) {
                    sb.append(SqlScript.COMMA);
                }
            }

        } else if (EnumUtil.isEnum(clzz)) {
            for (int j = 0; j < length; j++) {
                Object value = inList.get(j);
                if (value == null)
                    continue;

                value = EnumUtil.serialize(clzz, value);

                if (value instanceof String){
                    sb.append(SqlScript.SINGLE_QUOTES).append(value).append(SqlScript.SINGLE_QUOTES);//'string'
                }else {
                    sb.append(value);
                }
                if (j < length - 1) {
                    sb.append(SqlScript.COMMA);
                }
            }
        } else {
            for (int j = 0; j < length; j++) {
                Object value = inList.get(j);
                if (value == null)
                    continue;
                sb.append(value);
                if (j < length - 1) {
                    sb.append(SqlScript.COMMA);
                }
            }
        }

        sb.append(SqlScript.SPACE).append(SqlScript.RIGHT_PARENTTHESIS);
    }


    interface Filter {

        default void filter(List<Bb> bbList, Mappable mappable) {

            if (bbList == null || bbList.isEmpty() )
                return;

            Iterator<Bb> ite = bbList.iterator();
            while (ite.hasNext()) {
                Bb bb = ite.next();
                Op p = bb.getP();
                String key = bb.getKey();
                if (p == Op.SUB){
                    filter(bb.getSubList(), mappable);
                    if (bb.getSubList().isEmpty()) {
                        ite.remove();
                    }
                } else if (p == Op.EQ
                        || p == Op.NE
                        || p == Op.GT
                        || p == Op.GTE
                        || p == Op.LT
                        || p == Op.LTE) {

                    if (key.contains(".")){
                        String[] arr = key.split("\\.");
                        String alia = arr[0];
                        String clzName = mappable.getAliaMap().get(alia);
                        if (clzName == null)
                            clzName = alia;
                        Parsed parsed = Parser.get(clzName);
                        if (parsed != null) {
                            if (BaseTypeFilter.isBaseType(arr[1], bb.getValue(), parsed)) {
                                ite.remove();
                            } else {
                                Field field = parsed.getField(arr[1]);
                                if (field != null) {
                                    TimeSupport.testWriteNumberValueToTime(field.getType(), bb);
                                    if (bb.getValue() == null)
                                        ite.remove();
                                }
                            }
                        }
                    }else{
                        Parsed parsed = mappable.getParsed();
                        if (parsed == null) {
                            String ss = ((Q.X)mappable).sourceScript();
                            if (ss != null) {
                                parsed = Parser.get(ss);
                            }
                        }
                        if (parsed != null) {
                            if (BaseTypeFilter.isBaseType(key, bb.getValue(), parsed)) {
                                ite.remove();
                            } else {
                                Field field = parsed.getField(key);
                                if (field != null) {
                                    TimeSupport.testWriteNumberValueToTime(field.getType(), bb);
                                    if (bb.getValue() == null)
                                        ite.remove();
                                }
                            }
                        }
                    }
                }else if (p == Op.IN
                        || p == Op.NOT_IN) {

                    List valueList = (List) bb.getValue();
                    if (valueList.size() > 1)
                        continue;

                    if (key.contains(".")){
                        if (BaseTypeFilter.isBaseType(key,valueList.get(0),mappable)){
                            ite.remove();
                        }
                    }else{
                        Parsed parsed = mappable.getParsed();
                        if (parsed != null && BaseTypeFilter.isBaseType(key, valueList.get(0), parsed)) {
                                ite.remove();
                        }
                    }
                }
                List<Bb> subList = bb.getSubList();
                if (subList == null || subList.isEmpty())
                    continue;
                filter(subList, mappable);
            }
        }

    }

    interface Pre extends ValueCollector{
        default void pre(List<Object> valueList, List<Bb> bbList,Mappable mappable) {
            for (Bb bb : bbList) {
                Op p = bb.getP();
                if (p == Op.LIMIT || p == Op.OFFSET){
                    continue;
                }else if (p == Op.SUB){
                    pre(valueList, bb.getSubList(),mappable);
                    continue;
                }else if (p == Op.X) {
                    Object value = bb.getValue();
                    if (value == null)
                        continue;
                    if (value instanceof Object[] arr) {
                        for (Object v : arr){
                            add(valueList,v);
                        }
                    }else if (value instanceof List arr) { //deserialized from json
                        for (Object v : arr){
                            add(valueList,v);
                        }
                    }
                }else if (p == Op.EQ) {
                    Object value = bb.getValue();
                    Class clzz = value.getClass();
                    if (clzz == String.class) {
                        Class<?> vType = mapClzz(bb.getKey(), mappable);
                        if (EnumUtil.isEnum(vType)) {
                            value = EnumUtil.serialize((Class<Enum>)vType,value);
                        }
                    }else if (EnumUtil.isEnum(clzz)){
                        value = EnumUtil.serialize((Enum)value);
                    }
                    add(valueList,value);
                } else if (!(p == Op.IN
                        || p == Op.NOT_IN
                        || p == Op.IS_NULL
                        || p == Op.IS_NOT_NULL)) {
                    add(valueList, bb.getValue());
                }
            }
        }

    }

}

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

import top.jiangliuhong.wos.sql.builder.Q;
import top.jiangliuhong.wos.sql.builder.Qr;
import top.jiangliuhong.wos.sql.mapping.Mappable;
import top.jiangliuhong.wos.sql.parser.Parsed;

import java.util.List;



public interface Q2Sql extends CondQToSql, CondQToSql.Filter, CondQToSql.Pre {

    String toCondSql(CondQ condQ, List<Object> valueList, Mappable mappable) ;

    void toSql(boolean isSub, Q q, SqlBuilt sqlBuilt, SqlSubsAndValueBinding subsAndValueBinding) ;

    String toSql(Parsed parsed, Qr Qr, DialectSupport dialectSupport);

}

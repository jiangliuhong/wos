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
package top.jiangliuhong.wos.sql.support;

import top.jiangliuhong.wos.sql.builder.Q;
import top.jiangliuhong.wos.sql.builder.internal.Froms;
import top.jiangliuhong.wos.sql.parser.Parsed;
import top.jiangliuhong.wos.sql.parser.Parser;


public interface XSingleSourceSupport {

    default void supportSingleSource(Q.X xq) {
        if (xq.getSourceScripts().size() == 1 && xq.getParsed() == null) {
            Froms froms = xq.getSourceScripts().get(0);
            String source = froms.getSource();
            if (source != null) {
                Parsed parsed = Parser.get(source);
                xq.setParsed(parsed);
                xq.setClzz(parsed.getClzz());
            }
        }
    }
}

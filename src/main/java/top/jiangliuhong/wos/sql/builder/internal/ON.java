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

import top.jiangliuhong.wos.sql.builder.CondBuilder;

import java.util.ArrayList;
import java.util.List;


public final class ON {

    private List<Bb> bbs = new ArrayList<>();
    private CondBuilder builder;
    private String orUsingKey;

    public List<Bb> getBbs() {
        return bbs;
    }

    public void setBbs(List<Bb> bbs) {
        this.bbs = bbs;
    }

    public CondBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(CondBuilder builder) {
        this.builder = builder;
    }

    public String getOrUsingKey() {
        return orUsingKey;
    }

    public void setOrUsingKey(String orUsingKey) {
        this.orUsingKey = orUsingKey;
    }
}
